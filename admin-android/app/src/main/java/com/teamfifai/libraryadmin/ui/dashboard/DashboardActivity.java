package com.teamfifai.libraryadmin.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.teamfifai.libraryadmin.R;
import com.teamfifai.libraryadmin.session.SessionManager;
import com.teamfifai.libraryadmin.ui.catalog.books.BooksActivity;
import com.teamfifai.libraryadmin.ui.catalog.categories.CategoriesActivity;
import com.teamfifai.libraryadmin.ui.readers.ReadersActivity;
import com.teamfifai.libraryadmin.ui.loans.LoansActivity;
import com.teamfifai.libraryadmin.ui.fines.FinesActivity;
import com.teamfifai.libraryadmin.ui.reports.ReportsActivity;

public class DashboardActivity extends AppCompatActivity {
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        session = new SessionManager(this);

        if (!session.canUseLibraryFunctions()) {
            session.clear();
            finish();
            return;
        }

        findViewById(R.id.btnBooks).setOnClickListener(v ->
            startActivity(new Intent(this, BooksActivity.class)));

        findViewById(R.id.btnCategories).setOnClickListener(v ->
            startActivity(new Intent(this, CategoriesActivity.class)));

        findViewById(R.id.btnReaders).setOnClickListener(v ->
            startActivity(new Intent(this, ReadersActivity.class)));

        findViewById(R.id.btnLoans).setOnClickListener(v ->
            startActivity(new Intent(this, LoansActivity.class)));

        findViewById(R.id.btnFines).setOnClickListener(v ->
            startActivity(new Intent(this, FinesActivity.class)));

        findViewById(R.id.btnReports).setOnClickListener(v ->
            startActivity(new Intent(this, ReportsActivity.class)));

        addRoleControls();
    }

    private void addRoleControls() {
        View contentRoot = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        LinearLayout content;

        if (contentRoot instanceof ScrollView) {
            content = (LinearLayout) ((ScrollView) contentRoot).getChildAt(0);
        } else if (contentRoot instanceof LinearLayout) {
            content = (LinearLayout) contentRoot;
        } else {
            return;
        }


        if (session.isAdmin()) {
            addActionButton(content, "Quản lý nhân viên", "Chức năng này chỉ dành cho ADMIN.");
            addActionButton(content, "Cấu hình biểu phạt", "Chức năng này chỉ dành cho ADMIN.");
        }

        addActionButton(content, "Đăng xuất", "Bạn có muốn đăng xuất không?", true);
    }

    private void addActionButton(LinearLayout content, String title, String message) {
        addActionButton(content, title, message, false);
    }

    private void addActionButton(LinearLayout content, String title, String message, boolean logout) {
        Button button = new Button(this);
        button.setText(title);
        content.addView(button, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        button.setOnClickListener(v -> {
            if (logout) {
                new AlertDialog.Builder(this)
                    .setTitle("Đăng xuất")
                    .setMessage(message)
                    .setNegativeButton("Hủy", null)
                    .setPositiveButton("Đăng xuất", (dialog, which) -> {
                        session.clear();
                        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                        if (intent != null) {
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        finish();
                    })
                    .show();
            } else {
                new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("Đóng", null)
                    .show();
            }
        });
    }
}
