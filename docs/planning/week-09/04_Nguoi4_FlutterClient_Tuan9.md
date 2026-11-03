# Tuần 9  Flutter Client

Trong tuần 9, cung cấp phần phân tích Client để nhóm chốt ba bản nháp use case, sơ đồ CSDL và bảng phân công. Chốt luồng bạn đọc, màn hình, dữ liệu và sự phối hợp nhắc hạn trước khi triển khai; chưa phải hoàn thành app.

## Phạm vi tuần này

**Nộp giảng viên theo D.3:** bản nháp sơ đồ use case, sơ đồ cơ sở dữ liệu và bảng phân công của cả nhóm. API/JSON mẫu, wireframe, quy tắc nghiệp vụ và checklist kỹ thuật là tài liệu hỗ trợ nội bộ được đề xuất để thống nhất thiết kế.

Các ô dưới đây đang để trống để người thực hiện tự cập nhật khi có đầu ra. Skeleton hoặc chạy thử chỉ làm nếu đủ thời gian sau khi hoàn thành phần phân tích. Chưa coi code, deploy, kiểm thử hay APK là đã xong chỉ vì đã có kế hoạch.

## Công việc cần làm

### P4-W9-01

* \[ ] **Việc làm:** Đọc yêu cầu, xác định tác nhân và góp use case Client cho bản nộp tuần 9.
* **Đầu ra:** Ma trận chức năng của BAN\_DOC theo thẻ CHO\_DUYET, BINH\_THUONG và KHOA; danh sách use case đăng ký/đăng nhập, tra cứu/xem kệ, đặt trước, xem đang mượn, gia hạn, xem lịch sử/phạt và nhắc hạn. Mô tả ngắn các use case được nhóm trưởng giao với tiền điều kiện, luồng chính, lỗi và hậu điều kiện; ghi phần việc cá nhân cho bảng phân công.
* **Tiêu chí hoàn thành:** Người có thẻ CHO\_DUYET được tra cứu/xem thẻ nhưng chưa đặt/gia hạn; thẻ hợp lệ vẫn bị chặn khi backend xác định có nợ, quá hạn hoặc điều kiện khác. Backend kiểm quyền sở hữu dữ liệu. Người 2 xác nhận, nhận phần Client để ghép use case/phân công chung.
* **Phối hợp:** Người 2 là nhóm trưởng và người chốt nghiệp vụ; người 1 xác nhận đăng ký, hồ sơ và trạng thái thẻ.

### P4-W9-02

* \[ ] **Việc làm:** Lập danh mục màn hình và sơ đồ điều hướng cho sản phẩm cuối.
* **Đầu ra:** Danh mục dự kiến gồm đăng nhập/đăng ký, tra cứu, chi tiết nhan đề, đang mượn, đặt trước của tôi, lịch sử, tiền phạt, thông báo và hồ sơ/thẻ. Đánh dấu tra cứu và đang mượn là hai ListView.builder; vị trí Scaffold + NavigationBar hoặc Drawer, Form/validator và Navigator có kết quả trả về.
* **Tiêu chí hoàn thành:** Thiết kế bao phủ ít nhất 6 màn hình về sau, không dùng màn đăng nhập để thay thế chức năng nghiệp vụ. Chọn đăng nhập và danh sách sách làm hai màn ưu tiên API thật cho tuần 11; tuần 9 chỉ cần phân tích và chốt luồng.
* **Phối hợp:** Người 2 duyệt phạm vi và tiến độ; người 1/2 xác nhận nguồn dữ liệu.

### P4-W9-03

* \[ ] **Việc làm:** Vẽ wireframe đơn giản cho hai luồng trọng tâm.
* **Đầu ra:** Wireframe login → tìm sách → chi tiết/kệ → đặt trước → kết quả/vị trí hàng đợi, và đang mượn → xem điều kiện → gia hạn → tải hạn mới. Chú thích loading, rỗng, lỗi, thử lại, thẻ chờ duyệt và trường hợp server từ chối nghiệp vụ.
* **Tiêu chí hoàn thành:** Người khác nhìn biết người dùng nhập gì, xem gì, bấm gì và quay lại đâu. Nút đặt/gia hạn dùng điều kiện backend; không tự cộng ngày hoặc tính tồn kho. Chưa cần giao diện chạy hoặc API thật trong tuần 9.
* **Phối hợp:** Người 2 kiểm nhánh đặt/gia hạn; người 3 đối chiếu việc Android cho mượn/trả làm thay đổi dữ liệu Flutter.

### P4-W9-04

