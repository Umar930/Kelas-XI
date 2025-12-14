package com.umar.foodorderingapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.umar.foodorderingapp.admin.AdminDashboardActivity
import com.umar.foodorderingapp.auth.LoginActivity
import com.umar.foodorderingapp.util.Constants

class SplashActivity : AppCompatActivity() {
    
    private lateinit var prefs: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        // Initialize SharedPreferences
        prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE)

        // Handler untuk menunda perpindahan ke aktivitas berikutnya
        Handler(Looper.getMainLooper()).postDelayed({
            checkLoginStatus()
        }, 2000) // Delay 2 detik
    }
    
    private fun checkLoginStatus() {
        val isLoggedIn = prefs.getBoolean(Constants.KEY_IS_LOGGED_IN, false)
        val isAdmin = prefs.getBoolean(Constants.KEY_IS_ADMIN, false)
        val userId = prefs.getString(Constants.KEY_USER_ID, "")
        
        // Validasi status login dengan Firebase Auth
        val currentUser = FirebaseAuth.getInstance().currentUser
        
        if (isLoggedIn && userId?.isNotEmpty() == true && currentUser != null) {
            // User is logged in
            if (isAdmin) {
                // Navigate to Admin Dashboard
                startActivity(Intent(this, AdminDashboardActivity::class.java))
            } else {
                // Navigate to Main Activity
                startActivity(Intent(this, MainActivity::class.java))
            }
        } else {
            // User is not logged in
            startActivity(Intent(this, LoginActivity::class.java))
        }
        
        finish()
    }
}
