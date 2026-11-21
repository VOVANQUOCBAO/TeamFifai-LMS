
package com.teamfifai.libraryadmin.ui.catalog.books;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamfifai.libraryadmin.R;
import com.teamfifai.libraryadmin.data.dto.BookDto;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    public interface BookActionListener {
        void onEdit(BookDto book);
        void onDelete(BookDto book);
    }

    private final List<BookDto> books;
    private final BookActionListener listener;

    public BookAdapter(List<BookDto> books, BookActionListener listener) {
        this.books = books;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        BookDto book = books.get(position);

        holder.tvTitle.setText(book.nhanDe);
        holder.tvAuthor.setText("Tác giả: " + book.tacGia);
        holder.tvInfo.setText(
                "ISBN: " + book.isbn + " | Bản còn rảnh: " + book.soBanRanh
        );

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(book));

        holder.btnDelete.setOnClickListener(v ->
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc muốn xóa sách này?")
                        .setNegativeButton("Hủy", null)
                        .setPositiveButton("Xóa", (dialog, which) ->
                                listener.onDelete(book))
                        .show()
        );
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthor, tvInfo;
        Button btnEdit, btnDelete;

        BookViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvBookTitle);
            tvAuthor = itemView.findViewById(R.id.tvBookAuthor);
            tvInfo = itemView.findViewById(R.id.tvBookInfo);
            btnEdit = itemView.findViewById(R.id.btnEditBook);
            btnDelete = itemView.findViewById(R.id.btnDeleteBook);
        }
    }
}
