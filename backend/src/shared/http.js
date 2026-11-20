class ApiError extends Error {
  constructor(status, code, message) {
    super(message);
    this.status = status;
    this.code = code;
  }
}

const invalidData = () => new ApiError(
  400, 'DU_LIEU_KHONG_HOP_LE', 'Vui lòng kiểm tra dữ liệu đã nhập.',
);
const lockedAccount = () => new ApiError(403, 'TAI_KHOAN_BI_KHOA', 'Tài khoản đang bị khóa.');

function asyncHandler(handler) {
  return (req, res, next) => Promise.resolve(handler(req, res, next)).catch(next);
}

function errorHandler(error, req, res, next) {
  if (res.headersSent) return next(error);
  if (error instanceof ApiError) {
    return res.status(error.status).json({ error: { code: error.code, message: error.message } });
  }
  if (error.status >= 400 && error.status < 500) {
    return errorHandler(invalidData(), req, res, next);
  }
  console.error('Lỗi xử lý yêu cầu', req.method, error.code || error.name);
  return res.status(500).json({
    error: { code: 'LOI_HE_THONG', message: 'Máy chủ gặp lỗi. Vui lòng thử lại.' },
  });
}

module.exports = { ApiError, invalidData, lockedAccount, asyncHandler, errorHandler };
