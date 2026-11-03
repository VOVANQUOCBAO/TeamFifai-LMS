# Backend thư viện dùng chung

Đây là khung thư mục để hai người backend chia việc và commit đúng phạm vi. Chưa có ứng dụng chạy, API, kết nối DB, SQL, dependencies hay kiểm thử được triển khai. Chưa chạy kiểm thử nghiệp vụ.

Dự kiến dùng Node.js 20, Express 4, CommonJS và `pg` theo hợp đồng LIB21-v1. Nhóm cần chốt phiên bản và lockfile khi người 1 tạo ứng dụng. Hai app Android Admin và Flutter Client cùng gọi một backend, dùng một PostgreSQL/Storage trên Supabase; backend dự kiến deploy trên Render. Không tạo server, Pool, auth hoặc DB riêng cho người 2.

## Người sở hữu

| Khu vực | Người phụ trách | Cách phối hợp |
| --- | --- | --- |
| `src/config/`, `src/db/`, `src/middleware/` | Người 1 nền tảng | Cung cấp cấu hình, Pool, transaction helper, xác thực, quyền và xử lý lỗi chung. |
| `src/shared/` | Theo từng file | Người 1 sở hữu `AppError.js`, `response.js`; người 2 sở hữu `circulation.service.js`. Chốt giao diện trước khi tạo hoặc sửa file chung. |
| `src/modules/auth/` | Người 1 | Đăng ký, đăng nhập, thông tin người dùng và quản lý tài khoản nhân viên. |
| `src/modules/catalog/` | Người 1 | Phân loại, nhan đề và bản sách; gọi service lưu thông khi cần. |
| `src/modules/readers/` | Người 1 | Bạn đọc, thẻ, hạn thẻ và giới hạn; gọi service lưu thông khi đổi điều kiện mượn. |
| `src/modules/storage/` | Người 1 | Upload ảnh bìa và tích hợp Storage. |
| `src/modules/loans/` | Người 2 nghiệp vụ | Mượn, trả, gia hạn và phiếu cá nhân. |
| `src/modules/reservations/` | Người 2 | Đặt trước, giữ 48 giờ, hủy và hoàn tác. |
| `src/modules/fines/` | Người 2 | Biểu phạt phiên bản, tính và thu phạt. |
| `src/modules/reports/` | Người 2 | Bốn báo cáo theo định nghĩa đã chốt. |
| `src/modules/notifications/`, `src/jobs/` | Người 2 | Thông báo, hết giữ và nhắc hạn; người 1 hỗ trợ scheduler/môi trường. |
| `database/` | Người 1 tổng hợp; cả hai đóng góp theo phạm vi | Người 1 cấp số migration trước; người 2 gửi migration/seed nghiệp vụ để review. |
| `tests/` | Người viết chức năng tương ứng | Ca liên phần có một owner, cùng người liên quan review. |
| `scripts/` | Người 1 điều phối | Chỉ thêm script khi có nhu cầu thực và đã review tác động DB. |

## Cấu trúc module

Mỗi module dùng thư mục phẳng. Chỉ tạo file khi có triển khai thật:

- `routes.js`: Express Router của module, xuất bằng CommonJS; không tự `listen`.
- `controller.js`: đọc HTTP input và trả response.
- `service.js`: kiểm tra và điều phối nghiệp vụ.
- `repository.js`: truy vấn SQL; nhận cùng DB client khi ở trong transaction.
- `validation.js`: kiểm tra request khi module cần.

Không cần tạo đủ năm file cho một module đơn giản. `.gitkeep` chỉ giúp Git lưu thư mục đang rỗng; xóa nó khi thư mục có file thật. Không dùng file placeholder làm bằng chứng chức năng đã hoàn thành.

## File chung dự kiến

Người 1 sẽ tạo `src/app.js` để dựng và mount router, `src/index.js` để khởi động server. Chưa có hai file này trong scaffold.

Người 1 dự kiến cung cấp `src/db/index.js` với một Pool và helper `trongGiaoDich`, `src/middleware/auth.js`, `src/middleware/error.js`, `src/shared/AppError.js`, `src/shared/response.js`. Mọi truy vấn của cùng transaction phải dùng cùng client; không xen `pool.query` vào transaction.

Người 2 dự kiến tạo `src/shared/circulation.service.js` với giao diện đã chốt cùng người 1:

- `dongBoGiuCho(client, sachId, now)`
- `layKhaDung(client, sachIds, banDocId, now)`
- `kiemTraSuaBan(client, banSachId, thayDoi)`

Đây là vị trí và tên dự kiến, chưa phải các hàm đã được triển khai. Service dùng client được truyền vào; không tạo Pool hoặc transaction lồng độc lập.

## Quy tắc phối hợp

1. Dùng cùng hợp đồng trong `docs/` ở repo, thống nhất tên field/enum, quyền và response trước khi code. API/JSON nháp phải ghi rõ trạng thái thiết kế.
2. Mọi thay đổi trạng thái lưu thông đi qua service nghiệp vụ. CRUD metadata không được tự ghi `DANG_MUON` hoặc `GIU_CHO` để sửa tồn kho.
3. Hai người backend cùng tuân cơ chế transaction/khóa đã chốt, kể cả job và thao tác đổi thẻ/tình trạng bản ảnh hưởng quyền mượn. Không gọi mạng ngoài khi giữ transaction.
4. Người 1 cấp số migration; người 2 không tự lấy số trùng hoặc sửa migration đã được áp dụng. Xem `database/README.md`.
5. Mỗi người tự commit phần mình bằng tài khoản GitHub riêng. PR ghi phần thay đổi, ảnh hưởng API/DB, kiểm tra thật đã chạy và việc còn thiếu.
6. Ghi `CHƯA CHẠY` khi chưa thực thi kiểm tra; không tạo test rỗng hoặc số liệu giả để ghi nhận hoàn thành.

## Cấu hình và trạng thái chạy

`.env.example` chỉ liệt kê tên biến dự kiến, tất cả giá trị đang để trống. Khi bắt đầu triển khai, người 1 chốt biến thực sự cần dùng, sao chép sang file môi trường cục bộ và điền giá trị qua kênh phù hợp. Không commit `.env`, chuỗi kết nối, JWT secret, service key hoặc job secret. Không đưa service key/job secret vào hai app.

Scaffold chưa có `package.json`, lockfile, entry point hoặc lệnh chạy. Các lệnh cài đặt, migration, seed, kiểm thử và deploy chỉ được ghi là chạy được sau khi được triển khai và xác minh. Chưa cài dependencies hay gọi dịch vụ ngoài.
