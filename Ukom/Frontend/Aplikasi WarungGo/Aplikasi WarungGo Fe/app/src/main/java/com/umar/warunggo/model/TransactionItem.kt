package com.umar.warunggo.model

/**
 * Model untuk item dalam transaksi
 * Menyimpan detail produk dan quantity dalam satu transaksi
 */
data class TransactionItem(
    val id: Int,
    val product: Product,
    val quantity: Int,
    val subtotal: Double
)
