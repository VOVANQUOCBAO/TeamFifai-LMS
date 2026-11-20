# Backend thư viện · Tuần 11

Phần nền tảng do Nguyễn Đức Quang phụ trách. Backend dùng Node.js 20, Express 4, PostgreSQL, `pg`, bcrypt và JWT. [OpenAPI v1.0.0](../contracts/openapi.yaml) và [hợp đồng DB](../contracts/database.md) là nguồn chuẩn cho mốc tuần 11. [Tài liệu gọi API](docs/api.md) có quyền và lệnh kiểm tra.

## 1. Cài đặt và cấu hình

Từ thư mục `backend/`, dùng Node.js 20 và PostgreSQL 15 trở lên. Tạo một database demo trống rồi chạy:

```powershell
npm ci
Copy-Item .env.example .env
node -e "console.log(require('node:crypto').randomBytes(32).toString('hex'))"
```

Đặt chuỗi kết nối demo trong `DATABASE_URL` và đầu ra lệnh sinh khóa trong `JWT_SECRET` của `.env`. `PORT` mặc định là `3000`. `DATABASE_SSL` mặc định là `true`. Đặt `false` cho PostgreSQL thử nghiệm cục bộ chạy không TLS. Khi máy chủ dùng CA riêng, `DATABASE_CA_CERT` nhận chứng chỉ CA PEM, với xuống dòng thật hoặc `\n`.

## 2. Dựng dữ liệu và chạy

```powershell
npm run db:schema
npm run db:seed
npm start
```

`schema.sql` là migration khởi đầu, dựng 10 bảng, khóa ngoại, ràng buộc và các index. `seed.sql` thêm dữ liệu tuần 11 cho DB demo. Hai script dùng giao dịch riêng. Chạy lại giữ dữ liệu đã có theo ID, kể cả trạng thái lưu thông và thời hạn đã seed trước đó. Để dựng một bộ demo có thời hạn mới, dùng database thử nghiệm trống. `npm start` chỉ chạy máy chủ HTTP.

Gọi `http://localhost:3000/health` để kiểm tra kết nối PostgreSQL. Hai app dùng base URL `http://localhost:3000/api/v1/` khi truy cập được máy này, hoặc base URL HTTPS của Render khi chạy live.

## 3. Kiểm thử

Dùng riêng database PostgreSQL thử nghiệm đã dựng schema và seed ở bước 2. Suite có thử chèn phiếu đang mở trùng trong một transaction được rollback.

```powershell
$env:TEST_DATABASE_URL = 'postgresql://postgres:local-password@127.0.0.1:5432/thu_vien_demo'
npm test
```

Dùng `TEST_DATABASE_SSL=true` nếu DB kiểm thử yêu cầu TLS. Kiểm tra bộ hợp đồng từ thư mục gốc bằng `python contracts/verify_contracts.py`.

## 4. Supabase và Render

1. Trong Supabase, mở **Connect → Session pooler**, lấy chuỗi kết nối cổng `5432`. Mã hóa percent cho ký tự đặc biệt trong mật khẩu nếu tự điền URI. Dùng PostgreSQL owner của dự án demo để chạy `npm run db:schema` rồi `npm run db:seed` với `DATABASE_SSL=true` và CA phù hợp. Có thể áp dụng lần lượt hai tệp SQL trong Supabase SQL Editor. Các bảng bật Row Level Security và backend truy cập qua kết nối `pg`.
2. Trong Render, tạo **Web Service** từ repository nhóm. Chọn nhánh `member1/backend`, **Root Directory** `backend`, **Build Command** `npm ci`, **Start Command** `npm start`, **Health Check Path** `/health`. Dùng **Node** và đặt `NODE_VERSION=20.20.2` theo stack của môn. Render cấp `PORT`.
3. Đặt `NODE_ENV=production`, `DATABASE_URL`, `DATABASE_SSL=true`, `DATABASE_CA_CERT` nếu cần và khóa `JWT_SECRET` riêng trong Render Environment. Tuần 11 dùng PostgreSQL trực tiếp qua `pg`. Các biến Supabase Storage sẽ được cấu hình khi tích hợp ảnh bìa.
4. Sau khi Render triển khai, kiểm tra `GET https://<dịch-vụ>.onrender.com/health`, thử đăng nhập và gọi ba API đọc dữ liệu. Ghi URL HTTPS và SHA đã triển khai theo [bàn giao](../handoff/member1.md). Base URL của app có hậu tố `/api/v1/`.

[Chuỗi kết nối và CA của Supabase](https://supabase.com/docs/guides/database/connecting-to-postgres) · [Triển khai Express trên Render](https://render.com/docs/deploy-node-express-app) · [Chọn phiên bản Node trên Render](https://render.com/docs/node-version)

## Dữ liệu demo

| Email | Vai trò |
|---|---|
| `admin@thuviendemo.test` | `ADMIN` |
| `thuthu@thuviendemo.test` | `THU_THU` |
| `bandoc@thuviendemo.test` | `BAN_DOC` |

Ba tài khoản trong [hợp đồng demo](../contracts/demo-ids.json) dùng mật khẩu `DemoLib!4211` trên DB thử nghiệm. Seed gồm 20 nhan đề, 40 bản, 10 bạn đọc và 12 tài khoản. `BD0001` đủ điều kiện mượn `BS0001`. `BD0002` đang mượn `BS0002`. `BD0003` hết hạn, `BD0004` có thẻ khóa, `BD0005` còn nợ 12.000 VNĐ và `BD0006` đang mượn 2/3 bản. Tài khoản `bandoc10@thuviendemo.test` để kiểm tra khóa đăng nhập.

## Bàn giao module mượn

`src/db.js` xuất `{ pool }`. `src/middleware/auth.js` xuất `{ xacThuc, choPhep }`, trong đó `xacThuc` đặt `req.nguoiDung = { id, vaiTro }`. Người 2 thay router tại `src/modules/loans/index.js`, đã mount dưới `/api/v1`. Thêm `express.json()` trên route ghi sau middleware xác thực/phân quyền khi xử lý body. Dùng một client lấy từ `pool.connect()` cho toàn bộ transaction, khóa `ban_doc` rồi `ban_sach` theo hợp đồng DB và `release()` ở `finally`.
