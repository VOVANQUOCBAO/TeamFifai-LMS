package com.teamfifai.libraryadmin.data.api;

import com.teamfifai.libraryadmin.data.dto.BookDetailDto;
import com.teamfifai.libraryadmin.data.dto.BookDto;
import com.teamfifai.libraryadmin.data.dto.CreateLoanRequestDto;
import com.teamfifai.libraryadmin.data.dto.DataResponse;
import com.teamfifai.libraryadmin.data.dto.LoanDto;
import com.teamfifai.libraryadmin.data.dto.LoginDataDto;
import com.teamfifai.libraryadmin.data.dto.LoginRequestDto;
import com.teamfifai.libraryadmin.data.dto.PageResponse;
import com.teamfifai.libraryadmin.data.dto.ReaderDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/** Đường dẫn tương đối với API_BASE_URL (kết thúc bằng /api/v1/). Theo contracts/openapi.yaml v1.0.0. */
public interface LibraryApi {
    @POST("auth/dang-nhap")
    Call<DataResponse<LoginDataDto>> login(@Body LoginRequestDto body);

    @GET("sach")
    Call<PageResponse<BookDto>> listBooks(@Query("tuKhoa") String tuKhoa,
                                          @Query("page") int page,
                                          @Query("pageSize") int pageSize);

    @GET("sach/{id}")
    Call<DataResponse<BookDetailDto>> getBook(@Path("id") String sachId);

    @GET("ban-doc")
    Call<PageResponse<ReaderDto>> listReaders(@Query("tuKhoa") String tuKhoa,
                                              @Query("page") int page,
                                              @Query("pageSize") int pageSize);

    @POST("phieu-muon")
    Call<DataResponse<LoanDto>> createLoan(@Body CreateLoanRequestDto body);
}
