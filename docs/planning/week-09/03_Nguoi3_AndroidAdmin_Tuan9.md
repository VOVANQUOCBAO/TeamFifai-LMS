# Tuần 9  Android Admin


Trong tuần 9, cung cấp phần phân tích Android để nhóm chốt và nộp bản nháp use case, sơ đồ CSDL và bảng phân công. Chốt luồng, quyền, màn hình và dữ liệu cần từ backend; chưa phải hoàn thành ứng dụng.

## Phạm vi tuần này

**Nộp giảng viên theo D.3:** bản nháp sơ đồ use case, sơ đồ cơ sở dữ liệu và bảng phân công của cả nhóm. API/JSON mẫu, wireframe, quy tắc nghiệp vụ và checklist kỹ thuật là tài liệu hỗ trợ nội bộ được đề xuất để thống nhất thiết kế.

Các ô dưới đây đang để trống để người thực hiện tự cập nhật khi có đầu ra. Skeleton hoặc chạy thử chỉ làm nếu đủ thời gian sau khi hoàn thành phần phân tích. Chưa coi code, deploy, kiểm thử hay APK là đã xong chỉ vì đã có kế hoạch.

## Công việc cần làm

### P3-W9-01

- [ ] **Việc làm:** Đọc yêu cầu và lập ma trận quyền ADMIN/THU_THU.
- **Đầu ra:** Một bảng tác nhân × chức năng: cả hai được quản lý danh mục, bản sách, thẻ, mượn/trả, hàng đợi, thu phạt và xem báo cáo; ADMIN có thêm quản lý nhân viên và tạo phiên bản biểu phạt. BAN_DOC thuộc app Flutter.
- **Tiêu chí hoàn thành:** Mỗi chức năng có vai trò được phép và người sở hữu API; ghi rõ ẩn menu chỉ hỗ trợ giao diện, backend vẫn phải chặn trái quyền. Nhóm trưởng và người 1/2 đã review các quyền đề xuất.
- **Phối hợp:** Nhóm trưởng chốt; người 1 xác nhận auth và quyền nhân viên; người 2 xác nhận quyền nghiệp vụ.

### P3-W9-02

- [ ] **Việc làm:** Góp use case và bảng phân công cho bản nháp nộp tuần 9.
- **Đầu ra:** Danh sách use case phía Android và mô tả ngắn các luồng quan trọng được giao, ưu tiên đăng nhập, tra cứu sách/bạn đọc, cho mượn và nhận trả. Mỗi mô tả có tác nhân, tiền điều kiện, luồng chính, nhánh lỗi và hậu điều kiện. Ghi rõ người 3 sở hữu admin-android và đầu ra phân tích tuần này.
- **Tiêu chí hoàn thành:** Các use case khớp quyền ở P3-W9-01; dùng đúng khái niệm nhan đề và bản sách; người 2 xác nhận nhánh thẻ hết hạn, vượt giới hạn, nợ phạt, bản đang mượn/giữ. Gửi nhóm trưởng để ghép vào use case và phân công toàn nhóm.
- **Phối hợp:** Nhóm trưởng ghép sơ đồ và phân công; người 2 đối chiếu quy tắc; người 4 đối chiếu tác động sang bạn đọc.

### P3-W9-03

- [ ] **Việc làm:** Lập danh mục màn hình và sơ đồ điều hướng cho sản phẩm cuối.
- **Đầu ra:** Inventory dự kiến: đăng nhập/trang chính; phân loại; danh sách sách; chi tiết nhan đề/bản sách; bạn đọc/thẻ; cho mượn; nhận trả; gia hạn/hàng đợi; quản lý phạt; báo cáo; tài khoản nhân viên. Đánh dấu màn chỉ ADMIN, hai danh sách dùng RecyclerView là sách và bạn đọc, cùng vị trí biểu đồ báo cáo.
- **Tiêu chí hoàn thành:** Sơ đồ điều hướng bao phủ tối thiểu 6 màn hình về sau và không bỏ chức năng bắt buộc. Ghi đây là phạm vi cuối kỳ, không phải yêu cầu lập trình xong trong tuần 9. Chốt login và danh sách sách làm hai màn ưu tiên nối API cho mốc tuần 11.
- **Phối hợp:** Nhóm trưởng kiểm phạm vi và tải việc; người 1/2 kiểm API hỗ trợ.

