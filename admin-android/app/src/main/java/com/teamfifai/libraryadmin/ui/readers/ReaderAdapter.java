
package com.teamfifai.libraryadmin.ui.readers;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamfifai.libraryadmin.R;
import com.teamfifai.libraryadmin.data.dto.ReaderDto;

import java.util.List;

public class ReaderAdapter extends RecyclerView.Adapter<ReaderAdapter.ReaderViewHolder> {

    public interface ReaderActionListener {
        void onEdit(ReaderDto reader);
        void onDelete(ReaderDto reader);
    }

    private final List<ReaderDto> readers;
    private final ReaderActionListener listener;

    public ReaderAdapter(List<ReaderDto> readers, ReaderActionListener listener) {
        this.readers = readers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reader, parent, false);
        return new ReaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReaderViewHolder holder, int position) {
        ReaderDto reader = readers.get(position);

        holder.tvName.setText(reader.hoTen);
        holder.tvCode.setText("Mã thẻ: " + reader.maThe);
        holder.tvInfo.setText(
                "Lớp/Khoa: " + reader.lopKhoa
                        + " | Trạng thái: " + reader.trangThai
        );

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(reader));

        holder.btnDelete.setOnClickListener(v ->
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc muốn xóa bạn đọc này?")
                        .setNegativeButton("Hủy", null)
                        .setPositiveButton("Xóa", (dialog, which) ->
                                listener.onDelete(reader))
                        .show()
        );
    }

    @Override
    public int getItemCount() {
        return readers.size();
    }

    static class ReaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCode, tvInfo;
        Button btnEdit, btnDelete;

        ReaderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvReaderName);
            tvCode = itemView.findViewById(R.id.tvReaderCode);
            tvInfo = itemView.findViewById(R.id.tvReaderInfo);
            btnEdit = itemView.findViewById(R.id.btnEditReader);
            btnDelete = itemView.findViewById(R.id.btnDeleteReader);
        }
    }
}
