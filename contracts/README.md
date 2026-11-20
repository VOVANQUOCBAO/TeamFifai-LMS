# Cách dùng bộ contracts v1.0.0

Giữ nguyên bộ này trên cả ba nhánh. Đọc OpenAPI để viết DTO/request/live implementation; database.md để tạo schema; policies.json để không tự đặt enum/chính sách khác.

Fixture là dữ liệu mock minh họa, không phải DB seed đầy đủ. UI mock sao chép các file cần dùng vào assets và có nhãn DEMO. Live dùng HTTP thật và không tự fallback sang fixture. Người 1 tạo ID/tài khoản demo đã chốt, nhưng sinh thời gian phiếu/hạn thẻ theo lúc chạy seed.

File my-loans-empty là trạng thái bạn đọc BD0001 trước khi mượn; my-loans-after-loan và books-after-loan minh họa sau khi Android mượn BS0001. Người 3 có thể mô phỏng thao tác trong mock repository của mình. Mock của hai app không phải một backend đồng bộ; chỉ nghiệm thu đồng bộ khi cả hai dùng live.

JWT trong fixture mang tiền tố MOCK_TOKEN, không được đưa vào API thật. Khi live, đăng nhập để lấy JWT thật. Tài khoản demo trong demo-ids.json chỉ dùng trong môi trường thử, mật khẩu không phải secret của cơ sở dữ liệu.

Kiểm tra file hợp đồng không bị đổi: python contracts/verify_contracts.py, chạy từ repo root. Checksum chuẩn hóa CRLF thành LF để tương thích Git trên Windows. manifest giúp phát hiện lệch file; kiểm tra chéo version và commit bàn giao khi tích hợp.

Gói này là tài liệu phân công và hợp đồng dữ liệu; không chứa mã backend/Android/Flutter đã triển khai. Người 1/3/4 viết source theo task, người 2 ghép và viết nghiệp vụ sau cùng.
