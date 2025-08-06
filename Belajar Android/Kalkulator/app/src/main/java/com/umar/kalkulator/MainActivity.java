package com.umar.kalkulator;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Deklarasi variabel untuk komponen UI
    private TextView tvHasil;
    private EditText etAngka1, etAngka2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Memanggil method load untuk inisialisasi komponen
        load();
    }

    // Method untuk inisialisasi komponen UI
    private void load() {
        tvHasil = findViewById(R.id.tvHasil);
        etAngka1 = findViewById(R.id.etAngka1);
        etAngka2 = findViewById(R.id.etAngka2);
    }

    // Method untuk validasi input
    private boolean validateInput() {
        if (etAngka1.getText().toString().equals("") || etAngka2.getText().toString().equals("")) {
            Toast.makeText(this, "Ada Bilangan Yang Kosong", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Method untuk mengambil nilai dari EditText
    private double[] getNumbers() throws NumberFormatException {
        double bil_1 = Double.parseDouble(etAngka1.getText().toString());
        double bil_2 = Double.parseDouble(etAngka2.getText().toString());
        return new double[] { bil_1, bil_2 };
    }

    // Method untuk menangani klik tombol penjumlahan
    public void btnJumlah(View view) {
        if (validateInput()) {
            try {
                double[] numbers = getNumbers();
                double hasil = numbers[0] + numbers[1];
                tvHasil.setText(String.valueOf(hasil));
                System.out.println("Hasil Penjumlahan: " + hasil);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Input Tidak Valid", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Method untuk menangani klik tombol pengurangan
    public void btnKurang(View view) {
        if (validateInput()) {
            try {
                double[] numbers = getNumbers();
                double hasil = numbers[0] - numbers[1];
                tvHasil.setText(String.valueOf(hasil));
                System.out.println("Hasil Pengurangan: " + hasil);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Input Tidak Valid", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Method untuk menangani klik tombol perkalian
    public void btnKali(View view) {
        if (validateInput()) {
            try {
                double[] numbers = getNumbers();
                double hasil = numbers[0] * numbers[1];
                tvHasil.setText(String.valueOf(hasil));
                System.out.println("Hasil Perkalian: " + hasil);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Input Tidak Valid", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Method untuk menangani klik tombol pembagian
    public void btnBagi(View view) {
        if (validateInput()) {
            try {
                double[] numbers = getNumbers();
                if (numbers[1] == 0) {
                    Toast.makeText(this, "Tidak Bisa Membagi Dengan Nol", Toast.LENGTH_SHORT).show();
                    return;
                }
                double hasil = numbers[0] / numbers[1];
                tvHasil.setText(String.valueOf(hasil));
                System.out.println("Hasil Pembagian: " + hasil);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Input Tidak Valid", Toast.LENGTH_SHORT).show();
            }
        }
    }
}