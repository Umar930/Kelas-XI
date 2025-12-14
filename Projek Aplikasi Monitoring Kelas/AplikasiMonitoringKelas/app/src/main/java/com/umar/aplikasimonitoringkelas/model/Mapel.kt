package com.umar.aplikasimonitoringkelas.model

import com.google.gson.annotations.SerializedName

/**
 * Data class untuk merepresentasikan Mata Pelajaran
 */
data class Mapel(
    @SerializedName("id")
    val id: Int? = null,
    
    @SerializedName("kode_mapel")
    val kodeMapel: String,
    
    @SerializedName("nama_mapel")
    val namaMapel: String,
    
    @SerializedName("deskripsi")
    val deskripsi: String? = null,
    
    @SerializedName("created_at")
    val createdAt: String? = null,
    
    @SerializedName("updated_at")
    val updatedAt: String? = null
)

/**
 * Response wrapper untuk list mapel dari API
 */
data class MapelListResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("data")
    val data: List<Mapel>
)

/**
 * Response wrapper untuk single mapel dari API
 */
data class MapelResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("data")
    val data: Mapel?
)

/**
 * Request untuk create/update mapel
 */
data class MapelRequest(
    @SerializedName("kode_mapel")
    val kodeMapel: String,
    
    @SerializedName("nama_mapel")
    val namaMapel: String,
    
    @SerializedName("deskripsi")
    val deskripsi: String? = null
)
