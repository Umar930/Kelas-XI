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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.umar.foodorderingapp.R
import com.umar.foodorderingapp.databinding.ActivityAddFoodBinding
import com.umar.foodorderingapp.model.Category
import com.umar.foodorderingapp.model.Food
import com.umar.foodorderingapp.util.Constants
import com.umar.foodorderingapp.util.showToast
import java.util.UUID

class AddFoodActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAddFoodBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var imageUri: Uri? = null
    private val categories = ArrayList<Category>()
    
    companion object {
        private const val REQUEST_IMAGE_PICK = 100
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize Firebase
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        
        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Tambah Menu Makanan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        // Setup click listeners
        setupClickListeners()
        
        // Load categories for spinner
        loadCategories()
    }
    
    private fun setupClickListeners() {
        // Image selection
        binding.ivFoodImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }
        
        // Save button
        binding.btnSave.setOnClickListener {
            if (validateInput()) {
                saveFoodItem()
            }
        }
    }
    
    private fun loadCategories() {
        binding.progressBar.visibility = View.VISIBLE
        
        db.collection(Constants.CATEGORIES_COLLECTION)
            .get()
            .addOnSuccessListener { snapshot ->
                binding.progressBar.visibility = View.GONE
                
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
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                showToast("Gagal memuat kategori: ${e.message}")
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
        
        if (imageUri == null) {
            showToast("Pilih gambar untuk makanan")
            isValid = false
        }
        
        if (categories.isEmpty()) {
            showToast("Tidak ada kategori yang tersedia. Tambahkan kategori terlebih dahulu.")
            isValid = false
        }
        
        return isValid
    }
    
    private fun saveFoodItem() {
        // Show loading
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSave.isEnabled = false
        
        // Upload image first
        imageUri?.let { uri ->
            val storageRef = storage.reference
            val imageName = "foods/${UUID.randomUUID()}.jpg"
            val imageRef = storageRef.child(imageName)
            
            imageRef.putFile(uri)
                .addOnSuccessListener { taskSnapshot ->
                    // Get download URL
                    imageRef.downloadUrl
                        .addOnSuccessListener { downloadUri ->
                            // Create food object with image URL
                            saveFoodToFirestore(downloadUri.toString())
                        }
                        .addOnFailureListener { e: Exception ->
                            binding.progressBar.visibility = View.GONE
                            binding.btnSave.isEnabled = true
                            showToast("Gagal mendapatkan URL gambar: ${e.message}")
                        }
                }
                .addOnFailureListener { e: Exception ->
                    binding.progressBar.visibility = View.GONE
                    binding.btnSave.isEnabled = true
                    showToast("Gagal mengunggah gambar: ${e.message}")
                }
        }
    }
    
    private fun saveFoodToFirestore(imageUrl: String) {
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
        
        // Generate document ID
        val foodId = db.collection(Constants.FOODS_COLLECTION).document().id
        
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
        
        // Save to Firestore
        db.collection(Constants.FOODS_COLLECTION)
            .document(foodId)
            .set(food)
            .addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                binding.btnSave.isEnabled = true
                
                showToast("Makanan berhasil ditambahkan")
                finish()
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                binding.btnSave.isEnabled = true
                
                showToast("Gagal menyimpan data makanan: ${e.message}")
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