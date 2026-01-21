package com.umar.warunggo.model

import java.util.Date

/**
 * Model untuk transaksi penjualan
 * Menyimpan header transaksi dan list item produk yang dibeli
 */
data class Transaction(
    val id: Int,
    val date: Date,
    val items: List<TransactionItem>,
    val total: Double,
    val customerName: String = "",
    val paymentMethod: String = "Cash"
)
