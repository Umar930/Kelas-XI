package com.umar.warunggo.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umar.warunggo.data.model.*
import com.umar.warunggo.data.repository.FakeFinancialReportRepository
import com.umar.warunggo.data.repository.FinancialReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for Financial Report Screen
 * Manages business logic and state for financial reporting
 */
class FinancialReportViewModel(
    private val repository: FinancialReportRepository = FakeFinancialReportRepository()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FinancialReportUiState())
    val uiState: StateFlow<FinancialReportUiState> = _uiState.asStateFlow()
    
    init {
        loadReport()
    }
    
    /**
     * Load complete financial report
     */
    fun loadReport() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            try {
                // Load all data in parallel
                val period = _uiState.value.selectedPeriod
                val dateRange = _uiState.value.customDateRange
                
                // Load summary
                val summaryResult = repository.getFinancialSummary(period, dateRange)
                
                // Load chart data
                val chartResult = repository.getChartData(period, dateRange)
                
                // Load expense breakdown
                val expenseResult = repository.getExpenseBreakdown(period, dateRange)
                
                // Load best sellers
                val bestSellersResult = repository.getBestSellerProducts(period, 8, dateRange)
                
                // Load recent transactions
                val transactionsResult = repository.getRecentTransactions(5)
                
                _uiState.update {
                    it.copy(
                        summary = summaryResult.getOrNull(),
                        chartData = chartResult.getOrNull() ?: emptyList(),
                        expenseCategories = expenseResult.getOrNull() ?: emptyList(),
                        bestSellerProducts = bestSellersResult.getOrNull() ?: emptyList(),
                        recentTransactions = transactionsResult.getOrNull() ?: emptyList(),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Gagal memuat data: ${e.message}"
                    )
                }
            }
        }
    }
    
    /**
     * Refresh data
     */
    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            loadReport()
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }
    
    /**
     * Change period selection
     */
    fun selectPeriod(period: ReportPeriod) {
        _uiState.update {
            it.copy(
                selectedPeriod = period,
                customDateRange = if (period != ReportPeriod.CUSTOM) null else it.customDateRange
            )
        }
        loadReport()
    }
    
    /**
     * Set custom date range
     */
    fun setCustomDateRange(dateRange: DateRange) {
        _uiState.update {
            it.copy(
                selectedPeriod = ReportPeriod.CUSTOM,
                customDateRange = dateRange,
                showDatePicker = false
            )
        }
        loadReport()
    }
    
    /**
     * Change chart type
     */
    fun selectChartType(chartType: ChartType) {
        _uiState.update { it.copy(selectedChartType = chartType) }
    }
    
    /**
     * Show filter dialog
     */
    fun showFilterDialog() {
        _uiState.update { it.copy(showFilterDialog = true) }
    }
    
    /**
     * Hide filter dialog
     */
    fun hideFilterDialog() {
        _uiState.update { it.copy(showFilterDialog = false) }
    }
    
    /**
     * Show export dialog
     */
    fun showExportDialog() {
        _uiState.update { it.copy(showExportDialog = true) }
    }
    
    /**
     * Hide export dialog
     */
    fun hideExportDialog() {
        _uiState.update { it.copy(showExportDialog = false) }
    }
    
    /**
     * Show date picker
     */
    fun showDatePicker() {
        _uiState.update { it.copy(showDatePicker = true) }
    }
    
    /**
     * Hide date picker
     */
    fun hideDatePicker() {
        _uiState.update { it.copy(showDatePicker = false) }
    }
    
    /**
     * Export to PDF (placeholder)
     */
    fun exportToPdf() {
        // TODO: Implement PDF export
        hideExportDialog()
    }
    
    /**
     * Export to Excel (placeholder)
     */
    fun exportToExcel() {
        // TODO: Implement Excel export
        hideExportDialog()
    }
    
    /**
     * Share report (placeholder)
     */
    fun shareReport() {
        // TODO: Implement share functionality
        hideExportDialog()
    }
}
