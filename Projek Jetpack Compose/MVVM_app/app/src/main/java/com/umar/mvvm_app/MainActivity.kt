 package com.umar.mvvm_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.umar.mvvm_app.ui.theme.MVVM_appTheme

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            // Inisialisasi ViewModel menggunakan viewModel() di dalam composable
            val newsViewModel: NewsViewModel = viewModel()
            
            // Collect theme state
            val isDarkTheme by newsViewModel.isDarkTheme.collectAsStateWithLifecycle()
            
            // Terapkan tema berdasarkan state yang dikumpulkan
            MVVM_appTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold { innerPadding ->
                        HomePage(
                            viewModel = newsViewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityScreenPreview() {
    MVVM_appTheme {
        Surface {
            // Preview tidak bisa menggunakan ViewModel yang sebenarnya, jadi dibuatkan mock di HomePage.kt
        }
    }
}