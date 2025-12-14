package com.umar.foodorderingapp.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.umar.foodorderingapp.MainActivity
import com.umar.foodorderingapp.admin.AdminLoginActivity
import com.umar.foodorderingapp.databinding.ActivityLoginBinding
import com.umar.foodorderingapp.model.User
import com.umar.foodorderingapp.util.Constants
import com.umar.foodorderingapp.util.showToast

class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var prefs: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        
        // Initialize SharedPreferences
        prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE)
        
        // Check if user is already logged in
        checkLoggedInStatus()
        
        setupClickListeners()
    }
    
    private fun checkLoggedInStatus() {
        val isLoggedIn = prefs.getBoolean(Constants.KEY_IS_LOGGED_IN, false)
        val isAdmin = prefs.getBoolean(Constants.KEY_IS_ADMIN, false)
        
        if (isLoggedIn) {
            if (isAdmin) {
                // Navigate to Admin Dashboard
                val intent = Intent(this, AdminLoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                // Navigate to Main Activity
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }
    
    private fun setupClickListeners() {
        // Login button click listener
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            
            if (validateInput(email, password)) {
                // Show loading indicator
                binding.progressBar.visibility = View.VISIBLE
                binding.btnLogin.isEnabled = false
                
                // Tampilkan dialog untuk konfirmasi login
                showAuthOptionsDialog(email, password)
            }
        }
        
        // Register text click listener
        binding.tvRegisterPrompt.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        
        // Forgot password text click listener
        binding.tvForgotPassword.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            
            if (email.isEmpty()) {
                binding.tilEmail.error = "Masukkan email terlebih dahulu"
                return@setOnClickListener
            }
            
            binding.progressBar.visibility = View.VISIBLE
            
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    binding.progressBar.visibility = View.GONE
                    
                    if (task.isSuccessful) {
                        showToast("Email reset password telah dikirim ke $email")
                    } else {
                        showToast("Gagal mengirim email reset: ${task.exception?.message}")
                    }
                }
        }
        
        // Admin login text click listener
        binding.tvAdminLogin.setOnClickListener {
            startActivity(Intent(this, AdminLoginActivity::class.java))
        }
    }
    
    private fun showAuthOptionsDialog(email: String, password: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Informasi Autentikasi")
            .setMessage("Metode login email/password mungkin belum diaktifkan di Firebase Console. Apakah Anda ingin mencoba login alternatif untuk demo?")
            .setPositiveButton("Ya") { _, _ ->
                // Login alternatif - anonymous auth untuk demo
                loginAnonymously(email)
            }
            .setNegativeButton("Tetap Coba Email") { _, _ ->
                // Coba login dengan email/password
                tryEmailPasswordLogin(email, password)
            }
            .setCancelable(false)
            .create()
            .show()
    }
    
    private fun tryEmailPasswordLogin(email: String, password: String) {
        binding.progressBar.visibility = View.VISIBLE
        
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                // Hide loading indicator
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = true
                
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId == null) {
                        showToast("Error: Tidak dapat mendapatkan ID pengguna")
                        return@addOnCompleteListener
                    }
                    
                    // Get user data from Firestore
                    db.collection(Constants.USERS_COLLECTION)
                        .document(userId)
                        .get()
                        .addOnSuccessListener { document ->
                            // Check if document exists
                            if (!document.exists()) {
                                // Jika data pengguna tidak ada di Firestore, buat data baru
                                val user = auth.currentUser
                                if (user != null) {
                                    val newUser = User(
                                        id = userId,
                                        name = user.displayName ?: email.substringBefore("@"),
                                        email = email,
                                        phone = "",
                                        address = "",
                                        isAdmin = false
                                    )
                                    
                                    db.collection(Constants.USERS_COLLECTION)
                                        .document(userId)
                                        .set(newUser)
                                        .addOnSuccessListener {
                                            saveUserDataAndNavigate(userId, false)
                                        }
                                        .addOnFailureListener {
                                            showToast("Gagal membuat data pengguna")
                                            auth.signOut()
                                        }
                                } else {
                                    showToast("Data pengguna tidak ditemukan")
                                    auth.signOut()
                                }
                            } else {
                                // Data pengguna ada, simpan dan navigasi
                                val isAdmin = document.getBoolean("isAdmin") ?: false
                                saveUserDataAndNavigate(userId, isAdmin)
                            }
                        }
                        .addOnFailureListener {
                            showToast("Gagal mengambil data pengguna. Periksa koneksi internet Anda.")
                            auth.signOut()
                        }
                } else {
                    val errorMessage = when {
                        task.exception?.message?.contains("no user record") == true -> 
                            "Email tidak terdaftar. Silakan register terlebih dahulu."
                        task.exception?.message?.contains("password is invalid") == true -> 
                            "Password salah. Silakan coba lagi."
                        task.exception?.message?.contains("network") == true -> 
                            "Koneksi internet bermasalah. Periksa koneksi Anda."
                        task.exception?.message?.contains("sign-in provider is disabled") == true -> 
                            "Metode login email/password tidak diaktifkan di Firebase. Menggunakan login alternatif."
                        else -> "Login gagal: ${task.exception?.message}"
                    }
                    showToast(errorMessage)
                    
                    // Jika error karena provider disabled, gunakan login anonim
                    if (task.exception?.message?.contains("sign-in provider is disabled") == true) {
                        loginAnonymously(email)
                    }
                }
            }
    }
    
    private fun loginAnonymously(email: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnLogin.isEnabled = false
        
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = true
                
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId == null) {
                        showToast("Error: Tidak dapat mendapatkan ID pengguna")
                        return@addOnCompleteListener
                    }
                    
                    // Buat data pengguna demo
                    val username = email.substringBefore("@")
                    val user = User(
                        id = userId,
                        name = "Demo User ($username)",
                        email = email,
                        phone = "08123456789",
                        address = "Alamat Demo",
                        isAdmin = false
                    )
                    
                    // Simpan data pengguna ke Firestore
                    db.collection(Constants.USERS_COLLECTION)
                        .document(userId)
                        .set(user)
                        .addOnSuccessListener {
                            showToast("Login demo berhasil! Mode: Anonymous")
                            saveUserDataAndNavigate(userId, false)
                        }
                        .addOnFailureListener {
                            showToast("Login berhasil, tetapi gagal menyimpan data pengguna")
                            saveUserDataAndNavigate(userId, false)
                        }
                } else {
                    showToast("Login alternatif gagal: ${task.exception?.message}")
                }
            }
    }
    
    private fun saveUserDataAndNavigate(userId: String, isAdmin: Boolean) {
        // Save login status and user data to SharedPreferences
        prefs.edit()
            .putBoolean(Constants.KEY_IS_LOGGED_IN, true)
            .putString(Constants.KEY_USER_ID, userId)
            .putBoolean(Constants.KEY_IS_ADMIN, isAdmin)
            .apply()
        
        showToast("Login berhasil!")
        
        // Navigate to Main Activity
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    
    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.etEmail.error = "Email tidak boleh kosong"
            return false
        }
        
        if (password.isEmpty()) {
            binding.etPassword.error = "Kata sandi tidak boleh kosong"
            return false
        }
        
        binding.etEmail.error = null
        binding.etPassword.error = null
        return true
    }
}
