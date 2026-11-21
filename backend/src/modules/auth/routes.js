const { Router } = require('express');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const { pool } = require('../../db');
const { jwtSecret } = require('../../config/env');
const { ApiError, invalidData, lockedAccount, asyncHandler } = require('../../shared/http');

const router = Router();
const emailPattern = /^[^\s@\u0000-\u001f]+@[^\s@\u0000-\u001f]+\.[^\s@\u0000-\u001f]+$/;
// A fixed bcrypt hash keeps password checking comparable for unknown emails.
const dummyHash = '$2b$10$0iUePBr36nyXxClk43mvdefNM1UQG9Uo9iNLKSAp7HCVrdpLUZIrO';

router.post('/dang-nhap', asyncHandler(async (req, res) => {
  const body = req.body;
  if (!body || Array.isArray(body) || Object.keys(body).length !== 2
      || typeof body.email !== 'string' || typeof body.matKhau !== 'string' || !body.matKhau) {
    throw invalidData();
  }
  const email = body.email.trim().toLowerCase();
  if (!emailPattern.test(email)) throw invalidData();

  const { rows: [user] } = await pool.query(
    'SELECT id, ho_ten, vai_tro, trang_thai, mat_khau_hash FROM nguoi_dung WHERE email = $1',
    [email],
  );
  const validPassword = await bcrypt.compare(body.matKhau, user?.mat_khau_hash || dummyHash);
  if (!user || !validPassword || Buffer.byteLength(body.matKhau, 'utf8') > 72) {
    throw new ApiError(401, 'SAI_THONG_TIN_DANG_NHAP', 'Email hoặc mật khẩu không đúng.');
  }
  if (user.trang_thai === 'KHOA') throw lockedAccount();
  const nguoiDung = { id: user.id, hoTen: user.ho_ten, vaiTro: user.vai_tro };
  const token = jwt.sign({ vaiTro: nguoiDung.vaiTro }, jwtSecret(), {
    algorithm: 'HS256', subject: nguoiDung.id, expiresIn: '24h',
  });
  res.json({ data: { token, nguoiDung } });
}));

module.exports = router;
