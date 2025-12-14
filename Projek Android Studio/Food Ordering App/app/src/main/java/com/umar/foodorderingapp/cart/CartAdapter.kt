package com.umar.foodorderingapp.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umar.foodorderingapp.databinding.ItemCartBinding
import com.umar.foodorderingapp.food.FoodAdapter.Companion.formatPrice
import com.umar.foodorderingapp.model.CartItem

class CartAdapter(private val onQuantityChanged: (CartItem, Int) -> Unit) :
    ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItem) {
            val food = item.food
            
            binding.tvFoodName.text = food.name
            binding.tvPrice.text = formatPrice(food.price)
            binding.tvQuantity.text = item.quantity.toString()
            binding.tvSubtotal.text = formatPrice(food.price * item.quantity)
            
            // Load image with Glide
            Glide.with(binding.root.context)
                .load(food.imageUrl)
                .centerCrop()
                .into(binding.ivFoodImage)
            
            // Increase quantity button
            binding.btnIncrease.setOnClickListener {
                val newQuantity = item.quantity + 1
                binding.tvQuantity.text = newQuantity.toString()
                binding.tvSubtotal.text = formatPrice(food.price * newQuantity)
                onQuantityChanged(item, newQuantity)
            }
            
            // Decrease quantity button
            binding.btnDecrease.setOnClickListener {
                if (item.quantity > 1) {
                    val newQuantity = item.quantity - 1
                    binding.tvQuantity.text = newQuantity.toString()
                    binding.tvSubtotal.text = formatPrice(food.price * newQuantity)
                    onQuantityChanged(item, newQuantity)
                }
            }
        }
    }

    class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.food.id == newItem.food.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}
