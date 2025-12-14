package com.umar.ecommerceapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

@Composable
fun HeaderView() {
    // State untuk menyimpan nama user
    val userName = remember { mutableStateOf("User") }
    
    // Ambil nama user dari Firestore
    LaunchedEffect(Unit) {
        val currentUser = Firebase.auth.currentUser
        currentUser?.uid?.let { uid ->
            Firebase.firestore.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val fullName = document.getString("name") ?: "User"
                        // Ambil nama depan saja
                        val firstName = fullName.split(" ").firstOrNull() ?: fullName
                        userName.value = firstName
                    }
                }
                .addOnFailureListener {
                    userName.value = "User"
                }
        }
    }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Kolom Teks Sapaan
        Column {
            Text(
                text = "Welcome Back",
                fontSize = 16.sp
            )
            Text(
                text = userName.value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        // Icon Search
        IconButton(
            onClick = {
                // TODO: Implement Search Navigation
            }
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        }
    }
}
