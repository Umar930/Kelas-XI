package com.umar.listdemoapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umar.listdemoapp.ui.theme.ListDemoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListDemoAppTheme {
                // Menggunakan hanya LazyColumn untuk menyederhanakan tampilan
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        item {
                            Text(
                                text = "LazyColumn Demo (Efisien)",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                            
                            // Memisahkan komponen menjadi bagian-bagian yang lebih kecil
                            DemoListVertical()
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            Divider()
                            
                            Text(
                                text = "LazyRow Demo (Horizontal)",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        
                        item {
                            // Demonstrasi LazyRow
                            DemoListHorizontal()
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            Divider()
                            
                            Text(
                                text = "Column Demo (Tidak Efisien)",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        
                        item {
                            // Demonstrasi Column biasa (tidak efisien)
                            SimpleColumnItems()
                        }
                    }
                }
            }
        }
    }
}

/**
 * MarvelItemRow adalah komponen UI untuk menampilkan satu baris karakter Marvel
 * dengan gambar avatar, nama karakter, dan nama asli aktor.
 * 
 * @param item Data karakter Marvel yang akan ditampilkan
 */
@Composable
fun MarvelItemRow(item: MarvelChar) {
    val context = LocalContext.current
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable {
                // Menampilkan toast ketika item diklik
                Toast.makeText(context, "Aktor: ${item.name}", Toast.LENGTH_SHORT).show()
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Bagian 1: Gambar (Avatar)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(colorFromDrawable(item.imageResource))
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Bagian 2: Teks (Informasi Karakter)
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.characterName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                
                Text(
                    text = item.name,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

// Fungsi helper untuk mengekstrak warna dari drawable
@Composable
fun colorFromDrawable(drawableId: Int): Color {
    return when (drawableId) {
        R.drawable.ironman -> Color.Red
        R.drawable.captain_america -> Color.Blue
        R.drawable.thor -> Color(0xFFCCCC00)
        R.drawable.hulk -> Color.Green
        R.drawable.black_widow -> Color.Black
        R.drawable.hawkeye -> Color(0xFF6F4E37)
        R.drawable.spiderman -> Color.Red
        R.drawable.black_panther -> Color.DarkGray
        R.drawable.doctor_strange -> Color(0xFF660099)
        R.drawable.captain_marvel -> Color.Blue
        R.drawable.antman -> Color.Gray
        R.drawable.vision -> Color(0xFFAA0088)
        R.drawable.scarlet_witch -> Color(0xFFFF0088)
        R.drawable.loki -> Color(0xFF006600)
        R.drawable.winter_soldier -> Color.Gray
        else -> Color.LightGray
    }
}

/**
 * DemoListVertical menampilkan daftar karakter Marvel menggunakan LazyColumn
 * yang hanya me-render item yang terlihat di layar (seperti RecyclerView)
 */
@Composable
fun DemoListVertical() {
    // Mendapatkan data karakter Marvel
    val marvelChars = getAllMarvelChars()
    
    // LazyColumn hanya me-render item yang terlihat di layar dan mendaur ulang komponen
    // ketika di-scroll, mirip dengan RecyclerView, sehingga lebih efisien daripada
    // menggunakan Column dengan Modifier.verticalScroll()
    LazyColumn(
        modifier = Modifier.height(300.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    ) {
        items(marvelChars) { item ->
            MarvelItemRow(item = item)
        }
    }
}

/**
 * DemoListHorizontal menampilkan daftar karakter Marvel secara horizontal
 * menggunakan LazyRow
 */
@Composable
fun DemoListHorizontal() {
    // Mendapatkan data karakter Marvel
    val marvelChars = getAllMarvelChars()
    
    // LazyRow hanya me-render item yang terlihat di layar dan mendaur ulang komponen
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(marvelChars) { item ->
            MarvelCardHorizontal(item = item)
        }
    }
}

/**
 * SimpleColumnItems menampilkan daftar karakter Marvel menggunakan Column biasa
 * Semua item di-render sekaligus meskipun tidak terlihat di layar
 */
@Composable
fun SimpleColumnItems() {
    // Mendapatkan data karakter Marvel (hanya 5 item untuk demo)
    val marvelChars = getAllMarvelChars().take(3)
    
    Column(
        modifier = Modifier
            .height(300.dp)
            .padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        marvelChars.forEach { item ->
            MarvelItemRow(item = item)
        }
    }
}

/**
 * MarvelCardHorizontal adalah komponen UI untuk menampilkan kartu Marvel
 * dalam orientasi horizontal untuk LazyRow
 */
@Composable
fun MarvelCardHorizontal(item: MarvelChar) {
    val context = LocalContext.current
    
    Card(
        modifier = Modifier
            .width(120.dp)
            .padding(4.dp)
            .clickable {
                Toast.makeText(context, "${item.characterName} diperankan oleh ${item.name}", Toast.LENGTH_SHORT).show()
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(colorFromDrawable(item.imageResource))
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = item.characterName,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1
            )
            
            Text(
                text = item.name,
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MarvelItemRowPreview() {
    ListDemoAppTheme {
        MarvelItemRow(
            item = MarvelChar(
                "Iron Man", 
                "Robert Downey Jr.", 
                R.drawable.ironman
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DemoListVerticalPreview() {
    ListDemoAppTheme {
        DemoListVertical()
    }
}

@Preview(showBackground = true)
@Composable
fun DemoListHorizontalPreview() {
    ListDemoAppTheme {
        DemoListHorizontal()
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleColumnItemsPreview() {
    ListDemoAppTheme {
        SimpleColumnItems()
    }
}