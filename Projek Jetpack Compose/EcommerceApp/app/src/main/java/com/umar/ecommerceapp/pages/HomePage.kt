package com.umar.ecommerceapp.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umar.ecommerceapp.components.BannerView
import com.umar.ecommerceapp.components.CategoriesView
import com.umar.ecommerceapp.components.HeaderView

@Composable
fun HomePage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header dengan sapaan
        HeaderView()
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Banner swipeable
        BannerView()
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Daftar Kategori Horizontal
        CategoriesView()
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // TODO: Add more components here (Products, etc.)
    }
}
