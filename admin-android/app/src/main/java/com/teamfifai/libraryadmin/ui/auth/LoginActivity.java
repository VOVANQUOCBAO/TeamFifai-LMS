
package com.teamfifai.libraryadmin.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.teamfifai.libraryadmin.R;
import com.teamfifai.libraryadmin.ui.dashboard.DashboardActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (username.isEmpty()) {
                edtUsername.setError("Bạn chưa nhập tài khoản");
                return;
            }

            if (password.isEmpty()) {
                edtPassword.setError("Bạn chưa nhập mật khẩu");
                return;
            }

            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
