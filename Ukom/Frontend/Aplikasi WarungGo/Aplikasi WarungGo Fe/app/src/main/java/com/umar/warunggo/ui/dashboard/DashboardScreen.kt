package com.umar.warunggo.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Dashboard Screen - Redesigned
 * Fokus: Produktivitas, efisiensi ruang, visual hierarchy yang jelas
 * Layout: Compact header, grid summary, quick actions, mini insights
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    // Dummy data
    val summary = remember { getDummyDashboardSummary() }
    val recentTransactions = remember { getDummyRecentTransactions() }
    val lowStockItems = remember { getDummyLowStockItems() }
    
    Scaffold(
        topBar = { DashboardTopBar() }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Section
            item {
                SummarySection(summary = summary)
            }
            
            // Quick Action Section
            item {
                QuickActionSection(
                    onAddTransaction = { /* Navigate to transaction */ },
                    onAddProduct = { /* Navigate to product */ },
                    onAddExpense = { /* Navigate to expense */ }
                )
            }
            
            // Insights Section
            item {
                InsightsSection(
                    recentTransactions = recentTransactions,
                    lowStockItems = lowStockItems
                )
            }
            
            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

// =====================================================
// HEADER SECTION
// =====================================================

/**
 * Compact TopAppBar dengan tanggal dan action icons
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar() {
    val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
    val currentDate = dateFormat.format(Date())
    
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "WarungGo",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = currentDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }
        },
        actions = {
            IconButton(onClick = { /* Notification */ }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifikasi",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable { /* Profile */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "Profil",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

// =====================================================
// SUMMARY SECTION - Grid 2x2
// =====================================================

@Composable
fun SummarySection(summary: DashboardSummary) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Ringkasan Hari Ini",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        // Grid 2x2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Column 1
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CompactSummaryCard(
                    title = "Penjualan",
                    value = formatCurrency(summary.totalSales),
                    icon = "💰",
                    containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f),
                    contentColor = Color(0xFF2E7D32)
                )
                
                CompactSummaryCard(
                    title = "Laba Bersih",
                    value = formatCurrency(summary.netProfit),
                    icon = "📈",
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            }
            
            // Column 2
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CompactSummaryCard(
                    title = "Pengeluaran",
                    value = formatCurrency(summary.totalExpenses),
                    icon = "💸",
                    containerColor = Color(0xFFF44336).copy(alpha = 0.1f),
                    contentColor = Color(0xFFC62828)
                )
                
                CompactSummaryCard(
                    title = "Transaksi",
                    value = "${summary.transactionCount}x",
                    icon = "🛒",
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun CompactSummaryCard(
    title: String,
    value: String,
    icon: String,
    containerColor: Color,
    contentColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = icon,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// =====================================================
// QUICK ACTION SECTION
// =====================================================

@Composable
fun QuickActionSection(
    onAddTransaction: () -> Unit,
    onAddProduct: () -> Unit,
    onAddExpense: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Aksi Cepat",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                QuickActionButton(
                    label = "Transaksi",
                    icon = "🛒",
                    onClick = onAddTransaction
                )
            }
            item {
                QuickActionButton(
                    label = "Produk",
                    icon = "📦",
                    onClick = onAddProduct
                )
            }
            item {
                QuickActionButton(
                    label = "Pengeluaran",
                    icon = "💸",
                    onClick = onAddExpense
                )
            }
        }
    }
}

@Composable
fun QuickActionButton(
    label: String,
    icon: String,
    onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.width(140.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// =====================================================
// INSIGHTS SECTION
// =====================================================

@Composable
fun InsightsSection(
    recentTransactions: List<RecentTransaction>,
    lowStockItems: List<LowStockItem>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Recent Transactions
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Transaksi Terakhir",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                recentTransactions.take(3).forEach { transaction ->
                    RecentTransactionItem(transaction)
                    if (transaction != recentTransactions.take(3).last()) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
        
        // Low Stock Alert
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF3E0)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⚠️ Stok Menipis",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFE65100)
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                lowStockItems.take(3).forEach { item ->
                    LowStockItemCard(item)
                    if (item != lowStockItems.take(3).last()) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun RecentTransactionItem(transaction: RecentTransaction) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.items,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = transaction.time,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Text(
            text = formatCurrency(transaction.amount),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun LowStockItemCard(item: LowStockItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Stok: ${item.stock}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFD84315)
            )
        }
        
        Text(
            text = "⚠️",
            style = MaterialTheme.typography.titleMedium
        )
    }
}

// =====================================================
// DATA MODELS & DUMMY DATA
// =====================================================

data class DashboardSummary(
    val totalSales: Double,
    val totalExpenses: Double,
    val netProfit: Double,
    val transactionCount: Int
)

data class RecentTransaction(
    val items: String,
    val amount: Double,
    val time: String
)

data class LowStockItem(
    val name: String,
    val stock: Int
)

fun getDummyDashboardSummary() = DashboardSummary(
    totalSales = 2500000.0,
    totalExpenses = 500000.0,
    netProfit = 2000000.0,
    transactionCount = 45
)

fun getDummyRecentTransactions() = listOf(
    RecentTransaction("Indomie Goreng x2, Aqua", 10000.0, "10 menit lalu"),
    RecentTransaction("Teh Pucuk x3", 12000.0, "25 menit lalu"),
    RecentTransaction("Sabun Lifebuoy x2", 10000.0, "1 jam lalu")
)

fun getDummyLowStockItems() = listOf(
    LowStockItem("Indomie Goreng", 5),
    LowStockItem("Aqua 600ml", 8),
    LowStockItem("Teh Pucuk", 3)
)

/**
 * Helper function untuk format currency
 */
fun formatCurrency(value: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return formatter.format(value)
}

// =====================================================
// PREVIEW
// =====================================================

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    MaterialTheme {
        DashboardScreen()
    }
}
