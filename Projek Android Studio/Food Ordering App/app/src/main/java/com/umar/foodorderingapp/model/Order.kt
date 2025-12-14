package com.umar.foodorderingapp.model

data class Order(
    val id: String = "",
    val userId: String = "",
    val items: List<CartItem> = listOf(),
    val totalAmount: Double = 0.0,
    val address: String = "",
    val status: String = "Pending", // Pending, Processing, OnDelivery, Delivered
    val timestamp: Long = System.currentTimeMillis()
)
