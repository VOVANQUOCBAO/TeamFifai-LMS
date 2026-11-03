# Cách dùng sườn thư mục

Đây là khung lưu trữ, chưa phải project sinh đủ bằng Android Studio hoặc Flutter CLI. Những file build, entry point và cấu hình chạy sẽ được người phụ trách tạo sau khi nhóm chốt phân tích.

## Backend

`src/modules/` chia theo chức năng nghiệp vụ. Controller/router, service, repository và validation của cùng chức năng nằm trong module đó theo README backend. `src/shared/` dành cho phần thật sự dùng chung, không gom mọi chức năng vào một file lớn.

`database/` lưu SQL và seed; `tests/` lưu mã kiểm thử. Kết quả chạy và ảnh/log cần nộp nằm ở `docs/testing/evidence/`.

## Android

`app/src/main/java/com/teamfifai/libraryadmin/` chứa package Java dự kiến. `data/` cho API/DTO/repository; `ui/` chia màn hình theo chức năng; `res/` cho XML, hình, menu và giá trị dùng chung.

P3 đối chiếu mã nhóm và namespace khi tạo project thật. Nếu namespace thay đổi, chuyển cả cây Java sang package đã chốt thay vì giữ hai cây song song.

## Flutter

`lib/core/` cho kết nối, phiên và lỗi; `lib/data/` cho model/repository; `lib/features/` chia chức năng; `lib/shared/widgets/` dành cho widget dùng chung. Thư mục platform do công cụ Flutter sinh khi khởi tạo project thật.

## Tài liệu và sản phẩm

- Bản nguồn sơ đồ và bản xuất cùng chức năng đặt trong `docs/design/`.
- Ảnh giao diện để giải thích thiết kế đặt trong `docs/design/screenshots/`; bằng chứng của một ca kiểm thử đặt trong `docs/testing/evidence/`.
- Phần báo cáo của mỗi người đặt trong `docs/report/sections/`; bản tổng hợp/export ở `docs/report/exports/`.
- APK, video và build output không commit vào Git; ghi đường dẫn nhận sản phẩm trong `docs/submission/artifacts.md`.

## Thư mục rỗng

Git không lưu thư mục rỗng. File `.gitkeep` hiện giữ vị trí để GitHub hiển thị sườn. Khi thêm code/tài liệu thật, có thể xóa `.gitkeep` tại thư mục đó. Không đổi tên module chỉ để ghi tên thành viên; cập nhật bảng người phụ trách nếu phân công thay đổi.
