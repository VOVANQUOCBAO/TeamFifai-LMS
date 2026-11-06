# Ma trận quyền ADMIN / THU_THU

| STT | Chức năng | ADMIN | THU_THU | Người sở hữu API | Ghi chú |
|---|---|---|---|---|---|
| 1 | Đăng nhập, xem tài khoản của mình | ✔ | ✔ | P1 | |
| 2 | Quản lý danh mục (phân loại, nhan đề) | ✔ | ✔ | P1 | |
| 3 | Quản lý bản sách (mã cá biệt, kệ, tình trạng) | ✔ | ✔ | P1 | |
| 4 | Quản lý bạn đọc / thẻ | ✔ | ✔ | P1 | |
| 5 | Cho mượn | ✔ | ✔ | P2 | |
| 6 | Nhận trả | ✔ | ✔ | P2 | |
| 7 | Hàng đợi / giữ chỗ | ✔ | ✔ | P2 | |
| 8 | Thu phạt | ✔ | ✔ | P2 | |
| 9 | Xem báo cáo | ✔ | ✔ | P2 | |
| 10 | Quản lý nhân viên | ✔ | ✘ | P1 | Chỉ ADMIN |
| 11 | Tạo phiên bản biểu phạt | ✔ | ✘ | P2 | Chỉ ADMIN |

> Lưu ý: Ẩn menu/nút chỉ hỗ trợ giao diện. Backend vẫn phải chặn gọi trái quyền
> và trả 403. BAN_DOC thuộc app Flutter nên không nằm trong bảng này.
