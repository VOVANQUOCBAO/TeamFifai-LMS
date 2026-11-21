package com.teamfifai.libraryadmin.ui.catalog.books;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.teamfifai.libraryadmin.R;
import com.teamfifai.libraryadmin.config.AppConfig;
import com.teamfifai.libraryadmin.ui.common.StaffActivity;
import com.teamfifai.libraryadmin.ui.dashboard.DashboardActivity;
import com.teamfifai.libraryadmin.ui.loans.CreateLoanActivity;

/** Màn hình 2: danh sách sách (RecyclerView), tìm kiếm, số bản rảnh; chọn nhan đề để cho mượn. */
public class BooksActivity extends StaffActivity {
    private static final int MENU_DEMO = 1;
    private static final int MENU_LOGOUT = 2;

    private BooksViewModel viewModel;
    private BookAdapter adapter;
    private TextInputEditText edtSearch;
    private SwipeRefreshLayout swipe;
    private ProgressBar progress;
    private View stateBox;
    private TextView txtState;
    private TextView txtSummary;
    private Button btnRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!requireStaffSession()) return;
        setContentView(R.layout.activity_books);
        bindDemoBanner();
        setTitle(R.string.books_title);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(session.getHoTen() + " · " + session.getVaiTro());
        }

        edtSearch = findViewById(R.id.edtSearch);
        swipe = findViewById(R.id.swipe);
        progress = findViewById(R.id.progress);
        stateBox = findViewById(R.id.stateBox);
        txtState = findViewById(R.id.txtState);
        txtSummary = findViewById(R.id.txtSummary);
        btnRetry = findViewById(R.id.btnRetry);

        RecyclerView recycler = findViewById(R.id.recycler);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        recycler.setLayoutManager(layout);
        adapter = new BookAdapter(book -> {
            Intent intent = new Intent(this, CreateLoanActivity.class);
            intent.putExtra(CreateLoanActivity.EXTRA_SACH_ID, book.id);
            intent.putExtra(CreateLoanActivity.EXTRA_NHAN_DE, book.nhanDe);
            startActivity(intent);
        });
        recycler.setAdapter(adapter);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                if (dy > 0 && layout.findLastVisibleItemPosition() >= adapter.getItemCount() - 3) {
                    viewModel.loadMore();
                }
            }
        });

        findViewById(R.id.btnSearch).setOnClickListener(v -> search());
        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search();
                return true;
            }
            return false;
        });
        btnRetry.setOnClickListener(v -> viewModel.refresh());
        swipe.setOnRefreshListener(() -> viewModel.refresh());

        viewModel = new ViewModelProvider(this).get(BooksViewModel.class);
        viewModel.state().observe(this, this::render);
        viewModel.loadIfNeeded();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Quay lại từ màn cho mượn: tải lại để số bản rảnh phản ánh phiếu vừa tạo.
        viewModel.refresh();
    }

    private void search() {
        viewModel.search(edtSearch.getText() == null ? "" : edtSearch.getText().toString());
    }

    private void render(BooksViewModel.State s) {
        if (s.error != null && handleAuthError(s.error)) return;
        adapter.submit(s.items);
        boolean firstLoad = s.loading && s.items.isEmpty();
        progress.setVisibility(firstLoad && !swipe.isRefreshing() ? View.VISIBLE : View.GONE);
        if (!s.loading) swipe.setRefreshing(false);

        if (s.error != null) {
            stateBox.setVisibility(View.VISIBLE);
            txtState.setText(s.error.userMessage());
            btnRetry.setVisibility(View.VISIBLE);
        } else if (s.loaded && !s.loading && s.items.isEmpty()) {
            stateBox.setVisibility(View.VISIBLE);
            txtState.setText(R.string.empty_books);
            btnRetry.setVisibility(View.GONE);
        } else {
            stateBox.setVisibility(View.GONE);
        }
        txtSummary.setText(s.loaded && s.error == null
            ? "Hiển thị " + s.items.size() + "/" + s.total + " nhan đề · chạm vào sách để cho mượn"
            : "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (AppConfig.isMock()) menu.add(Menu.NONE, MENU_DEMO, 0, R.string.demo_modules);
        menu.add(Menu.NONE, MENU_LOGOUT, 1, R.string.logout);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == MENU_LOGOUT) {
            logout();
            return true;
        }
        if (item.getItemId() == MENU_DEMO) {
            startActivity(new Intent(this, DashboardActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
