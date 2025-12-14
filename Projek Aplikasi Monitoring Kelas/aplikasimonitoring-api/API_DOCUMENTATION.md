# API Monitoring Kelas - Backend Laravel

Backend RESTful API untuk Aplikasi Monitoring Kelas menggunakan Laravel dengan otentikasi berbasis token (Laravel Sanctum).

## Persyaratan Sistem

-   PHP >= 8.2
-   Composer
-   MySQL >= 5.7
-   Laravel 12.x

## Teknologi yang Digunakan

-   **Framework**: Laravel 12.x
-   **Otentikasi**: Laravel Sanctum (Token-based Authentication)
-   **Database**: MySQL
-   **Arsitektur**: Route -> Controller -> Model -> Database

## Setup Proyek

### 1. Instalasi Dependencies

```bash
composer install
```

### 2. Konfigurasi Environment

Copy file `.env.example` menjadi `.env` (jika belum):

```bash
copy .env.example .env
```

Konfigurasi database di file `.env`:

```env
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=db_monitoring_kelas
DB_USERNAME=root
DB_PASSWORD=
```

### 3. Generate Application Key

```bash
php artisan key:generate
```

### 4. Buat Database

Buat database MySQL dengan nama `db_monitoring_kelas` melalui phpMyAdmin atau MySQL command:

```sql
CREATE DATABASE db_monitoring_kelas;
```

### 5. Jalankan Migrasi

```bash
php artisan migrate:fresh
```

### 6. Jalankan Seeder (Opsional - untuk data testing)

```bash
php artisan db:seed
```

Seeder akan membuat 4 user testing:

-   **Admin**: email: `admin@example.com`, password: `password`, role: `admin`
-   **Kurikulum**: email: `kurikulum@example.com`, password: `password`, role: `kurikulum`
-   **Kepala Sekolah**: email: `kepsek@example.com`, password: `password`, role: `kepala_sekolah`
-   **Siswa**: email: `siswa@example.com`, password: `password`, role: `siswa`

### 7. Jalankan Server

```bash
php artisan serve
```

Server akan berjalan di: `http://127.0.0.1:8000`

Untuk Android Emulator, gunakan: `http://10.0.2.2:8000`

## Struktur Database

### Tabel `users`

| Kolom      | Tipe             | Keterangan                                    |
| ---------- | ---------------- | --------------------------------------------- |
| id         | BIGINT (PK)      | Primary Key                                   |
| name       | VARCHAR          | Nama User                                     |
| email      | VARCHAR (UNIQUE) | Email User                                    |
| password   | VARCHAR          | Password (Hashed)                             |
| role       | ENUM             | Role: admin, kurikulum, kepala_sekolah, siswa |
| created_at | TIMESTAMP        | Waktu Dibuat                                  |
| updated_at | TIMESTAMP        | Waktu Diupdate                                |

### Tabel `gurus`

| Kolom      | Tipe               | Keterangan     |
| ---------- | ------------------ | -------------- |
| id         | BIGINT (PK)        | Primary Key    |
| kode_guru  | VARCHAR (UNIQUE)   | Kode Guru      |
| guru       | VARCHAR            | Nama Guru      |
| telepon    | VARCHAR (NULLABLE) | Nomor Telepon  |
| created_at | TIMESTAMP          | Waktu Dibuat   |
| updated_at | TIMESTAMP          | Waktu Diupdate |

### Tabel `mapels`

| Kolom      | Tipe             | Keterangan     |
| ---------- | ---------------- | -------------- |
| id         | BIGINT (PK)      | Primary Key    |
| kode_mapel | VARCHAR (UNIQUE) | Kode Mapel     |
| mapel      | VARCHAR          | Nama Mapel     |
| created_at | TIMESTAMP        | Waktu Dibuat   |
| updated_at | TIMESTAMP        | Waktu Diupdate |

### Tabel `tahun_ajarans`

| Kolom      | Tipe        | Keterangan               |
| ---------- | ----------- | ------------------------ |
| id         | BIGINT (PK) | Primary Key              |
| tahun      | VARCHAR     | Tahun Ajaran (2024/2025) |
| flag       | BOOLEAN     | Status Aktif (1=aktif)   |
| created_at | TIMESTAMP   | Waktu Dibuat             |
| updated_at | TIMESTAMP   | Waktu Diupdate           |

