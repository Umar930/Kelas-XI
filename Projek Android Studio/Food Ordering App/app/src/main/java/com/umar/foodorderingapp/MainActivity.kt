package com.umar.foodorderingapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import com.umar.foodorderingapp.cart.CartActivity
import com.umar.foodorderingapp.category.CategoryAdapter
import com.umar.foodorderingapp.databinding.ActivityMainBinding
import com.umar.foodorderingapp.food.FoodAdapter
import com.umar.foodorderingapp.model.Category
import com.umar.foodorderingapp.model.Food
import com.umar.foodorderingapp.profile.ProfileActivity
import com.umar.foodorderingapp.util.Constants
import com.umar.foodorderingapp.util.showToast

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var db: FirebaseFirestore
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var popularFoodAdapter: FoodAdapter
    private lateinit var recommendedFoodAdapter: FoodAdapter
    private lateinit var prefs: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize Firebase Analytics
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        
        // Initialize Firestore
        db = FirebaseFirestore.getInstance()
        
        // Initialize SharedPreferences
        prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE)
        
        setupRecyclerViews()
        setupClickListeners()
        loadData()
        
        // Tambahkan contoh badge notifikasi di menu Chat
        com.umar.foodorderingapp.util.BottomNavBadgeUtil.setBadgeCount(
            binding.bottomNav, 
            R.id.navigation_chat, 
            3
        )
    }
    
    private fun setupRecyclerViews() {
        // Setup Categories RecyclerView
        categoryAdapter = CategoryAdapter()
        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }
        
        // Setup Popular Food RecyclerView
        popularFoodAdapter = FoodAdapter(true)
        binding.rvPopularFood.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularFoodAdapter
        }
        
        // Setup Recommended Food RecyclerView
        recommendedFoodAdapter = FoodAdapter(false)
        binding.rvRecommendedFood.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = recommendedFoodAdapter
        }
    }
    
    private fun setupClickListeners() {
        // Cart button click
        binding.fabCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        
        // Profile button click
        binding.ivProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        
        // Bottom Navigation
        binding.bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    // Sudah di halaman Home
                    true
                }
                R.id.navigation_promo -> {
                    showToast("Halaman Promo sedang dalam pengembangan")
                    true
                }
                R.id.navigation_activity -> {
                    showToast("Halaman Aktivitas sedang dalam pengembangan")
                    true
                }
                R.id.navigation_chat -> {
                    showToast("Halaman Chat sedang dalam pengembangan")
                    true
                }
                else -> false
            }
        }
    }
    
    private fun loadData() {
        // Mencoba memuat dari Firestore terlebih dahulu
        try {
            // Load Categories
            db.collection(Constants.CATEGORIES_COLLECTION)
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {
                        val categories = result.map { document ->
                            document.toObject(Category::class.java)
                        }
                        categoryAdapter.submitList(categories)
                    } else {
                        // Jika data dari Firestore kosong, gunakan data sampel
                        loadSampleData()
                    }
                    binding.shimmerCategories.visibility = View.GONE
                    binding.rvCategories.visibility = View.VISIBLE
                }
                .addOnFailureListener {
                    // Jika gagal, gunakan data sampel
                    loadSampleData()
                    binding.shimmerCategories.visibility = View.GONE
                    binding.rvCategories.visibility = View.VISIBLE
                }
            
            // Load Popular Food
            db.collection(Constants.FOODS_COLLECTION)
                .whereEqualTo("isPopular", true)
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {
                        val popularFoods = result.map { document ->
                            document.toObject(Food::class.java)
                        }
                        popularFoodAdapter.submitList(popularFoods)
                    } else {
                        // Gunakan data sampel jika Firestore kosong
                        popularFoodAdapter.submitList(com.umar.foodorderingapp.data.SampleData.getPopularFoods())
                    }
                    binding.shimmerPopular.visibility = View.GONE
                    binding.rvPopularFood.visibility = View.VISIBLE
                }
                .addOnFailureListener {
                    // Gunakan data sampel jika gagal
                    popularFoodAdapter.submitList(com.umar.foodorderingapp.data.SampleData.getPopularFoods())
                    binding.shimmerPopular.visibility = View.GONE
                    binding.rvPopularFood.visibility = View.VISIBLE
                }
            
            // Load Recommended Food
            db.collection(Constants.FOODS_COLLECTION)
                .whereEqualTo("isRecommended", true)
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {
                        val recommendedFoods = result.map { document ->
                            document.toObject(Food::class.java)
                        }
                        recommendedFoodAdapter.submitList(recommendedFoods)
                    } else {
                        // Gunakan data sampel jika Firestore kosong
                        recommendedFoodAdapter.submitList(com.umar.foodorderingapp.data.SampleData.getRecommendedFoods())
                    }
                    binding.shimmerRecommended.visibility = View.GONE
                    binding.rvRecommendedFood.visibility = View.VISIBLE
                }
                .addOnFailureListener {
                    // Gunakan data sampel jika gagal
                    recommendedFoodAdapter.submitList(com.umar.foodorderingapp.data.SampleData.getRecommendedFoods())
                    binding.shimmerRecommended.visibility = View.GONE
                    binding.rvRecommendedFood.visibility = View.VISIBLE
                }
        } catch (e: Exception) {
            // Gunakan data sampel jika terjadi error
            loadSampleData()
        }
    }
    
    /**
     * Memuat data sampel jika data dari Firestore tidak tersedia
     */
    private fun loadSampleData() {
        categoryAdapter.submitList(com.umar.foodorderingapp.data.SampleData.categories)
        popularFoodAdapter.submitList(com.umar.foodorderingapp.data.SampleData.getPopularFoods())
        recommendedFoodAdapter.submitList(com.umar.foodorderingapp.data.SampleData.getRecommendedFoods())
        
        binding.shimmerCategories.visibility = View.GONE
        binding.rvCategories.visibility = View.VISIBLE
        binding.shimmerPopular.visibility = View.GONE
        binding.rvPopularFood.visibility = View.VISIBLE
        binding.shimmerRecommended.visibility = View.GONE
        binding.rvRecommendedFood.visibility = View.VISIBLE
    }
}