### P3-W9-04

- [ ] **Việc làm:** Vẽ wireframe mức đơn giản cho luồng ưu tiên.
- **Đầu ra:** Wireframe có thể dùng giấy hoặc công cụ sẵn có: login → danh sách sách → chi tiết nhan đề/bản; chọn cho mượn → quét/nhập mã thẻ và mã cá biệt → xác nhận → kết quả. Chú thích thêm đường nhận trả. Mỗi bước có chỗ hiện loading, dữ liệu/rỗng, lỗi và nút thử lại hoặc quay lại.
- **Tiêu chí hoàn thành:** Người khác nhìn wireframe biết nhập gì, gửi gì, xem gì tiếp theo. Màn xác nhận hiển thị bạn đọc, nhan đề, mã bản; hạn trả và phạt lấy từ server. Wireframe ghi camera và nhập tay là hai cách nhập mã dự kiến, chưa yêu cầu camera chạy thật tuần này.
- **Phối hợp:** Người 2 review luồng mượn/trả; người 1 xác nhận tra cứu mã; người 4 xác nhận sau cho mượn bạn đọc sẽ thấy gì.

### P3-W9-05

- [ ] **Việc làm:** Lập bảng màn hình–API và yêu cầu field/error cho hai người backend.
- **Đầu ra:** Bảng gồm màn hình, hành động, method/path, request, response cần, quyền, lỗi, người làm API và trạng thái đã chốt/chưa chốt. Tối thiểu có POST /auth/dang-nhap + GET /auth/toi; GET /sach + GET /sach/:id; GET /ban-sach?sachId= hoặc maCaBiet=; GET /ban-doc?maThe=; POST /phieu-muon với banDocId và banSachId; GET /phieu-muon theo banSachId/trangThai; PATCH /phieu-muon/:id/tra. Ghi các endpoint gia hạn/phạt/hàng đợi/báo cáo vào backlog để chốt phạm vi.
- **Tiêu chí hoàn thành:** Xác nhận UUID là String; JSON camelCase; envelope data/meta/error; danh sách có page/pageSize/total; hanTra/hanThe là DATE, giuDenLuc là thời điểm; tiền VNĐ nguyên. Yêu cầu rõ token/expiresAt/vaiTro, nhanDe/soBanRanh, maCaBiet/viTriKe/trangThai, banDocId và lý do bị chặn; lỗi 400/401/403/404/409 cùng error.code/message. Với trả sách cần phieuMuon, phat hoặc null và giuCho hoặc null. Có ít nhất một JSON mẫu đăng nhập, danh sách sách và kết quả mượn/trả, hoặc đánh dấu thiếu mẫu kèm người cung cấp; không tự đoán hợp đồng.
- **Phối hợp:** Người 1 cấp auth/catalog/readers và cấu trúc JSON; người 2 cấp loans/returns/errors; nhóm trưởng ghi các quyết định còn mở.

### P3-W9-06

- [ ] **Việc làm:** Review ERD từ góc nhìn Android và gửi câu hỏi dữ liệu.
- **Đầu ra:** Một phiếu góp ý ERD: phân biệt sachId với banSachId; mã cá biệt riêng từng bản; maThe gắn bạn đọc; phiếu gắn bản và bạn đọc; cần kệ/tình trạng, hạn thẻ, hạn trả, số lần gia hạn, phạt, giữ đến lúc và dữ liệu báo cáo. Liệt kê các filter còn thiếu cho màn hình.
- **Tiêu chí hoàn thành:** Người 1/2 đã nhận góp ý, trả lời hoặc ghi mục chưa chốt. Không yêu cầu app tự tính tồn kho, phạt hay quyền; không tự sửa schema backend chỉ để thuận tiện DTO.
- **Phối hợp:** Người 1 sở hữu ERD tổng hợp; người 2 đối chiếu các bảng nghiệp vụ; nhóm trưởng chốt bản nháp CSDL.

