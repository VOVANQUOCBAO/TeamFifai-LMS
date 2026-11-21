package com.teamfifai.libraryadmin.ui.loans;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.teamfifai.libraryadmin.data.dto.BookDetailDto;
import com.teamfifai.libraryadmin.data.dto.LoanDto;
import com.teamfifai.libraryadmin.data.dto.PageResponse;
import com.teamfifai.libraryadmin.data.dto.ReaderDto;
import com.teamfifai.libraryadmin.data.repository.AppError;
import com.teamfifai.libraryadmin.data.repository.LibraryRepository;
import com.teamfifai.libraryadmin.data.repository.RepositoryProvider;

import java.util.ArrayList;
import java.util.List;

/** Form cho mượn: giữ lựa chọn, kết quả và trạng thái gửi qua xoay màn hình; chặn gửi lặp. */
public class CreateLoanViewModel extends AndroidViewModel {
    private static final int READER_PAGE_SIZE = 100;

    public static class State {
        public BookDetailDto book;
        public List<ReaderDto> readers = new ArrayList<>();
        public boolean loadingBook;
        public boolean loadingReaders;
        public AppError loadError;
        public String selectedCopyId;
        public String selectedReaderId;
        public boolean submitting;
        public LoanDto createdLoan;
        public String createdForReader;
        public AppError submitError;
        /** Tăng mỗi khi danh sách đổi để Activity biết cần dựng lại Spinner. */
        public int dataVersion;
    }

    private final LibraryRepository repository;
    private final MutableLiveData<State> state = new MutableLiveData<>(new State());
    private String sachId;
    private String readerKeyword = "";

    public CreateLoanViewModel(@NonNull Application application) {
        super(application);
        repository = RepositoryProvider.get(application);
    }

    public LiveData<State> state() {
        return state;
    }

    public void start(String sachId) {
        if (this.sachId != null) return; // đã tải (ví dụ sau khi xoay màn hình)
        this.sachId = sachId;
        reload();
    }

    public void reload() {
        loadBook();
        loadReaders();
    }

    public void searchReaders(String keyword) {
        readerKeyword = keyword == null ? "" : keyword.trim();
        loadReaders();
    }

    public void selectCopy(String id) {
        state.getValue().selectedCopyId = id;
    }

    public void selectReader(String id) {
        state.getValue().selectedReaderId = id;
    }

    private void loadBook() {
        State s = state.getValue();
        s.loadingBook = true;
        s.loadError = null;
        state.setValue(s);
        repository.getBook(sachId, new LibraryRepository.Callback<BookDetailDto>() {
            @Override
            public void onSuccess(BookDetailDto value) {
                s.loadingBook = false;
                s.book = value;
                s.dataVersion++;
                state.setValue(s);
            }

            @Override
            public void onError(AppError error) {
                s.loadingBook = false;
                s.loadError = error;
                state.setValue(s);
            }
        });
    }

    private void loadReaders() {
        State s = state.getValue();
        s.loadingReaders = true;
        s.loadError = null;
        state.setValue(s);
        repository.listReaders(readerKeyword, 1, READER_PAGE_SIZE,
            new LibraryRepository.Callback<PageResponse<ReaderDto>>() {
                @Override
                public void onSuccess(PageResponse<ReaderDto> value) {
                    s.loadingReaders = false;
                    s.readers = value.data;
                    s.dataVersion++;
                    state.setValue(s);
                }

                @Override
                public void onError(AppError error) {
                    s.loadingReaders = false;
                    s.loadError = error;
                    state.setValue(s);
                }
            });
    }

    /** Activity đã kiểm tra form trước khi gọi. Chặn bấm lặp khi đang gửi. */
    public void submit() {
        State s = state.getValue();
        if (s.submitting || s.selectedCopyId == null || s.selectedReaderId == null) return;
        String readerLabel = null;
        for (ReaderDto r : s.readers) {
            if (r.id.equals(s.selectedReaderId)) readerLabel = r.maThe + " · " + r.hoTen;
        }
        final String forReader = readerLabel;
        s.submitting = true;
        s.submitError = null;
        s.createdLoan = null;
        state.setValue(s);
        repository.createLoan(s.selectedReaderId, s.selectedCopyId, new LibraryRepository.Callback<LoanDto>() {
            @Override
            public void onSuccess(LoanDto value) {
                s.submitting = false;
                s.createdLoan = value;
                s.createdForReader = forReader;
                s.selectedCopyId = null;
                state.setValue(s);
                reload(); // bản sách vừa mượn đổi trạng thái, số đang mượn của bạn đọc tăng
            }

            @Override
            public void onError(AppError error) {
                s.submitting = false;
                s.submitError = error;
                state.setValue(s);
            }
        });
    }
}
