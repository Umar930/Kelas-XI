package com.umar.foodorderingapp.food

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.umar.foodorderingapp.databinding.ActivityFoodDetailBinding
import com.umar.foodorderingapp.food.FoodAdapter.Companion.formatPrice
import com.umar.foodorderingapp.model.CartItem
import com.umar.foodorderingapp.model.Food
import com.umar.foodorderingapp.util.Constants
import com.umar.foodorderingapp.util.showToast

class FoodDetailActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityFoodDetailBinding
    private lateinit var db: FirebaseFirestore
    private var foodId: String? = null
    private var food: Food? = null
    private var quantity = 1
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize Firestore
        db = FirebaseFirestore.getInstance()
        
        // Setup Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        
        // Get Food ID from Intent
        foodId = intent.getStringExtra("FOOD_ID")
        if (foodId == null) {
            showToast("Makanan tidak ditemukan")
            finish()
            return
        }
        
        loadFoodData()
        setupClickListeners()
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    
    private fun loadFoodData() {
        foodId?.let { id ->
            db.collection(Constants.FOODS_COLLECTION)
                .document(id)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        food = document.toObject(Food::class.java)
                        food?.let {
                            updateUI(it)
                        }
                    } else {
                        showToast("Makanan tidak ditemukan")
                        finish()
                    }
                }
                .addOnFailureListener {
                    showToast("Gagal memuat data makanan: ${it.message}")
                    finish()
                }
        }
    }
    
    private fun updateUI(food: Food) {
        binding.tvFoodName.text = food.name
        binding.tvFoodPrice.text = formatPrice(food.price)
        binding.tvFoodDescription.text = food.description
        binding.ratingBar.rating = food.rating
        binding.tvTotalPrice.text = formatPrice(food.price * quantity)
        
        // Load food image - handle both URL and drawable resources
        try {
            if (food.imageUrl.startsWith("drawable/")) {
                val resourceId = resources.getIdentifier(
                    food.imageUrl.removePrefix("drawable/"), 
                    "drawable", 
                    packageName
                )
                Glide.with(this)
                    .load(resourceId)
                    .centerCrop()
                    .into(binding.ivFoodImage)
            } else {
                Glide.with(this)
                    .load(food.imageUrl)
                    .centerCrop()
                    .into(binding.ivFoodImage)
            }
        } catch (e: Exception) {
            // Handle error
            binding.ivFoodImage.setImageResource(com.umar.foodorderingapp.R.drawable.placeholder_image)
        }
    }
    
    private fun setupClickListeners() {
        binding.btnDecrease.setOnClickListener {
            if (quantity > 1) {
                quantity--
                updateQuantityAndTotal()
            }
        }
        
        binding.btnIncrease.setOnClickListener {
            quantity++
            updateQuantityAndTotal()
        }
        
        binding.btnAddToCart.setOnClickListener {
            addToCart()
        }
    }
    
    private fun updateQuantityAndTotal() {
        binding.tvQuantity.text = quantity.toString()
        food?.let {
            binding.tvTotalPrice.text = formatPrice(it.price * quantity)
        }
    }
    
    private fun addToCart() {
        food?.let {
            // In a real app, you would add this to a local database or Firebase
            // For now, let's just show a toast
            val cartItem = CartItem(it, quantity)
            
            // TODO: Add to cart implementation
            // Here you would add the item to your cart storage
            
            showToast("Ditambahkan ke keranjang: ${it.name} (${quantity}x)")
            finish()
        }
    }
}
