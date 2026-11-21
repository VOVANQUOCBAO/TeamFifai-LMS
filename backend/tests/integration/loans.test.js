const assert = require('node:assert/strict');
const { once } = require('node:events');
const { randomBytes } = require('node:crypto');
const { before, after, test } = require('node:test');
const jwt = require('jsonwebtoken');
const { ids, demoAccounts } = require('../../../contracts/demo-ids.json');
const policies = require('../../../contracts/policies.json');
const loanResponseFixture = require('../../../contracts/fixtures/create-loan-response.json');

assert.ok(process.env.TEST_DATABASE_URL, 'Cần đặt TEST_DATABASE_URL cho PostgreSQL demo đã seed.');
process.env.DATABASE_URL = process.env.TEST_DATABASE_URL;
process.env.DATABASE_SSL = process.env.TEST_DATABASE_SSL ?? 'false';
process.env.JWT_SECRET = randomBytes(32).toString('hex');
const app = require('../../src/app');
const { pool } = require('../../src/db');
const { LOAN_DURATION_HOURS } = require('../../src/modules/loans');

const api = '/api/v1';
const readerId = (n) => `20000000-0000-4000-8000-${String(n).padStart(12, '0')}`;
const userId = (n) => `10000000-0000-4000-8000-${String(n).padStart(12, '0')}`;
const copyId = (n) => `50000000-0000-4000-8000-${String(n).padStart(12, '0')}`;
const sign = (sub, vaiTro) => jwt.sign({ vaiTro }, process.env.JWT_SECRET, { subject: sub, expiresIn: '1h' });
const tokens = Object.fromEntries(demoAccounts.map(({ vaiTro, userId: sub }) => [vaiTro, sign(sub, vaiTro)]));
// Bạn đọc n trong seed gắn với nguoi_dung n + 2.
const readerToken = (n) => sign(userId(n + 2), 'BAN_DOC');

let server;
let baseUrl;
const touchedCopies = new Set();

before(async () => {
  server = app.listen(0, '127.0.0.1');
  await once(server, 'listening');
  baseUrl = `http://127.0.0.1:${server.address().port}`;
});

// Trả DB demo về trạng thái seed: xóa phiếu do test tạo và đặt lại bản sách.
async function cleanup() {
  if (touchedCopies.size === 0) return;
  const copies = [...touchedCopies];
  const client = await pool.connect();
  try {
    await client.query('BEGIN');
    await client.query(`
      DELETE FROM nhat_ky_thu_vien WHERE phieu_muon_id IN (
        SELECT id FROM phieu_muon WHERE ban_sach_id = ANY($1::uuid[]) AND id::text NOT LIKE '60000000-%')
    `, [copies]);
    await client.query(`
      DELETE FROM phieu_muon WHERE ban_sach_id = ANY($1::uuid[]) AND id::text NOT LIKE '60000000-%'
    `, [copies]);
    await client.query(`
      UPDATE ban_sach SET trang_thai = 'RANH' WHERE id = ANY($1::uuid[])
        AND NOT EXISTS (SELECT 1 FROM phieu_muon p WHERE p.ban_sach_id = ban_sach.id
                        AND p.trang_thai = 'DANG_MUON' AND p.tra_luc IS NULL)
    `, [copies]);
    await client.query('COMMIT');
  } catch (error) {
    await client.query('ROLLBACK');
    throw error;
  } finally {
    client.release();
  }
  touchedCopies.clear();
}

after(async () => {
  try {
    await cleanup();
    if (server) await new Promise((resolve, reject) => server.close((e) => (e ? reject(e) : resolve())));
  } finally {
    await pool.end();
  }
});

async function request(path, { token, body, method = 'GET' } = {}) {
  const response = await fetch(baseUrl + path, {
    method,
    headers: {
      ...(token && { Authorization: `Bearer ${token}` }),
      ...(body !== undefined && { 'Content-Type': 'application/json' }),
    },
    body: typeof body === 'string' ? body : JSON.stringify(body),
  });
  return { status: response.status, body: await response.json() };
}

function borrow(banDocId, banSachId, token = tokens.THU_THU) {
  touchedCopies.add(banSachId);
  return request(api + '/phieu-muon', { method: 'POST', token, body: { banDocId, banSachId } });
}

