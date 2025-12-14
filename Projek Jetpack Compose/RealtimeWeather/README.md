# Aplikasi Cuaca Real-time 🌤️

Aplikasi Android untuk menampilkan informasi cuaca real-time menggunakan Jetpack Compose dan arsitektur MVVM.

---

## 📋 RINGKASAN LAYOUT DAN KOMPONEN

### **1. WeatherPage (Main Screen)**

**Layout Utama:** `Column` (Vertical, Scrollable)

#### **Komponen:**

- **Row (Search Bar)**
  - `OutlinedTextField` - Input nama kota
  - `IconButton` - Tombol search dengan ikon 🔍
- **Conditional Content (When Expression)**
  - **Loading State:** `CircularProgressIndicator` (animasi loading)
  - **Error State:** `Text` dengan pesan error (merah)
  - **Success State:** Memanggil `WeatherDetails`
  - **Initial State:** `Text` instruksi awal

### **2. WeatherDetails (Weather Display)**

**Layout:** `Column` dengan `verticalScroll` (bisa di-scroll)

#### **Struktur:**

```
Column (Scrollable)
├── Row (Lokasi)
│   ├── Icon (Pin lokasi)
│   └── Column (Nama kota & negara)
├── Text (Suhu besar - 72sp)
├── AsyncImage (Ikon cuaca - 128x128)
├── Text (Kondisi cuaca)
└── Card (Detail lengkap)
    └── 9x WeatherKeyValue items
```

#### **Detail dalam Card:**

1. Kelembaban (%)
2. Kecepatan Angin (kph)
3. Arah Angin (N/S/E/W)
4. Terasa Seperti (°C)
5. Tekanan Udara (mb)
6. Jarak Pandang (km)
7. UV Index
8. Tanggal
9. Waktu Lokal

### **3. WeatherKeyValue (Helper Component)**

**Layout:** `Row` dengan `SpaceBetween`

- Text kiri (Key) - Gray, Medium weight
- Text kanan (Value) - Bold

---

## 🔄 ALUR KERJA APLIKASI

### **FASE 1: User Input**

```
User mengetik nama kota → State `city` update → UI recompose
```

### **FASE 2: Search Trigger**

```
User tap tombol Search
    ↓
viewModel.getData(city) dipanggil
    ↓
Keyboard otomatis disembunyikan
```

### **FASE 3: Data Fetching**

```
ViewModel set state → NetworkResponse.Loading
    ↓
UI menampilkan CircularProgressIndicator
    ↓
Retrofit membuat HTTP GET request
    ↓
API URL: https://api.weatherapi.com/v1/current.json?key=xxx&q=Jakarta
    ↓
Server memproses (1-3 detik)
```

### **FASE 4: Response Handling**

**✅ Success:**

```
Response OK → JSON di-parse ke WeatherModel
    ↓
State update → NetworkResponse.Success(data)
    ↓
UI recompose → WeatherDetails ditampilkan
```

**❌ Error:**

```
Response gagal → NetworkResponse.Error(message)
    ↓
UI recompose → Text error ditampilkan (merah)
```

### **FASE 5: Display Data**

```
WeatherDetails dirender:
    - Lokasi (kota & negara)
    - Suhu utama (besar & bold)
    - Ikon cuaca (download dari URL)
    - Kondisi cuaca (text)
    - Card dengan 9 detail info
    ↓
User dapat scroll untuk melihat semua detail
```

---

## 🏗️ ARSITEKTUR MVVM

```
┌─────────────────────────────────┐
│     VIEW (UI Layer)              │
│  WeatherPage, WeatherDetails    │
│  - Jetpack Compose               │
│  - Observes LiveData             │
└──────────────┬──────────────────┘
               │
               │ observes
               │ calls methods
               ↓
┌─────────────────────────────────┐
│   VIEWMODEL (Logic Layer)        │
│  WeatherViewModel                │
│  - getData(city)                 │
│  - LiveData<NetworkResponse>     │
│  - Coroutines                    │
└──────────────┬──────────────────┘
               │
               │ fetches data
               ↓
┌─────────────────────────────────┐
│     MODEL (Data Layer)           │
│  - RetrofitInstance              │
│  - WeatherAPI Interface          │
│  - Data Classes                  │
│  - NetworkResponse (Sealed)      │
└──────────────────────────────────┘
```

---

## 📊 PENJELASAN DETAIL FUNGSI

