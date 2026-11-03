# Triển khai môi trường chung

P1 phụ trách môi trường và deploy; P2 phụ trách nội dung job và điều kiện chạy lặp an toàn. Hiện chưa có backend public hoặc cấu hình triển khai hoạt động.

Điền khi có môi trường thật:

| Mục | Giá trị |
|---|---|
| Backend Base URL | Chưa triển khai |
| Health URL | Chưa triển khai |
| Commit triển khai | Chưa có |
| Phiên bản hợp đồng/seed | Chưa có |
| Người vận hành | P1, điền tên thật |
| Lịch/trigger job | P1/P2 chốt sau |

Ghi tên biến, lệnh build/start, cách migrate/seed, rollback và cách xem log. Giá trị secret nằm ở môi trường chạy, không dán vào tài liệu. Dùng kết nối Supabase đúng hướng dẫn hiện hành khi triển khai; ghi cấu hình đã kiểm thực tế. Hai app dùng cùng Base URL và phiên bản dữ liệu.
