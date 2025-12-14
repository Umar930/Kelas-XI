package com.umar.bottomnavigation.ui.data

import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val label: String, // Teks label item
    val icon: ImageVector, // Icon item (Gunakan Icons.Default)
    val badgeCount: Int = 0 // Jumlah badge, default 0
)