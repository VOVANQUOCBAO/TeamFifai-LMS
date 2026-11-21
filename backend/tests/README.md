# Kiểm thử backend

Suite tuần 11 tại [`integration/platform.test.js`](integration/platform.test.js) dùng Node.js test runner, HTTP thật qua cổng loopback và PostgreSQL thật. Chạy schema và seed trên database thử nghiệm trống, đặt `TEST_DATABASE_URL` rồi chạy `npm test` từ `backend/`. [Hướng dẫn chạy](../README.md) và [kết quả bàn giao](../../handoff/member1.md).

| Thư mục | Loại kiểm tra dự kiến |
| --- | --- |
| `unit/` | Hàm thuần như tính ngày Việt Nam, phạt bậc thang; truyền clock cố định khi cần. |
| `integration/` | API, quyền, ownership, repository và transaction trên DB test. |
| `concurrency/` | Hai kết nối/request thật tranh cùng bản hoặc cùng giới hạn mượn; kiểm tra retry, giữ chỗ và chống trùng. |

Người 1 viết test nền tảng; người 2 viết test nghiệp vụ. Tên file nên có phạm vi, ví dụ `p2-loans-concurrency.test.js`, khi đã thực sự có kiểm thử. Không thêm file test trống để thể hiện tiến độ.

Dùng DB test riêng; không ghi hoặc reset DB tích hợp/public. Mock không chứng minh được ràng buộc DB hay tranh chấp đồng thời. Mọi ca có tác động thời gian dùng clock có thể kiểm soát trong test, không mở API sản phẩm cho đổi giờ server.

Bộ ca toàn kỳ dự kiến theo tài liệu phân công: P1-01 đến P1-07 và P2-01 đến P2-09. Hồ sơ bằng chứng dùng chung nằm trong `docs/` của repo khi được tạo, ghi expected, actual, trạng thái, người chạy, commit và bằng chứng. Chỉ ghi đạt sau khi thực thi; ghi `CHƯA CHẠY` cho ca chưa chạy. Những đường dẫn hoặc ca nêu ở đây chưa phải kết quả kiểm thử.
