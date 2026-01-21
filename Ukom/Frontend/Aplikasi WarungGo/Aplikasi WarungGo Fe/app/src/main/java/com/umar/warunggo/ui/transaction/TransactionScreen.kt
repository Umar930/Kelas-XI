package com.umar.warunggo.ui.transaction

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.umar.warunggo.data.model.PaymentMethod
import com.umar.warunggo.data.model.Product
import com.umar.warunggo.ui.components.CartItemRow
import com.umar.warunggo.ui.components.EmptyCartState
import com.umar.warunggo.ui.components.ReceiptDialog
import com.umar.warunggo.ui.theme.WarungGoTheme

/**
 * Main Transaction Screen
 * Complete POS transaction interface with cart, payment, and checkout
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    viewModel: TransactionViewModel = viewModel(),
    onNavigateToHistory: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Show error message
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }
    
    Scaffold(
        topBar = {
            TransactionTopBar(
                storeName = uiState.storeName,
                onHistoryClick = onNavigateToHistory,
                onSettingsClick = onNavigateToSettings
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (!uiState.isCartEmpty()) {
                PaymentBottomPanel(
                    uiState = uiState,
                    onSelectPaymentMethod = { viewModel.showPaymentMethodDialog() },
                    onPaidAmountChange = { viewModel.updatePaidAmount(it) },
                    onSetExactAmount = { viewModel.setExactAmount() },
                    onSubmit = { viewModel.submitTransaction() }
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = if (!uiState.isCartEmpty()) 200.dp else 0.dp)
        ) {
            // Search section
            item {
                SearchSection(
                    searchQuery = uiState.searchQuery,
                    onSearchChange = { viewModel.updateSearchQuery(it) },
                    isSearching = uiState.isSearching
                )
            }
            
            // Quick actions
            item {
                QuickActionsRow(
                    onFavoriteClick = { /* Filter favorites */ },
                    onBundleClick = { /* Show bundles */ }
                )
            }
            
            // Cart items section (if cart has items)
            if (!uiState.isCartEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Keranjang (${uiState.cartItems.size} item)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                
                items(uiState.cartItems, key = { it.product.id }) { cartItem ->
                    CartItemRow(
                        cartItem = cartItem,
                        onIncreaseQuantity = { viewModel.increaseQuantity(cartItem.product.id) },
                        onDecreaseQuantity = { viewModel.decreaseQuantity(cartItem.product.id) },
                        onRemove = { viewModel.removeFromCart(cartItem.product.id) }
                    )
                }
                
                item {
                    Divider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                }
            }
            
            // Product list header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (uiState.searchQuery.isNotBlank()) "Hasil Pencarian" else "Semua Produk",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Product list
            if (uiState.filteredProducts.isEmpty()) {
                item {
                    EmptyCartState(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                items(uiState.filteredProducts, key = { it.id }) { product ->
                    ProductSearchItem(
                        product = product,
                        onClick = { viewModel.addToCart(product) }
                    )
                }
            }
        }
    }
    
    // Payment method dialog
    if (uiState.showPaymentMethodDialog) {
        PaymentMethodDialog(
            selectedMethod = uiState.selectedPaymentMethod,
            onMethodSelected = { 
                viewModel.selectPaymentMethod(it)
                viewModel.hidePaymentMethodDialog()
            },
            onDismiss = { viewModel.hidePaymentMethodDialog() }
        )
    }
    
    // Receipt dialog
    if (uiState.showReceiptDialog && uiState.completedTransaction != null) {
        ReceiptDialog(
            transaction = uiState.completedTransaction!!,
            storeName = uiState.storeName,
            onDismiss = { viewModel.hideReceiptDialog() },
            onPrint = { /* TODO: Print receipt */ },
            onShare = { /* TODO: Share receipt */ },
            onNewTransaction = { viewModel.resetTransaction() }
        )
    }
}

/**
 * Top app bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionTopBar(
    storeName: String,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Transaksi",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = storeName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        },
        actions = {
            IconButton(onClick = onHistoryClick) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Riwayat"
                )
            }
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Pengaturan"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

/**
 * Search section
 */
@Composable
private fun SearchSection(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    isSearching: Boolean
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                placeholder = {
                    Text("Cari produk atau scan barcode...")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Cari"
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Hapus"
                            )
                        }
                    } else {
                        IconButton(onClick = { /* TODO: Open barcode scanner */ }) {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = "Scan"
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                    }
                ),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            
            // Searching indicator
            AnimatedVisibility(visible = isSearching) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Mencari...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/**
 * Quick actions row
 */
@Composable
private fun QuickActionsRow(
    onFavoriteClick: () -> Unit,
    onBundleClick: () -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            QuickActionChip(
                icon = Icons.Default.Star,
                label = "Favorit",
                onClick = onFavoriteClick
            )
        }
        
        item {
            QuickActionChip(
                icon = Icons.Default.LocalOffer,
                label = "Paket",
                onClick = onBundleClick
            )
        }
    }
}

/**
 * Quick action chip
 */
@Composable
private fun QuickActionChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    FilterChip(
        selected = false,
        onClick = onClick,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }
    )
}

/**
 * Product search results
 */
@Composable
private fun ProductSearchResults(
    products: List<Product>,
    onProductClick: (Product) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(products, key = { it.id }) { product ->
            ProductSearchItem(
                product = product,
                onClick = { onProductClick(product) }
            )
        }
    }
}

