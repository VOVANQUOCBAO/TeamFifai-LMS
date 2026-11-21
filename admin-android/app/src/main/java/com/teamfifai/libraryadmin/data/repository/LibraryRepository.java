package com.teamfifai.libraryadmin.data.repository;

import com.teamfifai.libraryadmin.data.dto.BookDetailDto;
import com.teamfifai.libraryadmin.data.dto.BookDto;
import com.teamfifai.libraryadmin.data.dto.LoanDto;
import com.teamfifai.libraryadmin.data.dto.LoginDataDto;
import com.teamfifai.libraryadmin.data.dto.PageResponse;
import com.teamfifai.libraryadmin.data.dto.ReaderDto;

/** Activity chỉ biết interface này; mock và live là hai cài đặt thay thế được. */
public interface LibraryRepository {
    /** Kết quả bất đồng bộ; luôn được gọi trên main thread. */
    interface Callback<T> {
        void onSuccess(T value);

        void onError(AppError error);
    }

    void login(String email, String matKhau, Callback<LoginDataDto> callback);

    void listBooks(String tuKhoa, int page, int pageSize, Callback<PageResponse<BookDto>> callback);

    void getBook(String sachId, Callback<BookDetailDto> callback);

    void listReaders(String tuKhoa, int page, int pageSize, Callback<PageResponse<ReaderDto>> callback);

    void createLoan(String banDocId, String banSachId, Callback<LoanDto> callback);
}
