# TeamFifai LMS

Hệ thống quản lý thư viện trường học cho đồ án INT4211: Android Admin, Flutter Client và một backend dùng chung PostgreSQL.

Repository hiện là **sườn thư mục để phân công và bắt đầu triển khai**. Chưa có ứng dụng chạy, API thật, schema hoàn chỉnh hay APK. Mỗi người tạo project và bổ sung mã nguồn trong đúng khu vực bên dưới.

## Bắt đầu từ đâu

1. Đọc [bảng người phụ trách và vị trí đặt file](docs/OWNERSHIP.md).
2. Đọc [kế hoạch tuần 9](docs/planning/week-09/00_TongHop_Tuan9.md) và checklist của mình.
3. Xem [cách dùng nhánh, commit và PR](docs/github-workflow.md).
4. Đọc [hợp đồng chung bản nháp](docs/contract-v1.md); xác nhận API và chính sách với nhóm trước khi viết chức năng.

## Bốn khu vực chính

```text
TeamFifai-LMS/
├── backend/          # P1 nền tảng + P2 nghiệp vụ; cùng một server và DB
├── admin-android/    # P3; Android Java/XML
├── client-flutter/   # P4; Flutter/Dart
├── docs/             # Thiết kế, kế hoạch, API, kiểm thử, báo cáo và bàn giao
├── .github/          # Mẫu Issue/PR và cấu hình review khi biết tài khoản nhóm
├── AGENTS.md         # Quy ước cho công cụ AI khi làm trong repo
└── README.md
```

| Thành viên | Chỗ đặt code chính | Việc phối hợp |
|---|---|---|
| P1 — Backend nền tảng | `backend/src/config`, `db`, `middleware`; modules `auth`, `catalog`, `readers`, `storage` | Tổng hợp ERD, migration, DB và deploy |
| P2 — Backend nghiệp vụ | Modules `loans`, `reservations`, `fines`, `reports`, `notifications`; `backend/src/jobs` | Chốt nghiệp vụ, use case, phân công và ghép hệ thống |
| P3 — Android Admin | `admin-android/` | Luồng ADMIN/THU_THU; API của P1/P2 |
| P4 — Flutter Client | `client-flutter/` | Luồng BAN_DOC; API và nhắc hạn của P1/P2 |

Xem README trong từng thư mục trước khi thêm file. Cây thư mục sâu hơn được giải thích tại [docs/structure.md](docs/structure.md).

## Quy ước chung

- Một nhan đề `sach` có nhiều bản vật lý `ban_sach`. Mượn theo bản; đặt trước theo nhan đề.
- P1 và P2 dùng chung Express app, DB Pool, xác thực và cơ sở dữ liệu. Hai app gọi backend.
- Định nghĩa API tập trung ở `docs/openapi.yaml`, kèm bảng đọc nhanh `docs/api.md` và JSON mẫu trong `docs/examples/`.
- P1 tổng hợp migration; P2 gửi phần nghiệp vụ và nhận số migration trước khi tạo file.
- `.gitkeep` chỉ giúp Git lưu thư mục rỗng. Khi có file thật, có thể bỏ `.gitkeep` trong chính thư mục đó.
- Commit của người nào dùng tài khoản của người đó. Toàn kỳ mỗi người cần ít nhất 15 commit có ý nghĩa; sườn ban đầu này chỉ là một thay đổi khởi tạo.

## Mốc của môn

| Mốc | Sản phẩm |
|---|---|
| Tuần 9 | Bản nháp use case, ERD và bảng phân công |
| Tuần 11 | Backend deploy; ít nhất 3 API thật; 2 màn hình mỗi app |
| Tuần 13 | Hai app gọi API thật và chạy luồng nghiệp vụ chính |
| Tuần 15 | Sản phẩm hoàn chỉnh, APK, báo cáo, video và bảo vệ |

Stack theo tài liệu môn: Java/XML, Flutter/Dart, Node.js 20 + Express 4 + pg, PostgreSQL/Supabase và backend trên Render. Việc đổi stack cần xử lý theo quy định môn. Phiên bản dependency cụ thể sẽ được khóa khi tạo project.

## Tài liệu và sản phẩm

- [Thiết kế và tài liệu dùng chung](docs/README.md)
- [Quy tắc nghiệp vụ đang chờ chốt](docs/business-rules.md)
- [Quyết định và câu hỏi còn mở](docs/decisions.md)
- [Kế hoạch và bằng chứng kiểm thử](docs/testing/README.md)
- [Triển khai](docs/deployment/README.md)
- [Báo cáo](docs/report/README.md)
- [Danh mục sản phẩm nộp](docs/submission/README.md)

Không đưa `.env`, khóa ký app, token hoặc chuỗi kết nối thật vào repo. APK/video/build output được giữ ngoài mã nguồn; ghi vị trí bàn giao trong `docs/submission/artifacts.md`.
