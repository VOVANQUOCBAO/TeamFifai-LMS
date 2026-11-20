const { Router } = require('express');
const { pool } = require('../../db');
const { xacThuc, choPhep } = require('../../middleware/auth');
const { asyncHandler } = require('../../shared/http');
const { listOptions } = require('../../shared/validation');

const router = Router();

router.get('/', xacThuc, choPhep('ADMIN', 'THU_THU'), asyncHandler(async (req, res) => {
  const { page, pageSize, offset, keyword } = listOptions(req.query);
  const { rows: [result] } = await pool.query(`
    WITH filtered AS (
      SELECT * FROM ban_doc WHERE $1::text IS NULL
        OR strpos(lower(ma_the), lower($1)) > 0
        OR strpos(lower(ho_ten), lower($1)) > 0
        OR strpos(lower(lop_khoa), lower($1)) > 0
    ), paged AS (
      SELECT * FROM filtered ORDER BY ma_the LIMIT $2 OFFSET $3
    ), readers AS (
      SELECT b.id, b.nguoi_dung_id AS "nguoiDungId", b.ma_the AS "maThe",
             b.ho_ten AS "hoTen", b.lop_khoa AS "lopKhoa", to_char(b.han_the, 'YYYY-MM-DD') AS "hanThe",
             b.trang_thai AS "trangThai", b.gioi_han_muon AS "gioiHanMuon",
             (SELECT count(*) FROM phieu_muon p WHERE p.ban_doc_id = b.id
               AND p.trang_thai = 'DANG_MUON' AND p.tra_luc IS NULL) AS "soDangMuon",
             COALESCE((SELECT sum(f.so_tien) FROM phat f JOIN phieu_muon p ON p.id = f.phieu_muon_id
               WHERE p.ban_doc_id = b.id AND f.da_thu = false), 0) AS "tienPhatConNo"
      FROM paged b
    )
    SELECT COALESCE(json_agg(readers ORDER BY readers."maThe"), '[]') AS data,
           (SELECT count(*) FROM filtered) AS total FROM readers
  `, [keyword, pageSize, offset]);
  res.json({ data: result.data, meta: { page, pageSize, total: Number(result.total) } });
}));

module.exports = router;
