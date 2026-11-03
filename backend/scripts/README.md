# Script hỗ trợ backend

Chưa có script thực thi. Người 1 điều phối các script dùng chung; người 2 góp phần nghiệp vụ qua review.

Khi cần, có thể bổ sung công cụ migrate, seed, verify dữ liệu hoặc chạy tác vụ nội bộ trên môi trường được chỉ rõ. Chỉ thêm lệnh vào README/package scripts sau khi script thật được viết và chạy kiểm tra.

Script có tác động DB phải kiểm tra môi trường và mục tiêu kết nối; không tự reset DB tích hợp/public. Không đưa secret thật vào file, tham số được ghi log hoặc ví dụ lệnh. Ghi rõ chủ sở hữu, đầu vào, tác động và cách xác minh trong phần hướng dẫn của từng script.
