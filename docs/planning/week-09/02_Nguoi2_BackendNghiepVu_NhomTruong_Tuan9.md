# Tuần 9 Người 2 Backend nghiệp vụ và nhóm trưởng

**Người thực hiện:** ................................  **Hạn bàn giao:** ................................

Cuối tuần 9, nhóm nộp bản nháp sơ đồ use case, sơ đồ cơ sở dữ liệu và bảng phân công theo D.3. Bạn điều phối ba đầu ra này, đồng thời chốt cách hiểu nghiệp vụ và đầu vào cho hai app; chưa cần hoàn thành transaction, job hay toàn bộ API.

## Phạm vi tuần này

**Nộp giảng viên theo D.3:** bản nháp sơ đồ use case, sơ đồ cơ sở dữ liệu và bảng phân công của cả nhóm. API/JSON mẫu, wireframe, quy tắc nghiệp vụ và checklist kỹ thuật là tài liệu hỗ trợ nội bộ được đề xuất để thống nhất thiết kế.

Các ô dưới đây đang để trống để người thực hiện tự cập nhật khi có đầu ra. Skeleton hoặc chạy thử chỉ làm nếu đủ thời gian sau khi hoàn thành phần phân tích. Chưa coi code, deploy, kiểm thử hay APK là đã xong chỉ vì đã có kế hoạch.

## Công việc cần làm

### P2-W9-01

- [ ] **Việc làm:** Chủ trì buổi chốt đề tài và phạm vi chung. Kiểm tra đủ bốn thành viên, mã nhóm, tài khoản GitHub và repo đã đăng ký; xác nhận dùng một backend và một DB chung.
- **Đầu ra:** Biên bản ngắn ghi thành viên, vai trò, phạm vi chính, phần chưa làm ở bản cơ bản và danh sách quyết định còn mở.
- **Tiêu chí hoàn thành:** Cả bốn người xác nhận cùng phạm vi; không ai hiểu phần mình là dự án hoặc backend độc lập. Thông tin thiếu của mốc đăng ký được ghi rõ để bổ sung.
- **Phối hợp:** Cả nhóm; người 1 xác nhận nền tảng, người 3 và 4 xác nhận phạm vi app.

### P2-W9-02

- [ ] **Việc làm:** Tổng hợp tác nhân và vẽ sơ đồ use case của cả hệ thống; làm rõ ADMIN, THU_THU, BAN_DOC và các chức năng từng vai trò.
- **Đầu ra:** Một sơ đồ use case bản nháp có file nguồn chỉnh sửa được và bản xuất dễ xem. Các nhóm chức năng gồm danh mục/thẻ, mượn trả, gia hạn, đặt trước, phạt, báo cáo và thông báo.
- **Tiêu chí hoàn thành:** Người 3 đối chiếu được luồng quản trị, người 4 đối chiếu được luồng bạn đọc; sơ đồ không cấp cho bạn đọc quyền cho mượn hoặc thu phạt. Đây là một trong ba đầu ra phải nộp tuần 9.
- **Phối hợp:** Người 1 rà auth/danh mục/thẻ; người 3 rà Admin; người 4 rà Client.

### P2-W9-03

- [ ] **Việc làm:** Viết nháp 5–7 use case quan trọng để thống nhất cách vận hành, ưu tiên mượn, trả và tính phạt, gia hạn, đặt trước/nhận sách, hủy/hoàn tác, thu phạt.
- **Đầu ra:** Mỗi use case có tác nhân, tiền điều kiện, dữ liệu vào, luồng chính, nhánh bị từ chối và kết quả dữ liệu. Kèm sơ đồ trạng thái bản sách, phiếu và lượt đặt.
- **Tiêu chí hoàn thành:** Tự đi qua được một ví dụ từ mượn đến trả và một ví dụ A rồi B chờ sách; biết trạng thái nào thay đổi và lỗi nào cần hiện trên app. Quá hạn được tính từ hạn trả, không tạo thêm trạng thái phiếu mâu thuẫn. Đây là nháp phân tích nội bộ hỗ trợ sơ đồ.
- **Phối hợp:** Người 3 xác nhận thao tác nhân viên; người 4 xác nhận thông tin và lỗi cần hiện.