### **1. STATE MANAGEMENT**

#### **Local State (Composable)**

```kotlin
var city by remember { mutableStateOf("") }
```

- Menyimpan input user (nama kota)
- Trigger recomposition saat berubah
- Hilang saat Composable di-dispose

#### **ViewModel State (LiveData)**

```kotlin
val weatherResult: LiveData<NetworkResponse<WeatherModel>>
```

- Menyimpan hasil API call
- Survive configuration changes (rotasi layar)
- Observed oleh UI untuk auto-update

#### **NetworkResponse (Sealed Class)**

```kotlin
sealed class NetworkResponse<out T> {
    data class Success<T>(val data: T)
    data class Error(val message: String)
    object Loading
}
```

**Keuntungan:**

- Type-safe state representation
- Exhaustive when expression
- Clear state transitions

---

### **2. NETWORKING (Retrofit + Coroutines)**

#### **Retrofit Setup**

```kotlin
Retrofit.Builder()
    .baseUrl("https://api.weatherapi.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
```

- Base URL untuk semua API calls
- Gson untuk JSON ↔ Kotlin object
- Singleton pattern untuk efficiency

#### **API Interface**

```kotlin
@GET("v1/current.json")
suspend fun getWeather(
    @Query("key") apiKey: String,
    @Query("q") city: String
): Response<WeatherModel>
```

- `@GET`: HTTP GET request
- `suspend`: Non-blocking coroutine function
- `@Query`: URL query parameters
- Return: `Response<WeatherModel>` dengan status code

#### **Coroutines Usage**

```kotlin
viewModelScope.launch {
    try {
        val response = RetrofitInstance.weatherAPI.getWeather(...)
        if (response.isSuccessful) {
            // Success handling
        } else {
            // Error handling
        }
    } catch (e: Exception) {
        // Exception handling
    }
}
```

**Benefits:**

- Non-blocking I/O
- Auto-cancelled dengan ViewModel
- Main thread tetap responsive

---

### **3. UI RENDERING (Jetpack Compose)**

#### **Declarative UI**

```kotlin
@Composable
fun WeatherPage() {
    when (weatherResult.value) {
        is Loading -> ShowLoading()
        is Error -> ShowError()
        is Success -> ShowData()
    }
}
```

- UI = function of state
- Auto-recompose saat state berubah
- Efficient: hanya update yang berubah

#### **Scrollable Content**

```kotlin
val scrollState = rememberScrollState()

Column(
    modifier = Modifier.verticalScroll(scrollState)
)
```

- User dapat scroll untuk melihat semua detail
- State tersimpan across recompositions
- Smooth scrolling experience

#### **Image Loading (Coil)**

```kotlin
AsyncImage(
    model = iconUrl,
    contentDescription = "Ikon Cuaca",
    modifier = Modifier.size(128.dp)
)
```

- Asynchronous image loading
- Auto caching
- Placeholder support

---

### **4. MATERIAL DESIGN 3**

#### **Komponen Digunakan:**

1. **Scaffold** - Top-level layout structure
2. **OutlinedTextField** - Input field dengan outline
3. **Card** - Elevated surface untuk detail
4. **IconButton** - Clickable icon dengan ripple
5. **CircularProgressIndicator** - Loading animation

#### **Design Principles:**

- Consistent spacing (8dp, 12dp, 16dp)
- Color scheme (Purple primary)
- Typography hierarchy (72sp, 28sp, 20sp, 16sp)
- Elevation & shadows untuk depth
- Rounded corners (12dp, 16dp)

---

## 🎯 KELEBIHAN APLIKASI

### **Architecture:**

✅ **MVVM** - Separation of concerns
✅ **Clean Code** - Readable dan maintainable
✅ **Testability** - Easy to unit test

### **User Experience:**

✅ **Responsive** - Smooth transitions
✅ **Loading States** - Clear feedback
✅ **Error Handling** - Informative messages
✅ **Auto-hide Keyboard** - Better UX
✅ **Scrollable** - Semua info bisa dilihat

### **Technology:**

✅ **Jetpack Compose** - Modern UI toolkit
✅ **Kotlin Coroutines** - Async programming
✅ **Retrofit** - Reliable networking
✅ **LiveData** - Reactive updates
✅ **Material Design 3** - Beautiful UI

---

## 📐 DETAIL LAYOUT MEASUREMENTS

### **Spacing:**

