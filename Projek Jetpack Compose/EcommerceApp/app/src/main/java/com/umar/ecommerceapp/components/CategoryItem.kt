package com.umar.ecommerceapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.umar.ecommerceapp.R
import com.umar.ecommerceapp.model.CategoryModel
import com.umar.ecommerceapp.navigation.GlobalNavigation

@Composable
fun CategoryItem(category: CategoryModel) {
    // Mapping kategori ke drawable icon
    val iconResource = when (category.id.lowercase()) {
        "electronics" -> R.drawable.ic_electronics
        "fashion" -> R.drawable.ic_fashion
        "sports" -> R.drawable.ic_sports
        "books" -> R.drawable.ic_books
        "toys" -> R.drawable.ic_toys
        "beauty" -> R.drawable.ic_beauty
        "food" -> R.drawable.ic_food
        else -> R.drawable.ic_category_default
    }
    
    // Ukuran gambar lebih besar untuk books, toys, beauty, food
    val imageSize = when (category.id.lowercase()) {
        "books", "toys", "beauty", "food" -> 60.dp
        else -> 50.dp
    }
    
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(120.dp)
            .clickable {
                // Navigasi ke halaman produk kategori dengan ID
                GlobalNavigation.navController.navigate("category_products/${category.id}")
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon Kategori - gunakan AsyncImage jika ada imageUrl, fallback ke drawable
            if (category.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = category.imageUrl,
                    contentDescription = category.name,
                    placeholder = painterResource(id = iconResource),
                    error = painterResource(id = iconResource),
                    modifier = Modifier
                        .size(imageSize)
                        .padding(4.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                AsyncImage(
                    model = iconResource,
                    contentDescription = category.name,
                    modifier = Modifier
                        .size(imageSize)
                        .padding(4.dp),
                    contentScale = ContentScale.Fit
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Nama Kategori
            Text(
                text = category.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
