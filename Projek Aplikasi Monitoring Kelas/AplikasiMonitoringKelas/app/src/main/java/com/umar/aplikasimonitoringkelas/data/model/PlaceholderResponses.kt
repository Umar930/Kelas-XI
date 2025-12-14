package com.umar.aplikasimonitoringkelas.data.model

import com.google.gson.annotations.SerializedName
import com.umar.aplikasimonitoringkelas.model.Guru

// ==================== GURU RESPONSES ====================

data class GuruListResponse(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("data")
    val data: List<Guru>? = null
)

data class GuruResponse(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("data")
    val data: Guru? = null
)

// GuruRequest menggunakan yang dari com.umar.aplikasimonitoringkelas.model.GuruRequest
// karena sudah ada di ViewModel

// ==================== OTHER PLACEHOLDER RESPONSES ====================

data class SiswaListResponse(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("data")
    val data: List<com.umar.aplikasimonitoringkelas.model.Siswa>? = null
)

data class SiswaResponse(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("data")
    val data: com.umar.aplikasimonitoringkelas.model.Siswa? = null
)

// SiswaRequest - using model from com.umar.aplikasimonitoringkelas.model if exists

data class KelasListResponse(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("data")
    val data: List<com.umar.aplikasimonitoringkelas.model.Kelas>? = null
)

data class KelasResponse(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("data")
    val data: com.umar.aplikasimonitoringkelas.model.Kelas? = null
)

// KelasRequest - using model from com.umar.aplikasimonitoringkelas.model if exists

data class MapelListResponse(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("data")
    val data: List<com.umar.aplikasimonitoringkelas.model.Mapel>? = null
)

data class MapelResponse(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("data")
    val data: com.umar.aplikasimonitoringkelas.model.Mapel? = null
)

// MapelRequest - using model from com.umar.aplikasimonitoringkelas.model if exists

data class TahunAjaranListResponse(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("data")
    val data: List<com.umar.aplikasimonitoringkelas.model.TahunAjaran>? = null
)

data class TahunAjaranResponse(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("data")
    val data: com.umar.aplikasimonitoringkelas.model.TahunAjaran? = null
)

// TahunAjaranRequest - using model from com.umar.aplikasimonitoringkelas.model if exists

data class AbsensiGuruListResponse(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("data")
    val data: List<com.umar.aplikasimonitoringkelas.model.AbsensiGuru>? = null
)

data class AbsensiGuruResponse(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("data")
    val data: com.umar.aplikasimonitoringkelas.model.AbsensiGuru? = null
)

// AbsensiGuruRequest - using model from com.umar.aplikasimonitoringkelas.model if exists
