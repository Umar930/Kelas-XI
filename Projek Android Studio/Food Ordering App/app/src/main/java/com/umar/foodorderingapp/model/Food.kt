package com.umar.foodorderingapp.model

data class Food(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val category: String = "",
    val rating: Float = 0f,
    val isPopular: Boolean = false,
    val isRecommended: Boolean = false
)
