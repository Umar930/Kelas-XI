package com.umar.onlineimageapp;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // Initialize ImageView references
        ImageView imageView1 = findViewById(R.id.imageViewOnline1);
        ImageView imageView2 = findViewById(R.id.imageViewOnline2);

        // Define two publicly accessible image URLs (placeholders)
        String URL_GAMBAR_1 = "https://picsum.photos/800/600?random=1";
        String URL_GAMBAR_2 = "https://picsum.photos/800/600?random=2";

        // Load first image with placeholder
        com.squareup.picasso.Picasso.get()
                .load(URL_GAMBAR_1)
                .placeholder(R.drawable.placeholder_image)
                .into(imageView1);

        // Load second image without placeholder (direct load)
        com.squareup.picasso.Picasso.get()
                .load(URL_GAMBAR_2)
                .into(imageView2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}