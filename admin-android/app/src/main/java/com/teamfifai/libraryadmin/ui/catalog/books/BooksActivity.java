
package com.teamfifai.libraryadmin.ui.catalog.books;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

public class BooksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(24, 24, 24, 24);
        layout.setBackgroundColor(Color.rgb(245, 246, 250));

        TextView title = new TextView(this);
        title.setText("QUẢN LÝ SÁCH");
        title.setTextSize(24);
        title.setTextColor(Color.rgb(38, 59, 128));
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 20, 0, 30);

        TextView description = new TextView(this);
        description.setText("Danh sách sách trong thư viện");
        description.setTextSize(16);
        description.setTextColor(Color.DKGRAY);

        TextView addButton = new TextView(this);
        addButton.setText("+ Thêm sách");
        addButton.setTextSize(17);
        addButton.setTextColor(Color.WHITE);
        addButton.setGravity(Gravity.CENTER);
        addButton.setPadding(20, 25, 20, 25);
        addButton.setBackgroundColor(Color.rgb(38, 59, 128));

        TextView emptyMessage = new TextView(this);
        emptyMessage.setText("Chưa có dữ liệu sách.");
        emptyMessage.setTextSize(16);
        emptyMessage.setTextColor(Color.GRAY);
        emptyMessage.setGravity(Gravity.CENTER);
        emptyMessage.setPadding(0, 60, 0, 0);

        layout.addView(title);
        layout.addView(description);
        layout.addView(addButton);

        layout.addView(emptyMessage, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        setContentView(layout);
    }
}
