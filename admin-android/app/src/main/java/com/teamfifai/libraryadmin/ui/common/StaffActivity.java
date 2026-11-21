package com.teamfifai.libraryadmin.ui.common;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.teamfifai.libraryadmin.R;
import com.teamfifai.libraryadmin.config.AppConfig;
import com.teamfifai.libraryadmin.data.repository.AppError;
import com.teamfifai.libraryadmin.session.SessionManager;
import com.teamfifai.libraryadmin.ui.auth.LoginActivity;

/** Màn hình cần đăng nhập ADMIN/THU_THU: kiểm tra phiên, nhãn DEMO, xử lý 401/403 chung. */
public abstract class StaffActivity extends AppCompatActivity {
    protected SessionManager session;

    /** Gọi đầu onCreate; trả false nếu đã chuyển về màn đăng nhập. */
    protected boolean requireStaffSession() {
        session = new SessionManager(this);
        if (!session.canUseLibraryFunctions()) {
            goToLogin(null);
            return false;
        }
        return true;
    }

    protected void bindDemoBanner() {
        View banner = findViewById(R.id.demoBanner);
        if (banner != null) banner.setVisibility(AppConfig.isMock() ? View.VISIBLE : View.GONE);
    }

    /** Trả true nếu lỗi đã được xử lý bằng cách đăng xuất (token hết hạn hoặc mất quyền). */
    protected boolean handleAuthError(AppError error) {
        if (error.isSessionExpired() || "TAI_KHOAN_BI_KHOA".equals(error.code)) {
            goToLogin(error.userMessage());
            return true;
        }
        return false;
    }

    protected void logout() {
        goToLogin(null);
    }

    private void goToLogin(String message) {
        if (session == null) session = new SessionManager(this);
        session.clear();
        if (message != null) Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
