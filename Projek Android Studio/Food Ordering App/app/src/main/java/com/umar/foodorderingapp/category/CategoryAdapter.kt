package com.umar.foodorderingapp.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umar.foodorderingapp.databinding.ItemCategoryBinding
import com.umar.foodorderingapp.model.Category

class CategoryAdapter : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CategoryViewHolder(private val binding: ItemCategoryBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(category: Category) {
            binding.tvCategoryName.text = category.name
            
            try {
                // Load image with Glide - handle both URL and drawable resources
                if (category.imageUrl.startsWith("drawable/")) {
                    val resourceId = binding.root.context.resources.getIdentifier(
                        category.imageUrl.removePrefix("drawable/"), 
                        "drawable", 
                        binding.root.context.packageName
                    )
                    Glide.with(binding.root.context)
                        .load(resourceId)
                        .fitCenter()
                        .into(binding.ivCategoryImage)
                } else {
                    Glide.with(binding.root.context)
                        .load(category.imageUrl)
                        .fitCenter()
                        .into(binding.ivCategoryImage)
                }
            } catch (e: Exception) {
                // Handle error - load placeholder
                binding.ivCategoryImage.setImageResource(com.umar.foodorderingapp.R.drawable.placeholder_image)
            }
            
            binding.root.setOnClickListener {
                // TODO: Navigate to category details or filter by category
            }
        }
    }

    class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }
}
