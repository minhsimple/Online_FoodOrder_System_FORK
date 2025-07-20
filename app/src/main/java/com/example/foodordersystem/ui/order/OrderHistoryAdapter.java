package com.example.foodordersystem.ui.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodordersystem.R;
import com.example.foodordersystem.data.entity.Order;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private final List<Order> orders;

    public OrderHistoryAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.txtId.setText("Mã đơn: " + order.orderId);
        holder.txtDate.setText(order.orderDate);
        holder.txtTotal.setText(String.format("%.0f đ", order.totalPrice));
        holder.txtStatus.setText(order.status);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtId, txtDate, txtTotal, txtStatus;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.txtOrderId);
            txtDate = itemView.findViewById(R.id.txtOrderDate);
            txtTotal = itemView.findViewById(R.id.txtOrderTotal);
            txtStatus = itemView.findViewById(R.id.txtOrderStatus);
        }
    }
}
