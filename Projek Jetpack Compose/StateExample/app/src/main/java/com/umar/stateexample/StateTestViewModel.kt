package com.umar.stateexample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel untuk manajemen state pada aplikasi.
 * 
 * ViewModel digunakan sebagai "single source of truth" untuk menyimpan state yang tahan terhadap:
 * - Recomposition pada UI Jetpack Compose
 * - Perubahan konfigurasi (seperti rotasi layar)
 * - Lifecycle events lainnya
 * 
 * Keuntungan menggunakan ViewModel:
 * 1. State tetap bertahan selama siklus hidup Activity/Fragment
 * 2. Memisahkan logic bisnis dari UI
 * 3. Mudah di-test karena terpisah dari komponen UI
 */
class StateTestViewModel : ViewModel() {
    
    // Backing property untuk nama (private, hanya bisa diubah di dalam ViewModel)
    private val _name = MutableLiveData<String>("")
    
    // Public property yang hanya bisa dibaca dari luar
    // Ini penting untuk menerapkan prinsip "Unidirectional Data Flow"
    val name: LiveData<String> = _name
    
    // Backing property untuk nama belakang
    private val _surname = MutableLiveData<String>("")
    
    // Public property untuk nama belakang
    val surname: LiveData<String> = _surname
    
    /**
     * Fungsi untuk memperbarui nama depan.
     * Ini adalah event handler yang akan dipanggil oleh UI saat nilai input berubah.
     */
    fun onNameUpdate(newName: String) {
        _name.value = newName
    }
    
    /**
     * Fungsi untuk memperbarui nama belakang.
     * Ini adalah event handler yang akan dipanggil oleh UI saat nilai input berubah.
     */
    fun onSurnameUpdate(newSurname: String) {
        _surname.value = newSurname
    }
}