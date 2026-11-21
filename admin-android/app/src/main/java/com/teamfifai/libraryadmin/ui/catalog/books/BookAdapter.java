package com.teamfifai.libraryadmin.ui.catalog.books;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.teamfifai.libraryadmin.R;
import com.teamfifai.libraryadmin.data.dto.BookDto;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    public interface OnBookClick {
        void onClick(BookDto book);
    }

    private final List<BookDto> items = new ArrayList<>();
    private final OnBookClick listener;

    public BookAdapter(OnBookClick listener) {
        this.listener = listener;
    }

    public void submit(List<BookDto> books) {
        items.clear();
        items.addAll(books);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView author;
        private final TextView available;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtTitle);
            author = itemView.findViewById(R.id.txtAuthor);
            available = itemView.findViewById(R.id.txtAvailable);
        }

        void bind(BookDto book, OnBookClick listener) {
            title.setText(book.nhanDe);
            author.setText(book.tacGia);
            boolean has = book.soBanRanh > 0;
            available.setText(has ? "Rảnh: " + book.soBanRanh : "Hết bản rảnh");
            available.setTextColor(ContextCompat.getColor(itemView.getContext(),
                has ? R.color.ok_text : R.color.error_text));
            available.setBackgroundColor(ContextCompat.getColor(itemView.getContext(),
                has ? R.color.ok_bg : R.color.error_bg));
            itemView.setOnClickListener(v -> listener.onClick(book));
        }
    }
}