/**
 * Product search item
 */
@Composable
private fun ProductSearchItem(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product icon
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = product.name.first().toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Product info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = product.category.displayName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = product.formattedPrice,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            // Add icon
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Tambah",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

/**
 * Cart list
 */
@Composable
private fun CartList(
    cartItems: List<com.umar.warunggo.data.model.CartItem>,
    onIncreaseQuantity: (String) -> Unit,
    onDecreaseQuantity: (String) -> Unit,
    onRemove: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
    ) {
        items(cartItems, key = { it.product.id }) { cartItem ->
            CartItemRow(
                cartItem = cartItem,
                onIncreaseQuantity = { onIncreaseQuantity(cartItem.product.id) },
                onDecreaseQuantity = { onDecreaseQuantity(cartItem.product.id) },
                onRemove = { onRemove(cartItem.product.id) }
            )
        }
        
        // Bottom padding for payment panel
        item {
            Spacer(modifier = Modifier.height(200.dp))
        }
    }
}

/**
 * Payment bottom panel
 */
@Composable
private fun PaymentBottomPanel(
    uiState: TransactionUiState,
    onSelectPaymentMethod: () -> Unit,
    onPaidAmountChange: (String) -> Unit,
    onSetExactAmount: () -> Unit,
    onSubmit: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Summary section
            PaymentSummarySection(uiState = uiState)
            
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))
            
            // Payment method
            PaymentMethodSection(
                selectedMethod = uiState.selectedPaymentMethod,
                onSelectMethod = onSelectPaymentMethod
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Paid amount input
            PaidAmountSection(
                paidAmount = uiState.paidAmount,
                total = uiState.total,
                change = uiState.change,
                selectedMethod = uiState.selectedPaymentMethod,
                onPaidAmountChange = onPaidAmountChange,
                onSetExactAmount = onSetExactAmount
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Submit button
            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = uiState.canSubmitTransaction(),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (uiState.isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Bayar Sekarang",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Payment summary section
 */
@Composable
private fun PaymentSummarySection(uiState: TransactionUiState) {
    Column {
        SummaryRow(
            label = "Subtotal (${uiState.getTotalItemCount()} item)",
            value = uiState.getFormattedSubtotal()
        )
        
        if (uiState.discount > 0) {
            Spacer(modifier = Modifier.height(4.dp))
            SummaryRow(
                label = "Diskon",
                value = "- ${uiState.getFormattedDiscount()}",
                valueColor = MaterialTheme.colorScheme.error
            )
        }
        
        if (uiState.tax > 0) {
            Spacer(modifier = Modifier.height(4.dp))
            SummaryRow(
                label = "Pajak",
                value = uiState.getFormattedTax()
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        SummaryRow(
            label = "Total",
            value = uiState.getFormattedTotal(),
            labelStyle = MaterialTheme.typography.titleLarge,
            valueStyle = MaterialTheme.typography.titleLarge
        )
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
 * Payment method section
 */
@Composable
private fun PaymentMethodSection(
    selectedMethod: PaymentMethod,
    onSelectMethod: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectMethod() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Metode Pembayaran",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = selectedMethod.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Pilih metode"
            )
        }
    }
}

/**
 * Paid amount section
 */
@Composable
private fun PaidAmountSection(
    paidAmount: String,
    total: Double,
    change: Double,
    selectedMethod: PaymentMethod,
    onPaidAmountChange: (String) -> Unit,
    onSetExactAmount: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = paidAmount,
                onValueChange = onPaidAmountChange,
                modifier = Modifier.weight(1f),
                label = { Text("Dibayar") },
                leadingIcon = { Text("Rp") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                enabled = selectedMethod != PaymentMethod.CREDIT
            )
            
            if (selectedMethod == PaymentMethod.CASH) {
                OutlinedButton(
                    onClick = onSetExactAmount,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text("Pas")
                }
            }
        }
        
        // Change display
        if (change > 0 && selectedMethod == PaymentMethod.CASH) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Kembalian",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Rp ${"%,d".format(change.toLong()).replace(',', '.')}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        
        // Payment validation
        if (selectedMethod != PaymentMethod.CREDIT) {
            val paid = paidAmount.toDoubleOrNull() ?: 0.0
            if (paid > 0 && paid < total) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Pembayaran kurang dari total",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

/**
 * Payment method dialog
 */
@Composable
private fun PaymentMethodDialog(
    selectedMethod: PaymentMethod,
    onMethodSelected: (PaymentMethod) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Pilih Metode Pembayaran",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column {
                PaymentMethod.values().forEach { method ->
                    PaymentMethodOption(
                        method = method,
                        isSelected = method == selectedMethod,
                        onClick = { onMethodSelected(method) }
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

/**
 * Payment method option
 */
@Composable
private fun PaymentMethodOption(
    method: PaymentMethod,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        color = if (isSelected) 
            MaterialTheme.colorScheme.primaryContainer 
        else 
            Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = method.displayName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
            
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Dipilih",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// ============================================
// PREVIEW
// ============================================

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TransactionScreenPreview() {
    WarungGoTheme {
        TransactionScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TransactionScreenDarkPreview() {
    WarungGoTheme {
        TransactionScreen()
    }
}
