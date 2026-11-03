# Hướng dẫn làm việc trong TeamFifai LMS

Đọc README, `docs/OWNERSHIP.md`, `docs/contract-v1.md` và tài liệu phần việc liên quan trước khi sửa.

- P1: nền tảng backend; P2: nghiệp vụ backend và nhóm trưởng; P3: Android; P4: Flutter.
- Giữ một backend và DB chung; P2 bổ sung module vào app của P1.
- Thống nhất thay đổi endpoint, JSON, enum và schema với các phần dùng dữ liệu đó. Cập nhật tài liệu trong cùng thay đổi.
- Tránh sửa code ngoài phần việc được giao. Phần giao nhau cần người sở hữu cùng review.
- Không thêm secret hoặc dữ liệu cá nhân thật. Dùng `.env.example` cho tên biến cấu hình.
- Chạy kiểm tra phù hợp nếu phần mềm đã có công cụ kiểm tra. Ghi đúng lệnh/kết quả; chưa chạy thì ghi chưa chạy. Sườn thư mục hiện chưa có project để build/test.
- Giữ dấu vết đóng góp cá nhân; không tạo commit rỗng hoặc sửa lịch sử chỉ để đạt chỉ tiêu.

<!-- CODEGRAPH_START -->
## CodeGraph

Nếu `.codegraph/` tồn tại tại root repo, dùng `codegraph_explore` hoặc `codegraph explore` trước khi grep/find/đọc code để hiểu hoặc tìm code. Nếu không có `.codegraph/`, bỏ qua CodeGraph; không tự index repo.
<!-- CODEGRAPH_END -->