### Tabel `kelas`

| Kolom      | Tipe                  | Keterangan              |
| ---------- | --------------------- | ----------------------- |
| id         | BIGINT (PK)           | Primary Key             |
| kode_kelas | VARCHAR (UNIQUE)      | Kode Kelas              |
| nama_kelas | VARCHAR               | Nama Kelas              |
| guru_id    | BIGINT (FK, NULLABLE) | ID Guru (Wali Kelas)    |
| tingkat    | VARCHAR               | Tingkat (X, XI, XII)    |
| jurusan    | VARCHAR               | Jurusan (RPL, TKJ, dll) |
| kapasitas  | INTEGER               | Kapasitas Siswa         |
| created_at | TIMESTAMP             | Waktu Dibuat            |
| updated_at | TIMESTAMP             | Waktu Diupdate          |

### Tabel `siswas`

| Kolom         | Tipe                                   | Keterangan        |
| ------------- | -------------------------------------- | ----------------- |
| id            | BIGINT (PK)                            | Primary Key       |
| nis           | VARCHAR (UNIQUE)                       | Nomor Induk Siswa |
| nama          | VARCHAR                                | Nama Siswa        |
| jenis_kelamin | ENUM ('L', 'P')                        | Jenis Kelamin     |
| tempat_lahir  | VARCHAR (NULLABLE)                     | Tempat Lahir      |
| tanggal_lahir | DATE (NULLABLE)                        | Tanggal Lahir     |
| alamat        | TEXT (NULLABLE)                        | Alamat            |
| telepon       | VARCHAR (NULLABLE)                     | Nomor Telepon     |
| email         | VARCHAR (NULLABLE)                     | Email             |
| kelas_id      | BIGINT (FK, NULLABLE)                  | ID Kelas          |
| user_id       | BIGINT (FK, NULLABLE)                  | ID User           |
| status        | ENUM (aktif, non-aktif, lulus, pindah) | Status Siswa      |
| created_at    | TIMESTAMP                              | Waktu Dibuat      |
| updated_at    | TIMESTAMP                              | Waktu Diupdate    |

### Tabel `absensi_siswas`

| Kolom      | Tipe                             | Keterangan       |
| ---------- | -------------------------------- | ---------------- |
| id         | BIGINT (PK)                      | Primary Key      |
| siswa_id   | BIGINT (FK)                      | ID Siswa         |
| kelas_id   | BIGINT (FK)                      | ID Kelas         |
| mapel_id   | BIGINT (FK, NULLABLE)            | ID Mapel         |
| tanggal    | DATE                             | Tanggal Absensi  |
| status     | ENUM (hadir, sakit, izin, alpha) | Status Kehadiran |
| keterangan | TEXT (NULLABLE)                  | Keterangan       |
| jam_masuk  | TIME (NULLABLE)                  | Jam Masuk        |
| jam_keluar | TIME (NULLABLE)                  | Jam Keluar       |
| created_at | TIMESTAMP                        | Waktu Dibuat     |
| updated_at | TIMESTAMP                        | Waktu Diupdate   |

### Tabel `absensi_gurus`

| Kolom      | Tipe                                    | Keterangan       |
| ---------- | --------------------------------------- | ---------------- |
| id         | BIGINT (PK)                             | Primary Key      |
| guru_id    | BIGINT (FK)                             | ID Guru          |
| mapel_id   | BIGINT (FK, NULLABLE)                   | ID Mapel         |
| kelas_id   | BIGINT (FK, NULLABLE)                   | ID Kelas         |
| tanggal    | DATE                                    | Tanggal Absensi  |
| status     | ENUM (hadir, sakit, izin, alpha, dinas) | Status Kehadiran |
| keterangan | TEXT (NULLABLE)                         | Keterangan       |
| jam_masuk  | TIME (NULLABLE)                         | Jam Masuk        |
| jam_keluar | TIME (NULLABLE)                         | Jam Keluar       |
| created_at | TIMESTAMP                               | Waktu Dibuat     |
| updated_at | TIMESTAMP                               | Waktu Diupdate   |
| created_at | TIMESTAMP                               | Waktu Dibuat     |
| updated_at | TIMESTAMP                               | Waktu Diupdate   |

