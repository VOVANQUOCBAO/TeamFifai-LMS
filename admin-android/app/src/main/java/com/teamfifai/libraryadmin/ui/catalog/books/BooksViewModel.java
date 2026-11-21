package com.teamfifai.libraryadmin.ui.catalog.books;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.teamfifai.libraryadmin.data.dto.BookDto;
import com.teamfifai.libraryadmin.data.dto.PageResponse;
import com.teamfifai.libraryadmin.data.repository.AppError;
import com.teamfifai.libraryadmin.data.repository.LibraryRepository;
import com.teamfifai.libraryadmin.data.repository.RepositoryProvider;

import java.util.ArrayList;
import java.util.List;

/** Danh sách sách có tìm kiếm và phân trang; sống qua xoay màn hình. */
public class BooksViewModel extends AndroidViewModel {
    static final int PAGE_SIZE = 20;

    public static class State {
        public final List<BookDto> items = new ArrayList<>();
        public String keyword = "";
        public int total;
        public boolean loading;
        public boolean loaded;
        public AppError error;
    }

    private final LibraryRepository repository;
    private final MutableLiveData<State> state = new MutableLiveData<>(new State());
    private int requestId;

    public BooksViewModel(@NonNull Application application) {
        super(application);
        repository = RepositoryProvider.get(application);
    }

    public LiveData<State> state() {
        return state;
    }

    public void loadIfNeeded() {
        State s = state.getValue();
        if (s != null && !s.loaded && !s.loading) search(s.keyword);
    }

    public void refresh() {
        State s = state.getValue();
        search(s == null ? "" : s.keyword);
    }

    public void search(String keyword) {
        State s = state.getValue();
        s.keyword = keyword == null ? "" : keyword.trim();
        s.items.clear();
        s.total = 0;
        fetch(s, 1);
    }

    public void loadMore() {
        State s = state.getValue();
        if (s == null || s.loading || s.error != null || s.items.size() >= s.total) return;
        fetch(s, s.items.size() / PAGE_SIZE + 1);
    }

    private void fetch(State s, int page) {
        int id = ++requestId;
        s.loading = true;
        s.error = null;
        state.setValue(s);
        repository.listBooks(s.keyword, page, PAGE_SIZE, new LibraryRepository.Callback<PageResponse<BookDto>>() {
            @Override
            public void onSuccess(PageResponse<BookDto> value) {
                if (id != requestId) return; // bỏ phản hồi của lượt tìm cũ
                if (page == 1) s.items.clear();
                s.items.addAll(value.data);
                s.total = value.meta.total;
                s.loading = false;
                s.loaded = true;
                state.setValue(s);
            }

            @Override
            public void onError(AppError error) {
                if (id != requestId) return;
                s.loading = false;
                s.loaded = true;
                s.error = error;
                state.setValue(s);
            }
        });
    }
}
