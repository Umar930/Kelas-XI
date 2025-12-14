package com.umar.aplikasimonitoringkelas.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.umar.aplikasimonitoringkelas.KepalaSekolahActivity
import com.umar.aplikasimonitoringkelas.KurikulumActivity
import com.umar.aplikasimonitoringkelas.SiswaDashboardActivity
import com.umar.aplikasimonitoringkelas.data.session.SessionManager
import com.umar.aplikasimonitoringkelas.ui.screens.GuruListScreen
import com.umar.aplikasimonitoringkelas.ui.screens.KelasListScreen
import com.umar.aplikasimonitoringkelas.ui.screens.LoginScreen
import com.umar.aplikasimonitoringkelas.ui.screens.MainScreen
import com.umar.aplikasimonitoringkelas.ui.screens.SiswaListScreen

/**
 * Navigation Host untuk mengelola perpindahan layar dalam aplikasi
 * 
 * @param navController Controller untuk navigasi
 */
@Composable
fun AppNavHost(
    navController: NavHostController
) {
    val context = LocalContext.current
    val sessionManager = SessionManager.getInstance(context)
    
    // Cek apakah user sudah login
    val isLoggedIn by sessionManager.isLoggedIn().collectAsState(initial = false)
    val userRole by sessionManager.getUserRole().collectAsState(initial = null)
    
    // Handle redirect to XML activities for specific roles on app resume
    LaunchedEffect(isLoggedIn, userRole) {
        if (isLoggedIn && !userRole.isNullOrEmpty()) {
            when (userRole?.lowercase()) {
                "siswa" -> {
                    val intent = Intent(context, SiswaDashboardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                }
                "kurikulum" -> {
                    val intent = Intent(context, KurikulumActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                }
                "kepala_sekolah" -> {
                    val intent = Intent(context, KepalaSekolahActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                }
                // admin stays in Compose
            }
        }
    }
    
    // Tentukan start destination berdasarkan status login
    val startDestination = if (isLoggedIn && !userRole.isNullOrEmpty()) {
        Screens.createDashboardRoute(userRole ?: "Admin")
    } else {
        Screens.LOGIN
    }
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Layar Login
        composable(route = Screens.LOGIN) {
            LoginScreen(navController = navController)
        }
        
        // Layar Dashboard Utama dengan parameter role
        composable(
            route = Screens.MAIN_DASHBOARD,
            arguments = listOf(
                navArgument("role") {
                    type = NavType.StringType
                    defaultValue = "Admin"
                }
            )
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "Admin"
            MainScreen(
                navController = navController,
                role = role
            )
        }
        
        // Layar Daftar Guru
        composable(route = Screens.GURU_LIST) {
            GuruListScreen(navController = navController)
        }
        
        // Layar Daftar Siswa
        composable(route = Screens.SISWA_LIST) {
            SiswaListScreen(navController = navController)
        }
        
        // Layar Daftar Kelas
        composable(route = Screens.KELAS_LIST) {
            KelasListScreen(navController = navController)
        }
    }
}
