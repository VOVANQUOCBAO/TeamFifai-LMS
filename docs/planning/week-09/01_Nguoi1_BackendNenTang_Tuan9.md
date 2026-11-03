# Tuần 9  Backend nền tảng


Trong tuần 9, bạn cùng nhóm chốt phân tích và chuẩn bị bản nháp để nhóm trưởng nộp: sơ đồ use case, sơ đồ cơ sở dữ liệu và bảng phân công. Bạn chịu trách nhiệm chính về thiết kế dữ liệu nền tảng và tổng hợp ERD với người 2; nhóm trưởng là người 2, phụ trách điều phối và xác nhận chung. Các ghi chú API, JSON, kiến trúc và repo giúp nhóm triển khai sau tuần 9, không thay thế ba đầu ra bắt buộc và không đòi hỏi backend đã hoàn thành.

## Phạm vi tuần này

**Nộp giảng viên theo D.3:** bản nháp sơ đồ use case, sơ đồ cơ sở dữ liệu và bảng phân công của cả nhóm. API/JSON mẫu, wireframe, quy tắc nghiệp vụ và checklist kỹ thuật là tài liệu hỗ trợ nội bộ được đề xuất để thống nhất thiết kế.

Các ô dưới đây đang để trống để người thực hiện tự cập nhật khi có đầu ra. Skeleton hoặc chạy thử chỉ làm nếu đủ thời gian sau khi hoàn thành phần phân tích. Chưa coi code, deploy, kiểm thử hay APK là đã xong chỉ vì đã có kế hoạch.

## Công việc cần làm

### P1-W9-01

* \[ ] **Việc làm:** Đọc đề và đánh dấu yêu cầu thuộc nền tảng: đăng nhập, phân quyền, danh mục, nhan đề, bản sách, bạn đọc, thẻ và ảnh bìa. Trao đổi ranh giới với người 2 trước khi chia đầu việc.
* **Đầu ra:** Danh sách phạm vi người 1 và các điểm giao tiếp với người 2; nội dung để điền vào bảng phân công bắt buộc.
* **Tiêu chí hoàn thành:** Nêu rõ người 1 làm auth/catalog/readers/storage và hạ tầng chung; người 2 làm mượn trả, đặt trước, phạt, báo cáo, thông báo và chuyển trạng thái lưu thông. Không có chức năng chưa rõ người chịu trách nhiệm.
* **Phối hợp:** Người 2 là nhóm trưởng xác nhận; người 3 và 4 góp ý nhu cầu sử dụng.

### P1-W9-02

* \[ ] **Việc làm:** Góp các use case nền tảng vào sơ đồ chung và mô tả quyền ADMIN, THU\_THU, BAN\_DOC; phân biệt đăng ký bạn đọc với tạo tài khoản nhân viên.
* **Đầu ra:** Phần use case nền tảng và bảng quyền ngắn để nhóm trưởng ghép vào sơ đồ use case bắt buộc.
* **Tiêu chí hoàn thành:** Mỗi tác nhân có chức năng phù hợp; ADMIN có quyền quản lý nhân viên khác THU\_THU; việc mượn trả/đặt trước được người 2 mô tả và nối với dữ liệu nền tảng, không bị viết lại thành nghiệp vụ khác.
* **Phối hợp:** Người 2 tổng hợp sơ đồ; người 3 xác nhận thao tác quản trị; người 4 xác nhận đăng ký, tra cứu và hồ sơ.

### P1-W9-03

* \[ ] **Việc làm:** Vẽ ERD nháp cho nguoi\_dung, phan\_loai, sach, ban\_sach, ban\_doc; nhận thiết kế bảng nghiệp vụ từ người 2 rồi tổng hợp thành một ERD của cả hệ thống.
* **Đầu ra:** Một sơ đồ cơ sở dữ liệu chung có bản nguồn chỉnh sửa và bản xuất dễ đọc; đây là đầu ra bắt buộc tuần 9.
* **Tiêu chí hoàn thành:** Có ít nhất 8 bảng được thiết kế với PK/FK và lực lượng quan hệ rõ; thể hiện một nhan đề có nhiều bản sách, tài khoản khác hồ sơ bạn đọc, phiếu mượn gắn bản sách còn đặt trước gắn nhan đề. Đây là thiết kế nháp, chưa cần tạo đủ bảng thật trong DB tuần này.
* **Phối hợp:** Người 2 chịu trách nhiệm thiết kế bảng nghiệp vụ và cùng review toàn bộ quan hệ.

### P1-W9-04

