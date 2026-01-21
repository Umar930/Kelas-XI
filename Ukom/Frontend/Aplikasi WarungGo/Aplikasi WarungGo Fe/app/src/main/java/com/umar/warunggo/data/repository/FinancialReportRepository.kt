package com.umar.warunggo.data.repository

import com.umar.warunggo.data.model.*

/**
 * Repository interface for financial reports
 * Ready for Retrofit/API integration
 */
interface FinancialReportRepository {
    suspend fun getFinancialSummary(
        period: ReportPeriod,
        dateRange: DateRange? = null
    ): Result<FinancialSummary>
    
    suspend fun getChartData(
        period: ReportPeriod,
        dateRange: DateRange? = null
    ): Result<List<ChartDataPoint>>
    
    suspend fun getExpenseBreakdown(
        period: ReportPeriod,
        dateRange: DateRange? = null
    ): Result<List<ExpenseCategory>>
    
    suspend fun getBestSellerProducts(
        period: ReportPeriod,
        limit: Int = 10,
        dateRange: DateRange? = null
    ): Result<List<BestSellerProduct>>
    
    suspend fun getRecentTransactions(
        limit: Int = 5
    ): Result<List<TransactionItem>>
}

/**
 * Fake implementation with dummy data for testing
 * Replace with real API implementation later
 */
class FakeFinancialReportRepository : FinancialReportRepository {
    
    override suspend fun getFinancialSummary(
        period: ReportPeriod,
        dateRange: DateRange?
    ): Result<FinancialSummary> {
        // Simulate network delay
        kotlinx.coroutines.delay(800)
        
        return Result.success(
            FinancialSummary(
                totalRevenue = when (period) {
                    ReportPeriod.TODAY -> 5_500_000.0
                    ReportPeriod.WEEKLY -> 38_500_000.0
                    ReportPeriod.MONTHLY -> 165_000_000.0
                    ReportPeriod.CUSTOM -> 12_000_000.0
                },
                totalExpense = when (period) {
                    ReportPeriod.TODAY -> 3_200_000.0
                    ReportPeriod.WEEKLY -> 22_400_000.0
                    ReportPeriod.MONTHLY -> 98_000_000.0
                    ReportPeriod.CUSTOM -> 7_000_000.0
                },
                netProfit = when (period) {
                    ReportPeriod.TODAY -> 2_300_000.0
                    ReportPeriod.WEEKLY -> 16_100_000.0
                    ReportPeriod.MONTHLY -> 67_000_000.0
                    ReportPeriod.CUSTOM -> 5_000_000.0
                },
                totalTransactions = when (period) {
                    ReportPeriod.TODAY -> 45
                    ReportPeriod.WEEKLY -> 315
                    ReportPeriod.MONTHLY -> 1350
                    ReportPeriod.CUSTOM -> 120
                },
                revenueChange = 12.5,
                expenseChange = 8.3,
                profitChange = 18.7,
                transactionChange = 10.2
            )
        )
    }
    
    override suspend fun getChartData(
        period: ReportPeriod,
        dateRange: DateRange?
    ): Result<List<ChartDataPoint>> {
        kotlinx.coroutines.delay(600)
        
        val data = when (period) {
            ReportPeriod.TODAY -> listOf(
                ChartDataPoint("00:00", 200_000.0, 150_000.0, 50_000.0),
                ChartDataPoint("04:00", 300_000.0, 180_000.0, 120_000.0),
                ChartDataPoint("08:00", 800_000.0, 450_000.0, 350_000.0),
                ChartDataPoint("12:00", 1_500_000.0, 900_000.0, 600_000.0),
                ChartDataPoint("16:00", 1_200_000.0, 750_000.0, 450_000.0),
                ChartDataPoint("20:00", 1_500_000.0, 770_000.0, 730_000.0)
            )
            ReportPeriod.WEEKLY -> listOf(
                ChartDataPoint("Sen", 4_500_000.0, 2_800_000.0, 1_700_000.0),
                ChartDataPoint("Sel", 5_200_000.0, 3_100_000.0, 2_100_000.0),
                ChartDataPoint("Rab", 6_100_000.0, 3_600_000.0, 2_500_000.0),
                ChartDataPoint("Kam", 5_800_000.0, 3_400_000.0, 2_400_000.0),
                ChartDataPoint("Jum", 7_200_000.0, 4_200_000.0, 3_000_000.0),
                ChartDataPoint("Sab", 6_500_000.0, 3_800_000.0, 2_700_000.0),
                ChartDataPoint("Min", 3_200_000.0, 1_500_000.0, 1_700_000.0)
            )
            ReportPeriod.MONTHLY -> listOf(
                ChartDataPoint("W1", 32_000_000.0, 19_000_000.0, 13_000_000.0),
                ChartDataPoint("W2", 38_500_000.0, 22_400_000.0, 16_100_000.0),
                ChartDataPoint("W3", 45_000_000.0, 26_500_000.0, 18_500_000.0),
                ChartDataPoint("W4", 49_500_000.0, 30_100_000.0, 19_400_000.0)
            )
            ReportPeriod.CUSTOM -> listOf(
                ChartDataPoint("Day 1", 2_000_000.0, 1_200_000.0, 800_000.0),
                ChartDataPoint("Day 2", 2_500_000.0, 1_400_000.0, 1_100_000.0),
                ChartDataPoint("Day 3", 3_000_000.0, 1_600_000.0, 1_400_000.0),
                ChartDataPoint("Day 4", 2_800_000.0, 1_500_000.0, 1_300_000.0),
                ChartDataPoint("Day 5", 1_700_000.0, 1_300_000.0, 400_000.0)
            )
        }
        
        return Result.success(data)
    }
    