## Endpoint API

### Base URL

```
http://127.0.0.1:8000/api
```

### 1. Otentikasi

#### Login (Public)

-   **Endpoint**: `POST /api/login`
-   **Headers**:
    ```
    Content-Type: application/json
    Accept: application/json
    ```
-   **Body**:
    ```json
    {
        "email": "admin@example.com",
        "password": "password",
        "role": "admin"
    }
    ```
-   **Response Success (200)**:
    ```json
    {
        "success": true,
        "message": "Login successful",
        "data": {
            "user": {
                "id": 1,
                "name": "Admin User",
                "email": "admin@example.com",
                "role": "admin"
            },
            "token": "1|abcdefghijklmnopqrstuvwxyz...",
            "token_type": "Bearer"
        }
    }
    ```

#### Logout (Protected)

-   **Endpoint**: `POST /api/logout`
-   **Headers**:
    ```
    Content-Type: application/json
    Accept: application/json
    Authorization: Bearer {token}
    ```
-   **Response Success (200)**:
    ```json
    {
        "success": true,
        "message": "Logout successful"
    }
    ```

### 2. CRUD Data Guru (Protected - Memerlukan Token)

Semua endpoint guru memerlukan header:

```
Authorization: Bearer {token}
Content-Type: application/json
Accept: application/json
```

#### Get All Guru

-   **Endpoint**: `GET /api/gurus`
-   **Response Success (200)**:
    ```json
    {
        "success": true,
        "message": "Data guru berhasil diambil",
        "data": [
            {
                "id": 1,
                "kode_guru": "GR001",
                "guru": "John Doe",
                "telepon": "081234567890",
                "created_at": "2025-11-12T02:00:00.000000Z",
                "updated_at": "2025-11-12T02:00:00.000000Z"
            }
        ]
    }
    ```

#### Get Single Guru

-   **Endpoint**: `GET /api/gurus/{id}`
-   **Response Success (200)**:
    ```json
    {
        "success": true,
        "message": "Data guru berhasil diambil",
        "data": {
            "id": 1,
            "kode_guru": "GR001",
            "guru": "John Doe",
            "telepon": "081234567890",
            "created_at": "2025-11-12T02:00:00.000000Z",
            "updated_at": "2025-11-12T02:00:00.000000Z"
        }
    }
    ```
-   **Response Not Found (404)**:
    ```json
    {
        "success": false,
        "message": "Data guru tidak ditemukan"
    }
    ```

#### Create Guru

-   **Endpoint**: `POST /api/gurus`
-   **Body**:
    ```json
    {
        "kode_guru": "GR001",
        "guru": "John Doe",
        "telepon": "081234567890"
    }
    ```
-   **Response Success (201)**:
    ```json
    {
        "success": true,
        "message": "Data guru berhasil ditambahkan",
        "data": {
            "id": 1,
            "kode_guru": "GR001",
            "guru": "John Doe",
            "telepon": "081234567890",
            "created_at": "2025-11-12T02:00:00.000000Z",
            "updated_at": "2025-11-12T02:00:00.000000Z"
        }
    }
    ```
-   **Response Validation Error (422)**:
    ```json
    {
        "success": false,
        "message": "Validasi gagal",
        "errors": {
            "kode_guru": ["The kode guru has already been taken."]
        }
    }
    ```

#### Update Guru

-   **Endpoint**: `PUT /api/gurus/{id}` atau `PATCH /api/gurus/{id}`
-   **Body**:
    ```json
    {
        "kode_guru": "GR001",
        "guru": "John Doe Updated",
        "telepon": "081234567890"
    }
    ```
-   **Response Success (200)**:
    ```json
    {
        "success": true,
        "message": "Data guru berhasil diupdate",
        "data": {
            "id": 1,
            "kode_guru": "GR001",
            "guru": "John Doe Updated",
            "telepon": "081234567890",
            "created_at": "2025-11-12T02:00:00.000000Z",
            "updated_at": "2025-11-12T02:05:00.000000Z"
        }
    }
    ```

