# Hợp đồng dùng chung LIB21 v1

**Trạng thái:** bản khung dựa trên tài liệu phân công, chờ cả nhóm xác nhận ở mốc chốt phân tích. Đây không phải bằng chứng API đã hoạt động. Ghi quyết định và câu hỏi tại `decisions.md`.

## Kiến trúc

Một Express app và PostgreSQL chung. P1 cung cấp nền tảng; P2 bổ sung module nghiệp vụ. P3/P4 gọi backend, không giữ service-role key hoặc ghi trực tiếp vào DB.

## Dữ liệu trao đổi dự kiến

- API nghiệp vụ dưới `/api/v1`; `/health` là endpoint riêng dự kiến của hạ tầng.
- UUID là string; tên cột DB snake_case, JSON camelCase.
- Thành công dùng `data`; danh sách thêm `meta.page`, `pageSize`, `total`.
- Lỗi dùng `error.code`, `message`, `details` nếu cần.
- `hanTra`, `hanThe` là ngày; `giuDenLuc` và thời điểm sự kiện là timestamp.
- Tiền là số nguyên VNĐ; trường chưa có giá trị dùng null theo schema được chốt.
- JWT và quyền sở hữu dữ liệu được kiểm tra tại backend.

## Quyền

ADMIN quản lý hệ thống và nhân viên; THU_THU làm lưu thông/danh mục/bạn đọc/báo cáo; BAN_DOC xem dữ liệu cá nhân và dùng chức năng bạn đọc. P1/P2 cần mô tả chính xác quyền từng endpoint, P3/P4 review trước khi làm model.

## Giao diện P1 và P2 cần chốt

| P1 cung cấp | P2 cung cấp |
|---|---|
| Pool/helper transaction, xác thực, quyền, AppError và formatter | Router/service/repository nghiệp vụ dùng cùng client |
| Metadata sách/bản, thẻ và tài khoản | Service lưu thông, trạng thái mượn/giữ, phạt/hàng đợi |
| Chuỗi migration và deploy chung | Migration/seed nghiệp vụ, nội dung job và kiểm thử |

Ghi tên hàm và schema cụ thể khi triển khai. Không tự mở server/Pool/auth khác cho phần nghiệp vụ. Mọi đường ghi liên quan cùng tuân cơ chế transaction/khóa được hai người thống nhất.

## Khi thay hợp đồng

Trong PR ghi phần cũ, phần mới, lý do, người bị ảnh hưởng, migration nếu có và cách kiểm tra. Cập nhật `openapi.yaml`, `api.md`, JSON mẫu và model của app liên quan trong các thay đổi phối hợp. Ghi commit áp dụng để bốn người dùng cùng phiên bản.
