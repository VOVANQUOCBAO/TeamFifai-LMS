package com.teamfifai.libraryadmin.data.repository.live;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.teamfifai.libraryadmin.data.api.LibraryApi;
import com.teamfifai.libraryadmin.data.dto.BookDetailDto;
import com.teamfifai.libraryadmin.data.dto.BookDto;
import com.teamfifai.libraryadmin.data.dto.CreateLoanRequestDto;
import com.teamfifai.libraryadmin.data.dto.DataResponse;
import com.teamfifai.libraryadmin.data.dto.ErrorResponseDto;
import com.teamfifai.libraryadmin.data.dto.LoanDto;
import com.teamfifai.libraryadmin.data.dto.LoginDataDto;
import com.teamfifai.libraryadmin.data.dto.LoginRequestDto;
import com.teamfifai.libraryadmin.data.dto.PageResponse;
import com.teamfifai.libraryadmin.data.dto.ReaderDto;
import com.teamfifai.libraryadmin.data.repository.AppError;
import com.teamfifai.libraryadmin.data.repository.LibraryRepository;
import com.teamfifai.libraryadmin.session.SessionManager;
import com.teamfifai.libraryadmin.util.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** Gọi backend thật. Lỗi được trả nguyên lý do; không bao giờ rơi về dữ liệu giả. */
public class LiveLibraryRepository implements LibraryRepository {
    private final LibraryApi api;
    private final Gson gson = new Gson();

    public LiveLibraryRepository(Context context, String baseUrl) {
        SessionManager session = new SessionManager(context);
        OkHttpClient http = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            // Render free có thể ngủ; lần gọi đầu có thể mất vài chục giây.
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(chain -> {
                Request request = chain.request();
                String token = session.getToken();
                if (token != null && request.header("Authorization") == null) {
                    request = request.newBuilder().header("Authorization", "Bearer " + token).build();
                }
                return chain.proceed(request);
            })
            .build();
        api = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(http)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(LibraryApi.class);
    }

    @Override
    public void login(String email, String matKhau, Callback<LoginDataDto> callback) {
        api.login(new LoginRequestDto(email, matKhau)).enqueue(data(callback));
    }

    @Override
    public void listBooks(String tuKhoa, int page, int pageSize, Callback<PageResponse<BookDto>> callback) {
        api.listBooks(blankToNull(tuKhoa), page, pageSize).enqueue(page(callback));
    }

    @Override
    public void getBook(String sachId, Callback<BookDetailDto> callback) {
        api.getBook(sachId).enqueue(data(callback));
    }

    @Override
    public void listReaders(String tuKhoa, int page, int pageSize, Callback<PageResponse<ReaderDto>> callback) {
        api.listReaders(blankToNull(tuKhoa), page, pageSize).enqueue(page(callback));
    }

    @Override
    public void createLoan(String banDocId, String banSachId, Callback<LoanDto> callback) {
        api.createLoan(new CreateLoanRequestDto(banDocId, banSachId)).enqueue(data(callback));
    }

    private static String blankToNull(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }

    private <T> retrofit2.Callback<DataResponse<T>> data(Callback<T> callback) {
        return new retrofit2.Callback<DataResponse<T>>() {
            @Override
            public void onResponse(Call<DataResponse<T>> call, Response<DataResponse<T>> response) {
                if (response.isSuccessful()) {
                    DataResponse<T> body = response.body();
                    if (body == null || body.data == null) callback.onError(AppError.badResponse());
                    else callback.onSuccess(body.data);
                } else {
                    callback.onError(toError(response));
                }
            }

            @Override
            public void onFailure(Call<DataResponse<T>> call, Throwable t) {
                callback.onError(failure(t));
            }
        };
    }

    private <T> retrofit2.Callback<PageResponse<T>> page(Callback<PageResponse<T>> callback) {
        return new retrofit2.Callback<PageResponse<T>>() {
            @Override
            public void onResponse(Call<PageResponse<T>> call, Response<PageResponse<T>> response) {
                if (response.isSuccessful()) {
                    PageResponse<T> body = response.body();
                    if (body == null || body.data == null || body.meta == null) {
                        callback.onError(AppError.badResponse());
                    } else {
                        callback.onSuccess(body);
                    }
                } else {
                    callback.onError(toError(response));
                }
            }

            @Override
            public void onFailure(Call<PageResponse<T>> call, Throwable t) {
                callback.onError(failure(t));
            }
        };
    }

    private AppError toError(Response<?> response) {
        try (ResponseBody errorBody = response.errorBody()) {
            if (errorBody != null) {
                ErrorResponseDto dto = gson.fromJson(errorBody.charStream(), ErrorResponseDto.class);
                if (dto != null && dto.error != null && dto.error.code != null) {
                    return new AppError(response.code(), dto.error.code, dto.error.message);
                }
            }
        } catch (RuntimeException e) {
            Log.w(Constants.TAG, "Không đọc được body lỗi HTTP " + response.code(), e);
        }
        return new AppError(response.code(), response.code() == 501 ? "CHUC_NANG_CHUA_TICH_HOP" : null, null);
    }

    private static AppError failure(Throwable t) {
        Log.w(Constants.TAG, "Gọi API thất bại", t);
        return t instanceof IOException ? AppError.network() : AppError.badResponse();
    }
}
