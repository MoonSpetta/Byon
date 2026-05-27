package com.example.uprak2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private List<Product> productList;
    private CartDatabaseHelper dbHelper;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        dbHelper = new CartDatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_small, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText("Rp " + String.format("%,d", product.getPrice()).replace(",", "."));

        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            Glide.with(context).load(product.getImageUrl()).placeholder(R.drawable.ic_launcher_foreground).into(holder.ivImage);
        } else {
            Glide.with(context).load(R.drawable.ic_launcher_foreground).into(holder.ivImage);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product_id", String.valueOf(product.getId()));
            context.startActivity(intent);
        });

        holder.btnAddToCart.setOnClickListener(v -> {
            dbHelper.addToCart(product);
            Toast.makeText(context, product.getName() + " ditambahkan ke keranjang", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() { return productList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvPrice;
        Button btnAddToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivProductImage);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}