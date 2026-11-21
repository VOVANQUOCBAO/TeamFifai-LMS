const { Pool } = require('pg');
const { databaseConfig } = require('./config/env');

const pool = new Pool({
  ...databaseConfig(),
  max: 5,
  connectionTimeoutMillis: 5000,
  statement_timeout: 10000,
});
pool.on('error', (error) => {
  console.error('Lỗi kết nối PostgreSQL nhàn rỗi', error.code || error.name);
});

module.exports = { pool };