#### Delete Guru

-   **Endpoint**: `DELETE /api/gurus/{id}`
-   **Response Success (200)**:
    ```json
    {
        "success": true,
        "message": "Data guru berhasil dihapus"
    }
    ```

## Testing dengan Postman/Thunder Client

### 1. Login

1. Method: `POST`
2. URL: `http://127.0.0.1:8000/api/login`
3. Headers:
    - `Content-Type: application/json`
    - `Accept: application/json`
4. Body (raw JSON):
    ```json
    {
        "email": "admin@example.com",
        "password": "password",
        "role": "admin"
    }
    ```
5. Simpan token yang didapat dari response

### 2. Test CRUD Guru

Gunakan token dari login di header:

-   `Authorization: Bearer {token_dari_login}`
-   `Content-Type: application/json`
-   `Accept: application/json`

#### CREATE

-   Method: `POST`
-   URL: `http://127.0.0.1:8000/api/gurus`
-   Body:
    ```json
    {
        "kode_guru": "GR001",
        "guru": "Budi Santoso",
        "telepon": "081234567890"
    }
    ```

#### READ ALL

-   Method: `GET`
-   URL: `http://127.0.0.1:8000/api/gurus`

#### READ ONE

-   Method: `GET`
-   URL: `http://127.0.0.1:8000/api/gurus/1`

#### UPDATE

-   Method: `PUT` atau `PATCH`
-   URL: `http://127.0.0.1:8000/api/gurus/1`
-   Body:
    ```json
    {
        "kode_guru": "GR001",
        "guru": "Budi Santoso Updated",
        "telepon": "081234567891"
    }
    ```

#### DELETE

-   Method: `DELETE`
-   URL: `http://127.0.0.1:8000/api/gurus/1`

### 3. Logout

-   Method: `POST`
-   URL: `http://127.0.0.1:8000/api/logout`
-   Headers:
    -   `Authorization: Bearer {token}`

## Error Handling

API ini mengimplementasikan error handling untuk berbagai kondisi:

### 401 Unauthorized

Terjadi ketika token tidak valid atau tidak disediakan:

```json
{
    "message": "Unauthenticated."
}
```

### 404 Not Found

Terjadi ketika resource tidak ditemukan:

```json
{
    "success": false,
    "message": "Data guru tidak ditemukan"
}
```

### 422 Validation Error

Terjadi ketika validasi input gagal:

```json
{
    "success": false,
    "message": "Validasi gagal",
    "errors": {
        "kode_guru": ["The kode guru field is required."]
    }
}
```

### 500 Internal Server Error

Terjadi ketika ada kesalahan di server:

```json
{
    "success": false,
    "message": "Terjadi kesalahan pada server",
    "error": "Error message details"
}
```

## CORS Configuration

API sudah dikonfigurasi untuk menerima request dari berbagai origin termasuk Android emulator (`http://10.0.2.2:8000`).

Konfigurasi CORS ada di:

-   `config/cors.php`
-   `bootstrap/app.php`

## File-File Penting

### Controllers

-   `app/Http/Controllers/Api/AuthController.php` - Menangani login dan logout
-   `app/Http/Controllers/Api/GuruController.php` - Menangani CRUD data guru

### Models

-   `app/Models/User.php` - Model untuk tabel users
-   `app/Models/Guru.php` - Model untuk tabel gurus

### Migrations

-   `database/migrations/0001_01_01_000000_create_users_table.php` - Migrasi tabel users
-   `database/migrations/2025_11_12_020327_create_gurus_table.php` - Migrasi tabel gurus
-   `database/migrations/2019_12_14_000001_create_personal_access_tokens_table.php` - Migrasi untuk Sanctum

### Routes

-   `routes/api.php` - Definisi semua endpoint API

### Seeders

-   `database/seeders/UserSeeder.php` - Seeder untuk data user testing
-   `database/seeders/DatabaseSeeder.php` - Main seeder

## Keamanan

