package com.umar.newsapp.navigation

import kotlinx.serialization.Serializable

/**
 * Definisi rute untuk navigasi type-safe
 */
object HomeScreen {
    const val route = "home_screen"
}

/**
 * Rute untuk menampilkan detail artikel berita
 * @param articleUrl URL artikel yang akan ditampilkan
 */
@Serializable
data class NewsArticleScreen(val articleUrl: String) {
    companion object {
        const val route = "news_article_screen/{articleUrl}"
    }
}