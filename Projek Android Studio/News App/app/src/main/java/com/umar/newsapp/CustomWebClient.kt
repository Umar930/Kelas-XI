package com.umar.newsapp

import android.graphics.Bitmap
import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar

class CustomWebClient(private val progressBar: ProgressBar?) : WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        progressBar?.visibility = android.view.View.VISIBLE
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        progressBar?.visibility = android.view.View.GONE
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        // For API 24+ and modern usage
        val url = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            request?.url.toString()
        } else {
            request?.url.toString()
        }
        view?.loadUrl(url)
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        view?.loadUrl(url ?: "")
        return true
    }
}
