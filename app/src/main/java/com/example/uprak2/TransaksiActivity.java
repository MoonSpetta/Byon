package com.example.uprak2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TransaksiActivity extends AppCompatActivity {

    private EditText etKomponen, etJumlah, etHarga;
    private TextView tvTotal;
    private Button btnHitung, btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        etKomponen = findViewById(R.id.etKomponen);
        etJumlah = findViewById(R.id.etJumlah);
        etHarga = findViewById(R.id.etHarga);
        tvTotal = findViewById(R.id.tvTotal);
        btnHitung = findViewById(R.id.btnHitung);
        btnSimpan = findViewById(R.id.btnSimpan);

        btnHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitungTotal();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanTransaksi();
            }
        });
    }

    private void hitungTotal() {
        String jumlahStr = etJumlah.getText().toString().trim();
        String hargaStr = etHarga.getText().toString().trim();

        if (jumlahStr.isEmpty() || hargaStr.isEmpty()) {
            Toast.makeText(this, "Isi jumlah dan harga", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int jumlah = Integer.parseInt(jumlahStr);
            int harga = Integer.parseInt(hargaStr);
            int total = jumlah * harga;
            tvTotal.setText("Total: Rp " + total);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Masukkan angka yang valid", Toast.LENGTH_SHORT).show();
        }
    }

    private void simpanTransaksi() {
        String komponen = etKomponen.getText().toString().trim();
        if (komponen.isEmpty()) {
            Toast.makeText(this, "Nama komponen harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }
        // Simulasi penyimpanan
        Toast.makeText(this, "Transaksi " + komponen + " disimpan", Toast.LENGTH_SHORT).show();
        finish(); // Kembali ke Home
    }
}