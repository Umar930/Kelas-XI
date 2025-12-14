package com.umar.firstcomposeapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umar.firstcomposeapp.ui.theme.FirstComposeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstComposeAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Memanggil fungsi utama UI kita
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

/**
 * Fungsi composable utama yang menampilkan UI aplikasi
 * 
 * @param modifier Modifier yang akan diterapkan ke elemen UI
 */
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    // Mengambil context untuk menampilkan Toast
    val context = LocalContext.current
    
    // Column untuk menyusun elemen UI secara vertikal
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Teks pertama: "Hello World"
        Text(
            text = "Hello World",
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // Teks kedua: "Good Morning"
        Text(
            text = "Good Morning",
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Tombol dengan teks "Click Me"
        Button(
            onClick = {
                // Menampilkan Toast ketika tombol diklik
                Toast.makeText(context, "Button is working", Toast.LENGTH_SHORT).show()
            }
        ) {
            Text(text = "Click Me")
        }
    }
}

/**
 * Preview untuk melihat tampilan UI tanpa menjalankan aplikasi
 */
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    FirstComposeAppTheme {
        MainScreen()
    }
}