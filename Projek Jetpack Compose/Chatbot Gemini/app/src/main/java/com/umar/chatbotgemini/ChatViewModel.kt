package com.umar.chatbotgemini

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    // Observable message list for Compose
    val messageList: SnapshotStateList<MessageModel> = mutableStateListOf()

    // NOTE: Replace the below with real Gemini SDK initialization.
    // For example (pseudo):
    // private val client = GenerativeAIClient.builder().setApiKey(Constants.API_KEY).build()
    // private val generativeModel = client.getModel(Constants.MODEL_NAME)
    // private val chat = generativeModel.startChat()

    fun sendMessage(question: String) {
        val trimmed = question.trim()
        if (trimmed.isEmpty()) return

        // Add user message
        messageList.add(MessageModel(message = trimmed, role = "user"))

        // Add typing indicator
        val typing = MessageModel(message = "typing...", role = "model")
        messageList.add(typing)

        viewModelScope.launch {
            try {
                // Build history mapping here by mapping messageList to the SDK 'Content' type.
                // Example (pseudo-code):
                // val history: List<Content> = messageList.map { msg ->
                //     if (msg.role == "user") Content.ofUserText(msg.message) else Content.ofModelText(msg.message)
                // }
                // val response = chat.sendMessage(trimmed, history = history)
                // val botText = response.text

                // Since SDK wiring depends on the exact library version, provide a safe fallback
                // Remove typing and add a simulated response after a short delay to emulate network call.
                delay(900)

                // Simulated response — echo + short transformation (developer should replace this)
                val botText = "Echo: $trimmed"

                // remove the typing indicator (last occurrence)
                val idx = messageList.indexOfLast { it.message == "typing..." && it.role == "model" }
                if (idx >= 0) messageList.removeAt(idx)

                // add the bot reply
                messageList.add(MessageModel(message = botText, role = "model"))

            } catch (e: Exception) {
                // remove typing indicator
                val idx = messageList.indexOfLast { it.message == "typing..." && it.role == "model" }
                if (idx >= 0) messageList.removeAt(idx)

                // add error message
                messageList.add(
                    MessageModel(
                        message = "Failed to get response: ${e.localizedMessage}",
                        role = "model"
                    )
                )
            }
        }
    }
}
