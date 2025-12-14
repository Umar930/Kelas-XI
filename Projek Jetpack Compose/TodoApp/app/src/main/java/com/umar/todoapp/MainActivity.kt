package com.umar.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.umar.todoapp.data.local.TodoDataStore
import com.umar.todoapp.data.repository.TodoRepository
import com.umar.todoapp.ui.TodoListPage
import com.umar.todoapp.ui.theme.TodoAppTheme
import com.umar.todoapp.ui.viewmodel.TodoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inisialisasi komponen DataStore
        val todoDataStore = TodoDataStore(applicationContext)
        val repository = TodoRepository(todoDataStore)
        val factory = TodoViewModel.Factory(repository)
        val viewModel = ViewModelProvider(this, factory)[TodoViewModel::class.java]
        
        setContent {
            TodoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        TodoListPage(viewModel)
                    }
                }
            }
        }
    }
}