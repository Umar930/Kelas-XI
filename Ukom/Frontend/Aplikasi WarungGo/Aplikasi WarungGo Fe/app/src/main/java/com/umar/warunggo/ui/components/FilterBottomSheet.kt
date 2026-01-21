package com.umar.warunggo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umar.warunggo.data.model.ProductCategory
import com.umar.warunggo.data.model.StockStatus
import com.umar.warunggo.ui.product.SortOption

/**
 * Filter and sort bottom sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    selectedCategory: ProductCategory?,
    selectedStockStatus: StockStatus?,
    selectedSortOption: SortOption,
    onCategorySelected: (ProductCategory?) -> Unit,
    onStockStatusSelected: (StockStatus?) -> Unit,
    onSortOptionSelected: (SortOption) -> Unit,
    onDismiss: () -> Unit,
    onApply: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Title
            Text(
                text = "Filter & Urutkan",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Category Filter Section
            Text(
                text = "Kategori",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            FilterChipGroup(
                options = listOf(null) + ProductCategory.entries,
                selectedOption = selectedCategory,
                onOptionSelected = onCategorySelected,
                optionLabel = { it?.displayName ?: "Semua" }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Stock Status Filter Section
            Text(
                text = "Status Stok",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            FilterChipGroup(
                options = listOf(null, StockStatus.AVAILABLE, StockStatus.LOW_STOCK, StockStatus.OUT_OF_STOCK),
                selectedOption = selectedStockStatus,
                onOptionSelected = onStockStatusSelected,
                optionLabel = { 
                    when (it) {
                        null -> "Semua"
                        StockStatus.AVAILABLE -> "Tersedia"
                        StockStatus.LOW_STOCK -> "Stok Menipis"
                        StockStatus.OUT_OF_STOCK -> "Habis"
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Sort Options Section
            Text(
                text = "Urutkan",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            SortOption.entries.forEach { option ->
                FilterChip(
                    selected = selectedSortOption == option,
                    onClick = { onSortOptionSelected(option) },
                    label = { Text(option.displayName) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Apply Button
            Button(
                onClick = {
                    onApply()
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Terapkan Filter")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Generic filter chip group
 */
@Composable
private fun <T> FilterChipGroup(
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T?) -> Unit,
    optionLabel: (T?) -> String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            FilterChip(
                selected = selectedOption == option,
                onClick = { onOptionSelected(option) },
                label = { Text(optionLabel(option)) }
            )
        }
    }
}
