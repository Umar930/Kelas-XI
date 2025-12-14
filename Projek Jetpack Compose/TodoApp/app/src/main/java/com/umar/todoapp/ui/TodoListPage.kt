package com.umar.todoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umar.todoapp.ui.components.TodoItem
import com.umar.todoapp.ui.viewmodel.TodoViewModel

@Composable
fun TodoListPage(viewModel: TodoViewModel) {
    // Observe state from ViewModel using Flow
    val todoList by viewModel.todos.collectAsState()
    var inputTitle by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Input dan Tombol Add
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputTitle,
                onValueChange = { inputTitle = it },
                label = { Text("New Task") },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            )
            
            Button(
                onClick = {
                    viewModel.addTodo(inputTitle)
                    inputTitle = ""
                }
            ) {
                Text("Add")
            }
        }
        
        // Daftar Tugas
        if (todoList.isEmpty()) {
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("No items yet")
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).padding(top = 16.dp)
            ) {
                items(todoList) { item ->
                    TodoItem(
                        item = item,
                        onDelete = { todo -> viewModel.deleteTodo(todo) }
                    )
                }
            }
        }
    }
}