package com.umar.foodorderingapp.cart

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.umar.foodorderingapp.databinding.ActivityCartBinding
import com.umar.foodorderingapp.food.FoodAdapter.Companion.formatPrice
import com.umar.foodorderingapp.model.CartItem
import com.umar.foodorderingapp.util.showToast

class CartActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter
    private val cartItems = mutableListOf<CartItem>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        setupCartRecyclerView()
        setupClickListeners()
        checkCartEmpty()
        updateTotalPrice()
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    
    private fun setupCartRecyclerView() {
        cartAdapter = CartAdapter { item, newQuantity ->
            // Update quantity in our list
            val index = cartItems.indexOfFirst { it.food.id == item.food.id }
            if (index != -1) {
                cartItems[index].quantity = newQuantity
                updateTotalPrice()
            }
        }
        
        binding.rvCart.adapter = cartAdapter
        
        // For now, we'll just use fake data
        // In a real app, you would load this from a local database or SharedPreferences
        // loadCartItems()
    }
    
    private fun setupClickListeners() {
        binding.btnCheckout.setOnClickListener {
            if (cartItems.isEmpty()) {
                showToast("Keranjang kosong")
                return@setOnClickListener
            }
            
            // TODO: Navigate to checkout screen
            showToast("Fitur checkout akan segera hadir")
        }
    }
    
    private fun checkCartEmpty() {
        if (cartItems.isEmpty()) {
            binding.rvCart.visibility = View.GONE
            binding.tvEmptyCart.visibility = View.VISIBLE
            binding.cardView.visibility = View.GONE
        } else {
            binding.rvCart.visibility = View.VISIBLE
            binding.tvEmptyCart.visibility = View.GONE
            binding.cardView.visibility = View.VISIBLE
        }
    }
    
    private fun updateTotalPrice() {
        val total = cartItems.sumOf { it.food.price * it.quantity }
        binding.tvTotalPrice.text = formatPrice(total)
    }
}
