package com.umar.warunggo.ui.transaction

import com.umar.warunggo.data.model.CartItem
import com.umar.warunggo.data.model.PaymentMethod
import com.umar.warunggo.data.model.Product
import com.umar.warunggo.data.model.Transaction

/**
 * UI state for Transaction screen
 */
data class TransactionUiState(
    // Product list
    val allProducts: List<Product> = emptyList(),
    val filteredProducts: List<Product> = emptyList(),
    
    // Search
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    
    // Cart
    val cartItems: List<CartItem> = emptyList(),
    
    // Calculations
    val subtotal: Double = 0.0,
    val discount: Double = 0.0,
    val discountPercentage: Double = 0.0,
    val tax: Double = 0.0,
    val taxPercentage: Double = 0.0,
    val isTaxEnabled: Boolean = false,
    val total: Double = 0.0,
    
    // Payment
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.CASH,
    val paidAmount: String = "",
    val change: Double = 0.0,
    
    // Transaction
    val isProcessing: Boolean = false,
    val completedTransaction: Transaction? = null,
    val errorMessage: String? = null,
    
    // Dialog states
    val showReceiptDialog: Boolean = false,
    val showPaymentMethodDialog: Boolean = false,
    
    // Store info
    val storeName: String = "Warung Berkah Jaya"
) {
    fun getFormattedSubtotal(): String = "Rp ${"%,d".format(subtotal.toLong()).replace(',', '.')}"
    fun getFormattedDiscount(): String = "Rp ${"%,d".format(discount.toLong()).replace(',', '.')}"
    fun getFormattedTax(): String = "Rp ${"%,d".format(tax.toLong()).replace(',', '.')}"
    fun getFormattedTotal(): String = "Rp ${"%,d".format(total.toLong()).replace(',', '.')}"
    fun getFormattedChange(): String = "Rp ${"%,d".format(change.toLong()).replace(',', '.')}"
    
    fun isCartEmpty(): Boolean = cartItems.isEmpty()
    
    fun canSubmitTransaction(): Boolean {
        if (isCartEmpty()) return false
        if (isProcessing) return false
        
        val paid = paidAmount.toDoubleOrNull() ?: 0.0
        return when (selectedPaymentMethod) {
            PaymentMethod.CASH -> paid >= total
            PaymentMethod.CREDIT -> true // Credit allows payment later
            else -> paid >= total
        }
    }
    
    fun getTotalItemCount(): Int = cartItems.sumOf { it.quantity }
}
