# Bảng màn hình–API Android Admin (P3)

> Bản nháp tuần 9. Mục lục API lấy từ docs/api.md (chưa triển khai). Prefix dự kiến: /api/v1.
> Path, request, response dưới đây là ĐỀ NGHỊ của Android, tất cả "Chưa chốt" cho tới khi P1/P2 xác nhận trong openapi.yaml.
> Chưa có JSON mẫu trong docs/examples/ (thiếu mẫu: đăng nhập, danh sách sách, kết quả mượn/trả; người cung cấp: P1 cho đăng nhập và danh sách sách, P2 cho mượn/trả).

## 1. Quy ước cần xác nhận
- UUID là String; JSON camelCase; envelope data/meta/error.
- Danh sách có page, pageSize, total.
- hanTra, hanThe là DATE; giuDenLuc là thời điểm; tiền VNĐ là số nguyên.
- Lỗi 400/401/403/404/409 kèm error.code và error.message.
- 401: về đăng nhập. 403: báo thiếu quyền.
- Mutation gửi Idempotency-Key, retry sau timeout dùng cùng key.

## 2. Bảng màn hình–API (ưu tiên)

| Màn hình | Hành động | Method/path (sau /api/v1) | Request | Response Android cần | Quyền | Lỗi | Người làm API | Trạng thái |
|---|---|---|---|---|---|---|---|---|
| Đăng nhập | Đăng nhập | POST /auth/dang-nhap | tên đăng nhập, mật khẩu | token, expiresAt, vaiTro | Công khai | 400, 401 | P1 | Chưa chốt |
| Trang chính | Lấy tài khoản hiện tại | GET /auth/toi | token | thông tin nhân viên, vaiTro | ADMIN, THU_THU | 401 | P1 | Chưa chốt |
| Danh sách sách | Tìm, phân trang | GET /sach | từ khóa, page, pageSize | danh sách: id (String), nhanDe, soBanRanh; meta page/pageSize/total | ADMIN, THU_THU | 401, 403 | P1 | Chưa chốt |
| Chi tiết nhan đề | Xem nhan đề | GET /sach/:id | id | nhanDe, soBanRanh | ADMIN, THU_THU | 401, 403, 404 | P1 | Chưa chốt |
| Chi tiết / Cho mượn / Nhận trả | Tra bản sách | GET /ban-sach?sachId= hoặc ?maCaBiet= | sachId hoặc maCaBiet | banSachId, maCaBiet, viTriKe, trangThai | ADMIN, THU_THU | 401, 403, 404 | P1 | Chưa chốt |
| Bạn đọc / Cho mượn | Tra bạn đọc theo mã thẻ | GET /ban-doc?maThe= | maThe | banDocId, hanThe, trạng thái, lý do bị chặn (nếu có) | ADMIN, THU_THU | 401, 403, 404 | P1 | Chưa chốt |
| Cho mượn | Tạo phiếu mượn | POST /phieu-muon | banDocId, banSachId (không gửi sachId); Idempotency-Key | phieuMuon gồm hanTra do server tính | ADMIN, THU_THU | 400, 401, 403, 404, 409 (thẻ hết hạn, vượt giới hạn, nợ phạt, bản đang mượn/giữ) | P2 | Chưa chốt |
| Nhận trả | Tìm phiếu đang mượn | GET /phieu-muon | banSachId, trangThai | phiếu đang mượn, bạn đọc, nhan đề, mã bản | ADMIN, THU_THU | 401, 403, 404 | P2 | Chưa chốt |
| Nhận trả | Trả sách | PATCH /phieu-muon/:id/tra | id; Idempotency-Key | phieuMuon, phat (hoặc null), giuCho (hoặc null) | ADMIN, THU_THU | 401, 403, 404, 409 | P2 | Chưa chốt |

## 3. Backlog (chốt phạm vi sau)

| Chức năng | Nhóm API trong api.md | Người làm | Ghi chú |
|---|---|---|---|
| Gia hạn | /phieu-muon | P2 | Endpoint chưa có |
| Hàng đợi, hủy/hoàn tác | /dat-truoc | P2 | Chưa có |
| Biểu phạt, thu phạt | /bieu-phat, /phat | P2 | Biểu phạt chỉ ADMIN |
| Báo cáo | /bao-cao | P2 | Chưa có |
| Nhân viên | /nguoi-dung | P1 | Chỉ ADMIN |
| Phân loại | /phan-loai | P1 | Chưa có |

## 4. Câu hỏi và quyết định còn mở
- Path đăng nhập và hồ sơ phiên cụ thể là gì (/auth/dang-nhap, /auth/toi)? (P1)
- Cách trả lý do bị chặn: error.code, message hay field riêng? (P1, P2)
- Idempotency-Key có áp dụng cho POST /phieu-muon và PATCH trả không? (P2)
- Cần thêm filter nào cho màn danh sách sách và bạn đọc? (P1)
