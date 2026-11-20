BEGIN;
SET LOCAL search_path = public;
SELECT pg_advisory_xact_lock(4211, 21);

CREATE TABLE IF NOT EXISTS nguoi_dung (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  email TEXT NOT NULL UNIQUE CHECK (email <> '' AND email = lower(btrim(email))),
  mat_khau_hash TEXT NOT NULL,
  ho_ten TEXT NOT NULL,
  vai_tro TEXT NOT NULL CHECK (vai_tro IN ('ADMIN', 'THU_THU', 'BAN_DOC')),
  trang_thai TEXT NOT NULL CHECK (trang_thai IN ('HOAT_DONG', 'KHOA'))
);

CREATE TABLE IF NOT EXISTS phan_loai (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  ma TEXT NOT NULL UNIQUE,
  ten TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS sach (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  nhan_de TEXT NOT NULL,
  tac_gia TEXT NOT NULL,
  nxb TEXT,
  nam_xb INTEGER CHECK (nam_xb BETWEEN 0 AND 9999),
  isbn TEXT UNIQUE,
  phan_loai_id UUID NOT NULL REFERENCES phan_loai(id),
  anh_bia TEXT
);
CREATE INDEX IF NOT EXISTS sach_phan_loai_idx ON sach (phan_loai_id);

CREATE TABLE IF NOT EXISTS ban_sach (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  sach_id UUID NOT NULL REFERENCES sach(id),
  ma_ca_biet TEXT NOT NULL UNIQUE,
  vi_tri_ke TEXT NOT NULL,
  tinh_trang TEXT NOT NULL CHECK (tinh_trang IN ('TOT', 'HONG', 'MAT')),
  trang_thai TEXT NOT NULL CHECK (trang_thai IN ('RANH', 'DANG_MUON', 'GIU_CHO', 'HONG')),
  UNIQUE (sach_id, id)
);
CREATE INDEX IF NOT EXISTS ban_sach_ranh_idx ON ban_sach (sach_id)
  WHERE tinh_trang = 'TOT' AND trang_thai = 'RANH';

CREATE TABLE IF NOT EXISTS ban_doc (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  nguoi_dung_id UUID NOT NULL UNIQUE REFERENCES nguoi_dung(id),
  ma_the TEXT NOT NULL UNIQUE,
  ho_ten TEXT NOT NULL,
  lop_khoa TEXT NOT NULL,
  han_the DATE NOT NULL,
  trang_thai TEXT NOT NULL CHECK (trang_thai IN ('BINH_THUONG', 'KHOA')),
  gioi_han_muon INTEGER NOT NULL DEFAULT 3 CHECK (gioi_han_muon > 0)
);

CREATE TABLE IF NOT EXISTS phieu_muon (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  ban_doc_id UUID NOT NULL REFERENCES ban_doc(id),
  ban_sach_id UUID NOT NULL REFERENCES ban_sach(id),
  muon_luc TIMESTAMPTZ NOT NULL,
  han_tra TIMESTAMPTZ NOT NULL CHECK (han_tra > muon_luc),
  tra_luc TIMESTAMPTZ CHECK (tra_luc >= muon_luc),
  so_lan_gia_han INTEGER NOT NULL DEFAULT 0 CHECK (so_lan_gia_han >= 0),
  trang_thai TEXT NOT NULL CHECK (trang_thai IN ('DANG_MUON', 'DA_TRA')),
  CHECK ((trang_thai = 'DANG_MUON' AND tra_luc IS NULL)
      OR (trang_thai = 'DA_TRA' AND tra_luc IS NOT NULL))
);
CREATE UNIQUE INDEX IF NOT EXISTS phieu_muon_mo_ban_sach_uk ON phieu_muon (ban_sach_id)
  WHERE trang_thai = 'DANG_MUON' AND tra_luc IS NULL;
CREATE INDEX IF NOT EXISTS phieu_muon_ban_sach_idx ON phieu_muon (ban_sach_id);
CREATE INDEX IF NOT EXISTS phieu_muon_ban_doc_trang_thai_idx ON phieu_muon (ban_doc_id, trang_thai);
CREATE INDEX IF NOT EXISTS phieu_muon_han_tra_idx ON phieu_muon (han_tra);

CREATE TABLE IF NOT EXISTS bieu_phat (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  so_ngay_tu INTEGER NOT NULL CHECK (so_ngay_tu >= 1),
  so_ngay_den INTEGER CHECK (so_ngay_den >= so_ngay_tu),
  tien_moi_ngay INTEGER NOT NULL CHECK (tien_moi_ngay >= 0)
);

CREATE TABLE IF NOT EXISTS phat (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  phieu_muon_id UUID NOT NULL UNIQUE REFERENCES phieu_muon(id),
  so_ngay_qua_han INTEGER NOT NULL CHECK (so_ngay_qua_han >= 0),
  so_tien INTEGER NOT NULL CHECK (so_tien >= 0),
  da_thu BOOLEAN NOT NULL DEFAULT false,
  thu_luc TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS dat_truoc (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  sach_id UUID NOT NULL REFERENCES sach(id),
  ban_doc_id UUID NOT NULL REFERENCES ban_doc(id),
  ban_sach_id UUID REFERENCES ban_sach(id),
  dat_luc TIMESTAMPTZ NOT NULL,
  trang_thai TEXT NOT NULL CHECK (trang_thai IN ('DANG_CHO', 'DANG_GIU', 'DA_NHAN', 'DA_HUY', 'HET_HAN')),
  giu_den_luc TIMESTAMPTZ,
  CHECK (trang_thai <> 'DANG_GIU' OR (ban_sach_id IS NOT NULL AND giu_den_luc IS NOT NULL)),
  FOREIGN KEY (sach_id, ban_sach_id) REFERENCES ban_sach(sach_id, id)
);
CREATE INDEX IF NOT EXISTS dat_truoc_hang_doi_idx ON dat_truoc (sach_id, dat_luc, id)
  WHERE trang_thai = 'DANG_CHO';
CREATE INDEX IF NOT EXISTS dat_truoc_ban_doc_idx ON dat_truoc (ban_doc_id);

CREATE TABLE IF NOT EXISTS nhat_ky_thu_vien (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  loai_su_kien TEXT NOT NULL,
  ban_doc_id UUID REFERENCES ban_doc(id),
  ban_sach_id UUID REFERENCES ban_sach(id),
  phieu_muon_id UUID REFERENCES phieu_muon(id),
  thoi_diem TIMESTAMPTZ NOT NULL,
  nguoi_thuc_hien UUID NOT NULL REFERENCES nguoi_dung(id)
);
CREATE INDEX IF NOT EXISTS nhat_ky_thoi_diem_idx ON nhat_ky_thu_vien (thoi_diem);

-- Supabase PostgREST roles have no row policies. Express uses the PostgreSQL owner.
ALTER TABLE nguoi_dung ENABLE ROW LEVEL SECURITY;
ALTER TABLE phan_loai ENABLE ROW LEVEL SECURITY;
ALTER TABLE sach ENABLE ROW LEVEL SECURITY;
ALTER TABLE ban_sach ENABLE ROW LEVEL SECURITY;
ALTER TABLE ban_doc ENABLE ROW LEVEL SECURITY;
ALTER TABLE phieu_muon ENABLE ROW LEVEL SECURITY;
ALTER TABLE bieu_phat ENABLE ROW LEVEL SECURITY;
ALTER TABLE phat ENABLE ROW LEVEL SECURITY;
ALTER TABLE dat_truoc ENABLE ROW LEVEL SECURITY;
ALTER TABLE nhat_ky_thu_vien ENABLE ROW LEVEL SECURITY;
COMMIT;
