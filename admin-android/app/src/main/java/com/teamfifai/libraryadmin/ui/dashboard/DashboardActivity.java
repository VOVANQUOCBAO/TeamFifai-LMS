
package com.teamfifai.libraryadmin.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.teamfifai.libraryadmin.R;
import com.teamfifai.libraryadmin.ui.catalog.books.BooksActivity;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        TextView booksButton = findViewById(R.id.btnBooks);

        booksButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, BooksActivity.class);
            startActivity(intent);
        });
    }
}
