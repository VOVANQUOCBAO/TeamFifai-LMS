const { invalidData } = require('./http');

const uuidPattern = /^[a-f0-9]{8}(?:-[a-f0-9]{4}){3}-[a-f0-9]{12}$/i;

function positiveInteger(value, fallback, maximum = Number.MAX_SAFE_INTEGER) {
  if (value === undefined) return fallback;
  if (typeof value !== 'string' || !/^\d+$/.test(value)) throw invalidData();
  const parsed = Number(value);
  if (parsed < 1 || parsed > maximum) throw invalidData();
  return parsed;
}

function listOptions(query) {
  const page = positiveInteger(query.page, 1);
  const pageSize = positiveInteger(query.pageSize, 20, 100);
  const keyword = query.tuKhoa;
  if (keyword !== undefined && (typeof keyword !== 'string' || keyword.includes('\0'))) {
    throw invalidData();
  }
  return {
    page, pageSize,
    offset: ((BigInt(page) - 1n) * BigInt(pageSize)).toString(),
    keyword: keyword?.trim() || null,
  };
}

module.exports = { uuidPattern, listOptions };
