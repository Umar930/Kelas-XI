package com.umar.sharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText etNamaBarang, etStokBarang;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi komponen UI
        etNamaBarang = findViewById(R.id.et_nama_barang);
        etStokBarang = findViewById(R.id.et_stok_barang);

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("data_barang", MODE_PRIVATE);
    }

    public void simpan(View view) {
        // Ambil teks dari EditText
        String namaBarang = etNamaBarang.getText().toString().trim();
        String stokText = etStokBarang.getText().toString().trim();

        // Validasi input
        if (namaBarang.isEmpty() || stokText.isEmpty()) {
            Toast.makeText(this, "Data Kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Konversi stok ke float
            float stokBarang = Float.parseFloat(stokText);

            // Simpan ke SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("barang", namaBarang);
            editor.putFloat("stok", stokBarang);
            editor.apply();

            // Tampilkan pesan sukses
            Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();

            // Kosongkan EditText
            etNamaBarang.setText("");
            etStokBarang.setText("");

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Stok harus berupa angka!", Toast.LENGTH_SHORT).show();
        }
    }

    public void tampil(View view) {
        // Ambil data dari SharedPreferences
        String namaBarang = sharedPreferences.getString("barang", "");
        float stokBarang = sharedPreferences.getFloat("stok", 0.0f);

        // Tampilkan data di EditText
        etNamaBarang.setText(namaBarang);
        etStokBarang.setText(String.valueOf(stokBarang));

        // Tampilkan pesan konfirmasi
        if (namaBarang.isEmpty() && stokBarang == 0.0f) {
            Toast.makeText(this, "Tidak ada data yang tersimpan", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Data berhasil dimuat", Toast.LENGTH_SHORT).show();
        }
    }
}
