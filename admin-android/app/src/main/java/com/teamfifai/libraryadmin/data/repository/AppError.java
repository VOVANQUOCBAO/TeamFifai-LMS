package com.teamfifai.libraryadmin.data.repository;

/** Lỗi đã chuẩn hóa từ HTTP/mạng để UI hiển thị lý do, không báo thành công giả. */
public class AppError {
    public static final int NETWORK = -1;
    public static final int BAD_RESPONSE = -2;

    public final int httpStatus;
    public final String code;
    public final String serverMessage;

    public AppError(int httpStatus, String code, String serverMessage) {
        this.httpStatus = httpStatus;
        this.code = code == null ? "LOI_HE_THONG" : code;
        this.serverMessage = serverMessage;
    }

    public static AppError network() {
        return new AppError(NETWORK, "LOI_MANG", null);
    }

    public static AppError badResponse() {
        return new AppError(BAD_RESPONSE, "PHAN_HOI_KHONG_HOP_LE", null);
    }

    /** 401 do token thiếu/sai/hết hạn: phải đăng nhập lại. Sai mật khẩu xử lý riêng ở màn đăng nhập. */
    public boolean isSessionExpired() {
        return httpStatus == 401 && "TOKEN_KHONG_HOP_LE".equals(code);
    }

    public boolean isPendingIntegration() {
        return httpStatus == 501;
    }

    public String userMessage() {
        switch (code) {
            case "LOI_MANG": return "Không kết nối được máy chủ. Kiểm tra mạng rồi thử lại.";
            case "PHAN_HOI_KHONG_HOP_LE": return "Máy chủ trả dữ liệu không đúng định dạng.";
            case "DU_LIEU_KHONG_HOP_LE": return "Dữ liệu gửi lên không hợp lệ.";
            case "SAI_THONG_TIN_DANG_NHAP": return "Email hoặc mật khẩu không đúng.";
            case "TOKEN_KHONG_HOP_LE": return "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.";
            case "TAI_KHOAN_BI_KHOA": return "Tài khoản đang bị khóa.";
            case "KHONG_CO_QUYEN": return "Bạn không có quyền thực hiện thao tác này.";
            case "KHONG_TIM_THAY": return "Không tìm thấy dữ liệu yêu cầu.";
            case "THE_HET_HAN": return "Không thể cho mượn: thẻ bạn đọc đã hết hạn.";
            case "BAN_DOC_BI_KHOA": return "Không thể cho mượn: bạn đọc đang bị khóa.";
            case "CON_NO_PHAT": return "Không thể cho mượn: bạn đọc còn nợ phạt.";
            case "VUOT_GIOI_HAN_MUON": return "Không thể cho mượn: bạn đọc đã đạt giới hạn số cuốn đang mượn.";
            case "BAN_SACH_KHONG_SAN_SANG": return "Không thể cho mượn: bản sách không còn sẵn sàng.";
            case "CHUC_NANG_CHUA_TICH_HOP":
                return "Chức năng đang chờ tích hợp backend (501). Chưa có thay đổi nào được ghi.";
            default:
                return serverMessage != null ? serverMessage : "Máy chủ gặp lỗi. Vui lòng thử lại.";
        }
    }
}
