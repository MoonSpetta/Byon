package com.example.uprak2;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvCategoryTitle;
    private RecyclerView rvProducts;
    private ProductAdapter adapter;
    private List<Product> productList = new ArrayList<>();
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        btnBack = findViewById(R.id.btnBack);
        tvCategoryTitle = findViewById(R.id.tvCategoryTitle);
        rvProducts = findViewById(R.id.rvCategoryProducts);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(this, productList);
        rvProducts.setAdapter(adapter);

        category = getIntent().getStringExtra("category");
        if (category == null) category = "Fashion";
        tvCategoryTitle.setText(category);

        btnBack.setOnClickListener(v -> finish());

        fetchProductsByCategory(category);
    }

    private void fetchProductsByCategory(String category) {
        // Gunakan API search_products.php dengan parameter kategori, keyword kosong, min 0, max besar
        ApiService apiService = RetrofitClient.getApiService();
        Call<ApiResponse> call = apiService.searchProducts("", category, 0, Integer.MAX_VALUE);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    if ("success".equals(apiResponse.getStatus())) {
                        List<Product> products = apiResponse.getData();
                        productList.clear();
                        if (products != null && !products.isEmpty()) {
                            productList.addAll(products);
                        } else {
                            // Tampilkan pesan kosong
                            Toast.makeText(CategoryActivity.this, "Tidak ada produk", Toast.LENGTH_SHORT).show();
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(CategoryActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CategoryActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(CategoryActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}