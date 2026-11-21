package com.teamfifai.libraryadmin.data.demo;

import java.util.ArrayList;
import java.util.List;

public class DemoAdminData {
    public static class Loan {
        public String reader;
        public String book;
        public String date;
        public String status;

        public Loan(String reader, String book, String date, String status) {
            this.reader = reader;
            this.book = book;
            this.date = date;
            this.status = status;
        }
    }

    public static class Fine {
        public String reader;
        public String reason;
        public int amount;
        public boolean paid;

        public Fine(String reader, String reason, int amount, boolean paid) {
            this.reader = reader;
            this.reason = reason;
            this.amount = amount;
            this.paid = paid;
        }
    }

    public static final List<Loan> loans = new ArrayList<>();
    public static final List<Fine> fines = new ArrayList<>();

    static {
        loans.add(new Loan("Nguyễn Văn An", "Lập trình Java cơ bản", "20/11/2026", "Đang mượn"));
        loans.add(new Loan("Trần Thị Bình", "Cấu trúc dữ liệu", "19/11/2026", "Đang mượn"));
        loans.add(new Loan("Lê Minh Cường", "Phát triển Android", "15/11/2026", "Đã trả"));

        fines.add(new Fine("Nguyễn Văn An", "Trả sách trễ hạn", 20000, false));
        fines.add(new Fine("Trần Thị Bình", "Làm hư hỏng sách", 50000, false));
        fines.add(new Fine("Lê Minh Cường", "Đã thanh toán", 10000, true));
    }

    public static int countBorrowing() {
        int count = 0;
        for (Loan loan : loans) {
            if ("Đang mượn".equals(loan.status)) count++;
        }
        return count;
    }

    public static int countUnpaidFines() {
        int count = 0;
        for (Fine fine : fines) {
            if (!fine.paid) count++;
        }
        return count;
    }

    public static int totalUnpaidAmount() {
        int total = 0;
        for (Fine fine : fines) {
            if (!fine.paid) total += fine.amount;
        }
        return total;
    }
}
