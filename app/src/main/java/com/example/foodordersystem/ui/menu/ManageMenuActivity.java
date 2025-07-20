package com.example.foodordersystem.ui.menu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodordersystem.R;
import com.example.foodordersystem.data.database.DatabaseClient;
import com.example.foodordersystem.data.entity.MenuItem;

import java.util.List;
import java.util.concurrent.Executors;

public class ManageMenuActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MenuItemAdapter adapter;
    private Button btnAddItem;
    private Button btnBack;
    private MenuFormDialog currentDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_menu);

        recyclerView = findViewById(R.id.recyclerMenu);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnBack = findViewById(R.id.btnBack);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadMenuItems();

        btnAddItem.setOnClickListener(v -> {
            MenuFormDialog dialog = new MenuFormDialog(this, this::loadMenuItems, null);
            currentDialog = dialog;
            dialog.show();
        });
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadMenuItems() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<MenuItem> items = DatabaseClient.getInstance(this)
                    .getAppDatabase()
                    .menuDao()
                    .getAllMenuItems();

            runOnUiThread(() -> {
                adapter = new MenuItemAdapter(items, this, this::loadMenuItems);
                recyclerView.setAdapter(adapter);
            });
        });
    }

    // 🟡 Ghi đè onActivityResult để nhận ảnh từ Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MenuFormDialog.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            if (currentDialog != null) {
                currentDialog.setImageUri(imageUri);
            }
        }
    }
}
