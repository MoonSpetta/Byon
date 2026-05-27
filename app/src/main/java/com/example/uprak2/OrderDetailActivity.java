package com.example.uprak2;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderNumber, tvOrderDate, tvOrderStatus, tvCustomerName, tvAddress, tvPhone, tvPayment, tvTotalPrice;
    private RecyclerView rvOrderItems;
    private OrderItemAdapter itemAdapter;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Detail Pesanan");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // Inisialisasi view dengan ID yang sesuai dari layout
        tvOrderNumber = findViewById(R.id.tvOrderNumber);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);
        tvPayment = findViewById(R.id.tvPayment);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        rvOrderItems = findViewById(R.id.rvOrderItems);
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));

        orderId = getIntent().getIntExtra("order_id", 0);
        if (orderId == 0) {
            Toast.makeText(this, "ID pesanan tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchOrderDetail();
    }

    private void fetchOrderDetail() {
        RetrofitClient.getApiService().getOrderDetail(orderId).enqueue(new Callback<OrderDetailResponse>() {
            @Override
            public void onResponse(Call<OrderDetailResponse> call, Response<OrderDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null && "success".equals(response.body().status)) {
                    Order order = response.body().order;
                    List<OrderItem> items = response.body().items;

                    tvOrderNumber.setText("Order #" + order.order_number);
                    tvOrderDate.setText("Tanggal: " + order.created_at);
                    tvOrderStatus.setText("Status: " + order.status);
                    tvCustomerName.setText("Nama: " + order.customer_name);
                    tvAddress.setText("Alamat: " + order.address);
                    tvPhone.setText("Telepon: " + order.phone);
                    String paymentInfo = order.payment_method;
                    if (order.bank_name != null && !order.bank_name.isEmpty()) {
                        paymentInfo += " - " + order.bank_name;
                        if (order.account_number != null && !order.account_number.isEmpty())
                            paymentInfo += " (" + order.account_number + ")";
                    }
                    tvPayment.setText("Pembayaran: " + paymentInfo);
                    tvTotalPrice.setText("Total: Rp " + String.format("%,d", order.total_price).replace(",", "."));

                    itemAdapter = new OrderItemAdapter(items);
                    rvOrderItems.setAdapter(itemAdapter);
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Gagal memuat detail pesanan", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<OrderDetailResponse> call, Throwable t) {
                Toast.makeText(OrderDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}