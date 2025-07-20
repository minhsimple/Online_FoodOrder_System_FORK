package com.example.foodordersystem.ui.menu;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodordersystem.R;
import com.example.foodordersystem.data.database.DatabaseClient;
import com.example.foodordersystem.data.entity.CartItem;
import com.example.foodordersystem.data.entity.MenuItem;

import java.util.List;
import java.util.concurrent.Executors;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MenuViewHolder> {

    private final List<MenuItem> items;
    private final Context context;
    private final int userId;

    public MenuListAdapter(List<MenuItem> items, Context context, int userId) {
        this.items = items;
        this.context = context;
        this.userId = userId;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu_list, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem item = items.get(position);
        holder.txtName.setText(item.itemName);
        holder.txtPrice.setText(String.format("%.0f đ", item.price));

        holder.btnAdd.setOnClickListener(v -> {
            CartItem cartItem = new CartItem();
            cartItem.userId = userId;
            cartItem.itemId = item.itemId;
            cartItem.quantity = 1;

            Executors.newSingleThreadExecutor().execute(() -> {
                DatabaseClient.getInstance(context)
                        .getAppDatabase()
                        .cartDao()
                        .insertCartItem(cartItem);
                ((Activity) context).runOnUiThread(() ->
                        Toast.makeText(context, "Đã thêm vào giỏ", Toast.LENGTH_SHORT).show());
            });
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice;
        Button btnAdd;

        MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            btnAdd = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
