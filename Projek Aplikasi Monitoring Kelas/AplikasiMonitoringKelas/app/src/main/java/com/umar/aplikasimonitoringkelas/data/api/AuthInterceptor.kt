package com.umar.aplikasimonitoringkelas.data.api

import android.util.Log
import com.umar.aplikasimonitoringkelas.data.session.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor untuk menambahkan Authorization header secara otomatis
 */
class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    
    companion object {
        private const val TAG = "AuthInterceptor"
    }
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()
        
        // Ambil token dari SessionManager secara sinkron
        val token = sessionManager.getAuthTokenSync()
        
        Log.d(TAG, "Request URL: $url")
        Log.d(TAG, "Token available: ${!token.isNullOrEmpty()}")
        if (!token.isNullOrEmpty()) {
            Log.d(TAG, "Token: ${token.take(20)}...") // Log first 20 chars only
        }
        
        // Tambahkan header Accept dan Authorization jika ada token
        val newRequest = if (!token.isNullOrEmpty()) {
            originalRequest.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest.newBuilder()
                .addHeader("Accept", "application/json")
                .build()
        }
        
        val response = chain.proceed(newRequest)
        
        Log.d(TAG, "Response code: ${response.code}")
        
        // Auto logout jika token expired (401 Unauthorized)
        if (response.code == 401) {
            Log.w(TAG, "Received 401 Unauthorized - clearing auth")
            // Clear token saat unauthorized
            sessionManager.clearAuthSync()
        }
        
        return response
    }
}
