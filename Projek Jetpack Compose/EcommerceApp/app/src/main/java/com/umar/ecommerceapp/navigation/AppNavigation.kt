package com.umar.ecommerceapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.umar.ecommerceapp.pages.CategoryProductsPage
import com.umar.ecommerceapp.screen.AuthScreen
import com.umar.ecommerceapp.screen.HomeScreen
import com.umar.ecommerceapp.screen.LoginScreen
import com.umar.ecommerceapp.screen.SignUpScreen

// Global Navigation Singleton
object GlobalNavigation {
    lateinit var navController: NavHostController
}

@Composable
fun AppNavigation(navController: NavHostController) {
    // Inisialisasi Global Navigation
    GlobalNavigation.navController = navController
    
    // Conditional Navigation: Cek apakah user sudah login
    val isUserLoggedIn = Firebase.auth.currentUser != null
    val startDestination = if (isUserLoggedIn) "home" else "auth"
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Rute Auth Screen
        composable("auth") {
            AuthScreen(navController = navController)
        }

        // Rute Login Screen
        composable("login") {
            LoginScreen(navController = navController)
        }

        // Rute Sign Up Screen
        composable("signup") {
            SignUpScreen(navController = navController)
        }
        
        // Rute Home Screen
        composable("home") {
            HomeScreen(navController = navController)
        }
        
        // Rute Category Products dengan argumen
        composable(
            route = "category_products/{categoryId}",
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            CategoryProductsPage(categoryId = categoryId)
        }
    }
}
