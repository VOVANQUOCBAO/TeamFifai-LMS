package com.teamfifai.libraryadmin.ui.loans;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.teamfifai.libraryadmin.R;
import com.teamfifai.libraryadmin.data.dto.BookCopyDto;
import com.teamfifai.libraryadmin.data.dto.LoanDto;
import com.teamfifai.libraryadmin.data.dto.ReaderDto;
import com.teamfifai.libraryadmin.data.repository.AppError;
import com.teamfifai.libraryadmin.ui.common.StaffActivity;
import com.teamfifai.libraryadmin.util.Formatters;

import java.util.ArrayList;
import java.util.List;

/**
 * Màn hình 3: chọn một banSachId từ chi tiết sách, chọn banDocId từ GET /ban-doc,
 * gửi POST /phieu-muon. Hạn trả do backend tính; app không tự tính.
 */
public class CreateLoanActivity extends StaffActivity {
    public static final String EXTRA_SACH_ID = "sachId";
    public static final String EXTRA_NHAN_DE = "nhanDe";

    private CreateLoanViewModel viewModel;
    private TextView txtBookTitle;
    private TextView txtBookAuthor;
    private ProgressBar progress;
    private View loadErrorBox;
    private TextView txtLoadError;
    private View formBox;
    private Spinner spCopy;
    private Spinner spReader;
    private TextView txtCopyError;
    private TextView txtReaderError;
    private TextView txtReaderInfo;
    private TextInputEditText edtReaderSearch;
    private Button btnSubmit;
    private ProgressBar submitProgress;
    private TextView txtResult;

    private final List<Option> copyOptions = new ArrayList<>();
    private final List<Option> readerOptions = new ArrayList<>();
    private List<ReaderDto> readers = new ArrayList<>();
    private int renderedVersion = -1;
    private boolean bindingSpinners;

    /** Một dòng trong Spinner; id null là dòng nhắc chọn. */
    private static class Option {
        final String id;
        final String label;
        final boolean enabled;

        Option(String id, String label, boolean enabled) {
            this.id = id;
            this.label = label;
            this.enabled = enabled;
        }

        @NonNull
        @Override
        public String toString() {
            return label;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!requireStaffSession()) return;
        setContentView(R.layout.activity_create_loan);
        bindDemoBanner();
        setTitle(R.string.create_loan_title);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String sachId = getIntent().getStringExtra(EXTRA_SACH_ID);
        if (sachId == null) {
            finish();
            return;
        }

        txtBookTitle = findViewById(R.id.txtBookTitle);
        txtBookAuthor = findViewById(R.id.txtBookAuthor);
        progress = findViewById(R.id.progress);
        loadErrorBox = findViewById(R.id.loadErrorBox);
        txtLoadError = findViewById(R.id.txtLoadError);
        formBox = findViewById(R.id.formBox);
        spCopy = findViewById(R.id.spCopy);
        spReader = findViewById(R.id.spReader);
        txtCopyError = findViewById(R.id.txtCopyError);
        txtReaderError = findViewById(R.id.txtReaderError);
        txtReaderInfo = findViewById(R.id.txtReaderInfo);
        edtReaderSearch = findViewById(R.id.edtReaderSearch);
        btnSubmit = findViewById(R.id.btnSubmit);
        submitProgress = findViewById(R.id.submitProgress);
        txtResult = findViewById(R.id.txtResult);
        txtBookTitle.setText(getIntent().getStringExtra(EXTRA_NHAN_DE));

        spCopy.setAdapter(new OptionAdapter(this, copyOptions));
        spReader.setAdapter(new OptionAdapter(this, readerOptions));
        spCopy.setOnItemSelectedListener(new SimpleSelection(position -> {
            Option o = copyOptions.get(position);
            viewModel.selectCopy(o.enabled ? o.id : null);
            if (o.id != null) txtCopyError.setVisibility(View.GONE);
        }));
        spReader.setOnItemSelectedListener(new SimpleSelection(position -> {
            Option o = readerOptions.get(position);
            viewModel.selectReader(o.id);
            if (o.id != null) txtReaderError.setVisibility(View.GONE);
            showReaderInfo(o.id);
        }));

