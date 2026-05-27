package com.example.uprak2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FilterActivity extends AppCompatActivity {

    private RadioGroup rgKategori;
    private RadioButton rbSemua, rbFashion, rbElektronik, rbFurniture, rbKecantikan;
    private EditText etHargaMin, etHargaMax;
    private RatingBar ratingBar;
    private Button btnReset, btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        initViews();
        setupListeners();
    }

    private void initViews() {
        rgKategori = findViewById(R.id.rgKategori);
        rbSemua = findViewById(R.id.rbSemua);
        rbFashion = findViewById(R.id.rbFashion);
        rbElektronik = findViewById(R.id.rbElektronik);
        rbFurniture = findViewById(R.id.rbFurniture);
        rbKecantikan = findViewById(R.id.rbKecantikan);
        etHargaMin = findViewById(R.id.etHargaMin);
        etHargaMax = findViewById(R.id.etHargaMax);
        ratingBar = findViewById(R.id.ratingBar);
        btnReset = findViewById(R.id.btnReset);
        btnApply = findViewById(R.id.btnApply);
    }

    private void setupListeners() {
        btnReset.setOnClickListener(v -> resetFilters());
        btnApply.setOnClickListener(v -> applyFilters());
    }

    private void resetFilters() {
        rbSemua.setChecked(true);
        etHargaMin.setText("");
        etHargaMax.setText("");
        ratingBar.setRating(0f);
    }

    private void applyFilters() {
        String kategori = "Semua";
        int selectedId = rgKategori.getCheckedRadioButtonId();
        if (selectedId == R.id.rbFashion) kategori = "Fashion";
        else if (selectedId == R.id.rbElektronik) kategori = "Electronics";
        else if (selectedId == R.id.rbFurniture) kategori = "Furniture";
        else if (selectedId == R.id.rbKecantikan) kategori = "Beauty";

        String hargaMinStr = etHargaMin.getText().toString().trim();
        String hargaMaxStr = etHargaMax.getText().toString().trim();
        int hargaMin = hargaMinStr.isEmpty() ? 0 : Integer.parseInt(hargaMinStr);
        int hargaMax = hargaMaxStr.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(hargaMaxStr);
        float rating = ratingBar.getRating();

        Intent intent = new Intent(FilterActivity.this, SearchResultActivity.class);
        intent.putExtra("filter_kategori", kategori);
        intent.putExtra("filter_harga_min", hargaMin);
        intent.putExtra("filter_harga_max", hargaMax);
        intent.putExtra("filter_rating", rating);
        startActivity(intent);
        finish();
    }
}