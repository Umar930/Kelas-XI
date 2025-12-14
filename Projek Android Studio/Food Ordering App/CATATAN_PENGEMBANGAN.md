# Catatan Pengembangan Food Ordering App

## Perubahan Terbaru

### Implementasi Gambar Lokal

Aplikasi telah diperbarui untuk menggunakan gambar lokal (drawable resource) daripada URL gambar dari internet. Hal ini meningkatkan:

1. **Kecepatan Loading** - Gambar dimuat lebih cepat karena tidak perlu mengunduh dari internet
2. **Ketahanan Offline** - Aplikasi dapat berjalan penuh tanpa koneksi internet
3. **Efisiensi Data** - Tidak menggunakan kuota data pengguna untuk menampilkan gambar

### Resource Drawable yang Digunakan

Semua gambar makanan dan minuman sekarang tersimpan di folder `res/drawable/`:

1. `img_nasi_goreng.jpg` - Untuk Nasi Goreng Spesial
2. `img_mie_goreng.jpg` - Untuk Mie Goreng Special
3. `img_ayam_bakar.jpg` - Untuk Ayam Bakar
4. `img_es_teh.jpg` - Untuk Es Teh Manis
5. `img_es_jeruk.jpg` - Untuk Es Jeruk
6. `img_sate_ayam.jpg` - Untuk Sate Ayam
7. `img_soto_ayam.jpg` - Untuk Soto Ayam
8. `img_es_kopi.jpg` - Untuk Es Kopi Susu
9. `img_kentang_goreng.jpg` - Untuk Kentang Goreng
10. `img_es_krim.jpg` - Untuk Es Krim Coklat
11. `placeholder_image.jpg` - Gambar default jika ID makanan tidak dikenali

### Perubahan Kode

File yang diubah:

1. **SampleData.kt**:

   - Mengganti property `imageUrl` dengan `imageResId` pada class `Food`
   - Mengubah data sample untuk menggunakan resource ID drawable

2. **FoodAdapter.kt**:

   - Mengubah cara loading gambar di `FoodViewHolder` untuk menggunakan resource ID lokal

3. **FoodDetailActivity.kt**:
   - Mengubah metode `updateUI()` untuk memuat gambar dari resource ID lokal

### Panduan Menambahkan Menu Baru

Untuk menambahkan menu makanan atau minuman baru, ikuti langkah-langkah berikut:

1. Tambahkan gambar baru ke folder `res/drawable/`
2. Tambahkan data baru di `SampleData.kt` dengan format:

```kotlin
Food(
    id = "11", // Gunakan ID yang belum ada
    name = "Nama Makanan Baru",
    description = "Deskripsi makanan baru",
    price = 20000, // Harga dalam Rupiah
    category = "Kategori", // Makanan/Minuman/Snack/Dessert
    rating = 4.5f, // Rating 0-5
    imageResId = R.drawable.nama_file_gambar, // Resource ID gambar
    isPopular = true/false,
    isRecommended = true/false
)
```

3. Jika diperlukan, tambahkan case baru di `FoodDetailActivity.kt` pada metode `updateUI()` untuk mapping ID ke resource drawable.

## Rencana Pengembangan Selanjutnya

1. Implementasi fitur keranjang belanja (cart)
2. Sistem login dan registrasi pengguna
3. Integrasi dengan Firebase untuk penyimpanan data online
4. Fitur pencarian makanan dan minuman
5. Sistem riwayat pesanan
