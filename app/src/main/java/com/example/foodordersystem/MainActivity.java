package com.example.foodordersystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodordersystem.ui.cart.CartActivity;
import com.example.foodordersystem.ui.menu.ManageMenuActivity;
import com.example.foodordersystem.ui.menu.MenuListActivity;
import com.example.foodordersystem.ui.order.OrderHistoryActivity;
import com.example.foodordersystem.ProfileActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnMenu = findViewById(R.id.btnBrowseMenu);
        Button btnCart = findViewById(R.id.btnCart);
        Button btnOrders = findViewById(R.id.btnOrders);
        Button btnProfile = findViewById(R.id.btnProfile);
        Button btnManage = findViewById(R.id.btnManageMenu);

        btnMenu.setOnClickListener(v ->
                startActivity(new Intent(this, MenuListActivity.class)));
        btnCart.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));
        btnOrders.setOnClickListener(v ->
                startActivity(new Intent(this, OrderHistoryActivity.class)));
        btnProfile.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));
        btnManage.setOnClickListener(v ->
                startActivity(new Intent(this, ManageMenuActivity.class)));
    }
}
