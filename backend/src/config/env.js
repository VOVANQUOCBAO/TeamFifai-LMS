require('dotenv').config();

function databaseConfig() {
  const connectionString = process.env.DATABASE_URL;
  if (!connectionString) throw new Error('Cần đặt DATABASE_URL.');
  const url = new URL(connectionString);
  if (!['postgres:', 'postgresql:'].includes(url.protocol)) {
    throw new Error('DATABASE_URL phải dùng giao thức PostgreSQL.');
  }
  // The connection URL must not override the TLS settings below.
  for (const key of ['ssl', 'sslmode', 'sslcert', 'sslkey', 'sslrootcert']) {
    url.searchParams.delete(key);
  }
  const tls = process.env.DATABASE_SSL ?? 'true';
  if (!['true', 'false'].includes(tls)) throw new Error('DATABASE_SSL phải là true hoặc false.');
  const ca = process.env.DATABASE_CA_CERT?.replace(/\\n/g, '\n');
  return {
    connectionString: url.toString(),
    ssl: tls === 'true' && { rejectUnauthorized: true, ...(ca && { ca }) },
  };
}

function jwtSecret() {
  const secret = process.env.JWT_SECRET;
  if (!secret || secret.trim().length < 32) {
    throw new Error('JWT_SECRET phải có ít nhất 32 ký tự.');
  }
  return secret;
}

module.exports = { databaseConfig, jwtSecret };
