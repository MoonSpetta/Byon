package com.example.uprak2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private List<CartItem> cartItems;
    private CartDatabaseHelper dbHelper;
    private OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    public CartAdapter(Context context, List<CartItem> cartItems, OnCartChangeListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.dbHelper = new CartDatabaseHelper(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText("Rp " + String.format("%,d", item.getPrice()).replace(",", "."));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(context).load(item.getImageUrl()).placeholder(R.drawable.ic_launcher_foreground).into(holder.ivImage);
        } else {
            Glide.with(context).load(R.drawable.ic_launcher_foreground).into(holder.ivImage);
        }

        holder.btnIncrease.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            dbHelper.updateQuantity(item.getProductId(), newQuantity);
            item.setQuantity(newQuantity);
            notifyItemChanged(position);
            if (listener != null) listener.onCartChanged();
        });

        holder.btnDecrease.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() - 1;
            if (newQuantity <= 0) {
                dbHelper.removeFromCart(item.getProductId());
                cartItems.remove(position);
                notifyItemRemoved(position);
            } else {
                dbHelper.updateQuantity(item.getProductId(), newQuantity);
                item.setQuantity(newQuantity);
                notifyItemChanged(position);
            }
            if (listener != null) listener.onCartChanged();
        });

        holder.btnDelete.setOnClickListener(v -> {
            dbHelper.removeFromCart(item.getProductId());
            cartItems.remove(position);
            notifyItemRemoved(position);
            if (listener != null) listener.onCartChanged();
        });

        // 🔥 Tambahkan klik gambar untuk ke detail produk
        holder.ivImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product_id", String.valueOf(item.getProductId()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return cartItems.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvPrice, tvQuantity;
        ImageButton btnIncrease, btnDecrease, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivCartImage);
            tvName = itemView.findViewById(R.id.tvCartName);
            tvPrice = itemView.findViewById(R.id.tvCartPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}