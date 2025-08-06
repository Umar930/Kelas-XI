package com.umar.konversisuhu;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText etSuhuAwal;
    private Spinner spPilihanKonversi;
    private TextView tvHasilKonversi;

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

        // Inisialisasi views
        load();
    }

    private void load() {
        // Menghubungkan variabel dengan komponen di layout
        etSuhuAwal = findViewById(R.id.etSuhuAwal);
        spPilihanKonversi = findViewById(R.id.spPilihanKonversi);
        tvHasilKonversi = findViewById(R.id.tvHasilKonversi);
    }

    public void btnKonversi(View view) {
        try {
            // Validasi input kosong
            String suhuStr = etSuhuAwal.getText().toString().trim();
            if (suhuStr.isEmpty()) {
                Toast.makeText(this, "Nilai tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                tvHasilKonversi.setText("Hasil: -");
                return;
            }

            // Konversi input ke double
            double suhuAwal = Double.parseDouble(suhuStr);

            // Ambil pilihan konversi
            String pilihan = spPilihanKonversi.getSelectedItem().toString();
            double hasil = 0;
            String satuanHasil = "";

            // Lakukan konversi sesuai pilihan
            switch (pilihan) {
                case "Celsius ke Reamur":
                    hasil = cToR(suhuAwal);
                    satuanHasil = "°R";
                    break;
                case "Celsius ke Fahrenheit":
                    hasil = cToF(suhuAwal);
                    satuanHasil = "°F";
                    break;
                case "Reamur ke Celsius":
                    hasil = rToC(suhuAwal);
                    satuanHasil = "°C";
                    break;
                case "Reamur ke Fahrenheit":
                    hasil = rToF(suhuAwal);
                    satuanHasil = "°F";
                    break;
                case "Fahrenheit ke Celsius":
                    hasil = fToC(suhuAwal);
                    satuanHasil = "°C";
                    break;
                case "Fahrenheit ke Reamur":
                    hasil = fToR(suhuAwal);
                    satuanHasil = "°R";
                    break;
            }

            // Tampilkan hasil dengan format 2 angka desimal
            String hasilStr = String.format("%.2f%s", hasil, satuanHasil);
            tvHasilKonversi.setText(hasilStr);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Masukkan angka yang valid!", Toast.LENGTH_SHORT).show();
            tvHasilKonversi.setText("Hasil: -");
        } catch (Exception e) {
            Toast.makeText(this, "Terjadi kesalahan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            tvHasilKonversi.setText("Hasil: -");
        }
    }

    private double cToR(double celsius) {
        return celsius * 4 / 5;
    }

    private double cToF(double celsius) {
        return (celsius * 9 / 5) + 32;
    }

    private double rToC(double reamur) {
        return reamur * 5 / 4;
    }

    private double rToF(double reamur) {
        return (reamur * 9 / 4) + 32;
    }

    private double fToC(double fahrenheit) {
        return (fahrenheit - 32) * 5 / 9;
    }

    private double fToR(double fahrenheit) {
        return (fahrenheit - 32) * 4 / 9;
    }
}