# Danh sách API dự kiến

**Chưa triển khai.** Đây là mục lục thiết kế ban đầu theo phân công. P1/P2 bổ sung request, response, quyền, filter, mã lỗi và ví dụ trong `openapi.yaml` trước khi hai app làm DTO/model. Prefix nghiệp vụ dự kiến: `/api/v1`.

| Nhóm tài nguyên | Người phụ trách | Chức năng cần có |
|---|---|---|
| `/auth` | P1 | Đăng ký, đăng nhập, hồ sơ phiên |
| `/nguoi-dung` | P1 | Quản lý nhân viên dành ADMIN |
| `/phan-loai` | P1 | Danh mục phân loại |
| `/sach` | P1 | Nhan đề, tìm/lọc, chi tiết, số bản khả dụng |
| `/ban-sach` | P1 | Bản vật lý, mã cá biệt, kệ, metadata |
| `/ban-doc` | P1 | Thẻ, duyệt/khóa, hạn và giới hạn |
| `/uploads/anh-bia` | P1 | Upload ảnh bìa |
| `/phieu-muon` | P2 | Mượn, trả, gia hạn |
| `/ban-doc/toi/dang-muon`, `/ban-doc/toi/lich-su`, `/ban-doc/toi/phat` | P2 | Dữ liệu cá nhân theo JWT |
| `/dat-truoc` | P2 | Đặt, hàng đợi, hủy/hoàn tác, danh sách cá nhân |
| `/bieu-phat`, `/phat` | P2 | Biểu phạt và khoản phải thu |
| `/bao-cao` | P2 | Sách hot, quá hạn, chưa mượn, tiền phạt |
| `/thong-bao` | P2 | Thông báo cá nhân và đánh dấu đọc |
| `/internal/jobs/thu-vien` | P2 + P1 cấu hình | Chạy job bằng secret riêng, không đưa secret vào app |

JSON mẫu sau khi chốt đặt trong `docs/examples/`; không dán token, mật khẩu hoặc key thật. P3/P4 ghi các trường còn thiếu bằng Issue thay vì tự thay hợp đồng.
