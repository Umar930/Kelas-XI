package com.umar.warunggo.model

/**
 * Model untuk data produk
 * Menyimpan informasi produk yang dijual di warung
 */
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val stock: Int,
    val category: String = "",
    val description: String = ""
)
