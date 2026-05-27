package com.example.uprak2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class AdminProductListActivity extends AppCompatActivity {

    private RecyclerView rvProducts;
    private ProductAdapterForAdmin adapter;
    private List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Kelola Produk");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(AdminProductListActivity.this, AdminHomeActivity.class));
            finish();
        });

        rvProducts = findViewById(R.id.rvAdminProducts);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapterForAdmin(productList, this::onEditClick, this::onDeleteClick);
        rvProducts.setAdapter(adapter);

        fetchProducts();
    }

    private void fetchProducts() {
        RetrofitClient.getApiService().getProducts().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    if (response.body().getData() != null) productList.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                } else Toast.makeText(AdminProductListActivity.this, "Gagal memuat produk", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(AdminProductListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onEditClick(Product product) {
        Intent intent = new Intent(this, EditProductActivity.class);
        intent.putExtra("product_id", product.getId()); // kirim int
        startActivity(intent);
    }

    private void onDeleteClick(Product product) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Hapus Produk")
                .setMessage("Yakin ingin menghapus " + product.getName() + "?")
                .setPositiveButton("Ya", (dialog, which) -> deleteProduct(product.getId()))
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void deleteProduct(int id) {
        // Buat RequestBody dengan id
        RequestBody idPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(id));
        Call<UploadResponse> call = RetrofitClient.getApiService().deleteProduct(idPart);
        call.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(AdminProductListActivity.this, "Produk berhasil dihapus", Toast.LENGTH_SHORT).show();
                    fetchProducts(); // refresh daftar
                } else {
                    String msg = response.body() != null ? response.body().getMessage() : "Gagal hapus";
                    Toast.makeText(AdminProductListActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                Toast.makeText(AdminProductListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}