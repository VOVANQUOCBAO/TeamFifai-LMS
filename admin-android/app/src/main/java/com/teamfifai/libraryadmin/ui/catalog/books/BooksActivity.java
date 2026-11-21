
package com.teamfifai.libraryadmin.ui.catalog.books;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.teamfifai.libraryadmin.R;
import com.teamfifai.libraryadmin.data.dto.BookDto;

import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends AppCompatActivity {

    private final List<BookDto> allBooks = new ArrayList<>();
    private final List<BookDto> displayedBooks = new ArrayList<>();

    private BookAdapter adapter;
    private EditText edtSearch;
    private TextView tvBookCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        edtSearch = findViewById(R.id.edtSearchBook);
        tvBookCount = findViewById(R.id.tvBookCount);

        RecyclerView rvBooks = findViewById(R.id.rvBooks);
        rvBooks.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BookAdapter(displayedBooks, new BookAdapter.BookActionListener() {
            @Override
            public void onEdit(BookDto book) {
                showBookDialog(book);
            }

            @Override
            public void onDelete(BookDto book) {
                allBooks.remove(book);
                filterBooks(edtSearch.getText().toString());
                Toast.makeText(BooksActivity.this,
                        "Đã xóa sách khỏi danh sách mẫu",
                        Toast.LENGTH_SHORT).show();
            }
        });

        rvBooks.setAdapter(adapter);

        loadSampleBooks();

        findViewById(R.id.btnAddBook).setOnClickListener(v -> showBookDialog(null));

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBooks(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadSampleBooks() {
        addSampleBook("B001", "Lập trình Java cơ bản",
                "Nguyễn Văn A", "978604000001", 5);

        addSampleBook("B002", "Cấu trúc dữ liệu và giải thuật",
                "Trần Văn B", "978604000002", 3);

        addSampleBook("B003", "Phát triển ứng dụng Android",
                "Lê Văn C", "978604000003", 8);

        filterBooks("");
    }

    private void addSampleBook(String id, String title,
                               String author, String isbn, int available) {
        BookDto book = new BookDto();
        book.id = id;
        book.nhanDe = title;
        book.tacGia = author;
        book.isbn = isbn;
        book.soBanRanh = available;
        allBooks.add(book);
    }

    private void filterBooks(String keyword) {
        displayedBooks.clear();

        String search = keyword.trim().toLowerCase();

        for (BookDto book : allBooks) {
            String title = book.nhanDe == null ? "" : book.nhanDe.toLowerCase();
            String author = book.tacGia == null ? "" : book.tacGia.toLowerCase();

            if (title.contains(search) || author.contains(search)) {
                displayedBooks.add(book);
            }
        }

        adapter.notifyDataSetChanged();
        tvBookCount.setText("Danh sách sách (" + displayedBooks.size() + ")");
    }

    private void showBookDialog(BookDto existingBook) {
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_book, null);

        TextInputEditText edtTitle = dialogView.findViewById(R.id.edtBookTitle);
        TextInputEditText edtAuthor = dialogView.findViewById(R.id.edtBookAuthor);
        TextInputEditText edtIsbn = dialogView.findViewById(R.id.edtBookIsbn);
        TextInputEditText edtAvailable = dialogView.findViewById(R.id.edtBookAvailable);

        boolean isEditing = existingBook != null;

        if (isEditing) {
            edtTitle.setText(existingBook.nhanDe);
            edtAuthor.setText(existingBook.tacGia);
            edtIsbn.setText(existingBook.isbn);
            edtAvailable.setText(String.valueOf(existingBook.soBanRanh));
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(isEditing ? "Chỉnh sửa sách" : "Thêm sách mới")
                .setView(dialogView)
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Lưu", null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> {
                    String title = getValue(edtTitle);
                    String author = getValue(edtAuthor);
                    String isbn = getValue(edtIsbn);
                    String availableText = getValue(edtAvailable);

                    if (title.isEmpty() || author.isEmpty()
                            || isbn.isEmpty() || availableText.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int available;
                    try {
                        available = Integer.parseInt(availableText);
                        if (available < 0) {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException e) {
                        edtAvailable.setError("Số bản phải là số nguyên không âm");
                        return;
                    }

                    BookDto book;

                    if (isEditing) {
                        book = existingBook;
                    } else {
                        book = new BookDto();
                        book.id = "LOCAL_" + System.currentTimeMillis();
                        allBooks.add(book);
                    }

                    book.nhanDe = title;
                    book.tacGia = author;
                    book.isbn = isbn;
                    book.soBanRanh = available;

                    filterBooks(edtSearch.getText().toString());

                    Toast.makeText(this,
                            isEditing ? "Đã cập nhật sách mẫu" : "Đã thêm sách mẫu",
                            Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                }));

        dialog.show();
    }

    private String getValue(TextInputEditText editText) {
        return editText.getText() == null
                ? "" : editText.getText().toString().trim();
    }
}
