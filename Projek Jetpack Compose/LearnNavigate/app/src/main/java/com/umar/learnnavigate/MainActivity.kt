package com.umar.learnnavigate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.umar.learnnavigate.ui.theme.LearnNavigateTheme

/**
 * Activity utama yang menjalankan aplikasi navigasi Jetpack Compose.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnNavigateTheme {
                // Menggunakan komponen navigasi yang telah dibuat
                MyAppNavigation()
            }
        }
    }
}