package com.umar.warunggo.ui.transaction

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Transaction List Screen - Main POS Screen
 */
@Composable
fun TransactionListScreen(
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToCreate: () -> Unit
) {
    // Use the new TransactionScreen composable
    TransactionScreen(
        onNavigateToHistory = { /* TODO: Navigate to history */ },
        onNavigateToSettings = { /* TODO: Navigate to settings */ }
    )
}

/**
 * Transaction Create Screen (Placeholder)
 */
@Composable
fun TransactionCreateScreen(
    onNavigateBack: () -> Unit,
    onTransactionSaved: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Create Transaction - Coming soon")
    }
}

/**
 * Transaction Detail Screen (Placeholder)
 */
@Composable
fun TransactionDetailScreen(
    transactionId: Int,
    onNavigateBack: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Transaction Detail #$transactionId - Coming soon")
    }
}
