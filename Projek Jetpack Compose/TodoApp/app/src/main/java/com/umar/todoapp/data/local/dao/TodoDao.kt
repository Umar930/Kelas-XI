package com.umar.todoapp.data.local.dao

import com.umar.todoapp.data.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

// Interface yang kompatibel dengan kode lama
// Sudah diimplementasikan dalam TodoDataStore
interface TodoDao {
    fun getAllTodos(): Flow<List<TodoEntity>>
    
    suspend fun insertTodo(todo: TodoEntity)
    
    suspend fun deleteTodo(todo: TodoEntity)
}