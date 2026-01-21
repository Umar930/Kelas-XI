package com.umar.warunggo.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umar.warunggo.data.model.Product
import com.umar.warunggo.data.model.ProductCategory
import com.umar.warunggo.data.model.StockStatus
import com.umar.warunggo.data.repository.ProductRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * ViewModel for Product management
 * Handles business logic for product list, filtering, sorting, and CRUD operations
 */
class ProductViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()
    
    private val _formState = MutableStateFlow(ProductFormState())
    val formState: StateFlow<ProductFormState> = _formState.asStateFlow()
    
    init {
        loadProducts()
        // Observe product changes from repository
        viewModelScope.launch {
            ProductRepository.products.collect { products ->
                _uiState.update { 
                    it.copy(
                        products = products,
                        isLoading = false
                    )
                }
                applyFiltersAndSort()
            }
        }
    }
    
    /**
     * Load products from repository
     */
    private fun loadProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(300) // Small delay for UX
            
            val products = ProductRepository.getAllProducts()
            _uiState.update { 
                it.copy(
                    products = products,
                    filteredProducts = products,
                    isLoading = false
                )
            }
            applyFiltersAndSort()
        }
    }
    
    /**
     * Search products by name
     */
    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFiltersAndSort()
    }
    
    /**
     * Clear search query
     */
    fun clearSearch() {
        _uiState.update { it.copy(searchQuery = "") }
        applyFiltersAndSort()
    }
    
    /**
     * Show filter bottom sheet
     */
    fun showFilterSheet() {
        _uiState.update { it.copy(showFilterSheet = true) }
    }
    
    /**
     * Hide filter bottom sheet
     */
    fun hideFilterSheet() {
        _uiState.update { it.copy(showFilterSheet = false) }
    }
    
    /**
     * Filter by category
     */
    fun filterByCategory(category: ProductCategory?) {
        _uiState.update { it.copy(selectedCategory = category) }
        applyFiltersAndSort()
    }
    
    /**
     * Filter by stock status
     */
    fun filterByStockStatus(status: StockStatus?) {
        _uiState.update { it.copy(selectedStockStatus = status) }
        applyFiltersAndSort()
    }
    
    /**
     * Change sort option
     */
    fun changeSortOption(option: SortOption) {
        _uiState.update { it.copy(sortOption = option) }
        applyFiltersAndSort()
    }
    
    /**
     * Apply all filters and sorting
     */
    private fun applyFiltersAndSort() {
        val currentState = _uiState.value
        var filtered = currentState.products
        
        // Apply search filter
        if (currentState.searchQuery.isNotBlank()) {
            filtered = filtered.filter { 
                it.name.contains(currentState.searchQuery, ignoreCase = true) 
            }
        }
        
        // Apply category filter
        currentState.selectedCategory?.let { category ->
            filtered = filtered.filter { it.category == category }
        }
        
        // Apply stock status filter
        currentState.selectedStockStatus?.let { status ->
            filtered = filtered.filter { it.stockStatus == status }
        }
        
        // Apply sorting
        filtered = when (currentState.sortOption) {
            SortOption.NAME_ASC -> filtered.sortedBy { it.name }
            SortOption.NAME_DESC -> filtered.sortedByDescending { it.name }
            SortOption.PRICE_LOW_HIGH -> filtered.sortedBy { it.price }
            SortOption.PRICE_HIGH_LOW -> filtered.sortedByDescending { it.price }
            SortOption.STOCK_HIGH_LOW -> filtered.sortedByDescending { it.stock }
            SortOption.STOCK_LOW_HIGH -> filtered.sortedBy { it.stock }
        }
        
        _uiState.update { it.copy(filteredProducts = filtered) }
    }
    
    /**
     * Show delete confirmation dialog
     */
    fun showDeleteDialog(product: Product) {
        _uiState.update { 
            it.copy(
                showDeleteDialog = true,
                productToDelete = product
            )
        }
    }
    
    /**
     * Hide delete confirmation dialog
     */
    fun hideDeleteDialog() {
        _uiState.update { 
            it.copy(
                showDeleteDialog = false,
                productToDelete = null
            )
        }
    }
    
    /**
     * Delete product
     */
    fun deleteProduct() {
        val productToDelete = _uiState.value.productToDelete ?: return
        
        viewModelScope.launch {
            ProductRepository.deleteProduct(productToDelete.id)
            _uiState.update { 
                it.copy(
                    showDeleteDialog = false,
                    productToDelete = null
                )
            }
        }
    }
    
    /**
     * Quick add stock to product
     */
    fun quickAddStock(productId: String, amount: Int = 10) {
        viewModelScope.launch {
            val product = ProductRepository.getProductById(productId)
            if (product != null) {
                ProductRepository.updateStock(productId, product.stock + amount)
            }
        }
    }
    
    // ========== FORM MANAGEMENT ==========
    
    /**
     * Load product for editing
     */
    fun loadProductForEdit(productId: String) {
        val product = _uiState.value.products.find { it.id == productId } ?: return
        
        _formState.update {
            ProductFormState(
                id = product.id,
                name = product.name,
                price = product.price.toLong().toString(),
                stock = product.stock.toString(),
                category = product.category,
                description = product.description,
                imageUri = product.imageUri,
                sku = product.sku ?: ""
            )
        }
    }
    
    /**
     * Reset form state for new product
     */
    fun resetForm() {
        _formState.value = ProductFormState()
    }
    
    /**
     * Update product name
     */
    fun updateName(name: String) {
        _formState.update { it.copy(name = name) }
        validateName()
    }
    
    /**
     * Update product price
     */
    fun updatePrice(price: String) {
        // Only allow digits
        val numericPrice = price.replace("[^0-9]".toRegex(), "")
        _formState.update { it.copy(price = numericPrice) }
        validatePrice()
    }
    
    /**
     * Update product stock
     */
    fun updateStock(stock: String) {
        // Only allow digits
        val numericStock = stock.replace("[^0-9]".toRegex(), "")
        _formState.update { it.copy(stock = numericStock) }
        validateStock()
    }
    
    /**
     * Update product category
     */
    fun updateCategory(category: ProductCategory) {
        _formState.update { it.copy(category = category, showCategoryDropdown = false) }
    }
    
    /**
     * Update product description
     */
    fun updateDescription(description: String) {
        if (description.length <= 500) {
            _formState.update { it.copy(description = description) }
        }
    }
    
    /**
     * Update product SKU
     */
    fun updateSku(sku: String) {
        _formState.update { it.copy(sku = sku) }
    }
    
    /**
     * Update product image
     */
    fun updateImage(uri: String?) {
        _formState.update { it.copy(imageUri = uri, showImagePicker = false) }
    }
    
    /**
     * Show image picker
     */
    fun showImagePicker() {
        _formState.update { it.copy(showImagePicker = true) }
    }
    
    /**
     * Hide image picker
     */
    fun hideImagePicker() {
        _formState.update { it.copy(showImagePicker = false) }
    }
    
    /**
     * Toggle category dropdown
     */
    fun toggleCategoryDropdown() {
        _formState.update { it.copy(showCategoryDropdown = !it.showCategoryDropdown) }
    }
    
    /**
     * Validate product name
     */
    private fun validateName() {
        val name = _formState.value.name
        val error = when {
            name.isBlank() -> "Nama produk harus diisi"
            name.length < 3 -> "Nama produk minimal 3 karakter"
            else -> null
        }
        _formState.update { it.copy(nameError = error) }
    }
    
    /**
     * Validate product price
     */
    private fun validatePrice() {
        val price = _formState.value.numericPrice
        val error = when {
            _formState.value.price.isBlank() -> "Harga harus diisi"
            price == null || price <= 0 -> "Harga harus lebih dari 0"
            else -> null
        }
        _formState.update { it.copy(priceError = error) }
    }
    
    /**
     * Validate product stock
     */
    private fun validateStock() {
        val stock = _formState.value.numericStock
        val error = when {
            _formState.value.stock.isBlank() -> "Stok harus diisi"
            stock == null || stock < 0 -> "Stok tidak boleh negatif"
            else -> null
        }
        _formState.update { it.copy(stockError = error) }
    }
    
    /**
     * Save product (add or update)
     */
    fun saveProduct(onSuccess: () -> Unit) {
        // Validate all fields
        validateName()
        validatePrice()
        validateStock()
        
        if (!_formState.value.isValid) return
        
        viewModelScope.launch {
            _formState.update { it.copy(isSaving = true) }
            delay(1000) // Simulate API call
            
            val currentForm = _formState.value
            val product = Product(
                id = currentForm.id ?: UUID.randomUUID().toString(),
                name = currentForm.name,
                price = currentForm.numericPrice ?: 0.0,
                stock = currentForm.numericStock ?: 0,
                category = currentForm.category,
                description = currentForm.description,
                imageUri = currentForm.imageUri,
                sku = currentForm.sku.ifBlank { null }
            )
            
            if (currentForm.id != null) {
                // Update existing product
                ProductRepository.updateProduct(product)
            } else {
                // Add new product
                ProductRepository.addProduct(product)
            }
            
            _formState.update { it.copy(isSaving = false) }
            
            onSuccess()
        }
    }
}
