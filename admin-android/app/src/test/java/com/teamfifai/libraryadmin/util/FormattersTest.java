package com.teamfifai.libraryadmin.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.Date;

public class FormattersTest {
    @Test
    public void hanTraUtcHienThiTheoGioVietNam() {
        // Fixture create-loan-response: hanTra 2026-11-29T03:00:00Z -> 10:00 giờ Việt Nam.
        assertEquals("10:00 29/11/2026", Formatters.vnDateTime("2026-11-29T03:00:00Z"));
        assertEquals("10:00 29/11/2026", Formatters.vnDateTime("2026-11-29T03:00:00.000Z"));
    }

    @Test
    public void ngayTheoGioVietNamQuaNuaDem() {
        // 17:30 UTC ngày 20 là 00:30 ngày 21 ở Việt Nam: hạn thẻ phải so theo ngày VN.
        Date instant = Formatters.parseUtc("2026-11-20T17:30:00Z");
        assertEquals("2026-11-21", Formatters.dateVn(instant));
    }

    @Test
    public void chuoiKhongHopLeKhongBiDoanBua() {
        assertNull(Formatters.parseUtc("29/11/2026"));
        assertEquals("29/11/2026", Formatters.vnDateTime("29/11/2026"));
    }
}
