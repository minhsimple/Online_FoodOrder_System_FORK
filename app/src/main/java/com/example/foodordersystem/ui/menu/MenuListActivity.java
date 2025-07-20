package com.example.foodordersystem.ui.menu;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodordersystem.R;
import com.example.foodordersystem.data.database.DatabaseClient;
import com.example.foodordersystem.data.entity.MenuItem;

import java.util.List;
import java.util.concurrent.Executors;

public class MenuListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MenuListAdapter adapter;
    private int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        recyclerView = findViewById(R.id.recyclerMenuList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadMenuItems();
    }

    private void loadMenuItems() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<MenuItem> items = DatabaseClient.getInstance(this)
                    .getAppDatabase()
                    .menuDao()
                    .getAllMenuItems();

            runOnUiThread(() -> {
                adapter = new MenuListAdapter(items, this, userId);
                recyclerView.setAdapter(adapter);
            });
        });
    }
}
