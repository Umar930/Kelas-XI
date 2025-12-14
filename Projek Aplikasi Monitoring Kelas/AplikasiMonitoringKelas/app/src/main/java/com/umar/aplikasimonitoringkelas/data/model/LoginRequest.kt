package com.umar.aplikasimonitoringkelas.data.model

import com.google.gson.annotations.SerializedName

/**
 * Request body untuk endpoint login
 */
data class LoginRequest(
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String,
    
    @SerializedName("role")
    val role: String
)
