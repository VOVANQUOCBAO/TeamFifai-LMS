# Database contract v1.0.0

Người 1 triển khai toàn bộ schema này trước khi bàn giao. Người 2 dùng trực tiếp để làm nghiệp vụ, không phải chờ bổ sung các bảng chính. Mọi bảng dùng id UUID PRIMARY KEY; FK thật; timestamp nghiệp vụ là TIMESTAMPTZ. Các cột không ghi nullable mặc định NOT NULL; mặc định ID mới do DB/backend tạo, không dùng UUID cố định của seed cho dữ liệu mới.

## 1. nguoi_dung

id UUID; email TEXT UNIQUE (chuẩn hóa trim + lowercase trước ghi/tìm); mat_khau_hash TEXT; ho_ten TEXT; vai_tro TEXT CHECK trong ADMIN/THU_THU/BAN_DOC; trang_thai TEXT CHECK trong HOAT_DONG/KHOA.

JWT sub tham chiếu id bảng này. Backend sau xác thực đặt req.nguoiDung.id và req.nguoiDung.vaiTro. Token thiếu/hết hạn/sai chữ ký bị 401; tài khoản khóa bị chặn. Không có API cho người dùng công khai tự nâng vai trò.

## 2. phan_loai

id UUID; ma TEXT UNIQUE; ten TEXT.

## 3. sach

id UUID; nhan_de TEXT; tac_gia TEXT; nxb TEXT nullable; nam_xb INTEGER nullable CHECK 0..9999 khi có; isbn TEXT UNIQUE nullable; phan_loai_id UUID FK phan_loai; anh_bia TEXT nullable.

Không lưu tổng số bản rảnh làm con số nhập tay. API tính từ ban_sach: trang_thai=RANH và tinh_trang=TOT. Tìm kiếm theo nhan đề/tác giả; lọc phan_loai_id.

## 4. ban_sach

id UUID; sach_id UUID FK sach; ma_ca_biet TEXT UNIQUE; vi_tri_ke TEXT; tinh_trang TEXT CHECK TOT/HONG/MAT; trang_thai TEXT CHECK RANH/DANG_MUON/GIU_CHO/HONG.

Tuần 11 chỉ cho mượn khi tinh_trang=TOT và trang_thai=RANH. Đổi trạng thái lưu thông do service loans của người 2 sở hữu sau tích hợp; API danh mục không tùy tiện ghi đè bản đang mượn.

## 5. ban_doc

id UUID; nguoi_dung_id UUID UNIQUE FK nguoi_dung; ma_the TEXT UNIQUE; ho_ten TEXT; lop_khoa TEXT; han_the DATE; trang_thai TEXT CHECK BINH_THUONG/KHOA; gioi_han_muon INTEGER CHECK >0, mặc định demo 3.

han_the còn hạn nếu >= ngày hiện tại tại Asia/Ho_Chi_Minh. Số phiếu đang mở là số phieu_muon có trang_thai=DANG_MUON và tra_luc IS NULL. Không xóa người dùng/bạn đọc nếu đang được lịch sử tham chiếu.

## 6. phieu_muon

id UUID; ban_doc_id UUID FK ban_doc; ban_sach_id UUID FK ban_sach; muon_luc TIMESTAMPTZ; han_tra TIMESTAMPTZ; tra_luc TIMESTAMPTZ nullable; so_lan_gia_han INTEGER >=0 mặc định 0; trang_thai TEXT CHECK DANG_MUON/DA_TRA.

CHECK han_tra > muon_luc. DANG_MUON đi với tra_luc IS NULL, DA_TRA đi với tra_luc IS NOT NULL; tra_luc không trước muon_luc. Tạo partial UNIQUE index trên ban_sach_id WHERE trang_thai='DANG_MUON' AND tra_luc IS NULL, để không có hai phiếu đang mở cùng một bản.

Tạo index cho ban_doc_id/trang_thai và han_tra. Thao tác cho mượn phải khóa hàng ban_doc trước, rồi ban_sach; mọi truy vấn transaction dùng cùng client. Vi phạm UNIQUE do cạnh tranh được ánh xạ thành 409, không trả raw SQL.

## 7. bieu_phat

id UUID; so_ngay_tu INTEGER >=1; so_ngay_den INTEGER nullable, nếu có phải >= so_ngay_tu; tien_moi_ngay INTEGER >=0.

Nhóm chưa triển khai tính phạt bậc thang ở tuần 11; thiết kế bảng sẵn, không coi việc có bảng là đã làm xong tính phạt.

## 8. phat

id UUID; phieu_muon_id UUID UNIQUE FK phieu_muon; so_ngay_qua_han INTEGER >=0; so_tien INTEGER >=0; da_thu BOOLEAN mặc định false; thu_luc TIMESTAMPTZ nullable.

Tuần 11 dư nợ là SUM(so_tien) của các hàng da_thu=false theo phieu_muon.ban_doc_id. API bạn đọc trả giá trị 0 nếu không có nợ. Khoản đã thu không tính vào dư nợ.

## 9. dat_truoc

id UUID; sach_id UUID FK sach; ban_doc_id UUID FK ban_doc; ban_sach_id UUID nullable FK ban_sach; dat_luc TIMESTAMPTZ; trang_thai TEXT CHECK DANG_CHO/DANG_GIU/DA_NHAN/DA_HUY/HET_HAN; giu_den_luc TIMESTAMPTZ nullable.

DANG_GIU cần ban_sach_id và giu_den_luc. Chỉ bản của nhan đề tương ứng được giữ; nghiệp vụ mốc sau cần kiểm tra ràng buộc này. Vị trí hàng đợi là giá trị tính từ các lượt DANG_CHO theo dat_luc rồi id, không là cột thu_tu cập nhật thủ công. Đây là lựa chọn thiết kế rõ ràng của nhóm cho gợi ý bảng trong đề.

## 10. nhat_ky_thu_vien

id UUID; loai_su_kien TEXT; ban_doc_id UUID nullable FK ban_doc; ban_sach_id UUID nullable FK ban_sach; phieu_muon_id UUID nullable FK phieu_muon; thoi_diem TIMESTAMPTZ; nguoi_thuc_hien UUID FK nguoi_dung.

Cho mượn ghi loai_su_kien=CHO_MUON. Ghi nhật ký trong cùng transaction tạo phiếu, không ghi thành công trước khi nghiệp vụ có thể commit.

## Seed và ranh giới

Tối thiểu cho bộ demo tuần 11: 20 nhan đề, 40 bản, 10 bạn đọc. Giữ ID/tài khoản trong demo-ids.json. Bạn đọc đầu tiên có thẻ còn hạn, không nợ, chưa có phiếu đang mở; bản BS0001 RANH. BS0002 cùng nhan đề đang được người thứ hai mượn. Chuẩn bị người thẻ hết hạn, khóa, nợ phạt và người sát giới hạn để kiểm thử.

Seed hạn thẻ và phiếu demo theo ngày chạy, không phụ thuộc ngày trong JSON fixture. Fixture chỉ phục vụ hình dạng dữ liệu và giao diện. Mật khẩu fixture là tài khoản demo do người 1 tạo, không phải bí mật hệ thống thật.

Người 1 cung cấp schema.sql + seed.sql và migration khởi đầu có thể dựng lại DB trống; không được tự động xóa DB đang dùng khi backend khởi động. Người 2 thêm migration nếu cần chỉnh index/ràng buộc, giữ tương thích API/ID cũ.
