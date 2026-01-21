# 📦 Product Module - WarungGo

## Overview

Complete, production-ready inventory management system for UMKM businesses built with Jetpack Compose, Material 3, and MVVM architecture.

---

## 🎯 Features Implemented

### ✅ Product List Screen

- **Search Bar** - Real-time product search by name
- **Filter & Sort**
  - Filter by Category (Makanan, Minuman, Sembako, Kebersihan, Lainnya)
  - Filter by Stock Status (Available, Low Stock, Out of Stock)
  - Sort by Name (A-Z, Z-A)
  - Sort by Price (Low→High, High→Low)
  - Sort by Stock (High→Low, Low→High)
- **Product Cards**
  - Product image with placeholder
  - Name, price, stock, category badge
  - Color-coded stock indicators (Green/Yellow/Red)
  - Quick actions: Edit, Delete, Add Stock
- **Empty State** - When no products exist
- **Loading State** - During data fetching
- **Floating Action Button** - Add new product

### ✅ Add/Edit Product Screen

- **Image Picker** - Upload from gallery with preview
- **Form Fields**
  - Product Name (required, min 3 chars)
  - Price (required, numeric, formatted as Rupiah)
  - Stock (required, numeric, >= 0)
  - Category (dropdown selector)
  - Description (optional, max 500 chars)
  - SKU (optional)
- **Real-time Validation** - Instant error feedback
- **Loading State** - Disabled form during save
- **Auto Navigation** - Returns to list after save

### ✅ State Management

- `ProductUiState` - List screen state
- `ProductFormState` - Form screen state
- `ProductViewModel` - Business logic with StateFlow
- Immutable state pattern
- Event-driven updates

---

## 📂 File Structure

```
app/src/main/java/com/umar/warunggo/
├── data/
│   └── model/
│       └── Product.kt                    # Data model with Category & StockStatus enums
│
├── ui/
│   ├── product/
│   │   ├── ProductListScreen.kt          # Main product list with search/filter
│   │   ├── AddEditProductScreen.kt       # Add/Edit form with validation
│   │   ├── ProductUiState.kt             # List screen state
│   │   ├── ProductFormState.kt           # Form screen state
│   │   └── ProductViewModel.kt           # Business logic & state management
│   │
│   └── components/
│       ├── SearchBar.kt                  # Reusable search component
│       ├── FilterBottomSheet.kt          # Filter & sort modal
│       ├── ProductCard.kt                # Product list item
│       ├── ImagePicker.kt                # Image upload component
│       ├── DropdownField.kt              # Category selector
│       ├── EmptyState.kt                 # No products placeholder
│       ├── AppTextField.kt               # Text input (existing)
│       ├── PasswordField.kt              # Password input (existing)
│       └── PrimaryButton.kt              # Action button (existing)
│
└── navigation/
    ├── Screen.kt                          # Updated with AddProduct & EditProduct
    └── NavGraph.kt                        # Updated with product routes
```

---

## 🎨 Design System

### Colors (Dark Theme)

- **Primary**: `#2196F3` (Blue)
- **Success**: Green tint for high stock
- **Warning**: Orange tint for low stock
- **Error**: Red tint for out of stock

### Spacing

- `8dp` - Tight spacing
- `12dp` - Default card padding
- `16dp` - Screen padding, rounded corners
- `24dp` - Section spacing

### Typography

- **Headline Medium** - Screen titles
- **Title Medium** - Product names
- **Body Large** - Input fields
- **Label Medium** - Field labels

---

## 🔄 Navigation Routes

```kotlin
// Product List (with bottom navigation)
Screen.ProductList.route

// Add Product
Screen.AddProduct.route

// Edit Product (with ID parameter)
Screen.EditProduct.createRoute(productId: String)
```

### Navigation Flow

```
ProductListScreen
    ├─> AddEditProductScreen (mode: Add)
    └─> AddEditProductScreen (mode: Edit, productId)
```

---

## 💾 Data Model

### Product

```kotlin
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val stock: Int,
    val category: ProductCategory,
    val description: String = "",
    val imageUri: String? = null,
    val sku: String? = null,
    val createdAt: Long,
    val updatedAt: Long
)
```

### ProductCategory

- `MAKANAN` - Food products
- `MINUMAN` - Beverages
- `SEMBAKO` - Daily necessities
- `KEBERSIHAN` - Cleaning products
- `LAINNYA` - Other items

### StockStatus

- `AVAILABLE` - Stock > 20
- `LOW_STOCK` - Stock <= 20
- `OUT_OF_STOCK` - Stock = 0

---

## 🧪 Dummy Data

8 sample products included for testing:

