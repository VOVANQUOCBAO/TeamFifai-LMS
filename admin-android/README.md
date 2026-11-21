# Android Admin — TeamFifai Thư viện (tuần 11)

App cho ADMIN và THỦ THƯ. Java + XML, minSdk 24, Retrofit 2 + Gson. Gói `com.teamfifai.libraryadmin`, TAG log `TEAMFIFAI_ADMIN`.

## Ba màn hình tuần 11

| # | Màn hình | API | Ghi chú |
|---|---|---|---|
| 1 | Đăng nhập (`ui/auth/LoginActivity`) | `POST /auth/dang-nhap` | Kiểm tra email/mật khẩu; chỉ ADMIN/THU_THU được vào; token lưu SharedPreferences, gắn `Bearer` tự động |
| 2 | Danh sách sách (`ui/catalog/books/BooksActivity`) | `GET /sach?tuKhoa=&page=&pageSize=` | RecyclerView + Adapter + ViewHolder, tìm kiếm, phân trang khi cuộn, kéo để tải lại, số bản rảnh |
| 3 | Cho mượn (`ui/loans/CreateLoanActivity`) | `GET /sach/{id}`, `GET /ban-doc`, `POST /phieu-muon` | Chọn **banSachId** từ danh sách bản (chỉ bản RANH + TOT), chọn **banDocId** từ danh sách bạn đọc; hạn trả do backend tính |

Mọi màn hình có trạng thái đang tải / rỗng / lỗi + Thử lại. 401 (token sai/hết hạn) và tài khoản bị khóa tự đăng xuất. 409 hiển thị đúng lý do nghiệp vụ; 501 báo "chờ tích hợp", không báo thành công. ViewModel giữ form và kết quả khi xoay màn hình; nút Cho mượn bị khóa trong lúc gửi để chặn bấm lặp.

Các màn hình prototype cũ (danh mục, bạn đọc, phiếu, phạt, báo cáo — dữ liệu demo) vẫn giữ trong `ui/dashboard`… và chỉ mở được từ menu ở chế độ mock; chúng thuộc mốc sau.

## Kiến trúc

```text
config/AppConfig            appMode + apiBaseUrl từ BuildConfig
data/dto/*                  DTO đúng tên trường OpenAPI v1.0.0
data/api/LibraryApi         interface Retrofit
data/repository/LibraryRepository     interface mà Activity dùng
  ├─ live/LiveLibraryRepository       Retrofit + OkHttp, map lỗi {"error":{code,message}}
  └─ mock/MockLibraryRepository       đọc assets/fixtures (bản sao contracts/fixtures)
session/SessionManager      token theo phạm vi mode+URL (token mock không dùng ở live)
ui/…                        Activity + ViewModel
```

## Build

Chạy trong `admin-android/` bằng JDK 17+ (JBR của Android Studio dùng được). Gradle 9.1, AGP 9.0, compileSdk 36.

```powershell
# Mock (mặc định, có nhãn DEMO)
.\gradlew.bat assembleDebug
# Live: chỉ cần tham số, không sửa Java/XML
.\gradlew.bat assembleDebug -PappMode=live -PapiBaseUrl=https://<backend>.onrender.com/api/v1/
```

`apiBaseUrl` phải kết thúc bằng `/api/v1/`. APK ở `app/build/outputs/apk/debug/app-debug.apk`.

**Lưu ý Windows:** Android Gradle Plugin không build được khi đường dẫn thư mục có dấu tiếng Việt (ví dụ `D:\lập trình di động\…`). Hãy clone repo vào đường dẫn không dấu, ví dụ `D:\code\TeamFifai-LMS`.

Tài khoản mock: `thuthu@thuviendemo.test` hoặc `admin@thuviendemo.test`, mật khẩu `DemoLib!4211` (chỉ dữ liệu demo).
