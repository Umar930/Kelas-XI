package com.umar.warunggo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umar.warunggo.data.model.CartItem
import com.umar.warunggo.data.model.Product
import com.umar.warunggo.ui.theme.WarungGoTheme

/**
 * Cart item row component
 * Displays product in cart with quantity controls
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartItemRow(
    cartItem: CartItem,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(true) }
    
    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Product info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = cartItem.product.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = cartItem.product.formattedPrice,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Subtotal
                    Text(
                        text = cartItem.getFormattedSubtotal(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Quantity controls
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Quantity controller
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 2.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            // Decrease button
                            IconButton(
                                onClick = {
                                    if (cartItem.quantity > 1) {
                                        onDecreaseQuantity()
                                    } else {
                                        isVisible = false
                                        onRemove()
                                    }
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (cartItem.quantity > 1) Icons.Default.Remove else Icons.Default.Delete,
                                    contentDescription = if (cartItem.quantity > 1) "Kurangi" else "Hapus",
                                    tint = if (cartItem.quantity > 1) 
                                        MaterialTheme.colorScheme.primary 
                                    else 
                                        MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            
                            // Quantity text
                            Text(
                                text = cartItem.quantity.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .widthIn(min = 32.dp)
                                    .padding(horizontal = 8.dp)
                            )
                            
                            // Increase button
                            IconButton(
                                onClick = onIncreaseQuantity,
                                enabled = cartItem.canIncreaseQuantity(),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Tambah",
                                    tint = if (cartItem.canIncreaseQuantity()) 
                                        MaterialTheme.colorScheme.primary 
                                    else 
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    
                    // Stock warning
                    if (!cartItem.canIncreaseQuantity()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Stok maksimal",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Delete button
                IconButton(
                    onClick = {
                        isVisible = false
                        onRemove()
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

/**
 * Empty cart state
 */
@Composable
fun EmptyCartState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Empty icon
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            modifier = Modifier.size(120.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🛒",
                    style = MaterialTheme.typography.displayLarge
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Belum Ada Produk",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Cari dan tambahkan produk\nuntuk memulai transaksi",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

// ============================================
// PREVIEW
// ============================================

@Preview(showBackground = true)
@Composable
private fun CartItemRowPreview() {
    WarungGoTheme {
        CartItemRow(
            cartItem = CartItem(
                product = Product(
                    id = "P001",
                    name = "Indomie Goreng",
                    price = 3500.0,
                    stock = 50,
                    category = com.umar.warunggo.data.model.ProductCategory.MAKANAN
                ),
                quantity = 2
            ),
            onIncreaseQuantity = {},
            onDecreaseQuantity = {},
            onRemove = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyCartStatePreview() {
    WarungGoTheme {
        EmptyCartState()
    }
}
