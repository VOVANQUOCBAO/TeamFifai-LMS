# Kiểm thử và bằng chứng

Mã kiểm thử nằm trong project tương ứng; kết quả thực tế phục vụ báo cáo nằm ở đây.

| Nơi | Nội dung |
|---|---|
| `test-cases.md` | Danh mục ca, kỳ vọng và kết quả thực tế |
| `evidence/p1/` | Auth, catalog, DB, deploy của P1 |
| `evidence/p2/` | Mượn/trả, phạt, concurrency, job của P2 |
| `evidence/p3/` | Android, vai trò, form, rotation của P3 |
| `evidence/p4/` | Flutter, phiên, hoàn tác, nhắc hạn của P4 |
| `evidence/integration/` | Luồng xuyên Android → backend → Flutter |

Theo kế hoạch nguồn, 25 hồ sơ ca được chia P1: 7, P2: 9, P3: 5, P4: 4; mỗi hồ sơ có thể có subcase cần ghi kết quả riêng. Không đếm trùng cùng một ca chỉ vì nhiều người tham gia.

Ghi thiết bị, fixture, commit, bước, expected, actual và bằng chứng. Test transaction/đồng thời cần DB thật; mock không chứng minh chống mượn trùng. Chưa chạy phải ghi **CHƯA CHẠY**.
