# API Monitoring Kelas - Backend Laravel

<p align="center">
  <strong>RESTful API untuk Aplikasi Monitoring Kelas</strong><br>
  Backend server menggunakan Laravel dengan otentikasi berbasis Token (Sanctum)<br>
  <strong>✨ NOW WITH ADMIN PANEL MANAGEMENT ✨</strong>
</p>

---

## 🚀 **New Features - Admin Panel Management**

### ✅ Implemented Controllers (December 6, 2025)

### Local debug endpoints (development only) ⚠️

-   GET `/admin/debug/user-info` — Returns DB connectivity status and whether the seeded admin (`admin@monitoring.com`) exists and has password `admin123`. Only available when `APP_DEBUG=true`.
-   POST `/admin/debug/reset-admin-password` — Resets the admin user's password to `admin123`. Only available when `APP_DEBUG=true`. Use this to recover a known admin password in local development environments.

### 📊 Total Endpoints: **103**

-   **35 new admin panel endpoints**
-   **68 existing role-based endpoints**

### 📚 Complete Documentation

-   **[DOKUMENTASI_API_ADMIN_PANEL.md](DOKUMENTASI_API_ADMIN_PANEL.md)** - Full API specification
-   **[TESTING_GUIDE.md](TESTING_GUIDE.md)** - Testing examples (curl & PowerShell)
-   **[IMPLEMENTASI_SUMMARY.md](IMPLEMENTASI_SUMMARY.md)** - Implementation summary

---

## 📱 **Quick Start untuk Android Development**

### 1️⃣ Start Laravel Server

```bash
# Windows (Double-click)
start-server.bat

# Atau PowerShell
.\start-server.ps1

# Atau manual
php artisan serve --host=0.0.0.0 --port=8000
```

### 2️⃣ Base URL di Retrofit (Android)

**Untuk Emulator:**

```kotlin
const val BASE_URL = "http://10.0.2.2:8000/"
```

**Untuk Device Fisik:**

```kotlin
const val BASE_URL = "http://192.168.40.10:8000/"  // Sesuaikan dengan IP laptop Anda
```

### 3️⃣ Test Koneksi

```powershell
.\test-connection.ps1
```

### 🔐 Kredensial Testing

| Role           | Email                    | Password     |
| -------------- | ------------------------ | ------------ |
| **Admin**      | **admin@sekolah.sch.id** | **admin123** |
| Kurikulum      | kurikulum@example.com    | password     |
| Kepala Sekolah | kepsek@example.com       | password     |
| Siswa          | siswa@example.com        | password     |

**📖 Panduan Lengkap:** `ANDROID_API_SETUP.md`

---

## 📖 Deskripsi

API Monitoring Kelas adalah backend RESTful API yang dikembangkan menggunakan Laravel untuk mendukung aplikasi Android monitoring kelas. API ini menyediakan sistem otentikasi berbasis token dan endpoint CRUD untuk mengelola data guru, siswa, kelas, dan absensi.

### ✨ Fitur Utama

-   🔐 **Token-based Authentication** menggunakan Laravel Sanctum
-   👥 **Multi-role User System** (Admin, Kurikulum, Kepala Sekolah, Siswa)
-   📚 **CRUD Lengkap** (Guru, Mapel, Tahun Ajaran, Kelas, Siswa, Absensi)
-   🔍 **Pagination, Search & Filter** di semua endpoint
-   🛡️ **Protected Routes** dengan middleware Sanctum
-   ✅ **Input Validation** di semua endpoint
-   🌐 **CORS Support** untuk Android client
-   📝 **Error Handling** yang comprehensive
-   ✅ **100% Tested** - 42/42 tests passing

---

## 🛠️ Teknologi

-   **Framework**: Laravel 12.x
-   **Authentication**: Laravel Sanctum v4.2.0
-   **Database**: MySQL
-   **PHP**: >= 8.2
-   **Architecture**: Route → Controller → Model → Database

---

## 🚀 Quick Start

### Prasyarat

-   PHP >= 8.2
-   Composer
-   MySQL >= 5.7
-   Git

### Instalasi

1. **Clone Repository**

```bash
git clone <repository-url>
cd aplikasimonitoring-api
```

2. **Install Dependencies**

```bash
composer install
```

3. **Setup Environment**

