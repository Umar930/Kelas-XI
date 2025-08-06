# SharedPreferences Demo - Android Tutorial

## Deskripsi

Aplikasi Android sederhana yang mendemonstrasikan penggunaan SharedPreferences untuk menyimpan data lokal berupa nama barang dan stok barang.

## Fitur

- Input nama barang (String)
- Input stok barang (Float)
- Simpan data ke SharedPreferences
- Tampilkan notifikasi berhasil/gagal

## Cara Kerja SharedPreferences

### Apa itu SharedPreferences?

SharedPreferences adalah mekanisme penyimpanan data key-value di Android yang digunakan untuk menyimpan data sederhana secara persisten.

### Komponen Penting:

1. **SharedPreferences**: Interface untuk mengakses dan memodifikasi data
2. **Editor**: Interface untuk melakukan perubahan data
3. **apply()**: Metode untuk menyimpan perubahan secara asynchronous

### Alur Kerja:

1. Inisialisasi SharedPreferences dengan nama file "data_barang"
2. Mendapatkan Editor dari SharedPreferences
3. Menyimpan data dengan metode putString() dan putFloat()
4. Menyimpan perubahan dengan editor.apply()

## Struktur Kode

### activity_main.xml

- LinearLayout dengan orientasi vertical
- 2 EditText untuk input nama dan stok barang
- 2 Button untuk menyimpan dan menampilkan data

### MainActivity.java

- Variabel global untuk EditText dan SharedPreferences
- Inisialisasi komponen di onCreate()
- Metode simpan() untuk validasi dan penyimpanan data
- Metode tampil() sebagai placeholder

## Cara Penggunaan

1. Jalankan aplikasi di emulator atau device
2. Masukkan nama barang di field pertama
3. Masukkan jumlah stok di field kedua
4. Klik tombol "Simpan" untuk menyimpan data
5. Data akan tersimpan secara persisten di device

## Lokasi Data Tersimpan

Data tersimpan di direktori aplikasi:

```
/data/data/com.umar.sharedpreferences/shared_prefs/data_barang.xml
```

## Teknologi yang Digunakan

- Android SDK
- Java
- SharedPreferences
- Toast untuk notifikasi
