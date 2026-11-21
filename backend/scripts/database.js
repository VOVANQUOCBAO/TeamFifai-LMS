const { readFile } = require('node:fs/promises');
const path = require('node:path');
const { pool } = require('../src/db');

async function run() {
  try {
    const command = process.argv[2];
    if (!['schema', 'seed'].includes(command)) throw new Error('Cần chọn schema hoặc seed.');
    const sql = await readFile(path.join(__dirname, '..', `${command}.sql`), 'utf8');
    const client = await pool.connect();
    try {
      await client.query(sql);
    } catch (error) {
      await client.query('ROLLBACK').catch(() => {});
      throw error;
    } finally {
      client.release();
    }
    console.log(`PostgreSQL · ${command} thành công`);
  } finally {
    await pool.end();
  }
}

run().catch((error) => {
  console.error('Thao tác PostgreSQL thất bại', error.code || error.name);
  process.exitCode = 1;
});
