package com.umar.todoapp.data.repository

import com.umar.todoapp.data.local.TodoDataStore
import com.umar.todoapp.data.local.entity.TodoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TodoRepository(private val todoDataStore: TodoDataStore) {

    // Mengekspos todos sebagai Flow dari DataStore
    val todos: Flow<List<TodoEntity>> = todoDataStore.getAllTodos()

    suspend fun insert(todo: TodoEntity) {
        withContext(Dispatchers.IO) {
            todoDataStore.insertTodo(todo)
        }
    }

    suspend fun delete(todo: TodoEntity) {
        withContext(Dispatchers.IO) {
            todoDataStore.deleteTodo(todo)
        }
    }
}