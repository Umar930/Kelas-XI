package com.umar.warunggo.ui.expense

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Expense List Screen (Placeholder)
 */
@Composable
fun ExpenseListScreen(
    onNavigateToForm: (Int?) -> Unit,
    onNavigateBack: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Expense List - Coming soon")
    }
}

/**
 * Expense Form Screen (Placeholder)
 */
@Composable
fun ExpenseFormScreen(
    expenseId: Int?,
    onNavigateBack: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Expense Form - Coming soon")
    }
}