        findViewById(R.id.btnRetry).setOnClickListener(v -> viewModel.reload());
        findViewById(R.id.btnReaderSearch).setOnClickListener(v -> searchReaders());
        edtReaderSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchReaders();
                return true;
            }
            return false;
        });
        btnSubmit.setOnClickListener(v -> submit());

        viewModel = new ViewModelProvider(this).get(CreateLoanViewModel.class);
        viewModel.state().observe(this, this::render);
        viewModel.start(sachId);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void searchReaders() {
        viewModel.searchReaders(edtReaderSearch.getText() == null ? "" : edtReaderSearch.getText().toString());
    }

    private void submit() {
        CreateLoanViewModel.State s = viewModel.state().getValue();
        boolean valid = true;
        if (s.selectedCopyId == null) {
            txtCopyError.setText("Chọn một bản sách đang rảnh.");
            txtCopyError.setVisibility(View.VISIBLE);
            valid = false;
        }
        if (s.selectedReaderId == null) {
            txtReaderError.setText("Chọn bạn đọc nhận sách.");
            txtReaderError.setVisibility(View.VISIBLE);
            valid = false;
        }
        if (valid) viewModel.submit();
    }

    private void render(CreateLoanViewModel.State s) {
        AppError authError = s.loadError != null ? s.loadError : s.submitError;
        if (authError != null && handleAuthError(authError)) return;

        boolean loading = s.loadingBook || s.loadingReaders;
        progress.setVisibility(loading ? View.VISIBLE : View.GONE);
        loadErrorBox.setVisibility(s.loadError != null ? View.VISIBLE : View.GONE);
        if (s.loadError != null) txtLoadError.setText(s.loadError.userMessage());
        formBox.setVisibility(s.book != null ? View.VISIBLE : View.GONE);

        if (s.book != null) {
            txtBookTitle.setText(s.book.nhanDe);
            txtBookAuthor.setText(s.book.tacGia + " · còn " + s.book.soBanRanh + " bản rảnh");
        }
        if (s.dataVersion != renderedVersion) {
            renderedVersion = s.dataVersion;
            bindSpinners(s);
        }

        boolean busy = s.submitting || loading;
        btnSubmit.setEnabled(!busy && s.book != null);
        spCopy.setEnabled(!s.submitting);
        spReader.setEnabled(!s.submitting);
        submitProgress.setVisibility(s.submitting ? View.VISIBLE : View.GONE);
        renderResult(s);
    }

    private void bindSpinners(CreateLoanViewModel.State s) {
        bindingSpinners = true;
        copyOptions.clear();
        if (s.book != null && s.book.banSach != null) {
            int available = 0;
            for (BookCopyDto c : s.book.banSach) if (c.isAvailable()) available++;
            copyOptions.add(new Option(null, available > 0
                ? "— Chọn bản sách (" + available + " bản rảnh) —" : "— Không còn bản rảnh —", false));
            for (BookCopyDto c : s.book.banSach) {
                copyOptions.add(new Option(c.id,
                    c.maCaBiet + " · kệ " + c.viTriKe + " · " + c.trangThai
                        + ("TOT".equals(c.tinhTrang) ? "" : " · " + c.tinhTrang),
                    c.isAvailable()));
            }
        }
        // Bản đã chọn không còn rảnh sau khi tải lại thì bỏ chọn.
        int copyIndex = indexOf(copyOptions, s.selectedCopyId);
        if (copyIndex < 0 || !copyOptions.get(copyIndex).enabled) {
            s.selectedCopyId = null;
            copyIndex = 0;
        }

        readers = s.readers;
        readerOptions.clear();
        readerOptions.add(new Option(null, readers.isEmpty()
            ? "— Không tìm thấy bạn đọc —" : "— Chọn bạn đọc (" + readers.size() + ") —", false));
        for (ReaderDto r : readers) {
            readerOptions.add(new Option(r.id, r.maThe + " · " + r.hoTen, true));
        }
        int readerIndex = indexOf(readerOptions, s.selectedReaderId);
        if (readerIndex < 0) {
            s.selectedReaderId = null;
            readerIndex = 0;
        }

        ((OptionAdapter) spCopy.getAdapter()).notifyDataSetChanged();
        ((OptionAdapter) spReader.getAdapter()).notifyDataSetChanged();
        if (!copyOptions.isEmpty()) spCopy.setSelection(copyIndex, false);
        spReader.setSelection(readerIndex, false);
        showReaderInfo(s.selectedReaderId);
        bindingSpinners = false;
    }

    private void showReaderInfo(String readerId) {
        ReaderDto reader = null;
        for (ReaderDto r : readers) if (r.id.equals(readerId)) reader = r;
        if (reader == null) {
            txtReaderInfo.setText("");
            return;
        }
        String trangThai = "KHOA".equals(reader.trangThai) ? "Bị khóa" : "Bình thường";
        txtReaderInfo.setText(reader.lopKhoa + " · thẻ hết hạn " + reader.hanThe + " · " + trangThai
            + "\nĐang mượn " + reader.soDangMuon + "/" + reader.gioiHanMuon
            + " · nợ phạt " + Formatters.vnd(reader.tienPhatConNo == null ? 0 : reader.tienPhatConNo));
    }

    private void renderResult(CreateLoanViewModel.State s) {
        if (s.createdLoan != null) {
            LoanDto loan = s.createdLoan;
            txtResult.setVisibility(View.VISIBLE);
            txtResult.setBackgroundColor(ContextCompat.getColor(this, R.color.ok_bg));
            txtResult.setTextColor(ContextCompat.getColor(this, R.color.ok_text));
            txtResult.setText("Đã tạo phiếu mượn\n"
                + loan.nhanDe + " · bản " + loan.maCaBiet + "\n"
                + (s.createdForReader != null ? "Bạn đọc: " + s.createdForReader + "\n" : "")
                + "Mượn lúc: " + Formatters.vnDateTime(loan.muonLuc) + "\n"
                + "Hạn trả: " + Formatters.vnDateTime(loan.hanTra) + " (giờ Việt Nam)\n"
                + "Mã phiếu: " + loan.id);
        } else if (s.submitError != null) {
            txtResult.setVisibility(View.VISIBLE);
            txtResult.setBackgroundColor(ContextCompat.getColor(this, R.color.error_bg));
            txtResult.setTextColor(ContextCompat.getColor(this, R.color.error_text));
            txtResult.setText(s.submitError.userMessage());
        } else {
            txtResult.setVisibility(View.GONE);
        }
    }

    private static int indexOf(List<Option> options, String id) {
        if (id == null) return -1;
        for (int i = 0; i < options.size(); i++) if (id.equals(options.get(i).id)) return i;
        return -1;
    }

    private interface PositionListener {
        void onSelected(int position);
    }

    private class SimpleSelection implements AdapterView.OnItemSelectedListener {
        private final PositionListener listener;

        SimpleSelection(PositionListener listener) {
            this.listener = listener;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (!bindingSpinners && position >= 0 && position < parent.getCount()) {
                listener.onSelected(position);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // giữ nguyên lựa chọn
        }
    }

    /** Spinner làm mờ và không cho chọn bản sách không rảnh. */
    private static class OptionAdapter extends ArrayAdapter<Option> {
        OptionAdapter(Context context, List<Option> options) {
            super(context, android.R.layout.simple_spinner_item, options);
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }

        @Override
        public boolean isEnabled(int position) {
            Option o = getItem(position);
            return o != null && (o.enabled || o.id == null);
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            Option o = getItem(position);
            view.setAlpha(o != null && o.id != null && !o.enabled ? 0.4f : 1f);
            return view;
        }
    }
}
