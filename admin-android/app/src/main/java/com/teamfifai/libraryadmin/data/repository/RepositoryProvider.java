package com.teamfifai.libraryadmin.data.repository;

import android.content.Context;

import com.teamfifai.libraryadmin.config.AppConfig;
import com.teamfifai.libraryadmin.data.repository.live.LiveLibraryRepository;
import com.teamfifai.libraryadmin.data.repository.mock.MockLibraryRepository;

public final class RepositoryProvider {
    private static LibraryRepository instance;

    private RepositoryProvider() {}

    public static synchronized LibraryRepository get(Context context) {
        if (instance == null) {
            Context app = context.getApplicationContext();
            instance = AppConfig.isMock()
                ? new MockLibraryRepository(app)
                : new LiveLibraryRepository(app, AppConfig.apiBaseUrl());
        }
        return instance;
    }
}
