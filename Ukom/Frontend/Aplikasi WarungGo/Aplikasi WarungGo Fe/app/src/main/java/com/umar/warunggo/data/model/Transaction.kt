package com.umar.warunggo.data.model

/**
 * Transaction data models
 */

/**
 * Cart item model
 */
data class CartItem(
    val product: Product,
    val quantity: Int = 1
) {
    fun getSubtotal(): Double = product.price * quantity
    
    fun getFormattedSubtotal(): String = 
        "Rp ${"%,d".format(getSubtotal().toLong()).replace(',', '.')}"
    
    fun canIncreaseQuantity(): Boolean = quantity < product.stock
    
    fun getFormattedPrice(): String = product.formattedPrice
}

/**
 * Payment method enum
 */
enum class PaymentMethod(val displayName: String) {
    CASH("Tunai"),
    QRIS("QRIS"),
    TRANSFER("Transfer Bank"),
    CREDIT("Hutang")
}

/**
 * Transaction model
 */
data class Transaction(
    val id: String,
    val transactionDate: Long,
    val items: List<CartItem>,
    val subtotal: Double,
    val discount: Double = 0.0,
    val tax: Double = 0.0,
    val total: Double,
    val paymentMethod: PaymentMethod,
    val paidAmount: Double,
    val change: Double,
    val customerName: String? = null,
    val notes: String? = null
) {
    fun getFormattedSubtotal(): String = "Rp ${"%,d".format(subtotal.toLong()).replace(',', '.')}"
    fun getFormattedDiscount(): String = "Rp ${"%,d".format(discount.toLong()).replace(',', '.')}"
    fun getFormattedTax(): String = "Rp ${"%,d".format(tax.toLong()).replace(',', '.')}"
    fun getFormattedTotal(): String = "Rp ${"%,d".format(total.toLong()).replace(',', '.')}"
    fun getFormattedPaidAmount(): String = "Rp ${"%,d".format(paidAmount.toLong()).replace(',', '.')}"
    fun getFormattedChange(): String = "Rp ${"%,d".format(change.toLong()).replace(',', '.')}"
    
    fun getFormattedDate(): String {
        val sdf = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale("id", "ID"))
        return sdf.format(java.util.Date(transactionDate))
    }
}
