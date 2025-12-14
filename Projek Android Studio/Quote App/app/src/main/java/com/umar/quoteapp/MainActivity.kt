package com.umar.quoteapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    // UI components
    private lateinit var tvQuote: TextView
    private lateinit var btnShare: Button
    private lateinit var btnNewQuote: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Adjust padding for system bars when using edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        tvQuote = findViewById(R.id.tv_quote)
        btnShare = findViewById(R.id.btn_share)
        btnNewQuote = findViewById(R.id.btn_new_quote)

        // Show an initial random quote
        displayRandomQuote()

        // New quote button shows another random quote
        btnNewQuote.setOnClickListener {
            displayRandomQuote()
        }

        // Share button uses an implicit intent (ACTION_SEND) to share the current quote
        btnShare.setOnClickListener {
            val currentQuote = tvQuote.text.toString()

            // Create an implicit intent with action ACTION_SEND
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain" // plain text content
                putExtra(Intent.EXTRA_TEXT, currentQuote) // the quote to share
            }

            // Use a chooser to let the user pick the target app
            val chooser = Intent.createChooser(shareIntent, "Bagikan kutipan ini melalui...")
            // startActivity with the chooser will show available apps (WhatsApp, Email, etc.)
            startActivity(chooser)
        }
    }

    // Selects and displays a random quote from the static list
    private fun displayRandomQuote() {
        val index = Random.nextInt(quotes.size)
        tvQuote.text = quotes[index]
    }

    companion object {
        // Static list of quotes (minimal 10) declared here as required
        val quotes = listOf(
            "Hidup adalah 10% apa yang terjadi pada kita dan 90% bagaimana kita bereaksi terhadapnya. — Charles R. Swindoll",
            "Jadilah perubahan yang ingin kamu lihat di dunia. — Mahatma Gandhi",
            "Kesuksesan bukanlah akhir, kegagalan bukanlah fatal: yang penting adalah keberanian untuk melanjutkan. — Winston Churchill",
            "Jangan menunggu; waktu tidak akan pernah 'tepat'. Mulailah dari tempat yang Anda berdiri. — Napoleon Hill",
            "Rahasia untuk maju adalah memulai. — Mark Twain",
            "Kebahagiaan tidak bergantung pada keadaan luar, tetapi pada keadaan batin. — Dale Carnegie",
            "Pelajari aturan dengan baik, supaya kamu bisa melanggarnya dengan benar. — Dalai Lama",
            "Satu-satunya cara melakukan pekerjaan besar adalah mencintai apa yang Anda lakukan. — Steve Jobs",
            "Jangan takut mengambil langkah besar. Anda tidak dapat menyeberangi jurang dengan dua lompatan kecil. — David Lloyd George",
            "Keberanian bukanlah ketiadaan ketakutan, melainkan kemenangan atasnya. — Nelson Mandela"
        )
    }
}