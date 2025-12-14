package com.umar.ecommerceapp.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.umar.ecommerceapp.model.UserModel

class AuthViewModel : ViewModel() {
    
    // Inisialisasi Firebase Auth dan Firestore
    val auth: FirebaseAuth = Firebase.auth
    val db: FirebaseFirestore = Firebase.firestore
    
    /**
     * Fungsi untuk mendaftarkan pengguna baru
     * @param email Email pengguna
     * @param name Nama lengkap pengguna
     * @param password Password pengguna
     * @param onResult Callback dengan hasil (success/failure dan pesan error jika ada)
     */
    fun signUp(
        email: String,
        name: String,
        password: String,
        onResult: (isSuccess: Boolean, errorMessage: String?) -> Unit
    ) {
        // Step 1: Buat akun pengguna di Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    // Jika pendaftaran gagal, kembalikan pesan error
                    onResult(false, task.exception?.localizedMessage)
                } else {
                    // Step 2: Jika berhasil, ambil UID pengguna
                    val userId = task.result.user?.uid ?: ""
                    
                    // Step 3: Buat objek UserModel
                    val userModel = UserModel(
                        uid = userId,
                        name = name,
                        email = email
                    )
                    
                    // Step 4: Simpan data pengguna ke Firestore
                    db.collection("users")
                        .document(userId)
                        .set(userModel)
                        .addOnCompleteListener { firestoreTask ->
                            if (firestoreTask.isSuccessful) {
                                // Berhasil menyimpan data
                                onResult(true, null)
                            } else {
                                // Gagal menyimpan data
                                onResult(false, "Something went wrong during data storage.")
                            }
                        }
                }
            }
    }
    
    /**
     * Fungsi untuk login pengguna yang sudah terdaftar
     * @param email Email pengguna
     * @param password Password pengguna
     * @param onResult Callback dengan hasil (success/failure dan pesan error jika ada)
     */
    fun login(
        email: String,
        password: String,
        onResult: (isSuccess: Boolean, errorMessage: String?) -> Unit
    ) {
        // Otentikasi dengan Firebase
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login berhasil
                    onResult(true, null)
                } else {
                    // Login gagal, kembalikan pesan error
                    onResult(false, task.exception?.localizedMessage)
                }
            }
    }
}
