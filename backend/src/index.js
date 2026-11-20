const { jwtSecret } = require('./config/env');
const { pool } = require('./db');
const app = require('./app');

jwtSecret();
const port = Number(process.env.PORT ?? 3000);
if (!Number.isInteger(port) || port < 1 || port > 65535) {
  throw new Error('PORT phải nằm trong khoảng 1 đến 65535.');
}

const server = app.listen(port, '0.0.0.0', () => {
  console.log(`Backend thư viện đang nghe trên cổng ${port}`);
});
function stop() {
  server.close(async () => {
    await pool.end();
  });
}
process.once('SIGINT', stop);
process.once('SIGTERM', stop);
