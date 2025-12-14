package com.umar.aplikasimonitoringkelas.data.api

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.umar.aplikasimonitoringkelas.data.session.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * RetrofitClient untuk konfigurasi Retrofit dengan OkHttp Interceptor
 */
object RetrofitClient {
    
    private const val TAG = "RetrofitClient"
    
    // Base URL API - Untuk Emulator gunakan 10.0.2.2, untuk Device gunakan IP komputer
    // Untuk Emulator: http://10.0.2.2:8000/
    // Untuk Device: http://192.168.x.x:8000/
    private const val BASE_URL = "http://10.155.162.157:8000/"
    
    // Timeout configuration - Diperbesar untuk koneksi yang lambat
    private const val CONNECT_TIMEOUT = 60L
    private const val READ_TIMEOUT = 60L
    private const val WRITE_TIMEOUT = 60L
    
    private var apiService: ApiService? = null
    
    /**
     * Mendapatkan instance ApiService (singleton)
     */
    fun getApiService(context: Context): ApiService {
        if (apiService == null) {
            apiService = createApiService(context)
        }
        return apiService!!
    }
    
    /**
     * Membuat instance ApiService dengan konfigurasi lengkap
     */
    fun createApiService(context: Context): ApiService {
        val sessionManager = SessionManager(context)
        
        // Logging Interceptor untuk debugging
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        // Auth Interceptor untuk menambahkan token dan Accept header
        val authInterceptor = AuthInterceptor(sessionManager)
        
        // OkHttpClient dengan interceptor
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true) // Enable retry
            .build()
        
        // Gson untuk JSON parsing
        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()
        
        // Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        
        return retrofit.create(ApiService::class.java)
    }
}
