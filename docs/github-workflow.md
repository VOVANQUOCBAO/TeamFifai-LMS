# Cách làm việc với GitHub

## Nhánh và thư mục

Nhánh là phiên bản làm việc; thư mục là chỗ đặt file. Mỗi nhánh vẫn có đầy đủ `backend/`, `admin-android/`, `client-flutter/` và `docs/`.

| Nhánh | Mục đích |
|---|---|
| `main` | Bản tích hợp chung đã kiểm tra |
| `backend` | Nhận thay đổi backend của P1/P2 trước khi ghép vào main |
| `admin-android` | Nhận thay đổi app P3 |
| `client-flutter` | Nhận thay đổi app P4 |
| `feat/p1-*`, `feat/p2-*`, `feat/p3-*`, `feat/p4-*` | Nhánh nhỏ cho từng đầu việc |

P1/P2 tạo nhánh việc từ `backend`; P3 từ `admin-android`; P4 từ `client-flutter`. PR phần việc vào nhánh thành phần tương ứng. Nhóm trưởng phối hợp nhập các nhánh thành phần vào `main`, rồi đưa thay đổi chung từ `main` trở lại các nhánh thành phần khi cần để giữ API/tài liệu đồng bộ. PR chỉ sửa tài liệu chung có thể đi thẳng vào `main` với người liên quan review.

## Một lượt làm việc

1. Chọn Issue có người phụ trách, tiêu chí xong và thư mục đúng.
2. Cập nhật nhánh gốc trước khi tạo nhánh việc.
3. Chỉ sửa phần được giao; phối hợp nếu đụng file chung hoặc API/schema.
4. Tự đọc diff, kiểm tra phù hợp và commit bằng tài khoản của mình.
5. Push nhánh và mở PR; mô tả đúng việc đã làm, lệnh đã chạy và phần còn thiếu.
6. Người sở hữu phần bị ảnh hưởng review. Khi ghép, giữ commit có ý nghĩa để đối chiếu đóng góp cá nhân.

Ví dụ tên nhánh: `feat/p1-auth-contract`, `feat/p2-loan-rules`, `feat/p3-admin-wireframes`, `feat/p4-reader-navigation`.

## Dấu vết cá nhân

Theo E.3, mỗi thành viên cần ít nhất 15 commit có ý nghĩa trong cả đồ án, rải đều trong thời gian làm. Cấu hình tác giả Git đúng tài khoản cá nhân trước khi commit. Không dùng chung một tài khoản, không commit thay cả nhóm hoặc chia commit giả.

Sườn này được khởi tạo bằng một commit; các commit tiếp theo cần phản ánh thay đổi thật. Số commit không thay thế việc hiểu và giải thích được phần mình làm.

`.github/CODEOWNERS` hiện chỉ có chú thích vì chưa có đủ GitHub username của cả nhóm. Điền tài khoản thật sau khi nhóm xác nhận nếu muốn tự gán người review.
