package com.umar.todoapp.model

import java.time.Instant
import java.util.Date

data class Todo(
    val id: Int,
    val title: String,
    val createdAt: Date
)