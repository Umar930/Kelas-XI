package com.umar.foodorderingapp.profile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.umar.foodorderingapp.auth.LoginActivity
import com.umar.foodorderingapp.databinding.ActivityProfileBinding
import com.umar.foodorderingapp.util.Constants
import com.umar.foodorderingapp.util.showToast

class ProfileActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var prefs: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        
        // Initialize SharedPreferences
        prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE)
        
        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        setupClickListeners()
        loadUserData()
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    
    private fun setupClickListeners() {
        // Orders button click
        binding.btnOrders.setOnClickListener {
            // TODO: Navigate to orders screen
            showToast("Fitur Pesanan akan segera hadir")
        }
        
        // Logout button click
        binding.btnLogout.setOnClickListener {
            // Sign out from Firebase
            auth.signOut()
            
            // Clear shared preferences
            prefs.edit().clear().apply()
            
            // Navigate to login screen
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
    
    private fun loadUserData() {
        val userId = auth.currentUser?.uid ?: return
        
        db.collection(Constants.USERS_COLLECTION)
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    binding.tvName.text = document.getString("name") ?: ""
                    binding.tvEmail.text = document.getString("email") ?: ""
                    binding.tvPhone.text = document.getString("phone") ?: ""
                    binding.tvAddress.text = document.getString("address") ?: ""
                }
            }
            .addOnFailureListener {
                showToast("Gagal memuat data pengguna")
            }
    }
}
