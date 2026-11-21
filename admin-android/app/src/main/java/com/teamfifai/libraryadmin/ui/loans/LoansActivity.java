package com.teamfifai.libraryadmin.ui.loans;

import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.teamfifai.libraryadmin.data.demo.DemoAdminData;

public class LoansActivity extends AppCompatActivity {
    private LinearLayout list;

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    private TextView text(String value, int size) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(Color.rgb(35, 48, 75));
        t.setPadding(0, dp(6), 0, dp(6));
        return t;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(20), dp(20), dp(20));
        root.setBackgroundColor(Color.rgb(245, 246, 250));

        root.addView(text("QUẢN LÝ MƯỢN / TRẢ", 23));
        root.addView(text("Theo dõi phiếu mượn và tình trạng sách", 14));

        Button add = new Button(this);
        add.setText("Tạo phiếu mượn");
        root.addView(add);
        add.setOnClickListener(v -> showAddDialog());

        ScrollView scroll = new ScrollView(this);
        list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(list);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));

        setContentView(root);
        render();
    }

    private void showAddDialog() {
        LinearLayout form = new LinearLayout(this);
        form.setOrientation(LinearLayout.VERTICAL);
        form.setPadding(dp(20), 0, dp(20), 0);

        EditText reader = new EditText(this);
        reader.setHint("Tên bạn đọc");
        form.addView(reader);

        EditText book = new EditText(this);
        book.setHint("Tên sách");
        form.addView(book);

        new AlertDialog.Builder(this)
            .setTitle("Tạo phiếu mượn")
            .setView(form)
            .setNegativeButton("Hủy", null)
            .setPositiveButton("Tạo", (dialog, which) -> {
                String r = reader.getText().toString().trim();
                String b = book.getText().toString().trim();

                if (r.isEmpty() || b.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                DemoAdminData.loans.add(
                    new DemoAdminData.Loan(r, b, "20/11/2026", "Đang mượn")
                );
                render();
                Toast.makeText(this, "Đã tạo phiếu mượn demo", Toast.LENGTH_SHORT).show();
            })
            .show();
    }

    private void render() {
        list.removeAllViews();

        for (int i = 0; i < DemoAdminData.loans.size(); i++) {
            final DemoAdminData.Loan loan = DemoAdminData.loans.get(i);

            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(dp(14), dp(10), dp(14), dp(10));
            card.setBackgroundColor(Color.WHITE);

            card.addView(text("Bạn đọc: " + loan.reader, 16));
            card.addView(text("Sách: " + loan.book, 15));
            card.addView(text("Ngày mượn: " + loan.date, 14));
            card.addView(text("Trạng thái: " + loan.status, 14));

            if ("Đang mượn".equals(loan.status)) {
                Button returned = new Button(this);
                returned.setText("Xác nhận trả sách");
                card.addView(returned);
                returned.setOnClickListener(v -> {
                    loan.status = "Đã trả";
                    render();
                    Toast.makeText(this, "Đã cập nhật trả sách", Toast.LENGTH_SHORT).show();
                });
            }

            LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);
            params.bottomMargin = dp(12);
            list.addView(card, params);
        }
    }
}
