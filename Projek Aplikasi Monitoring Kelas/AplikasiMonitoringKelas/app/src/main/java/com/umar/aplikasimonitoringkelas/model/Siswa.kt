package com.umar.aplikasimonitoringkelas.model

import com.google.gson.annotations.SerializedName

/**
 * Data class untuk merepresentasikan Siswa
 */
data class Siswa(
    @SerializedName("id")
    val id: Int? = null,
    
    @SerializedName("nis")
    val nis: String,
    
    @SerializedName("nisn")
    val nisn: String? = null,
    
    @SerializedName("nama")
    val nama: String,
    
    @SerializedName("jenis_kelamin")
    val jenisKelamin: String,
    
    @SerializedName("tempat_lahir")
    val tempatLahir: String? = null,
    
    @SerializedName("tanggal_lahir")
    val tanggalLahir: String? = null,
    
    @SerializedName("alamat")
    val alamat: String? = null,
    
    @SerializedName("no_telepon")
    val noTelepon: String? = null,
    
    @SerializedName("email")
    val email: String? = null,
    
    @SerializedName("nama_orang_tua")
    val namaOrangTua: String? = null,
    
    @SerializedName("no_telepon_orang_tua")
    val noTeleponOrangTua: String? = null,
    
    @SerializedName("kelas_id")
    val kelasId: Int? = null,
    
    @SerializedName("kelas")
    val kelas: Kelas? = null,
    
    @SerializedName("created_at")
    val createdAt: String? = null,
    
    @SerializedName("updated_at")
    val updatedAt: String? = null
)

/**
 * Response wrapper untuk list siswa dari API
 */
data class SiswaListResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("data")
    val data: List<Siswa>
)

/**
 * Response wrapper untuk single siswa dari API
 */
data class SiswaResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("data")
    val data: Siswa?
)

/**
 * Request untuk create/update siswa
 */
data class SiswaRequest(
    @SerializedName("nis")
    val nis: String,
    
    @SerializedName("nisn")
    val nisn: String? = null,
    
    @SerializedName("nama")
    val nama: String,
    
    @SerializedName("jenis_kelamin")
    val jenisKelamin: String,
    
    @SerializedName("tempat_lahir")
    val tempatLahir: String? = null,
    
    @SerializedName("tanggal_lahir")
    val tanggalLahir: String? = null,
    
    @SerializedName("alamat")
    val alamat: String? = null,
    
    @SerializedName("no_telepon")
    val noTelepon: String? = null,
    
    @SerializedName("email")
    val email: String? = null,
    
    @SerializedName("nama_orang_tua")
    val namaOrangTua: String? = null,
    
    @SerializedName("no_telepon_orang_tua")
    val noTeleponOrangTua: String? = null,
    
    @SerializedName("kelas_id")
    val kelasId: Int? = null
)
