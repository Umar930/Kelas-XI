package com.umar.todoapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.umar.todoapp.data.local.dao.TodoDao
import com.umar.todoapp.data.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

// Extension property untuk DataStore
val Context.todoDataStore: DataStore<Preferences> by preferencesDataStore(name = "todo_preferences")

// Implementasi TodoDao menggunakan DataStore
class TodoDataStore(private val context: Context) : TodoDao {
    
    private val todoListKey = stringPreferencesKey("todo_list")
    
    // Implementasi TodoDao.getAllTodos()
    override fun getAllTodos(): Flow<List<TodoEntity>> = context.todoDataStore.data
        .map { preferences ->
            val jsonString = preferences[todoListKey] ?: "[]"
            parseTodoList(jsonString)
        }
    
    // Implementasi TodoDao.insertTodo()
    override suspend fun insertTodo(todo: TodoEntity) {
        context.todoDataStore.edit { preferences ->
            val currentList = preferences[todoListKey]?.let { parseTodoList(it) } ?: listOf()
            val newList = currentList + todo
            preferences[todoListKey] = serializeTodoList(newList)
        }
    }
    
    // Implementasi TodoDao.deleteTodo()
    override suspend fun deleteTodo(todo: TodoEntity) {
        context.todoDataStore.edit { preferences ->
            val currentList = preferences[todoListKey]?.let { parseTodoList(it) } ?: listOf()
            val newList = currentList.filter { it.id != todo.id }
            preferences[todoListKey] = serializeTodoList(newList)
        }
    }
    
    // Mengkonversi JSON string ke list TodoEntity
    private fun parseTodoList(jsonString: String): List<TodoEntity> {
        val result = mutableListOf<TodoEntity>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val todo = TodoEntity(
                    id = jsonObject.getInt("id"),
                    title = jsonObject.getString("title"),
                    createdAt = jsonObject.getLong("createdAt")
                )
                result.add(todo)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }
    
    // Mengkonversi list TodoEntity ke JSON string
    private fun serializeTodoList(todos: List<TodoEntity>): String {
        val jsonArray = JSONArray()
        todos.forEach { todo ->
            val jsonObject = JSONObject()
            jsonObject.put("id", todo.id)
            jsonObject.put("title", todo.title)
            jsonObject.put("createdAt", todo.createdAt)
            jsonArray.put(jsonObject)
        }
        return jsonArray.toString()
    }
}