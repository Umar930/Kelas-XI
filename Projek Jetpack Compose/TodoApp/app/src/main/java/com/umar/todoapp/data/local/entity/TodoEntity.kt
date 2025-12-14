package com.umar.todoapp.data.local.entity

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Hapus anotasi Room
data class TodoEntity(
    val id: Int? = null,
    val title: String,
    val createdAt: Long
) {
    fun getFormattedDate(): String {
        val date = Date(createdAt)
        val formatter = SimpleDateFormat("HH:mm a dd/MM/yyyy", Locale.getDefault())
        return formatter.format(date)
    }
}