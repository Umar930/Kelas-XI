package com.umar.warunggo.data.model

/**
 * Financial summary metrics for a specific period
 */
data class FinancialSummary(
    val totalRevenue: Double,
    val totalExpense: Double,
    val netProfit: Double,
    val totalTransactions: Int,
    val revenueChange: Double, // Percentage change from previous period
    val expenseChange: Double,
    val profitChange: Double,
    val transactionChange: Double
) {
    /**
     * Calculate profit margin percentage
     */
    val profitMargin: Double
        get() = if (totalRevenue > 0) (netProfit / totalRevenue) * 100 else 0.0
    
    /**
     * Format currency values
     */
    fun getFormattedRevenue(): String = formatCurrency(totalRevenue)
    fun getFormattedExpense(): String = formatCurrency(totalExpense)
    fun getFormattedProfit(): String = formatCurrency(netProfit)
    
    private fun formatCurrency(value: Double): String {
        return "Rp ${"%,.0f".format(value).replace(",", ".")}"
    }
}

/**
 * Chart data point for financial charts
 */
data class ChartDataPoint(
    val label: String, // Date or period label
    val revenue: Double,
    val expense: Double,
    val profit: Double
)

/**
 * Expense category breakdown
 */
data class ExpenseCategory(
    val id: String,
    val name: String,
    val amount: Double,
    val percentage: Double,
    val icon: String? = null
) {
    fun getFormattedAmount(): String {
        return "Rp ${"%,.0f".format(amount).replace(",", ".")}"
    }
}

/**
 * Best selling product data
 */
data class BestSellerProduct(
    val id: String,
    val name: String,
    val unitsSold: Int,
    val revenue: Double,
    val imageUrl: String? = null,
    val category: String
) {
    fun getFormattedRevenue(): String {
        return "Rp ${"%,.0f".format(revenue).replace(",", ".")}"
    }
}

/**
 * Transaction item for history preview
 */
data class TransactionItem(
    val id: String,
    val invoiceId: String,
    val date: Long, // Timestamp
    val totalAmount: Double,
    val status: TransactionStatus,
    val customerName: String? = null,
    val itemCount: Int
) {
    fun getFormattedAmount(): String {
        return "Rp ${"%,.0f".format(totalAmount).replace(",", ".")}"
    }
    
    fun getFormattedDate(): String {
        val dateFormat = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale("id", "ID"))
        return dateFormat.format(java.util.Date(date))
    }
}

/**
 * Transaction status enum
 */
enum class TransactionStatus {
    SUCCESS,
    PENDING,
    CANCELLED,
    REFUNDED
}

/**
 * Period selection type
 */
enum class ReportPeriod {
    TODAY,
    WEEKLY,
    MONTHLY,
    CUSTOM
}

/**
 * Chart type selection
 */
enum class ChartType {
    REVENUE,
    EXPENSE,
    PROFIT
}

/**
 * Date range for custom period
 */
data class DateRange(
    val startDate: Long,
    val endDate: Long
) {
    fun getFormattedRange(): String {
        val dateFormat = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale("id", "ID"))
        val start = dateFormat.format(java.util.Date(startDate))
        val end = dateFormat.format(java.util.Date(endDate))
        return "$start - $end"
    }
}
