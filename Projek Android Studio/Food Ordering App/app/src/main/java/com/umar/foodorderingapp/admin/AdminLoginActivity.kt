package com.umar.foodorderingapp.admin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.umar.foodorderingapp.databinding.ActivityAdminLoginBinding
import com.umar.foodorderingapp.util.Constants
import com.umar.foodorderingapp.util.showToast

class AdminLoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAdminLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var prefs: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        
        // Initialize SharedPreferences
        prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE)
        
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        // Login button click listener
        binding.btnAdminLogin.setOnClickListener {
            val email = binding.etAdminEmail.text.toString().trim()
            val password = binding.etAdminPassword.text.toString().trim()
            
            if (validateInput(email, password)) {
                loginAsAdmin(email, password)
            }
        }
        
        // Back button click listener
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }
    
    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.tilAdminEmail.error = "Email tidak boleh kosong"
            return false
        }
        
        if (password.isEmpty()) {
            binding.tilAdminPassword.error = "Kata sandi tidak boleh kosong"
            return false
        }
        
        binding.tilAdminEmail.error = null
        binding.tilAdminPassword.error = null
        return true
    }
    
    private fun loginAsAdmin(email: String, password: String) {
        // Show loading indicator
        binding.progressBar.visibility = View.VISIBLE
        binding.btnAdminLogin.isEnabled = false
        
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                // Hide loading indicator
                binding.progressBar.visibility = View.GONE
                binding.btnAdminLogin.isEnabled = true
                
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    
                    // Check if user is an admin
                    db.collection(Constants.USERS_COLLECTION)
                        .document(userId)
                        .get()
                        .addOnSuccessListener { document ->
                            val isAdmin = document.getBoolean("isAdmin") ?: false
                            
                            if (isAdmin) {
                                // Save user data to SharedPreferences
                                prefs.edit()
                                    .putBoolean(Constants.KEY_IS_LOGGED_IN, true)
                                    .putString(Constants.KEY_USER_ID, userId)
                                    .putBoolean(Constants.KEY_IS_ADMIN, true)
                                    .apply()
                                
                                showToast("Login admin berhasil!")
                                
                                // Navigate to Admin Dashboard
                                val intent = Intent(this, AdminDashboardActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            } else {
                                showToast("Akun ini bukan akun admin")
                            }
                        }
                        .addOnFailureListener {
                            showToast("Gagal mengambil data pengguna")
                        }
                } else {
                    showToast("Login gagal: ${task.exception?.message}")
                }
            }
    }
}
