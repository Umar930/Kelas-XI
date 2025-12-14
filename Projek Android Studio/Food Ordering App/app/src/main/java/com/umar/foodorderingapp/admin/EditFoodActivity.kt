package com.umar.foodorderingapp.admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.umar.foodorderingapp.R
import com.umar.foodorderingapp.databinding.ActivityEditFoodBinding
import com.umar.foodorderingapp.model.Category
import com.umar.foodorderingapp.model.Food
import com.umar.foodorderingapp.util.Constants
import com.umar.foodorderingapp.util.showToast
import java.util.UUID

class EditFoodActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityEditFoodBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var imageUri: Uri? = null
    private var foodId: String = ""
    private var currentImageUrl: String = ""
    private val categories = ArrayList<Category>()
    
    companion object {
        private const val REQUEST_IMAGE_PICK = 100
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize Firebase
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        
        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Edit Menu Makanan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        // Get food ID from intent
        foodId = intent.getStringExtra("foodId") ?: ""
        if (foodId.isEmpty()) {
            showToast("Data makanan tidak valid")
            finish()
            return
        }
        
        // Setup click listeners
        setupClickListeners()
        
        // Load categories and food data
        loadCategories()
    }
    
    private fun setupClickListeners() {
        // Image selection
        binding.ivFoodImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }
        
        // Save button
        binding.btnUpdate.setOnClickListener {
            if (validateInput()) {
                updateFoodItem()
            }
        }
    }
    
    private fun loadCategories() {
        binding.progressBar.visibility = View.VISIBLE
        
        db.collection(Constants.CATEGORIES_COLLECTION)
            .get()
            .addOnSuccessListener { snapshot ->
                // Clear existing list
                categories.clear()
                
                // Add data from Firestore
                for (document in snapshot.documents) {
                    val category = document.toObject(Category::class.java)
                    category?.let {
                        categories.add(it)
                    }
                }
                
                // Setup spinner with category names
                val categoryNames = categories.map { it.name }
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    categoryNames
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerCategory.adapter = adapter
                
                // Load food data after categories are loaded
                loadFoodData()
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                showToast("Gagal memuat kategori: ${e.message}")
            }
    }
    
    private fun loadFoodData() {
        db.collection(Constants.FOODS_COLLECTION)
            .document(foodId)
            .get()
            .addOnSuccessListener { document ->
                binding.progressBar.visibility = View.GONE
                
                val food = document.toObject(Food::class.java)
                if (food != null) {
                    displayFoodData(food)
                } else {
                    showToast("Data makanan tidak ditemukan")
                    finish()
                }
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                showToast("Gagal memuat data makanan: ${e.message}")
                finish()
            }
    }
    
    private fun displayFoodData(food: Food) {
        // Set current image URL
        currentImageUrl = food.imageUrl
        
        // Display data in views
        binding.etFoodName.setText(food.name)
        binding.etFoodPrice.setText(food.price.toString())
        binding.etFoodDescription.setText(food.description)
        binding.cbPopular.isChecked = food.isPopular
        binding.cbRecommended.isChecked = food.isRecommended
        
        // Select category in spinner
        val categoryIndex = categories.indexOfFirst { it.name == food.category }
        if (categoryIndex != -1) {
            binding.spinnerCategory.setSelection(categoryIndex)
        }
        
        // Load image
        if (food.imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(food.imageUrl)
                .placeholder(R.drawable.placeholder_food)
                .error(R.drawable.placeholder_food)
                .into(binding.ivFoodImage)
            
            binding.tvSelectImage.visibility = View.GONE
        }
    }
    
    private fun validateInput(): Boolean {
        var isValid = true
        
        val name = binding.etFoodName.text.toString().trim()
        if (name.isEmpty()) {
            binding.tilFoodName.error = "Nama makanan tidak boleh kosong"
            isValid = false
        } else {
            binding.tilFoodName.error = null
        }
        
        val priceText = binding.etFoodPrice.text.toString().trim()
        if (priceText.isEmpty()) {
            binding.tilFoodPrice.error = "Harga tidak boleh kosong"
            isValid = false
        } else {
            binding.tilFoodPrice.error = null
        }
        
        val description = binding.etFoodDescription.text.toString().trim()
        if (description.isEmpty()) {
            binding.tilFoodDescription.error = "Deskripsi tidak boleh kosong"
            isValid = false
        } else {
            binding.tilFoodDescription.error = null
        }
        
        if (categories.isEmpty()) {
            showToast("Tidak ada kategori yang tersedia. Tambahkan kategori terlebih dahulu.")
            isValid = false
        }
        
        return isValid
    }
    
    private fun updateFoodItem() {
        // Show loading
        binding.progressBar.visibility = View.VISIBLE
        binding.btnUpdate.isEnabled = false
        
        // Check if image is changed
        if (imageUri != null) {
            // Upload new image
            val storageRef = storage.reference
            val imageName = "foods/${UUID.randomUUID()}.jpg"
            val imageRef = storageRef.child(imageName)
            
            imageRef.putFile(imageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    // Get download URL
                    imageRef.downloadUrl
                        .addOnSuccessListener { downloadUri ->
                            // Update food with new image URL
                            updateFoodInFirestore(downloadUri.toString())
                        }
                        .addOnFailureListener { e: Exception ->
                            binding.progressBar.visibility = View.GONE
                            binding.btnUpdate.isEnabled = true
                            showToast("Gagal mendapatkan URL gambar: ${e.message}")
                        }
                }
                .addOnFailureListener { e: Exception ->
                    binding.progressBar.visibility = View.GONE
                    binding.btnUpdate.isEnabled = true
                    showToast("Gagal mengunggah gambar: ${e.message}")
                }
        } else {
            // Update food with existing image URL
            updateFoodInFirestore(currentImageUrl)
        }
    }
    
    private fun updateFoodInFirestore(imageUrl: String) {
        // Get input values
        val name = binding.etFoodName.text.toString().trim()
        val price = binding.etFoodPrice.text.toString().toDoubleOrNull() ?: 0.0
        val description = binding.etFoodDescription.text.toString().trim()
        val categoryPosition = binding.spinnerCategory.selectedItemPosition
        val category = if (categoryPosition >= 0 && categoryPosition < categories.size) {
            categories[categoryPosition].name
        } else {
            ""
        }
        val isPopular = binding.cbPopular.isChecked
        val isRecommended = binding.cbRecommended.isChecked
        
        // Create Food object
        val food = Food(
            id = foodId,
            name = name,
            description = description,
            price = price,
            imageUrl = imageUrl,
            category = category,
            isPopular = isPopular,
            isRecommended = isRecommended
        )
        
        // Update in Firestore
        db.collection(Constants.FOODS_COLLECTION)
            .document(foodId)
            .set(food)
            .addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                binding.btnUpdate.isEnabled = true
                
                showToast("Makanan berhasil diperbarui")
                finish()
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                binding.btnUpdate.isEnabled = true
                
                showToast("Gagal memperbarui data makanan: ${e.message}")
            }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            // Get selected image URI
            imageUri = data.data
            
            // Display selected image
            binding.ivFoodImage.setImageURI(imageUri)
            binding.tvSelectImage.visibility = View.GONE
        }
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}