package com.umar.todoapp.data.local

import android.content.Context

// Dummy class untuk kompatibilitas dengan kode lama
// Akan dihapus saat migrasi selesai
class TodoDatabase private constructor() {
    
    companion object {
        @JvmStatic
        fun getDatabase(context: Context): TodoDataStore {
            return TodoDataStore(context)
        }
    }
}