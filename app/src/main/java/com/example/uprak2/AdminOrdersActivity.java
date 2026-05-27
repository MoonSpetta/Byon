package com.example.uprak2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrdersActivity extends AppCompatActivity {

    private RecyclerView rvOrders;
    private OrderAdapter orderAdapter;
    private List<Order> orderList = new ArrayList<>();
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Kelola Pesanan");
        toolbar.setNavigationOnClickListener(v -> finish());

        rvOrders = findViewById(R.id.rvOrders);
        tvEmpty = findViewById(R.id.tvEmptyOrders);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        fetchOrders();
    }

    private void fetchOrders() {
        RetrofitClient.getApiService().getAllOrders().enqueue(new Callback<OrdersResponse>() {
            @Override
            public void onResponse(Call<OrdersResponse> call, Response<OrdersResponse> response) {
                if (response.isSuccessful() && response.body() != null && "success".equals(response.body().status)) {
                    orderList.clear();
                    if (response.body().data != null && !response.body().data.isEmpty()) {
                        orderList.addAll(response.body().data);
                        orderAdapter = new OrderAdapter(orderList, AdminOrdersActivity.this);
                        rvOrders.setAdapter(orderAdapter);
                        rvOrders.setVisibility(android.view.View.VISIBLE);
                        tvEmpty.setVisibility(android.view.View.GONE);
                    } else {
                        rvOrders.setVisibility(android.view.View.GONE);
                        tvEmpty.setVisibility(android.view.View.VISIBLE);
                    }
                }
            }
            @Override
            public void onFailure(Call<OrdersResponse> call, Throwable t) {
                tvEmpty.setVisibility(android.view.View.VISIBLE);
            }
        });
    }
}