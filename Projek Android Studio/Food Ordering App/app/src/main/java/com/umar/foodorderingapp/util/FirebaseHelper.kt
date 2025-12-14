package com.umar.foodorderingapp.util

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuthSettings

/**
 * Helper class untuk inisialisasi Firebase dengan konfigurasi khusus
 */
object FirebaseHelper {
    
    private const val TAG = "FirebaseHelper"
    
    /**
     * Inisialisasi Firebase dengan konfigurasi untuk mendukung autentikasi email dan anonim
     */
    fun initializeFirebase(context: Context) {
        try {
            // Pastikan Firebase terinisialisasi
            FirebaseApp.initializeApp(context)
            
            // Dapatkan instance Firebase Auth
            val auth = FirebaseAuth.getInstance()
            val settings = auth.firebaseAuthSettings
            
            // Aktifkan autentikasi tanpa jaringan (untuk kebutuhan demo)
            settings.setAppVerificationDisabledForTesting(true)
            
            Log.d(TAG, "Firebase berhasil diinisialisasi dengan konfigurasi khusus")
        } catch (e: Exception) {
            Log.e(TAG, "Error inisialisasi Firebase: ${e.message}", e)
        }
    }
    
    /**
     * Periksa apakah aplikasi terhubung ke Firebase Firestore
     */
    fun isFirestoreConnected(callback: (Boolean) -> Unit) {
        try {
            val db = FirebaseFirestore.getInstance()
            val docRef = db.collection("connection_test").document("test")
            
            docRef.set(hashMapOf("connected" to true))
                .addOnSuccessListener {
                    callback(true)
                    docRef.delete() // Hapus dokumen test
                }
                .addOnFailureListener {
                    callback(false)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error memeriksa koneksi Firestore: ${e.message}", e)
            callback(false)
        }
    }
}