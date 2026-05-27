package com.example.uprak2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReceiptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        TextView tvOrderId = findViewById(R.id.tvOrderId);
        TextView tvDate = findViewById(R.id.tvDate);
        TextView tvCustomer = findViewById(R.id.tvCustomer);
        TextView tvAddress = findViewById(R.id.tvAddress);
        TextView tvPhone = findViewById(R.id.tvPhone);
        TextView tvPayment = findViewById(R.id.tvPayment);
        TextView tvItems = findViewById(R.id.tvItems);
        TextView tvTotal = findViewById(R.id.tvTotal);
        Button btnBackToHome = findViewById(R.id.btnBackToHome);

        long orderId = getIntent().getLongExtra("order_id", 0);
        String name = getIntent().getStringExtra("customer_name");
        String address = getIntent().getStringExtra("address");
        String phone = getIntent().getStringExtra("phone");
        String payment = getIntent().getStringExtra("payment_method");
        int total = getIntent().getIntExtra("total_price", 0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String date = sdf.format(new Date());

        tvOrderId.setText("Order #" + orderId);
        tvDate.setText("Tanggal: " + date);
        tvCustomer.setText("Nama: " + name);
        tvAddress.setText("Alamat: " + address);
        tvPhone.setText("Telepon: " + phone);
        tvPayment.setText("Pembayaran: " + payment);
        tvTotal.setText("Total: Rp " + String.format("%,d", total).replace(",", "."));

        if (getIntent().hasExtra("product_name")) {
            String productName = getIntent().getStringExtra("product_name");
            int productPrice = getIntent().getIntExtra("product_price", 0);
            int qty = getIntent().getIntExtra("quantity", 1);
            tvItems.setText(productName + " x" + qty + " @ Rp " + String.format("%,d", productPrice).replace(",", "."));
        } else {
            tvItems.setText("Lihat detail pesanan di email (simulasi)");
        }

        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(ReceiptActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}