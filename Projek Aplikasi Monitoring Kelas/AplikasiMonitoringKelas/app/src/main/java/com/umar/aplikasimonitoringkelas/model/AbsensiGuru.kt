package com.umar.aplikasimonitoringkelas.model

import com.google.gson.annotations.SerializedName

/**
 * Data class untuk merepresentasikan Absensi Guru
 */
data class AbsensiGuru(
    @SerializedName("id")
    val id: Int? = null,
    
    @SerializedName("guru_id")
    val guruId: Int,
    
    @SerializedName("tanggal")
    val tanggal: String,
    
    @SerializedName("status")
    val status: String, // hadir, izin, sakit, alpha
    
    @SerializedName("keterangan")
    val keterangan: String? = null,
    
    @SerializedName("waktu_datang")
    val waktuDatang: String? = null,
    
    @SerializedName("waktu_pulang")
    val waktuPulang: String? = null,
    
    @SerializedName("guru")
    val guru: Guru? = null,
    
    @SerializedName("created_at")
    val createdAt: String? = null,
    
    @SerializedName("updated_at")
    val updatedAt: String? = null
)

/**
 * Response wrapper untuk list absensi guru dari API
 */
data class AbsensiGuruListResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("data")
    val data: List<AbsensiGuru>
)

/**
 * Response wrapper untuk single absensi guru dari API
 */
data class AbsensiGuruResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("data")
    val data: AbsensiGuru?
)

/**
 * Request untuk create/update absensi guru
 */
data class AbsensiGuruRequest(
    @SerializedName("guru_id")
    val guruId: Int,
    
    @SerializedName("tanggal")
    val tanggal: String,
    
    @SerializedName("status")
    val status: String, // hadir, izin, sakit, alpha
    
    @SerializedName("keterangan")
    val keterangan: String? = null,
    
    @SerializedName("waktu_datang")
    val waktuDatang: String? = null,
    
    @SerializedName("waktu_pulang")
    val waktuPulang: String? = null
)
