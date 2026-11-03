# Android Admin quản lý thư viện

Người 3 (P3) sở hữu thư mục `admin-android/`. Ứng dụng dành cho ADMIN và THU_THU, gọi backend chung của nhóm và dùng cùng dữ liệu với Flutter Client.

Đây là khung thư mục để phân công và đặt mã nguồn. Chưa có project Gradle chạy được, mã Java chức năng, AndroidManifest, APK hay kết quả build/kiểm thử. Các tệp `.gitkeep` chỉ giúp Git lưu các thư mục đang trống.

## Công nghệ và package dự kiến

Theo tài liệu đề tài, ứng dụng sử dụng **Java + XML, minSdk 24, Retrofit 2 + Gson**. Không dùng bộ khung này như một dự án Kotlin/Compose.

Package dự kiến là `com.teamfifai.libraryadmin`. P3 cần đối chiếu **mã nhóm được giảng viên công nhận** trước khi tạo project bằng Android Studio, rồi thống nhất applicationId, namespace, package nguồn, tên ứng dụng và TAG log. Nếu cần đổi package, đổi đồng bộ cả thư mục mã nguồn và các cấu hình do Android Studio sinh.

## Nơi đặt mã nguồn

```text
admin-android/
├── README.md
└── app/
    └── src/
        ├── main/
        │   ├── java/com/teamfifai/libraryadmin/
        │   │   ├── data/
        │   │   │   ├── api/
        │   │   │   ├── dto/
        │   │   │   └── repository/
        │   │   ├── session/
        │   │   ├── util/
        │   │   └── ui/
        │   │       ├── auth/
        │   │       ├── dashboard/
        │   │       ├── catalog/
        │   │       │   ├── categories/
        │   │       │   ├── books/
        │   │       │   └── copies/
        │   │       ├── readers/
        │   │       ├── loans/
        │   │       ├── returns/
        │   │       ├── reservations/
        │   │       ├── fines/
        │   │       ├── reports/
        │   │       └── staff/
        │   └── res/
        │       ├── layout/
        │       ├── drawable/
        │       ├── mipmap/
        │       ├── values/
        │       ├── menu/
        │       └── xml/
        ├── test/java/com/teamfifai/libraryadmin/
        └── androidTest/java/com/teamfifai/libraryadmin/
```

| Thư mục dưới package Java | Nội dung sẽ đặt |
|---|---|
| `data/api` | Retrofit interface, cấu hình gọi API và interceptor; dùng Base URL của người 1. |
| `data/dto` | DTO request/response, envelope `data/meta/error`, model theo hợp đồng API đã chốt. |
| `data/repository` | Lớp lấy/gửi dữ liệu cho UI; tách mock có cấu hình rõ nếu cần trong phát triển. |
| `session` | Lưu token, đăng xuất, phục hồi phiên và xử lý hết phiên. |
| `util` | Tiện ích hiển thị, định dạng ngày/tiền và kiểm tra dữ liệu nhập dùng chung. |
| `ui/auth` | Đăng nhập; lỗi đăng nhập và điều hướng theo phiên. |
| `ui/dashboard` | Trang chính và menu theo quyền ADMIN/THU_THU. |
| `ui/catalog/categories` | Danh sách/form phân loại, xác nhận xóa. |
| `ui/catalog/books` | RecyclerView sách, tìm/lọc, form nhan đề và chi tiết nhan đề. |
| `ui/catalog/copies` | Danh sách/chi tiết/form bản sách, mã cá biệt, vị trí kệ và tình trạng. |
| `ui/readers` | RecyclerView bạn đọc, tìm mã thẻ, duyệt thẻ, hạn thẻ và giới hạn mượn. |
| `ui/loans` | Cho mượn: quét/nhập mã thẻ và mã bản, xác nhận, gửi hai ID tới backend. |
| `ui/returns` | Tìm phiếu đang mượn, nhận trả, hiển thị phạt và giữ chỗ do backend trả. |
| `ui/reservations` | Xem hàng đợi/giữ chỗ và hạn nhận. |
| `ui/fines` | Danh sách phạt, xác nhận thu, xem biểu phạt; đổi biểu phạt chỉ ADMIN. |
| `ui/reports` | Sách mượn nhiều, quá hạn, chưa từng mượn, tiền phạt; biểu đồ và bộ lọc. |
| `ui/staff` | Quản lý tài khoản nhân viên, chỉ ADMIN. |