    override suspend fun getExpenseBreakdown(
        period: ReportPeriod,
        dateRange: DateRange?
    ): Result<List<ExpenseCategory>> {
        kotlinx.coroutines.delay(500)
        
        return Result.success(
            listOf(
                ExpenseCategory("1", "Pembelian Stok", 1_500_000.0, 46.9),
                ExpenseCategory("2", "Gaji Karyawan", 800_000.0, 25.0),
                ExpenseCategory("3", "Listrik & Air", 300_000.0, 9.4),
                ExpenseCategory("4", "Sewa Tempat", 400_000.0, 12.5),
                ExpenseCategory("5", "Lain-lain", 200_000.0, 6.2)
            )
        )
    }
    
    override suspend fun getBestSellerProducts(
        period: ReportPeriod,
        limit: Int,
        dateRange: DateRange?
    ): Result<List<BestSellerProduct>> {
        kotlinx.coroutines.delay(600)
        
        return Result.success(
            listOf(
                BestSellerProduct("1", "Indomie Goreng", 350, 1_225_000.0, null, "Makanan"),
                BestSellerProduct("2", "Aqua 600ml", 280, 840_000.0, null, "Minuman"),
                BestSellerProduct("3", "Beras Premium 5kg", 45, 3_375_000.0, null, "Sembako"),
                BestSellerProduct("4", "Kopi Kapal Api", 200, 2_400_000.0, null, "Minuman"),
                BestSellerProduct("5", "Teh Botol Sosro", 180, 720_000.0, null, "Minuman"),
                BestSellerProduct("6", "Sabun Lifebuoy", 95, 475_000.0, null, "Kebersihan"),
                BestSellerProduct("7", "Gula Pasir 1kg", 120, 1_800_000.0, null, "Sembako"),
                BestSellerProduct("8", "Minyak Goreng 2L", 85, 2_720_000.0, null, "Sembako")
            ).take(limit)
        )
    }
    
    override suspend fun getRecentTransactions(
        limit: Int
    ): Result<List<TransactionItem>> {
        kotlinx.coroutines.delay(400)
        
        val currentTime = System.currentTimeMillis()
        
        return Result.success(
            listOf(
                TransactionItem(
                    "1",
                    "INV-20260114-001",
                    currentTime - 300000,
                    125_000.0,
                    TransactionStatus.SUCCESS,
                    "Ahmad Fauzi",
                    5
                ),
                TransactionItem(
                    "2",
                    "INV-20260114-002",
                    currentTime - 900000,
                    89_500.0,
                    TransactionStatus.SUCCESS,
                    "Siti Nurhaliza",
                    3
                ),
                TransactionItem(
                    "3",
                    "INV-20260114-003",
                    currentTime - 1800000,
                    450_000.0,
                    TransactionStatus.PENDING,
                    "Budi Santoso",
                    12
                ),
                TransactionItem(
                    "4",
                    "INV-20260114-004",
                    currentTime - 3600000,
                    75_000.0,
                    TransactionStatus.SUCCESS,
                    "Dewi Lestari",
                    2
                ),
                TransactionItem(
                    "5",
                    "INV-20260114-005",
                    currentTime - 7200000,
                    320_000.0,
                    TransactionStatus.SUCCESS,
                    "Eko Prasetyo",
                    8
                )
            ).take(limit)
        )
    }
}
