package com.example.uprak2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ProductAdapterForAdmin extends RecyclerView.Adapter<ProductAdapterForAdmin.ViewHolder> {

    private List<Product> productList;
    private OnEditListener editListener;
    private OnDeleteListener deleteListener;

    public interface OnEditListener {
        void onEdit(Product product);
    }

    public interface OnDeleteListener {
        void onDelete(Product product);
    }

    public ProductAdapterForAdmin(List<Product> productList, OnEditListener editListener, OnDeleteListener deleteListener) {
        this.productList = productList;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText("Rp " + product.getPrice());
        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.ivImage);
        holder.btnEdit.setOnClickListener(v -> editListener.onEdit(product));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvPrice;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivAdminProductImage);
            tvName = itemView.findViewById(R.id.tvAdminProductName);
            tvPrice = itemView.findViewById(R.id.tvAdminProductPrice);
            btnEdit = itemView.findViewById(R.id.btnAdminEdit);
            btnDelete = itemView.findViewById(R.id.btnAdminDelete);
        }
    }
}