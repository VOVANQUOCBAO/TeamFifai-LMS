package com.teamfifai.libraryadmin.data.dto;

import java.util.List;

/** Bọc {"data": [...], "meta": {...}} theo OpenAPI v1.0.0. */
public class PageResponse<T> {
    public List<T> data;
    public Meta meta;

    public static class Meta {
        public int page;
        public int pageSize;
        public int total;
    }
}
