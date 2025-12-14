package com.umar.aplikasimonitoringkelas.data.model

import com.google.gson.annotations.SerializedName

/**
 * Response dari endpoint login
 */
data class LoginResponse(
    @SerializedName("success")
    val success: Boolean = false,
    
    @SerializedName("message")
    val message: String = "",
    
    @SerializedName("token")
    val token: String? = null,
    
    @SerializedName("user")
    val user: UserData? = null,
    
    // Untuk kompatibilitas dengan berbagai format Laravel
    @SerializedName("data")
    val data: LoginData? = null
)

/**
 * Data wrapper alternatif (jika API Laravel menggunakan struktur data)
 */
data class LoginData(
    @SerializedName("token")
    val token: String?,
    
    @SerializedName("token_type")
    val tokenType: String? = "Bearer",
    
    @SerializedName("user")
    val user: UserData?
)

/**
 * Data user yang dikembalikan dari login
 */
data class UserData(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("email")
    val email: String = "",
    
    @SerializedName("role")
    val role: String = "",
    
    @SerializedName("name")
    val name: String? = null,
    
    // Field tambahan yang mungkin ada di Laravel
    @SerializedName("username")
    val username: String? = null,
    
    @SerializedName("siswa_id")
    val siswaId: Int? = null,
    
    @SerializedName("guru_id")
    val guruId: Int? = null
)
