# Flutter Client cho bạn đọc thư viện

Người phụ trách: **Người 4 (P4)**. Thư mục này chuẩn bị nơi làm việc cho app Flutter của hệ thống thư viện. Người 1 cung cấp auth, danh mục và hồ sơ; Người 2 cung cấp phiếu mượn, đặt trước, phạt và thông báo. Người 2 đồng thời là nhóm trưởng.

## Trạng thái hiện tại

Đây là **skeleton thư mục**, chưa phải project Flutter có thể build. Chưa có `pubspec.yaml`, `lib/main.dart`, Android wrapper, cấu hình Gradle, dependency hoặc mã chức năng. Các `.gitkeep` giữ thư mục trống trong Git, không phải mã triển khai. Chưa chạy `flutter analyze`, test hay build APK vì project chưa được khởi tạo.

Sau khi nhóm chốt mã nhóm, tên package, Flutter SDK và cấu hình build, P4 dùng Flutter CLI để sinh project cùng Android wrapper chính thức. Giữ lại tài liệu và cây thư mục đã thống nhất khi ghép các file được công cụ sinh ra. Không tự viết Gradle, wrapper hoặc project giả để báo build được. Stack tham chiếu của đề là Flutter 3.35 trở lên và Dart 3; ghi phiên bản thực tế vào README khi khởi tạo.

## Cấu trúc và trách nhiệm

| Thư mục | Phần sẽ triển khai |
|---|---|
| `lib/core/config` | Cấu hình môi trường và Base URL qua `API_BASE_URL`; không hardcode secret |
| `lib/core/network` | Gọi API, token header, timeout và đọc envelope `data/meta/error` |
| `lib/core/session` | Phiên đăng nhập, lưu token theo yêu cầu môn, đăng xuất và đổi người dùng |
| `lib/core/errors` | Phân loại lỗi mạng và mã HTTP/nghiệp vụ để giao diện phản hồi |
| `lib/data/models` | Model/DTO theo hợp đồng API chung, gồm trường nullable |
| `lib/data/repositories/remote` | Repository gọi backend thật |
| `lib/data/repositories/mock` | Repository thử giao diện bằng fixture chung, tách khỏi remote |
| `lib/features/*/screens` | Màn hình theo tính năng |
| `lib/features/*/controllers` | Trạng thái và thao tác màn hình, tách khỏi widget |
| `lib/features/auth/widgets`, `lib/features/catalog/widgets` | Thành phần riêng cho hai luồng giao diện ưu tiên |
| `lib/features/notifications/services` | Đồng bộ lịch local notification, ánh xạ ID và hủy lịch cũ |
| `lib/shared/widgets` | Thành phần dùng chung: loading, rỗng, lỗi/thử lại, ảnh dự phòng |
| `assets/images`, `assets/icons` | Ảnh và icon đóng gói cần thiết, khai báo asset khi có project thật |
| `test/unit`, `test/widgets` | Nơi đặt test có nội dung thực tế về dữ liệu, phiên, lịch và giao diện |
| `integration_test` | Kiểm luồng app gọi API thật khi có project và môi trường thử |

P4 sở hữu toàn bộ `client-flutter/`. P4 không tạo backend, DB, bảng Supabase hoặc hợp đồng API riêng. Khi thiếu dữ liệu, gửi câu hỏi hoặc đề xuất sửa hợp đồng cho P1/P2.

## Tính năng và API phụ thuộc

Các đường dẫn dưới đây nằm sau Base URL chung `/api/v1`; đây là bản đồ phạm vi theo tài liệu phân công. Đối chiếu hợp đồng API đã được cả nhóm chốt trước khi viết DTO hoặc gọi API. Chủ sở hữu triển khai phía app trong mọi hàng là P4.

| Feature | Luồng của bạn đọc | API tham chiếu | Chủ API |
|---|---|---|---|
| `auth` | Đăng ký, đăng nhập và xác nhận phiên | `POST /auth/dang-ky`, `POST /auth/dang-nhap`, `GET /auth/toi` | P1 |
| `catalog` | Tra cứu, lọc, xem chi tiết nhan đề và vị trí kệ | `GET /phan-loai`, `GET /sach`, `GET /sach/:id` | P1, phối hợp khả dụng với P2 |
| `loans` | Xem đang mượn và yêu cầu gia hạn | `GET /ban-doc/toi/dang-muon`, `PATCH /phieu-muon/:id/gia-han` | P2 |
| `reservations` | Đặt trước, xem hàng đợi, hủy và hoàn tác | `POST /dat-truoc`, `GET /dat-truoc/cua-toi`, `DELETE /dat-truoc/:id`, `PATCH /dat-truoc/:id/khoi-phuc` | P2 |
| `history` | Xem lịch sử mượn | `GET /ban-doc/toi/lich-su` | P2 |
| `fines` | Xem tiền phạt còn nợ và đã thu | `GET /ban-doc/toi/phat` | P2 |
| `notifications` | Xem thông báo, đánh dấu đọc, đồng bộ lịch thiết bị | `GET /thong-bao/cua-toi`, `PATCH /thong-bao/:id/da-doc` và danh sách phiếu đang mượn | P2 |
| `profile` | Xem thẻ, hạn thẻ, giới hạn và lý do bị chặn | `GET /ban-doc/toi` | P1 |

