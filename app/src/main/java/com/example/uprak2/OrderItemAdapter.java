package com.example.uprak2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {
    private List<OrderItem> itemList;

    public OrderItemAdapter(List<OrderItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem item = itemList.get(position);
        holder.tvName.setText(item.product_name);
        holder.tvQty.setText("x" + item.quantity);
        holder.tvPrice.setText("Rp " + String.format("%,d", item.price).replace(",", "."));
    }

    @Override
    public int getItemCount() { return itemList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQty, tvPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvQty = itemView.findViewById(R.id.tvItemQty);
            tvPrice = itemView.findViewById(R.id.tvItemPrice);
        }
    }
}