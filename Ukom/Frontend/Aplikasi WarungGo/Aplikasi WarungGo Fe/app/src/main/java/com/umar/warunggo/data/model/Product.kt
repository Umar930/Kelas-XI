package com.umar.warunggo.data.model

/**
 * Product data model representing inventory item
 */
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val stock: Int,
    val category: ProductCategory,
    val description: String = "",
    val imageUri: String? = null,
    val sku: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Get stock status based on current stock level
     */
    val stockStatus: StockStatus
        get() = when {
            stock == 0 -> StockStatus.OUT_OF_STOCK
            stock <= 20 -> StockStatus.LOW_STOCK
            else -> StockStatus.AVAILABLE
        }

    /**
     * Get formatted price string in Rupiah
     */
    val formattedPrice: String
        get() = "Rp ${"%,d".format(price.toLong()).replace(",", ".")}"
}

/**
 * Product category enum
 */
enum class ProductCategory(val displayName: String) {
    MAKANAN("Makanan"),
    MINUMAN("Minuman"),
    SEMBAKO("Sembako"),
    KEBERSIHAN("Kebersihan"),
    LAINNYA("Lainnya")
}

/**
 * Stock status indicator
 */
enum class StockStatus {
    OUT_OF_STOCK,
    LOW_STOCK,
    AVAILABLE
}
