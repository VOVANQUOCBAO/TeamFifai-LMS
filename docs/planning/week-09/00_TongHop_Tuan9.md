# Phân công công việc tuần 9 cho nhóm thư viện

Tuần 9 dùng để chốt phân tích và chuẩn bị các đầu vào chung trước khi triển khai. Người 2 làm nhóm trưởng và backend nghiệp vụ. Bản này là kế hoạch giao việc; các mục chưa có bằng chứng vẫn để trạng thái chưa hoàn thành.


## Checklist giao riêng cho từng người

- [Người 1 Backend nền tảng](<01_Nguoi1_BackendNenTang_Tuan9.md>)
- [Người 2 Backend nghiệp vụ và nhóm trưởng](<02_Nguoi2_BackendNghiepVu_NhomTruong_Tuan9.md>)
- [Người 3 Android Admin](<03_Nguoi3_AndroidAdmin_Tuan9.md>)
- [Người 4 Flutter Client](<04_Nguoi4_FlutterClient_Tuan9.md>)

## Kết quả cả nhóm phải nộp

Theo mục D.3 của PDF đề tài và slide lịch nộp, mốc tuần 9 gồm ba sản phẩm bản nháp:

- [ ] Sơ đồ use case chung của hệ thống.
- [ ] Sơ đồ cơ sở dữ liệu có quan hệ giữa các bảng.
- [ ] Bảng phân công cá nhân.

Quy tắc nghiệp vụ, API, JSON mẫu, wireframe và backlog dưới đây là tài liệu làm việc được đề xuất để ba sản phẩm trên nhất quán. Deploy sớm là việc nên thử nếu còn thời gian. Mốc tuần 11 mới yêu cầu backend đã deploy, ít nhất 3 API thật và 2 màn hình mỗi app; không chuyển toàn bộ yêu cầu tuần 11 thành điều kiện hoàn thành tuần 9.

## Giao việc cho bốn người

| Người | Việc trọng tâm tuần 9 | Đầu ra bàn giao cho nhóm | Người kiểm chéo |
|---|---|---|---|
| 1 — Backend nền tảng | Phác thảo kiến trúc, ERD tổng, từ điển dữ liệu, quy ước API và hạ tầng chung | ERD; bảng dữ liệu nền tảng; API auth/danh mục bản nháp; JSON mẫu; ranh giới với người 2 | Người 2; người 3 và 4 kiểm dữ liệu cần hiển thị |
| 2 — Backend nghiệp vụ và nhóm trưởng | Tổng hợp use case, chốt quy tắc mượn/trả/đặt trước/phạt, bổ sung ERD và API nghiệp vụ, chốt phân công | Use case chung; quy tắc nghiệp vụ; mô tả trạng thái; API nghiệp vụ bản nháp; phân công và biên bản chốt | Người 1 kiểm dữ liệu; người 3 và 4 kiểm luồng sử dụng |
| 3 — Android Admin | Phân tích vai trò ADMIN/THU_THU, luồng thủ thư, màn hình và dữ liệu cần gọi | Use case Admin; sơ đồ màn hình; wireframe; bảng màn hình–API; checklist Android | Người 1 và 2 kiểm quyền/API; người 4 kiểm luồng xuyên hai app |
| 4 — Flutter Client | Phân tích luồng bạn đọc, màn hình, quyền cá nhân, hủy/hoàn tác và nhắc hạn | Use case Client; sơ đồ màn hình; wireframe; bảng màn hình–API; phương án nhắc hạn | Người 1 và 2 kiểm quyền/API; người 3 kiểm luồng xuyên hai app |

Người 1 tổng hợp ERD và thiết kế kỹ thuật. Người 2 quyết định phạm vi, tổ chức họp, tổng hợp ba sản phẩm và kiểm tra trước khi nộp. Mỗi người tự viết, sửa và commit phần mình; việc tổng hợp không thay thế đóng góp cá nhân.

## Trình tự làm việc trong tuần

