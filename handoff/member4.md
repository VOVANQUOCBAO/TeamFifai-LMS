# Bàn giao P4 — Flutter Client tuần 11

- Tác giả Git: HTrieuHao <trieuhao2005@gmail.com>.
- Nhánh: member4/flutter.
- Commit code được bàn giao: 807273736b8769cc1edf5e40a406e4717d6b8df8.
- Gốc nhánh: main d8a57de; repo chưa có tag contract-v1.0.0 khi bắt đầu.
- Chỉ sửa client-flutter/ và handoff/member4.md.

## Phần đã hoàn thành

- Project Flutter/Android chính thức, tên TeamFifai Library, applicationId com.teamfifai.teamfifai_client.
- Ba màn hình: đăng nhập, danh sách/tìm kiếm sách, danh sách đang mượn.
- Form/validator, Navigator, NavigationBar, hai ListView.builder, ảnh có loading/error.
- Interface repository, mock assets và live HTTP cho đủ 3 API.
- Token shared_preferences tách theo chế độ và URL; kiểm vai trò BAN_DOC, hết phiên, logout và xóa màn hình riêng.
- Loading/rỗng/lỗi/thử lại, phân trang, refresh, HTTP 401/403/501, timeout.
- Mock sách và phiếu cùng snapshot sau khi mượn; không hiển thị phiếu cho token khác.
- README có lệnh chạy/build, tài khoản demo, kiến trúc và kịch bản trình bày.

## Contract

Dùng contracts v1.0.0 trong docs/planning/week-10-11/ThanhVien_4_Flutter.zip.
Đã đối chiếu openapi.yaml và demo-ids.json trong ZIP với gói giao việc: giống nhau.
23 fixture trong assets giống nguyên bản. Không sửa docs/openapi.yaml bản nháp cũ.
P2 cần lưu ý hanTra là ISO 8601 UTC theo gói tuần 11; UI hiển thị UTC+7.

## SDK và cấu hình

Flutter 3.47.5 stable; Dart 3.13.4; Android SDK đã cài trên Windows.
Dependency khóa trong pubspec.lock; yêu cầu Flutter >=3.44.0/Dart >=3.13.0.
Gradle giới hạn 2 worker, heap 2 GB để giảm tài nguyên so với template mặc định 8 GB.

Chạy trong client-flutter:

```powershell
flutter pub get
flutter analyze
flutter test --timeout 30s
flutter build apk --debug --dart-define=APP_MODE=mock
flutter build apk --debug --dart-define=APP_MODE=live --dart-define=API_BASE_URL=https://TEN-BACKEND.onrender.com/api/v1/
```

APP_MODE chỉ mock/live. Live yêu cầu URL thật HTTPS và hậu tố /api/v1/.
Tài khoản mock: bandoc@thuviendemo.test / DemoLib!4211 (chỉ dữ liệu demo công khai).
APK ở client-flutter/build/app/outputs/flutter-apk/app-debug.apk; không commit APK vào Git.

## Kết quả kiểm tra

- flutter analyze: No issues found.
- flutter test --timeout 30s: 20/20 đạt sau thay đổi snapshot mock.
- Đã render và xem đủ 3 màn hình ở kích thước 390×844; không tràn bố cục.
- HTTP được kiểm bằng MockClient: đường dẫn, body, bearer, query, UTF-8, 401/403/501/500, timeout, response sai schema.
- Các kiểm tra HTTP trên KHÔNG phải gọi backend triển khai thật.
- Clone sạch từ commit code ở trên: flutter pub get và build APK debug mock đạt. Build lần đầu của clone bị chậm; lượt build lại với 2 worker đã thành công trong 137,2 giây. APK bàn giao: TeamFifai-Client-demo.apk; SHA256: 53B3AD35FC30A6038F1FDD65045FB9EF4AD74CEEEA57A6F7F83F1478EF391E86.

## Còn chờ P1/P2 và giới hạn

- Chưa có API_BASE_URL thật nên chưa thử login/sách/đang mượn trên server thật.
- P2 chạy luồng Android tạo phiếu → Flutter bạn đọc tương ứng tải lại; xác nhận số bản rảnh và quyền backend.
- Phải dùng cùng contract tuần 11, không dùng nhầm bản nháp hanTra dạng ngày.
- Mock chỉ hỗ trợ tài khoản BAN_DOC có trong demo-ids, không tự chế thêm tài khoản.
- Chưa kiểm APK trên điện thoại vật lý; ảnh là render widget, không phải ảnh máy thật.
- Đây là APK debug tuần 11. Release ký, đủ >=6 màn hình và nghiệp vụ cuối kỳ chưa thuộc lần bàn giao này.
- Chưa có lỗi chức năng được phát hiện trong phạm vi test; kiểm thử thiết bị và backend thật còn cần thực hiện.

Mỗi commit phản ánh một thay đổi thực tế và dùng danh tính người dùng đã xác nhận. Các commit trong phiên này cùng ngày, không thay thế yêu cầu đóng góp đều trong 8 tuần của giảng viên.
