package com.example.uprak2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AdminHomeActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Admin Panel");
        }

        Button btnAddProduct = findViewById(R.id.btnAddProduct);
        Button btnManageProducts = findViewById(R.id.btnManageProducts);
        Button btnLogout = findViewById(R.id.btnLogoutAdminHome);

        btnAddProduct.setOnClickListener(v -> startActivity(new Intent(this, AdminDashboardActivity.class)));
        btnManageProducts.setOnClickListener(v -> startActivity(new Intent(this, AdminProductListActivity.class)));
        btnLogout.setOnClickListener(v -> {
            prefs.edit().remove("isAdminLoggedIn").apply();
            startActivity(new Intent(this, AdminLoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!prefs.getBoolean("isAdminLoggedIn", false)) {
            startActivity(new Intent(this, AdminLoginActivity.class));
            finish();
        }
    }
}