package com.umar.aplikasimonitoringkelas

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.umar.aplikasimonitoringkelas.navigation.AppNavHost
import com.umar.aplikasimonitoringkelas.ui.theme.AplikasiMonitoringKelasTheme

/**
 * Main Activity - Entry point aplikasi
 * Menggunakan Jetpack Compose Navigation untuk mengelola navigasi antar layar
 */
class MainActivity : ComponentActivity() {
    
    companion object {
        private const val TAG = "MainActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "=================================")
        Log.d(TAG, "MainActivity onCreate() started")
        Log.d(TAG, "=================================")
        
        enableEdgeToEdge()
        setContent {
            AplikasiMonitoringKelasTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavHost(navController = navController)
                }
            }
        }
        
        Log.d(TAG, "MainActivity onCreate() completed")
    }
    
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "MainActivity onStart()")
    }
    
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "MainActivity onResume()")
    }
    
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "MainActivity onPause()")
    }
    
    override fun onDestroy() {
        Log.d(TAG, "MainActivity onDestroy()")
        super.onDestroy()
    }
}
