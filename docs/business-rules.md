# Quy tắc nghiệp vụ cần chốt

P2 tổng hợp; P1 kiểm dữ liệu; P3/P4 kiểm luồng sử dụng. Trạng thái hiện tại là bản nháp theo tài liệu được cung cấp, chưa xác nhận chính sách cuối cùng.

## Yêu cầu của đề

- Phân biệt nhan đề và bản sách; mượn bản cụ thể, đặt trước theo nhan đề.
- Kiểm hạn thẻ, giới hạn mượn và nợ phạt ở backend.
- Trả sách tính ngày quá hạn và phạt bậc thang.
- Khi sách được trả, xét hàng đợi và giữ cho người đầu đủ điều kiện trong 48 giờ.
- Gia hạn bị chặn khi có người đặt trước.
- Nhắc trước hạn 2 ngày và nhắc quá hạn.

## Phương án trong DOCX cần nhóm xác nhận

| Nội dung | Phương án ban đầu | Trạng thái |
|---|---|---|
| Hạn mượn | 14 ngày | Chờ chốt |
| Giới hạn | 5 bản, có thể cấu hình trên thẻ | Chờ chốt |
| Gia hạn | Một lần, thêm 7 ngày vào hạn hiện tại | Chờ chốt |
| Hủy/hoàn tác | Chỉ lượt CHO, 10 giây, khôi phục cuối hàng | Chờ chốt |
| Phạt cộng dồn | Ngày 1–3: 1.000đ/ngày; 4–7: 2.000đ; từ 8: 5.000đ | Chờ chốt |
| Nhắc hạn | 08:00 giờ Việt Nam | Chờ chốt |

## Câu hỏi trước khi code

- Hạn thẻ ngắn hơn hạn trả dự kiến: rút ngắn hay từ chối? Gia hạn vượt hạn thẻ xử lý thế nào?
- Lượt đang giữ có tính vào giới hạn hay không? Cấp nhiều bản giữ cho một người xử lý ra sao?
- Người đầu hàng không đủ điều kiện: trạng thái, lý do và thông báo nào?
- Điều kiện hoàn tác khi đã có một lượt mới của chính bạn đọc đó?
- Trả bản hỏng/mất và chỉnh metadata lúc đang lưu thông nằm trong phạm vi nào?
- Phạt dự kiến, phạt đã chốt, đã thu và nợ hiện tại được biểu diễn thế nào?
- Định nghĩa mẫu số tỷ lệ quá hạn và khoảng thời gian từng báo cáo?

Ghi quyết định đã chốt trong `decisions.md`; sửa các tài liệu liên quan cùng lúc.
