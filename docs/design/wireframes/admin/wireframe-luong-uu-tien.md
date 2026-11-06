# Wireframe Android Admin: luồng ưu tiên (P3)

> Bản nháp tuần 9, mức đơn giản. Chưa yêu cầu camera hay API chạy thật.
> Hạn trả, phạt, số bản rảnh do server trả về, app chỉ hiển thị.
> Mỗi màn có 4 trạng thái: Loading, Có dữ liệu, Rỗng, Lỗi (kèm Thử lại hoặc Quay lại).

## Luồng 1: Đăng nhập → Danh sách sách → Chi tiết nhan đề/bản

### Màn 1: Đăng nhập
```
+--------------------------+
|        THƯ VIỆN          |
| Tên đăng nhập [________] |
| Mật khẩu      [________] |
|        [ Đăng nhập ]     |
| (lỗi) Sai tên hoặc mật   |
|       khẩu               |
+--------------------------+
```
- Nhập: tên đăng nhập, mật khẩu. Gửi: POST /auth/dang-nhap.
- Loading: nút khóa, hiện vòng xoay (chống double submit).
- Thành công: lưu token, vào Trang chính. 401: báo lỗi, ở lại.
- Lỗi mạng: thông báo + nút [Thử lại].

### Màn 2: Danh sách sách (RecyclerView)
```
+--------------------------+
| < Sách        [Tìm: ___] |
|--------------------------|
| Nhan đề A     Rảnh: 2    |
| Nhan đề B     Rảnh: 0    |
| Nhan đề C     Rảnh: 5    |
|        ... cuộn thêm     |
+--------------------------+
```
- Gửi: GET /sach (page, pageSize, từ khóa). Xem: nhanDe, soBanRanh (server trả).
- Loading: vòng xoay giữa màn. Rỗng: "Không có sách phù hợp".
- Lỗi: thông báo + [Thử lại]. 401 về Đăng nhập. 403 báo thiếu quyền.

### Màn 3: Chi tiết nhan đề / bản sách
```
+--------------------------+
| < Nhan đề A              |
| Số bản rảnh: 2           |
|--------------------------|
| Mã CB: 000123  Kệ A2     |
|   Trạng thái: Rảnh       |
| Mã CB: 000124  Kệ A2     |
|   Trạng thái: Đang mượn  |
|      [ Cho mượn bản này ]|
+--------------------------+
```
- Gửi: GET /sach/:id, GET /ban-sach?sachId=. Xem: maCaBiet, viTriKe, trangThai.
- Rỗng: "Chưa có bản sách". Lỗi: [Thử lại] hoặc [Quay lại].

## Luồng 2: Cho mượn

### Màn 4: Nhập mã thẻ và mã cá biệt
```
+--------------------------+
| < Cho mượn               |
| Mã thẻ     [______] [Quét]|
| Mã cá biệt [______] [Quét]|
|   (hoặc nhập tay)        |
|        [ Tiếp tục ]      |
+--------------------------+
```
- Hai cách nhập dự kiến: quét camera và nhập tay (camera chưa làm tuần 9).
- Gửi: GET /ban-doc?maThe=, GET /ban-sach?maCaBiet=.
- 404: "Không tìm thấy mã thẻ / mã cá biệt". Lỗi mạng: [Thử lại].

### Màn 5: Xác nhận
```
+--------------------------+
| Xác nhận cho mượn        |
| Bạn đọc : Nguyễn Văn A   |
| Nhan đề : Nhan đề A      |
| Mã bản  : 000123         |
| (Hạn trả do server trả   |
|  sau khi xác nhận)       |
| [ Quay lại ] [ Xác nhận ]|
+--------------------------+
```
- Gửi: POST /phieu-muon với banDocId, banSachId (không gửi sachId) + Idempotency-Key.
- Loading: khóa nút Xác nhận. Timeout: [Thử lại] dùng cùng Idempotency-Key.

### Màn 6: Kết quả mượn
```
+--------------------------+
| Cho mượn thành công      |
| Hạn trả: (từ server)     |
| [ Cho mượn tiếp ] [Xong] |
|--- hoặc khi bị chặn ---  |
| Không thể cho mượn       |
| Lý do: (từ server)       |
| [ Quay lại ]             |
+--------------------------+
```
- Lý do bị chặn lấy từ error.code/message của server (thẻ hết hạn, vượt giới hạn, nợ phạt, bản đang mượn/giữ).

## Đường Nhận trả (chú thích)
```
Nhận trả → Nhập/quét mã cá biệt → Xác nhận (bạn đọc, nhan đề, mã bản)
        → Kết quả: phạt (hoặc không), giữ chỗ cho người khác (hoặc không)
```
- Gửi: GET /phieu-muon theo banSachId/trangThai, rồi PATCH /phieu-muon/:id/tra.
- Kết quả hiển thị phat và giuCho do server trả (có thể null). App không tự tính phạt.
- Cùng quy ước Loading, Rỗng, Lỗi, Thử lại như luồng Cho mượn.

## Chờ chốt
- Giao diện thu phạt ngay sau khi trả: có hay không (hỏi P2).
- Cách hiển thị lý do chặn: dùng message server hay map theo error.code (hỏi P1, P2).
