package com.umar.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.umar.newsapp.navigation.HomeScreen
import com.umar.newsapp.navigation.NewsArticleScreen
import java.net.URLDecoder
import com.umar.newsapp.ui.screens.HomePage
import com.umar.newsapp.ui.screens.detail.NewsArticlePage
import com.umar.newsapp.ui.theme.NewsAppTheme
import com.umar.newsapp.viewmodel.NewsViewModel

class MainActivity : ComponentActivity() {
    // Inisialisasi NewsViewModel
    private val newsViewModel: NewsViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsAppTheme {
                // Menggunakan Surface sebagai container utama
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()
                    
                    // Scaffold untuk layout dasar dengan padding
                    Scaffold { innerPadding ->
                        // NavHost untuk navigasi
                        NavHost(
                            navController = navController,
                            startDestination = HomeScreen.route
                        ) {
                            // Rute Home Screen
                            composable(HomeScreen.route) {
                                HomePage(
                                    viewModel = newsViewModel,
                                    navController = navController,
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            
                            // Rute News Article Screen
                            composable(NewsArticleScreen.route) { backStackEntry ->
                                val articleUrl = backStackEntry.arguments?.getString("articleUrl")
                                val decodedUrl = java.net.URLDecoder.decode(articleUrl, "UTF-8")
                                NewsArticlePage(articleUrl = decodedUrl)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsAppPreview() {
    NewsAppTheme {
        // Preview HomePage dengan mock data akan memerlukan ViewModel dummy
        // yang tidak ditampilkan di sini untuk kesederhanaan
    }
}