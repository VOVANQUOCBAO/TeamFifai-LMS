package com.teamfifai.libraryadmin.data.dto;

/** Body POST /phieu-muon: chỉ banDocId + banSachId; hạn trả do backend tính. */
public class CreateLoanRequestDto {
    public final String banDocId;
    public final String banSachId;

    public CreateLoanRequestDto(String banDocId, String banSachId) {
        this.banDocId = banDocId;
        this.banSachId = banSachId;
    }
}
