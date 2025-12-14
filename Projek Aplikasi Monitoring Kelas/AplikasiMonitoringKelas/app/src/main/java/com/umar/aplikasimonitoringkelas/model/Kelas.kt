package com.umar.aplikasimonitoringkelas.model

import com.google.gson.annotations.SerializedName

/**
 * Data class untuk merepresentasikan Kelas
 */
data class Kelas(
    @SerializedName("id")
    val id: Int? = null,
    
    @SerializedName("nama_kelas")
    val namaKelas: String,
    
    @SerializedName("tingkat")
    val tingkat: String,
    
    @SerializedName("jurusan")
    val jurusan: String? = null,
    
    @SerializedName("wali_kelas_id")
    val waliKelasId: Int? = null,
    
    @SerializedName("tahun_ajaran_id")
    val tahunAjaranId: Int? = null,
    
    @SerializedName("wali_kelas")
    val waliKelas: Guru? = null,
    
    @SerializedName("tahun_ajaran")
    val tahunAjaran: TahunAjaran? = null,
    
    @SerializedName("created_at")
    val createdAt: String? = null,
    
    @SerializedName("updated_at")
    val updatedAt: String? = null
)

/**
 * Response wrapper untuk list kelas dari API
 */
data class KelasListResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("data")
    val data: List<Kelas>
)

/**
 * Response wrapper untuk single kelas dari API
 */
data class KelasResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("data")
    val data: Kelas?
)

/**
 * Request untuk create/update kelas
 */
data class KelasRequest(
    @SerializedName("nama_kelas")
    val namaKelas: String,
    
    @SerializedName("tingkat")
    val tingkat: String,
    
    @SerializedName("jurusan")
    val jurusan: String? = null,
    
    @SerializedName("wali_kelas_id")
    val waliKelasId: Int? = null,
    
    @SerializedName("tahun_ajaran_id")
    val tahunAjaranId: Int? = null
)
