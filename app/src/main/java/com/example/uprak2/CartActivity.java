package com.example.uprak2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartChangeListener {
    private RecyclerView rvCart;
    private TextView tvTotalPrice, tvEmptyCart;
    private Button btnCheckout;
    private LinearLayout llCheckout;
    private CartDatabaseHelper dbHelper;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        rvCart = findViewById(R.id.rvCart);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);
        btnCheckout = findViewById(R.id.btnCheckout);
        llCheckout = findViewById(R.id.llCheckout);

        dbHelper = new CartDatabaseHelper(this);
        loadCartItems();

        btnCheckout.setOnClickListener(v -> {
            if (cartItems == null || cartItems.isEmpty()) {
                Toast.makeText(this, "Keranjang kosong", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            intent.putExtra("direct_buy", false);
            startActivity(intent);
        });
    }

    private void loadCartItems() {
        cartItems = dbHelper.getCartItems();
        if (cartItems.isEmpty()) {
            rvCart.setVisibility(android.view.View.GONE);
            llCheckout.setVisibility(android.view.View.GONE);
            tvEmptyCart.setVisibility(android.view.View.VISIBLE);
        } else {
            rvCart.setVisibility(android.view.View.VISIBLE);
            llCheckout.setVisibility(android.view.View.VISIBLE);
            tvEmptyCart.setVisibility(android.view.View.GONE);
            rvCart.setLayoutManager(new LinearLayoutManager(this));
            cartAdapter = new CartAdapter(this, cartItems, this);
            rvCart.setAdapter(cartAdapter);
            updateTotalPrice();
        }
    }

    private void updateTotalPrice() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        tvTotalPrice.setText("Total: Rp " + String.format("%,d", total).replace(",", "."));
    }

    @Override
    public void onCartChanged() {
        loadCartItems();
    }
}