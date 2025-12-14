package com.umar.foodorderingapp.admin

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.umar.foodorderingapp.databinding.ActivityManageFoodsBinding
import com.umar.foodorderingapp.model.Food
import com.umar.foodorderingapp.util.Constants
import com.umar.foodorderingapp.util.showToast

class ManageFoodsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityManageFoodsBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: FoodAdapter
    private val foodList = ArrayList<Food>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageFoodsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize Firestore
        db = FirebaseFirestore.getInstance()
        
        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Kelola Menu Makanan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        // Setup RecyclerView
        setupRecyclerView()
        
        // Setup click listeners
        setupClickListeners()
        
        // Load food data
        loadFoodData()
    }
    
    private fun setupRecyclerView() {
        adapter = FoodAdapter(foodList, 
            onEditClickListener = { food ->
                // Navigate to EditFoodActivity with food data
                val intent = Intent(this, EditFoodActivity::class.java)
                intent.putExtra("foodId", food.id)
                startActivity(intent)
            },
            onDeleteClickListener = { food ->
                // Delete food item
                deleteFoodItem(food)
            }
        )
        
        binding.rvFoods.layoutManager = LinearLayoutManager(this)
        binding.rvFoods.adapter = adapter
    }
    
    private fun setupClickListeners() {
        binding.fabAddFood.setOnClickListener {
            // Navigate to AddFoodActivity
            startActivity(Intent(this, AddFoodActivity::class.java))
        }
    }
    
    private fun loadFoodData() {
        // Show loading
        binding.shimmerViewContainer.visibility = View.VISIBLE
        binding.shimmerViewContainer.startShimmer()
        binding.rvFoods.visibility = View.GONE
        
        db.collection(Constants.FOODS_COLLECTION)
            .get()
            .addOnSuccessListener { snapshot ->
                // Stop shimmer and show RecyclerView
                binding.shimmerViewContainer.stopShimmer()
                binding.shimmerViewContainer.visibility = View.GONE
                binding.rvFoods.visibility = View.VISIBLE
                
                // Clear existing list
                foodList.clear()
                
                // Add data from Firestore
                for (document in snapshot.documents) {
                    val food = document.toObject(Food::class.java)
                    food?.let {
                        foodList.add(it)
                    }
                }
                
                // Update empty state visibility
                updateEmptyStateVisibility()
                
                // Notify adapter
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                // Stop shimmer and show RecyclerView
                binding.shimmerViewContainer.stopShimmer()
                binding.shimmerViewContainer.visibility = View.GONE
                binding.rvFoods.visibility = View.VISIBLE
                
                // Show error message
                showToast("Gagal memuat data: ${e.message}")
                
                // Update empty state visibility
                updateEmptyStateVisibility()
            }
    }
    
    private fun updateEmptyStateVisibility() {
        if (foodList.isEmpty()) {
            binding.emptyStateLayout.visibility = View.VISIBLE
            binding.rvFoods.visibility = View.GONE
        } else {
            binding.emptyStateLayout.visibility = View.GONE
            binding.rvFoods.visibility = View.VISIBLE
        }
    }
    
    private fun deleteFoodItem(food: Food) {
        // Show loading
        binding.progressBar.visibility = View.VISIBLE
        
        db.collection(Constants.FOODS_COLLECTION)
            .document(food.id)
            .delete()
            .addOnSuccessListener {
                // Hide loading
                binding.progressBar.visibility = View.GONE
                
                // Remove from list and notify adapter
                val position = foodList.indexOf(food)
                if (position != -1) {
                    foodList.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    updateEmptyStateVisibility()
                }
                
                showToast("Makanan berhasil dihapus")
            }
            .addOnFailureListener { e ->
                // Hide loading
                binding.progressBar.visibility = View.GONE
                
                showToast("Gagal menghapus makanan: ${e.message}")
            }
    }
    
    override fun onResume() {
        super.onResume()
        // Reload food data when returning to this activity
        loadFoodData()
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}