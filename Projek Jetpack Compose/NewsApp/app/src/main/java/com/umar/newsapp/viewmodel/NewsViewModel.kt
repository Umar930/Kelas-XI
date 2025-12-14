package com.umar.newsapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umar.newsapp.Constants
import com.umar.newsapp.api.RetrofitInstance
import com.umar.newsapp.model.Article
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    
    private val newsApiService = RetrofitInstance.newsApiService
    
    private val _articles = MutableLiveData<List<Article>>(emptyList())
    val articles: LiveData<List<Article>> = _articles
    
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error
    
    // Kategori yang sedang aktif
    private val _activeCategory = MutableLiveData<String>("general")
    val activeCategory: LiveData<String> = _activeCategory
    
    init {
        fetchTopHeadlines()
    }
    
    /**
     * Mendapatkan berita top headlines dengan kategori tertentu
     * 
     * @param category Kategori berita, nilai default "general"
     */
    fun fetchTopHeadlines(category: String = "general") {
        _isLoading.value = true
        _error.value = null
        _activeCategory.value = category.lowercase()
        
        viewModelScope.launch {
            try {
                // Panggil API dengan Retrofit dan kategori
                val response = newsApiService.getTopHeadlines(
                    apiKey = Constants.API_KEY,
                    language = "en", // Bahasa Inggris (en) atau ganti dengan "id" untuk Bahasa Indonesia
                    category = category.lowercase()
                )
                
                _isLoading.value = false
                
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    if (newsResponse?.articles != null) {
                        _articles.value = newsResponse.articles
                    } else {
                        _error.value = "Tidak ada artikel yang diterima"
                    }
                } else {
                    _error.value = "Error: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e.localizedMessage ?: "Terjadi kesalahan saat mengambil data"
                Log.e("NewsViewModel", "Exception in fetchTopHeadlines", e)
            }
        }
    }
    
    /**
     * Mencari berita berdasarkan query
     * 
     * @param query Kata kunci pencarian
     */
    fun fetchEverythingWithQuery(query: String) {
        if (query.isBlank()) return
        
        _isLoading.value = true
        _error.value = null
        
        viewModelScope.launch {
            try {
                // Panggil API dengan query
                val response = newsApiService.getEverything(
                    apiKey = Constants.API_KEY,
                    q = query,
                    language = "en" // Bahasa Inggris (en) atau ganti dengan "id" untuk Bahasa Indonesia
                )
                
                _isLoading.value = false
                
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    if (newsResponse?.articles != null) {
                        _articles.value = newsResponse.articles
                    } else {
                        _error.value = "Tidak ada artikel yang diterima"
                    }
                } else {
                    _error.value = "Error: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e.localizedMessage ?: "Terjadi kesalahan saat mengambil data"
                Log.e("NewsViewModel", "Exception in fetchEverythingWithQuery", e)
            }
        }
    }
}