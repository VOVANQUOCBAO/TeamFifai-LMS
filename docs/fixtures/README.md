# Danh tính và tình huống demo chung

P1 tổng hợp; P2 tạo tình huống nghiệp vụ. Khi có DB/seed thật, tạo `docs/demo-fixtures.json` như hợp đồng, chứa định danh fixture hợp lệ và phiên bản dữ liệu; không tạo UUID giả trong mã sản phẩm.

Cần các vai trò và tình huống: ADMIN, THU_THU, reader A/B/C, thẻ chờ/hết hạn/khóa, sách còn/hết bản, sách chưa mượn, phiếu quá hạn và hàng đợi hai người. SQL tạo dữ liệu đặt trong `backend/database/`; thư mục này chứa mô tả và kỳ vọng dùng chung.

Không đặt secret thật, thông tin cá nhân thật hoặc thao tác reset DB public vào fixture.
