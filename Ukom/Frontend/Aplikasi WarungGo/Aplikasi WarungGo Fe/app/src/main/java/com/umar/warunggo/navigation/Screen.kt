package com.umar.warunggo.navigation

/**
 * Sealed class untuk mendefinisikan semua route navigasi
 * Memudahkan type-safe navigation di aplikasi
 */
sealed class Screen(val route: String) {
    // Splash
    object Splash : Screen("splash")
    
    // Auth
    object Login : Screen("login")
    object Register : Screen("register")
    
    // Main (dengan Bottom Navigation)
    object Dashboard : Screen("dashboard")
    object ProductList : Screen("product_list")
    object TransactionList : Screen("transaction_list")
    object Report : Screen("report")
    object Profile : Screen("profile")
    
    // Product
    object AddProduct : Screen("add_product")
    object EditProduct : Screen("edit_product/{id}") {
        fun createRoute(id: String) = "edit_product/$id"
    }
    
    // Transaction
    object TransactionCreate : Screen("transaction_create")
    object TransactionDetail : Screen("transaction_detail/{id}") {
        fun createRoute(id: Int) = "transaction_detail/$id"
    }
    
    // Expense
    object ExpenseList : Screen("expense_list")
    object ExpenseForm : Screen("expense_form?id={id}") {
        fun createRoute(id: Int? = null) = if (id != null) "expense_form?id=$id" else "expense_form"
    }
}
