package com.umar.todoapp.model

import java.time.Instant
import java.util.Date

object TodoManager {
    private val todoList = mutableListOf<Todo>()
    
    fun getAllTodos(): List<Todo> {
        return todoList.toList()
    }
    
    fun addTodo(title: String) {
        val todo = Todo(
            id = System.currentTimeMillis().toInt(),
            title = title,
            createdAt = Date.from(Instant.now())
        )
        todoList.add(todo)
    }
    
    fun deleteTodo(id: Int) {
        todoList.removeIf { it.id == id }
    }
}