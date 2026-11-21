const assert = require('node:assert/strict');
const { once } = require('node:events');
const { randomBytes } = require('node:crypto');
const { before, after, test } = require('node:test');
const jwt = require('jsonwebtoken');
const { ids, demoAccounts } = require('../../../contracts/demo-ids.json');
const bookDetail = require('../../../contracts/fixtures/book-detail.json');
const health = require('../../../contracts/fixtures/health.json');

assert.ok(process.env.TEST_DATABASE_URL, 'Cần đặt TEST_DATABASE_URL cho PostgreSQL demo đã seed.');
process.env.DATABASE_URL = process.env.TEST_DATABASE_URL;
process.env.DATABASE_SSL = process.env.TEST_DATABASE_SSL ?? 'false';
process.env.JWT_SECRET = randomBytes(32).toString('hex');
const app = require('../../src/app');
const { pool } = require('../../src/db');

let server;
let baseUrl;
const tokens = Object.fromEntries(demoAccounts.map(({ vaiTro, userId }) => [
  vaiTro,
  jwt.sign({ vaiTro }, process.env.JWT_SECRET, { subject: userId, expiresIn: '1h' }),
]));
const api = '/api/v1';

before(async () => {
  server = app.listen(0, '127.0.0.1');
  await once(server, 'listening');
  baseUrl = `http://127.0.0.1:${server.address().port}`;
});

