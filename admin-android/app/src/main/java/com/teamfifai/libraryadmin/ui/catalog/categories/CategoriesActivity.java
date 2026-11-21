package com.teamfifai.libraryadmin.ui.catalog.categories;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamfifai.libraryadmin.R;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity
        implements CategoryAdapter.CategoryListener {

    private final List<Category> categories = new ArrayList<>();
    private final List<Category> filteredCategories = new ArrayList<>();

    private CategoryAdapter adapter;
    private TextView tvCategoryCount;
    private EditText edtSearchCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        edtSearchCategory = findViewById(R.id.edtSearchCategory);
        tvCategoryCount = findViewById(R.id.tvCategoryCount);

        RecyclerView rvCategories = findViewById(R.id.rvCategories);
        Button btnAddCategory = findViewById(R.id.btnAddCategory);

        rvCategories.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CategoryAdapter(filteredCategories, this);
        rvCategories.setAdapter(adapter);

        loadSampleData();
        updateCategoryCount();

        btnAddCategory.setOnClickListener(v -> showCategoryDialog(null));

        edtSearchCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCategories(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void loadSampleData() {
        categories.add(new Category("Công nghệ thông tin", "Sách lập trình và máy tính"));
        categories.add(new Category("Kinh tế", "Sách kinh doanh và tài chính"));
        categories.add(new Category("Văn học", "Tiểu thuyết và truyện"));
        categories.add(new Category("Khoa học", "Sách khoa học và nghiên cứu"));
        categories.add(new Category("Ngoại ngữ", "Sách học ngoại ngữ"));

        filteredCategories.clear();
        filteredCategories.addAll(categories);
        adapter.notifyDataSetChanged();
    }

    private void filterCategories(String keyword) {
        filteredCategories.clear();

        String query = keyword.trim().toLowerCase();

        for (Category category : categories) {
            if (category.getName().toLowerCase().contains(query)
                    || category.getDescription().toLowerCase().contains(query)) {
                filteredCategories.add(category);
            }
        }

        adapter.notifyDataSetChanged();
        updateCategoryCount();
    }

    private void updateCategoryCount() {
        tvCategoryCount.setText("Tổng số danh mục: " + categories.size());
    }

    private void showCategoryDialog(Category category) {
        View view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_category, null);

        EditText edtName = view.findViewById(R.id.edtCategoryName);
        EditText edtDescription = view.findViewById(R.id.edtCategoryDescription);

        boolean isEditing = category != null;

        if (isEditing) {
            edtName.setText(category.getName());
            edtDescription.setText(category.getDescription());
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(isEditing ? "Sửa danh mục" : "Thêm danh mục")
                .setView(view)
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Lưu", null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> {
                    String name = edtName.getText().toString().trim();
                    String description = edtDescription.getText().toString().trim();

                    if (name.isEmpty()) {
                        edtName.setError("Vui lòng nhập tên danh mục");
                        return;
                    }

                    if (isEditing) {
                        category.setName(name);
                        category.setDescription(description);
                    } else {
                        categories.add(new Category(name, description));
                    }

                    filterCategories(edtSearchCategory.getText().toString());
                    updateCategoryCount();
                    dialog.dismiss();
                }));

        dialog.show();
    }

    @Override
    public void onEdit(Category category) {
        showCategoryDialog(category);
    }

    @Override
    public void onDelete(Category category) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa danh mục")
                .setMessage("Bạn có chắc muốn xóa danh mục " + category.getName() + "?")
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Xóa", (dialog, which) -> {
                    categories.remove(category);
                    filterCategories(edtSearchCategory.getText().toString());
                    updateCategoryCount();
                })
                .show();
    }
}
