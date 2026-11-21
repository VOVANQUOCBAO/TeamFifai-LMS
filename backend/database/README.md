# Dữ liệu và migration backend

[`../schema.sql`](../schema.sql) là migration khởi đầu cho DB trống, theo [hợp đồng tuần 11](../../contracts/database.md). Chạy `npm run db:schema` từ `backend/`. Giữ migration khởi đầu khi thêm thay đổi đã áp dụng. Đặt migration tiếp theo trong `migrations/` và phối hợp người phụ trách backend để cấp số.

[`../seed.sql`](../seed.sql) tạo dữ liệu demo qua `npm run db:seed`. [ID và tài khoản demo](../../contracts/demo-ids.json) dùng chung cho cả hai app. Mỗi script chạy trong một transaction. Seed chỉ thêm các ID chưa có, giữ trạng thái lưu thông hiện tại và dùng ngày Việt Nam để tính hạn thẻ khi tạo mới. Dùng DB demo trống khi cần khởi tạo lại dữ liệu.

Dung lượng dữ liệu của mốc cuối kỳ được quản lý riêng. Bộ demo tuần 11 gồm 20 nhan đề, 40 bản và 10 bạn đọc.
