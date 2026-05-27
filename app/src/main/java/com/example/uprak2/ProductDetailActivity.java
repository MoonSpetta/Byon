package com.example.uprak2;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivProductImage;
    private TextView tvProductName, tvProductPrice, tvProductDescription, tvRatingValue;
    private RatingBar ratingBar;
    private Button btnBuyNow, btnAddToCartDetail;
    private Toolbar toolbar;
    private ImageView icCartToolbar;
    private int productId;
    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        toolbar = findViewById(R.id.toolbar);
        icCartToolbar = findViewById(R.id.icCartToolbar);
        ivProductImage = findViewById(R.id.ivProductImage);
        tvProductName = findViewById(R.id.tvProductName);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvProductDescription = findViewById(R.id.tvProductDescription);
        ratingBar = findViewById(R.id.ratingBar);
        tvRatingValue = findViewById(R.id.tvRatingValue);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        btnAddToCartDetail = findViewById(R.id.btnAddToCartDetail);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Detail Produk");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        String productIdStr = getIntent().getStringExtra("product_id");
        if (productIdStr == null || productIdStr.isEmpty()) {
            Toast.makeText(this, "ID produk tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        try {
            productId = Integer.parseInt(productIdStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "ID produk tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchProductDetail();

        icCartToolbar.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));

        btnBuyNow.setOnClickListener(v -> {
            if (currentProduct != null) showQuantityDialog(currentProduct, true);
        });
        btnAddToCartDetail.setOnClickListener(v -> {
            if (currentProduct != null) showQuantityDialog(currentProduct, false);
        });
    }

    private void fetchProductDetail() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<ProductDetailResponse> call = apiService.getProductDetail(productId);
        call.enqueue(new Callback<ProductDetailResponse>() {
            @Override
            public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductDetailResponse res = response.body();
                    if ("success".equals(res.getStatus())) {
                        currentProduct = res.getData();
                        displayProduct(currentProduct);
                    } else {
                        Toast.makeText(ProductDetailActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Gagal mengambil data produk", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ProductDetailResponse> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void displayProduct(Product product) {
        tvProductName.setText(product.getName());
        tvProductPrice.setText("Rp " + String.format("%,d", product.getPrice()).replace(",", "."));
        String desc = product.getDescription();
        if (desc == null || desc.isEmpty()) desc = "Tidak ada deskripsi untuk produk ini.";
        tvProductDescription.setText(desc);
        float rating = (float) product.getRating();
        ratingBar.setRating(rating);
        tvRatingValue.setText("(" + rating + ")");
        String imageUrl = product.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).placeholder(R.drawable.ic_launcher_foreground).into(ivProductImage);
        } else {
            Glide.with(this).load(R.drawable.ic_launcher_foreground).into(ivProductImage);
        }
    }

    private void showQuantityDialog(Product product, boolean isDirectBuy) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_quantity, null);
        TextView tvDialogProductName = dialogView.findViewById(R.id.tvDialogProductName);
        EditText etQuantity = dialogView.findViewById(R.id.etQuantity);
        tvDialogProductName.setText(product.getName());
        etQuantity.setText("1");
        builder.setView(dialogView)
                .setTitle("Pilih Jumlah")
                .setPositiveButton("OK", (dialog, which) -> {
                    int qty = 1;
                    try {
                        qty = Integer.parseInt(etQuantity.getText().toString());
                        if (qty < 1) qty = 1;
                    } catch (NumberFormatException e) { qty = 1; }
                    if (isDirectBuy) {
                        Intent intent = new Intent(ProductDetailActivity.this, CheckoutActivity.class);
                        intent.putExtra("direct_buy", true);
                        intent.putExtra("product_id", product.getId());
                        intent.putExtra("product_name", product.getName());
                        intent.putExtra("product_price", product.getPrice());
                        intent.putExtra("quantity", qty);
                        startActivity(intent);
                    } else {
                        CartDatabaseHelper db = new CartDatabaseHelper(ProductDetailActivity.this);
                        for (int i = 0; i < qty; i++) {
                            db.addToCart(product);
                        }
                        Toast.makeText(ProductDetailActivity.this, qty + " item ditambahkan ke keranjang", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Batal", null);
        builder.create().show();
    }
}