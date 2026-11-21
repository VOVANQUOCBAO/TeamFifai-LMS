BEGIN;
SET LOCAL search_path = public, extensions;
SELECT pg_advisory_xact_lock(4211, 21);
CREATE EXTENSION IF NOT EXISTS pgcrypto;

WITH accounts AS (
  SELECT 1 AS n, 'admin' AS email, 'Quản trị Demo' AS ho_ten, 'ADMIN' AS vai_tro
  UNION ALL SELECT 2, 'thuthu', 'Thủ thư Demo', 'THU_THU'
  UNION ALL
  SELECT n + 2, CASE WHEN n = 1 THEN 'bandoc' ELSE 'bandoc' || lpad(n::text, 2, '0') END,
         CASE WHEN n = 1 THEN 'Bạn đọc Demo' ELSE 'Bạn đọc Demo ' || n END, 'BAN_DOC'
  FROM generate_series(1, 10) AS n
)
INSERT INTO nguoi_dung (id, email, mat_khau_hash, ho_ten, vai_tro, trang_thai)
SELECT ('10000000-0000-4000-8000-' || lpad(n::text, 12, '0'))::uuid,
       email || '@thuviendemo.test', crypt('DemoLib!4211', gen_salt('bf', 10)),
       ho_ten, vai_tro, CASE WHEN n = 12 THEN 'KHOA' ELSE 'HOAT_DONG' END
FROM accounts ON CONFLICT (id) DO NOTHING;

INSERT INTO phan_loai (id, ma, ten) VALUES
  ('40000000-0000-4000-8000-000000000001', 'VH', 'Văn học'),
  ('40000000-0000-4000-8000-000000000002', 'KH', 'Khoa học'),
  ('40000000-0000-4000-8000-000000000003', 'KN', 'Kỹ năng')
ON CONFLICT (id) DO NOTHING;

INSERT INTO sach (id, nhan_de, tac_gia, phan_loai_id)
SELECT ('30000000-0000-4000-8000-' || lpad(n::text, 12, '0'))::uuid,
       nhan_de, tac_gia, ('40000000-0000-4000-8000-' || lpad(loai::text, 12, '0'))::uuid
FROM (VALUES
  (1, 'Dế Mèn phiêu lưu ký', 'Tô Hoài', 1),
  (2, 'Đất rừng phương Nam', 'Đoàn Giỏi', 1),
  (3, 'Quê nội', 'Võ Quảng', 1),
  (4, 'Tuổi thơ dữ dội', 'Phùng Quán', 1),
  (5, 'Hoàng tử bé', 'Antoine de Saint-Exupéry', 1),
  (6, 'Những tấm lòng cao cả', 'Edmondo De Amicis', 1),
  (7, 'Alice ở xứ sở thần tiên', 'Lewis Carroll', 1),
  (8, 'Không gia đình', 'Hector Malot', 1),
  (9, 'Những cuộc phiêu lưu của Tom Sawyer', 'Mark Twain', 1),
  (10, 'Hòn đảo bí mật', 'Jules Verne', 1),
  (11, 'Vũ trụ', 'Carl Sagan', 2),
  (12, 'Lược sử thời gian', 'Stephen Hawking', 2),
  (13, 'Nguồn gốc các loài', 'Charles Darwin', 2),
  (14, 'Lược sử vạn vật', 'Bill Bryson', 2),
  (15, 'Cấu trúc của các cuộc cách mạng khoa học', 'Thomas S. Kuhn', 2),
  (16, 'Tư duy nhanh và chậm', 'Daniel Kahneman', 3),
  (17, 'Thói quen nguyên tử', 'James Clear', 3),
  (18, 'Tư duy đa chiều', 'Edward de Bono', 3),
  (19, 'Nghệ thuật tư duy rành mạch', 'Rolf Dobelli', 3),
  (20, 'Đọc sách như một nghệ thuật', 'Mortimer J. Adler và Charles Van Doren', 3)
) AS titles(n, nhan_de, tac_gia, loai)
ON CONFLICT (id) DO NOTHING;

INSERT INTO ban_sach (id, sach_id, ma_ca_biet, vi_tri_ke, tinh_trang, trang_thai)
SELECT ('50000000-0000-4000-8000-' || lpad(n::text, 12, '0'))::uuid,
       ('30000000-0000-4000-8000-' || lpad(((n + 1) / 2)::text, 12, '0'))::uuid,
       'BS' || lpad(n::text, 4, '0'),
       'A1-' || lpad(((n + 1) / 2)::text, 2, '0'),
       CASE WHEN n = 40 THEN 'HONG' ELSE 'TOT' END,
       CASE WHEN n IN (2, 5, 6, 7) THEN 'DANG_MUON' WHEN n = 40 THEN 'HONG' ELSE 'RANH' END
FROM generate_series(1, 40) AS n
ON CONFLICT (id) DO NOTHING;

INSERT INTO ban_doc (id, nguoi_dung_id, ma_the, ho_ten, lop_khoa, han_the, trang_thai)
SELECT ('20000000-0000-4000-8000-' || lpad(n::text, 12, '0'))::uuid,
       u.id, 'BD' || lpad(n::text, 4, '0'), u.ho_ten, 'Lớp Demo',
       (now() AT TIME ZONE 'Asia/Ho_Chi_Minh')::date + CASE WHEN n = 3 THEN -1 ELSE 365 END,
       CASE WHEN n = 4 THEN 'KHOA' ELSE 'BINH_THUONG' END
FROM generate_series(1, 10) AS n
JOIN nguoi_dung u ON u.id = ('10000000-0000-4000-8000-' || lpad((n + 2)::text, 12, '0'))::uuid
ON CONFLICT (id) DO NOTHING;

INSERT INTO phieu_muon (id, ban_doc_id, ban_sach_id, muon_luc, han_tra, tra_luc, trang_thai)
SELECT ('60000000-0000-4000-8000-' || lpad(n::text, 12, '0'))::uuid,
       ('20000000-0000-4000-8000-' || lpad(reader::text, 12, '0'))::uuid,
       ('50000000-0000-4000-8000-' || lpad(copy::text, 12, '0'))::uuid,
       now() - age_hours * INTERVAL '1 hour',
       now() + (336 - age_hours) * INTERVAL '1 hour',
       CASE WHEN returned_hours IS NOT NULL THEN now() - returned_hours * INTERVAL '1 hour' END,
       CASE WHEN returned_hours IS NULL THEN 'DANG_MUON' ELSE 'DA_TRA' END
FROM (VALUES (1, 2, 2, 48, NULL::integer), (3, 5, 3, 480, 48),
             (4, 6, 5, 24, NULL), (5, 6, 6, 48, NULL), (6, 7, 7, 360, NULL),
             (7, 5, 4, 480, 48)) AS loans(n, reader, copy, age_hours, returned_hours)
ON CONFLICT (id) DO NOTHING;

INSERT INTO phat (id, phieu_muon_id, so_ngay_qua_han, so_tien, da_thu, thu_luc) VALUES
  ('70000000-0000-4000-8000-000000000001', '60000000-0000-4000-8000-000000000003', 4, 12000, false, NULL),
  ('70000000-0000-4000-8000-000000000002', '60000000-0000-4000-8000-000000000007', 4, 12000, true, now() - INTERVAL '24 hours')
ON CONFLICT (id) DO NOTHING;
COMMIT;
