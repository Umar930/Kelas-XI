package com.umar.warunggo.ui.product

import com.umar.warunggo.data.model.Product
import com.umar.warunggo.data.model.ProductCategory
import com.umar.warunggo.data.model.StockStatus

/**
 * UI state for Product List screen
 */
data class ProductUiState(
    val products: List<Product> = emptyList(),
    val filteredProducts: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: ProductCategory? = null,
    val selectedStockStatus: StockStatus? = null,
    val sortOption: SortOption = SortOption.NAME_ASC,
    val showFilterSheet: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val productToDelete: Product? = null,
    val errorMessage: String? = null
)

/**
 * Sort options for product list
 */
enum class SortOption(val displayName: String) {
    NAME_ASC("Nama A-Z"),
    NAME_DESC("Nama Z-A"),
    PRICE_LOW_HIGH("Harga: Rendah → Tinggi"),
    PRICE_HIGH_LOW("Harga: Tinggi → Rendah"),
    STOCK_HIGH_LOW("Stok: Tinggi → Rendah"),
    STOCK_LOW_HIGH("Stok: Rendah → Tinggi")
}