1. **Token-based Authentication**: Menggunakan Laravel Sanctum untuk otentikasi berbasis token
2. **Password Hashing**: Password di-hash menggunakan bcrypt
3. **Role-based Access**: Sistem role untuk membedakan akses user
4. **Validation**: Input validation di setiap endpoint
5. **CORS Protection**: Konfigurasi CORS untuk membatasi akses

## Troubleshooting

### Database Connection Error

Pastikan:

-   MySQL server berjalan
-   Database `db_monitoring_kelas` sudah dibuat
-   Kredensial di `.env` sudah benar

### Token Not Working

Pastikan:

-   Migrasi Sanctum sudah dijalankan
-   Token disimpan dengan format: `Bearer {token}`
-   Token dikirim di header `Authorization`

### CORS Error

Pastikan:

-   File `config/cors.php` sudah ada
-   Middleware CORS sudah terdaftar di `bootstrap/app.php`

## Pengembangan Lebih Lanjut

✅ **SUDAH DIIMPLEMENTASIKAN:**

### CRUD Entitas Lengkap

-   ✅ **Kelas** - Manajemen data kelas dengan wali kelas
-   ✅ **Siswa** - Manajemen data siswa dengan detail lengkap
-   ✅ **Absensi Siswa** - Pencatatan kehadiran siswa per mapel
-   ✅ **Absensi Guru** - Pencatatan kehadiran guru

### Fitur Advanced

-   ✅ **Pagination** - Semua endpoint list mendukung pagination
-   ✅ **Search** - Pencarian data berdasarkan keyword
-   ✅ **Filter** - Filter berdasarkan berbagai kriteria (kelas, status, tanggal, dll)

### Dokumentasi Endpoint Lengkap

## 3. CRUD Data Kelas (Protected)

### Get All Kelas (dengan Pagination & Filter)

-   **Endpoint**: `GET /api/kelas`
-   **Query Parameters**:
    -   `per_page` (optional): Jumlah data per halaman (default: 10)
    -   `search` (optional): Pencarian berdasarkan kode atau nama kelas
    -   `tingkat` (optional): Filter berdasarkan tingkat (X, XI, XII)
    -   `jurusan` (optional): Filter berdasarkan jurusan
-   **Example**: `GET /api/kelas?per_page=5&tingkat=XI&jurusan=RPL`
-   **Response Success (200)**:
    ```json
    {
        "success": true,
        "message": "Data kelas berhasil diambil",
        "data": [
            {
                "id": 1,
                "kode_kelas": "XI-RPL-1",
                "nama_kelas": "XI RPL 1",
                "guru_id": 1,
                "tingkat": "XI",
                "jurusan": "RPL",
                "kapasitas": 36,
                "guru": {
                    "id": 1,
                    "kode_guru": "GR001",
                    "guru": "Budi Santoso"
                }
            }
        ],
        "pagination": {
            "total": 50,
            "per_page": 10,
            "current_page": 1,
            "last_page": 5,
            "from": 1,
            "to": 10
        }
    }
    ```

### Get Single Kelas

-   **Endpoint**: `GET /api/kelas/{id}`
-   **Response Success (200)**:
    ```json
    {
        "success": true,
        "message": "Data kelas berhasil diambil",
        "data": {
            "id": 1,
            "kode_kelas": "XI-RPL-1",
            "nama_kelas": "XI RPL 1",
            "guru_id": 1,
            "tingkat": "XI",
            "jurusan": "RPL",
            "kapasitas": 36,
            "guru": { ... },
            "siswas": [ ... ]
        }
    }
    ```

### Create Kelas

-   **Endpoint**: `POST /api/kelas`
-   **Body**:
    ```json
    {
        "kode_kelas": "XI-RPL-1",
        "nama_kelas": "XI RPL 1",
        "guru_id": 1,
        "tingkat": "XI",
        "jurusan": "RPL",
        "kapasitas": 36
    }
    ```

### Update Kelas

-   **Endpoint**: `PUT /api/kelas/{id}`
-   **Body**: Same as Create

### Delete Kelas

-   **Endpoint**: `DELETE /api/kelas/{id}`

---

## 4. CRUD Data Siswa (Protected)

### Get All Siswa (dengan Pagination & Filter)

