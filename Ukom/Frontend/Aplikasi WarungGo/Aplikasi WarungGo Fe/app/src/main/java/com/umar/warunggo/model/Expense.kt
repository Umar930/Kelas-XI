package com.umar.warunggo.model

import java.util.Date

/**
 * Model untuk data pengeluaran
 * Menyimpan catatan pengeluaran operasional warung
 */
data class Expense(
    val id: Int,
    val description: String,
    val amount: Double,
    val date: Date,
    val category: String = ""
)
