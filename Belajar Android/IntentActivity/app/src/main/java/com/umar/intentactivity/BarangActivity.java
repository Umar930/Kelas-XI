package com.umar.intentactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BarangActivity extends AppCompatActivity {
    private TextView tvTampilNamaBarang;

    private void load() {
        tvTampilNamaBarang = findViewById(R.id.tvTampilNamaBarang);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barang);
        load();

        // Ambil Intent yang memanggil Activity ini
        Intent intent = getIntent();

        // Ambil data menggunakan kunci yang sama
        if (intent != null && intent.hasExtra("NAMA_BARANG_KEY")) {
            String namaBarangDiterima = intent.getStringExtra("NAMA_BARANG_KEY");
            tvTampilNamaBarang.setText(namaBarangDiterima);
        } else {
            tvTampilNamaBarang.setText("Tidak ada nama barang diterima.");
        }
    }
}
