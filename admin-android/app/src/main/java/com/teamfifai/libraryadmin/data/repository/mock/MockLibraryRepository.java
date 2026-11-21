package com.teamfifai.libraryadmin.data.repository.mock;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.teamfifai.libraryadmin.data.dto.BookCopyDto;
import com.teamfifai.libraryadmin.data.dto.BookDetailDto;
import com.teamfifai.libraryadmin.data.dto.BookDto;
import com.teamfifai.libraryadmin.data.dto.DataResponse;
import com.teamfifai.libraryadmin.data.dto.ErrorResponseDto;
import com.teamfifai.libraryadmin.data.dto.LoanDto;
import com.teamfifai.libraryadmin.data.dto.LoginDataDto;
import com.teamfifai.libraryadmin.data.dto.PageResponse;
import com.teamfifai.libraryadmin.data.dto.ReaderDto;
import com.teamfifai.libraryadmin.data.repository.AppError;
import com.teamfifai.libraryadmin.data.repository.LibraryRepository;
import com.teamfifai.libraryadmin.session.SessionManager;
import com.teamfifai.libraryadmin.util.Formatters;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Chế độ DEMO: đọc bản sao contracts/fixtures trong assets, giữ trạng thái "đã mượn" trong bộ nhớ.
 * Áp dụng cùng thứ tự kiểm tra nghiệp vụ như backend để form mượn được nghiệm thu trước khi có live.
 */
public class MockLibraryRepository implements LibraryRepository {
    private static final long DELAY_MS = 350;

    private final Context context;
    private final SessionManager session;
    private final Gson gson = new Gson();
    private final Handler main = new Handler(Looper.getMainLooper());
    private boolean loanCreated;

    public MockLibraryRepository(Context context) {
        this.context = context.getApplicationContext();
        this.session = new SessionManager(this.context);
    }

    @Override
    public void login(String email, String matKhau, Callback<LoginDataDto> callback) {
        JsonObject ids = readJson("demo-ids.json").getAsJsonObject();
        for (JsonElement element : ids.getAsJsonArray("demoAccounts")) {
            JsonObject account = element.getAsJsonObject();
            boolean sameEmail = account.get("email").getAsString()
                .equalsIgnoreCase(email == null ? "" : email.trim());
            if (sameEmail && account.get("matKhau").getAsString().equals(matKhau)) {
                String role = account.get("vaiTro").getAsString().toLowerCase(Locale.US);
                DataResponse<LoginDataDto> fixture = fixture("login-" + role + ".json",
                    new TypeToken<DataResponse<LoginDataDto>>() {}.getType());
                succeed(callback, fixture.data);
                return;
            }
        }
        failWith(callback, 401, "error-401-sai_thong_tin_dang_nhap.json");
    }

    @Override
    public void listBooks(String tuKhoa, int page, int pageSize, Callback<PageResponse<BookDto>> callback) {
        if (!authorized(callback)) return;
        PageResponse<BookDto> all = fixture(loanCreated ? "books-after-loan.json" : "books-before-loan.json",
            new TypeToken<PageResponse<BookDto>>() {}.getType());
        List<BookDto> filtered = new ArrayList<>();
        for (BookDto book : all.data) {
            if (matches(tuKhoa, book.nhanDe, book.tacGia)) filtered.add(book);
        }
        succeed(callback, paginate(filtered, page, pageSize));
    }

    @Override
    public void getBook(String sachId, Callback<BookDetailDto> callback) {
        if (!authorized(callback)) return;
        BookDetailDto detail = currentDetail();
        if (!detail.id.equals(sachId)) {
            failWith(callback, 404, null);
            return;
        }
        succeed(callback, detail);
    }

    @Override
    public void listReaders(String tuKhoa, int page, int pageSize, Callback<PageResponse<ReaderDto>> callback) {
        if (!authorized(callback)) return;
        List<ReaderDto> filtered = new ArrayList<>();
        for (ReaderDto reader : currentReaders()) {
            if (matches(tuKhoa, reader.maThe, reader.hoTen, reader.lopKhoa)) filtered.add(reader);
        }
        succeed(callback, paginate(filtered, page, pageSize));
    }

