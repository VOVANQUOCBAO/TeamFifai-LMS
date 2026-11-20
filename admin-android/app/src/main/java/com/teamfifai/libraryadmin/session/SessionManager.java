package com.teamfifai.libraryadmin.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF = "session";
    private final SharedPreferences sp;

    public SessionManager(Context ctx) {
        sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public void save(String token, String vaiTro, String expiresAt) {
        sp.edit()
            .putString("token", token)
            .putString("vaiTro", vaiTro)
            .putString("expiresAt", expiresAt)
            .apply();
    }

    public String getToken() {
        return sp.getString("token", null);
    }

    public String getVaiTro() {
        return sp.getString("vaiTro", null);
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(getVaiTro());
    }

    public boolean isThuThu() {
        return "THU_THU".equalsIgnoreCase(getVaiTro());
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

    public void clear() {
        sp.edit().clear().apply();
    }
}