1. Indomie Goreng (Stock: 150)
2. Aqua 600ml (Stock: 200)
3. Beras Premium 5kg (Stock: 25)
4. Teh Botol Sosro (Stock: 18 - Low)
5. Sabun Lifebuoy (Stock: 0 - Out)
6. Gula Pasir 1kg (Stock: 40)
7. Kopi Kapal Api (Stock: 60)
8. Minyak Goreng 2L (Stock: 15 - Low)

---

## 🔧 Dependencies Added

### gradle/libs.versions.toml

```toml
coil = "2.5.0"
```

### app/build.gradle.kts

```kotlin
implementation(libs.coil.compose)  // For image loading
```

---

## 🚀 Usage Examples

### Navigate to Product List

```kotlin
navController.navigate(Screen.ProductList.route)
```

### Add New Product

```kotlin
navController.navigate(Screen.AddProduct.route)
```

### Edit Existing Product

```kotlin
val productId = "product-123"
navController.navigate(Screen.EditProduct.createRoute(productId))
```

### Use ViewModel

```kotlin
@Composable
fun MyScreen() {
    val viewModel: ProductViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    // Search products
    viewModel.onSearchQueryChange("Indomie")

    // Filter by category
    viewModel.filterByCategory(ProductCategory.MAKANAN)

    // Sort products
    viewModel.changeSortOption(SortOption.PRICE_LOW_HIGH)
}
```

---

## ✨ Animations

- **Fade In/Out** - Screen transitions
- **Slide Horizontal** - Form entry/exit
- **Expand/Shrink** - Product card additions/removals
- **Content Size** - Button loading states
- **Scale** - FAB press effect

---

## 📋 Validation Rules

### Product Name

- ❌ Cannot be blank
- ❌ Minimum 3 characters
- ✅ Example: "Indomie Goreng"

### Price

- ❌ Cannot be blank
- ❌ Must be > 0
- ✅ Auto-formats with "Rp" prefix
- ✅ Example: "Rp 15.000"

### Stock

- ❌ Cannot be blank
- ❌ Cannot be negative
- ✅ Allows 0 (out of stock)
- ✅ Example: "50"

### Description

- ✅ Optional
- ✅ Max 500 characters
- ✅ Character counter shown

---

## 🎯 Quick Actions

### From Product Card

1. **Edit** - Opens edit form with pre-filled data
2. **Delete** - Shows confirmation dialog
3. **Quick Add Stock** - Adds +10 units instantly

---

## 🔜 Future Enhancements

### Backend Integration

- [ ] Connect to REST API
- [ ] Implement Repository pattern
- [ ] Add Hilt/Koin DI
- [ ] Room database for offline

### Advanced Features

- [ ] Barcode scanner
- [ ] Bulk import/export (CSV)
- [ ] Low stock notifications
- [ ] Product variants (size, color)
- [ ] Price history tracking
- [ ] Multi-image support
- [ ] QR code generation

### UX Improvements

- [ ] Swipe to delete gesture
- [ ] Pull to refresh
- [ ] Infinite scroll pagination
- [ ] Advanced filters (date range, price range)
- [ ] Sort by popularity
- [ ] Product analytics dashboard

---

## 📱 Screenshots Flow

```
┌─────────────────────┐
│  ProductListScreen  │  ← Search, Filter, Sort
│  • Indomie Goreng  │  ← Product Cards
│  • Aqua 600ml      │  ← Stock Indicators
│  • Beras Premium   │
│         [+]         │  ← FAB
└─────────────────────┘
         ↓
┌─────────────────────┐
│ AddEditProductScreen│
│  [Image Picker]    │
│  Name: _________   │
│  Price: Rp ___    │
│  Stock: _____     │
│  Category: [▼]    │
│  Description: ___  │
│   [Simpan]        │
└─────────────────────┘
```

---

## 🧑‍💻 Developer Notes

### Testing

- Use `ProductViewModel().generateDummyProducts()` for sample data
- Test empty states by clearing all products
- Test validation by entering invalid inputs
- Test search with partial names

### Performance

- LazyColumn for efficient scrolling
- Remember for composition optimization
- StateFlow for reactive updates
- Coil for lazy image loading

### Accessibility

- High contrast colors (WCAG AA)
- Descriptive contentDescription
- Large touch targets (48dp minimum)
- Screen reader support

---

## 📄 License

Part of WarungGo UMKM Management System
© 2026 Umar XI RPL

---

## 🤝 Contributing

This module follows:

- MVVM architecture
- Material 3 design guidelines
- Kotlin best practices
- Clean Code principles

---

**Status**: ✅ Production Ready
**Last Updated**: January 2026
**Module Version**: 1.0.0
