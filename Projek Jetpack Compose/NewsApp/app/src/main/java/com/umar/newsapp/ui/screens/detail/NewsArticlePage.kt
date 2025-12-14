package com.umar.newsapp.ui.screens.detail

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Halaman untuk menampilkan artikel berita dalam WebView
 * 
 * @param articleUrl URL artikel yang akan ditampilkan
 */
@Composable
fun NewsArticlePage(articleUrl: String) {
    // State untuk menunjukkan apakah halaman sedang dimuat
    var isLoading by remember { mutableStateOf(true) }
    
    Box(modifier = Modifier.fillMaxSize()) {
        // AndroidView untuk membungkus WebView
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    // Konfigurasi WebView
                    settings.javaScriptEnabled = true
                    
                    // WebViewClient untuk menangani pemuatan halaman
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            // Setelah halaman selesai dimuat, ubah state loading menjadi false
                            isLoading = false
                        }
                    }
                    
                    // Muat URL artikel
                    loadUrl(articleUrl)
                }
            },
            update = { webView ->
                // Update WebView jika URL berubah
                webView.loadUrl(articleUrl)
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // Tampilkan loading indicator saat halaman sedang dimuat
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}