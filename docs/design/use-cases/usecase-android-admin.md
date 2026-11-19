# Use case Android Admin (P3)

> Bản nháp tuần 9. Người sở hữu: P3 (admin-android). Chờ P2 xác nhận nhánh lỗi nghiệp vụ.
> Kiểm hạn thẻ, giới hạn mượn, nợ phạt, hạn trả và phạt do backend quyết định; app chỉ hiển thị kết quả server trả về.
> Phân biệt nhan đề (sachId) và bản sách (banSachId, có mã cá biệt).

## UC-01: Đăng nhập
- **Tác nhân:** ADMIN, THU_THU
- **Tiền điều kiện:** Nhân viên đã có tài khoản; thiết bị có kết nối mạng.
- **Luồng chính:**
  1. Nhân viên mở app, nhập tên đăng nhập và mật khẩu.
  2. App gửi POST /auth/dang-nhap.
  3. Server trả token, expiresAt, vaiTro.
  4. App lưu token vào session, gọi GET /auth/toi để lấy thông tin tài khoản.
  5. App vào trang chính, hiện menu theo vaiTro (ẩn menu chỉ hỗ trợ giao diện).
- **Nhánh lỗi:**
  - Thiếu thông tin nhập: báo lỗi trên form, không gọi server.
  - Sai tên đăng nhập hoặc mật khẩu (401): hiện thông báo server trả về, ở lại màn đăng nhập.
  - Tài khoản bị khóa: hiện lý do theo error.code/message của server (chờ P1 xác nhận mã lỗi).
  - Lỗi mạng hoặc timeout: hiện lỗi và nút Thử lại.
  - Token hết hạn khi đang dùng (401): xóa session, về màn đăng nhập.
- **Hậu điều kiện:** Có session hợp lệ; không lưu hoặc ghi log mật khẩu và token.

## UC-02: Tra cứu sách và bạn đọc
- **Tác nhân:** ADMIN, THU_THU
- **Tiền điều kiện:** Đã đăng nhập.
- **Luồng chính:**
  1. Nhân viên mở danh sách sách, tìm theo nhan đề (GET /sach, có page/pageSize).
  2. Chọn một nhan đề, app gọi GET /sach/:id (nhanDe, soBanRanh do server trả).
  3. Xem các bản sách của nhan đề (GET /ban-sach?sachId=): maCaBiet, viTriKe, trangThai.
  4. Hoặc tra một bản bằng mã cá biệt (GET /ban-sach?maCaBiet=).
  5. Tra bạn đọc bằng mã thẻ (GET /ban-doc?maThe=): banDocId, hạn thẻ, trạng thái.
- **Nhánh lỗi:**
  - Không có kết quả: hiện trạng thái rỗng.
  - Không tìm thấy mã (404): báo không tìm thấy mã cá biệt hoặc mã thẻ.
  - Lỗi mạng: hiện lỗi và nút Thử lại.
  - 401: về màn đăng nhập. 403: báo thiếu quyền.
- **Hậu điều kiện:** Không thay đổi dữ liệu trên server.

## UC-03: Cho mượn
- **Tác nhân:** ADMIN, THU_THU
- **Tiền điều kiện:** Đã đăng nhập; đã tra được bạn đọc (banDocId) và bản sách cần mượn (banSachId).
- **Luồng chính:**
  1. Nhân viên chọn cho mượn, quét hoặc nhập tay mã thẻ (maThe) và mã cá biệt (maCaBiet).
  2. App tra bạn đọc (GET /ban-doc?maThe=) và bản sách (GET /ban-sach?maCaBiet=).
  3. Màn xác nhận hiện bạn đọc, nhan đề, mã bản.
  4. Nhân viên xác nhận; app gửi POST /phieu-muon với banDocId và banSachId (không gửi sachId), kèm Idempotency-Key.
  5. Server kiểm tra và tạo phiếu; app hiện kết quả gồm hạn trả do server trả về.
- **Nhánh lỗi:**
  - Thẻ hết hạn hoặc bị khóa: server từ chối (409 hoặc mã tương ứng), app hiện lý do server trả.
  - Vượt giới hạn mượn: server từ chối, hiện lý do.
  - Bạn đọc đang nợ phạt: server từ chối, hiện lý do.
  - Bản sách đang được mượn hoặc đang giữ cho người khác: server từ chối, hiện lý do.
  - Không tìm thấy mã thẻ hoặc mã cá biệt (404).
  - Dữ liệu gửi sai (400); không có quyền (403); hết phiên (401, về đăng nhập).
  - Timeout sau khi gửi: Thử lại bằng cùng Idempotency-Key, không tạo key mới.
  - Chống double submit: khóa nút xác nhận khi đang gửi.
- **Hậu điều kiện:** Bản sách chuyển sang đang mượn; phiếu gắn đúng bạn đọc và bản; hạn trả do server quyết định.

## UC-04: Nhận trả
- **Tác nhân:** ADMIN, THU_THU
- **Tiền điều kiện:** Đã đăng nhập; bản sách đang được mượn.
- **Luồng chính:**
  1. Nhân viên chọn nhận trả, quét hoặc nhập tay mã cá biệt.
  2. App tìm phiếu đang mượn (GET /phieu-muon theo banSachId/trangThai).
  3. Màn xác nhận hiện bạn đọc, nhan đề, mã bản.
  4. Nhân viên xác nhận; app gửi PATCH /phieu-muon/:id/tra kèm Idempotency-Key.
  5. Server trả phieuMuon, phat (hoặc null), giuCho (hoặc null).
  6. App hiện: có phạt hay không (số tiền do server tính), có giữ chỗ cho người khác hay không.
- **Nhánh lỗi:**
  - Không có phiếu đang mượn cho mã cá biệt này (404 hoặc 409).
  - Phiếu đã trả trước đó (409): hiện trạng thái hiện tại.
  - Timeout: Thử lại bằng cùng Idempotency-Key.
  - 401 về đăng nhập; 403 báo thiếu quyền; lỗi mạng hiện nút Thử lại.
- **Hậu điều kiện:** Phiếu được đóng; bản sách chuyển trạng thái theo server (rảnh hoặc giữ cho người đầu hàng đợi); phạt (nếu có) do server tính, app không tự tính.

## Chờ chốt (hỏi P2, theo business-rules.md)
- Số liệu hạn mượn, giới hạn số bản, mức phạt: chưa chốt, app không tự quyết.
- Hạn thẻ ngắn hơn hạn trả dự kiến: server rút ngắn hay từ chối?
- Lượt đang giữ có tính vào giới hạn mượn không?
- Trả bản hỏng hoặc mất có nằm trong phạm vi không?
- Mã lỗi cụ thể (error.code) cho từng nhánh lỗi mượn/trả.
