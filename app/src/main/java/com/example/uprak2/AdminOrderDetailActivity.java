package com.example.uprak2;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

public class AdminOrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderNumber, tvDate, tvCustomer, tvAddress, tvPhone, tvPayment, tvStatus, tvTotal;
    private RecyclerView rvItems;
    private Spinner spinnerStatus;
    private Button btnUpdate;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        tvOrderNumber = findViewById(R.id.tvOrderNumber);
        tvDate = findViewById(R.id.tvOrderDate);
        tvCustomer = findViewById(R.id.tvCustomer);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);
        tvPayment = findViewById(R.id.tvPayment);
        tvStatus = findViewById(R.id.tvOrderStatus);
        tvTotal = findViewById(R.id.tvTotal);
        rvItems = findViewById(R.id.rvOrderItems);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnUpdate = findViewById(R.id.btnUpdateStatus);

        orderId = getIntent().getIntExtra("order_id", 0);
        if (orderId == 0) finish();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.order_status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        fetchDetail();
        btnUpdate.setOnClickListener(v -> updateStatus());
    }

    private void fetchDetail() {
        RetrofitClient.getApiService().getOrderDetail(orderId).enqueue(new Callback<OrderDetailResponse>() {
            @Override
            public void onResponse(Call<OrderDetailResponse> call, Response<OrderDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null && "success".equals(response.body().status)) {
                    Order order = response.body().order;
                    List<OrderItem> items = response.body().items;
                    tvOrderNumber.setText("Order #" + order.order_number);
                    tvDate.setText(order.created_at);
                    tvCustomer.setText("Nama: " + order.customer_name);
                    tvAddress.setText("Alamat: " + order.address);
                    tvPhone.setText("Telepon: " + order.phone);
                    String payment = order.payment_method;
                    if (order.bank_name != null && !order.bank_name.isEmpty())
                        payment += " (" + order.bank_name + " - " + order.account_number + ")";
                    tvPayment.setText("Pembayaran: " + payment);
                    tvStatus.setText("Status saat ini: " + order.status);
                    tvTotal.setText("Total: Rp " + String.format("%,d", order.total_price).replace(",", "."));
                    // set spinner ke status saat ini
                    String[] statusArray = getResources().getStringArray(R.array.order_status_array);
                    for (int i = 0; i < statusArray.length; i++) {
                        if (statusArray[i].equalsIgnoreCase(order.status)) {
                            spinnerStatus.setSelection(i);
                            break;
                        }
                    }
                    rvItems.setLayoutManager(new LinearLayoutManager(AdminOrderDetailActivity.this));
                    rvItems.setAdapter(new OrderItemAdapter(items));
                }
            }
            @Override
            public void onFailure(Call<OrderDetailResponse> call, Throwable t) { }
        });
    }

    private void updateStatus() {
        String newStatus = spinnerStatus.getSelectedItem().toString();
        UpdateStatusRequest req = new UpdateStatusRequest();
        req.order_id = orderId;
        req.status = newStatus;
        RetrofitClient.getApiService().updateOrderStatus(req).enqueue(new Callback<UpdateStatusResponse>() {
            @Override
            public void onResponse(Call<UpdateStatusResponse> call, Response<UpdateStatusResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().success) {
                    Toast.makeText(AdminOrderDetailActivity.this, "Status diperbarui", Toast.LENGTH_SHORT).show();
                    tvStatus.setText("Status saat ini: " + newStatus);
                } else {
                    Toast.makeText(AdminOrderDetailActivity.this, "Gagal update", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UpdateStatusResponse> call, Throwable t) {
                Toast.makeText(AdminOrderDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}