# WarungGo - Aplikasi Manajemen Warung

Aplikasi Android untuk membantu pemilik warung mengelola produk, transaksi, pengeluaran, dan laporan keuangan.

## 📱 Fitur Utama

### 1. **Authentication**

- Login Screen
- Register Screen (untuk Owner warung)

### 2. **Dashboard**

- Ringkasan penjualan hari ini
- Total pengeluaran
- Laba bersih
- Menu shortcut ke fitur utama

### 3. **Manajemen Produk**

- Daftar produk dengan stok
- Tambah/edit produk
- Informasi harga dan kategori

### 4. **Transaksi Penjualan**

- Buat transaksi baru dengan pilih produk & quantity
- Riwayat transaksi
- Detail transaksi lengkap

### 5. **Pengeluaran**

- Catat pengeluaran operasional
- Daftar riwayat pengeluaran
- Kategori pengeluaran

### 6. **Laporan Keuangan**

- Filter laporan harian/bulanan
- Total pemasukan dan pengeluaran
- Perhitungan laba bersih otomatis

### 7. **Profil**

- Informasi user dan warung
- Pengaturan akun
- Logout

## 🏗️ Arsitektur & Teknologi

### Stack Teknologi

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Design System**: Material 3
- **Navigation**: Navigation Compose
- **Architecture**: MVVM (siap untuk integrasi ViewModel)

### Struktur Project

```
com.umar.warunggo/
├── model/                    # Data classes
│   ├── Product.kt
│   ├── Transaction.kt
│   ├── TransactionItem.kt
│   └── Expense.kt
│
├── navigation/               # Navigation setup
│   ├── Screen.kt            # Route definitions
│   └── NavGraph.kt          # Navigation graph
│
├── ui/
│   ├── auth/                # Authentication screens
│   │   ├── LoginScreen.kt
│   │   └── RegisterScreen.kt
│   │
│   ├── dashboard/           # Dashboard screen
│   │   └── DashboardScreen.kt
│   │
│   ├── product/             # Product management
│   │   ├── ProductListScreen.kt
│   │   └── ProductFormScreen.kt
│   │
│   ├── transaction/         # Transaction management
│   │   ├── TransactionCreateScreen.kt
│   │   ├── TransactionListScreen.kt
│   │   └── TransactionDetailScreen.kt
│   │
│   ├── expense/             # Expense management
│   │   ├── ExpenseListScreen.kt
│   │   └── ExpenseFormScreen.kt
│   │
│   ├── report/              # Financial reports
│   │   └── ReportScreen.kt
│   │
│   ├── profile/             # User profile
│   │   └── ProfileScreen.kt
│   │
│   ├── components/          # Reusable components
│   │   ├── AppTopBar.kt
│   │   ├── LoadingView.kt
│   │   └── ErrorView.kt
│   │
│   └── theme/               # Theme configuration
│
└── MainActivity.kt
```

## 🎨 Design Patterns

### 1. **State Hoisting**

Semua state dikelola di composable parent dan di-pass ke child components.

### 2. **Reusable Components**

- AppTopBar: Top bar konsisten dengan back navigation
- LoadingView: Loading indicator standar
- ErrorView: Error state dengan retry option

### 3. **Type-Safe Navigation**

Menggunakan sealed class untuk route definitions yang type-safe.

### 4. **Bottom Navigation**

Navigation bar untuk 5 menu utama:

- Dashboard
- Produk
- Transaksi
- Laporan
- Profil

## 📝 Status Implementasi

✅ **Selesai - UI & Navigation**

- Semua screen UI sudah diimplementasikan
- Navigation flow lengkap
- Dummy data untuk testing
- Material 3 theming

⏳ **Belum Diimplementasikan**

- ViewModel layer
- Repository pattern
- API integration (Laravel backend)
- Local database (Room)
- Data persistence
- Real authentication
- Image upload
- Date picker yang proper
- Form validation yang lebih kompleks

## 🚀 Cara Menjalankan

1. Clone repository
2. Buka project di Android Studio
3. Sync Gradle
4. Run aplikasi di emulator atau device

### Testing Login

Untuk testing, gunakan email dan password apapun (minimal 6 karakter untuk password).

## 🔜 Next Steps - Integrasi Backend

### 1. Setup Repository Layer

```kotlin
interface ProductRepository {
    suspend fun getProducts(): List<Product>
    suspend fun getProduct(id: Int): Product
    suspend fun createProduct(product: Product): Product
    suspend fun updateProduct(product: Product): Product
    suspend fun deleteProduct(id: Int): Boolean
}
```

### 2. Setup ViewModel

```kotlin
class ProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    fun loadProducts() {
        viewModelScope.launch {
            _products.value = repository.getProducts()
        }
    }
}
```

### 3. API Integration

- Retrofit untuk REST API
- Kotlin Serialization untuk JSON parsing
- JWT token management
- Error handling

### 4. Local Database

- Room untuk caching
- Offline-first approach
- Sync strategy

## 📄 License

Copyright © 2026 WarungGo. All rights reserved.

## 👨‍💻 Developer

Dikembangkan dengan ❤️ menggunakan Jetpack Compose
