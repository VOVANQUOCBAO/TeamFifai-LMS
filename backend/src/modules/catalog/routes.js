const { Router } = require('express');
const { pool } = require('../../db');
const { xacThuc, choPhep } = require('../../middleware/auth');
const { ApiError, asyncHandler, invalidData } = require('../../shared/http');
const { listOptions, uuidPattern } = require('../../shared/validation');

const router = Router();
router.use(xacThuc, choPhep('ADMIN', 'THU_THU', 'BAN_DOC'));

const bookFields = `
  s.id, s.nhan_de AS "nhanDe", s.tac_gia AS "tacGia", s.phan_loai_id AS "phanLoaiId",
  s.nxb, s.nam_xb AS "namXb", s.isbn, s.anh_bia AS "anhBia",
  (SELECT count(*) FROM ban_sach b
   WHERE b.sach_id = s.id AND b.tinh_trang = 'TOT' AND b.trang_thai = 'RANH') AS "soBanRanh"
`;

router.get('/', asyncHandler(async (req, res) => {
  const { page, pageSize, offset, keyword } = listOptions(req.query);
  const category = req.query.phanLoai;
  if (category !== undefined && (typeof category !== 'string' || !uuidPattern.test(category))) {
    throw invalidData();
  }
  const { rows: [result] } = await pool.query(`
    WITH filtered AS (
      SELECT * FROM sach
      WHERE ($1::text IS NULL OR strpos(lower(nhan_de), lower($1)) > 0
             OR strpos(lower(tac_gia), lower($1)) > 0)
        AND ($2::uuid IS NULL OR phan_loai_id = $2)
    ), paged AS (
      SELECT id, nhan_de, tac_gia, phan_loai_id, nxb, nam_xb, isbn, anh_bia
      FROM filtered ORDER BY id LIMIT $3 OFFSET $4
    ), books AS (
      SELECT ${bookFields} FROM paged s
    )
    SELECT COALESCE(json_agg(books ORDER BY books.id), '[]') AS data,
           (SELECT count(*) FROM filtered) AS total FROM books
  `, [keyword, category ?? null, pageSize, offset]);
  res.json({ data: result.data, meta: { page, pageSize, total: Number(result.total) } });
}));

router.get('/:id', asyncHandler(async (req, res) => {
  if (!uuidPattern.test(req.params.id)) throw invalidData();
  const { rows: [result] } = await pool.query(`
    SELECT row_to_json(book) AS data FROM (
      SELECT ${bookFields},
        COALESCE((
          SELECT json_agg(copy ORDER BY copy."maCaBiet")
          FROM (
            SELECT id, sach_id AS "sachId", ma_ca_biet AS "maCaBiet", vi_tri_ke AS "viTriKe",
                   tinh_trang AS "tinhTrang", trang_thai AS "trangThai"
            FROM ban_sach WHERE sach_id = s.id
          ) copy
        ), '[]') AS "banSach"
      FROM sach s WHERE s.id = $1
    ) book
  `, [req.params.id]);
  if (!result) throw new ApiError(404, 'KHONG_TIM_THAY', 'Không tìm thấy sách.');
  res.json(result);
}));

module.exports = router;
