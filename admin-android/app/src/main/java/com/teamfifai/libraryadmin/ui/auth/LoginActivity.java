package com.teamfifai.libraryadmin.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.teamfifai.libraryadmin.R;
import com.teamfifai.libraryadmin.config.AppConfig;
import com.teamfifai.libraryadmin.session.SessionManager;
import com.teamfifai.libraryadmin.ui.catalog.books.BooksActivity;

/** Màn hình 1: đăng nhập, kiểm tra form, chỉ ADMIN/THU_THU được vào. */
public class LoginActivity extends AppCompatActivity {
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private TextInputEditText edtEmail;
    private TextInputEditText edtPassword;
    private TextView txtError;
    private Button btnLogin;
    private ProgressBar progress;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (new SessionManager(this).canUseLibraryFunctions()) {
            openBooks();
            return;
        }
        setContentView(R.layout.activity_login);
        setTitle(R.string.app_name);

        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        txtError = findViewById(R.id.txtError);
        btnLogin = findViewById(R.id.btnLogin);
        progress = findViewById(R.id.progress);
        findViewById(R.id.demoBanner).setVisibility(AppConfig.isMock() ? View.VISIBLE : View.GONE);

        TextView hint = findViewById(R.id.txtHint);
        hint.setText(AppConfig.isMock()
            ? "Tài khoản demo: thuthu@thuviendemo.test hoặc admin@thuviendemo.test · mật khẩu DemoLib!4211"
            : "Máy chủ: " + AppConfig.apiBaseUrl());

        btnLogin.setOnClickListener(v -> submit());
        edtPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submit();
                return true;
            }
            return false;
        });

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.state().observe(this, this::render);
    }

    private void submit() {
        String email = text(edtEmail).trim();
        String password = text(edtPassword);
        boolean valid = true;
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Email không hợp lệ");
            valid = false;
        } else {
            tilEmail.setError(null);
        }
        if (password.isEmpty()) {
            tilPassword.setError("Vui lòng nhập mật khẩu");
            valid = false;
        } else {
            tilPassword.setError(null);
        }
        if (valid) viewModel.login(email, password);
    }

    private void render(LoginViewModel.State state) {
        if (state.loggedIn) {
            openBooks();
            return;
        }
        progress.setVisibility(state.loading ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!state.loading);
        edtEmail.setEnabled(!state.loading);
        edtPassword.setEnabled(!state.loading);
        txtError.setVisibility(state.errorMessage == null ? View.GONE : View.VISIBLE);
        txtError.setText(state.errorMessage);
    }

    private void openBooks() {
        Intent intent = new Intent(this, BooksActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private static String text(TextInputEditText field) {
        return field.getText() == null ? "" : field.getText().toString();
    }
}
