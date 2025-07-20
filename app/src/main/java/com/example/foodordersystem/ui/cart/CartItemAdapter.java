package com.example.foodordersystem.ui.cart;

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
import com.example.foodordersystem.data.dao.CartDao;
import com.example.foodordersystem.data.dao.MenuDao;
import com.example.foodordersystem.data.entity.CartItem;
import com.example.foodordersystem.data.entity.MenuItem;

import java.util.List;
import java.util.concurrent.Executors;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartViewHolder> {

    public interface CartUpdateListener {
        void onCartUpdated();
    }

    private final List<CartItem> cartItems;
    private final Context context;
    private final CartUpdateListener listener;

    public CartItemAdapter(List<CartItem> cartItems, Context context, CartUpdateListener listener) {
        this.cartItems = cartItems;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        MenuDao menuDao = DatabaseClient.getInstance(context).getAppDatabase().menuDao();
        MenuItem menuItem = menuDao.getMenuItemById(cartItem.itemId);
        if (menuItem != null) {
            holder.txtName.setText(menuItem.itemName);
            holder.txtPrice.setText(String.format("%.0f đ", menuItem.price));
        } else {
            holder.txtName.setText("Item " + cartItem.itemId);
            holder.txtPrice.setText("0 đ");
        }
        holder.txtQuantity.setText(String.valueOf(cartItem.quantity));

        holder.btnIncrease.setOnClickListener(v -> updateQuantity(cartItem, cartItem.quantity + 1));
        holder.btnDecrease.setOnClickListener(v -> {
            if (cartItem.quantity > 1) {
                updateQuantity(cartItem, cartItem.quantity - 1);
            }
        });
        holder.btnRemove.setOnClickListener(v -> removeItem(cartItem));
    }

    private void updateQuantity(CartItem item, int newQuantity) {
        item.quantity = newQuantity;
        Executors.newSingleThreadExecutor().execute(() -> {
            CartDao cartDao = DatabaseClient.getInstance(context).getAppDatabase().cartDao();
            cartDao.updateCartItem(item);
            ((Activity) context).runOnUiThread(() -> {
                notifyDataSetChanged();
                listener.onCartUpdated();
            });
        });
    }

    private void removeItem(CartItem item) {
        Executors.newSingleThreadExecutor().execute(() -> {
            CartDao cartDao = DatabaseClient.getInstance(context).getAppDatabase().cartDao();
            cartDao.deleteCartItem(item);
            ((Activity) context).runOnUiThread(() -> {
                cartItems.remove(item);
                notifyDataSetChanged();
                listener.onCartUpdated();
                Toast.makeText(context, "Đã xóa khỏi giỏ", Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtQuantity;
        Button btnIncrease, btnDecrease, btnRemove;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
