package com.umar.learnnavigate

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

/**
 * Komponen navigasi utama aplikasi.
 * Mengatur semua rute dan navigasi antar layar.
 */
@Composable
fun MyAppNavigation() {
    // Inisialisasi NavController
    val navController = rememberNavController()
    
    // NavHost berisi semua destinasi navigasi
    NavHost(
        navController = navController,
        startDestination = Routes.ScreenA
    ) {
        // Definisi rute untuk Layar A
        composable(Routes.ScreenA) {
            ScreenA(navController)
        }
        
        // Definisi rute untuk Layar B dengan parameter "name"
        composable(
            route = "${Routes.ScreenB}/{name}",
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            // Mengambil parameter "name" dari arguments
            val name = backStackEntry.arguments?.getString("name")
            
            // Panggil ScreenB dan berikan nilai name
            ScreenB(name)
        }
    }
}