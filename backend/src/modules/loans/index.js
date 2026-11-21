const express = require('express');
const { pool } = require('../../db');
const { xacThuc, choPhep } = require('../../middleware/auth');
const { ApiError, asyncHandler, invalidData } = require('../../shared/http');
const { listOptions, uuidPattern } = require('../../shared/validation');

// Theo contracts/policies.json v1.0.0 (loanDurationHours). Kiểm tra khớp trong test tích hợp.
const LOAN_DURATION_HOURS = 336;
const OPEN_LOAN_UNIQUE = 'phieu_muon_mo_ban_sach_uk';

const router = express.Router();

const conflict = (code, message) => new ApiError(409, code, message);
const notFound = () => new ApiError(404, 'KHONG_TIM_THAY', 'Không tìm thấy tài nguyên.');
const copyUnavailable = () => conflict(
  'BAN_SACH_KHONG_SAN_SANG', 'Bản sách không ở trạng thái sẵn sàng cho mượn.',
);

// Trả mốc thời gian dạng UTC ISO 8601 không phần nghìn giây, giống fixture hợp đồng.
const isoUtc = (column) => `to_char(${column} AT TIME ZONE 'UTC', 'YYYY-MM-DD"T"HH24:MI:SS"Z"')`;

const loanFields = `
  p.id, p.ban_doc_id AS "banDocId", p.ban_sach_id AS "banSachId",
  s.nhan_de AS "nhanDe", b.ma_ca_biet AS "maCaBiet",
  ${isoUtc('p.muon_luc')} AS "muonLuc", ${isoUtc('p.han_tra')} AS "hanTra",
  p.trang_thai AS "trangThai"
`;

function parseCreateLoan(body) {
  if (!body || typeof body !== 'object' || Array.isArray(body)) throw invalidData();
  const keys = Object.keys(body).sort();
  if (keys.length !== 2 || keys[0] !== 'banDocId' || keys[1] !== 'banSachId') throw invalidData();
  const { banDocId, banSachId } = body;
  for (const value of [banDocId, banSachId]) {
    if (typeof value !== 'string' || !uuidPattern.test(value)) throw invalidData();
  }
  return { banDocId: banDocId.toLowerCase(), banSachId: banSachId.toLowerCase() };
}

async function createLoan(client, { banDocId, banSachId }, staffId) {
  // Khóa theo thứ tự cố định: bạn đọc trước, bản sách sau.
  const { rows: [reader] } = await client.query(`
    SELECT bd.id, bd.trang_thai, bd.gioi_han_muon, u.trang_thai AS tai_khoan,
           bd.han_the >= (now() AT TIME ZONE 'Asia/Ho_Chi_Minh')::date AS the_con_han
    FROM ban_doc bd JOIN nguoi_dung u ON u.id = bd.nguoi_dung_id
    WHERE bd.id = $1
    FOR UPDATE OF bd
  `, [banDocId]);
  if (!reader) throw notFound();

  const { rows: [copy] } = await client.query(`
    SELECT b.id, b.trang_thai, b.tinh_trang
    FROM ban_sach b WHERE b.id = $1
    FOR UPDATE
  `, [banSachId]);
  if (!copy) throw notFound();

  if (reader.trang_thai === 'KHOA' || reader.tai_khoan === 'KHOA') {
    throw conflict('BAN_DOC_BI_KHOA', 'Bạn đọc đang bị khóa.');
  }
  if (!reader.the_con_han) throw conflict('THE_HET_HAN', 'Thẻ bạn đọc đã hết hạn.');

  const { rows: [usage] } = await client.query(`
    SELECT
      (SELECT count(*)::int FROM phieu_muon
        WHERE ban_doc_id = $1 AND trang_thai = 'DANG_MUON' AND tra_luc IS NULL) AS dang_muon,
      (SELECT COALESCE(sum(f.so_tien), 0)::bigint FROM phat f
        JOIN phieu_muon p ON p.id = f.phieu_muon_id
        WHERE p.ban_doc_id = $1 AND f.da_thu = false) AS no_phat
  `, [banDocId]);
  if (Number(usage.no_phat) > 0) throw conflict('CON_NO_PHAT', 'Bạn đọc còn nợ phạt chưa thanh toán.');
  if (usage.dang_muon >= reader.gioi_han_muon) {
    throw conflict('VUOT_GIOI_HAN_MUON', 'Bạn đọc đã đạt giới hạn số cuốn đang mượn.');
  }
  if (copy.trang_thai !== 'RANH' || copy.tinh_trang !== 'TOT') throw copyUnavailable();

  const { rows: [loan] } = await client.query(`
    INSERT INTO phieu_muon (ban_doc_id, ban_sach_id, muon_luc, han_tra, trang_thai)
    VALUES ($1, $2, date_trunc('second', now()),
            date_trunc('second', now()) + $3 * INTERVAL '1 hour', 'DANG_MUON')
    RETURNING id, muon_luc
  `, [banDocId, banSachId, LOAN_DURATION_HOURS]);

  await client.query(`UPDATE ban_sach SET trang_thai = 'DANG_MUON' WHERE id = $1`, [banSachId]);
  await client.query(`
    INSERT INTO nhat_ky_thu_vien
      (loai_su_kien, ban_doc_id, ban_sach_id, phieu_muon_id, thoi_diem, nguoi_thuc_hien)
    VALUES ('CHO_MUON', $1, $2, $3, $4, $5)
  `, [banDocId, banSachId, loan.id, loan.muon_luc, staffId]);

  const { rows: [result] } = await client.query(`
    SELECT ${loanFields}
    FROM phieu_muon p
    JOIN ban_sach b ON b.id = p.ban_sach_id
    JOIN sach s ON s.id = b.sach_id
    WHERE p.id = $1
  `, [loan.id]);
  return result;
}

