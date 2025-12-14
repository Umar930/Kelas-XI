package com.umar.aplikasimonitoringkelas.navigation

/**
 * Object untuk mendefinisikan semua route navigasi aplikasi
 * Mencegah typo pada string route
 */
object Screens {
    const val LOGIN = "route.login"
    const val MAIN_DASHBOARD = "route.main_dashboard/{role}"
    const val GURU_LIST = "route.guru_list"
    const val SISWA_LIST = "route.siswa_list"
    const val KELAS_LIST = "route.kelas_list"
    
    /**
     * Function untuk membuat route dashboard dengan parameter role
     */
    fun createDashboardRoute(role: String): String {
        return "route.main_dashboard/$role"
    }
}
