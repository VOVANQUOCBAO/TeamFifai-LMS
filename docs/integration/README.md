# Tích hợp tuần 11 — Người 2

Nhánh `member2/integration`, tạo từ `main` @ `afd7c3f` (tag `contract-v1.0.0`). Ngày 21/11/2026.

## 1. Mã đã nhận và ghép

| Người | Nhánh @ SHA đã merge | Commit code trong handoff | Kết quả nhận |
|---|---|---|---|
| P1 Backend | `member1/backend` @ `15b5380` | `7d9db36` | Merge sạch. Đủ schema 10 bảng, seed, 4 API, `/health`, router loans trả 501 như hợp đồng. Chưa deploy (chờ Supabase/Render của nhóm). |
| P3 Android | `member3/android` @ `2c83200` | không có handoff | Conflict add/add 5 file với skeleton cũ trên `main` (cùng tác giả), lấy bản mới của nhánh. Nhánh chỉ có giao diện dữ liệu demo: chưa gọi API, form mượn nhập tên tay, chưa có `appMode/apiBaseUrl`, gradle wrapper cần Gradle 8.7. **P2 hoàn thiện phần còn thiếu** (xem mục 3). |
| P4 Flutter | `member4/flutter` @ `08214fb` | `8072737` | Merge sạch. Nhánh gốc `d8a57de` (trước tag), nhưng contract trong ZIP trùng v1.0.0. Có `APP_MODE`/`API_BASE_URL`, 3 màn hình, gọi `GET /ban-doc/toi/dang-muon`. |

`python contracts/verify_contracts.py` → `Contract 1.0.0: OK` sau khi ghép.

## 2. Backend — phần của P2

`backend/src/modules/loans/index.js` thay router 501 bằng nghiệp vụ thật, giữ nguyên hợp đồng:

- `POST /api/v1/phieu-muon` (ADMIN/THU_THU): body đúng `{banDocId, banSachId}`. Trong **một transaction trên một client**: khóa `ban_doc` rồi `ban_sach` (`FOR UPDATE`, thứ tự cố định) → kiểm tra bạn đọc/tài khoản bị khóa (`BAN_DOC_BI_KHOA`), thẻ hết hạn theo ngày Asia/Ho_Chi_Minh (`THE_HET_HAN`), nợ phạt chưa thu (`CON_NO_PHAT`), số phiếu đang mở ≥ giới hạn (`VUOT_GIOI_HAN_MUON`), bản phải `TOT` + `RANH` (`BAN_SACH_KHONG_SAN_SANG`) → ghi `phieu_muon` (hạn trả = mượn + 336 giờ theo `policies.json`), đổi `ban_sach` sang `DANG_MUON`, ghi `nhat_ky_thu_vien` `CHO_MUON`. Lỗi bất kỳ → ROLLBACK. Vi phạm unique index `phieu_muon_mo_ban_sach_uk` do cạnh tranh → 409, không lộ SQL.
- `GET /api/v1/ban-doc/toi/dang-muon` (BAN_DOC): lấy bạn đọc từ `sub` của JWT, không nhận ID từ app; phân trang `page/pageSize` như hợp đồng.
- Không đổi schema/seed; không cần migration mới (ràng buộc chống hai phiếu mở đã có từ P1).

## 3. Android — hoàn thiện P3 để nối API

Chi tiết ở [admin-android/README.md](../../admin-android/README.md). Tóm tắt: DTO theo OpenAPI, `LibraryRepository` có bản mock (đọc `assets/fixtures`) và live (Retrofit), 3 màn hình Đăng nhập → Sách → Cho mượn chọn `banSachId`/`banDocId` thật, build live chỉ bằng tham số Gradle. Màn hình prototype cũ của P3 giữ lại, chỉ mở ở mock.

## 4. Kết quả kiểm thử có thật

Môi trường: Windows 11, Node.js 24.19 (package khai báo 20; CI/Render dùng 20.20.2), PostgreSQL 17.5 cục bộ (embedded) dựng từ `schema.sql` + `seed.sql` trên DB trống; Android emulator API 36 x86_64; JDK 25 (JBR Android Studio), Gradle 9.1.0.

### Backend — `npm test` (14/14 đạt, chạy 2 lần liên tiếp đều đạt)

| Ca | Kết quả |
|---|---|
| 8 nhóm ca nền tảng của P1 (health, login 3 vai trò, JWT sai/hết hạn/khóa, phân quyền, sách, bạn đọc, ràng buộc DB) | Đạt |
| Thủ thư cho mượn BS0001 cho BD0001 → 201, đúng 8 trường Loan, hạn trả = mượn + 336h, bản đổi `DANG_MUON`, có nhật ký `CHO_MUON` của đúng thủ thư, `soBanRanh` giảm 1 | Đạt |
| BAN_DOC tương ứng thấy đúng phiếu mới; bạn đọc khác không thấy | Đạt |
| Thẻ hết hạn / bạn đọc khóa / tài khoản khóa / nợ phạt / bản hỏng / bản đang mượn → 409 đúng mã, DB không ghi dở | Đạt |
| BD0006 còn 1 suất, 2 yêu cầu đồng thời → đúng 1 thành công (201) + 1 `VUOT_GIOI_HAN_MUON`; tổng phiếu = giới hạn | Đạt |
| 2 yêu cầu đồng thời cùng một bản → 1 thành công + 1 `BAN_SACH_KHONG_SAN_SANG`; đúng 1 phiếu mở | Đạt |
| Body sai/thiếu/thừa trường/ID không tồn tại/BAN_DOC gọi API mượn/thiếu token → 400/404/403/401 | Đạt |
| Không còn route tuần 11 trả 501 | Đạt |

