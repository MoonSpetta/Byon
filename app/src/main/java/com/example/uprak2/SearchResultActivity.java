package com.example.uprak2;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultActivity extends AppCompatActivity {

    private TextView tvQueryInfo;
    private RecyclerView rvResults;
    private ProductAdapter adapter;
    private List<Product> filteredProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        tvQueryInfo = findViewById(R.id.tvQueryInfo);
        rvResults = findViewById(R.id.rvSearchResults);
        rvResults.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductAdapter(this, filteredProducts);
        rvResults.setAdapter(adapter);

        String query = getIntent().getStringExtra("query");
        String filterKategori = getIntent().getStringExtra("filter_kategori");
        int filterHargaMin = getIntent().getIntExtra("filter_harga_min", 0);
        int filterHargaMax = getIntent().getIntExtra("filter_harga_max", Integer.MAX_VALUE);

        String info = "Menampilkan hasil untuk: ";
        if (query != null && !query.isEmpty()) info += "\"" + query + "\"";
        if (filterKategori != null && !filterKategori.equals("Semua")) info += " | Kategori: " + filterKategori;
        tvQueryInfo.setText(info);

        searchProducts(query, filterKategori, filterHargaMin, filterHargaMax);
    }

    private void searchProducts(String keyword, String category, int minPrice, int maxPrice) {
        ApiService api = RetrofitClient.getApiService();
        Call<ApiResponse> call = api.searchProducts(keyword, category, minPrice, maxPrice);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse res = response.body();
                    if ("success".equals(res.getStatus())) {
                        filteredProducts.clear();
                        if (res.getData() != null) filteredProducts.addAll(res.getData());
                        adapter.notifyDataSetChanged();
                        if (filteredProducts.isEmpty()) Toast.makeText(SearchResultActivity.this, "Tidak ada produk", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(SearchResultActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                } else Toast.makeText(SearchResultActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(SearchResultActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}