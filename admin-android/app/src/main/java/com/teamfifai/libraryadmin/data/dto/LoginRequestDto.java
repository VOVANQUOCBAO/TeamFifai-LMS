package com.teamfifai.libraryadmin.data.dto;

public class LoginRequestDto {
    public final String email;
    public final String matKhau;

    public LoginRequestDto(String email, String matKhau) {
        this.email = email;
        this.matKhau = matKhau;
    }
}
