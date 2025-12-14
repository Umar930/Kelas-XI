package com.umar.foodorderingapp.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.umar.foodorderingapp.MainActivity
import com.umar.foodorderingapp.databinding.ActivityRegisterBinding
import com.umar.foodorderingapp.model.User
import com.umar.foodorderingapp.util.Constants
import com.umar.foodorderingapp.util.showToast

class RegisterActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var prefs: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        
        // Initialize SharedPreferences
        prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE)
        
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        // Register button click listener
        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val address = binding.etAddress.text.toString().trim()
            
            if (validateInput(name, email, password, phone, address)) {
                // Show loading indicator
                binding.progressBar.visibility = View.VISIBLE
                binding.btnRegister.isEnabled = false
                
                // Show registration options
                showRegistrationOptionsDialog(name, email, password, phone, address)
            }
        }
        
        // Login text click listener
        binding.tvLoginPrompt.setOnClickListener {
            finish() // Return to login screen
        }
    }
    
    private fun showRegistrationOptionsDialog(
        name: String,
        email: String,
        password: String,
        phone: String,
        address: String
    ) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Informasi Registrasi")
            .setMessage("Metode registrasi email mungkin belum diaktifkan di Firebase Console. Pilih metode registrasi:")
            .setPositiveButton("Registrasi Anonymous") { _, _ ->
                // Register with anonymous auth
                registerWithAnonymous(name, email, phone, address)
            }
            .setNegativeButton("Coba Email/Password") { _, _ ->
                // Try standard registration
                registerWithEmailPassword(name, email, password, phone, address)
            }
            .setCancelable(false)
            .create()
            .show()
    }
    
    private fun registerWithEmailPassword(
        name: String,
        email: String,
        password: String,
        phone: String,
        address: String
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registration successful
                    val user = auth.currentUser
                    if (user != null) {
                        // Update display name
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build()
                        
                        user.updateProfile(profileUpdates)
                            .addOnCompleteListener { profileTask ->
                                if (!profileTask.isSuccessful) {
                                    showToast("Gagal memperbarui profil pengguna")
                                }
                            }
                        
                        // Create user data in Firestore
                        createUserInFirestore(user.uid, name, email, phone, address, false)
                    } else {
                        showToast("Error: Tidak dapat mendapatkan data pengguna")
                        binding.progressBar.visibility = View.GONE
                        binding.btnRegister.isEnabled = true
                    }
                } else {
                    // If registration fails, display a message to the user
                    binding.progressBar.visibility = View.GONE
                    binding.btnRegister.isEnabled = true
                    
                    val errorMessage = when {
                        task.exception?.message?.contains("email address is already in use") == true ->
                            "Email sudah terdaftar. Silakan login."
                        task.exception?.message?.contains("password") == true ->
                            "Password terlalu lemah. Gunakan minimal 6 karakter."
                        task.exception?.message?.contains("sign-in provider is disabled") == true ->
                            "Metode registrasi email belum diaktifkan di Firebase Console."
                        else -> "Registrasi gagal: ${task.exception?.message}"
                    }
                    showToast(errorMessage)
                    
                    // If provider disabled, offer anonymous registration
                    if (task.exception?.message?.contains("sign-in provider is disabled") == true) {
                        offerAnonymousRegistration(name, email, phone, address)
                    }
                }
            }
    }
    
    private fun registerWithAnonymous(
        name: String,
        email: String,
        phone: String,
        address: String
    ) {
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Anonymous sign-in success
                    val user = auth.currentUser
                    if (user != null) {
                        // Create user data in Firestore
                        createUserInFirestore(user.uid, name, email, phone, address, false)
                    } else {
                        binding.progressBar.visibility = View.GONE
                        binding.btnRegister.isEnabled = true
                        showToast("Error: Tidak dapat mendapatkan data pengguna")
                    }
                } else {
                    // If sign-in fails, display a message to the user
                    binding.progressBar.visibility = View.GONE
                    binding.btnRegister.isEnabled = true
                    showToast("Registrasi anonymous gagal: ${task.exception?.message}")
                }
            }
    }
    
    private fun offerAnonymousRegistration(
        name: String,
        email: String,
        phone: String,
        address: String
    ) {
        AlertDialog.Builder(this)
            .setTitle("Registrasi Email Dinonaktifkan")
            .setMessage("Metode registrasi email belum diaktifkan di Firebase. Apakah Anda ingin mendaftar sebagai pengguna anonymous?")
            .setPositiveButton("Ya") { _, _ ->
                registerWithAnonymous(name, email, phone, address)
            }
            .setNegativeButton("Tidak") { _, _ ->
                binding.progressBar.visibility = View.GONE
                binding.btnRegister.isEnabled = true
            }
            .setCancelable(false)
            .create()
            .show()
    }
    
    private fun createUserInFirestore(
        userId: String,
        name: String,
        email: String,
        phone: String,
        address: String,
        isAdmin: Boolean
    ) {
        val user = User(
            id = userId,
            name = name,
            email = email,
            phone = phone,
            address = address,
            isAdmin = isAdmin
        )
        
        db.collection(Constants.USERS_COLLECTION).document(userId)
            .set(user)
            .addOnSuccessListener {
                // Save user info to SharedPreferences
                saveUserInfoLocally(userId, isAdmin)
                
                showToast("Registrasi berhasil!")
                
                // Navigate to Main Activity
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                binding.btnRegister.isEnabled = true
                showToast("Error creating user profile: ${e.message}")
                
                // Sign out the user if profile creation fails
                auth.signOut()
            }
    }
    
    private fun saveUserInfoLocally(userId: String, isAdmin: Boolean) {
        with(prefs.edit()) {
            putString(Constants.KEY_USER_ID, userId)
            putBoolean(Constants.KEY_IS_LOGGED_IN, true)
            putBoolean(Constants.KEY_IS_ADMIN, isAdmin)
            apply()
        }
    }
    
    private fun validateInput(
        name: String,
        email: String,
        password: String,
        phone: String,
        address: String
    ): Boolean {
        if (name.isEmpty()) {
            binding.etName.error = "Nama harus diisi"
            binding.etName.requestFocus()
            return false
        }
        
        if (email.isEmpty()) {
            binding.etEmail.error = "Email harus diisi"
            binding.etEmail.requestFocus()
            return false
        }
        
        if (password.isEmpty()) {
            binding.etPassword.error = "Password harus diisi"
            binding.etPassword.requestFocus()
            return false
        }
        
        if (password.length < 6) {
            binding.etPassword.error = "Password minimal 6 karakter"
            binding.etPassword.requestFocus()
            return false
        }
        
        if (phone.isEmpty()) {
            binding.etPhone.error = "Nomor telepon harus diisi"
            binding.etPhone.requestFocus()
            return false
        }
        
        if (address.isEmpty()) {
            binding.etAddress.error = "Alamat harus diisi"
            binding.etAddress.requestFocus()
            return false
        }
        
        return true
    }
}
