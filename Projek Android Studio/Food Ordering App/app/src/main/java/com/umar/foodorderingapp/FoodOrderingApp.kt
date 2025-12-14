package com.umar.foodorderingapp

import android.app.Application
import com.umar.foodorderingapp.util.FirebaseHelper

class FoodOrderingApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Inisialisasi Firebase dengan konfigurasi khusus
        FirebaseHelper.initializeFirebase(this)
    }
}