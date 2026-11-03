# Dữ liệu và migration backend

Thư mục này mới là nơi phân chia công việc; chưa có schema, migration hoặc seed SQL để chạy.

| Thư mục hoặc file dự kiến | Chủ trì | Mục đích |
| --- | --- | --- |
| `migrations/` | Người 1 quản lý thứ tự; người 1/2 viết phần được cấp | Thay đổi DB tăng dần, có số duy nhất và được review trước khi áp dụng. |
| `seeds/platform/` | Người 1 | Dữ liệu tài khoản demo, phân loại, nhan đề, bản sách, bạn đọc. |
| `seeds/circulation/` | Người 2 | Phiếu mượn, đặt trước, biểu phạt, phạt, nhật ký và thông báo hợp lệ. |
| `fixtures/` | Người 1 tổng hợp; người 2 bổ sung nghiệp vụ | Đầu vào fixture nhỏ, có thể dựng lại để kiểm thử/demo; chỉ dùng dữ liệu giả. |
| `schema.sql` dự kiến | Người 1 tổng hợp | Schema đầy đủ cho DB trống, sau khi thiết kế được chốt. Chưa tạo file này. |

## Nhận số migration

Trước khi tạo file, thống nhất số, owner, mục đích và phụ thuộc với người 1. Tên dự kiến có dạng `NNN_mo_ta.sql`; quy ước tăng số và cơ chế theo dõi migration sẽ được người 1 quyết định khi triển khai. Hai người không tự tạo hai file cùng số. Không sửa migration đã áp dụng để thay cho migration mới.

Người 1 phụ trách bảng nền tảng `nguoi_dung`, `phan_loai`, `sach`, `ban_sach`, `ban_doc`. Người 2 đề xuất DDL cho `phieu_muon`, `dat_truoc`, `bieu_phat`, `phat`, `nhat_ky_thu_vien`, `thong_bao`, `yeu_cau_lap`, cùng unique index và ràng buộc nghiệp vụ. Người 1 review và ghép thành chuỗi chung.

## Seed và fixture

Tạo dữ liệu theo chuỗi nghiệp vụ hợp lệ; không random trạng thái độc lập. Kế hoạch toàn kỳ theo tài liệu phân công là ít nhất 300 nhan đề, 700 bản, 200 bạn đọc, 400 phiếu và 40 lượt đặt; đây chưa phải số liệu đã được tạo trong scaffold.

`fixtures/` giữ đầu vào phục vụ dựng dữ liệu. Danh mục UUID/mã demo dùng chung của các app được người 1 công bố tại `docs/demo-fixtures.json` khi có dữ liệu thật; không tạo một bản công bố cạnh tranh trong thư mục này. Không hardcode UUID demo vào mã sản phẩm.

Tách DB phát triển/kiểm thử khỏi DB tích hợp. Không chạy seed reset phá dữ liệu tích hợp hoặc public. Trước khi có lệnh dựng/verify và kết quả chạy thực tế, mọi mục ở đây đều là kế hoạch.
