package com.example.uprak2;

import android.os.Bundle;
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

public class PromoActivity extends AppCompatActivity {

    private RecyclerView rvFlashSale;
    private ProductAdapter flashSaleAdapter;
    private List<Product> flashSaleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Flash Sale");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        rvFlashSale = findViewById(R.id.rvFlashSale);
        rvFlashSale.setLayoutManager(new GridLayoutManager(this, 2));
        flashSaleAdapter = new ProductAdapter(this, flashSaleList);
        rvFlashSale.setAdapter(flashSaleAdapter);

        fetchFlashSaleProducts();
    }

    private void fetchFlashSaleProducts() {
        RetrofitClient.getApiService().getFlashSaleProducts().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && "success".equals(response.body().getStatus())) {
                    flashSaleList.clear();
                    if (response.body().getData() != null) flashSaleList.addAll(response.body().getData());
                    flashSaleAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(PromoActivity.this, "Gagal memuat flash sale", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(PromoActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}