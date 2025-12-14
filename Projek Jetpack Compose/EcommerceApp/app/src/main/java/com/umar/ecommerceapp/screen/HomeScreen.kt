package com.umar.ecommerceapp.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.umar.ecommerceapp.model.NavItem
import com.umar.ecommerceapp.pages.CartPage
import com.umar.ecommerceapp.pages.FavoritePage
import com.umar.ecommerceapp.pages.HomePage
import com.umar.ecommerceapp.pages.ProfilePage

@Composable
fun HomeScreen(navController: NavHostController) {
    // Daftar Item Navigasi
    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Favorite", Icons.Default.Favorite),
        NavItem("Cart", Icons.Default.ShoppingCart),
        NavItem("Profile", Icons.Default.Person)
    )
    
    // State Item Terpilih
    val selectedIndex = remember { mutableStateOf(0) }
    
    // Scaffold dengan BottomNavigationBar
    Scaffold(
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = index == selectedIndex.value,
                        onClick = {
                            selectedIndex.value = index
                        },
                        icon = {
                            Icon(
                                imageVector = navItem.icon,
                                contentDescription = navItem.label
                            )
                        },
                        label = {
                            Text(text = navItem.label)
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        // Content Screen
        ContentScreen(
            selectedIndex = selectedIndex.value,
            paddingValues = paddingValues,
            navController = navController
        )
    }
}

@Composable
fun ContentScreen(
    selectedIndex: Int,
    paddingValues: PaddingValues,
    navController: NavHostController
) {
    when (selectedIndex) {
        0 -> HomePage()
        1 -> FavoritePage()
        2 -> CartPage()
        3 -> ProfilePage(navController = navController)
    }
}
