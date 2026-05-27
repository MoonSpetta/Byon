package com.example.uprak2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> orderList;
    private Context context;

    public OrderAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvOrderNumber.setText("Order #" + order.order_number);
        holder.tvOrderDate.setText("Tanggal: " + order.created_at);
        holder.tvOrderTotal.setText("Total: Rp " + String.format("%,d", order.total_price).replace(",", "."));
        holder.tvOrderStatus.setText("Status: " + order.status);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("order_id", order.id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return orderList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderNumber, tvOrderDate, tvOrderTotal, tvOrderStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderNumber = itemView.findViewById(R.id.tvOrderNumber);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
        }
    }
}