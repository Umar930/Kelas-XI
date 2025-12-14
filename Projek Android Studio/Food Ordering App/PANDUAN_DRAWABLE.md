# Panduan Penggunaan Drawable Resource di Food Ordering App

## Pendahuluan

Dokumen ini memberikan panduan tentang bagaimana menggunakan drawable resource (gambar lokal) dalam aplikasi Food Ordering App. Pendekatan ini lebih direkomendasikan dibandingkan menggunakan URL gambar dari internet karena lebih efisien, cepat, dan tetap berfungsi tanpa koneksi internet.

## Struktur Folder Drawable

Semua gambar yang digunakan dalam aplikasi disimpan di folder:

```
app/src/main/res/drawable/
```

## Aturan Penamaan Gambar

Untuk konsistensi dan kemudahan pengelolaan, kami menggunakan konvensi penamaan berikut:

1. Gunakan format `img_[nama_makanan].jpg` untuk semua gambar makanan
2. Gunakan huruf kecil dan garis bawah (\_) untuk memisahkan kata
3. Hindari penggunaan spasi atau karakter khusus dalam nama file

Contoh: `img_nasi_goreng.jpg`, `img_es_teh.jpg`

## Cara Menambahkan Gambar Baru

### 1. Menyiapkan Gambar

- Gunakan gambar dengan rasio aspek 16:9 atau 4:3 untuk konsistensi tampilan
- Optimalkan ukuran gambar (disarankan <200KB) untuk performa yang baik
- Format yang didukung: JPG, PNG, WebP

### 2. Menambahkan ke Folder Drawable

Ada beberapa cara untuk menambahkan gambar ke folder drawable:

#### Menggunakan Android Studio:

1. Klik kanan pada folder `res/drawable/`
2. Pilih **New > Image Asset** atau **Copy Files**
3. Ikuti wizard untuk mengimpor gambar

#### Melalui Explorer:

1. Buka folder proyek di Explorer
2. Navigasikan ke `app/src/main/res/drawable/`
3. Salin gambar ke folder tersebut
4. Refresh proyek di Android Studio

### 3. Menggunakan Gambar dalam Kode

Setelah menambahkan gambar, Anda dapat mengaksesnya di kode dengan:

```kotlin
// Di dalam class yang sama dengan resource R
imageView.setImageResource(R.drawable.img_nama_gambar)

// Atau di activity/fragment
imageView.setImageResource(com.umar.foodorderingapp.R.drawable.img_nama_gambar)

// Dengan Glide
Glide.with(context)
    .load(R.drawable.img_nama_gambar)
    .into(imageView)
```

### 4. Memperbarui SampleData

Untuk menambahkan gambar ke makanan baru di `SampleData.kt`:

```kotlin
Food(
    id = "11",
    name = "Nama Makanan Baru",
    description = "Deskripsi makanan",
    price = 25000,
    category = "Makanan",
    rating = 4.6f,
    imageResId = R.drawable.img_nama_gambar, // Resource drawable baru
    isPopular = true,
    isRecommended = false
)
```

## Keuntungan Menggunakan Drawable Resource

1. **Performa Lebih Baik**: Gambar dimuat langsung dari storage lokal
2. **Pengalaman Offline**: Aplikasi berfungsi tanpa koneksi internet
3. **Tidak Ada Delay Loading**: Menghilangkan waktu tunggu download gambar
4. **Hemat Kuota Data**: Tidak menggunakan bandwidth pengguna
5. **Kemudahan Pengelolaan**: Semua aset terkontrol dalam satu tempat

## Tips dan Praktik Terbaik

1. **Ukuran Optimal**: Jaga ukuran gambar tetap kecil untuk performa yang baik
2. **Gunakan Night Mode**: Pertimbangkan menyediakan versi gambar berbeda untuk tema gelap
3. **Density Buckets**: Untuk aplikasi dengan dukungan multi-resolusi, gunakan folder drawable-hdpi, drawable-xhdpi, dll.
4. **Gunakan Vector jika Memungkinkan**: Untuk ikon dan gambar sederhana, gunakan format Vector Drawable (.xml)
5. **Konsistensi Visual**: Pastikan semua gambar makanan memiliki gaya dan rasio yang konsisten

## Troubleshooting

### Gambar Tidak Muncul

1. Periksa nama resource (case sensitive)
2. Pastikan ekstensi file benar (.jpg bukan .jpeg)
3. Restart Android Studio dan clean build project
4. Periksa logcat untuk error resource not found

### Gambar Terpotong atau Tidak Proporsional

1. Periksa ScaleType pada ImageView
2. Gunakan `centerCrop` untuk mengisi area atau `fitCenter` untuk menampilkan gambar penuh

```kotlin
// Dengan Glide
Glide.with(context)
    .load(R.drawable.img_nama_gambar)
    .centerCrop() // atau .fitCenter()
    .into(imageView)
```
