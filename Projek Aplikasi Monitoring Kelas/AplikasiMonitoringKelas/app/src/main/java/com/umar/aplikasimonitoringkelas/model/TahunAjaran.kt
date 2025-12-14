package com.umar.aplikasimonitoringkelas.model

import com.google.gson.annotations.SerializedName

/**
 * Data class untuk merepresentasikan Tahun Ajaran
 */
data class TahunAjaran(
    @SerializedName("id")
    val id: Int? = null,
    
    @SerializedName("tahun_ajaran")
    val tahunAjaran: String,
    
    @SerializedName("semester")
    val semester: String,
    
    @SerializedName("is_active")
    val isActive: Boolean = false,
    
    @SerializedName("tanggal_mulai")
    val tanggalMulai: String? = null,
    
    @SerializedName("tanggal_selesai")
    val tanggalSelesai: String? = null,
    
    @SerializedName("created_at")
    val createdAt: String? = null,
    
    @SerializedName("updated_at")
    val updatedAt: String? = null
)

/**
 * Response wrapper untuk list tahun ajaran dari API
 */
data class TahunAjaranListResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("data")
    val data: List<TahunAjaran>
)

/**
 * Response wrapper untuk single tahun ajaran dari API
 */
data class TahunAjaranResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("data")
    val data: TahunAjaran?
)

/**
 * Request untuk create/update tahun ajaran
 */
data class TahunAjaranRequest(
    @SerializedName("tahun_ajaran")
    val tahunAjaran: String,
    
    @SerializedName("semester")
    val semester: String,
    
    @SerializedName("is_active")
    val isActive: Boolean = false,
    
    @SerializedName("tanggal_mulai")
    val tanggalMulai: String? = null,
    
    @SerializedName("tanggal_selesai")
    val tanggalSelesai: String? = null
)
