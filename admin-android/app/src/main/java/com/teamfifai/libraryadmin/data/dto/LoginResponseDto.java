package com.teamfifai.libraryadmin.data.dto;

public class LoginResponseDto {
    public Data data;

    public static class Data {
        public String token;
        public UserDto nguoiDung;
    }
}