after(async () => {
  try {
    if (server) await new Promise((resolve, reject) => server.close((error) => {
      if (error) reject(error);
      else resolve();
    }));
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
  assert.match(response.headers.get('content-type'), /^application\/json\b/);
  return { status: response.status, body: await response.json() };
}

function login(body) {
  return request(api + '/auth/dang-nhap', { method: 'POST', body });
}

async function expectError(response, status, code) {
  const actual = await response;
  assert.equal(actual.status, status);
  assert.deepEqual(Object.keys(actual.body), ['error']);
  assert.deepEqual(Object.keys(actual.body.error).sort(), ['code', 'message']);
  assert.equal(actual.body.error.code, code);
  assert.equal(typeof actual.body.error.message, 'string');
}

test('health checks a real PostgreSQL connection', async () => {
  assert.deepEqual(await request('/health'), { status: 200, body: health });
});

test('login returns the contract fields and a 24-hour JWT for every demo role', async () => {
  for (const { email, matKhau, vaiTro, userId } of demoAccounts) {
    const actual = await login({ email: ' ' + email.toUpperCase() + ' ', matKhau });
    assert.equal(actual.status, 200);
    assert.deepEqual(Object.keys(actual.body), ['data']);
    const { token, nguoiDung } = actual.body.data;
    assert.deepEqual(Object.keys(actual.body.data).sort(), ['nguoiDung', 'token']);
    assert.deepEqual(Object.keys(nguoiDung).sort(), ['hoTen', 'id', 'vaiTro']);
    assert.equal(nguoiDung.id, userId);
    assert.equal(nguoiDung.vaiTro, vaiTro);
    const { sub, exp, iat } = jwt.verify(token, process.env.JWT_SECRET);
    assert.equal(sub, userId);
    assert.equal(exp - iat, 24 * 60 * 60);
  }
});

test('login handles malformed input, wrong credentials and locked accounts', async () => {
  await expectError(login('{'), 400, 'DU_LIEU_KHONG_HOP_LE');
  await expectError(login({ email: 'invalid', matKhau: 'x' }), 400, 'DU_LIEU_KHONG_HOP_LE');
  await expectError(login({ email: demoAccounts[0].email, matKhau: 'wrong' }), 401,
    'SAI_THONG_TIN_DANG_NHAP');
  await expectError(login({ email: 'unknown@thuviendemo.test', matKhau: 'wrong' }), 401,
    'SAI_THONG_TIN_DANG_NHAP');
  await expectError(login({ email: 'bandoc10@thuviendemo.test', matKhau: demoAccounts[0].matKhau }),
    403, 'TAI_KHOAN_BI_KHOA');
});

test('restricted routes check signature, expiration, account state and role', async () => {
  const books = api + '/sach';
  await expectError(request(books), 401, 'TOKEN_KHONG_HOP_LE');
  await expectError(request(books, { token: 'MOCK_TOKEN_ADMIN' }), 401, 'TOKEN_KHONG_HOP_LE');
  const expired = jwt.sign({ vaiTro: 'ADMIN' }, process.env.JWT_SECRET, {
    subject: ids.adminUserId, expiresIn: -1,
  });
  await expectError(request(books, { token: expired }), 401, 'TOKEN_KHONG_HOP_LE');
  const locked = jwt.sign({ vaiTro: 'BAN_DOC' }, process.env.JWT_SECRET, {
    subject: '10000000-0000-4000-8000-000000000012', expiresIn: '1h',
  });
  await expectError(request(books, { token: locked }), 403, 'TAI_KHOAN_BI_KHOA');
  await expectError(request(api + '/ban-doc', { token: tokens.BAN_DOC }), 403, 'KHONG_CO_QUYEN');
});

test('catalog paginates, filters, preserves nulls and counts only available good copies', async () => {
  const auth = { token: tokens.BAN_DOC };
  const books = api + '/sach';
  const list = await request(books, auth);
  assert.equal(list.status, 200);
  assert.equal(list.body.data.length, 20);
  assert.deepEqual(list.body.meta, { page: 1, pageSize: 20, total: 20 });
  const { banSach, ...book } = bookDetail.data;
  const params = new URLSearchParams({ tuKhoa: 'Tô Hoài', phanLoai: ids.categoryId, pageSize: '1' });
  assert.deepEqual(await request(`${books}?${params}`, auth), {
    status: 200, body: { data: [book], meta: { page: 1, pageSize: 1, total: 1 } },
  });
  params.set('page', '2');
  assert.deepEqual((await request(`${books}?${params}`, auth)).body, {
    data: [], meta: { page: 2, pageSize: 1, total: 1 },
  });
  assert.deepEqual(await request(books + '/' + ids.bookId, auth), { status: 200, body: bookDetail });
  assert.equal(list.body.data.at(-1).soBanRanh, 1);
  await expectError(request(books + '?pageSize=101', auth), 400, 'DU_LIEU_KHONG_HOP_LE');
  await expectError(request(books + '?page=1&page=2', auth), 400, 'DU_LIEU_KHONG_HOP_LE');
  await expectError(request(books + '?phanLoai=abc', auth), 400, 'DU_LIEU_KHONG_HOP_LE');
  await expectError(request(books + '/abc', auth), 400, 'DU_LIEU_KHONG_HOP_LE');
  await expectError(request(books + '/ffffffff-ffff-ffff-ffff-ffffffffffff', auth), 404,
    'KHONG_TIM_THAY');
});

test('staff sees seeded reader states, open loans and unpaid fines', async () => {
  const path = api + '/ban-doc';
  const { status, body } = await request(path, { token: tokens.THU_THU });
  assert.equal(status, 200);
  assert.deepEqual(body.meta, { page: 1, pageSize: 20, total: 10 });
  const [first, second, expired, locked, owes, nearLimit] = body.data;
  assert.equal(first.id, ids.readerId);
  assert.equal(first.nguoiDungId, ids.readerUserId);
  assert.deepEqual(Object.keys(first).sort(), [
    'gioiHanMuon', 'hanThe', 'hoTen', 'id', 'lopKhoa', 'maThe', 'nguoiDungId',
    'soDangMuon', 'tienPhatConNo', 'trangThai',
  ].sort());
  assert.equal(first.soDangMuon, 0);
  assert.equal(first.tienPhatConNo, 0);
  const today = new Intl.DateTimeFormat('sv-SE', { timeZone: 'Asia/Ho_Chi_Minh' }).format();
  assert.ok(first.hanThe >= today);
  assert.ok(expired.hanThe < today);
  assert.equal(locked.trangThai, 'KHOA');
  assert.equal(second.soDangMuon, 1);
  assert.equal(owes.tienPhatConNo, 12000);
  assert.equal(nearLimit.soDangMuon, nearLimit.gioiHanMuon - 1);
  const filtered = await request(path + '?tuKhoa=BD0001&pageSize=1', { token: tokens.ADMIN });
  assert.deepEqual(filtered.body, { data: [first], meta: { page: 1, pageSize: 1, total: 1 } });
});

test('loans routes require the expected roles (business cases in loans.test.js)', async () => {
  const create = api + '/phieu-muon';
  const myLoans = api + '/ban-doc/toi/dang-muon';
  const body = { banDocId: ids.readerId, banSachId: ids.availableCopyId };
  await expectError(request(create, { token: tokens.BAN_DOC, method: 'POST', body }),
    403, 'KHONG_CO_QUYEN');
  await expectError(request(myLoans, { token: tokens.ADMIN }), 403, 'KHONG_CO_QUYEN');
  await expectError(request(myLoans), 401, 'TOKEN_KHONG_HOP_LE');
});

test('database has demo counts and bcrypt hashes, and rejects duplicate open loans', async () => {
  const { rows: [counts] } = await pool.query(`
    SELECT (SELECT count(*)::int FROM ban_sach) AS copies,
           (SELECT count(*)::int FROM nguoi_dung WHERE mat_khau_hash ~ '^\\$2[aby]\\$') AS hashes,
           (SELECT count(*)::int FROM phieu_muon WHERE id = $1 AND ban_doc_id = $2
             AND ban_sach_id = $3) AS existing_loan
  `, [ids.existingLoanId, ids.secondReaderId, ids.borrowedCopyId]);
  assert.deepEqual(counts, { copies: 40, hashes: 12, existing_loan: 1 });
  const client = await pool.connect();
  try {
    await client.query('BEGIN');
    await assert.rejects(client.query(`
      INSERT INTO phieu_muon (ban_doc_id, ban_sach_id, muon_luc, han_tra, trang_thai)
      VALUES ($1, $2, now(), now() + INTERVAL '14 days', 'DANG_MUON')
    `, [ids.readerId, ids.borrowedCopyId]), { code: '23505', constraint: 'phieu_muon_mo_ban_sach_uk' });
  } finally {
    try {
      await client.query('ROLLBACK');
    } finally {
      client.release();
    }
  }
});
