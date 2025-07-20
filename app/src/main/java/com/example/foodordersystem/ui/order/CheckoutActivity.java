package com.example.foodordersystem.ui.order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodordersystem.R;
import com.example.foodordersystem.data.dao.CartDao;
import com.example.foodordersystem.data.dao.MenuDao;
import com.example.foodordersystem.data.dao.OrderDao;
import com.example.foodordersystem.data.dao.OrderInfoDao;
import com.example.foodordersystem.data.database.DatabaseClient;
import com.example.foodordersystem.data.entity.CartItem;
import com.example.foodordersystem.data.entity.MenuItem;
import com.example.foodordersystem.data.entity.Order;
import com.example.foodordersystem.data.entity.OrderInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class CheckoutActivity extends AppCompatActivity {

    private TextView txtMessage;
    private Button btnHistory;
    private int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        txtMessage = findViewById(R.id.txtCheckoutMessage);
        btnHistory = findViewById(R.id.btnViewHistory);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        processCheckout();

        btnHistory.setOnClickListener(v ->
                startActivity(new Intent(this, OrderHistoryActivity.class)));
    }

    private void processCheckout() {
        Executors.newSingleThreadExecutor().execute(() -> {
            DatabaseClient client = DatabaseClient.getInstance(this);
            CartDao cartDao = client.getAppDatabase().cartDao();
            MenuDao menuDao = client.getAppDatabase().menuDao();
            OrderDao orderDao = client.getAppDatabase().orderDao();
            OrderInfoDao orderInfoDao = client.getAppDatabase().orderInfoDao();

            List<CartItem> cartItems = cartDao.getCartItemsByUser(userId);
            if (cartItems.isEmpty()) {
                runOnUiThread(() -> txtMessage.setText("Giỏ hàng trống"));
                return;
            }

            double total = 0;
            for (CartItem item : cartItems) {
                MenuItem menuItem = menuDao.getMenuItemById(item.itemId);
                if (menuItem != null) {
                    total += menuItem.price * item.quantity;
                }
            }

            Order order = new Order();
            order.userId = userId;
            order.orderDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            order.totalPrice = total;
            order.status = "Pending";
            orderDao.insertOrder(order);

            List<Order> orders = orderDao.getOrdersByUser(userId);
            int orderId = orders.get(orders.size() - 1).orderId;

            for (CartItem item : cartItems) {
                MenuItem menuItem = menuDao.getMenuItemById(item.itemId);
                if (menuItem != null) {
                    OrderInfo info = new OrderInfo();
                    info.orderId = orderId;
                    info.itemId = item.itemId;
                    info.quantity = item.quantity;
                    info.priceAtOrder = menuItem.price;
                    orderInfoDao.insert(info);
                }
            }

            cartDao.clearCart(userId);

            runOnUiThread(() -> txtMessage.setText("Đặt hàng thành công"));
        });
    }
}
