package com.umar.bottomnavigation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.umar.bottomnavigation.ui.data.NavItem
import com.umar.bottomnavigation.ui.pages.HomePage
import com.umar.bottomnavigation.ui.pages.NotificationPage
import com.umar.bottomnavigation.ui.pages.SettingsPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    // Daftar item navigasi
    val navItemList = listOf(
        NavItem(
            label = "Home",
            icon = Icons.Default.Home,
            badgeCount = 0
        ),
        NavItem(
            label = "Notifications",
            icon = Icons.Default.Notifications,
            badgeCount = 5
        ),
        NavItem(
            label = "Settings",
            icon = Icons.Default.Settings,
            badgeCount = 0
        )
    )
    
    // Menyimpan index item yang dipilih
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) } // Nilai awal 0 (Home)
    
    // Scaffold layout
    Scaffold(
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            if (item.badgeCount > 0) {
                                BadgedBox(
                                    badge = {
                                        Badge { 
                                            Text(text = item.badgeCount.toString())
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.label
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            }
                        },
                        label = { Text(text = item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        // Tampilkan konten halaman sesuai dengan index yang dipilih
        ContentScreen(
            selectedIndex = selectedIndex,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun ContentScreen(selectedIndex: Int, modifier: Modifier = Modifier) {
    when (selectedIndex) {
        0 -> HomePage(modifier = modifier)
        1 -> NotificationPage(modifier = modifier)
        2 -> SettingsPage(modifier = modifier)
    }
}