package com.teamfifai.libraryadmin.data.dto;

public class LoginRequestDto {
    public String email;
    public String matKhau;

    public LoginRequestDto(String email, String matKhau) {
        this.email = email;
        this.matKhau = matKhau;
    }
}
