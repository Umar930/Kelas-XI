package com.umar.newsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment

class NewsWebFragment : Fragment() {

    private var webView: WebView? = null
    private var progressBar: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news_web, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView = view.findViewById(R.id.webViewContainer)
        progressBar = view.findViewById(R.id.progressBar)

        webView?.settings?.javaScriptEnabled = true
        webView?.settings?.domStorageEnabled = true
        webView?.settings?.cacheMode = WebSettings.LOAD_DEFAULT
        webView?.webViewClient = CustomWebClient(progressBar)
    }

    fun loadNewUrl(url: String) {
        if (webView == null) return
        webView?.loadUrl(url)
    }

    override fun onDestroyView() {
        webView = null
        progressBar = null
        super.onDestroyView()
    }
}
