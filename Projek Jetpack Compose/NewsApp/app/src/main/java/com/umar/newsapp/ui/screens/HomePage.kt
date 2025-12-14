package com.umar.newsapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.umar.newsapp.model.Article
import com.umar.newsapp.ui.components.ArticleItem
import com.umar.newsapp.ui.components.CategoriesBar
import com.umar.newsapp.viewmodel.NewsViewModel

@Composable
fun HomePage(
    viewModel: NewsViewModel, 
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val articles by viewModel.articles.observeAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val error by viewModel.error.observeAsState(initial = null)
    
    // State untuk fitur pencarian
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isSearchExpanded by rememberSaveable { mutableStateOf(false) }
    
    Column(modifier = modifier.fillMaxSize()) {
        // App Title
        Text(
            text = "News Now",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        // Search UI - Expandable Search Bar
        if (isSearchExpanded) {
            // Tampilan Search Field Penuh
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                label = { Text("Cari berita...") },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (searchQuery.isNotBlank()) {
                                viewModel.fetchEverythingWithQuery(searchQuery)
                                isSearchExpanded = false
                                // Opsional: Reset query setelah search
                                // searchQuery = ""
                            }
                        }
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Cari")
                    }
                },
                shape = CircleShape,
                singleLine = true
            )
        } else {
            // Tampilan Icon Saja
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { isSearchExpanded = true },
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Tampilkan Pencarian"
                    )
                }
            }
        }
        
        // Categories Bar
        CategoriesBar(viewModel = viewModel)
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Content
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isLoading -> {
                    // Loading indicator
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                error != null -> {
                    // Error message
                    Text(
                        text = error ?: "Terjadi kesalahan",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                articles.isEmpty() -> {
                    // No articles available
                    Text(
                        text = "Tidak ada artikel tersedia",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                else -> {
                    // Articles list
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(articles) { article ->
                            ArticleItem(
                                article = article,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}