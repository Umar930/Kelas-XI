package com.umar.warunggo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.umar.warunggo.data.model.CartItem
import com.umar.warunggo.data.model.PaymentMethod
import com.umar.warunggo.data.model.Product
import com.umar.warunggo.data.model.Transaction
import com.umar.warunggo.ui.theme.WarungGoTheme

/**
 * Receipt dialog component
 * Shows transaction success with receipt details
 */
@Composable
fun ReceiptDialog(
    transaction: Transaction,
    storeName: String,
    onDismiss: () -> Unit,
    onPrint: () -> Unit = {},
    onShare: () -> Unit = {},
    onNewTransaction: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                ReceiptHeader(
                    storeName = storeName,
                    onClose = onDismiss
                )
                
                // Receipt content (scrollable)
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(24.dp)
                ) {
                    // Success icon
                    item {
                        SuccessIcon()
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    
                    // Transaction info
                    item {
                        TransactionInfo(transaction = transaction)
                        Spacer(modifier = Modifier.height(24.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    
                    // Items header
                    item {
                        Text(
                            text = "Detail Pembelian",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    
                    // Items list
                    items(transaction.items) { cartItem ->
                        ReceiptItemRow(cartItem = cartItem)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    // Summary
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(16.dp))
                        PaymentSummarySection(transaction = transaction)
                    }
                }
                
                // Action buttons
                ReceiptActions(
                    onPrint = onPrint,
                    onShare = onShare,
                    onNewTransaction = {
                        onNewTransaction()
                        onDismiss()
                    }
                )
            }
        }
    }
}

/**
 * Receipt header
 */
@Composable
private fun ReceiptHeader(
    storeName: String,
    onClose: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = storeName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Struk Pembayaran",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
            
            IconButton(
                onClick = onClose,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Tutup",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

/**
 * Success icon
 */
@Composable
private fun SuccessIcon() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(80.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "✓",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * Transaction info
 */
@Composable
private fun TransactionInfo(transaction: Transaction) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Transaksi Berhasil",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = transaction.id,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        
        Text(
            text = transaction.getFormattedDate(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

/**
 * Receipt item row
 */
@Composable
private fun ReceiptItemRow(cartItem: CartItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = cartItem.product.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${cartItem.quantity} x ${cartItem.product.formattedPrice}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        
        Text(
            text = cartItem.getFormattedSubtotal(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/**
 * Payment summary section
 */
@Composable
private fun PaymentSummarySection(transaction: Transaction) {
    Column {
        SummaryRow(label = "Subtotal", value = transaction.getFormattedSubtotal())
        
        if (transaction.discount > 0) {
            Spacer(modifier = Modifier.height(8.dp))
            SummaryRow(
                label = "Diskon",
                value = "- ${transaction.getFormattedDiscount()}",
                valueColor = MaterialTheme.colorScheme.error
            )
        }
        
        if (transaction.tax > 0) {
            Spacer(modifier = Modifier.height(8.dp))
            SummaryRow(label = "Pajak", value = transaction.getFormattedTax())
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        Divider()
        Spacer(modifier = Modifier.height(12.dp))
        
        SummaryRow(
            label = "Total",
            value = transaction.getFormattedTotal(),
            labelStyle = MaterialTheme.typography.titleLarge,
            valueStyle = MaterialTheme.typography.titleLarge
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        SummaryRow(
            label = "Metode Pembayaran",
            value = transaction.paymentMethod.displayName
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        SummaryRow(
            label = "Dibayar",
            value = transaction.getFormattedPaidAmount()
        )
        
        if (transaction.change > 0) {
            Spacer(modifier = Modifier.height(8.dp))
            SummaryRow(
                label = "Kembalian",
                value = transaction.getFormattedChange(),
                valueColor = MaterialTheme.colorScheme.primary,
                valueStyle = MaterialTheme.typography.titleMedium
            )
        }
    }
}

/**
 * Summary row
 */
@Composable
private fun SummaryRow(
    label: String,
    value: String,
    labelStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium,
    valueStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = labelStyle,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = valueStyle,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}

/**
 * Receipt actions
 */
@Composable
private fun ReceiptActions(
    onPrint: () -> Unit,
    onShare: () -> Unit,
    onNewTransaction: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Print button
            OutlinedButton(
                onClick = onPrint,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Print,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cetak")
            }
            
            // Share button
            OutlinedButton(
                onClick = onShare,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Bagikan")
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // New transaction button
        Button(
            onClick = onNewTransaction,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Transaksi Baru",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

// ============================================
// PREVIEW
// ============================================

@Preview(showBackground = true)
@Composable
private fun ReceiptDialogPreview() {
    WarungGoTheme {
        ReceiptDialog(
            transaction = Transaction(
                id = "TRX20250114001",
                transactionDate = System.currentTimeMillis(),
                items = listOf(
                    CartItem(
                        product = Product(
                            id = "P001",
                            name = "Indomie Goreng",
                            price = 3500.0,
                            stock = 50,
                            category = com.umar.warunggo.data.model.ProductCategory.MAKANAN
                        ),
                        quantity = 2
                    ),
                    CartItem(
                        product = Product(
                            id = "P002",
                            name = "Aqua 600ml",
                            price = 3000.0,
                            stock = 100,
                            category = com.umar.warunggo.data.model.ProductCategory.MINUMAN
                        ),
                        quantity = 3
                    )
                ),
                subtotal = 16000.0,
                discount = 1000.0,
                tax = 0.0,
                total = 15000.0,
                paymentMethod = PaymentMethod.CASH,
                paidAmount = 20000.0,
                change = 5000.0
            ),
            storeName = "Warung Berkah Jaya",
            onDismiss = {},
            onNewTransaction = {}
        )
    }
}
