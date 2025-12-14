package com.umar.todoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umar.todoapp.model.Todo
import com.umar.todoapp.model.TodoManager

class TodoViewModel : ViewModel() {
    
    private val _todoList = MutableLiveData<List<Todo>>(emptyList())
    val todoList: LiveData<List<Todo>> = _todoList
    
    init {
        loadTodos()
    }
    
    fun loadTodos() {
        _todoList.value = TodoManager.getAllTodos().reversed()
    }
    
    fun addTodo(title: String) {
        if (title.isNotBlank()) {
            TodoManager.addTodo(title)
            loadTodos()
        }
    }
    
    fun deleteTodo(id: Int) {
        TodoManager.deleteTodo(id)
        loadTodos()
    }
}