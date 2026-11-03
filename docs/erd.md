# Thiết kế cơ sở dữ liệu

**Chưa có ERD hoàn chỉnh hoặc schema chạy được.** P1 lưu bản nguồn và bản xuất sơ đồ ở `docs/design/erd/`; P2 góp bảng nghiệp vụ.

| Phần | Thực thể dự kiến | Người thiết kế |
|---|---|---|
| Nền tảng | nguoi_dung, phan_loai, sach, ban_sach, ban_doc | P1 |
| Nghiệp vụ | phieu_muon, dat_truoc, bieu_phat, phat, nhat_ky_thu_vien, thong_bao, yeu_cau_lap | P2 |

ERD cần PK/FK, quan hệ, nullable, unique và ràng buộc ngăn dữ liệu lưu thông mâu thuẫn. Phân biệt tài khoản với hồ sơ bạn đọc; phiếu gắn bản sách, lượt đặt gắn nhan đề.

SQL thật đặt tại `backend/database/`; JSON nhận dạng demo chung ở `docs/fixtures/`. Bộ dữ liệu cuối kỳ cần ít nhất 300 nhan đề, 700 bản, 200 bạn đọc, 400 phiếu và 40 lượt đặt theo đề. Sườn hiện chưa có các dữ liệu này.
