package com.umar.aplikasimonitoringkelas.data.api

import android.util.Log
import com.umar.aplikasimonitoringkelas.MonitoringApplication

/**
 * Singleton provider untuk ApiService
 * Memastikan hanya ada satu instance ApiService di seluruh aplikasi
 */
object ApiServiceProvider {
    
    private const val TAG = "ApiServiceProvider"
    
    @Volatile
    private var apiService: ApiService? = null
    
    /**
     * Mendapatkan instance ApiService
     * Membuat instance baru jika belum ada
     */
    fun getApiService(): ApiService {
        Log.d(TAG, "getApiService() called, current instance: $apiService")
        
        return apiService ?: synchronized(this) {
            apiService ?: try {
                Log.d(TAG, "Creating new ApiService instance")
                val context = MonitoringApplication.instance.applicationContext
                Log.d(TAG, "Got application context: $context")
                
                val service = RetrofitClient.createApiService(context)
                Log.d(TAG, "ApiService created successfully: $service")
                
                apiService = service
                service
            } catch (e: Exception) {
                Log.e(TAG, "Error creating ApiService", e)
                throw e
            }
        }
    }
    
    /**
     * Reset ApiService (berguna untuk logout atau refresh)
     */
    fun reset() {
        synchronized(this) {
            Log.d(TAG, "Resetting ApiService")
            apiService = null
        }
    }
}