- Container padding: 16.dp
- Section spacing: 16dp - 32.dp
- Item spacing: 8dp - 12.dp

### **Typography:**

- Temperature: 72.sp (Extra Large)
- City name: 28.sp (Large)
- Condition: 24.sp (Medium-Large)
- Section title: 20.sp (Medium)
- Detail text: 16.sp (Body)

### **Component Sizes:**

- IconButton: 56.dp
- Search icon: 32.dp
- Location icon: 32.dp
- Weather image: 128.dp
- TextField height: ~56.dp (default)
- Card corner radius: 16.dp
- TextField corner radius: 12.dp

---

## 🔒 DATA FLOW

```
User Input (TextField)
    ↓
Local State Update (city)
    ↓
Button Click
    ↓
ViewModel.getData(city)
    ↓
Set Loading State
    ↓
UI Shows Loading
    ↓
Retrofit API Call
    ↓
Server Processing
    ↓
JSON Response
    ↓
Gson Parsing
    ↓
WeatherModel Object
    ↓
Update LiveData (Success/Error)
    ↓
UI Observes Change
    ↓
Recomposition
    ↓
Display Results
```

---

## 📝 DATA MODELS

### **WeatherModel**

```
- location: Location
- current: Current
```

### **Location**

```
- name: String (nama kota)
- country: String (nama negara)
- localtime: String (waktu lokal)
- lat, lon, region, tz_id
```

### **Current**

```
- temp_c: String (suhu Celsius)
- humidity: String (kelembaban)
- wind_kph: String (kecepatan angin)
- wind_dir: String (arah angin)
- pressure_mb: String (tekanan udara)
- vis_km: String (jarak pandang)
- uv: String (UV index)
- feelslike_c: String (suhu yang dirasakan)
- condition: Condition (kondisi cuaca)
```

### **Condition**

```
- text: String (deskripsi)
- icon: String (URL ikon)
- code: String (kode kondisi)
```

---

## 🚀 Cara Setup

### 1. Dapatkan API Key

