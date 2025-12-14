package com.umar.ecommerceapp.util

import android.content.Context
import android.widget.Toast

object AppUtil {
    /**
     * Menampilkan Toast message
     * @param context Context aplikasi
     * @param message Pesan yang akan ditampilkan
     */
    fun showToast(context: Context, message: String?) {
        Toast.makeText(context, message ?: "Unknown error", Toast.LENGTH_LONG).show()
    }
}