async function expectError(response, status, code) {
  const actual = await response;
  assert.equal(actual.status, status, JSON.stringify(actual.body));
  assert.equal(actual.body.error.code, code);
  assert.equal(typeof actual.body.error.message, 'string');
}

async function openLoanCount(banSachId) {
  const { rows: [row] } = await pool.query(`
    SELECT count(*)::int AS n FROM phieu_muon
    WHERE ban_sach_id = $1 AND trang_thai = 'DANG_MUON' AND tra_luc IS NULL
  `, [banSachId]);
  return row.n;
}

async function soBanRanh(sachId) {
  return (await request(`${api}/sach/${sachId}`, { token: tokens.BAN_DOC })).body.data.soBanRanh;
}

test('loan duration matches the shared policy', () => {
  assert.equal(LOAN_DURATION_HOURS, policies.loanDurationHours);
});

test('librarian lends an available copy; reader sees it; available count drops', async () => {
  const before = await soBanRanh(ids.bookId);
  const empty = await request(api + '/ban-doc/toi/dang-muon', { token: tokens.BAN_DOC });
  assert.deepEqual(empty, { status: 200, body: { data: [], meta: { page: 1, pageSize: 20, total: 0 } } });

  const created = await borrow(ids.readerId, ids.availableCopyId);
  assert.equal(created.status, 201, JSON.stringify(created.body));
  const loan = created.body.data;
  assert.deepEqual(Object.keys(loan).sort(), Object.keys(loanResponseFixture.data).sort());
  assert.equal(loan.banDocId, ids.readerId);
  assert.equal(loan.banSachId, ids.availableCopyId);
  assert.equal(loan.nhanDe, loanResponseFixture.data.nhanDe);
  assert.equal(loan.maCaBiet, 'BS0001');
  assert.equal(loan.trangThai, 'DANG_MUON');
  assert.match(loan.muonLuc, /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}Z$/);
  assert.equal(Date.parse(loan.hanTra) - Date.parse(loan.muonLuc), policies.loanDurationHours * 3600e3);
  assert.ok(Math.abs(Date.parse(loan.muonLuc) - Date.now()) < 60e3);

  const { rows: [state] } = await pool.query(`
    SELECT b.trang_thai, (SELECT count(*)::int FROM nhat_ky_thu_vien n
      WHERE n.phieu_muon_id = $1 AND n.loai_su_kien = 'CHO_MUON'
        AND n.nguoi_thuc_hien = $2) AS logs
    FROM ban_sach b WHERE b.id = $3
  `, [loan.id, ids.librarianUserId, ids.availableCopyId]);
  assert.deepEqual(state, { trang_thai: 'DANG_MUON', logs: 1 });
  assert.equal(await soBanRanh(ids.bookId), before - 1);

  const mine = await request(api + '/ban-doc/toi/dang-muon', { token: tokens.BAN_DOC });
  assert.deepEqual(mine, { status: 200, body: { data: [loan], meta: { page: 1, pageSize: 20, total: 1 } } });
  const page2 = await request(api + '/ban-doc/toi/dang-muon?page=2&pageSize=1', { token: tokens.BAN_DOC });
  assert.deepEqual(page2.body, { data: [], meta: { page: 2, pageSize: 1, total: 1 } });

  // Bạn đọc khác không thấy phiếu này.
  const other = await request(api + '/ban-doc/toi/dang-muon', { token: readerToken(2) });
  assert.equal(other.status, 200);
  assert.ok(other.body.data.every((item) => item.banDocId === ids.secondReaderId));
  assert.ok(!other.body.data.some((item) => item.id === loan.id));

  // Bản vừa mượn không thể mượn lại.
  await expectError(borrow(readerId(8), ids.availableCopyId), 409, 'BAN_SACH_KHONG_SAN_SANG');
  await cleanup();
});

