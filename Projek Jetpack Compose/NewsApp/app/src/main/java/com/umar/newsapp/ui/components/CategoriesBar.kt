package com.umar.newsapp.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umar.newsapp.viewmodel.NewsViewModel

/**
 * Daftar kategori berita yang dapat dipilih
 */
private val categories = listOf(
    "General", 
    "Business", 
    "Entertainment", 
    "Health", 
    "Science", 
    "Sports", 
    "Technology"
)

/**
 * Composable untuk menampilkan bar kategori yang dapat di-scroll horizontal
 */
@Composable
fun CategoriesBar(
    viewModel: NewsViewModel,
    modifier: Modifier = Modifier
) {
    val activeCategory by viewModel.activeCategory.observeAsState("general")
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        // Iterasi semua kategori
        categories.forEach { category ->
            // Periksa apakah kategori ini sedang aktif
            val isSelected = category.lowercase() == activeCategory
            
            Button(
                onClick = { viewModel.fetchTopHeadlines(category) },
                modifier = Modifier.padding(horizontal = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
                )
            ) {
                Text(text = category)
            }
            
            if (category != categories.last()) {
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}