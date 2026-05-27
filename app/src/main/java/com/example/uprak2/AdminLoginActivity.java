package com.example.uprak2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "admin123";
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        etEmail = findViewById(R.id.etAdminUsername);
        etPassword = findViewById(R.id.etAdminPassword);
        btnLogin = findViewById(R.id.btnAdminLogin);

        // Jika sudah login sebelumnya, langsung ke AdminHomeActivity
        if (prefs.getBoolean("isAdminLoggedIn", false)) {
            startActivity(new Intent(this, AdminHomeActivity.class));
            finish();
            return;
        }

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD)) {
                prefs.edit().putBoolean("isAdminLoggedIn", true).apply();
                startActivity(new Intent(AdminLoginActivity.this, AdminHomeActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Email atau password salah!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}