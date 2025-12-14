package com.umar.aplikasimonitoringkelas.data.model

/**
 * Sealed class untuk merepresentasikan state data dari API
 * 
 * @param T Tipe data yang dibungkus
 */
sealed class Resource<out T> {
    /**
     * State saat data sedang dimuat
     */
    object Loading : Resource<Nothing>()
    
    /**
     * State saat data berhasil dimuat
     * 
     * @param data Data yang berhasil dimuat
     */
    data class Success<T>(val data: T) : Resource<T>()
    
    /**
     * State saat terjadi error
     * 
     * @param message Pesan error
     */
    data class Error(val message: String) : Resource<Nothing>()
    
    /**
     * State saat data kosong
     */
    object Empty : Resource<Nothing>()
}
