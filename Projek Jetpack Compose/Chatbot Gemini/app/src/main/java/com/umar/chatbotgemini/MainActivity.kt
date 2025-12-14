package com.umar.chatbotgemini

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
// removed unused activity result imports
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.umar.chatbotgemini.ui.theme.ChatbotGeminiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatbotGeminiTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val vm: ChatViewModel = viewModel()
                    ChatScreen(viewModel = vm)
                }
            }
        }
    }
}

@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    val messages = viewModel.messageList
    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader()

        if (messages.isEmpty()) {
            EmptyState(modifier = Modifier.weight(1f))
        } else {
            MessageList(
                modifier = Modifier.weight(1f),
                messageList = messages
            )
        }

        MessageInput(onMessageSent = { text -> viewModel.sendMessage(text) })
    }
}

@Composable
fun AppHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Easy Bot", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Ask me anything", style = MaterialTheme.typography.headlineSmall)
    }
}

@Composable
fun MessageList(modifier: Modifier = Modifier, messageList: List<MessageModel>) {
    val reversed = remember(messageList) { messageList.asReversed() }
    LazyColumn(modifier = modifier.padding(8.dp), reverseLayout = true) {
        items(reversed, key = { it.hashCode() }) { msg ->
            MessageRow(messageModel = msg)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun MessageRow(messageModel: MessageModel) {
    // use full Alignment (both axes) for Box.align — CenterEnd/CenterStart provide both axes
    val alignment = if (messageModel.role == "user") Alignment.CenterEnd else Alignment.CenterStart
    val background = if (messageModel.role == "user") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val contentColor = if (messageModel.role == "user") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .align(alignment)
                .clip(RoundedCornerShape(12.dp))
                .background(background)
                .padding(12.dp)
                .widthIn(max = 320.dp)
        ) {
            SelectionContainer {
                Text(text = messageModel.message, color = contentColor)
            }
        }
    }
}

@Composable
fun MessageInput(onMessageSent: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text(text = "Type a message...") }
        )
        IconButton(onClick = {
            val trimmed = text.trim()
            if (trimmed.isNotEmpty()) {
                onMessageSent(trimmed)
                text = ""
            }
        }) {
            Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatPreview() {
    ChatbotGeminiTheme {
        EmptyState()
    }
}