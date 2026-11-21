# API backend tuần 11

[OpenAPI v1.0.0](../../contracts/openapi.yaml) định nghĩa đầy đủ schema, tham số và phản hồi. URL API của app kết thúc bằng `/api/v1/`. Gửi `Authorization: Bearer <token>` cho route có phân quyền.

| Phương thức | Đường dẫn | Quyền | Phản hồi nền tảng |
|---|---|---|---|
| GET | `/health` | Công khai | 200 khi HTTP và PostgreSQL hoạt động, 500 khi DB lỗi |
| POST | `/api/v1/auth/dang-nhap` | Công khai | 200 trả JWT 24 giờ và thông tin người dùng |
| GET | `/api/v1/sach` | ADMIN, THU_THU, BAN_DOC | 200 trả nhan đề và số bản vừa TOT vừa RANH |
| GET | `/api/v1/sach/{id}` | ADMIN, THU_THU, BAN_DOC | 200 trả nhan đề và các bản sách |
| GET | `/api/v1/ban-doc` | ADMIN, THU_THU | 200 trả thẻ, số phiếu đang mở và dư nợ |
| POST | `/api/v1/phieu-muon` | ADMIN, THU_THU | 501 chờ Người 2 |
| GET | `/api/v1/ban-doc/toi/dang-muon` | BAN_DOC | 501 chờ Người 2 |

Các danh sách dùng `page` và `pageSize`, mặc định 1 và 20, tối đa 100 mục mỗi trang. Sách hỗ trợ `tuKhoa` theo nhan đề/tác giả và `phanLoai` bằng UUID. Bạn đọc hỗ trợ `tuKhoa` theo mã thẻ, họ tên hoặc lớp/khoa. `total` tính trên các kết quả sau lọc.

Phản hồi lỗi dùng `error.code` và `error.message`. Dữ liệu sai trả 400. Token thiếu, sai hoặc hết hạn trả 401. Tài khoản khóa hoặc sai vai trò trả 403. UUID sách hợp lệ mà không có bản ghi trả 404. Hai route chờ tích hợp kiểm quyền trước khi trả 501 `CHUC_NANG_CHUA_TICH_HOP`.

```powershell
$base = 'http://localhost:3000'
$credentials = @{email = 'thuthu@thuviendemo.test'; matKhau = 'DemoLib!4211'} | ConvertTo-Json
$session = Invoke-RestMethod "$base/api/v1/auth/dang-nhap" -Method Post -ContentType 'application/json' -Body $credentials
$headers = @{Authorization = "Bearer $($session.data.token)"}
Invoke-RestMethod "$base/health"
Invoke-RestMethod "$base/api/v1/sach?page=1&pageSize=5" -Headers $headers
Invoke-RestMethod "$base/api/v1/sach/30000000-0000-4000-8000-000000000001" -Headers $headers
Invoke-RestMethod "$base/api/v1/ban-doc?tuKhoa=BD0001" -Headers $headers
```
