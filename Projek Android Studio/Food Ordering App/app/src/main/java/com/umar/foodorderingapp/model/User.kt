package com.umar.foodorderingapp.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val isAdmin: Boolean = false
)
