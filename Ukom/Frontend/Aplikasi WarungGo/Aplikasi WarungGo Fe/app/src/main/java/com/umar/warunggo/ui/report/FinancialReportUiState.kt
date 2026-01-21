package com.umar.warunggo.ui.report

import com.umar.warunggo.data.model.*

/**
 * UI state for Financial Report Screen
 */
data class FinancialReportUiState(
    // Period selection
    val selectedPeriod: ReportPeriod = ReportPeriod.TODAY,
    val customDateRange: DateRange? = null,
    
    // Summary metrics
    val summary: FinancialSummary? = null,
    
    // Chart data
    val selectedChartType: ChartType = ChartType.REVENUE,
    val chartData: List<ChartDataPoint> = emptyList(),
    
    // Expense breakdown
    val expenseCategories: List<ExpenseCategory> = emptyList(),
    
    // Best sellers
    val bestSellerProducts: List<BestSellerProduct> = emptyList(),
    
    // Transaction history
    val recentTransactions: List<TransactionItem> = emptyList(),
    
    // UI states
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    
    // Dialog states
    val showFilterDialog: Boolean = false,
    val showExportDialog: Boolean = false,
    val showDatePicker: Boolean = false
)
