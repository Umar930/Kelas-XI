package com.umar.foodorderingapp.app

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.umar.foodorderingapp.util.FirebaseHelper

class FoodOrderingApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        
        // Inisialisasi Firebase dengan konfigurasi khusus
        FirebaseHelper.initializeFirebase(this)
        
        // Pastikan anonymous auth siap digunakan
        FirebaseAuth.getInstance()
    }
}
