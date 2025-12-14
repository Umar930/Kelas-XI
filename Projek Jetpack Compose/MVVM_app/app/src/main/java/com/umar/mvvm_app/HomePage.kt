package com.umar.mvvm_app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umar.mvvm_app.ui.theme.MVVM_appTheme

@Composable
fun HomePage(
    viewModel: NewsViewModel,
    modifier: Modifier = Modifier
) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Aplikasi Berita",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        
        // Theme switch
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Mode Gelap",
                style = MaterialTheme.typography.bodyLarge
            )
            
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { viewModel.setTheme(it) }
            )
        }
        
        Divider(Modifier.padding(vertical = 16.dp))
        
        // News content would go here
        Text(
            text = "Konten berita akan ditampilkan di sini",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePageScreenPreview() {
    // This is just a preview so we can't actually use the real ViewModel
    MVVM_appTheme {
        Surface {
            // In a real scenario, pass the actual viewModel
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Aplikasi Berita",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Mode Gelap",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    
                    Switch(
                        checked = false,
                        onCheckedChange = { }
                    )
                }
                
                Divider(Modifier.padding(vertical = 16.dp))
                
                Text(
                    text = "Konten berita akan ditampilkan di sini",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}