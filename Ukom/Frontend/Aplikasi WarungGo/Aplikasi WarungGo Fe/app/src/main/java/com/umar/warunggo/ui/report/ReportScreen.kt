package com.umar.warunggo.ui.report

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Report Screen - Wrapper for Financial Report
 * Displays comprehensive financial reports with KPIs, charts, and insights
 */
@Composable
fun ReportScreen(
    viewModel: FinancialReportViewModel = viewModel()
) {
    FinancialReportScreen(viewModel = viewModel)
}
