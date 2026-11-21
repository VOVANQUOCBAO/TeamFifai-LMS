package com.teamfifai.libraryadmin.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/** Backend gửi UTC ISO 8601; app hiển thị giờ Asia/Ho_Chi_Minh. */
public final class Formatters {
    public static final TimeZone VN = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
    private static final String[] UTC_PATTERNS = {
        "yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
    };

    private Formatters() {}

    public static Date parseUtc(String iso) {
        if (iso == null) return null;
        for (String pattern : UTC_PATTERNS) {
            SimpleDateFormat f = new SimpleDateFormat(pattern, Locale.US);
            f.setTimeZone(TimeZone.getTimeZone("UTC"));
            f.setLenient(false);
            try {
                return f.parse(iso);
            } catch (ParseException ignored) {
                // thử mẫu tiếp theo
            }
        }
        return null;
    }

    public static String vnDateTime(String iso) {
        Date date = parseUtc(iso);
        if (date == null) return iso == null ? "" : iso;
        SimpleDateFormat out = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.US);
        out.setTimeZone(VN);
        return out.format(date);
    }

    /** Ngày (yyyy-MM-dd) theo giờ Việt Nam, dùng so sánh hạn thẻ. */
    public static String dateVn(Date instant) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        f.setTimeZone(VN);
        return f.format(instant);
    }

    public static String vnd(long amount) {
        return NumberFormat.getIntegerInstance(new Locale("vi", "VN")).format(amount) + " đ";
    }
}