```bash
copy .env.example .env
php artisan key:generate
```

4. **Konfigurasi Database**
   Edit file `.env`:

```env
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=db_monitoring_kelas
DB_USERNAME=root
DB_PASSWORD=
```

5. **Buat Database**

```sql
CREATE DATABASE db_monitoring_kelas;
```

Atau import `database_setup.sql`

6. **Jalankan Migration & Seeder**

```bash
php artisan migrate:fresh --seed
```

7. **Jalankan Server**

```bash
php artisan serve
```

Server berjalan di: `http://127.0.0.1:8000`

---

## 📚 Dokumentasi

### 📄 File Dokumentasi Tersedia:

1. **[API_DOCUMENTATION.md](API_DOCUMENTATION.md)** - Dokumentasi lengkap API
2. **[PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)** - Ringkasan proyek
3. **[database_setup.sql](database_setup.sql)** - Script SQL setup database
4. **[API_Monitoring_Kelas.postman_collection.json](API_Monitoring_Kelas.postman_collection.json)** - Postman Collection

### 🔗 Endpoint Overview

#### Public Endpoint

-   `POST /api/login` - Login user

#### Protected Endpoints (Require Token)

-   `POST /api/logout` - Logout user
-   `GET /api/gurus` - Get all guru
-   `POST /api/gurus` - Create guru
-   `GET /api/gurus/{id}` - Get single guru
-   `PUT /api/gurus/{id}` - Update guru
-   `DELETE /api/gurus/{id}` - Delete guru

### 📱 Base URL untuk Android

```
http://10.0.2.2:8000/api
```

---

## 🧪 Testing

### Data User Testing

Setelah menjalankan seeder, gunakan kredensial berikut:

| Role           | Email                 | Password |
| -------------- | --------------------- | -------- |
| Admin          | admin@example.com     | password |
| Kurikulum      | kurikulum@example.com | password |
| Kepala Sekolah | kepsek@example.com    | password |
| Siswa          | siswa@example.com     | password |

### Postman Testing

1. Import `API_Monitoring_Kelas.postman_collection.json`
2. Set variable `base_url` = `http://127.0.0.1:8000`
3. Login dan copy token
4. Set variable `token` dengan token yang didapat
5. Test endpoint lainnya

---

## 📁 Struktur Proyek

```
aplikasimonitoring-api/
├── app/
│   ├── Http/
│   │   └── Controllers/
│   │       └── Api/
│   │           ├── AuthController.php      # Login & Logout
│   │           └── GuruController.php      # CRUD Guru
│   └── Models/
│       ├── User.php                        # Model User
│       └── Guru.php                        # Model Guru
├── config/
│   ├── cors.php                            # CORS Configuration
│   └── sanctum.php                         # Sanctum Configuration
├── database/
│   ├── migrations/
│   │   ├── 0001_01_01_000000_create_users_table.php
│   │   └── 2025_11_12_020327_create_gurus_table.php
│   └── seeders/
│       ├── UserSeeder.php                  # User Testing Data
│       └── DatabaseSeeder.php
├── routes/
│   ├── api.php                             # API Routes
│   └── web.php
├── .env                                    # Environment Config
├── API_DOCUMENTATION.md                    # Dokumentasi API
├── PROJECT_SUMMARY.md                      # Ringkasan Proyek
├── database_setup.sql                      # SQL Setup Script
└── API_Monitoring_Kelas.postman_collection.json
```

---

## 🗄️ Database Schema

### Table: users

```sql
- id (PK)
- name
- email (UNIQUE)
- password
- role (ENUM: admin, kurikulum, kepala_sekolah, siswa)
- timestamps
```

### Table: gurus

```sql
- id (PK)
- kode_guru (UNIQUE)
- guru
- telepon (NULLABLE)
- timestamps
```

---

## 🔐 Authentication Flow

1. **Login**: Client kirim email, password, dan role
2. **Validation**: Server validasi kredensial dan role
3. **Token Generation**: Server generate token dengan Sanctum
4. **Client Storage**: Client simpan token
5. **Request with Token**: Client kirim token di header `Authorization: Bearer {token}`
6. **Logout**: Server hapus token aktif

---

## 🌐 CORS Configuration

API sudah dikonfigurasi untuk menerima request dari:

-   Localhost
-   Android Emulator (`http://10.0.2.2:8000`)
-   Origin lainnya

