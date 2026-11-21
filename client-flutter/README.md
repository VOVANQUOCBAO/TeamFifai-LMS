# TeamFifai Library — Flutter Client, tuần 11

App dành cho BAN_DOC: đăng nhập, tra cứu sách, xem sách đang mượn.
Mã nguồn thuộc P4; backend thuộc P1/P2. Tên app/package hiện dùng TeamFifai.

## Môi trường đã dùng

- Flutter 3.47.5 stable, Dart 3.13.4, Windows.
- Android wrapper do `flutter create --platforms=android` sinh, applicationId `com.teamfifai.teamfifai_client`.
- `http` 1.6.0 và `shared_preferences` 2.5.6; giữ `pubspec.lock` trong Git.
- Bộ dependency đã khóa yêu cầu Flutter >=3.44.0, Dart >=3.13.0. Nên dùng chính Flutter 3.47.5 để tái lập build; đề yêu cầu tối thiểu 3.35+, không bắt buộc dùng đúng 3.35.

## Chạy và build

Chạy trong `client-flutter/`:

```powershell
flutter pub get
flutter analyze
flutter test
flutter run --dart-define=APP_MODE=mock
flutter build apk --debug --dart-define=APP_MODE=mock
```

APK: `build/app/outputs/flutter-apk/app-debug.apk`. Đây là APK debug demo tuần 11, chưa phải bản release ký để nộp cuối kỳ.

Chế độ live (thay URL ví dụ bằng backend thật):

```powershell
flutter run --dart-define=APP_MODE=live --dart-define=API_BASE_URL=https://TEN-BACKEND.onrender.com/api/v1/
flutter build apk --debug --dart-define=APP_MODE=live --dart-define=API_BASE_URL=https://TEN-BACKEND.onrender.com/api/v1/
```

Chỉ nhận mock/live; mock là mặc định. Live yêu cầu HTTPS và hậu tố `/api/v1/`.
Không sửa widget/model để đổi môi trường; không tự chuyển sang fixture khi live bị lỗi.
Token lưu riêng theo chế độ và URL backend. Logout xóa phiên; hết JWT hoặc HTTP 401 đưa về đăng nhập.
Sai mật khẩu đăng nhập hiển thị tại form, không coi là phiên đang hoạt động bị hết hạn.

## Tài khoản mock công khai

- Email: `bandoc@thuviendemo.test`
- Mật khẩu mẫu: `DemoLib!4211`
- Thông tin này lấy từ `assets/demo-ids.json`, không phải bí mật/tài khoản thật.
- Admin/thủ thư bị từ chối trong app Client. Mock chỉ có một tài khoản BAN_DOC đã được hợp đồng cấp; token khác không được xem phiếu.
- Nhãn DEMO luôn xuất hiện ở chế độ mock.

## Hợp đồng được triển khai

Dùng gói `docs/planning/week-10-11/ThanhVien_4_Flutter.zip`, contracts v1.0.0 (cùng gói người dùng cung cấp).
Fixtures và demo-ids được sao chép nguyên vẹn vào assets. Không sửa hợp đồng chung hoặc backend.

| API | Yêu cầu |
|---|---|
| POST auth/dang-nhap | JSON `email`, `matKhau`; nhận `data.token`, `data.nguoiDung` |
| GET sach | `tuKhoa`, `page`, `pageSize`; bearer token |
| GET ban-doc/toi/dang-muon | `page`, `pageSize`; chỉ định danh qua token, không gửi banDocId |

Danh sách có `data` và `meta`; lỗi có `error.code/message`. Trang mặc định 20 phần tử, nút Xem thêm tải trang kế.
`hanTra` theo hợp đồng tuần 11 là ISO 8601 UTC, hiển thị UTC+7. JSON giữ nguyên tên trường camelCase.
HTTP 501 hiển thị “Chức năng chưa tích hợp”. Timeout 90 giây, lỗi mạng có thử lại.

**Lưu ý tích hợp:** `docs/contract-v1.md` và README cũ của repo là bản nháp tuần 9, có quy ước `hanTra` dạng ngày khác gói tuần 11. P2 cần dùng đúng contracts tuần 11 khi ghép. Nhánh này lấy từ main `d8a57de`, vì repo chưa có tag contract-v1.0.0; không tự tạo tag hợp đồng thay P1.

## Cấu trúc code để trình bày khi bảo vệ

1. `core/config/app_config.dart`: đọc tham số build, kiểm tra URL, khóa lưu phiên.
2. `data/models/library_models.dart`: chuyển JSON thành User/Book/Loan/PageResult; xử lý nullable.
3. `core/network/api_service.dart`: HTTP, bearer token, UTF-8, timeout và lỗi.
4. `data/repositories/`: interface chung; mock đọc assets; remote gọi 3 endpoint.
5. `core/session/session_controller.dart`: đăng nhập, kiểm vai trò, lưu/khôi phục token, hết phiên/logout. Đọc exp chỉ để UX; backend vẫn phải xác minh JWT thật.
6. `features/*/screens/`: form đăng nhập, tìm sách, đang mượn.
7. `shared/widgets/paged_list.dart`: loading/rỗng/lỗi/thử lại, phân trang, loại bỏ kết quả bất đồng bộ cũ.
8. `main.dart`: chọn repository, khởi tạo phiên, Navigator và NavigationBar; bỏ các màn hình riêng khi logout.

Luồng dữ liệu: widget → repository → HTTP hoặc fixture → model → widget.
Không gọi HTTP trực tiếp trong widget và không gọi lại API mỗi lần build.

## Kiểm thử và giới hạn

- `test/unit/repository_test.dart`: JSON nullable/UTC, request và mã lỗi, timeout, phiên, vai trò, token khác.
- `test/widget_test.dart`: đăng nhập, validator, tìm kiếm không có kết quả, đang mượn, logout.
- `test/widgets/error_states_test.dart`: 401/403/501, retry, empty/loading, phân trang và kết quả sau dispose; màn hình 390×844.
- Kết quả thực tế và SHA bàn giao ở `../handoff/member4.md`.
- Chưa có URL backend thật nên chưa xác nhận login/sách/đang mượn live hoặc luồng Android tạo phiếu → Flutter tải lại.
- Chưa thử trên điện thoại Android vật lý. Mock không chứng minh API thật.
- Chưa triển khai chức năng cuối kỳ: đăng ký, chi tiết/vị trí kệ, lọc phân loại, đặt trước, gia hạn, lịch sử, tiền phạt, thông báo.
- APK demo dùng debug signing. Bản release cuối kỳ cần cấu hình khóa ký riêng; không commit khóa.

## Checklist demo

1. Đăng nhập sai → hiện lỗi; email không hợp lệ → validator chặn gửi.
2. Đăng nhập tài khoản mock → thấy DEMO và Dế Mèn phiêu lưu ký.
3. Tìm Tô Hoài → có sách; tìm chuỗi không tồn tại → danh sách trống.
4. Chọn Đang mượn → BS0001, hạn 29/11/2026 10:00 Việt Nam; kéo xuống/tải lại.
5. Logout → trở lại đăng nhập; không còn dữ liệu phiếu trên màn hình.
6. Khi có backend: build live, đăng nhập đúng BAN_DOC, thử mất mạng/401/403/501 và mượn xuyên hai app.

AI hỗ trợ triển khai và kiểm thử. Người đóng góp cần đọc, chạy và giải thích từng phần trước bảo vệ; commit được ghi đúng tác giả cá nhân, không đổi ngày hay tạo commit rỗng.
