
package com.teamfifai.libraryadmin.ui.catalog.books;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.teamfifai.libraryadmin.R;

public class BookDetailActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvIsbn;
    private TextView tvPublisher;
    private TextView tvYear;
    private TextView tvAvailable;
    private LinearLayout layoutCopies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        tvTitle = findViewById(R.id.tvDetailTitle);
        tvAuthor = findViewById(R.id.tvDetailAuthor);
        tvIsbn = findViewById(R.id.tvDetailIsbn);
        tvPublisher = findViewById(R.id.tvDetailPublisher);
        tvYear = findViewById(R.id.tvDetailYear);
        tvAvailable = findViewById(R.id.tvDetailAvailable);
        layoutCopies = findViewById(R.id.layoutBookCopies);

        loadBookInformation();
        loadDemoCopies();
    }

    private void loadBookInformation() {
        String title = getIntent().getStringExtra("book_title");
        String author = getIntent().getStringExtra("book_author");
        String isbn = getIntent().getStringExtra("book_isbn");
        String publisher = getIntent().getStringExtra("book_publisher");
        String year = getIntent().getStringExtra("book_year");
        int available = getIntent().getIntExtra("book_available", 0);

        tvTitle.setText(title == null ? "Chưa có tên sách" : title);
        tvAuthor.setText("Tác giả: " + safe(author));
        tvIsbn.setText("ISBN: " + safe(isbn));
        tvPublisher.setText("Nhà xuất bản: " + safe(publisher));
        tvYear.setText("Năm xuất bản: " + safe(year));
        tvAvailable.setText("Số bản còn rảnh: " + available);
    }

    private void loadDemoCopies() {
        String bookId = getIntent().getStringExtra("book_id");

        int available = getIntent().getIntExtra("book_available", 0);

        if (available <= 0) {
            TextView empty = createTextView("Hiện chưa có bản sách rảnh.");
            layoutCopies.addView(empty);
            return;
        }

        for (int i = 1; i <= available; i++) {
            String copyId = safe(bookId) + "_COPY_" + i;

            TextView item = createTextView(
                    "Mã bản sách: " + copyId
                            + "\nVị trí kệ: Kệ A"
                            + "\nTrạng thái: Có thể mượn"
            );

            layoutCopies.addView(item);
        }
    }

    private TextView createTextView(String content) {
        TextView textView = new TextView(this);
        textView.setText(content);
        textView.setTextSize(15);
        textView.setTextColor(0xFF333333);
        textView.setPadding(16, 16, 16, 16);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(0, 0, 0, 12);
        textView.setLayoutParams(params);
        textView.setBackgroundColor(0xFFF1F3F8);

        return textView;
    }

    private String safe(String value) {
        return value == null || value.trim().isEmpty()
                ? "Chưa cập nhật" : value;
    }
}