router.post('/phieu-muon', xacThuc, choPhep('ADMIN', 'THU_THU'), express.json(),
  asyncHandler(async (req, res) => {
    const input = parseCreateLoan(req.body);
    const client = await pool.connect();
    let loan;
    try {
      await client.query('BEGIN');
      loan = await createLoan(client, input, req.nguoiDung.id);
      await client.query('COMMIT');
    } catch (error) {
      await client.query('ROLLBACK').catch(() => {});
      // Hai yêu cầu đồng thời cùng một bản: ràng buộc DB là lưới an toàn cuối cùng.
      if (error.code === '23505' && error.constraint === OPEN_LOAN_UNIQUE) throw copyUnavailable();
      throw error;
    } finally {
      client.release();
    }
    res.status(201).json({ data: loan });
  }));

router.get('/ban-doc/toi/dang-muon', xacThuc, choPhep('BAN_DOC'), asyncHandler(async (req, res) => {
  const { page, pageSize, offset } = listOptions(req.query);
  if (req.query.tuKhoa !== undefined) throw invalidData();
  // Định danh lấy từ JWT, không nhận banDocId từ app.
  const { rows: [reader] } = await pool.query(
    'SELECT id FROM ban_doc WHERE nguoi_dung_id = $1', [req.nguoiDung.id],
  );
  if (!reader) throw notFound();

  const { rows: [result] } = await pool.query(`
    WITH open_loans AS (
      SELECT * FROM phieu_muon
      WHERE ban_doc_id = $1 AND trang_thai = 'DANG_MUON' AND tra_luc IS NULL
    ), paged AS (
      SELECT ${loanFields}, p.muon_luc AS sort_at
      FROM open_loans p
      JOIN ban_sach b ON b.id = p.ban_sach_id
      JOIN sach s ON s.id = b.sach_id
      ORDER BY p.muon_luc DESC, p.id
      LIMIT $2 OFFSET $3
    )
    SELECT COALESCE(json_agg(json_build_object(
             'id', id, 'banDocId', "banDocId", 'banSachId', "banSachId", 'nhanDe', "nhanDe",
             'maCaBiet', "maCaBiet", 'muonLuc', "muonLuc", 'hanTra', "hanTra",
             'trangThai', "trangThai") ORDER BY sort_at DESC, id), '[]') AS data,
           (SELECT count(*) FROM open_loans) AS total
    FROM paged
  `, [reader.id, pageSize, offset]);
  res.json({ data: result.data, meta: { page, pageSize, total: Number(result.total) } });
}));

module.exports = router;
module.exports.LOAN_DURATION_HOURS = LOAN_DURATION_HOURS;
