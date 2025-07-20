package com.example.foodordersystem.ui.cart;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodordersystem.R;
import com.example.foodordersystem.data.dao.CartDao;
import com.example.foodordersystem.data.dao.MenuDao;
import com.example.foodordersystem.data.database.DatabaseClient;
import com.example.foodordersystem.data.entity.CartItem;
import com.example.foodordersystem.data.entity.MenuItem;

import java.util.List;
import java.util.concurrent.Executors;

public class CartActivity extends AppCompatActivity implements CartItemAdapter.CartUpdateListener {

    private RecyclerView recyclerView;
    private TextView txtTotalPrice;
    private Button btnCheckout;
    private Button btnBack;
    private int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerCart);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnBack = findViewById(R.id.btnBack);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        loadCartItems();

        btnCheckout.setOnClickListener(v ->
                startActivity(new android.content.Intent(this, com.example.foodordersystem.ui.order.CheckoutActivity.class)));
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadCartItems() {
        Executors.newSingleThreadExecutor().execute(() -> {
            CartDao cartDao = DatabaseClient.getInstance(this).getAppDatabase().cartDao();
            MenuDao menuDao = DatabaseClient.getInstance(this).getAppDatabase().menuDao();
            List<CartItem> items = cartDao.getCartItemsByUser(userId);

            double total = 0;
            for (CartItem item : items) {
                MenuItem menuItem = menuDao.getMenuItemById(item.itemId);
                if (menuItem != null) {
                    total += menuItem.price * item.quantity;
                }
            }
            double finalTotal = total;
            runOnUiThread(() -> {
                recyclerView.setAdapter(new CartItemAdapter(items, this, this));
                txtTotalPrice.setText(String.format("Tổng: %.0f đ", finalTotal));
            });
        });
    }

    @Override
    public void onCartUpdated() {
        loadCartItems();
    }
}
