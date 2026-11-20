# Script hỗ trợ backend

`database.js` áp dụng `schema.sql` hoặc `seed.sql` vào PostgreSQL qua Pool chung. Chạy `npm run db:schema` rồi `npm run db:seed` từ `backend/`. Xem [cấu hình và cách chạy](../README.md).

Script có tác động DB phải kiểm tra môi trường và mục tiêu kết nối; không tự reset DB tích hợp/public. Không đưa secret thật vào file, tham số được ghi log hoặc ví dụ lệnh. Ghi rõ chủ sở hữu, đầu vào, tác động và cách xác minh trong phần hướng dẫn của từng script.
