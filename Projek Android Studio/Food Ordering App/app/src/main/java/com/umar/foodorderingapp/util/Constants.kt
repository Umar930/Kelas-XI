package com.umar.foodorderingapp.util

import android.content.Context
import android.widget.Toast

object Constants {
    // Firebase collections
    const val USERS_COLLECTION = "users"
    const val FOODS_COLLECTION = "foods"
    const val CATEGORIES_COLLECTION = "categories"
    const val ORDERS_COLLECTION = "orders"
    
    // Order status
    const val STATUS_PENDING = "Pending"
    const val STATUS_PROCESSING = "Processing"
    const val STATUS_ON_DELIVERY = "OnDelivery"
    const val STATUS_DELIVERED = "Delivered"
    
    // Shared preferences
    const val PREFS_NAME = "FoodOrderingAppPrefs"
    const val KEY_USER_ID = "user_id"
    const val KEY_IS_LOGGED_IN = "is_logged_in"
    const val KEY_IS_ADMIN = "is_admin"
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
