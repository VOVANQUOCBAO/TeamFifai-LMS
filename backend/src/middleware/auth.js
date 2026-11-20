const jwt = require('jsonwebtoken');
const { jwtSecret } = require('../config/env');
const { pool } = require('../db');
const { ApiError, lockedAccount, asyncHandler } = require('../shared/http');
const { uuidPattern } = require('../shared/validation');

const tokenError = () => new ApiError(
  401, 'TOKEN_KHONG_HOP_LE', 'Phiên đăng nhập không hợp lệ hoặc đã hết hạn.',
);

const xacThuc = asyncHandler(async (req, res, next) => {
  const match = /^Bearer ([^\s]+)$/i.exec(req.get('Authorization') || '');
  if (!match) throw tokenError();
  const secret = jwtSecret();
  let claims;
  try {
    claims = jwt.verify(match[1], secret, { algorithms: ['HS256'] });
  } catch (error) {
    if (error instanceof jwt.JsonWebTokenError) throw tokenError();
    throw error;
  }
  if (typeof claims?.sub !== 'string' || !uuidPattern.test(claims.sub)
      || !Number.isInteger(claims.exp)) throw tokenError();

  const { rows: [user] } = await pool.query(
    'SELECT id, vai_tro, trang_thai FROM nguoi_dung WHERE id = $1', [claims.sub],
  );
  if (!user) throw tokenError();
  if (user.trang_thai === 'KHOA') throw lockedAccount();
  if (user.vai_tro !== claims.vaiTro) throw tokenError();
  req.nguoiDung = { id: user.id, vaiTro: user.vai_tro };
  next();
});

function choPhep(...vaiTro) {
  return (req, res, next) => {
    if (!req.nguoiDung) return next(tokenError());
    if (!vaiTro.includes(req.nguoiDung.vaiTro)) {
      return next(new ApiError(403, 'KHONG_CO_QUYEN', 'Bạn không có quyền thực hiện thao tác này.'));
    }
    return next();
  };
}

module.exports = { xacThuc, choPhep };
