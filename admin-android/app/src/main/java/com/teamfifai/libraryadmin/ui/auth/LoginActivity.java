package com.teamfifai.libraryadmin.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.teamfifai.libraryadmin.session.SessionManager;
import com.teamfifai.libraryadmin.ui.dashboard.DashboardActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPassword;
    private SessionManager session;

    private final int navy = Color.rgb(20, 39, 82);
    private final int blue = Color.rgb(43, 91, 190);
    private final int gray = Color.rgb(105, 116, 135);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(navy);
        getWindow().setNavigationBarColor(navy);

        session = new SessionManager(this);

        if (session.canUseLibraryFunctions()) {
            openDashboard();
            return;
        }

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(navy);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(dp(24), dp(28), dp(24), dp(24));
        scroll.addView(root);

        TextView logo = new TextView(this);
        logo.setText("📚");
        logo.setTextSize(48);
        logo.setGravity(Gravity.CENTER);
        root.addView(logo);

        TextView brand = new TextView(this);
        brand.setText("TEAMFIFAI");
        brand.setTextSize(27);
        brand.setTextColor(Color.WHITE);
        brand.setTypeface(null, Typeface.BOLD);
        brand.setGravity(Gravity.CENTER);
        brand.setPadding(0, dp(10), 0, dp(4));
        root.addView(brand);

        TextView description = new TextView(this);
        description.setText("HỆ THỐNG QUẢN LÝ THƯ VIỆN");
        description.setTextSize(12);
        description.setTextColor(Color.rgb(205, 216, 238));
        description.setGravity(Gravity.CENTER);
        root.addView(description);

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(22), dp(26), dp(22), dp(24));
        card.setBackground(roundRect(Color.WHITE, 22));

        LinearLayout.LayoutParams cardParams =
            new LinearLayout.LayoutParams(-1, -2);
        cardParams.setMargins(0, dp(34), 0, dp(20));
        root.addView(card, cardParams);

        TextView heading = new TextView(this);
        heading.setText("Đăng nhập");
        heading.setTextSize(24);
        heading.setTextColor(navy);
        heading.setTypeface(null, Typeface.BOLD);
        card.addView(heading);

        TextView hint = new TextView(this);
        hint.setText("Vui lòng đăng nhập để tiếp tục");
        hint.setTextSize(14);
        hint.setTextColor(gray);
        hint.setPadding(0, dp(6), 0, dp(24));
        card.addView(hint);

        addLabel(card, "TÊN ĐĂNG NHẬP");

        edtUsername = new EditText(this);
        edtUsername.setSingleLine(true);
        edtUsername.setTextSize(15);
        edtUsername.setTextColor(navy);
        edtUsername.setHintTextColor(Color.rgb(155, 163, 177));
        edtUsername.setHint("Nhập tên đăng nhập");
        edtUsername.setPadding(dp(14), 0, dp(14), 0);
        edtUsername.setBackground(roundRect(Color.rgb(244, 246, 250), 12));
        card.addView(edtUsername, new LinearLayout.LayoutParams(-1, dp(52)));

        TextView passwordLabel = new TextView(this);
        passwordLabel.setText("MẬT KHẨU");
        passwordLabel.setTextSize(12);
        passwordLabel.setTextColor(navy);
        passwordLabel.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams labelParams =
            new LinearLayout.LayoutParams(-1, -2);
        labelParams.setMargins(0, dp(20), 0, dp(8));
        card.addView(passwordLabel, labelParams);

        edtPassword = new EditText(this);
        edtPassword.setSingleLine(true);
        edtPassword.setTextSize(15);
        edtPassword.setTextColor(navy);
        edtPassword.setHintTextColor(Color.rgb(155, 163, 177));
        edtPassword.setHint("Nhập mật khẩu");
        edtPassword.setInputType(129);
        edtPassword.setPadding(dp(14), 0, dp(14), 0);
        edtPassword.setBackground(roundRect(Color.rgb(244, 246, 250), 12));
        card.addView(edtPassword, new LinearLayout.LayoutParams(-1, dp(52)));

        Button login = new Button(this);
        login.setText("ĐĂNG NHẬP");
        login.setTextSize(15);
        login.setTextColor(Color.WHITE);
        login.setTypeface(null, Typeface.BOLD);
        login.setAllCaps(false);
        login.setBackground(roundRect(blue, 12));

        LinearLayout.LayoutParams buttonParams =
            new LinearLayout.LayoutParams(-1, dp(52));
        buttonParams.setMargins(0, dp(28), 0, 0);
        card.addView(login, buttonParams);

        TextView footer = new TextView(this);
        footer.setText("© TeamFifai Library Management");
        footer.setTextSize(12);
        footer.setTextColor(Color.rgb(190, 204, 231));
        footer.setGravity(Gravity.CENTER);
        root.addView(footer);

        setContentView(scroll);

        login.setOnClickListener(v -> doLogin());
        edtPassword.setOnEditorActionListener((v, actionId, event) -> {
            doLogin();
            return true;
        });
    }

    private void addLabel(LinearLayout parent, String text) {
        TextView label = new TextView(this);
        label.setText(text);
        label.setTextSize(12);
        label.setTextColor(navy);
        label.setTypeface(null, Typeface.BOLD);

        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(-1, -2);
        params.setMargins(0, 0, 0, dp(8));
        parent.addView(label, params);
    }

    private GradientDrawable roundRect(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        return drawable;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private void doLogin() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty()) {
            edtUsername.setError("Vui lòng nhập tài khoản");
            return;
        }

        if (password.isEmpty()) {
            edtPassword.setError("Vui lòng nhập mật khẩu");
            return;
        }

        String role;

        if ("admin".equals(username) && "admin123".equals(password)) {
            role = "ADMIN";
        } else if ("thuthu".equals(username) && "thu123".equals(password)) {
            role = "THU_THU";
        } else {
            Toast.makeText(this,
                "Tài khoản hoặc mật khẩu không đúng",
                Toast.LENGTH_SHORT).show();
            return;
        }

        session.save("DEMO_TOKEN", role, "");
        openDashboard();
    }

    private void openDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK |
            Intent.FLAG_ACTIVITY_CLEAR_TASK
        );
        startActivity(intent);
        finish();
    }
}
