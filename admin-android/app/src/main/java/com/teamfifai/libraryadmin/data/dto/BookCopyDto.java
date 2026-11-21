package com.teamfifai.libraryadmin.data.dto;

/** BookCopy: id ở đây chính là banSachId gửi khi cho mượn. */
public class BookCopyDto {
    public String id;
    public String sachId;
    public String maCaBiet;
    public String viTriKe;
    /** TOT | HONG | MAT */
    public String tinhTrang;
    /** RANH | DANG_MUON | GIU_CHO | HONG */
    public String trangThai;

    public boolean isAvailable() {
        return "RANH".equals(trangThai) && "TOT".equals(tinhTrang);
    }
}
