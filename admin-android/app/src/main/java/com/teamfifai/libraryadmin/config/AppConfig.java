package com.teamfifai.libraryadmin.config;

import com.teamfifai.libraryadmin.BuildConfig;

/** Chế độ mock/live và URL backend lấy từ BuildConfig (tham số Gradle appMode, apiBaseUrl). */
public final class AppConfig {
    private AppConfig() {}

    public static boolean isMock() {
        return !"live".equals(BuildConfig.APP_MODE);
    }

    public static String apiBaseUrl() {
        return BuildConfig.API_BASE_URL;
    }

    /** Khóa phiên theo chế độ + URL để token mock không bị dùng lại ở live và ngược lại. */
    public static String sessionScope() {
        return BuildConfig.APP_MODE + "|" + BuildConfig.API_BASE_URL;
    }
}
