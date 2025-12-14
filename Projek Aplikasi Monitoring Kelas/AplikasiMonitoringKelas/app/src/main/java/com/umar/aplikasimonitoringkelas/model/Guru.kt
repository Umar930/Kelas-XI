package com.umar.aplikasimonitoringkelas.model

import com.google.gson.annotations.SerializedName

/**
 * Data class untuk merepresentasikan Guru
 */
data class Guru(
    @SerializedName("id")
    val id: Int? = null,
    
    @SerializedName("kode_guru")
    val kodeGuru: String? = null,
    
    @SerializedName("nama")
    val nama: String? = null,
    
    @SerializedName("email")
    val email: String? = null,
    
    @SerializedName("no_telepon")
    val noTelepon: String? = null,
    
    @SerializedName("mata_pelajaran")
    val mataPelajaran: String? = null,
    
    @SerializedName("alamat")
    val alamat: String? = null,
    
    @SerializedName("created_at")
    val createdAt: String? = null,
    
    @SerializedName("updated_at")
    val updatedAt: String? = null
)

/**
 * Response wrapper untuk list guru dari API
 */
data class GuruListResponse(
    @SerializedName("success")
    val success: Boolean = false,
    
    @SerializedName("message")
    val message: String? = null,
    
    @SerializedName("data")
    val data: List<Guru>? = null
)

/**
 * Response wrapper untuk single guru dari API
 */
data class GuruResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("data")
    val data: Guru?
)

/**
 * Request untuk create/update guru
 */
data class GuruRequest(
    @SerializedName("kode_guru")
    val kodeGuru: String,
    
    @SerializedName("nama")
    val nama: String,
    
    @SerializedName("email")
    val email: String? = null,
    
    @SerializedName("no_telepon")
    val noTelepon: String? = null,
    
    @SerializedName("mata_pelajaran")
    val mataPelajaran: String? = null,
    
    @SerializedName("alamat")
    val alamat: String? = null
)