| Thời điểm | Việc thực hiện | Kết quả cần có |
|---|---|---|
| Đầu tuần hoặc buổi đầu còn lại | Mỗi người đọc tài liệu, viết nháp phần mình và ghi câu hỏi | Nháp use case, ERD, luồng màn hình và danh sách điểm chưa rõ |
| Giữa tuần hoặc buổi làm việc tiếp theo | Họp chốt 60 phút; đối chiếu use case → bảng → API → màn hình | Quyết định được ghi lại; mỗi vấn đề còn mở có người xử lý và hạn trả lời |
| Sau họp | Mỗi người cập nhật tài liệu đúng quyết định, commit phần mình và gửi review | Bốn phần không còn dùng enum, tên trường hoặc chính sách khác nhau |
| Trước hạn nộp tuần 9 | Kiểm chéo, xuất sơ đồ và hoàn thiện bảng phân công; nhóm trưởng nộp bản nháp | Đủ ba sản phẩm, mở đọc được, đã xác nhận nội bộ |

Các mốc trên là thứ tự tương đối. Nhóm trưởng điền ngày và giờ theo hạn LMS thực tế; tài liệu không giả định một ngày nộp tuần 9 cụ thể.

## Những quyết định phải chốt cùng nhau

- [ ] Mượn bằng mã bản sách; đặt trước theo nhan đề. Một tài khoản khác với một hồ sơ/thẻ bạn đọc.
- [ ] Quyền ADMIN, THU_THU và BAN_DOC; ai được đọc/sửa dữ liệu nào; quyền sở hữu phiếu cá nhân.
- [ ] Hạn mượn, giới hạn bản đang mượn, gia hạn; xử lý khi hạn thẻ sớm hơn hạn trả dự kiến.
- [ ] Hàng đợi, giữ đúng 48 giờ, người đầu không đủ điều kiện, hết giữ và lượt tiếp theo.
- [ ] Cách tính ngày quá hạn và phạt bậc thang; phân biệt phạt dự kiến với khoản phạt đã chốt.
- [ ] Hủy/hoàn tác lượt chờ; thời gian và vị trí sau khi khôi phục.
- [ ] Định nghĩa bốn báo cáo và cách phối hợp nhắc hạn giữa backend với Flutter.
- [ ] Một backend, một DB, một tài liệu API chung; ai cập nhật trạng thái lưu thông và ai sửa metadata.

Các con số 14 ngày, 5 bản, gia hạn một lần thêm 7 ngày, hoàn tác 10 giây và mức tiền từng bậc trong DOCX là phương án đề xuất cần nhóm xác nhận. Giữ 48 giờ và nhắc trước hạn 2 ngày là yêu cầu của đề. Chỗ chưa rõ phải ghi là chưa chốt, không tự biến thành quy định giảng viên.

## Điều kiện nghiệm thu cuối tuần 9

- [ ] Use case bao quát chức năng bắt buộc của cả hai app; vai trò và người thực hiện được ghi rõ.
- [ ] ERD phân biệt sách/bản sách, có ít nhất 8 bảng theo yêu cầu chung, thể hiện PK/FK và các ràng buộc quan trọng ở mức thiết kế.
- [ ] Bảng phân công có tên thật, đầu việc, đầu ra, hạn, người review và tỷ lệ đóng góp tổng 100%.
- [ ] Người 3 và 4 đã kiểm tra API/JSON mẫu đủ dùng để bắt đầu làm model và giao diện; nội dung thiếu được ghi thành vấn đề cần giải quyết.
- [ ] Các thông số nghiệp vụ được ghi rõ là đã chốt hoặc còn mở; không có hai phiên bản khác nhau trong tài liệu của hai người.
- [ ] Mỗi người đã commit thay đổi có nội dung thật bằng tài khoản riêng; nhóm trưởng kiểm tra lịch sử trước khi nộp.

## Theo dõi commit

Quy định cả đồ án là **ít nhất 15 commit có ý nghĩa mỗi người, rải đều trong quá trình làm**, theo E.3. Mục tiêu khoảng 3 commit có nội dung thật trong tuần 9 chỉ là đề xuất: bản phân tích đầu tiên, thiết kế sau kiểm chéo, và sửa theo buổi chốt. Không cần tách giả một thay đổi thành nhiều commit; không ghi một commit là đã có khi chưa thực hiện.

Mỗi Issue nên có một người sở hữu, đầu ra và tiêu chí xong. Người 1 và 2 review chéo backend; người 3 và 4 kiểm luồng xuyên hai app. Thay API cần người dùng API kiểm lại. Nhóm trưởng theo dõi và nghiệm thu, không sửa hộ toàn bộ công việc rồi nhận hết commit.


## Tài liệu tham chiếu

Xem [danh mục nguồn của nhóm](../../references.md). Các checklist là kế hoạch tuần 9; đánh dấu hoàn thành khi có đầu ra thực tế.
