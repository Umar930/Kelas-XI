package com.umar.warunggo.data.repository

import com.umar.warunggo.data.model.Product
import com.umar.warunggo.data.model.ProductCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/**
 * Product Repository - Singleton
 * Central data source for products across the app
 * Shared between ProductViewModel and TransactionViewModel
 */
object ProductRepository {
    
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()
    
    init {
        // Initialize with dummy data
        _products.value = generateDummyProducts()
    }
    
    /**
     * Get all products
     */
    fun getAllProducts(): List<Product> {
        return _products.value
    }
    
    /**
     * Get product by ID
     */
    fun getProductById(id: String): Product? {
        return _products.value.find { it.id == id }
    }
    
    /**
     * Add new product
     */
    fun addProduct(product: Product) {
        _products.value = _products.value + product
    }
    
    /**
     * Update existing product
     */
    fun updateProduct(product: Product) {
        _products.value = _products.value.map { 
            if (it.id == product.id) product else it 
        }
    }
    
    /**
     * Delete product
     */
    fun deleteProduct(productId: String) {
        _products.value = _products.value.filter { it.id != productId }
    }
    
    /**
     * Update product stock
     */
    fun updateStock(productId: String, newStock: Int) {
        _products.value = _products.value.map { product ->
            if (product.id == productId) {
                product.copy(stock = newStock)
            } else {
                product
            }
        }
    }
    
    /**
     * Reduce stock after transaction
     */
    fun reduceStock(productId: String, quantity: Int) {
        _products.value = _products.value.map { product ->
            if (product.id == productId) {
                product.copy(stock = (product.stock - quantity).coerceAtLeast(0))
            } else {
                product
            }
        }
    }
    
    /**
     * Search products by name
     */
    fun searchProducts(query: String): List<Product> {
        if (query.isBlank()) return _products.value
        return _products.value.filter { 
            it.name.contains(query, ignoreCase = true)
        }
    }
    
    /**
     * Filter products by category
     */
    fun filterByCategory(category: ProductCategory?): List<Product> {
        if (category == null) return _products.value
        return _products.value.filter { it.category == category }
    }
    
    /**
     * Get products with stock available
     */
    fun getAvailableProducts(): List<Product> {
        return _products.value.filter { it.stock > 0 }
    }
    
    /**
     * Generate dummy products for testing
     */
    private fun generateDummyProducts(): List<Product> {
        return listOf(
            Product(
                id = "1",
                name = "Indomie Goreng",
                price = 3500.0,
                stock = 150,
                category = ProductCategory.MAKANAN,
                description = "Mi instan rasa goreng"
            ),
            Product(
                id = "2",
                name = "Aqua 600ml",
                price = 3000.0,
                stock = 200,
                category = ProductCategory.MINUMAN,
                description = "Air mineral dalam kemasan"
            ),
            Product(
                id = "3",
                name = "Beras Premium 5kg",
                price = 75000.0,
                stock = 25,
                category = ProductCategory.SEMBAKO,
                description = "Beras kualitas premium"
            ),
            Product(
                id = "4",
                name = "Teh Botol Sosro",
                price = 4000.0,
                stock = 80,
                category = ProductCategory.MINUMAN,
                description = "Teh kemasan botol"
            ),
            Product(
                id = "5",
                name = "Sabun Lifebuoy",
                price = 5000.0,
                stock = 45,
                category = ProductCategory.KEBERSIHAN,
                description = "Sabun mandi batang"
            ),
            Product(
                id = "6",
                name = "Gula Pasir 1kg",
                price = 15000.0,
                stock = 40,
                category = ProductCategory.SEMBAKO,
                description = "Gula pasir putih"
            ),
            Product(
                id = "7",
                name = "Kopi Kapal Api",
                price = 12000.0,
                stock = 60,
                category = ProductCategory.MINUMAN,
                description = "Kopi bubuk 165g"
            ),
            Product(
                id = "8",
                name = "Minyak Goreng 2L",
                price = 32000.0,
                stock = 35,
                category = ProductCategory.SEMBAKO,
                description = "Minyak goreng kemasan 2 liter"
            ),
            Product(
                id = "9",
                name = "Susu Ultra 1L",
                price = 18000.0,
                stock = 50,
                category = ProductCategory.MINUMAN,
                description = "Susu UHT full cream"
            ),
            Product(
                id = "10",
                name = "Telur Ayam 1kg",
                price = 28000.0,
                stock = 30,
                category = ProductCategory.SEMBAKO,
                description = "Telur ayam negeri"
            ),
            Product(
                id = "11",
                name = "Kecap Bango 220ml",
                price = 11000.0,
                stock = 55,
                category = ProductCategory.SEMBAKO,
                description = "Kecap manis"
            ),
            Product(
                id = "12",
                name = "Shampo Clear",
                price = 18500.0,
                stock = 40,
                category = ProductCategory.KEBERSIHAN,
                description = "Shampo anti ketombe 170ml"
            ),
            Product(
                id = "13",
                name = "Pasta Gigi Pepsodent",
                price = 8500.0,
                stock = 65,
                category = ProductCategory.KEBERSIHAN,
                description = "Pasta gigi 150g"
            ),
            Product(
                id = "14",
                name = "Detergen Rinso 800g",
                price = 22000.0,
                stock = 30,
                category = ProductCategory.KEBERSIHAN,
                description = "Detergen bubuk"
            ),
            Product(
                id = "15",
                name = "Roti Tawar Sari Roti",
                price = 14000.0,
                stock = 25,
                category = ProductCategory.MAKANAN,
                description = "Roti tawar jumbo"
            ),
            Product(
                id = "16",
                name = "Mentega Blue Band",
                price = 16000.0,
                stock = 40,
                category = ProductCategory.MAKANAN,
                description = "Mentega 200g"
            ),
            Product(
                id = "17",
                name = "Terigu Segitiga Biru 1kg",
                price = 13000.0,
                stock = 45,
                category = ProductCategory.SEMBAKO,
                description = "Tepung terigu"
            ),
            Product(
                id = "18",
                name = "Sikat Gigi Formula",
                price = 6500.0,
                stock = 70,
                category = ProductCategory.KEBERSIHAN,
                description = "Sikat gigi medium"
            ),
            Product(
                id = "19",
                name = "Coca Cola 1.5L",
                price = 10000.0,
                stock = 60,
                category = ProductCategory.MINUMAN,
                description = "Minuman bersoda"
            ),
            Product(
                id = "20",
                name = "Biskuit Roma Kelapa",
                price = 9500.0,
                stock = 80,
                category = ProductCategory.MAKANAN,
                description = "Biskuit rasa kelapa"
            )
        )
    }
}
