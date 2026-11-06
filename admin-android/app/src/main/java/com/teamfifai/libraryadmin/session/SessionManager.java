package com.teamfifai.libraryadmin.session;

import android.content.Context;
import android.content.SharedPreferences;

/** Luu token/vaiTro/expiresAt. Khong log token. */
public class SessionManager {
    private static final String PREF = "session";
    private final SharedPreferences sp;

    public SessionManager(Context ctx) {
        sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }
    public void save(String token, String vaiTro, String expiresAt) {
        sp.edit().putString("token", token).putString("vaiTro", vaiTro)
                .putString("expiresAt", expiresAt).apply();
    }
    public String getToken() { return sp.getString("token", null); }
    public String getVaiTro() { return sp.getString("vaiTro", null); }
    public void clear() { sp.edit().clear().apply(); }
}
