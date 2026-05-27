package com.example.uprak2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrderActivity extends AppCompatActivity {

    private RecyclerView rvOrders;
    private OrderAdapter orderAdapter;
    private List<Order> orderList = new ArrayList<>();
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Pesanan Saya");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        rvOrders = findViewById(R.id.rvOrders);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrderAdapter(orderList, this);
        rvOrders.setAdapter(orderAdapter);

        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        fetchOrders();
    }

    private void fetchOrders() {
        int userId = prefs.getInt("user_id", 0);
        if (userId == 0) {
            Toast.makeText(this, "Silakan login kembali", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        RetrofitClient.getApiService().getMyOrders(userId).enqueue(new Callback<OrdersResponse>() {
            @Override
            public void onResponse(Call<OrdersResponse> call, Response<OrdersResponse> response) {
                if (response.isSuccessful() && response.body() != null && "success".equals(response.body().status)) {
                    orderList.clear();
                    if (response.body().data != null) orderList.addAll(response.body().data);
                    orderAdapter.notifyDataSetChanged();
                    if (orderList.isEmpty()) {
                        Toast.makeText(MyOrderActivity.this, "Belum ada pesanan", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MyOrderActivity.this, "Gagal memuat pesanan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<OrdersResponse> call, Throwable t) {
                Toast.makeText(MyOrderActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }
}