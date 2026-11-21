package com.teamfifai.libraryadmin.data.dto;

/** {"error": {"code": "...", "message": "..."}} */
public class ErrorResponseDto {
    public Body error;

    public static class Body {
        public String code;
        public String message;
    }
}
