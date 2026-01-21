package com.umar.warunggo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.umar.warunggo.navigation.NavGraph
import com.umar.warunggo.ui.theme.WarungGoTheme

/**
 * MainActivity
 * Entry point aplikasi WarungGo
 * Setup navigation dan theme
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WarungGoTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}