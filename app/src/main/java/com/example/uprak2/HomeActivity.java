package com.example.uprak2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    TextView tvGreeting, tvUserName;
    ImageView icCart;
    EditText etSearch;
    ImageView ivSearchIcon, ivFilter;
    CardView cvBannerPromo;
    LinearLayout catFashion, catElectronics, catFurniture, catBeauty;
    CardView cardMyOrder, cardCart;
    private RecyclerView rvProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    Button btnLogout;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        initializeViews();
        setWelcomeMessage();
        setupListeners();
        fetchProductsFromApi();
    }

    private void initializeViews() {
        tvGreeting = findViewById(R.id.tvGreeting);
        tvUserName = findViewById(R.id.tvUserName);
        icCart = findViewById(R.id.icCart);
        etSearch = findViewById(R.id.etSearch);
        ivSearchIcon = findViewById(R.id.ivSearchIcon);
        ivFilter = findViewById(R.id.ivFilter);
        cvBannerPromo = findViewById(R.id.cvBannerPromo);
        catFashion = findViewById(R.id.catFashion);
        catElectronics = findViewById(R.id.catElectronics);
        catFurniture = findViewById(R.id.catFurniture);
        catBeauty = findViewById(R.id.catBeauty);
        cardMyOrder = findViewById(R.id.cardMyOrder);
        cardCart = findViewById(R.id.cardCart);
        btnLogout = findViewById(R.id.btnLogout);
        rvProducts = findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        productAdapter = new ProductAdapter(this, productList);
        rvProducts.setAdapter(productAdapter);
    }

    private void setWelcomeMessage() {
        String user = getIntent().getStringExtra("user");
        if (user == null || user.isEmpty()) user = prefs.getString("username", "User");
        tvGreeting.setText("Halo,");
        tvUserName.setText(user);
    }

    private void fetchProductsFromApi() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getProducts().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && "success".equals(response.body().getStatus())) {
                    productList.clear();
                    if (response.body().getData() != null) productList.addAll(response.body().getData());
                    productAdapter.notifyDataSetChanged();
                } else Toast.makeText(HomeActivity.this, "Gagal memuat produk", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListeners() {
        icCart.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, CartActivity.class)));
        View.OnClickListener searchAction = v -> {
            String query = etSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                Intent i = new Intent(HomeActivity.this, SearchResultActivity.class);
                i.putExtra("query", query);
                startActivity(i);
            } else Toast.makeText(HomeActivity.this, "Masukkan kata kunci", Toast.LENGTH_SHORT).show();
        };
        ivSearchIcon.setOnClickListener(searchAction);
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                searchAction.onClick(v);
                return true;
            }
            return false;
        });
        ivFilter.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, FilterActivity.class)));
        cvBannerPromo.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, PromoActivity.class)));
        View.OnClickListener categoryListener = v -> {
            String category = "";
            int id = v.getId();
            if (id == R.id.catFashion) category = "Fashion";
            else if (id == R.id.catElectronics) category = "Electronics";
            else if (id == R.id.catFurniture) category = "Furniture";
            else if (id == R.id.catBeauty) category = "Beauty";
            Intent i = new Intent(HomeActivity.this, CategoryActivity.class);
            i.putExtra("category", category);
            startActivity(i);
        };
        catFashion.setOnClickListener(categoryListener);
        catElectronics.setOnClickListener(categoryListener);
        catFurniture.setOnClickListener(categoryListener);
        catBeauty.setOnClickListener(categoryListener);
        cardMyOrder.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, MyOrderActivity.class)));
        cardCart.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, CartActivity.class)));
        btnLogout.setOnClickListener(v -> showLogoutConfirmation());
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("Ya", (dialog, which) -> performLogout())
                .setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss())
                .show();
    }


    private void performLogout() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();
        Toast.makeText(HomeActivity.this, "Logout berhasil", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(HomeActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Keluar Aplikasi")
                .setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("Ya", (dialog, which) -> finishAffinity())
                .setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss())
                .show();
    }
}