package com.teamfifai.libraryadmin.data.dto;

/** Envelope data/meta/error theo hop dong noi bo (cho nguoi 1/2 xac nhan). */
public class ApiResponse<T> {
    public T data;
    public Meta meta;
    public Error error;

    public static class Meta { public int page; public int pageSize; public int total; }
    public static class Error { public String code; public String message; }
}
