package com.umar.warunggo.ui.product

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.umar.warunggo.data.model.ProductCategory
import com.umar.warunggo.ui.components.*

/**
 * Add/Edit Product Screen
 * Form for creating new products or editing existing ones
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(
    productId: String? = null,
    onNavigateBack: () -> Unit,
    viewModel: ProductViewModel = viewModel()
) {
    val formState by viewModel.formState.collectAsState()
    val snackbarHostState = androidx.compose.material3.SnackbarHostState()
    
    // Load product if editing
    LaunchedEffect(productId) {
        if (productId != null) {
            viewModel.loadProductForEdit(productId)
        } else {
            viewModel.resetForm()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(if (productId == null) "Tambah Produk" else "Edit Produk") 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Image Picker
            ImagePicker(
                imageUri = formState.imageUri,
                onImageSelected = viewModel::updateImage
            )
            
            // Product Name
            AppTextField(
                value = formState.name,
                onValueChange = viewModel::updateName,
                label = "Nama Produk",
                placeholder = "Contoh: Indomie Goreng",
                errorMessage = formState.nameError,
                isRequired = true
            )
            
            // Price
            AppTextField(
                value = formState.price,
                onValueChange = viewModel::updatePrice,
                label = "Harga",
                placeholder = "0",
                errorMessage = formState.priceError,
                isRequired = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                prefix = "Rp "
            )
            
            // Stock
            AppTextField(
                value = formState.stock,
                onValueChange = viewModel::updateStock,
                label = "Stok",
                placeholder = "0",
                errorMessage = formState.stockError,
                isRequired = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            
            // Category Dropdown
            DropdownField(
                value = formState.category,
                options = ProductCategory.entries,
                onValueChange = viewModel::updateCategory,
                label = "Kategori",
                optionLabel = { it.displayName },
                expanded = formState.showCategoryDropdown,
                onExpandedChange = { viewModel.toggleCategoryDropdown() }
            )
            
            // Description
            OutlinedTextField(
                value = formState.description,
                onValueChange = viewModel::updateDescription,
                label = { Text("Deskripsi (Opsional)") },
                placeholder = { Text("Tambahkan deskripsi produk...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5,
                supportingText = {
                    Text("${formState.description.length}/500")
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )
            
            // SKU (Optional)
            AppTextField(
                value = formState.sku,
                onValueChange = viewModel::updateSku,
                label = "SKU (Opsional)",
                placeholder = "Contoh: PROD-001",
                errorMessage = null
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Save Button
            PrimaryButton(
                text = if (productId == null) "Simpan Produk" else "Update Produk",
                onClick = {
                    viewModel.saveProduct {
                        onNavigateBack()
                    }
                },
                isLoading = formState.isSaving,
                enabled = formState.isValid && !formState.isSaving,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Enhanced AppTextField for the form
 */
@Composable
private fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    errorMessage: String?,
    isRequired: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    prefix: String? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Label
        Row {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            if (isRequired) {
                Text(
                    text = " *",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Text Field
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            prefix = prefix?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null,
            keyboardOptions = keyboardOptions,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        // Error Message
        AnimatedVisibility(visible = errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}