### P2-W9-04

- [ ] **Việc làm:** Chốt bảng chính sách đề xuất và xử lý câu hỏi còn mơ hồ trước khi viết logic.
- **Đầu ra:** Bảng quyết định có người xác nhận: mượn 14 ngày, tối đa 5 bản; gia hạn 1 lần thêm 7 ngày; giữ 48 giờ; hoàn tác lượt CHO trong 10 giây và xếp cuối hàng; phạt bậc 1–3/4–7/từ 8 ngày; ngày hạn theo Việt Nam. Cần chọn rõ: thẻ còn dưới 14 ngày thì rút ngắn hạn hay từ chối; gia hạn vượt hạn thẻ thì xử lý thế nào; lượt giữ có chiếm giới hạn mượn không; câu “có lượt mới” khi hoàn tác nghĩa là lượt mới của ai; không tự suy đoán các điểm này khi code.
- **Tiêu chí hoàn thành:** Các quyết định được ghi trong một bản chung đã review; điểm chưa thống nhất có người chịu trách nhiệm và hạn trả lời. Phân biệt rõ: giữ 48 giờ và nhắc trước hạn 2 ngày là yêu cầu của đề; 14 ngày mượn, 5 bản, gia hạn một lần thêm 7 ngày, hoàn tác 10 giây và mức tiền từng bậc là phương án DOCX đề xuất cần nhóm xác nhận. Ví dụ 10 ngày ra 26.000 đồng chỉ áp dụng nếu nhóm chốt đúng biểu phạt đề xuất đó.
- **Phối hợp:** Bạn chốt với vai trò nhóm trưởng; người 1 rà dữ liệu, người 3/4 rà trải nghiệm app.

### P2-W9-05

- [ ] **Việc làm:** Cùng người 1 hoàn thiện ERD bản nháp; bạn thiết kế các thực thể nghiệp vụ và kiểm tra quan hệ với dữ liệu nền tảng.
- **Đầu ra:** ERD thể hiện sách khác bản sách; bạn đọc, phiếu mượn, đặt trước, biểu phạt, khoản phạt, nhật ký, thông báo và yêu cầu lặp; có PK/FK, quan hệ, null và ràng buộc quan trọng. Ghi mượn dùng banSachId, đặt trước dùng sachId.
- **Tiêu chí hoàn thành:** Thiết kế thể hiện FK phiếu tới bạn đọc/bản sách, ràng buộc ngăn hai phiếu mở cho cùng bản, khoản phạt gắn đúng phiếu và banSachId có thể null khi lượt đặt còn CHO. Người 1 ghép được phần nghiệp vụ vào ERD toàn hệ thống. Đây là điều kiện kiểm tra bản thiết kế; chưa yêu cầu chạy migration hoặc chứng minh ràng buộc bằng DB thật trong tuần 9.
- **Phối hợp:** Người 1 sở hữu bản ERD tổng; bạn chịu trách nhiệm phần nghiệp vụ. Người 3/4 rà xem dữ liệu đủ cho màn hình.

### P2-W9-06

- [ ] **Việc làm:** Phác thảo hợp đồng trao đổi nội bộ cho những luồng sẽ làm đầu tiên.
- **Đầu ra:** Bảng API dự kiến và JSON mẫu cho tạo phiếu, trả, gia hạn, đặt/hủy/hoàn tác; thống nhất body, quyền, mã lỗi, UUID, envelope data/error, hanTra dạng ngày và giuDenLuc dạng thời điểm. Ghi rõ Người 1 cung cấp transaction helper/auth và bạn cung cấp circulation.service.
- **Tiêu chí hoàn thành:** Người 3 biết gửi banDocId và banSachId khi mượn; người 4 biết đặt bằng sachId, xem phiếu cá nhân từ token và không tự tính tiền phạt. Bảng này ghi rõ là thiết kế nội bộ, chưa phải API đã chạy. Không biến OpenAPI đầy đủ thành điều kiện nộp tuần 9.
- **Phối hợp:** Người 1 chốt helper, đường dẫn và dữ liệu; người 3/4 kiểm tra DTO và các lỗi UI cần xử lý.

