package com.umar.warunggo.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.umar.warunggo.ui.auth.login.LoginScreen
import com.umar.warunggo.ui.auth.register.RegisterScreen
import com.umar.warunggo.ui.dashboard.DashboardScreen
import com.umar.warunggo.ui.splash.SplashScreen
import com.umar.warunggo.ui.expense.ExpenseFormScreen
import com.umar.warunggo.ui.expense.ExpenseListScreen
import com.umar.warunggo.ui.product.ProductListScreen
import com.umar.warunggo.ui.product.AddEditProductScreen
import com.umar.warunggo.ui.profile.ProfileScreen
import com.umar.warunggo.ui.report.ReportScreen
import com.umar.warunggo.ui.transaction.TransactionCreateScreen
import com.umar.warunggo.ui.transaction.TransactionDetailScreen
import com.umar.warunggo.ui.transaction.TransactionListScreen

/**
 * Main navigation graph untuk aplikasi WarungGo
 * Menangani routing antar screen dan bottom navigation
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    // Check if current screen should show bottom navigation
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val showBottomBar = currentRoute in listOf(
        Screen.Dashboard.route,
        Screen.ProductList.route,
        Screen.TransactionList.route,
        Screen.Report.route,
        Screen.Profile.route
    )
    
    // Hide bottom bar for splash and auth screens
    val isAuthOrSplash = currentRoute in listOf(
        Screen.Splash.route,
        Screen.Login.route,
        Screen.Register.route
    )
    
    Scaffold(
        bottomBar = {
            if (showBottomBar && !isAuthOrSplash) {
                BottomNavigationBar(navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = if (isAuthOrSplash) Modifier else Modifier.padding(paddingValues)
        ) {
            // Splash Screen
            composable(
                route = Screen.Splash.route,
                exitTransition = {
                    fadeOut() + slideOutHorizontally(targetOffsetX = { -it })
                }
            ) {
                SplashScreen(
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }
            
            // Auth Screens
            composable(
                route = Screen.Login.route,
                enterTransition = {
                    fadeIn() + slideInHorizontally(initialOffsetX = { it })
                },
                exitTransition = {
                    fadeOut() + slideOutHorizontally(targetOffsetX = { -it })
                }
            ) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }
            
            composable(
                route = Screen.Register.route,
                enterTransition = {
                    fadeIn() + slideInHorizontally(initialOffsetX = { it })
                },
                exitTransition = {
                    fadeOut() + slideOutHorizontally(targetOffsetX = { it })
                },
                popEnterTransition = {
                    fadeIn() + slideInHorizontally(initialOffsetX = { -it })
                }
            ) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }
            
            // Main Screens with Bottom Navigation
            composable(Screen.Dashboard.route) {
                DashboardScreen()
            }
            
            // Product Screens
            composable(
                route = Screen.ProductList.route,
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() }
            ) {
                ProductListScreen(
                    onNavigateToAddProduct = {
                        navController.navigate(Screen.AddProduct.route)
                    },
                    onNavigateToEditProduct = { productId ->
                        navController.navigate(Screen.EditProduct.createRoute(productId))
                    }
                )
            }
            
            composable(
                route = Screen.AddProduct.route,
                enterTransition = { 
                    fadeIn() + slideInHorizontally(initialOffsetX = { it }) 
                },
                exitTransition = { 
                    fadeOut() + slideOutHorizontally(targetOffsetX = { it }) 
                }
            ) {
                AddEditProductScreen(
                    productId = null,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            composable(
                route = Screen.EditProduct.route,
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.StringType
                    }
                ),
                enterTransition = { 
                    fadeIn() + slideInHorizontally(initialOffsetX = { it }) 
                },
                exitTransition = { 
                    fadeOut() + slideOutHorizontally(targetOffsetX = { it }) 
                }
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("id")
                AddEditProductScreen(
                    productId = productId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            // Transaction Screens
            composable(Screen.TransactionCreate.route) {
                TransactionCreateScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onTransactionSaved = {
                        navController.navigate(Screen.TransactionList.route) {
                            popUpTo(Screen.TransactionCreate.route) { inclusive = true }
                        }
                    }
                )
            }
            
            composable(Screen.TransactionList.route) {
                TransactionListScreen(
                    onNavigateToDetail = { transactionId ->
                        navController.navigate(Screen.TransactionDetail.createRoute(transactionId))
                    },
                    onNavigateToCreate = {
                        navController.navigate(Screen.TransactionCreate.route)
                    }
                )
            }
            
            composable(
                route = Screen.TransactionDetail.route,
                arguments = listOf(
                    navArgument("id") { type = NavType.IntType }
                )
            ) {
                TransactionDetailScreen(
                    transactionId = it.arguments?.getInt("id") ?: 0,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            // Expense Screens
            composable(Screen.ExpenseList.route) {
                ExpenseListScreen(
                    onNavigateToForm = { expenseId ->
                        navController.navigate(Screen.ExpenseForm.createRoute(expenseId))
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            composable(
                route = Screen.ExpenseForm.route,
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.StringType
                        nullable = true
                    }
                )
            ) {
                ExpenseFormScreen(
                    expenseId = it.arguments?.getString("id")?.toIntOrNull(),
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            // Report Screen
            composable(Screen.Report.route) {
                ReportScreen()
            }
            
            // Profile Screen
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

/**
 * Bottom Navigation Bar dengan Material Icons
 * Menampilkan menu navigasi utama: Dashboard, Produk, Transaksi, Laporan, Profil
 */
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Products,
        BottomNavItem.Transactions,
        BottomNavItem.Report,
        BottomNavItem.Profile
    )
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    NavigationBar {
        items.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true
            NavigationBarItem(
                icon = {
                    if (item.useTextIcon) {
                        Text(
                            text = item.textIcon ?: "",
                            style = androidx.compose.material3.MaterialTheme.typography.titleLarge
                        )
                    } else {
                        Icon(
                            imageVector = if (selected) item.selectedIcon!! else item.unselectedIcon!!,
                            contentDescription = item.title
                        )
                    }
                },
                label = { Text(item.title) },
                selected = selected,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

/**
 * Data class untuk item bottom navigation dengan Material Icons
 */
sealed class BottomNavItem(
    val screen: Screen,
    val title: String,
    val selectedIcon: ImageVector? = null,
    val unselectedIcon: ImageVector? = null,
    val useTextIcon: Boolean = false,
    val textIcon: String? = null
) {
    object Dashboard : BottomNavItem(
        Screen.Dashboard,
        "Dashboard",
        Icons.Filled.Home,
        Icons.Outlined.Home
    )
    
    object Products : BottomNavItem(
        Screen.ProductList,
        "Produk",
        useTextIcon = true,
        textIcon = "📦"
    )
    
    object Transactions : BottomNavItem(
        Screen.TransactionList,
        "Transaksi",
        Icons.Filled.ShoppingCart,
        Icons.Outlined.ShoppingCart
    )
    
    object Report : BottomNavItem(
        Screen.Report,
        "Laporan",
        useTextIcon = true,
        textIcon = "📊"
    )
    
    object Profile : BottomNavItem(
        Screen.Profile,
        "Profil",
        Icons.Filled.AccountCircle,
        Icons.Outlined.AccountCircle
    )
}
