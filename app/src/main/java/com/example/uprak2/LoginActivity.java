package com.example.uprak2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvToRegister;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvToRegister = findViewById(R.id.tvToRegister);

        btnLogin.setOnClickListener(v -> loginUser());
        tvToRegister.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email dan password harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        String registeredEmail = prefs.getString("userEmail", "");
        String registeredPassword = prefs.getString("userPassword", "");
        String registeredUsername = prefs.getString("userName", "");
        int userId = prefs.getInt("user_id", 0);

        if (email.equals(registeredEmail) && password.equals(registeredPassword)) {
            // Login berhasil, simpan status login dan user_id (pastikan tidak 0)
            if (userId == 0) {
                // Jika user_id belum tersimpan (kemungkinan dari versi lama), coba dapatkan dari email
                // Untuk sementara, kita buat user_id baru (tidak ideal, tapi hindari crash)
                int lastUserId = prefs.getInt("last_user_id", 0);
                userId = lastUserId + 1;
                prefs.edit().putInt("user_id", userId).putInt("last_user_id", userId).apply();
            }
            prefs.edit()
                    .putBoolean("isLoggedIn", true)
                    .putString("username", registeredUsername)
                    .apply();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("user", registeredUsername);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Email atau password salah!", Toast.LENGTH_SHORT).show();
        }
    }
}