### P2-W9-07

- [ ] **Việc làm:** Chốt bảng phân công và backlog có người sở hữu, đầu vào, tiêu chí xong và mốc bàn giao.
- **Đầu ra:** Bảng bốn vai trò: người 1 nền tảng/DB/auth/danh mục/deploy; bạn nghiệp vụ và điều phối; người 3 Android Admin; người 4 Flutter Client. Kèm owner file/module, người review, thứ tự phụ thuộc và mốc tuần 11/13/15.
- **Tiêu chí hoàn thành:** Mỗi việc có một owner; hai backend không cùng tạo một số migration; việc nhóm trưởng review/ghép được tính là công việc riêng. Bảng phân công bản nháp được cả bốn người xác nhận và nằm trong gói nộp tuần 9.
- **Phối hợp:** Cả nhóm; đặc biệt người 1 thống nhất quyền sửa file chung và lịch ghép.

### P2-W9-08

- [ ] **Việc làm:** Thiết lập cách ghi nhận đóng góp GitHub ngay từ tuần phân tích.
- **Đầu ra:** Quy ước nhánh/PR, kiểm tra GitHub riêng và user.email của từng người; bảng việc có liên kết issue/commit; kế hoạch mỗi người đạt ít nhất 15 commit có ý nghĩa trải trong toàn kỳ theo E.3.
- **Tiêu chí hoàn thành:** Mỗi người tự commit phần tài liệu mình thực hiện, thông điệp mô tả việc thật; không dùng chung tài khoản, không gom mọi thay đổi dưới tên nhóm trưởng, không sửa lịch sử làm mất dấu đóng góp. Tuần 9 chỉ commit các đầu việc tuần 9 thực sự hoàn thành, không cố tạo đủ 15 ngay tuần này.
- **Phối hợp:** Cả bốn người; bạn theo dõi và review chứ không commit thay tất cả.

### P2-W9-09

- [ ] **Việc làm:** Tổ chức một lượt review chéo rồi chốt gói nộp bản nháp tuần 9 và các đầu vào tuần sau.
- **Đầu ra:** Gói gồm use case, ERD, bảng phân công; kèm nội bộ bảng chính sách, câu hỏi mở, API/JSON nháp, backlog và biên bản thống nhất. Ghi phiên bản hoặc commit chốt để mọi người dùng cùng bản.
- **Tiêu chí hoàn thành:** Ba bản nộp khớp tên thực thể, vai trò và chức năng; mỗi thành viên giải thích được phần mình; lưu được bằng chứng nộp theo kênh giảng viên quy định. Các tài liệu chưa chạy được ghi là thiết kế, không đánh dấu kiểm thử đạt.
- **Phối hợp:** Cả nhóm review; bạn tổng hợp và nộp theo hướng dẫn lớp.

## Bộ bàn giao cuối tuần

- [ ] Nộp giảng viên: sơ đồ use case bản nháp, sơ đồ CSDL/ERD bản nháp, bảng phân công bản nháp theo D.3.
- [ ] Gửi người 1: thực thể nghiệp vụ, PK/FK/ràng buộc dự kiến, chính sách đã thống nhất, yêu cầu helper chung và danh sách migration sẽ cần cấp số.
- [ ] Gửi người 3: luồng mượn/trả/thu phạt, quyền ADMIN–THU_THU, request/response nháp và định nghĩa bốn báo cáo để tránh vẽ chart sai.
- [ ] Gửi người 4: trạng thái đặt trước, hạn mượn/giữ, điều kiện gia hạn/hủy/hoàn tác, thông báo và JSON nháp để thiết kế UI.
- [ ] Lưu chung: biên bản chốt, version/commit tài liệu, câu hỏi mở có owner, backlog tuần 10 và quy ước GitHub.

## Thứ tự thực hiện