Gia hạn phiếu đặt tại luồng phiếu mượn trong `ui/loans`; từ hàng đợi có thể điều hướng tới phiếu liên quan. Có thể dùng Activity hoặc Fragment phù hợp luồng; không cần tạo một Activity cho mỗi thư mục.

Mã Java của từng màn (Activity/Fragment, ViewModel, Adapter) đặt cùng thư mục chức năng tương ứng khi bắt đầu triển khai. Không tạo lớp rỗng chỉ để lấp đầy bộ khung.

| Thư mục tài nguyên/kiểm thử | Nội dung sẽ đặt |
|---|---|
| `app/src/main/res/layout` | XML màn hình, item RecyclerView và dialog. |
| `app/src/main/res/drawable` | Shape/vector/ảnh dùng trong giao diện. |
| `app/src/main/res/mipmap` | Biểu tượng launcher; Android Studio có thể sinh thêm các thư mục mật độ. |
| `app/src/main/res/values` | Strings, màu sắc, kích thước và theme. |
| `app/src/main/res/menu` | XML menu điều hướng hoặc thao tác. |
| `app/src/main/res/xml` | Tài nguyên cấu hình XML cần thiết sau khi chốt thiết kế. |
| `app/src/test/java/com/teamfifai/libraryadmin` | Unit test chạy trên JVM khi đã có cấu hình dự án. |
| `app/src/androidTest/java/com/teamfifai/libraryadmin` | Kiểm thử cần thiết bị/emulator khi đã có cấu hình dự án. |

## Khi tạo project Android thật

1. Xác nhận mã nhóm/package, Java + XML, minSdk 24 và hợp đồng API với nhóm trưởng.
2. Dùng Android Studio sinh project Android thật. Có thể sinh vào một thư mục tạm, rồi đưa cấu hình và mã được sinh vào `admin-android/` sau khi kiểm tra; giữ mapping thư mục ở trên.
3. Các tệp Gradle cấp project, settings, Gradle Wrapper và `gradle/` sẽ nằm tại gốc `admin-android/`; Gradle cấp module nằm trong `admin-android/app/`. Để Android Studio sinh các tệp này theo phiên bản được nhóm thống nhất.
4. Manifest thật nằm tại `admin-android/app/src/main/AndroidManifest.xml`. Chỉ khai báo quyền/tính năng cần cho luồng đã triển khai; chưa có manifest trong khung hiện tại.
5. Chỉ thêm Retrofit/Gson hoặc thư viện quét/biểu đồ khi triển khai phần tương ứng và đã chốt phiên bản. Cập nhật README bằng phiên bản SDK/Gradle, cách cấu hình Base URL và lệnh build thực tế.
6. Xóa `.gitkeep` ở thư mục khi đã có tệp thật. Giữ lại marker ở những thư mục còn trống nhưng vẫn cần theo phân công.

Các tệp cục bộ như `local.properties`, keystore và mật khẩu ký không phải nội dung bộ khung để chia sẻ. Không đặt khóa backend hoặc khóa Supabase service role vào ứng dụng.

## Hợp đồng và phối hợp

- Người 1 cung cấp auth, catalog, readers, cấu trúc JSON, Base URL, tài khoản demo và fixture.
- Người 2 cung cấp loans, returns, reservations, fines, reports và mã lỗi nghiệp vụ.
- Retrofit cần Base URL có dấu `/` cuối, ví dụ cấu hình backend kết thúc `/api/v1/`; URL thật sẽ do người 1 cấp.
- UUID là chuỗi; JSON camelCase; UI đọc `data/meta/error` theo hợp đồng. Không tự đổi model/enum để làm một màn chạy riêng.
- Backend quyết quyền, tồn kho, hạn trả, gia hạn, phạt và hàng đợi. Android gửi yêu cầu rồi hiển thị kết quả, không tự tính thay.
- ADMIN và THU_THU có menu khác nhau; phân quyền thật vẫn phải được backend kiểm tra.
- Thay đổi từ Android cần được người 4 đối chiếu trên Flutter ở các mốc tích hợp. Khi thiếu hoặc sai API, gửi lỗi/đề nghị thay đổi cho người sở hữu; không sửa ngoài phạm vi để chữa nhanh DTO.

Tuần 9 ưu tiên phân tích use case, wireframe, mapping màn hình–API và góp ý ERD/phân công. Mốc tuần 11 mới cần hai màn Android gọi API thật; bộ khung thư mục này chưa chứng minh đã đạt mốc đó.
