package com.teamfifai.libraryadmin.ui.fines;

import android.os.Bundle;
import android.graphics.Color;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.teamfifai.libraryadmin.data.demo.DemoAdminData;

public class FinesActivity extends AppCompatActivity {
    private LinearLayout list;

    private int dp(int n) {
        return (int) (n * getResources().getDisplayMetrics().density);
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

        root.addView(text("QUẢN LÝ TIỀN PHẠT", 23));
        root.addView(text("Theo dõi khoản phạt và thanh toán", 14));

        ScrollView scroll = new ScrollView(this);
        list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(list);
        root.addView(scroll);

        setContentView(root);
        render();
    }

    private void render() {
        list.removeAllViews();

        TextView summary = text(
            "Số khoản chưa thanh toán: " + DemoAdminData.countUnpaidFines()
            + "\nTổng tiền còn nợ: "
            + String.format("%,d", DemoAdminData.totalUnpaidAmount()) + " VNĐ",
            17
        );
        list.addView(summary);

        for (DemoAdminData.Fine fine : DemoAdminData.fines) {
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(dp(14), dp(10), dp(14), dp(10));
            card.setBackgroundColor(Color.WHITE);

            card.addView(text("Bạn đọc: " + fine.reader, 16));
            card.addView(text("Lý do: " + fine.reason, 14));
            card.addView(text("Số tiền: " + String.format("%,d", fine.amount) + " VNĐ", 14));
            card.addView(text("Trạng thái: " + (fine.paid ? "Đã thanh toán" : "Chưa thanh toán"), 14));

            if (!fine.paid) {
                Button pay = new Button(this);
                pay.setText("Xác nhận đã thanh toán");
                card.addView(pay);
                pay.setOnClickListener(v -> {
                    fine.paid = true;
                    render();
                    Toast.makeText(this, "Đã cập nhật thanh toán demo", Toast.LENGTH_SHORT).show();
                });
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
            params.bottomMargin = dp(12);
            list.addView(card, params);
        }
    }
}
