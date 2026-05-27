package com.example.uprak2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private EditText etFullName, etAddress, etPhone, etAccountNumber;
    private RadioGroup rgPayment;
    private Spinner spinnerBank;
    private Button btnConfirm;
    private TextView tvOrderSummary, tvTotalPrice;
    private boolean directBuy;
    private int productId, quantity, productPrice;
    private String productName;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        etFullName = findViewById(R.id.etFullName);
        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);
        etAccountNumber = findViewById(R.id.etAccountNumber);
        rgPayment = findViewById(R.id.rgPayment);
        spinnerBank = findViewById(R.id.spinnerBank);
        btnConfirm = findViewById(R.id.btnConfirmOrder);
        tvOrderSummary = findViewById(R.id.tvOrderSummary);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        directBuy = getIntent().getBooleanExtra("direct_buy", false);
        int total = 0;
        if (directBuy) {
            productId = getIntent().getIntExtra("product_id", 0);
            productName = getIntent().getStringExtra("product_name");
            productPrice = getIntent().getIntExtra("product_price", 0);
            quantity = getIntent().getIntExtra("quantity", 1);
            total = productPrice * quantity;
            tvOrderSummary.setText(productName + " x" + quantity + " @ Rp " + String.format("%,d", productPrice).replace(",", "."));
        } else {
            CartDatabaseHelper db = new CartDatabaseHelper(this);
            List<CartItem> cartItems = db.getCartItems();
            StringBuilder summary = new StringBuilder();
            for (CartItem item : cartItems) {
                summary.append(item.getName()).append(" x").append(item.getQuantity()).append("\n");
                total += item.getPrice() * item.getQuantity();
            }
            tvOrderSummary.setText(summary.toString());
        }
        tvTotalPrice.setText("Total: Rp " + String.format("%,d", total).replace(",", "."));
        final int finalTotal = total;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.bank_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBank.setAdapter(adapter);

        rgPayment.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbTransfer) {
                spinnerBank.setVisibility(View.VISIBLE);
                etAccountNumber.setVisibility(View.VISIBLE);
            } else {
                spinnerBank.setVisibility(View.GONE);
                etAccountNumber.setVisibility(View.GONE);
            }
        });
        spinnerBank.setVisibility(View.GONE);
        etAccountNumber.setVisibility(View.GONE);

        btnConfirm.setOnClickListener(v -> {
            String name = etFullName.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(CheckoutActivity.this, "Lengkapi data pengiriman", Toast.LENGTH_SHORT).show();
                return;
            }

            final String paymentMethod;
            final String bankName;
            final String accountNumber;
            int selectedId = rgPayment.getCheckedRadioButtonId();
            if (selectedId == R.id.rbCod) {
                paymentMethod = "COD";
                bankName = "";
                accountNumber = "";
            } else if (selectedId == R.id.rbTransfer) {
                paymentMethod = "Transfer Bank";
                bankName = spinnerBank.getSelectedItem().toString();
                accountNumber = etAccountNumber.getText().toString().trim();
                if (accountNumber.isEmpty()) {
                    Toast.makeText(CheckoutActivity.this, "Masukkan nomor rekening", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                Toast.makeText(CheckoutActivity.this, "Pilih metode pembayaran", Toast.LENGTH_SHORT).show();
                return;
            }

            int userId = prefs.getInt("user_id", 0);
            if (userId == 0) {
                Toast.makeText(CheckoutActivity.this, "Sesi login tidak valid. Silakan login ulang.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(CheckoutActivity.this, LoginActivity.class));
                finish();
                return;
            }

            PlaceOrderRequest request = new PlaceOrderRequest();
            request.user_id = userId;
            request.customer_name = name;
            request.address = address;
            request.phone = phone;
            request.payment_method = paymentMethod;
            request.bank_name = bankName;
            request.account_number = accountNumber;
            request.total_price = finalTotal;
            request.items = new ArrayList<>();

            if (directBuy) {
                PlaceOrderRequest.PlaceOrderItem item = new PlaceOrderRequest.PlaceOrderItem();
                item.product_id = productId;
                item.name = productName;
                item.price = productPrice;
                item.quantity = quantity;
                request.items.add(item);
            } else {
                CartDatabaseHelper db = new CartDatabaseHelper(CheckoutActivity.this);
                List<CartItem> cartItems = db.getCartItems();
                for (CartItem ci : cartItems) {
                    PlaceOrderRequest.PlaceOrderItem item = new PlaceOrderRequest.PlaceOrderItem();
                    item.product_id = ci.getProductId();
                    item.name = ci.getName();
                    item.price = ci.getPrice();
                    item.quantity = ci.getQuantity();
                    request.items.add(item);
                }
            }

            RetrofitClient.getApiService().placeOrder(request).enqueue(new retrofit2.Callback<PlaceOrderResponse>() {
                @Override
                public void onResponse(retrofit2.Call<PlaceOrderResponse> call, retrofit2.Response<PlaceOrderResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().success) {
                        Toast.makeText(CheckoutActivity.this, "Pesanan berhasil!", Toast.LENGTH_LONG).show();
                        if (!directBuy) new CartDatabaseHelper(CheckoutActivity.this).clearCart();
                        Intent intent = new Intent(CheckoutActivity.this, ReceiptActivity.class);
                        intent.putExtra("order_id", response.body().order_id);
                        intent.putExtra("order_number", response.body().order_number);
                        intent.putExtra("customer_name", name);
                        intent.putExtra("address", address);
                        intent.putExtra("phone", phone);
                        intent.putExtra("payment_method", paymentMethod);
                        intent.putExtra("total_price", finalTotal);
                        if (directBuy) {
                            intent.putExtra("product_name", productName);
                            intent.putExtra("product_price", productPrice);
                            intent.putExtra("quantity", quantity);
                        }
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(CheckoutActivity.this, "Gagal memesan: " + (response.body() != null ? response.body().message : "unknown"), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(retrofit2.Call<PlaceOrderResponse> call, Throwable t) {
                    Toast.makeText(CheckoutActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}