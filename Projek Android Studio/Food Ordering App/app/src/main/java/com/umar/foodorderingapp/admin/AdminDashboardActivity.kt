package com.umar.foodorderingapp.admin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.umar.foodorderingapp.auth.LoginActivity
import com.umar.foodorderingapp.databinding.ActivityAdminDashboardBinding
import com.umar.foodorderingapp.util.Constants
import com.umar.foodorderingapp.util.showToast

class AdminDashboardActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var prefs: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize SharedPreferences
        prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE)
        
        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Admin Dashboard"
        
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        // Manage Foods button
        binding.btnManageFoods.setOnClickListener {
            // Navigate to Manage Foods Activity
            startActivity(Intent(this, ManageFoodsActivity::class.java))
        }
        
        // Manage Categories button
        binding.btnManageCategories.setOnClickListener {
            // Navigate to Manage Categories Activity
            // startActivity(Intent(this, ManageCategoriesActivity::class.java))
            showToast("Fitur kelola kategori akan segera hadir")
        }
        
        // View Orders button
        binding.btnViewOrders.setOnClickListener {
            // Navigate to Orders Activity
            // startActivity(Intent(this, OrdersActivity::class.java))
            showToast("Fitur lihat pesanan akan segera hadir")
        }
        
        // Logout button
        binding.btnLogout.setOnClickListener {
            logoutAdmin()
        }
    }
    
    private fun logoutAdmin() {
        // Sign out from Firebase Auth
        FirebaseAuth.getInstance().signOut()
        
        // Clear shared preferences
        prefs.edit()
            .remove(Constants.KEY_IS_LOGGED_IN)
            .remove(Constants.KEY_USER_ID)
            .remove(Constants.KEY_IS_ADMIN)
            .apply()
        
        // Navigate back to Login screen
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
