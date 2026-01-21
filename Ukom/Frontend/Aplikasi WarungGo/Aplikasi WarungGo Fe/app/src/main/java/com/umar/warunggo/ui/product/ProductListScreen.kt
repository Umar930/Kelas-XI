package com.umar.warunggo.ui.product

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.umar.warunggo.ui.components.*

/**
 * Product List Screen
 * Displays inventory with search, filter, sort, and CRUD operations
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onNavigateToAddProduct: () -> Unit,
    onNavigateToEditProduct: (String) -> Unit,
    viewModel: ProductViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            ProductListTopBar(
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = viewModel::onSearchQueryChange,
                onClearSearch = viewModel::clearSearch,
                onFilterClick = viewModel::showFilterSheet
            )
        },
        floatingActionButton = {
            AddProductFab(onClick = onNavigateToAddProduct)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingState()
                }
                uiState.filteredProducts.isEmpty() -> {
                    EmptyProductState(onAddProduct = onNavigateToAddProduct)
                }
                else -> {
                    ProductList(
                        products = uiState.filteredProducts,
                        onEditClick = onNavigateToEditProduct,
                        onDeleteClick = viewModel::showDeleteDialog,
                        onQuickAddStock = viewModel::quickAddStock
                    )
                }
            }
        }
        
        // Filter Bottom Sheet
        if (uiState.showFilterSheet) {
            FilterBottomSheet(
                selectedCategory = uiState.selectedCategory,
                selectedStockStatus = uiState.selectedStockStatus,
                selectedSortOption = uiState.sortOption,
                onCategorySelected = viewModel::filterByCategory,
                onStockStatusSelected = viewModel::filterByStockStatus,
                onSortOptionSelected = viewModel::changeSortOption,
                onDismiss = viewModel::hideFilterSheet,
                onApply = viewModel::hideFilterSheet
            )
        }
        
        // Delete Confirmation Dialog
        if (uiState.showDeleteDialog && uiState.productToDelete != null) {
            DeleteConfirmationDialog(
                productName = uiState.productToDelete!!.name,
                onConfirm = viewModel::deleteProduct,
                onDismiss = viewModel::hideDeleteDialog
            )
        }
    }
}

/**
 * Top bar with search and filter
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductListTopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onFilterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Title and Filter Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Produk",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            IconButton(onClick = onFilterClick) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        // Search Bar
        SearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            onClear = onClearSearch,
            placeholder = "Cari produk..."
        )
    }
}

/**
 * Product list with cards
 */
@Composable
private fun ProductList(
    products: List<com.umar.warunggo.data.model.Product>,
    onEditClick: (String) -> Unit,
    onDeleteClick: (com.umar.warunggo.data.model.Product) -> Unit,
    onQuickAddStock: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = products,
            key = { it.id }
        ) { product ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                ProductCard(
                    product = product,
                    onEditClick = { onEditClick(product.id) },
                    onDeleteClick = { onDeleteClick(product) },
                    onQuickAddStock = { onQuickAddStock(product.id) }
                )
            }
        }
    }
}

/**
 * Empty state when no products exist
 */
@Composable
private fun EmptyProductState(onAddProduct: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        EmptyState(
            title = "Belum Ada Produk",
            message = "Mulai kelola inventory Anda dengan menambahkan produk pertama",
            actionText = "Tambah Produk",
            onActionClick = onAddProduct
        )
    }
}

/**
 * Loading state indicator
 */
@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

/**
 * Floating action button for adding product
 */
@Composable
private fun AddProductFab(onClick: () -> Unit) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        label = "fab_scale"
    )
    
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.scale(scale),
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Tambah Produk"
        )
    }
}

/**
 * Delete confirmation dialog
 */
@Composable
private fun DeleteConfirmationDialog(
    productName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Hapus Produk?") },
        text = { Text("Apakah Anda yakin ingin menghapus \"$productName\"? Tindakan ini tidak dapat dibatalkan.") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Hapus")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
