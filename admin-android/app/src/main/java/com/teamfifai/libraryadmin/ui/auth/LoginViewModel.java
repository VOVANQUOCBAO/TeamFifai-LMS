package com.teamfifai.libraryadmin.ui.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.teamfifai.libraryadmin.data.dto.LoginDataDto;
import com.teamfifai.libraryadmin.data.repository.AppError;
import com.teamfifai.libraryadmin.data.repository.LibraryRepository;
import com.teamfifai.libraryadmin.data.repository.RepositoryProvider;
import com.teamfifai.libraryadmin.session.SessionManager;

/** Giữ trạng thái đăng nhập qua xoay màn hình. */
public class LoginViewModel extends AndroidViewModel {
    public static class State {
        public boolean loading;
        public String errorMessage;
        public boolean loggedIn;
    }

    private final LibraryRepository repository;
    private final SessionManager session;
    private final MutableLiveData<State> state = new MutableLiveData<>(new State());

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = RepositoryProvider.get(application);
        session = new SessionManager(application);
    }

    public LiveData<State> state() {
        return state;
    }

    public void login(String email, String password) {
        State current = state.getValue();
        if (current == null || current.loading) return;
        State loading = new State();
        loading.loading = true;
        state.setValue(loading);
        repository.login(email, password, new LibraryRepository.Callback<LoginDataDto>() {
            @Override
            public void onSuccess(LoginDataDto value) {
                State next = new State();
                if (value.nguoiDung == null || !SessionManager.isStaffRole(value.nguoiDung.vaiTro)) {
                    // Không lưu token của BAN_DOC trong app quản trị.
                    next.errorMessage = "Tài khoản này không có quyền vào app quản trị. "
                        + "Bạn đọc vui lòng dùng app Client.";
                } else {
                    session.save(value.token, value.nguoiDung.vaiTro, value.nguoiDung.hoTen);
                    next.loggedIn = true;
                }
                state.setValue(next);
            }

            @Override
            public void onError(AppError error) {
                State next = new State();
                next.errorMessage = error.userMessage();
                state.setValue(next);
            }
        });
    }
}