* \[ ] **Việc làm:** Ghi từ điển dữ liệu tối thiểu và những ràng buộc cần thống nhất: định danh, mã sách/bản/thẻ, enum, ngày và thời điểm, quyền sửa metadata và quyền chuyển trạng thái lưu thông.
* **Đầu ra:** Ghi chú kèm ERD về trường chính, kiểu dự kiến, nullable, unique và người chịu trách nhiệm thay đổi dữ liệu; tài liệu hỗ trợ nội bộ.
* **Tiêu chí hoàn thành:** Nhóm thống nhất UUID dạng chuỗi, DB snake\_case và JSON camelCase; hanThe/hanTra là ngày, giuDenLuc là thời điểm; email, maCaBiet và maThe có ràng buộc duy nhất. Người 1 không tự sửa DANG\_MUON/GIU\_CHO; các quyết định chưa chốt được ghi lại và giao người 2 điều phối.
* **Phối hợp:** Người 2 xác nhận trạng thái và ràng buộc nghiệp vụ; người 3 và 4 xác nhận cách đọc dữ liệu.

### P1-W9-05

* \[ ] **Việc làm:** Soạn danh sách API nền tảng dự kiến cùng một vài JSON mẫu cho đăng nhập, sách, bản sách và thẻ; cùng người 2 thống nhất cách trả dữ liệu và lỗi.
* **Đầu ra:** Bản nháp API/JSON dùng chung trong repo, ghi rõ là hợp đồng dự kiến hỗ trợ nội bộ, không phải API đã chạy.
* **Tiêu chí hoàn thành:** Người 3 và 4 đọc mẫu và chỉ ra được tên trường, kiểu, null, data/meta/error, phân trang và vai trò gọi. Chốt được mẫu có ảnh/không có ảnh và thẻ chờ duyệt. Không cần hoàn thiện toàn bộ OpenAPI hay triển khai endpoint production trong tuần 9.
* **Phối hợp:** Người 2 bổ sung API nghiệp vụ; người 3 và 4 review mẫu để làm model và mock sau khi thống nhất.

### P1-W9-06

* \[ ] **Việc làm:** Điền phần việc người 1 vào bảng phân công và đề xuất thứ tự phụ thuộc để hai app không phải chờ toàn bộ backend mới bắt đầu.
* **Đầu ra:** Phần người 1 trong bảng phân công bắt buộc: nhiệm vụ, đầu ra, mốc dự kiến, người phối hợp và người review.
* **Tiêu chí hoàn thành:** Có đầu việc tách được thành phần nhỏ: hợp đồng, khung chung, auth, danh mục, bạn đọc, DB, deploy và bàn giao. Mốc tuần 9 ghi phân tích; mốc tuần 11 mới yêu cầu backend deploy, ít nhất 3 API thật và 2 màn hình mỗi app theo đề. Nhóm trưởng xác nhận phân công toàn nhóm.
* **Phối hợp:** Người 2 là nhóm trưởng; người 3 và 4 xác nhận đầu vào và mốc cần nhận.

### P1-W9-07

* \[ ] **Việc làm:** Viết phương án kiến trúc và chuẩn bị chỗ lưu tài liệu chung trong repo đã đăng ký. Nếu repo chưa sẵn sàng, báo nhóm trưởng xử lý ngay; chỉ dựng khung thư mục khi không làm chậm ba tài liệu phải nộp.
* **Đầu ra:** Ghi chú kiến trúc và README ban đầu: một backend Node.js/Express dùng pg, một PostgreSQL Supabase, Storage ảnh và kế hoạch chạy backend trên Render; thư mục backend/admin-android/client-flutter/docs khi phù hợp.
* **Tiêu chí hoàn thành:** Bốn người biết dùng cùng một hệ thống, người 1 và 2 không dựng hai server/DB. Ghi tên biến môi trường cần dùng và cách phân công migration dự kiến; không điền secret thật hay URL deploy giả. Việc tạo khung chạy thử chỉ là phần thêm nếu đủ thời gian.
* **Phối hợp:** Người 2 xác nhận cấu trúc repo và quyền cộng tác; người 3 và 4 nhận vị trí tài liệu chung.

### P1-W9-08

* \[ ] **Việc làm:** Tổ chức một lượt review kỹ thuật ngắn với người 2, 3 và 4 rồi bàn giao phiên bản nháp cuối tuần cho nhóm trưởng tổng hợp và nộp.
* **Đầu ra:** Bộ nháp use case, ERD và bảng phân công đã đối chiếu; kèm danh sách điểm cần hỏi giảng viên và đường dẫn commit tài liệu.
* **Tiêu chí hoàn thành:** Tên tác nhân, chức năng và thực thể khớp giữa ba tài liệu; người 2 đã nhận phần của bạn; mọi chỗ chưa chốt có người xử lý và hạn dự kiến. Không đánh dấu hoàn thành code, deploy hay kiểm thử nếu mới có thiết kế.
* **Phối hợp:** Người 2 duyệt và nộp bản chung; người 3 và 4 xác nhận thiết kế đủ để triển khai phần mình.

