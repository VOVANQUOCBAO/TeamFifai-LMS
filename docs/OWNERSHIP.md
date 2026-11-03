# Ai đặt file ở đâu

P1 đến P4 là vai trò, chưa phải GitHub username. Điền thông tin thật khi nhóm xác nhận. P2 là nhóm trưởng theo phân công hiện tại.

| Vai trò | Họ tên | GitHub |
|---|---|---|
| P1 — Backend nền tảng | Chưa điền | Chưa điền |
| P2 — Backend nghiệp vụ, nhóm trưởng | Chưa điền | Chưa điền |
| P3 — Android Admin | Chưa điền | Chưa điền |
| P4 — Flutter Client | Chưa điền | Chưa điền |

## Code và dữ liệu

| Đường dẫn | Người phụ trách | Đặt gì vào đây |
|---|---|---|
| `backend/src/config/` | P1 | Đọc và kiểm cấu hình môi trường |
| `backend/src/db/` | P1 | Pool chung và helper transaction |
| `backend/src/middleware/` | P1 | JWT, quyền, validation chung, xử lý lỗi |
| `backend/src/shared/` | P1/P2 theo file | P1: AppError, response; P2: circulation.service.js dự kiến |
| `backend/src/modules/auth/` | P1 | Đăng ký, đăng nhập, tài khoản nhân viên |
| `backend/src/modules/catalog/` | P1 | Phân loại, nhan đề, bản sách và tra cứu |
| `backend/src/modules/readers/` | P1 | Hồ sơ, thẻ, duyệt/khóa và hạn thẻ |
| `backend/src/modules/storage/` | P1 | Upload ảnh bìa |
| `backend/src/modules/loans/` | P2 | Cho mượn, trả, gia hạn, phiếu cá nhân |
| `backend/src/modules/reservations/` | P2 | Hàng đợi, giữ 48 giờ, hủy/hoàn tác |
| `backend/src/modules/fines/` | P2 | Biểu phạt, tính phạt và thu phạt |
| `backend/src/modules/reports/` | P2 | Bốn báo cáo nghiệp vụ |
| `backend/src/modules/notifications/` | P2 | Thông báo lưu trong DB và API cá nhân |
| `backend/src/jobs/` | P2 | Xử lý hết giữ và nhắc hạn; P1 phối hợp lịch chạy |
| `backend/database/` | P1 tổng hợp; P2 góp phần nghiệp vụ | Schema, migration, seed và dữ liệu thử |
| `backend/tests/` | Người sở hữu chức năng | Unit, integration, concurrency phù hợp |
| `admin-android/` | P3 | Toàn bộ project Java/XML, test và cấu hình Android |
| `client-flutter/` | P4 | Toàn bộ project Dart, test, assets và cấu hình Flutter |

## Điểm giao nhau phải review chung

- P1 sửa metadata bản sách; P2 điều khiển trạng thái mượn/giữ qua service lưu thông. Sửa thẻ hoặc tình trạng bản có ảnh hưởng quyền mượn cần phối hợp cả hai.
- P1 cấp số migration tăng dần; P2 nhận số rồi thêm migration nghiệp vụ. Không cùng sửa một migration đã áp dụng.
- P1/P2 cùng thống nhất JSON, enum, lỗi và API; P3/P4 kiểm dữ liệu đủ dùng trước khi viết model.
- P2 tạo thông báo và dữ liệu hạn; P4 phụ trách lịch nhắc trên thiết bị và đồng bộ lại khi dữ liệu đổi.

## Ví dụ đặt file về sau

| Việc làm | Vị trí |
|---|---|
| P1 thêm API tìm sách | Trong `backend/src/modules/catalog/` |
| P2 viết logic trả sách | Trong `backend/src/modules/loans/` |
| P2 viết xử lý hết 48 giờ | `backend/src/jobs/`, gọi service nghiệp vụ chung |
| P3 thêm màn cho mượn | Khu vực `ui/loans/` trong package Java |
| P4 thêm màn đặt trước | `client-flutter/lib/features/reservations/` |
| P3 vẽ wireframe | `docs/design/wireframes/admin/` |
| P4 ghi kết quả test | `docs/testing/evidence/p4/` |

Không để mã riêng của mỗi người trong thư mục như `code-cua-bao/` hoặc `bai-moi/` tại root. Dùng thư mục chức năng và lịch sử Git để xác định tác giả.