### P3-W9-07

- [ ] **Việc làm:** Chốt checklist kỹ thuật, rủi ro và tiêu chí nghiệm thu Android.
- **Đầu ra:** Checklist triển khai về sau: Java/XML, minSdk 24, package/tên app/TAG có mã nhóm; Retrofit/Gson; cấu hình Base URL; token/session; RecyclerView/Adapter/ViewHolder; validate form và xác nhận xóa; giữ form qua rotation; loading/rỗng/lỗi/Thử lại; 401 về login và 403 báo thiếu quyền; không lộ token; không double submit. Lập các ca kiểm thử dự kiến, chưa đánh dấu đã chạy.
- **Tiêu chí hoàn thành:** Có cách kiểm cho login ADMIN/THU_THU, hai danh sách, form xoay máy, mượn/trả và lỗi mạng. Ghi rõ mutation cần giữ cùng Idempotency-Key khi retry sau timeout theo hợp đồng nội bộ. Có danh sách phụ thuộc cần trước tuần 11: Base URL, tài khoản test, JSON fixture và API thật.
- **Phối hợp:** Người 1 chốt Base URL/auth; người 2 chốt idempotency và lỗi nghiệp vụ; nhóm trưởng chốt lịch nhận API.

### P3-W9-08

- [ ] **Việc làm:** Nếu còn thời gian sau khi xong phân tích, tạo skeleton Android tối thiểu.
- **Đầu ra:** Tùy chọn: project Java/XML, minSdk 24, mã nhóm đúng, Gradle Wrapper và README cách mở/build; bộ khung package data/api, data/dto, data/repository, session, ui, util. Có thể dừng ở màn trống hoặc điều hướng mẫu.
- **Tiêu chí hoàn thành:** Chỉ ghi build được nếu đã chạy lệnh thật và lưu kết quả; chưa chạy phải ghi CHƯA CHẠY. Nếu chưa làm, ghi chuyển tuần 10; không ảnh hưởng việc hoàn tất ba bản nháp tuần 9. Chưa bắt buộc Retrofit hoạt động hay hai màn gọi API thật.
- **Phối hợp:** Người 1 cung cấp cấu hình dự kiến; nhóm trưởng ưu tiên sản phẩm phân tích trước.

## Bộ bàn giao cuối tuần

- [ ] Gửi nhóm trưởng ma trận quyền, đóng góp use case, danh mục màn hình/điều hướng và dòng phân công người 3 để ghép ba bản nháp nộp tuần 9.
- [ ] Gửi người 1 bảng màn hình–API auth/catalog/readers, field/filter cần, góp ý ERD và danh sách JSON mẫu còn thiếu.
- [ ] Gửi người 2 wireframe mượn/trả, các request/response/error cần, câu hỏi nghiệp vụ và checklist idempotency.
- [ ] Gửi người 4 điểm ghép dự kiến: Android thêm sách/bản thì Flutter xem được; Android cho mượn/nhận trả/gia hạn thì Flutter thấy trạng thái tương ứng.
- [ ] Mỗi bàn giao ghi người làm, đường dẫn hoặc link tài liệu, commit, mục đã chốt, mục chưa chốt, người cần trả lời và hạn; không gửi secret thật.

## Thứ tự thực hiện

1. Đầu tuần: hoàn thành P3-W9-01 đến P3-W9-03; đọc yêu cầu, góp use case/phân công và xác định phạm vi màn hình.
2. Giữa tuần: thực hiện P3-W9-04 đến P3-W9-06; review wireframe, API mapping và ERD cùng hai người backend.
3. Cuối tuần: hoàn thành P3-W9-07, sửa các mục được góp ý, gửi bàn giao để nhóm trưởng nộp ba bản nháp; làm P3-W9-08 nếu còn thời gian.