Test tự dọn phiếu đã tạo nên DB demo trở về trạng thái seed sau khi chạy.

### Android trên emulator, build live trỏ backend cục bộ (`http://10.0.2.2:3000/api/v1/`)

| Ca | Kết quả |
|---|---|
| Đăng nhập `bandoc@…` → từ chối vào app quản trị, không lưu token | Đạt |
| Đăng nhập `thuthu@…` → danh sách 20 nhan đề thật, số bản rảnh đúng seed | Đạt |
| Bấm Cho mượn khi chưa chọn → báo lỗi tại chỗ, không gửi | Đạt |
| Danh sách bản: BS0002 `DANG_MUON` bị làm mờ, không chọn được | Đạt |
| BS0001 + BD0003 (thẻ hết hạn) → "Không thể cho mượn: thẻ bạn đọc đã hết hạn." | Đạt |
| BS0001 + BD0001 → "Đã tạo phiếu mượn", hạn trả hiển thị giờ VN (+14 ngày); sách còn 0 bản rảnh; bạn đọc 1/3 | Đạt |
| Xoay màn hình sau khi tạo phiếu → giữ lựa chọn và trạng thái | Đạt |
| Sau đó gọi `GET /ban-doc/toi/dang-muon` bằng token BD0001 → thấy đúng phiếu vừa tạo | Đạt (bằng curl, xem mục 5) |
| Build mock: nhãn DEMO, sai mật khẩu báo lỗi, đăng nhập fixture → 1 sách fixture | Đạt |
| Unit test JVM (`FormattersTest`, đổi UTC → giờ VN, ngày hạn thẻ) 3/3 | Đạt |

## 5. Chưa thực hiện / còn mở

- **Deploy Render + Supabase: CHƯA.** Cần tài khoản của nhóm. Làm theo mục 6, rồi build lại hai APK live với URL HTTPS thật.
- **Flutter: CHƯA build lại trên máy tích hợp** (máy này chưa cài Flutter SDK). P4 báo `flutter analyze` sạch, 20/20 test, build APK mock đạt. Luồng Flutter live mới được chứng minh ở mức API (curl bằng token bạn đọc), chưa chạy app thật với backend.
- Chưa cài APK lên điện thoại thật.
- Nghiệp vụ trả, gia hạn, đặt trước 48 giờ, phạt bậc thang, báo cáo, nhắc hạn: mốc sau. Seed hiện 20 nhan đề/40 bản/10 bạn đọc; đề yêu cầu tối thiểu 300/700/200/400 phiếu/40 đặt trước trước khi bảo vệ.
- Android build: Android Gradle Plugin từ chối đường dẫn có dấu tiếng Việt trên Windows — clone repo vào thư mục không dấu.

## 6. Deploy bản tích hợp (cần làm để đạt mốc tuần 11)

1. **Supabase** → tạo project → *Connect* → *Session pooler* (cổng 5432) → copy URI. Mở *SQL Editor*, chạy `backend/schema.sql` rồi `backend/seed.sql`.
2. *Project Settings → Database → SSL Configuration* → tải chứng chỉ CA (PEM).
3. **Render** → *New → Blueprint* → chọn repo này (file `render.yaml` ở gốc, deploy nhánh `member2/integration`). Nhập `DATABASE_URL` (URI pooler) và `DATABASE_CA_CERT` (nội dung PEM). `JWT_SECRET` do Render tự sinh.
4. Mở `https://<ten>.onrender.com/health` → `{"data":{"status":"ok","database":"ok"}}`. Ghi URL vào README và handoff.
5. Build lại hai app với URL đó:

```powershell
# admin-android (thư mục không dấu)
.\gradlew.bat assembleDebug -PappMode=live -PapiBaseUrl=https://<ten>.onrender.com/api/v1/
# client-flutter
flutter build apk --debug --dart-define=APP_MODE=live --dart-define=API_BASE_URL=https://<ten>.onrender.com/api/v1/
```

6. Chạy lại ca bắt buộc: Android (THU_THU) cho mượn BS0001 → Flutter (BAN_DOC `bandoc@thuviendemo.test`) tải lại thấy phiếu. Sau khi đạt mới merge `member2/integration` vào `main` và gắn tag `week11-demo`.

Tài khoản demo (mật khẩu `DemoLib!4211`): `admin@thuviendemo.test` (ADMIN), `thuthu@thuviendemo.test` (THU_THU), `bandoc@thuviendemo.test` (BAN_DOC).
