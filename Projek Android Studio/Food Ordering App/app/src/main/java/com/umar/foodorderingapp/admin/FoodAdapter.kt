package com.umar.foodorderingapp.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umar.foodorderingapp.R
import com.umar.foodorderingapp.databinding.ItemFoodManageBinding
import com.umar.foodorderingapp.model.Food
import java.text.NumberFormat
import java.util.*

class FoodAdapter(
    private val foodList: List<Food>,
    private val onEditClickListener: (Food) -> Unit,
    private val onDeleteClickListener: (Food) -> Unit
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {
    
    // Currency formatter for IDR
    private val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemFoodManageBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return FoodViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(foodList[position])
    }
    
    override fun getItemCount(): Int = foodList.size
    
    inner class FoodViewHolder(private val binding: ItemFoodManageBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(food: Food) {
            // Bind data to views
            binding.tvFoodName.text = food.name
            binding.tvFoodPrice.text = formatter.format(food.price)
            binding.tvFoodCategory.text = food.category
            
            // Load image
            Glide.with(binding.root.context)
                .load(food.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(binding.ivFoodImage)
            
            // Set click listeners
            binding.btnEdit.setOnClickListener {
                onEditClickListener(food)
            }
            
            binding.btnDelete.setOnClickListener {
                onDeleteClickListener(food)
            }
        }
    }
}