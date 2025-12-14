package com.umar.chatbotgemini

data class MessageModel(
    val message: String,
    val role: String // "user" or "model"
)
