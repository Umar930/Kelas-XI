package com.umar.intentactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText etNamaBarang;

    private void load() {
        etNamaBarang = findViewById(R.id.etNamaBarang);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        load();
    }

    public void bukaBarangActivity(View view) {
        // Ambil teks dari EditText
        String namaBarang = etNamaBarang.getText().toString();

        // Buat Intent untuk berpindah ke BarangActivity
        Intent intent = new Intent(this, BarangActivity.class);

        // Kirim data menggunakan putExtra
        intent.putExtra("NAMA_BARANG_KEY", namaBarang);

        // Mulai Activity baru
        startActivity(intent);
    }
}