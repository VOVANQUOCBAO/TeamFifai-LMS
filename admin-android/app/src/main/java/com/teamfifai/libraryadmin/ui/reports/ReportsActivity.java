package com.teamfifai.libraryadmin.ui.reports;

import android.os.Bundle;
import android.graphics.Color;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.teamfifai.libraryadmin.data.demo.DemoAdminData;

public class ReportsActivity extends AppCompatActivity {
    private int dp(int n) {
        return (int) (n * getResources().getDisplayMetrics().density);
    }

    private void addLine(LinearLayout root, String value, int size) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(Color.rgb(35, 48, 75));
        t.setPadding(0, dp(12), 0, dp(12));
        root.addView(t);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(20), dp(20), dp(20));
        root.setBackgroundColor(Color.rgb(245, 246, 250));

        addLine(root, "BÁO CÁO THỐNG KÊ", 23);
        addLine(root, "Tổng số phiếu mượn: " + DemoAdminData.loans.size(), 18);
        addLine(root, "Số phiếu đang mượn: " + DemoAdminData.countBorrowing(), 18);

        int returned = 0;
        for (DemoAdminData.Loan loan : DemoAdminData.loans) {
            if ("Đã trả".equals(loan.status)) returned++;
        }

        addLine(root, "Số phiếu đã trả: " + returned, 18);
        addLine(root, "Tổng số khoản phạt: " + DemoAdminData.fines.size(), 18);
        addLine(root, "Khoản phạt chưa thanh toán: " + DemoAdminData.countUnpaidFines(), 18);
        addLine(root, "Tổng tiền phạt còn nợ: "
            + String.format("%,d", DemoAdminData.totalUnpaidAmount()) + " VNĐ", 18);

        addLine(root, "Lưu ý: Đây là số liệu minh họa phục vụ chạy thử ứng dụng.", 14);

        setContentView(root);
    }
}
