package com.umar.foodorderingapp.data

import com.umar.foodorderingapp.model.Category
import com.umar.foodorderingapp.model.Food

/**
 * Kelas yang menyediakan data statis untuk aplikasi
 */
object SampleData {
    
    /**
     * Daftar kategori makanan dengan ikon lokal
     */
    val categories = listOf(
        Category(id = "1", name = "Makanan", imageUrl = "drawable/icon_makanan"),
        Category(id = "2", name = "Minuman", imageUrl = "drawable/icon_minuman"),
        Category(id = "3", name = "Snack", imageUrl = "drawable/icon_snack"),
        Category(id = "4", name = "Dessert", imageUrl = "drawable/icon_dessert")
    )
    
    /**
     * Daftar makanan
     */
    val foods = listOf(
        // Makanan Populer
        Food(
            id = "1",
            name = "Nasi Goreng Spesial",
            description = "Nasi goreng dengan telur, ayam, dan sayuran segar. Dimasak dengan bumbu rahasia.",
            price = 25000.0,
            imageUrl = "drawable/nasi_goreng",
            category = "Makanan",
            rating = 4.8f,
            isPopular = true,
            isRecommended = true
        ),
        Food(
            id = "2",
            name = "Mie Goreng Special",
            description = "Mie goreng dengan telur, bakso, dan sayuran segar. Dimasak dengan bumbu khas restoran kami.",
            price = 23000.0,
            imageUrl = "drawable/mie_goreng",
            category = "Makanan",
            rating = 4.5f,
            isPopular = true,
            isRecommended = false
        ),
        Food(
            id = "3",
            name = "Ayam Bakar",
            description = "Ayam bakar dengan bumbu khas Indonesia. Disajikan dengan sambal dan lalapan.",
            price = 30000.0,
            imageUrl = "drawable/ayam_bakar",
            category = "Makanan",
            rating = 4.7f,
            isPopular = true,
            isRecommended = true
        ),
        
        // Minuman Populer
        Food(
            id = "4",
            name = "Es Teh Manis",
            description = "Teh manis dingin dengan es batu. Minuman penyegar dahaga.",
            price = 8000.0,
            imageUrl = "drawable/es_teh",
            category = "Minuman",
            rating = 4.3f,
            isPopular = true,
            isRecommended = false
        ),
        Food(
            id = "5",
            name = "Es Jeruk",
            description = "Jeruk segar diperas dan disajikan dengan es batu. Menyegarkan dan kaya vitamin C.",
            price = 10000.0,
            imageUrl = "drawable/es_jeruk",
            category = "Minuman",
            rating = 4.4f,
            isPopular = true,
            isRecommended = false
        ),
        
        // Makanan Direkomendasikan
        Food(
            id = "6",
            name = "Sate Ayam",
            description = "10 tusuk sate ayam dengan bumbu kacang khas. Disajikan dengan lontong dan acar.",
            price = 28000.0,
            imageUrl = "drawable/sate_ayam",
            category = "Makanan",
            rating = 4.9f,
            isPopular = false,
            isRecommended = true
        ),
        Food(
            id = "7",
            name = "Soto Ayam",
            description = "Soto ayam dengan kuah bening, suwiran ayam, dan taburan bawang goreng. Disajikan dengan nasi putih.",
            price = 22000.0,
            imageUrl = "drawable/soto_ayam",
            category = "Makanan",
            rating = 4.6f,
            isPopular = false,
            isRecommended = true
        ),
        
        // Minuman Direkomendasikan
        Food(
            id = "8",
            name = "Es Kopi Susu",
            description = "Kopi susu dengan es batu. Kombinasi sempurna antara kopi robusta dan susu creamy.",
            price = 15000.0,
            imageUrl = "drawable/es_kopi",
            category = "Minuman",
            rating = 4.7f,
            isPopular = false,
            isRecommended = true
        ),
        
        // Snack
        Food(
            id = "9",
            name = "Kentang Goreng",
            description = "Kentang goreng renyah dengan taburan bumbu khusus. Disajikan dengan saus sambal dan mayonaise.",
            price = 18000.0,
            imageUrl = "drawable/kentang_goreng",
            category = "Snack",
            rating = 4.5f,
            isPopular = false,
            isRecommended = false
        ),
        
        // Dessert
        Food(
            id = "10",
            name = "Es Krim Coklat",
            description = "Es krim coklat premium dengan topping choco chips. Dessert yang sempurna untuk penutup makan.",
            price = 12000.0,
            imageUrl = "drawable/es_krim",
            category = "Dessert",
            rating = 4.8f,
            isPopular = false,
            isRecommended = false
        )
    )

    // Mendapatkan makanan populer
    fun getPopularFoods(): List<Food> {
        return foods.filter { it.isPopular }
    }

    // Mendapatkan makanan yang direkomendasikan
    fun getRecommendedFoods(): List<Food> {
        return foods.filter { it.isRecommended }
    }

    // Mendapatkan makanan berdasarkan kategori
    fun getFoodsByCategory(category: String): List<Food> {
        return foods.filter { it.category == category }
    }
}