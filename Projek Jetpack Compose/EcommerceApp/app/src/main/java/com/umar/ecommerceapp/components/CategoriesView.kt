package com.umar.ecommerceapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.umar.ecommerceapp.model.CategoryModel

@Composable
fun CategoriesView() {
    val categoryList = remember { mutableStateOf(emptyList<CategoryModel>()) }
    
    // Pengambilan data kategori dari Firestore
    LaunchedEffect(Unit) {
        FirebaseFirestore.getInstance()
            .collection("data")
            .document("stock")
            .collection("categories")
            .get()
            .addOnSuccessListener { task ->
                val categories = task.documents.mapNotNull { document ->
                    document.toObject(CategoryModel::class.java)?.copy(id = document.id)
                }
                categoryList.value = categories
            }
            .addOnFailureListener { exception ->
                // Handle error
                println("Error fetching categories: ${exception.message}")
            }
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        // Judul Kategori
        Text(
            text = "Categories",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        // LazyRow untuk daftar kategori horizontal
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(categoryList.value) { category ->
                CategoryItem(category = category)
            }
        }
    }
}
