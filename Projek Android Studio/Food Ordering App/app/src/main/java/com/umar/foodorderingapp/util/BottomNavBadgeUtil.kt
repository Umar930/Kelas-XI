package com.umar.foodorderingapp.util

import android.annotation.SuppressLint
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Utilitas untuk menampilkan badge notifikasi di BottomNavigationView
 */
object BottomNavBadgeUtil {
    
    /**
     * Fungsi untuk menampilkan badge notifikasi
     * @param bottomNav BottomNavigationView
     * @param menuItemId ID menu item yang akan ditampilkan badge
     * @param count Jumlah notifikasi (0 untuk menghilangkan badge)
     */
    @SuppressLint("RestrictedApi")
    fun setBadgeCount(bottomNav: BottomNavigationView, menuItemId: Int, count: Int) {
        try {
            val badge = bottomNav.getOrCreateBadge(menuItemId)
            badge.isVisible = count > 0
            if (count > 0) {
                badge.number = count
            }
        } catch (e: Exception) {
            // Tangani error jika ada
            e.printStackTrace()
        }
    }
}