# Bàn giao Người 1 · Nguyễn Đức Quang

- Repository nhóm `VOVANQUOCBAO/TeamFifai-LMS`
- Nhánh bàn giao `member1/backend`
- Tài khoản GitHub `Nguyen-DucQuang`
- Commit mã nguồn `7d9db36f70de2a380e110b9e83a615fcc3f6ebb3`
- Tag hợp đồng `contract-v1.0.0` tại commit `afd7c3f092307e3ca856ac1822abbf0d5683ee38`
- Mốc tuần 11, đề 21 · Kiểm thử cục bộ ngày 20/11/2026

## Phần đã hoàn thành

- Node.js 20, Express 4, PostgreSQL qua `pg`, xác thực JWT 24 giờ và mật khẩu bcrypt.
- Bốn API nghiệp vụ thật gồm đăng nhập, danh sách sách, chi tiết sách và danh sách bạn đọc. `GET /health` kiểm tra PostgreSQL.
- Migration khởi đầu có 10 bảng, khóa ngoại và unique index chống hai phiếu đang mở cho cùng bản. Seed demo gồm 20 nhan đề, 40 bản, 10 bạn đọc và 12 tài khoản. `BD0001` và `BS0001` sẵn sàng cho luồng tích hợp.
- Router mượn đã mount dưới `/api/v1`. Sau xác thực và phân quyền, `POST /phieu-muon` và `GET /ban-doc/toi/dang-muon` trả 501 theo hợp đồng.

## Cách chạy

Clone repository, checkout `member1/backend` rồi chạy trong `backend/`:

```powershell
npm ci
Copy-Item .env.example .env
# Đặt DATABASE_URL, DATABASE_SSL, DATABASE_CA_CERT nếu cần và JWT_SECRET trong .env.
npm run db:schema
npm run db:seed
npm start
```

Đặt `TEST_DATABASE_URL` trỏ tới PostgreSQL thử nghiệm đã seed, sau đó chạy `npm test` trong `backend/`. Từ gốc repo, chạy `python contracts/verify_contracts.py` để kiểm checksum. Xem [cấu hình Supabase/Render](../backend/README.md) và [cách gọi API](../backend/docs/api.md).

| Vai trò | Email demo | Mật khẩu demo |
|---|---|---|
| ADMIN | `admin@thuviendemo.test` | `DemoLib!4211` |
| THU_THU | `thuthu@thuviendemo.test` | `DemoLib!4211` |
| BAN_DOC | `bandoc@thuviendemo.test` | `DemoLib!4211` |

## Kết quả kiểm thử

| Hạng mục | Kết quả |
|---|---|
| Checksum hợp đồng v1.0.0 | Đạt |
| Clone sạch và `npm ci` với Node.js 20.20.2 | Đạt |
| Dựng schema và seed trên DB PostgreSQL 18.7 trống, sau đó chạy lại | Đạt |
| `npm test` qua HTTP thật và PostgreSQL thật | 8/8 nhóm ca đạt |
| JWT thiếu/sai/hết hạn, tài khoản khóa, quyền các role | Đạt |
| Trạng thái demo, dư nợ, số bản rảnh, phân trang, 501 và chặn phiếu mở trùng bản | Đạt |
| Supabase của nhóm, HTTPS Render và kiểm thử ngoài máy | Chưa chạy. Đang chờ thông tin dự án và quyền truy cập của nhóm. |

## Việc còn lại

- URL HTTPS Render và API base URL chờ triển khai cùng dự án Supabase của nhóm.
- Người 2 thay nội dung `backend/src/modules/loans/index.js` bằng hai API thật. Dùng middleware hiện có và một client từ `pool.connect()` cho toàn bộ transaction.
- APK do Người 3 và Người 4 bàn giao riêng.
- Lỗi đã biết trong phạm vi kiểm thử cục bộ: chưa ghi nhận. Hai phản hồi 501 là hành vi dự kiến ở mốc bàn giao nền tảng.
