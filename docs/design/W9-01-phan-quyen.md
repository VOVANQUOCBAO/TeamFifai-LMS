# P3-W9-01 – Ma trận phân quyền ADMIN/THU_THU

## 1. Mục tiêu

Xác định quyền truy cập của ADMIN và THU_THU trong ứng dụng Android Admin.

BAN_DOC sử dụng ứng dụng Flutter, không thuộc phạm vi Android Admin.

## 2. Ma trận phân quyền

| STT | Chức năng | ADMIN | THU_THU | API Owner |
|---|---|---|---|---|
| 1 | Quản lý danh mục | Có | Có | Backend |
| 2 | Quản lý đầu sách | Có | Có | Backend |
| 3 | Quản lý bản sách | Có | Có | Backend |
| 4 | Quản lý thẻ bạn đọc | Có | Có | Backend |
| 5 | Quản lý mượn sách | Có | Có | Backend |
| 6 | Quản lý trả sách | Có | Có | Backend |
| 7 | Quản lý hàng đợi | Có | Có | Backend |
| 8 | Quản lý thu phạt | Có | Có | Backend |
| 9 | Xem báo cáo | Có | Có | Backend |
| 10 | Quản lý nhân viên | Có | Không | Backend |
| 11 | Tạo phiên bản biểu phạt | Có | Không | Backend |

## 3. Quyền ADMIN

- Quản lý danh mục, đầu sách và bản sách.
- Quản lý thẻ bạn đọc.
- Quản lý mượn và trả sách.
- Quản lý hàng đợi.
- Quản lý thu phạt.
- Xem báo cáo, thống kê.
- Quản lý nhân viên.
- Tạo phiên bản biểu phạt.

## 4. Quyền THU_THU

- Quản lý danh mục, đầu sách và bản sách.
- Quản lý thẻ bạn đọc.
- Quản lý mượn và trả sách.
- Quản lý hàng đợi.
- Quản lý thu phạt.
- Xem báo cáo, thống kê.
- Không được quản lý nhân viên.
- Không được tạo phiên bản biểu phạt.

## 5. Quy định kiểm tra quyền

- Android chỉ ẩn hoặc hiện menu theo vai trò.
- Backend phải tự kiểm tra quyền trước khi xử lý API.
- Không được xem việc ẩn menu là biện pháp bảo mật.
- Nếu người dùng chưa xác thực, Backend trả HTTP 401.
- Nếu người dùng không đủ quyền, Backend trả HTTP 403.
- THU_THU không được gọi trực tiếp API quản lý nhân viên hoặc tạo phiên bản biểu phạt.

## 6. Phối hợp xác nhận

| Người phụ trách | Nội dung xác nhận |
|---|---|
| Nhóm trưởng | Chốt ma trận phân quyền |
| Người 1 | Xác nhận đăng nhập, token và quyền nhân viên |
| Người 2 | Xác nhận quyền nghiệp vụ thư viện |
| Người 3 | Xây dựng Android Admin theo ma trận |
| Backend | Kiểm tra quyền trên API |

## 7. Trạng thái review

- [ ] Nhóm trưởng đã review.
- [ ] Người 1 đã xác nhận.
- [ ] Người 2 đã xác nhận.
- [ ] Backend đã xác nhận API owner.

**Trạng thái:** Bản nháp chờ review.
