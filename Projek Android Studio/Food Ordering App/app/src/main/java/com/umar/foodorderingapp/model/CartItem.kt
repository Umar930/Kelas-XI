package com.umar.foodorderingapp.model

data class CartItem(
    val food: Food,
    var quantity: Int = 1
)