## Bộ bàn giao cuối tuần

* \[ ] Bắt buộc theo D.3 tuần 9: góp phần use case nền tảng vào sơ đồ use case chung của nhóm.
* \[ ] Bắt buộc theo D.3 tuần 9: bàn giao ERD nháp toàn hệ thống đã tổng hợp với người 2, có bản nguồn chỉnh sửa và bản xuất dễ đọc.
* \[ ] Bắt buộc theo D.3 tuần 9: bàn giao phần nhiệm vụ, đầu ra và mốc của người 1 trong bảng phân công để nhóm trưởng ghép.
* \[ ] Hỗ trợ nội bộ: ghi chú từ điển dữ liệu, ràng buộc và điểm giao tiếp nền tảng với nghiệp vụ.
* \[ ] Hỗ trợ nội bộ: danh sách API nền tảng dự kiến và JSON mẫu đủ để hai app thống nhất model.
* \[ ] Hỗ trợ nội bộ: README/ghi chú kiến trúc, cách lưu tài liệu trong repo và danh sách câu hỏi còn mở; khung thư mục nếu đã có thời gian làm.

## Thứ tự thực hiện

1. Đầu tuần: đọc đề, xác nhận ranh giới với người 2, liệt kê use case nền tảng và dựng ERD ban đầu; điền phần phân công của mình.
2. Giữa tuần: nhận bảng nghiệp vụ của người 2 để ghép ERD; review quan hệ, quyền và tên trường cùng hai người làm app. Soạn API/JSON hỗ trợ sau khi ba tài liệu bắt buộc đã có bản đầu.
3. Cuối tuần: sửa các điểm không khớp giữa use case, ERD và phân công; gửi phiên bản nháp có commit cho nhóm trưởng, chốt câu hỏi cần hỏi giảng viên. Chỉ dựng khung kỹ thuật nếu còn thời gian.

Đây là lịch tương đối đầu–giữa–cuối tuần. Nếu tuần đã bắt đầu, giữ thứ tự công việc và điền hạn thực tế theo LMS cùng nhóm trưởng.

## Gợi ý commit có ý nghĩa

Toàn kỳ cần ít nhất 15 commit có ý nghĩa mỗi người, theo E.3. Ba ví dụ dưới đây là gợi ý cho phần việc thật tuần 9, không phải định mức riêng của giảng viên và không yêu cầu tạo commit giả cho đủ số. Mỗi người tự commit phần mình bằng tài khoản riêng.

* docs(p1): add platform use cases and role matrix — bổ sung use case auth/catalog/readers và bảng quyền có nội dung để review.
* docs(p1): draft shared library ERD and data constraints — bổ sung ERD chung sau khi nhận bảng nghiệp vụ, kèm các quan hệ và ràng buộc chính.
* docs(p1): revise API examples and platform task plan after review — ghi nhận sửa đổi thực tế từ người 2, 3, 4 và cập nhật phần phân công.

## Điểm cần tránh

* Dành cả tuần viết code/deploy rồi chưa có đủ use case, ERD và phân công để nộp. Ưu tiên hoàn thành ba bản nháp trước.
* Tự thiết kế lại mượn trả hoặc sửa trạng thái lưu thông thay người 2, gây hai cách hiểu dữ liệu. Chốt ranh giới và review ERD chung.
* Coi toàn bộ hướng dẫn triển khai dài hạn là việc phải xong tuần 9, hoặc mặc định người 1 làm nhóm trưởng. Tuần này người 1 tổng hợp kỹ thuật, người 2 điều phối nhóm.

## Việc triển khai ở các tuần sau

* Hoàn thiện toàn bộ Express app, auth, CRUD, middleware và helper transaction: chuyển sang kế hoạch triển khai sau khi chốt phân tích.
* Deploy production và có đủ API thật: chuẩn bị để đạt mốc tuần 11, không đặt làm điều kiện hoàn tất tuần 9.
* Schema/migrations/seed chạy đầy đủ cùng 300 nhan đề, 700 bản và 200 bạn đọc: thiết kế yêu cầu trước, triển khai và kiểm chứng ở các tuần sau.
* Storage upload, health kiểm DB, OpenAPI đầy đủ và bộ kiểm thử thực thi: có thể ghi kế hoạch và đầu vào tuần 9; chưa cần hoàn tất trong tuần này.
* Báo cáo cuối kỳ, APK release, video demo và minh chứng kiểm thử đầy đủ: phân công người thực hiện ngay, hoàn thiện theo các mốc sau.


## Tài liệu tham chiếu

Xem [danh mục nguồn của nhóm](../../references.md). Các checklist là kế hoạch tuần 9; đánh dấu hoàn thành khi có đầu ra thực tế.