* \[ ] **Việc làm:** Đối chiếu màn hình với API, JSON và dữ liệu cần có trong ERD.
* **Đầu ra:** Bảng màn hình–API gồm auth, GET /sach, GET /sach/:id, GET /ban-doc/toi, GET /ban-doc/toi/dang-muon, GET /ban-doc/toi/lich-su, đặt trước, gia hạn, phạt và thông báo theo hợp đồng chung /api/v1. Ghi method/body, field hiển thị, quyền, ví dụ thành công/rỗng/lỗi và bên cung cấp. Liệt kê field còn thiếu để người 1/2 bổ sung thiết kế.
* **Tiêu chí hoàn thành:** Đã đọc mẫu data/meta/error; UUID là string; tiền là số nguyên; hanTra/hanThe là ngày YYYY-MM-DD, khác timestamp UTC có Z. Đã nêu trường null như ảnh, traLuc, giuDenLuc, banSachId khi chưa cấp giữ. Thiết kế phân biệt 401 hết phiên, 403 thiếu quyền và 409 xung đột nghiệp vụ; không tự phát minh endpoint hoặc sửa ERD riêng.
* **Phối hợp:** Người 1 giao auth/catalog/profile và tổng hợp ERD/API; người 2 giao phiếu/đặt/phạt/thông báo. Hai backend xác nhận bảng trước khi viết DTO.

### P4-W9-05

* \[ ] **Việc làm:** Chốt thiết kế hủy và Hoàn tác lượt đặt trước bằng thao tác backend thật.
* **Đầu ra:** Luồng CHO → xác nhận Hủy → DELETE /dat-truoc/:id → SnackBar Hoàn tác → PATCH /dat-truoc/:id/khoi-phuc → tải lại. Ghi cửa sổ theo hoanTacDenLuc, lượt GIU\_CHO không hủy từ Client ở v1, trạng thái khi timeout/409 và khi đổi tài khoản.
* **Tiêu chí hoàn thành:** Nhóm xác nhận phương án hoàn tác 10 giây và xếp cuối hàng là đề xuất trong DOCX. Wireframe không hứa giữ vị trí cũ; không mô tả xóa/thêm item cục bộ là đã đổi dữ liệu. Retry cùng ý định giữ cùng Idempotency-Key; chưa cần triển khai endpoint hay SnackBar chạy thật tuần 9.
* **Phối hợp:** Người 2 chốt điều kiện hủy/khôi phục; người 3 xác nhận Android sẽ thấy trạng thái nào.

### P4-W9-06

* \[ ] **Việc làm:** Viết phương án phối hợp nhắc hạn giữa backend và thiết bị.
* **Đầu ra:** Một trang thiết kế: backend ghi thông báo trước hạn 2 ngày/quá hạn và cung cấp dữ liệu; Flutter hiển thị in-app và lên lịch local cho phiếu đã đồng bộ. Chọn giờ nhắc đề xuất 08:00 Việt Nam; mô tả sync khi login/foreground/refresh, hủy lịch khi trả/gia hạn/đăng xuất/đổi tài khoản và xử lý quyền notification bị từ chối.
* **Tiêu chí hoàn thành:** Phân rõ trách nhiệm người 2 và người 4; local chỉ biết dữ liệu đã tải. Ghi giới hạn nếu Android trả/gia hạn lúc Flutter đang tắt: lịch cũ chỉ được sửa sau lần đồng bộ, không hứa cập nhật tức thì khi chưa có push. Tuần 9 chỉ thiết kế; chưa chọn bừa phiên bản plugin hoặc đánh dấu đã thử nhắc trên máy thật.
* **Phối hợp:** Người 2 chốt dữ liệu, thời điểm và job; người 3 cung cấp tình huống trả/gia hạn từ Android; người 1 xác nhận API và cấu hình chung.

### P4-W9-07

* \[ ] **Việc làm:** Kiểm chéo luồng xuyên hai app và bàn giao phần phân tích cho nhóm trưởng.
* **Đầu ra:** Kịch bản thiết kế Android thêm bản → Flutter thấy; Android cho mượn → đúng bạn đọc thấy phiếu; Flutter gia hạn → Android thấy hạn; Android trả → Flutter bỏ phiếu đang mượn và cập nhật nhắc sau sync. Danh sách câu hỏi/thiếu API có người xử lý, cùng danh sách việc cá nhân tuần 10–11.
* **Tiêu chí hoàn thành:** Use case, màn hình và field khớp bản hợp đồng nhóm chốt. Mỗi điểm chưa rõ có owner và hạn phản hồi. Người 2 đã nhận tài liệu để ghép ba bản nháp tuần 9. Đây là walkthrough thiết kế, không ghi là ca tích hợp thực tế đã đạt.
* **Phối hợp:** Người 2 tổng hợp và nộp; người 1 kiểm API/ERD; người 3 walkthrough phần Admin.

### P4-W9-08

