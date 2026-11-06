package com.teamfifai.libraryadmin.data.api;

import com.teamfifai.libraryadmin.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** Khung Retrofit. Chua co interface API: cho hop dong duoc chot, khong tu doan. */
public final class ApiClient {
    private static Retrofit retrofit;

    public static Retrofit get() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    private ApiClient() {}
}