Đây là lịch tương đối đầu–giữa–cuối tuần. Nếu tuần đã bắt đầu, giữ thứ tự công việc và điền hạn thực tế theo LMS cùng nhóm trưởng.

## Gợi ý commit có ý nghĩa

Toàn kỳ cần ít nhất 15 commit có ý nghĩa mỗi người, theo E.3. Ba ví dụ dưới đây là gợi ý cho phần việc thật tuần 9, không phải định mức riêng của giảng viên và không yêu cầu tạo commit giả cho đủ số. Mỗi người tự commit phần mình bằng tài khoản riêng.

- `docs(android): phan tich tac nhan quyen va use case quan tri` — Commit tài liệu thật của P3-W9-01 và P3-W9-02 sau khi đã viết và tự review.
- `docs(android): them so do man hinh va wireframe muon tra` — Commit inventory, điều hướng và wireframe có nội dung của P3-W9-03 và P3-W9-04.
- `docs(android): chot du lieu API va checklist trien khai tuan 9` — Commit mapping, góp ý ERD, quyết định sau review và checklist của P3-W9-05 đến P3-W9-07.

## Điểm cần tránh

- Nhầm số màn hình cuối kỳ với việc phải code xong tuần 9; bỏ thời gian làm giao diện khiến thiếu use case, ERD hoặc phân công.
- Tự coi THU_THU có mọi quyền ADMIN; chỉ ẩn nút mà không xác nhận API trả 403 khi gọi trái quyền.
- Đồng nhất ISBN/nhan đề với mã cá biệt của bản; gửi sachId vào API cho mượn thay vì banSachId.
- Tự tạo field/enum hoặc parse object trần khi API bọc data; cần ghi câu hỏi/đề nghị đổi hợp đồng thay vì sửa backend hộ.
- Tự tính phạt, số bản rảnh hoặc hạn trả trên Android; các kết quả quyết định nghiệp vụ phải do backend trả.
- Không phân biệt 401 với 403; retry mutation bằng key mới sau timeout có thể tạo thao tác trùng.
- Ghi thử camera, máy thật hoặc API thật là ĐẠT khi mới có thiết kế; tuần 9 chỉ ghi ca dự kiến và bằng chứng thực sự đã có.
- Chia thay đổi thành commit rỗng/đổi một chữ để đủ số; ba commit ở đây chỉ hợp lệ khi có ba phần việc thật và dùng tài khoản cá nhân.

## Việc triển khai ở các tuần sau

- Chuyển phần lập trình đầy đủ tối thiểu 6 màn hình, hai RecyclerView và toàn bộ form sang các tuần triển khai; tuần 9 chỉ xác định phạm vi và thiết kế.
- Hai màn login/danh sách sách gọi API thật là mục tiêu mốc tuần 11, không phải yêu cầu hoàn thành tuần 9.
- Triển khai camera, xin quyền, quét QR/barcode trên máy thật và kiểm nhập tay dự phòng để làm sau khi chốt luồng/mã/API; tuần 9 chỉ thiết kế và ghi điểm cần xác nhận.
- Chưa cần chart hoạt động, upload ảnh thật, mượn/trả/thu phạt thật, toàn bộ kiểm thử tích hợp, APK release signed hoặc video bảo vệ trong tuần 9.
- Các số chính sách 14 ngày, 5 bản, gia hạn thêm 7 ngày và timeout đề xuất phải chờ nhóm chốt hợp đồng; Android không tự quyết.


## Tài liệu tham chiếu

Xem [danh mục nguồn của nhóm](../../references.md). Các checklist là kế hoạch tuần 9; đánh dấu hoàn thành khi có đầu ra thực tế.