App dành cho `BAN_DOC`. Đăng ký tạo thẻ `CHO_DUYET`: người dùng có thể tra cứu và xem hồ sơ nhưng chưa được đặt trước/gia hạn. Với thẻ `BINH_THUONG` hoặc `KHOA`, app hiển thị điều kiện và lý do backend trả về; backend vẫn kiểm vai trò, điều kiện và quyền sở hữu. App không gửi `banDocId` tùy ý để đọc dữ liệu của người khác.

## Quy ước dữ liệu và thao tác

- UUID là chuỗi; DB và JSON theo hợp đồng chung. Danh sách nằm trong `data` cùng `meta` phân trang, lỗi nằm trong `error`; không mặc định response là mảng trần.
- `hanTra`, `hanThe` là ngày `YYYY-MM-DD` theo Việt Nam. Không đổi múi giờ như timestamp rồi làm lệch ngày. `muonLuc`, `traLuc`, `giuDenLuc` là thời điểm ISO 8601 UTC có `Z`, hiển thị theo giờ Việt Nam.
- Tiền là số nguyên VNĐ. Backend tính phạt, hạn, số bản rảnh và quyền thao tác; Flutter hiển thị kết quả. Phạt dự kiến trên phiếu mở khác khoản phạt đã chốt.
- Trường chưa có giá trị có thể là `null`, gồm ảnh, ngày trả, bản được giữ và hạn giữ. Giao diện có nhánh dự phòng phù hợp.
- Thiết kế loading/có dữ liệu/rỗng/lỗi/thử lại. Phân biệt `401` hết phiên, `403` thiếu quyền và `409` xung đột nghiệp vụ; thông báo nghiệp vụ lấy từ backend.
- Mutation dùng `Idempotency-Key` theo hợp đồng: cùng ý định khi retry giữ nguyên key/body. Timeout không chứng minh backend chưa ghi. Disable thao tác đang gửi và tải lại khi cần xác nhận.
- Hủy/Hoàn tác đặt trước phải thay đổi backend thật. Cửa sổ và trạng thái lấy từ server; không chỉ xóa/thêm item cục bộ. Phương án 10 giây, xếp cuối hàng và chỉ hủy lượt `CHO` cần giữ đúng quyết định nhóm đã chốt.

## Mock và fixture

Khi backend chưa sẵn sàng, P4 có thể dùng `repositories/mock` đọc JSON mẫu/fixture được P1/P2 thống nhất trong tài liệu chung của repository. Remote và mock phải tuân cùng model/hợp đồng. Không tạo bộ UUID, enum hoặc chính sách riêng rồi buộc backend chạy theo.

Việc chuyển mock/remote phải có cấu hình rõ. Bản release/demo nghiệm thu dùng API thật và không tự rơi về dữ liệu giả khi backend lỗi. Base URL đến từ cấu hình build; không đưa Supabase service key, token thử nghiệm, mật khẩu hoặc UUID fixture vào widget.

## Nhắc hạn phía server và thiết bị

P2 phụ trách job backend và dữ liệu thông báo trước hạn 2 ngày, khi quá hạn và khi được giữ sách. P4 phụ trách trung tâm thông báo và lịch local trên thiết bị cho các phiếu đã đồng bộ. Giờ 08:00 Việt Nam là phương án trong tài liệu, cần nhóm xác nhận trước khi triển khai; chọn và khóa plugin sau khi kiểm tra tài liệu chính thức tại thời điểm thực hiện.

Khi đăng nhập, trở về foreground hoặc refresh, tải phiếu thật và so lịch đã lưu theo người dùng/phiếu/hạn. Hủy lịch của phiếu đã trả, hạn đã đổi hoặc người dùng cũ; đặt lịch mới cho mốc còn liên quan. Đăng xuất/đổi tài khoản phải hủy lịch của phiên trước. Lưu ánh xạ ID thông báo ổn định và xử lý trường hợp người dùng từ chối quyền.

Nếu thủ thư trả sách hoặc gia hạn từ Android khi Flutter đang tắt, lịch local chỉ cập nhật sau khi app đồng bộ lại. Không cam kết cập nhật tức thì khi chưa có cơ chế push. Thông báo in-app phía server và lịch local có vai trò khác nhau; cần kiểm thử cả trả/gia hạn/đổi tài khoản trước khi nghiệm thu.

## Mốc phát triển sau skeleton

1. Chốt use case, màn hình, API/JSON và wireframe trong tuần 9. Tuần 9 chưa yêu cầu app hoàn chỉnh.
2. Khởi tạo project bằng Flutter CLI, cấu hình môi trường, network/session và hai luồng ưu tiên: đăng nhập, danh sách sách.
3. Nối API thật để đạt mốc tuần 11, sau đó mở rộng nghiệp vụ theo hợp đồng.
4. Hoàn thiện ít nhất 6 màn theo đề, hai danh sách API, form/điều hướng/ảnh/lifecycle, hủy–hoàn tác và nhắc hạn; kiểm trên máy thật, build release khi có đầy đủ project.

Không đánh dấu test hoặc mốc là đã đạt chỉ vì có sẵn thư mục. Khi triển khai, ghi lệnh thực sự đã chạy, kết quả và bằng chứng tương ứng.

## Nguồn phạm vi

Tài liệu `04_Nguoi4_Flutter_Client.docx` do nhóm cung cấp, đặc biệt mục phạm vi, bước 1–8 và phụ lục A. Yêu cầu môn lấy từ PDF đề tài INT4211. Các file nguồn là căn cứ phân tích; hợp đồng chung đã được nhóm chốt trong repository là căn cứ triển khai sau này.