- Kunjungi [WeatherAPI.com](https://www.weatherapi.com/)
- Daftar untuk mendapatkan API key gratis

### 2. Konfigurasi API Key

Buka: `app/src/main/java/com/umar/realtimeweather/api/Constants.kt`

```kotlin
object Constants {
    const val BASE_URL = "https://api.weatherapi.com/"
    const val API_KEY = "MASUKKAN_API_KEY_ANDA_DI_SINI"
}
```

### 3. Sync & Run

- Sync project dengan Gradle
- Pilih device/emulator
- Run aplikasi (▶️)

---

## 📖 DOKUMENTASI LENGKAP

Untuk penjelasan yang lebih detail dan mendalam, lihat:
📄 **[DOKUMENTASI_LENGKAP.md](./DOKUMENTASI_LENGKAP.md)**

File tersebut berisi:

- Penjelasan setiap layout secara detail
- Alur kerja aplikasi step-by-step
- Arsitektur dan design patterns
- Code examples dan best practices
- Visual diagrams dan flowcharts

---

## 👨‍💻 Developer

Dikembangkan oleh: **Umar XI RPL**

---

**Built with ❤️ using Kotlin & Jetpack Compose**

## 📋 Fitur

- ✅ Pencarian cuaca berdasarkan nama kota
- ✅ Menampilkan suhu, kondisi cuaca, dan ikon cuaca
- ✅ Detail cuaca lengkap (kelembaban, kecepatan angin, tekanan udara, dll.)
- ✅ Arsitektur MVVM dengan Sealed Class untuk manajemen state
- ✅ UI modern dengan Material Design 3
- ✅ Loading state dan error handling

## 🛠️ Teknologi yang Digunakan

- **Kotlin** - Bahasa pemrograman
- **Jetpack Compose** - UI toolkit modern
- **Retrofit** - HTTP client untuk API calls
- **Gson** - JSON parser
- **Coil** - Image loading library
- **Coroutines** - Asynchronous programming
- **LiveData** - Observable data holder
- **ViewModel** - UI state management

## 🚀 Cara Setup

### 1. Clone atau Download Project

### 2. Dapatkan API Key

1. Kunjungi [WeatherAPI.com](https://www.weatherapi.com/)
2. Daftar untuk mendapatkan API key gratis
3. Copy API key yang Anda dapatkan

### 3. Konfigurasi API Key

Buka file `Constants.kt` di path:

```
app/src/main/java/com/umar/realtimeweather/api/Constants.kt
```

Ganti nilai `API_KEY` dengan API key Anda:

```kotlin
object Constants {
    const val BASE_URL = "https://api.weatherapi.com/"
    const val API_KEY = "MASUKKAN_API_KEY_ANDA_DI_SINI" // <-- Ganti dengan API key Anda
}
```

### 4. Sync Project dengan Gradle

1. Buka project di Android Studio
2. Tunggu hingga Gradle sync selesai
3. Jika ada error, klik "Sync Project with Gradle Files"

### 5. Jalankan Aplikasi

1. Pilih emulator atau device fisik
2. Klik tombol Run (▶️) atau tekan Shift + F10
3. Aplikasi akan ter-install dan berjalan di device Anda

## 📱 Cara Menggunakan Aplikasi

1. Buka aplikasi
2. Masukkan nama kota di kolom pencarian (contoh: "Jakarta", "Surabaya", "London")
3. Tekan tombol search (🔍)
4. Tunggu beberapa saat hingga data cuaca ditampilkan
5. Lihat detail cuaca lengkap termasuk:
   - Suhu saat ini
   - Kondisi cuaca
   - Kelembaban
   - Kecepatan angin
   - Tekanan udara
   - Jarak pandang
   - UV index
   - Dan informasi lainnya

## 📁 Struktur Proyek

```
app/src/main/java/com/umar/realtimeweather/
├── api/
│   ├── Constants.kt          # Konfigurasi API (BASE_URL, API_KEY)
│   ├── WeatherAPI.kt         # Interface Retrofit
│   ├── RetrofitInstance.kt   # Singleton Retrofit
│   ├── NetworkResponse.kt    # Sealed class untuk state management
│   ├── WeatherModel.kt       # Model data utama
│   ├── Location.kt           # Model data lokasi
│   ├── Current.kt            # Model data cuaca saat ini
│   └── Condition.kt          # Model data kondisi cuaca
├── viewmodel/
│   └── WeatherViewModel.kt   # ViewModel dengan LiveData
├── ui/
│   └── WeatherPage.kt        # Composable UI
└── MainActivity.kt            # Entry point aplikasi
```

## 🔧 Dependencies

Semua dependencies sudah ditambahkan di `build.gradle.kts (Module :app)`:

```kotlin
// Retrofit untuk API
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// Kotlin Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

// ViewModel dan LiveData
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
implementation("androidx.compose.runtime:runtime-livedata:1.6.1")

// Coil untuk loading gambar
implementation("io.coil-kt:coil-compose:2.5.0")
```

## ⚠️ Troubleshooting

### Aplikasi tidak dapat mengambil data cuaca

1. **Periksa koneksi internet** - Pastikan device memiliki koneksi internet aktif
2. **Periksa API Key** - Pastikan API key sudah diisi dengan benar di `Constants.kt`
3. **Periksa nama kota** - Pastikan nama kota dieja dengan benar
4. **Periksa quota API** - API gratis memiliki limit request per bulan

### Build Error

1. Jalankan `./gradlew clean` di terminal
2. Sync project dengan Gradle
3. Invalidate cache: **File > Invalidate Caches / Restart**

### Keyboard tidak tertutup setelah search

Sudah dihandle dengan `LocalSoftwareKeyboardController` dan `LocalFocusManager`

## 📝 Arsitektur MVVM

Aplikasi ini mengimplementasikan arsitektur MVVM (Model-View-ViewModel):

- **Model**: Data classes (WeatherModel, Location, Current, Condition)
- **View**: Jetpack Compose UI (WeatherPage, WeatherDetails)
- **ViewModel**: WeatherViewModel dengan LiveData untuk state management

### Flow Data:

```
UI (WeatherPage)
  → ViewModel (getData)
    → Repository (RetrofitInstance)
      → API (WeatherAPI)
        → Response
      ← JSON Data
    ← NetworkResponse<WeatherModel>
  ← LiveData Update
→ UI Update (Recompose)
```

## 🎨 Fitur UI

- Material Design 3
- Responsive layout
- Loading indicator
- Error handling dengan pesan yang jelas
- Card design untuk detail cuaca
- Icon cuaca dinamis dari API
- Smooth animations

## 📄 Lisensi

Project ini dibuat untuk tujuan pembelajaran.

## 👨‍💻 Developer

Dikembangkan oleh: Umar XI RPL

---

**Selamat mencoba! 🚀**