-   **Endpoint**: `GET /api/siswas`
-   **Query Parameters**:
    -   `per_page` (optional): Jumlah data per halaman
    -   `search` (optional): Pencarian berdasarkan NIS, nama, atau email
    -   `kelas_id` (optional): Filter berdasarkan kelas
    -   `status` (optional): Filter berdasarkan status (aktif, non-aktif, lulus, pindah)
    -   `jenis_kelamin` (optional): Filter berdasarkan jenis kelamin (L, P)
-   **Example**: `GET /api/siswas?per_page=20&kelas_id=3&status=aktif`
-   **Response Success (200)**:
    ```json
    {
        "success": true,
        "message": "Data siswa berhasil diambil",
        "data": [
            {
                "id": 1,
                "nis": "2021001",
                "nama": "Ahmad Fauzi",
                "jenis_kelamin": "L",
                "tempat_lahir": "Jakarta",
                "tanggal_lahir": "2005-01-15",
                "alamat": "Jl. Merdeka No. 10",
                "telepon": "081234567801",
                "email": "ahmad.fauzi@example.com",
                "kelas_id": 3,
                "user_id": 8,
                "status": "aktif",
                "kelas": { ... },
                "user": { ... }
            }
        ],
        "pagination": { ... }
    }
    ```

### Create Siswa

-   **Endpoint**: `POST /api/siswas`
-   **Body**:
    ```json
    {
        "nis": "2021001",
        "nama": "Ahmad Fauzi",
        "jenis_kelamin": "L",
        "tempat_lahir": "Jakarta",
        "tanggal_lahir": "2005-01-15",
        "alamat": "Jl. Merdeka No. 10",
        "telepon": "081234567801",
        "email": "ahmad.fauzi@example.com",
        "kelas_id": 3,
        "user_id": 8,
        "status": "aktif"
    }
    ```

### Update Siswa

-   **Endpoint**: `PUT /api/siswas/{id}`
-   **Body**: Same as Create

### Get Single Siswa

-   **Endpoint**: `GET /api/siswas/{id}`

### Delete Siswa

-   **Endpoint**: `DELETE /api/siswas/{id}`

---

## 5. CRUD Absensi Siswa (Protected)

### Get All Absensi Siswa (dengan Pagination & Filter)

-   **Endpoint**: `GET /api/absensi-siswas`
-   **Query Parameters**:
    -   `per_page` (optional): Jumlah data per halaman
    -   `siswa_id` (optional): Filter berdasarkan siswa
    -   `kelas_id` (optional): Filter berdasarkan kelas
    -   `mapel_id` (optional): Filter berdasarkan mapel
    -   `tanggal_from` (optional): Filter tanggal mulai (format: YYYY-MM-DD)
    -   `tanggal_to` (optional): Filter tanggal akhir (format: YYYY-MM-DD)
    -   `status` (optional): Filter berdasarkan status (hadir, sakit, izin, alpha)
-   **Example**: `GET /api/absensi-siswas?siswa_id=1&tanggal_from=2025-01-01&tanggal_to=2025-01-31`
-   **Response Success (200)**:
    ```json
    {
        "success": true,
        "message": "Data absensi siswa berhasil diambil",
        "data": [
            {
                "id": 1,
                "siswa_id": 1,
                "kelas_id": 3,
                "mapel_id": 1,
                "tanggal": "2025-01-15",
                "status": "hadir",
                "keterangan": null,
                "jam_masuk": "07:00:00",
                "jam_keluar": "14:00:00",
                "siswa": { ... },
                "kelas": { ... },
                "mapel": { ... }
            }
        ],
        "pagination": { ... }
    }
    ```

### Create Absensi Siswa

-   **Endpoint**: `POST /api/absensi-siswas`
-   **Body**:
    ```json
    {
        "siswa_id": 1,
        "kelas_id": 3,
        "mapel_id": 1,
        "tanggal": "2025-01-15",
        "status": "hadir",
        "keterangan": "",
        "jam_masuk": "07:00",
        "jam_keluar": "14:00"
    }
    ```

### Update Absensi Siswa

-   **Endpoint**: `PUT /api/absensi-siswas/{id}`
-   **Body**: Same as Create

