package com.umar.counter;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Deklarasi variabel global
    private int count = 0;
    private TextView tvCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        // Inisialisasi komponen UI
        load();
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    // Metode untuk inisialisasi komponen UI
    private void load() {
        tvCounter = findViewById(R.id.tvCounter);
        tvCounter.setText(String.valueOf(count));
    }
    
    // Metode untuk menambah nilai counter
    public void btnTambah(View view) {
        count++;
        tvCounter.setText(String.valueOf(count));
        System.out.println("Tombol TAMBAH ditekan, count: " + count);
    }
    
    // Metode untuk mengurangi nilai counter
    public void btnKurang(View view) {
        count--;
        tvCounter.setText(String.valueOf(count));
        System.out.println("Tombol KURANG ditekan, count: " + count);
    }
}