1. Đầu tuần: họp chốt phạm vi và vai trò, rà yêu cầu tuần 9; phác use case, chính sách và danh sách câu hỏi; mỗi người nhận phần tài liệu cụ thể.
2. Giữa tuần: bạn mô tả use case nghiệp vụ, cùng người 1 ghép ERD; người 3/4 rà luồng màn hình và JSON; xử lý các câu hỏi chính sách rồi cập nhật bảng phân công/backlog.
3. Cuối tuần: review chéo ba đầu ra bắt buộc, sửa cho nhất quán, commit phiên bản chốt, nộp bản nháp theo lịch lớp; giao đầu vào tuần 10. Nếu đã sẵn khung thì có thể thử một luồng nhỏ, nhưng không lấy việc hoàn thành full API làm điều kiện nộp tuần 9.

Đây là lịch tương đối đầu–giữa–cuối tuần. Nếu tuần đã bắt đầu, giữ thứ tự công việc và điền hạn thực tế theo LMS cùng nhóm trưởng.

## Gợi ý commit có ý nghĩa

Toàn kỳ cần ít nhất 15 commit có ý nghĩa mỗi người, theo E.3. Ba ví dụ dưới đây là gợi ý cho phần việc thật tuần 9, không phải định mức riêng của giảng viên và không yêu cầu tạo commit giả cho đủ số. Mỗi người tự commit phần mình bằng tài khoản riêng.

- docs: mô tả use case mượn trả và trạng thái đặt trước
- docs: chốt chính sách lưu thông và rà ERD nghiệp vụ
- docs: cập nhật phân công tuần 9 và kế hoạch tích hợp tuần 10

## Điểm cần tránh

- Nhầm “trên 10 commit” thành đủ yêu cầu: E.3 quy định tối thiểu 15 commit có ý nghĩa cho mỗi người trong toàn kỳ, không phải 15 trong tuần 9.
- Coi con số chính sách đề xuất trong DOCX là yêu cầu bắt buộc của giảng viên; cần ghi quyết định nhóm và hỏi lại điểm liên quan đề chưa rõ.
- Vẽ phiếu mượn chỉ gắn nhan đề thay vì bản sách; hai app sẽ không xác định được bản vật lý đang lưu thông.
- Ba người tự đặt tên endpoint/enum/field khác nhau; cần một phiên bản thiết kế được cả nhóm chốt.
- Tập trung viết code transaction/job ngay khiến bỏ sót use case, ERD và bảng phân công là ba đầu ra tuần 9.
- Nhóm trưởng làm và commit thay mọi người, hoặc chỉ chia chức danh mà không có đầu ra và tiêu chí nghiệm thu.
- Ghi “đã đạt” cho test hoặc API chưa thực thi; tuần 9 có thể ghi ca kiểm thử dự kiến và trạng thái CHƯA CHẠY.
- Hiểu nhầm khuyến nghị deploy sớm là yêu cầu phải hoàn thành toàn bộ backend tuần 9; D.3 dành mốc backend public và 3 API thật cho tuần 11.

## Việc triển khai ở các tuần sau

- Từ tuần 10, nhận khung backend/helper của người 1, triển khai từng lát nghiệp vụ và migration đã cấp số; ưu tiên cho mượn–hiển thị phiếu xuyên Android/backend/Flutter.
- Triển khai transaction, khóa DB, idempotency, unique index và kiểm thử đồng thời trên DB thật; tuần 9 chỉ thiết kế invariant và ca kiểm thử dự kiến.
- Mở rộng trả sách/phạt, FIFO/giữ 48 giờ, gia hạn và hủy/hoàn tác theo hợp đồng đã chốt.
- Hoàn thiện bốn báo cáo, scheduler/nhắc hạn/thông báo, seed 400 phiếu/40 lượt đặt và hồ sơ P2-01 đến P2-09 theo các mốc sau.
- Phối hợp chuẩn bị mốc tuần 11 có backend đã deploy, tối thiểu 3 API thật và 2 màn hình mỗi app; không chờ toàn bộ nghiệp vụ xong mới tích hợp.


## Tài liệu tham chiếu

Xem [danh mục nguồn của nhóm](../../references.md). Các checklist là kế hoạch tuần 9; đánh dấu hoàn thành khi có đầu ra thực tế.
