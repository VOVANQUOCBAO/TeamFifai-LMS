
package com.teamfifai.libraryadmin.ui.readers;

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

import com.teamfifai.libraryadmin.R;
import com.teamfifai.libraryadmin.data.dto.ReaderDto;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class ReadersActivity extends AppCompatActivity {

    private final List<ReaderDto> allReaders = new ArrayList<>();
    private final List<ReaderDto> displayedReaders = new ArrayList<>();

    private ReaderAdapter adapter;
    private EditText edtSearch;
    private TextView tvReaderCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readers);

        edtSearch = findViewById(R.id.edtSearchReader);
        tvReaderCount = findViewById(R.id.tvReaderCount);

        RecyclerView rvReaders = findViewById(R.id.rvReaders);
        rvReaders.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ReaderAdapter(displayedReaders,
                new ReaderAdapter.ReaderActionListener() {
                    @Override
                    public void onEdit(ReaderDto reader) {
                        showReaderDialog(reader);
                    }

                    @Override
                    public void onDelete(ReaderDto reader) {
                        allReaders.remove(reader);
                        filterReaders(edtSearch.getText().toString());

                        Toast.makeText(ReadersActivity.this,
                                "Đã xóa bạn đọc khỏi danh sách mẫu",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        rvReaders.setAdapter(adapter);

        loadSampleReaders();

        findViewById(R.id.btnAddReader).setOnClickListener(v ->
                showReaderDialog(null));

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                filterReaders(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void loadSampleReaders() {
        addSampleReader("R001", "Nguyễn Văn An",
                "CNTT", "20/11/2027");

        addSampleReader("R002", "Trần Thị Bình",
                "Quản trị kinh doanh", "15/12/2027");

        addSampleReader("R003", "Lê Minh Cường",
                "Ngôn ngữ Anh", "10/01/2028");

        filterReaders("");
    }

    private void addSampleReader(String code, String name,
                                 String className, String expiry) {
        ReaderDto reader = new ReaderDto();
        reader.id = code;
        reader.maThe = code;
        reader.hoTen = name;
        reader.lopKhoa = className;
        reader.hanThe = expiry;
        reader.trangThai = "Hoạt động";
        reader.gioiHanMuon = 5;
        reader.soDangMuon = 0;
        reader.tienPhatConNo = 0;

        allReaders.add(reader);
    }

    private void filterReaders(String keyword) {
        displayedReaders.clear();

        String search = keyword.trim().toLowerCase();

        for (ReaderDto reader : allReaders) {
            String name = reader.hoTen == null
                    ? "" : reader.hoTen.toLowerCase();

            String code = reader.maThe == null
                    ? "" : reader.maThe.toLowerCase();

            if (name.contains(search) || code.contains(search)) {
                displayedReaders.add(reader);
            }
        }

        adapter.notifyDataSetChanged();
        tvReaderCount.setText(
                "Danh sách bạn đọc (" + displayedReaders.size() + ")");
    }

    private void showReaderDialog(ReaderDto existingReader) {
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_reader, null);

        TextInputEditText edtName =
                dialogView.findViewById(R.id.edtReaderName);

        TextInputEditText edtCode =
                dialogView.findViewById(R.id.edtReaderCode);

        TextInputEditText edtClass =
                dialogView.findViewById(R.id.edtReaderClass);

        TextInputEditText edtExpiry =
                dialogView.findViewById(R.id.edtReaderExpiry);

        boolean isEditing = existingReader != null;

        if (isEditing) {
            edtName.setText(existingReader.hoTen);
            edtCode.setText(existingReader.maThe);
            edtClass.setText(existingReader.lopKhoa);
            edtExpiry.setText(existingReader.hanThe);
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(isEditing
                        ? "Chỉnh sửa bạn đọc" : "Thêm bạn đọc")
                .setView(dialogView)
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Lưu", null)
                .create();

        dialog.setOnShowListener(d ->
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(v -> {

                            String name = getValue(edtName);
                            String code = getValue(edtCode);
                            String className = getValue(edtClass);
                            String expiry = getValue(edtExpiry);

                            if (name.isEmpty() || code.isEmpty()
                                    || className.isEmpty()
                                    || expiry.isEmpty()) {
                                Toast.makeText(this,
                                        "Vui lòng nhập đầy đủ thông tin",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }

                            for (ReaderDto reader : allReaders) {
                                if (reader.maThe != null
                                        && reader.maThe.equalsIgnoreCase(code)
                                        && reader != existingReader) {
                                    edtCode.setError("Mã thẻ đã tồn tại");
                                    return;
                                }
                            }

                            ReaderDto reader;

                            if (isEditing) {
                                reader = existingReader;
                            } else {
                                reader = new ReaderDto();
                                reader.id = "LOCAL_" + System.currentTimeMillis();
                                reader.trangThai = "Hoạt động";
                                reader.gioiHanMuon = 5;
                                reader.soDangMuon = 0;
                                reader.tienPhatConNo = 0;
                                allReaders.add(reader);
                            }

                            reader.hoTen = name;
                            reader.maThe = code;
                            reader.lopKhoa = className;
                            reader.hanThe = expiry;

                            filterReaders(edtSearch.getText().toString());

                            Toast.makeText(this,
                                    isEditing
                                            ? "Đã cập nhật bạn đọc mẫu"
                                            : "Đã thêm bạn đọc mẫu",
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