File konfigurasi: `config/cors.php`

---

## ⚠️ Error Responses

| Status Code | Keterangan                           |
| ----------- | ------------------------------------ |
| 200         | Success                              |
| 201         | Created                              |
| 401         | Unauthorized (Token invalid/missing) |
| 404         | Resource Not Found                   |
| 422         | Validation Error                     |
| 500         | Internal Server Error                |

---

## 🤝 Kontribusi

Untuk pengembangan lebih lanjut:

1. Fork repository
2. Buat branch baru (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Buat Pull Request

---

## 📝 Lisensi

Proyek ini dibuat untuk keperluan pembelajaran.

---

## 📞 Support

Untuk pertanyaan atau bantuan:

-   📖 Baca [API_DOCUMENTATION.md](API_DOCUMENTATION.md)
-   📋 Lihat [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)
-   🐛 Buat issue di repository

---

## ✅ Checklist Fitur

-   [x] Laravel 12.x Installation
-   [x] Sanctum Authentication
-   [x] MySQL Database
-   [x] User Model with Roles
-   [x] Guru Model & Migration
-   [x] AuthController (Login/Logout)
-   [x] GuruController (CRUD)
-   [x] API Routes
-   [x] CORS Configuration
-   [x] Input Validation
-   [x] Error Handling
-   [x] Database Seeders
-   [x] API Documentation
-   [x] Postman Collection

---

<p align="center">
  Made with ❤️ for Learning Purpose
</p>

## About Laravel

Laravel is a web application framework with expressive, elegant syntax. We believe development must be an enjoyable and creative experience to be truly fulfilling. Laravel takes the pain out of development by easing common tasks used in many web projects, such as:

-   [Simple, fast routing engine](https://laravel.com/docs/routing).
-   [Powerful dependency injection container](https://laravel.com/docs/container).
-   Multiple back-ends for [session](https://laravel.com/docs/session) and [cache](https://laravel.com/docs/cache) storage.
-   Expressive, intuitive [database ORM](https://laravel.com/docs/eloquent).
-   Database agnostic [schema migrations](https://laravel.com/docs/migrations).
-   [Robust background job processing](https://laravel.com/docs/queues).
-   [Real-time event broadcasting](https://laravel.com/docs/broadcasting).

Laravel is accessible, powerful, and provides tools required for large, robust applications.

## Learning Laravel

Laravel has the most extensive and thorough [documentation](https://laravel.com/docs) and video tutorial library of all modern web application frameworks, making it a breeze to get started with the framework. You can also check out [Laravel Learn](https://laravel.com/learn), where you will be guided through building a modern Laravel application.

If you don't feel like reading, [Laracasts](https://laracasts.com) can help. Laracasts contains thousands of video tutorials on a range of topics including Laravel, modern PHP, unit testing, and JavaScript. Boost your skills by digging into our comprehensive video library.

## Laravel Sponsors

We would like to extend our thanks to the following sponsors for funding Laravel development. If you are interested in becoming a sponsor, please visit the [Laravel Partners program](https://partners.laravel.com).

### Premium Partners

-   **[Vehikl](https://vehikl.com)**
-   **[Tighten Co.](https://tighten.co)**
-   **[Kirschbaum Development Group](https://kirschbaumdevelopment.com)**
-   **[64 Robots](https://64robots.com)**
-   **[Curotec](https://www.curotec.com/services/technologies/laravel)**
-   **[DevSquad](https://devsquad.com/hire-laravel-developers)**
-   **[Redberry](https://redberry.international/laravel-development)**
-   **[Active Logic](https://activelogic.com)**

## Contributing

Thank you for considering contributing to the Laravel framework! The contribution guide can be found in the [Laravel documentation](https://laravel.com/docs/contributions).

## Code of Conduct

In order to ensure that the Laravel community is welcoming to all, please review and abide by the [Code of Conduct](https://laravel.com/docs/contributions#code-of-conduct).

## Security Vulnerabilities

If you discover a security vulnerability within Laravel, please send an e-mail to Taylor Otwell via [taylor@laravel.com](mailto:taylor@laravel.com). All security vulnerabilities will be promptly addressed.

## License

The Laravel framework is open-sourced software licensed under the [MIT license](https://opensource.org/licenses/MIT).
