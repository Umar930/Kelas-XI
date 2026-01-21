package com.umar.warunggo.ui.product

import com.umar.warunggo.data.model.ProductCategory

/**
 * Form state for Add/Edit Product screen
 */
data class ProductFormState(
    val id: String? = null,
    val name: String = "",
    val price: String = "",
    val stock: String = "",
    val category: ProductCategory = ProductCategory.LAINNYA,
    val description: String = "",
    val imageUri: String? = null,
    val sku: String = "",
    
    // Validation errors
    val nameError: String? = null,
    val priceError: String? = null,
    val stockError: String? = null,
    
    // UI state
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val showImagePicker: Boolean = false,
    val showCategoryDropdown: Boolean = false
) {
    /**
     * Check if form is valid for submission
     */
    val isValid: Boolean
        get() = name.isNotBlank() &&
                price.isNotBlank() &&
                stock.isNotBlank() &&
                nameError == null &&
                priceError == null &&
                stockError == null
    
    /**
     * Get numeric price value
     */
    val numericPrice: Double?
        get() = price.replace("[^0-9]".toRegex(), "").toDoubleOrNull()
    
    /**
     * Get numeric stock value
     */
    val numericStock: Int?
        get() = stock.replace("[^0-9]".toRegex(), "").toIntOrNull()
}
