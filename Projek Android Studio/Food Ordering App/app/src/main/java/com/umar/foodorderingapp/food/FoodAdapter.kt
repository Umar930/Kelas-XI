package com.umar.foodorderingapp.food

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umar.foodorderingapp.databinding.ItemFoodHorizontalBinding
import com.umar.foodorderingapp.databinding.ItemFoodVerticalBinding
import com.umar.foodorderingapp.model.Food
import java.text.NumberFormat
import java.util.Locale

class FoodAdapter(private val isHorizontal: Boolean) : 
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    private val foodList = mutableListOf<Food>()
    
    fun submitList(list: List<Food>) {
        foodList.clear()
        foodList.addAll(list)
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (isHorizontal) {
            val binding = ItemFoodHorizontalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            HorizontalFoodViewHolder(binding)
        } else {
            val binding = ItemFoodVerticalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            VerticalFoodViewHolder(binding)
        }
    }
    
    override fun getItemCount(): Int = foodList.size
    
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val food = foodList[position]
        if (isHorizontal) {
            (holder as HorizontalFoodViewHolder).bind(food)
        } else {
            (holder as VerticalFoodViewHolder).bind(food)
        }
    }
    
    inner class HorizontalFoodViewHolder(private val binding: ItemFoodHorizontalBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(food: Food) {
            binding.tvFoodName.text = food.name
            binding.tvFoodPrice.text = formatPrice(food.price)
            binding.ratingBar.rating = food.rating
            
            // Load image with Glide - handle both URL and drawable resources
            try {
                if (food.imageUrl.startsWith("drawable/")) {
                    val resourceId = binding.root.context.resources.getIdentifier(
                        food.imageUrl.removePrefix("drawable/"), 
                        "drawable", 
                        binding.root.context.packageName
                    )
                    Glide.with(binding.root.context)
                        .load(resourceId)
                        .placeholder(com.umar.foodorderingapp.R.drawable.placeholder_image)
                        .error(com.umar.foodorderingapp.R.drawable.placeholder_image)
                        .fitCenter()
                        .into(binding.ivFoodImage)
                } else {
                    Glide.with(binding.root.context)
                        .load(food.imageUrl)
                        .placeholder(com.umar.foodorderingapp.R.drawable.placeholder_image)
                        .error(com.umar.foodorderingapp.R.drawable.placeholder_image)
                        .centerCrop()
                        .into(binding.ivFoodImage)
                }
            } catch (e: Exception) {
                binding.ivFoodImage.setImageResource(com.umar.foodorderingapp.R.drawable.placeholder_image)
            }
            
            binding.root.setOnClickListener {
                navigateToFoodDetail(food)
            }
        }
        
        private fun navigateToFoodDetail(food: Food) {
            val context = binding.root.context
            val intent = Intent(context, FoodDetailActivity::class.java)
            intent.putExtra("FOOD_ID", food.id)
            context.startActivity(intent)
        }
    }
    
    inner class VerticalFoodViewHolder(private val binding: ItemFoodVerticalBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(food: Food) {
            binding.tvFoodName.text = food.name
            binding.tvFoodDescription.text = food.description
            binding.tvFoodPrice.text = formatPrice(food.price)
            binding.ratingBar.rating = food.rating
            
            // Load image with Glide - handle both URL and drawable resources
            try {
                if (food.imageUrl.startsWith("drawable/")) {
                    val resourceId = binding.root.context.resources.getIdentifier(
                        food.imageUrl.removePrefix("drawable/"), 
                        "drawable", 
                        binding.root.context.packageName
                    )
                    Glide.with(binding.root.context)
                        .load(resourceId)
                        .placeholder(com.umar.foodorderingapp.R.drawable.placeholder_image)
                        .error(com.umar.foodorderingapp.R.drawable.placeholder_image)
                        .fitCenter()
                        .into(binding.ivFoodImage)
                } else {
                    Glide.with(binding.root.context)
                        .load(food.imageUrl)
                        .placeholder(com.umar.foodorderingapp.R.drawable.placeholder_image)
                        .error(com.umar.foodorderingapp.R.drawable.placeholder_image)
                        .fitCenter()
                        .into(binding.ivFoodImage)
                }
            } catch (e: Exception) {
                binding.ivFoodImage.setImageResource(com.umar.foodorderingapp.R.drawable.placeholder_image)
            }
            
            binding.root.setOnClickListener {
                navigateToFoodDetail(food)
            }
        }
        
        private fun navigateToFoodDetail(food: Food) {
            val context = binding.root.context
            val intent = Intent(context, FoodDetailActivity::class.java)
            intent.putExtra("FOOD_ID", food.id)
            context.startActivity(intent)
        }
    }
    
    companion object {
        fun formatPrice(price: Double): String {
            val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            return format.format(price)
        }
    }
}