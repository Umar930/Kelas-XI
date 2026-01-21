package com.umar.warunggo.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umar.warunggo.data.model.*
import com.umar.warunggo.data.repository.ProductRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

/**
 * ViewModel for Transaction screen
 * Manages cart, calculations, and transaction submission
 */
class TransactionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadProducts()
        // Observe product changes from repository
        viewModelScope.launch {
            ProductRepository.products.collect { products ->
                _uiState.update {
                    it.copy(
                        allProducts = products,
                        filteredProducts = if (it.searchQuery.isBlank()) products else it.filteredProducts
                    )
                }
                if (_uiState.value.searchQuery.isNotBlank()) {
                    filterProducts(_uiState.value.searchQuery)
                }
            }
        }
    }

    /**
     * Load products from repository
     */
    private fun loadProducts() {
        viewModelScope.launch {
            val products = ProductRepository.getAllProducts()
            _uiState.update {
                it.copy(
                    allProducts = products,
                    filteredProducts = products
                )
            }
        }
    }

    /**
     * Update search query with debouncing
     */
    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query, isSearching = true) }
        
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300) // Debounce delay
            filterProducts(query)
            _uiState.update { it.copy(isSearching = false) }
        }
    }

    /**
     * Filter products based on search query
     */
    private fun filterProducts(query: String) {
        val filtered = if (query.isBlank()) {
            _uiState.value.allProducts
        } else {
            _uiState.value.allProducts.filter { product ->
                product.name.contains(query, ignoreCase = true) ||
                product.sku?.contains(query) == true ||
                product.category.displayName.contains(query, ignoreCase = true)
            }
        }
        
        _uiState.update { it.copy(filteredProducts = filtered) }
    }

    /**
     * Add product to cart
     */
    fun addToCart(product: Product) {
        val currentCart = _uiState.value.cartItems.toMutableList()
        
        val existingItem = currentCart.find { it.product.id == product.id }
        
        if (existingItem != null) {
            // Product already in cart, increase quantity
            if (existingItem.canIncreaseQuantity()) {
                val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
                currentCart[currentCart.indexOf(existingItem)] = updatedItem
            }
        } else {
            // Add new product to cart
            if (product.stock > 0) {
                currentCart.add(CartItem(product = product, quantity = 1))
            }
        }
        
        _uiState.update { it.copy(cartItems = currentCart) }
        calculateTotals()
    }

    /**
     * Remove product from cart
     */
    fun removeFromCart(productId: String) {
        val updatedCart = _uiState.value.cartItems.filter { it.product.id != productId }
        _uiState.update { it.copy(cartItems = updatedCart) }
        calculateTotals()
    }

    /**
     * Increase quantity of cart item
     */
    fun increaseQuantity(productId: String) {
        val currentCart = _uiState.value.cartItems.toMutableList()
        val itemIndex = currentCart.indexOfFirst { it.product.id == productId }
        
        if (itemIndex != -1) {
            val item = currentCart[itemIndex]
            if (item.canIncreaseQuantity()) {
                currentCart[itemIndex] = item.copy(quantity = item.quantity + 1)
                _uiState.update { it.copy(cartItems = currentCart) }
                calculateTotals()
            }
        }
    }

    /**
     * Decrease quantity of cart item
     */
    fun decreaseQuantity(productId: String) {
        val currentCart = _uiState.value.cartItems.toMutableList()
        val itemIndex = currentCart.indexOfFirst { it.product.id == productId }
        
        if (itemIndex != -1) {
            val item = currentCart[itemIndex]
            if (item.quantity > 1) {
                currentCart[itemIndex] = item.copy(quantity = item.quantity - 1)
                _uiState.update { it.copy(cartItems = currentCart) }
                calculateTotals()
            } else {
                // Remove item if quantity becomes 0
                removeFromCart(productId)
            }
        }
    }

    /**
     * Calculate totals (subtotal, discount, tax, total)
     */
    private fun calculateTotals() {
        val subtotal = _uiState.value.cartItems.sumOf { it.getSubtotal() }
        
        val discount = if (_uiState.value.discountPercentage > 0) {
            subtotal * (_uiState.value.discountPercentage / 100.0)
        } else {
            _uiState.value.discount
        }
        
        val afterDiscount = subtotal - discount
        
        val tax = if (_uiState.value.isTaxEnabled) {
            if (_uiState.value.taxPercentage > 0) {
                afterDiscount * (_uiState.value.taxPercentage / 100.0)
            } else {
                _uiState.value.tax
            }
        } else {
            0.0
        }
        
        val total = afterDiscount + tax
        
        // Calculate change
        val paid = _uiState.value.paidAmount.toDoubleOrNull() ?: 0.0
        val change = if (paid >= total) paid - total else 0.0
        
        _uiState.update {
            it.copy(
                subtotal = subtotal,
                discount = discount,
                tax = tax,
                total = total,
                change = change
            )
        }
    }

    /**
     * Update discount
     */
    fun updateDiscount(amount: Double) {
        _uiState.update { it.copy(discount = amount, discountPercentage = 0.0) }
        calculateTotals()
    }

    /**
     * Update discount percentage
     */
    fun updateDiscountPercentage(percentage: Double) {
        _uiState.update { it.copy(discountPercentage = percentage, discount = 0.0) }
        calculateTotals()
    }

    /**
     * Toggle tax
     */
    fun toggleTax(enabled: Boolean) {
        _uiState.update { it.copy(isTaxEnabled = enabled) }
        calculateTotals()
    }

    /**
     * Update tax percentage
     */
    fun updateTaxPercentage(percentage: Double) {
        _uiState.update { it.copy(taxPercentage = percentage) }
        calculateTotals()
    }

    /**
     * Select payment method
     */
    fun selectPaymentMethod(method: PaymentMethod) {
        _uiState.update { it.copy(selectedPaymentMethod = method) }
        
        // For CREDIT payment, set paid amount to 0
        if (method == PaymentMethod.CREDIT) {
            updatePaidAmount("0")
        }
    }

    /**
     * Update paid amount
     */
    fun updatePaidAmount(amount: String) {
        // Only allow numbers
        val filtered = amount.filter { it.isDigit() }
        _uiState.update { it.copy(paidAmount = filtered) }
        calculateTotals()
    }

    /**
     * Quick set paid amount to exact total
     */
    fun setExactAmount() {
        val exactAmount = _uiState.value.total.toLong().toString()
        updatePaidAmount(exactAmount)
    }

    /**
     * Show payment method dialog
     */
    fun showPaymentMethodDialog() {
        _uiState.update { it.copy(showPaymentMethodDialog = true) }
    }

    /**
     * Hide payment method dialog
     */
    fun hidePaymentMethodDialog() {
        _uiState.update { it.copy(showPaymentMethodDialog = false) }
    }

    /**
     * Submit transaction
     */
    fun submitTransaction() {
        if (!_uiState.value.canSubmitTransaction()) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }
            
            try {
                // Simulate API call
                delay(1000)
                
                val transaction = Transaction(
                    id = "TRX${System.currentTimeMillis()}",
                    transactionDate = System.currentTimeMillis(),
                    items = _uiState.value.cartItems,
                    subtotal = _uiState.value.subtotal,
                    discount = _uiState.value.discount,
                    tax = _uiState.value.tax,
                    total = _uiState.value.total,
                    paymentMethod = _uiState.value.selectedPaymentMethod,
                    paidAmount = _uiState.value.paidAmount.toDoubleOrNull() ?: 0.0,
                    change = _uiState.value.change
                )
                
                // Update stock in repository
                transaction.items.forEach { cartItem ->
                    ProductRepository.reduceStock(cartItem.product.id, cartItem.quantity)
                }
                
                _uiState.update {
                    it.copy(
                        isProcessing = false,
                        completedTransaction = transaction,
                        showReceiptDialog = true
                    )
                }
                
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isProcessing = false,
                        errorMessage = "Gagal memproses transaksi: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Reset transaction after successful submission
     */
    fun resetTransaction() {
        _uiState.update {
            TransactionUiState(
                allProducts = it.allProducts,
                filteredProducts = it.allProducts,
                storeName = it.storeName
            )
        }
    }

    /**
     * Hide receipt dialog
     */
    fun hideReceiptDialog() {
        _uiState.update { it.copy(showReceiptDialog = false) }
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