* \[ ] **Việc làm:** Nếu còn thời gian, chuẩn bị project skeleton và môi trường Flutter.
* **Đầu ra:** Tùy chọn: tạo client-flutter theo stack của đề, ghi flutter --version, khung thư mục network/session/models/repositories/features, cấu hình API\_BASE\_URL và ghi chú dự kiến http hoặc dio/shared\_preferences. Có thể dùng mock repository tách biệt từ JSON chung để thử luồng.
* **Tiêu chí hoàn thành:** Ba sản phẩm phân tích bắt buộc đã được ưu tiên trước. Nếu có tạo project, ghi đúng lệnh pub get/analyze đã chạy và kết quả; nếu chưa chạy thì ghi CHƯA CHẠY. Không đưa service key, mật khẩu hoặc UUID fixture vào widget; không tự xây backend/DB riêng.
* **Phối hợp:** Người 1 cấp cấu hình/API khi có; người 2 xác nhận JSON và ưu tiên công việc.

## Bộ bàn giao cuối tuần

* \[ ] Phần use case và ma trận quyền/thẻ Client gửi người 2 để ghép bản nháp chung.
* \[ ] Danh mục màn hình, sơ đồ điều hướng và wireframe hai luồng ưu tiên; nêu rõ đây là thiết kế phạm vi cuối kỳ.
* \[ ] Bảng màn hình–API, danh sách field JSON/null/ngày/lỗi cần có và câu hỏi gửi người 1/2 để phản ánh vào API/ERD.
* \[ ] Thiết kế hủy/Hoàn tác và phương án nhắc hạn/sync; đánh dấu chính sách đề xuất chờ nhóm chốt.
* \[ ] Danh sách việc cá nhân tuần 10–11 và các commit thật của tuần 9; nhóm trưởng điền tên/thời hạn thực tế.

## Thứ tự thực hiện

1. Đầu tuần hoặc buổi đầu còn lại: đọc hướng dẫn, làm P4-W9-01/02, ghi câu hỏi và phác hai luồng wireframe.
2. Giữa tuần hoặc buổi tiếp theo: hoàn thiện P4-W9-03/04/05/06; họp với người 1/2 để xác nhận field, quyền và quy tắc, walkthrough với người 3.
3. Cuối tuần, trước hạn nhóm trưởng đặt: cập nhật theo quyết định, hoàn thành P4-W9-07, commit và gửi review; chỉ làm skeleton P4-W9-08 nếu các phần phân tích đã đủ. Điền ngày giờ theo hạn LMS, không giả định một ngày nộp.

Đây là lịch tương đối đầu–giữa–cuối tuần. Nếu tuần đã bắt đầu, giữ thứ tự công việc và điền hạn thực tế theo LMS cùng nhóm trưởng.

## Gợi ý commit có ý nghĩa

Toàn kỳ cần ít nhất 15 commit có ý nghĩa mỗi người, theo E.3. Ba ví dụ dưới đây là gợi ý cho phần việc thật tuần 9, không phải định mức riêng của giảng viên và không yêu cầu tạo commit giả cho đủ số. Mỗi người tự commit phần mình bằng tài khoản riêng.

* docs(flutter): phân tích use case bạn đọc và trạng thái thẻ
* docs(flutter): thiết kế điều hướng wireframe và bảng màn hình API
* docs(flutter): chốt hoàn tác nhắc hạn và sửa thiết kế sau review

## Điểm cần tránh

* Để backend quyết định quyền gia hạn, điều kiện đặt, hạn và tiền phạt. Dữ liệu đang hiển thị có thể cũ; không biến nút đang bật thành bảo đảm mutation sẽ thành công.
* Không nhầm DATE với timestamp, null với chuỗi null, một trang sách với toàn bộ tồn kho; không lấy dữ liệu cá nhân bằng banDocId tự nhập.
* Không coi xóa/hoàn tác cục bộ hoặc lịch nhắc chưa sync là trạng thái đã được backend xác nhận. Không ghi đã test máy thật khi mới phân tích hoặc chạy giả.

## Việc triển khai ở các tuần sau

* Triển khai đầy đủ tối thiểu 6 màn, đăng nhập/token, hai ListView.builder API thật và toàn bộ trạng thái lỗi theo tiến độ sau tuần 9.
* Mốc tuần 11: cùng người 1/2 hoàn thiện đăng nhập và danh sách sách dùng backend đã deploy; yêu cầu 2 màn API thật mỗi app thuộc tuần 11.
* Code hủy/Hoàn tác, gia hạn, lịch local notification và thử quyền/background trên máy thật; hoàn thiện sau khi API và chính sách được chốt.
* Build APK release, hoàn thành hồ sơ kiểm thử thực tế và video/báo cáo cuối kỳ theo các mốc sau. Không cần 15 commit ngay tuần 9; quy định là ít nhất 15 commit có ý nghĩa rải đều toàn kỳ cho mỗi người.


## Tài liệu tham chiếu

Xem [danh mục nguồn của nhóm](../../references.md). Các checklist là kế hoạch tuần 9; đánh dấu hoàn thành khi có đầu ra thực tế.
