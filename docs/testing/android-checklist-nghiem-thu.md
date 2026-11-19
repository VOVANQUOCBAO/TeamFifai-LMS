# Checklist kỹ thuật và nghiệm thu Android Admin (P3)

> Bản nháp tuần 9. Đây là checklist cho giai đoạn triển khai sau, CHƯA có mục nào được thực hiện hay kiểm thử.
> Mọi ca kiểm thử bên dưới đều ở trạng thái "Dự kiến", không ghi "Đạt" khi chưa chạy thật và chưa có bằng chứng.

## 1. Checklist kỹ thuật (triển khai về sau)

| STT | Hạng mục | Trạng thái |
|---|---|---|
| 1 | Ngôn ngữ Java, giao diện XML | Chưa làm |
| 2 | minSdk 24 | Chưa làm |
| 3 | Package, tên app, TAG có mã nhóm (mã nhóm: chờ nhóm trưởng xác nhận) | Chưa làm |
| 4 | Retrofit và Gson | Chưa làm |
| 5 | Base URL cấu hình tại một chỗ, không hard-code rải rác | Chưa làm |
| 6 | Token và session: lưu, xóa khi đăng xuất hoặc 401 | Chưa làm |
| 7 | RecyclerView, Adapter, ViewHolder cho danh sách sách và bạn đọc | Chưa làm |
| 8 | Validate form; hộp thoại xác nhận trước khi xóa | Chưa làm |
| 9 | Giữ dữ liệu form khi xoay máy | Chưa làm |
| 10 | Mỗi màn gọi API có 4 trạng thái: loading, có dữ liệu, rỗng, lỗi kèm nút Thử lại | Chưa làm |
| 11 | 401: xóa session, về đăng nhập; 403: báo thiếu quyền, ở lại màn | Chưa làm |
| 12 | Không lộ token (không log, không hiện lên giao diện) | Chưa làm |
| 13 | Không double submit: khóa nút khi đang gửi | Chưa làm |
| 14 | Mutation (mượn, trả...) gửi Idempotency-Key; retry sau timeout dùng cùng key | Chưa làm |
| 15 | Phạt, hạn trả, số bản rảnh lấy từ server, app không tự tính | Chưa làm |

## 2. Rủi ro

| Rủi ro | Cách giảm |
|---|---|
| Nhầm sachId với banSachId khi gọi API mượn | Đặt tên field rõ trong DTO, review với P2 |
| Tạo Idempotency-Key mới khi retry nên mượn trùng | Giữ key theo từng thao tác, chỉ tạo key mới cho thao tác mới |
| Nhầm 401 với 403 | Xử lý tập trung ở một chỗ (interceptor hoặc lớp xử lý lỗi chung) |
| Hợp đồng API đổi sau khi viết DTO | Ghi thay đổi bằng Issue, đối chiếu openapi.yaml trước khi sửa model |
| Chưa có API thật, JSON mẫu ở tuần 11 | Chốt danh sách phụ thuộc (mục 4), dùng fixture khi chờ |
| Camera quét mã khó kiểm trên máy ảo | Luôn có nhập tay dự phòng; kiểm camera trên máy thật |

## 3. Ca kiểm thử dự kiến (chưa chạy)

| Mã | Ca kiểm thử | Kết quả mong đợi | Trạng thái |
|---|---|---|---|
| TC-01 | Đăng nhập bằng tài khoản ADMIN | Vào trang chính, thấy đủ menu kể cả nhân viên và biểu phạt | Dự kiến |
| TC-02 | Đăng nhập bằng tài khoản THU_THU | Vào trang chính, không thấy menu chỉ ADMIN | Dự kiến |
| TC-03 | THU_THU gọi chức năng chỉ ADMIN | Server trả 403, app báo thiếu quyền | Dự kiến |
| TC-04 | Sai mật khẩu | Báo lỗi, ở lại màn đăng nhập (401) | Dự kiến |
| TC-05 | Danh sách sách: có dữ liệu, rỗng, lỗi mạng và Thử lại | Hiển thị đúng từng trạng thái | Dự kiến |
| TC-06 | Danh sách bạn đọc: có dữ liệu, rỗng, lỗi mạng và Thử lại | Hiển thị đúng từng trạng thái | Dự kiến |
| TC-07 | Xoay máy khi đang nhập form | Dữ liệu đã nhập còn nguyên | Dự kiến |
| TC-08 | Cho mượn thành công | Hiện kết quả với hạn trả do server trả | Dự kiến |
| TC-09 | Cho mượn bị chặn (thẻ hết hạn, vượt giới hạn, nợ phạt, bản đang mượn/giữ) | Hiện lý do server trả, không tạo phiếu | Dự kiến |
| TC-10 | Nhận trả có phạt và có người giữ chỗ | Hiện phat và giuCho do server trả | Dự kiến |
| TC-11 | Tắt mạng khi đang thao tác | Báo lỗi mạng, có nút Thử lại | Dự kiến |
| TC-12 | Timeout sau khi gửi mượn, bấm Thử lại | Dùng cùng Idempotency-Key, không tạo phiếu trùng | Dự kiến |
| TC-13 | Bấm nhanh nút Xác nhận nhiều lần | Chỉ gửi một request | Dự kiến |
| TC-14 | Token hết hạn khi đang dùng | Về màn đăng nhập (401) | Dự kiến |
| TC-15 | Nhập mã bằng tay và (sau này) bằng camera | Cả hai cách tra được bản sách và bạn đọc | Dự kiến |

## 4. Phụ thuộc cần có trước tuần 11

| Phụ thuộc | Người cung cấp | Trạng thái |
|---|---|---|
| Base URL (kể cả prefix /api/v1) | P1 | Chưa có |
| Tài khoản test ADMIN và THU_THU | P1 | Chưa có |
| JSON fixture: đăng nhập, danh sách sách, kết quả mượn/trả | P1 (đăng nhập, sách), P2 (mượn/trả) | Chưa có |
| API thật cho đăng nhập và danh sách sách | P1 | Chưa có |
| Quy ước idempotency và mã lỗi nghiệp vụ | P2 | Chưa chốt |
| Lịch nhận API | P2 (nhóm trưởng) | Chưa chốt |

Không đưa secret, token hay mật khẩu thật vào tài liệu hoặc repo.