### Get Single Absensi Siswa

-   **Endpoint**: `GET /api/absensi-siswas/{id}`

### Delete Absensi Siswa

-   **Endpoint**: `DELETE /api/absensi-siswas/{id}`

---

## 6. CRUD Absensi Guru (Protected)

### Get All Absensi Guru (dengan Pagination & Filter)

-   **Endpoint**: `GET /api/absensi-gurus`
-   **Query Parameters**:
    -   `per_page` (optional): Jumlah data per halaman
    -   `guru_id` (optional): Filter berdasarkan guru
    -   `mapel_id` (optional): Filter berdasarkan mapel
    -   `kelas_id` (optional): Filter berdasarkan kelas
    -   `tanggal_from` (optional): Filter tanggal mulai
    -   `tanggal_to` (optional): Filter tanggal akhir
    -   `status` (optional): Filter berdasarkan status (hadir, sakit, izin, alpha, dinas)
-   **Example**: `GET /api/absensi-gurus?guru_id=1&status=hadir`
-   **Response Success (200)**:
    ```json
    {
        "success": true,
        "message": "Data absensi guru berhasil diambil",
        "data": [
            {
                "id": 1,
                "guru_id": 1,
                "mapel_id": 1,
                "kelas_id": 3,
                "tanggal": "2025-01-15",
                "status": "hadir",
                "keterangan": null,
                "jam_masuk": "06:45:00",
                "jam_keluar": "14:30:00",
                "guru": { ... },
                "mapel": { ... },
                "kelas": { ... }
            }
        ],
        "pagination": { ... }
    }
    ```

### Create Absensi Guru

-   **Endpoint**: `POST /api/absensi-gurus`
-   **Body**:
    ```json
    {
        "guru_id": 1,
        "mapel_id": 1,
        "kelas_id": 3,
        "tanggal": "2025-01-15",
        "status": "hadir",
        "keterangan": "",
        "jam_masuk": "06:45",
        "jam_keluar": "14:30"
    }
    ```

### Update Absensi Guru

-   **Endpoint**: `PUT /api/absensi-gurus/{id}`
-   **Body**: Same as Create

### Get Single Absensi Guru

-   **Endpoint**: `GET /api/absensi-gurus/{id}`

### Delete Absensi Guru

-   **Endpoint**: `DELETE /api/absensi-gurus/{id}`

---

## Fitur Pagination, Search & Filter

### Pagination

Semua endpoint list (GET All) mendukung pagination dengan parameter:

-   `per_page`: Jumlah data per halaman (default: 10)

Response pagination mencakup:

```json
"pagination": {
    "total": 100,           // Total semua data
    "per_page": 10,         // Data per halaman
    "current_page": 1,      // Halaman saat ini
    "last_page": 10,        // Halaman terakhir
    "from": 1,              // Data dimulai dari
    "to": 10                // Data sampai
}
```

### Search

Endpoint yang mendukung search:

-   **Kelas**: Search by `kode_kelas` atau `nama_kelas`
-   **Siswa**: Search by `nis`, `nama`, atau `email`

Example: `GET /api/siswas?search=ahmad`

### Filter

Setiap endpoint memiliki filter spesifik:

-   **Kelas**: `tingkat`, `jurusan`
-   **Siswa**: `kelas_id`, `status`, `jenis_kelamin`
-   **Absensi Siswa**: `siswa_id`, `kelas_id`, `mapel_id`, `tanggal_from`, `tanggal_to`, `status`
-   **Absensi Guru**: `guru_id`, `mapel_id`, `kelas_id`, `tanggal_from`, `tanggal_to`, `status`

Example kombinasi:

```
GET /api/absensi-siswas?kelas_id=3&status=hadir&tanggal_from=2025-01-01&tanggal_to=2025-01-31&per_page=20
```

---

## Fitur Tambahan yang Dapat Dikembangkan

Anda bisa menambahkan fitur-fitur tambahan seperti:

-   Upload file/gambar
-   Middleware untuk role-based access control
-   API Documentation dengan Swagger/OpenAPI

## Lisensi

Proyek ini dibuat untuk keperluan pembelajaran.
