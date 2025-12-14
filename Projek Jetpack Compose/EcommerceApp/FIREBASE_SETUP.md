# Setup Firebase untuk EasyShop

## ⚠️ PENTING: Konfigurasi Firebase

File `google-services.json` saat ini adalah file placeholder/demo. Untuk menggunakan Firebase Authentication dan Firestore, Anda harus:

### Langkah-langkah Setup Firebase:

1. **Buka Firebase Console**

   - Kunjungi: https://console.firebase.google.com/
   - Login dengan akun Google Anda

2. **Buat Proyek Baru**

   - Klik "Add project" atau "Tambah proyek"
   - Nama proyek: `EasyShop` (atau nama yang Anda inginkan)
   - Ikuti wizard setup

3. **Tambahkan Android App**

   - Klik icon Android
   - Package name: `com.umar.ecommerceapp`
   - App nickname: `EasyShop`
   - Download file `google-services.json`

4. **Replace File google-services.json**

   - Hapus file `app/google-services.json` yang ada
   - Copy file `google-services.json` yang baru Anda download
   - Paste ke folder `app/`

5. **Aktifkan Authentication**

   - Di Firebase Console, pilih proyek Anda
   - Klik "Authentication" di menu sebelah kiri
   - Klik tab "Sign-in method"
   - Enable "Email/Password"

6. **Aktifkan Firestore Database**

   - Di Firebase Console, pilih proyek Anda
   - Klik "Firestore Database" di menu sebelah kiri
   - Klik "Create database"
   - Pilih mode: "Start in test mode" (untuk development)
   - Pilih region: sesuai lokasi Anda

7. **Sync & Build**
   - Sync Gradle di Android Studio
   - Clean & Rebuild project

## 🔥 Fitur yang Sudah Terimplementasi

### ✅ Firebase Authentication

- Sign Up dengan Email & Password
- Error handling untuk pendaftaran gagal

### ✅ Firestore Database

- Menyimpan data pengguna ke collection `users`
- Document ID sama dengan User UID dari Authentication

### ✅ ViewModel Architecture

- `AuthViewModel` untuk mengelola logika authentication
- Separation of concerns yang baik

### ✅ User Model

- Data class `UserModel` dengan properti:
  - `uid`: User ID dari Firebase Auth
  - `name`: Nama lengkap pengguna
  - `email`: Email pengguna

### ✅ Utility

- `AppUtil` untuk menampilkan Toast messages
- Error handling yang user-friendly

## 📱 Cara Menggunakan

1. Jalankan aplikasi
2. Klik tombol "Daftar" di AuthScreen
3. Isi form:
   - Email Address
   - Full Name
   - Password
4. Klik "Sign Up"
5. Jika berhasil, akan muncul toast "Pendaftaran berhasil!"
6. Data pengguna tersimpan di Firestore

## 🔐 Keamanan

- Password di-hash otomatis oleh Firebase Authentication
- Password field menggunakan `PasswordVisualTransformation`
- Data sensitif tidak disimpan di local storage

## 📝 Catatan

- File `google-services.json` yang ada saat ini adalah DEMO
- Aplikasi tidak akan berfungsi tanpa file `google-services.json` yang valid
- Pastikan Firebase Authentication dan Firestore sudah diaktifkan

## 🚀 Next Steps

Setelah setup Firebase selesai, Anda bisa:

1. Implementasi Login function di `AuthViewModel`
2. Tambahkan validasi input (email format, password strength)
3. Tambahkan loading indicator saat proses sign up
4. Implement navigation ke home screen setelah berhasil sign up