test('business rules block expired, locked, indebted and over-limit readers without partial writes', async () => {
  const copy = copyId(11);
  await expectError(borrow(readerId(3), copy), 409, 'THE_HET_HAN');
  await expectError(borrow(readerId(4), copy), 409, 'BAN_DOC_BI_KHOA');
  await expectError(borrow(readerId(10), copy), 409, 'BAN_DOC_BI_KHOA'); // tài khoản bị khóa
  await expectError(borrow(readerId(5), copy), 409, 'CON_NO_PHAT');
  await expectError(borrow(readerId(8), copyId(40)), 409, 'BAN_SACH_KHONG_SAN_SANG'); // bản hỏng
  await expectError(borrow(readerId(8), ids.borrowedCopyId), 409, 'BAN_SACH_KHONG_SAN_SANG');
  assert.equal(await openLoanCount(copy), 0);
  const { rows: [row] } = await pool.query('SELECT trang_thai FROM ban_sach WHERE id = $1', [copy]);
  assert.equal(row.trang_thai, 'RANH');

  // BD0006 còn đúng 1 suất: hai yêu cầu đồng thời chỉ một được chấp nhận.
  const results = await Promise.all([borrow(readerId(6), copyId(13)), borrow(readerId(6), copyId(15))]);
  assert.deepEqual(results.map((r) => r.status).sort(), [201, 409]);
  assert.equal(results.find((r) => r.status === 409).body.error.code, 'VUOT_GIOI_HAN_MUON');
  const { rows: [limit] } = await pool.query(`
    SELECT count(*)::int AS n FROM phieu_muon WHERE ban_doc_id = $1 AND trang_thai = 'DANG_MUON'
  `, [readerId(6)]);
  assert.equal(limit.n, 3);
  await cleanup();
});

test('two concurrent requests for the same copy create exactly one open loan', async () => {
  const copy = copyId(21);
  const results = await Promise.all([borrow(readerId(8), copy), borrow(readerId(9), copy)]);
  assert.deepEqual(results.map((r) => r.status).sort(), [201, 409]);
  assert.equal(results.find((r) => r.status === 409).body.error.code, 'BAN_SACH_KHONG_SAN_SANG');
  assert.equal(await openLoanCount(copy), 1);
  await cleanup();
});

test('input validation, unknown ids, roles and authentication', async () => {
  const path = api + '/phieu-muon';
  const send = (body, token = tokens.THU_THU) => request(path, { method: 'POST', token, body });
  await expectError(send('{'), 400, 'DU_LIEU_KHONG_HOP_LE');
  await expectError(send({ banDocId: ids.readerId }), 400, 'DU_LIEU_KHONG_HOP_LE');
  await expectError(send({ banDocId: 'abc', banSachId: ids.availableCopyId }), 400, 'DU_LIEU_KHONG_HOP_LE');
  await expectError(send({ banDocId: ids.readerId, banSachId: ids.availableCopyId, hanTra: 'x' }),
    400, 'DU_LIEU_KHONG_HOP_LE');
  await expectError(send([ids.readerId]), 400, 'DU_LIEU_KHONG_HOP_LE');
  await expectError(send({ banDocId: 'ffffffff-ffff-4fff-8fff-ffffffffffff', banSachId: ids.availableCopyId }),
    404, 'KHONG_TIM_THAY');
  await expectError(send({ banDocId: ids.readerId, banSachId: 'ffffffff-ffff-4fff-8fff-ffffffffffff' }),
    404, 'KHONG_TIM_THAY');
  await expectError(send({ banDocId: ids.readerId, banSachId: ids.availableCopyId }, tokens.BAN_DOC),
    403, 'KHONG_CO_QUYEN');
  await expectError(send({ banDocId: ids.readerId, banSachId: ids.availableCopyId }, null),
    401, 'TOKEN_KHONG_HOP_LE');
  assert.equal(await openLoanCount(ids.availableCopyId), 0);

  const mine = api + '/ban-doc/toi/dang-muon';
  await expectError(request(mine, { token: tokens.ADMIN }), 403, 'KHONG_CO_QUYEN');
  await expectError(request(mine, { token: tokens.THU_THU }), 403, 'KHONG_CO_QUYEN');
  await expectError(request(mine), 401, 'TOKEN_KHONG_HOP_LE');
  await expectError(request(mine + '?pageSize=101', { token: tokens.BAN_DOC }), 400, 'DU_LIEU_KHONG_HOP_LE');
  await expectError(request(mine + '?banDocId=' + ids.secondReaderId + '&tuKhoa=x', { token: tokens.BAN_DOC }),
    400, 'DU_LIEU_KHONG_HOP_LE');
});

test('no week 11 route answers 501 any more', async () => {
  const mine = await request(api + '/ban-doc/toi/dang-muon', { token: tokens.BAN_DOC });
  assert.notEqual(mine.status, 501);
});