    @Override
    public void createLoan(String banDocId, String banSachId, Callback<LoanDto> callback) {
        if (!authorized(callback)) return;
        if (banDocId == null || banSachId == null) {
            failWith(callback, 400, "error-400-du_lieu_khong_hop_le.json");
            return;
        }
        ReaderDto reader = null;
        for (ReaderDto r : currentReaders()) if (r.id.equals(banDocId)) reader = r;
        BookCopyDto copy = null;
        for (BookCopyDto c : currentDetail().banSach) if (c.id.equals(banSachId)) copy = c;
        if (reader == null || copy == null) {
            failWith(callback, 404, null);
            return;
        }
        String today = Formatters.dateVn(new Date());
        if ("KHOA".equals(reader.trangThai)) {
            failWith(callback, 409, "error-409-ban_doc_bi_khoa.json");
        } else if (reader.hanThe.compareTo(today) < 0) {
            failWith(callback, 409, "error-409-the_het_han.json");
        } else if (reader.tienPhatConNo != null && reader.tienPhatConNo > 0) {
            failWith(callback, 409, "error-409-con_no_phat.json");
        } else if (reader.soDangMuon >= reader.gioiHanMuon) {
            failWith(callback, 409, "error-409-vuot_gioi_han_muon.json");
        } else if (!copy.isAvailable()) {
            failWith(callback, 409, "error-409-ban_sach_khong_san_sang.json");
        } else {
            DataResponse<LoanDto> response = fixture("create-loan-response.json",
                new TypeToken<DataResponse<LoanDto>>() {}.getType());
            loanCreated = true;
            succeed(callback, response.data);
        }
    }

    private BookDetailDto currentDetail() {
        DataResponse<BookDetailDto> response = fixture("book-detail.json",
            new TypeToken<DataResponse<BookDetailDto>>() {}.getType());
        BookDetailDto detail = response.data;
        if (loanCreated) {
            DataResponse<LoanDto> loan = fixture("create-loan-response.json",
                new TypeToken<DataResponse<LoanDto>>() {}.getType());
            for (BookCopyDto copy : detail.banSach) {
                if (copy.id.equals(loan.data.banSachId) && copy.isAvailable()) {
                    copy.trangThai = "DANG_MUON";
                    detail.soBanRanh--;
                }
            }
        }
        return detail;
    }

    private List<ReaderDto> currentReaders() {
        PageResponse<ReaderDto> response = fixture("readers.json",
            new TypeToken<PageResponse<ReaderDto>>() {}.getType());
        if (loanCreated) {
            DataResponse<LoanDto> loan = fixture("create-loan-response.json",
                new TypeToken<DataResponse<LoanDto>>() {}.getType());
            for (ReaderDto reader : response.data) {
                if (reader.id.equals(loan.data.banDocId)) reader.soDangMuon++;
            }
        }
        return response.data;
    }

    private <T> boolean authorized(Callback<T> callback) {
        String token = session.getToken();
        if (token == null || !token.startsWith("MOCK_TOKEN_")) {
            failWith(callback, 401, "error-401-token_khong_hop_le.json");
            return false;
        }
        if (!SessionManager.isStaffRole(session.getVaiTro())) {
            failWith(callback, 403, "error-403-khong_co_quyen.json");
            return false;
        }
        return true;
    }

    private static boolean matches(String keyword, String... fields) {
        if (keyword == null || keyword.trim().isEmpty()) return true;
        String needle = keyword.trim().toLowerCase(Locale.ROOT);
        for (String field : fields) {
            if (field != null && field.toLowerCase(Locale.ROOT).contains(needle)) return true;
        }
        return false;
    }

    private static <T> PageResponse<T> paginate(List<T> items, int page, int pageSize) {
        PageResponse<T> result = new PageResponse<>();
        int from = Math.min(items.size(), (page - 1) * pageSize);
        int to = Math.min(items.size(), from + pageSize);
        result.data = new ArrayList<>(items.subList(from, to));
        result.meta = new PageResponse.Meta();
        result.meta.page = page;
        result.meta.pageSize = pageSize;
        result.meta.total = items.size();
        return result;
    }

    private <T> void succeed(Callback<T> callback, T value) {
        main.postDelayed(() -> callback.onSuccess(value), DELAY_MS);
    }

    private <T> void failWith(Callback<T> callback, int status, String fixtureName) {
        AppError error;
        if (fixtureName == null) {
            error = new AppError(status, "KHONG_TIM_THAY", "Không tìm thấy tài nguyên.");
        } else {
            ErrorResponseDto dto = fixture(fixtureName, ErrorResponseDto.class);
            error = new AppError(status, dto.error.code, dto.error.message);
        }
        main.postDelayed(() -> callback.onError(error), DELAY_MS);
    }

    private <T> T fixture(String name, Type type) {
        return gson.fromJson(readJson("fixtures/" + name), type);
    }

    private JsonElement readJson(String assetPath) {
        try (Reader reader = new InputStreamReader(context.getAssets().open(assetPath), StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, JsonElement.class);
        } catch (IOException e) {
            throw new IllegalStateException("Thiếu fixture " + assetPath, e);
        }
    }
}
