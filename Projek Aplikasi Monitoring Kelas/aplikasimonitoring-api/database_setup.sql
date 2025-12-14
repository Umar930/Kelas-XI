-- ============================================
-- Script SQL untuk Aplikasi Monitoring Kelas
-- ============================================

-- 1. Buat Database
CREATE DATABASE IF NOT EXISTS db_sekolah;
USE db_sekolah;

-- 2. Tabel Users
CREATE TABLE IF NOT EXISTS users (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    email_verified_at TIMESTAMP NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'kurikulum', 'kepala_sekolah', 'siswa') NOT NULL DEFAULT 'siswa',
    remember_token VARCHAR(100) NULL,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Tabel Personal Access Tokens (untuk Sanctum)
CREATE TABLE IF NOT EXISTS personal_access_tokens (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    tokenable_type VARCHAR(255) NOT NULL,
    tokenable_id BIGINT UNSIGNED NOT NULL,
    name VARCHAR(255) NOT NULL,
    token VARCHAR(64) NOT NULL UNIQUE,
    abilities TEXT NULL,
    last_used_at TIMESTAMP NULL,
    expires_at TIMESTAMP NULL,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    INDEX personal_access_tokens_tokenable_type_tokenable_id_index (tokenable_type, tokenable_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Tabel Gurus
CREATE TABLE IF NOT EXISTS gurus (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    kode_guru VARCHAR(255) NOT NULL UNIQUE,
    guru VARCHAR(255) NOT NULL,
    telepon VARCHAR(255) NULL,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Tabel Mapel (Mata Pelajaran)
CREATE TABLE IF NOT EXISTS mapels (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    kode_mapel VARCHAR(255) NOT NULL UNIQUE,
    mapel VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Tabel Tahun Ajaran
CREATE TABLE IF NOT EXISTS tahun_ajarans (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    tahun VARCHAR(255) NOT NULL,
    flag TINYINT(1) NOT NULL DEFAULT 1 COMMENT '0=tidak aktif, 1=aktif',
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Tabel Password Reset Tokens
CREATE TABLE IF NOT EXISTS password_reset_tokens (
    email VARCHAR(255) PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. Tabel Sessions
CREATE TABLE IF NOT EXISTS sessions (
    id VARCHAR(255) PRIMARY KEY,
    user_id BIGINT UNSIGNED NULL,
    ip_address VARCHAR(45) NULL,
    user_agent TEXT NULL,
    payload LONGTEXT NOT NULL,
    last_activity INT NOT NULL,
    INDEX sessions_user_id_index (user_id),
    INDEX sessions_last_activity_index (last_activity)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 9. Tabel Cache
CREATE TABLE IF NOT EXISTS cache (
    `key` VARCHAR(255) PRIMARY KEY,
    value MEDIUMTEXT NOT NULL,
    expiration INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS cache_locks (
    `key` VARCHAR(255) PRIMARY KEY,
    owner VARCHAR(255) NOT NULL,
    expiration INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 10. Tabel Jobs
CREATE TABLE IF NOT EXISTS jobs (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    queue VARCHAR(255) NOT NULL,
    payload LONGTEXT NOT NULL,
    attempts TINYINT UNSIGNED NOT NULL,
    reserved_at INT UNSIGNED NULL,
    available_at INT UNSIGNED NOT NULL,
    created_at INT UNSIGNED NOT NULL,
    INDEX jobs_queue_index (queue)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS job_batches (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    total_jobs INT NOT NULL,
    pending_jobs INT NOT NULL,
    failed_jobs INT NOT NULL,
    failed_job_ids LONGTEXT NOT NULL,
    options MEDIUMTEXT NULL,
    cancelled_at INT NULL,
    created_at INT NOT NULL,
    finished_at INT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS failed_jobs (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    uuid VARCHAR(255) NOT NULL UNIQUE,
    connection TEXT NOT NULL,
    queue TEXT NOT NULL,
    payload LONGTEXT NOT NULL,
    exception LONGTEXT NOT NULL,
    failed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 9. Insert Data User untuk Testing
-- Password untuk semua user: "password" (Hash: $2y$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi)
INSERT INTO users (name, email, password, role, created_at, updated_at) VALUES
-- Admin Users
('Administrator', 'admin@monitoring.com', '$2y$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'admin', NOW(), NOW()),

-- Kurikulum Users
('Kurikulum User', 'kurikulum@example.com', '$2y$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'kurikulum', NOW(), NOW()),

-- Kepala Sekolah Users
('Kepala Sekolah', 'kepsek@example.com', '$2y$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'kepala_sekolah', NOW(), NOW()),
-- Siswa Users
('Siswa User', 'siswa@example.com', '$2y$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'siswa', NOW(), NOW());

-- Catatan: Password untuk semua user di atas adalah "password"
-- Hash: $2y$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi

-- 11. Insert Data Guru untuk Testing (Opsional)
INSERT INTO gurus (kode_guru, guru, telepon, created_at, updated_at) VALUES
('GR001', 'Budi Santoso', '081234567890', NOW(), NOW()),
('GR002', 'Ani Wijaya', '081234567891', NOW(), NOW()),
('GR003', 'Citra Dewi', '081234567892', NOW(), NOW());

-- 12. Insert Data Mapel untuk Testing (Opsional)
INSERT INTO mapels (kode_mapel, mapel, created_at, updated_at) VALUES
('MP001', 'Matematika', NOW(), NOW()),
('MP002', 'Bahasa Indonesia', NOW(), NOW()),
('MP003', 'Bahasa Inggris', NOW(), NOW()),
('MP004', 'Bahasa Jerman', NOW(), NOW()),
('MP005', 'Bahasa Jepang', NOW(), NOW()),
('MP006', 'Pendidikan Pancasila', NOW(), NOW()),
('MP007', 'Sejarah', NOW(), NOW()),
('MP008', 'Bahasa Jawa', NOW(), NOW()),
('MP009', 'Pendidikan Agama Islam', NOW(), NOW()),
('MP010', 'Pjok', NOW(), NOW());

-- 13. Insert Data Tahun Ajaran untuk Testing (Opsional)
INSERT INTO tahun_ajarans (tahun, flag, created_at, updated_at) VALUES
('2023/2024', 0, NOW(), NOW()),
('2024/2025', 1, NOW(), NOW()),
('2025/2026', 0, NOW(), NOW());
