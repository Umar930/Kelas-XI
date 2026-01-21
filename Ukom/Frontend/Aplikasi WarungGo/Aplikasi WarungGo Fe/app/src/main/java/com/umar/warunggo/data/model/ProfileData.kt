package com.umar.warunggo.data.model

/**
 * User profile data model
 */
data class ProfileData(
    val id: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val role: UserRole,
    val avatarUrl: String?,
    val joinDate: Long,
    val lastLogin: Long,
    val storeInfo: StoreInfo
)

/**
 * Store information data model
 */
data class StoreInfo(
    val storeName: String,
    val address: String,
    val phone: String,
    val category: String,
    val operationalHours: String,
    val isActive: Boolean
)

/**
 * Business summary statistics
 */
data class BusinessSummary(
    val totalProducts: Int,
    val totalTransactions: Int,
    val monthlyRevenue: Double,
    val monthlyProfit: Double
) {
    fun getFormattedRevenue(): String = "Rp ${"%,d".format(monthlyRevenue.toLong())}"
    fun getFormattedProfit(): String = "Rp ${"%,d".format(monthlyProfit.toLong())}"
}

/**
 * User role enum
 */
enum class UserRole(val displayName: String) {
    OWNER("Owner"),
    CASHIER("Kasir"),
    ADMIN("Admin"),
    STAFF("Staff")
}

/**
 * Settings menu item model
 */
data class SettingsMenuItem(
    val id: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val hasTrailing: Boolean = true
)
