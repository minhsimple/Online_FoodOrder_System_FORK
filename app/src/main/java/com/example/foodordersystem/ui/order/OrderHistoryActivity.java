package com.example.foodordersystem.ui.order;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodordersystem.R;
import com.example.foodordersystem.data.dao.OrderDao;
import com.example.foodordersystem.data.database.DatabaseClient;
import com.example.foodordersystem.data.entity.Order;

import java.util.List;
import java.util.concurrent.Executors;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private int userId;
    private Button btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerView = findViewById(R.id.recyclerOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnBack = findViewById(R.id.btnBack);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        loadOrders();

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadOrders() {
        Executors.newSingleThreadExecutor().execute(() -> {
            OrderDao orderDao = DatabaseClient.getInstance(this).getAppDatabase().orderDao();
            List<Order> orders = orderDao.getOrdersByUser(userId);
            runOnUiThread(() -> recyclerView.setAdapter(new OrderHistoryAdapter(orders)));
        });
    }
}
