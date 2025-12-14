# Presentasi Aplikasi RealtimeWeather

## 🌦️ Ringkasan Aplikasi

**RealtimeWeather** adalah aplikasi cuaca real-time berbasis Android yang dibangun menggunakan Jetpack Compose. Aplikasi ini menyediakan informasi cuaca terkini berdasarkan pencarian nama kota.

## 🏗️ Arsitektur Aplikasi

- **MVVM Architecture**: Model-View-ViewModel
- **Jetpack Compose**: UI modern dan deklaratif
- **Retrofit**: Koneksi API cuaca
- **Coroutines**: Operasi asinkron
- **LiveData**: Reaktivitas state management

## 📱 Komponen Utama

### 1. MainActivity

- Entry point aplikasi
- Mengatur tema dan scaffold dasar
- Memanggil composable utama (WeatherPage)

### 2. WeatherPage (Layout Utama)

- **Pencarian Kota**: Input field dan tombol pencarian
- **Status Tampilan**: Loading, Error, Success, atau Initial State
- **WeatherDetails**: Tampilan detail cuaca

### 3. Weather Details

- **Layout Scrollable** untuk informasi lengkap
- Lokasi dan negara
- Temperatur dan icon cuaca
- Card detail cuaca (kelembaban, angin, dll)

## 🔄 Alur Kerja Aplikasi

1. **Input**: Pengguna memasukkan nama kota
2. **Proses**:
   - WeatherViewModel mengirim request ke API
   - Response diproses menjadi state (Loading → Success/Error)
3. **Output**:
   - Menampilkan data cuaca lengkap jika berhasil
   - Menampilkan pesan error jika gagal

## 📊 Fitur Utama

- **Real-time Weather Data**: Informasi cuaca terbaru
- **Pencarian Berdasarkan Kota**: Fleksibel untuk lokasi manapun
- **Informasi Lengkap**: Suhu, kelembaban, angin, tekanan udara, dll
- **UI Responsif**: Loading state dan error handling
- **UI Modern**: Material Design 3 dengan Jetpack Compose

## 📱 Cara Penggunaan

1. Buka aplikasi RealtimeWeather
2. Masukkan nama kota di kolom pencarian
3. Tekan tombol pencarian atau Enter
4. Lihat informasi cuaca lengkap
5. Scroll untuk melihat detail tambahan

---

**Dikembangkan Oleh**: Umar  
**Teknologi**: Kotlin, Jetpack Compose, Retrofit, MVVM
