package com.teamfifai.libraryadmin.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.teamfifai.libraryadmin.config.AppConfig;

/** Lưu token JWT + vai trò; chỉ ADMIN/THU_THU được dùng app quản trị. */
public class SessionManager {
    private static final String PREF = "session";
    private final SharedPreferences sp;

    public SessionManager(Context ctx) {
        sp = ctx.getApplicationContext().getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public void save(String token, String vaiTro, String hoTen) {
        sp.edit()
            .putString("token", token)
            .putString("vaiTro", vaiTro)
            .putString("hoTen", hoTen)
            .putString("scope", AppConfig.sessionScope())
            .apply();
    }

    /** Token chỉ hợp lệ nếu được tạo cùng chế độ/URL đang chạy. */
    public String getToken() {
        if (!AppConfig.sessionScope().equals(sp.getString("scope", null))) return null;
        return sp.getString("token", null);
    }

    public String getVaiTro() {
        return getToken() == null ? null : sp.getString("vaiTro", null);
    }

    public String getHoTen() {
        return sp.getString("hoTen", "");
    }

    public boolean isAdmin() {
        return "ADMIN".equals(getVaiTro());
    }

    public boolean isThuThu() {
        return "THU_THU".equals(getVaiTro());
    }

    public boolean canManageStaff() {
        return isAdmin();
    }

    public boolean canManageFinePolicy() {
        return isAdmin();
    }

    public boolean canUseLibraryFunctions() {
        return isAdmin() || isThuThu();
    }

    public static boolean isStaffRole(String vaiTro) {
        return "ADMIN".equals(vaiTro) || "THU_THU".equals(vaiTro);
    }

    public void clear() {
        sp.edit().clear().apply();
    }
}
