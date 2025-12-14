package com.umar.stateexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.umar.stateexample.ui.theme.StateExampleTheme

/**
 * Activity utama aplikasi. 
 * 
 * Aplikasi ini mendemonstrasikan tiga metode utama manajemen state di Jetpack Compose:
 * 1. remember - untuk state lokal yang bertahan selama recomposition
 * 2. rememberSaveable - untuk state lokal yang bertahan selama perubahan konfigurasi
 * 3. ViewModel & LiveData - untuk state yang diangkat (hoisted) dan dikelola di luar UI
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StateExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Gunakan StateTestScreen sebagai layar utama
                    // ViewModel secara otomatis dibuat oleh Compose pada pemanggilan pertama
                    StateTestScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}