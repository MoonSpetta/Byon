package com.example.uprak2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private ImageView ivLogo;
    private TextView tvAppName;
    private int clickCount = 0;
    private Handler handler = new Handler();
    private Runnable resetCounter;
    private Runnable goToNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivLogo = findViewById(R.id.ivLogo);
        tvAppName = findViewById(R.id.tvAppName);

        // Klik rahasia untuk admin (5x cepat)
        View.OnClickListener secretClick = v -> {
            clickCount++;
            if (clickCount == 5) {
                if (goToNext != null) handler.removeCallbacks(goToNext);
                startActivity(new Intent(SplashActivity.this, AdminLoginActivity.class));
                Toast.makeText(SplashActivity.this, "Admin mode", Toast.LENGTH_SHORT).show();
                resetCounter();
                finish();
            } else {
                if (resetCounter != null) handler.removeCallbacks(resetCounter);
                resetCounter = () -> clickCount = 0;
                handler.postDelayed(resetCounter, 1000);
            }
        };
        ivLogo.setOnClickListener(secretClick);
        tvAppName.setOnClickListener(secretClick);

        // Default: setelah 2 detik pindah ke LoginActivity (atau Home jika sudah login)
        goToNext = () -> {
            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
            Intent intent;
            if (isLoggedIn) {
                intent = new Intent(SplashActivity.this, HomeActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        };
        handler.postDelayed(goToNext, 2000);
    }

    private void resetCounter() {
        clickCount = 0;
    }
}