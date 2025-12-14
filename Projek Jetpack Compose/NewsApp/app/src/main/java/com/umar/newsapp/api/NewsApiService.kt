package com.umar.newsapp.api

import com.umar.newsapp.model.NewsApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface untuk mendefinisikan endpoint dari News API
 */
interface NewsApiService {
    
    /**
     * Mendapatkan top headlines
     * 
     * @param apiKey API Key untuk News API
     * @param country Kode negara (opsional)
     * @param category Kategori berita (opsional)
     * @param language Kode bahasa (opsional)
     */
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("apiKey") apiKey: String,
        @Query("country") country: String? = null,
        @Query("category") category: String? = null,
        @Query("language") language: String? = null
    ): Response<NewsApiResponse>
    
    /**
     * Mendapatkan berita dengan query pencarian
     * 
     * @param apiKey API Key untuk News API
     * @param q Query pencarian
     * @param language Kode bahasa (opsional)
     * @param sortBy Pengurutan hasil (opsional): relevancy, popularity, publishedAt
     */
    @GET("v2/everything")
    suspend fun getEverything(
        @Query("apiKey") apiKey: String,
        @Query("q") q: String,
        @Query("language") language: String? = null,
        @Query("sortBy") sortBy: String? = "publishedAt"
    ): Response<NewsApiResponse>
}