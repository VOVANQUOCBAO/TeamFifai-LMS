# Góp ý ERD từ góc nhìn Android Admin (P3)

> Bản nháp tuần 9. Người góp ý: P3. Người nhận: P1 (sở hữu ERD tổng hợp), P2 (bảng nghiệp vụ).
> Chưa đối chiếu với ERD thật. Cột "Trạng thái" điền sau khi P1/P2 trả lời.
> Android không tự tính tồn kho, phạt hay quyền, và không yêu cầu sửa schema chỉ để thuận tiện DTO.

## 1. Điểm cần ERD thể hiện rõ

| STT | Nội dung | Vì sao Android cần | Người trả lời | Trạng thái |
|---|---|---|---|---|
| 1 | Phân biệt nhan đề (sachId) và bản sách (banSachId) | API mượn chỉ nhận banSachId; tránh gửi nhầm sachId | P1 | Chưa chốt |
| 2 | Mã cá biệt (maCaBiet) riêng từng bản, không trùng | Quét hoặc nhập mã để tra bản | P1 | Chưa chốt |
| 3 | maThe gắn với bạn đọc (banDocId) | Tra bạn đọc khi cho mượn | P1 | Chưa chốt |
| 4 | Phiếu mượn gắn cả bản sách và bạn đọc | Màn xác nhận hiện bạn đọc, nhan đề, mã bản | P2 | Chưa chốt |
| 5 | Kệ (viTriKe) và tình trạng/trạng thái bản sách | Hiện ở chi tiết nhan đề/bản | P1 | Chưa chốt |
| 6 | Hạn thẻ (hanThe) | Hiện khi tra bạn đọc; server chặn thẻ hết hạn | P1 | Chưa chốt |
| 7 | Hạn trả (hanTra), số lần gia hạn | Hiện kết quả mượn và màn gia hạn | P2 | Chưa chốt |
| 8 | Phạt (đã tính, đã thu, nợ hiện tại) | Hiện kết quả trả và màn quản lý phạt | P2 | Chưa chốt |
| 9 | Giữ đến lúc (giuDenLuc), hàng đợi | Hiện giuCho khi trả sách | P2 | Chưa chốt |
| 10 | Dữ liệu báo cáo (nguồn, khoảng thời gian, mẫu số tỷ lệ quá hạn) | Màn báo cáo và biểu đồ | P2 | Chưa chốt |
| 11 | Phiên bản biểu phạt | Màn tạo biểu phạt (chỉ ADMIN) | P2 | Chưa chốt |
| 12 | Vai trò nhân viên (ADMIN, THU_THU) | Hiện menu theo vaiTro | P1 | Chưa chốt |

## 2. Filter còn thiếu cho màn hình (đề nghị)

| Màn hình | Filter cần | Ghi chú |
|---|---|---|
| Danh sách sách | Từ khóa nhan đề, phân loại, còn bản rảnh | Phân trang page/pageSize |
| Chi tiết nhan đề | Bản theo sachId, theo trạng thái | |
| Tra bản sách | maCaBiet | Trả đúng một bản |
| Danh sách bạn đọc | maThe, tên, trạng thái thẻ | |
| Nhận trả | Phiếu theo banSachId, trangThai | |
| Báo cáo | Khoảng thời gian | Chờ P2 chốt định nghĩa |

## 3. Câu hỏi dữ liệu mở
- Hạn thẻ ngắn hơn hạn trả dự kiến: server rút ngắn hay từ chối? (P2)
- Lượt đang giữ có tính vào giới hạn mượn không? (P2)
- Trả bản hỏng hoặc mất: có trạng thái riêng không? (P1, P2)
- Phạt dự kiến, đã chốt, đã thu, nợ hiện tại biểu diễn bằng field nào? (P2)
- Giới hạn mượn lưu ở thẻ hay cấu hình chung? (P1, P2)

## 4. Ghi chú
- App chỉ hiển thị kết quả do backend trả về.
- Mọi thay đổi schema do P1/P2 quyết định, P3 chỉ ghi nhu cầu hiển thị.
