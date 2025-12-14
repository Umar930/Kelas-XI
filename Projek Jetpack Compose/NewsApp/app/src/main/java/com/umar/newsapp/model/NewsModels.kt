package com.umar.newsapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Model untuk merepresentasikan respons dari API berita
 */
data class NewsApiResponse(
    @SerializedName("status")
    val status: String,
    
    @SerializedName("totalResults")
    val totalResults: Int,
    
    @SerializedName("articles")
    val articles: List<Article>?
) : Serializable

/**
 * Model untuk merepresentasikan artikel berita
 */
data class Article(
    @SerializedName("source")
    val source: Source,
    
    @SerializedName("author")
    val author: String?,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("description")
    val description: String?,
    
    @SerializedName("url")
    val url: String,
    
    @SerializedName("urlToImage")
    val urlToImage: String?,
    
    @SerializedName("publishedAt")
    val publishedAt: String,
    
    @SerializedName("content")
    val content: String?
) : Serializable

/**
 * Model untuk merepresentasikan sumber berita
 */
data class Source(
    @SerializedName("id")
    val id: String?,
    
    @SerializedName("name")
    val name: String
) : Serializable