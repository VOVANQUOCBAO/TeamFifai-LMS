const express = require('express');
const { pool } = require('./db');
const { ApiError, asyncHandler, errorHandler } = require('./shared/http');

const app = express();
app.disable('x-powered-by');
app.set('query parser', 'simple');

app.get('/health', asyncHandler(async (req, res) => {
  await pool.query('SELECT 1');
  res.json({ data: { status: 'ok', database: 'ok' } });
}));

app.use('/api/v1/auth', express.json(), require('./modules/auth/routes'));
app.use('/api/v1/sach', require('./modules/catalog/routes'));
app.use('/api/v1/ban-doc', require('./modules/readers/routes'));
app.use('/api/v1', require('./modules/loans'));
app.use((req, res, next) => next(new ApiError(404, 'KHONG_TIM_THAY', 'Không tìm thấy tài nguyên.')));
app.use(errorHandler);

module.exports = app;
