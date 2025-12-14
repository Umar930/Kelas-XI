-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 14 Des 2025 pada 10.16
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_sekolah`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `absensi_gurus`
--

CREATE TABLE `absensi_gurus` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `guru_id` bigint(20) UNSIGNED NOT NULL,
  `mapel_id` bigint(20) UNSIGNED DEFAULT NULL,
  `kelas_id` bigint(20) UNSIGNED DEFAULT NULL,
  `tanggal` date NOT NULL,
  `status` enum('hadir','sakit','izin','alpha','dinas') NOT NULL DEFAULT 'hadir',
  `keterangan` text DEFAULT NULL,
  `jam_masuk` time DEFAULT NULL,
  `jam_keluar` time DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `absensi_gurus`
--

INSERT INTO `absensi_gurus` (`id`, `guru_id`, `mapel_id`, `kelas_id`, `tanggal`, `status`, `keterangan`, `jam_masuk`, `jam_keluar`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 3, '2025-12-14', 'hadir', NULL, '06:45:00', '14:30:00', '2025-12-13 22:30:11', '2025-12-13 22:30:11'),
(2, 2, 2, NULL, '2025-12-14', 'hadir', NULL, '06:50:00', '14:30:00', '2025-12-13 22:30:11', '2025-12-13 22:30:11'),
(3, 3, 3, 1, '2025-12-14', 'hadir', NULL, '07:00:00', '14:30:00', '2025-12-13 22:30:11', '2025-12-13 22:30:11'),
(4, 1, 1, 3, '2025-12-13', 'hadir', NULL, '06:45:00', '14:30:00', '2025-12-13 22:30:11', '2025-12-13 22:30:11'),
(5, 2, 2, NULL, '2025-12-13', 'sakit', 'Flu', NULL, NULL, '2025-12-13 22:30:11', '2025-12-13 22:30:11'),
(6, 3, 3, 1, '2025-12-13', 'dinas', 'Pelatihan di kantor dinas', NULL, NULL, '2025-12-13 22:30:11', '2025-12-13 22:30:11'),
(7, 1, 1, 3, '2025-12-14', 'hadir', NULL, '06:45:00', '14:30:00', '2025-12-13 22:45:42', '2025-12-13 22:45:42'),
(8, 2, 2, NULL, '2025-12-14', 'hadir', NULL, '06:50:00', '14:30:00', '2025-12-13 22:45:42', '2025-12-13 22:45:42'),
(9, 3, 3, 1, '2025-12-14', 'hadir', NULL, '07:00:00', '14:30:00', '2025-12-13 22:45:42', '2025-12-13 22:45:42'),
(10, 1, 1, 3, '2025-12-13', 'hadir', NULL, '06:45:00', '14:30:00', '2025-12-13 22:45:42', '2025-12-13 22:45:42'),
(11, 2, 2, NULL, '2025-12-13', 'sakit', 'Flu', NULL, NULL, '2025-12-13 22:45:42', '2025-12-13 22:45:42'),
(12, 3, 3, 1, '2025-12-13', 'dinas', 'Pelatihan di kantor dinas', NULL, NULL, '2025-12-13 22:45:42', '2025-12-13 22:45:42'),
(13, 1, 1, 3, '2025-12-14', 'hadir', NULL, '06:45:00', '14:30:00', '2025-12-13 22:56:35', '2025-12-13 22:56:35'),
(14, 2, 2, NULL, '2025-12-14', 'hadir', NULL, '06:50:00', '14:30:00', '2025-12-13 22:56:35', '2025-12-13 22:56:35'),
(15, 3, 3, 1, '2025-12-14', 'hadir', NULL, '07:00:00', '14:30:00', '2025-12-13 22:56:35', '2025-12-13 22:56:35'),
(16, 1, 1, 3, '2025-12-13', 'hadir', NULL, '06:45:00', '14:30:00', '2025-12-13 22:56:35', '2025-12-13 22:56:35'),
(17, 2, 2, NULL, '2025-12-13', 'sakit', 'Flu', NULL, NULL, '2025-12-13 22:56:35', '2025-12-13 22:56:35'),
(18, 3, 3, 1, '2025-12-13', 'dinas', 'Pelatihan di kantor dinas', NULL, NULL, '2025-12-13 22:56:35', '2025-12-13 22:56:35');

-- --------------------------------------------------------

--
-- Struktur dari tabel `cache`
--

CREATE TABLE `cache` (
  `key` varchar(255) NOT NULL,
  `value` mediumtext NOT NULL,
  `expiration` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `cache_locks`
--

CREATE TABLE `cache_locks` (
  `key` varchar(255) NOT NULL,
  `owner` varchar(255) NOT NULL,
  `expiration` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `failed_jobs`
--

CREATE TABLE `failed_jobs` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `uuid` varchar(255) NOT NULL,
  `connection` text NOT NULL,
  `queue` text NOT NULL,
  `payload` longtext NOT NULL,
  `exception` longtext NOT NULL,
  `failed_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `gurus`
--

CREATE TABLE `gurus` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `kode_guru` varchar(255) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `no_telepon` varchar(255) DEFAULT NULL,
  `mata_pelajaran` varchar(100) DEFAULT NULL,
  `alamat` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `gurus`
--

INSERT INTO `gurus` (`id`, `kode_guru`, `nama`, `email`, `no_telepon`, `mata_pelajaran`, `alamat`, `created_at`, `updated_at`) VALUES
(1, 'G001', 'Budi Santoso', 'budi.santoso01@smk.sch.id', '081234567801', 'Pemrograman Web', 'Jl. Teknologi No. 1, Surabaya', '2025-12-13 22:27:35', '2025-12-13 22:27:35'),
(2, 'G002', 'Ani Wijaya', 'ani.wijaya@smk.sch.id', '081234567802', 'Basis Data', 'Jl. Teknologi No. 2, Surabaya', '2025-12-13 22:27:35', '2025-12-13 22:27:35'),
(3, 'G003', 'Citra Dewi', 'citra.dewi@smk.sch.id', '081234567803', 'Pemrograman Mobile', 'Jl. Teknologi No. 3, Surabaya', '2025-12-13 22:27:35', '2025-12-13 22:27:35'),
(4, 'G004', 'Tejo', 'ahmad.subarjo@smk.sch.id', '08123456789', 'Matematika', 'Jl. Pendidikan No. 4, Surabaya', '2025-12-13 22:27:35', '2025-12-14 01:48:32'),
(5, 'G005', 'Siti Nurhaliza, S.Pd', 'siti.nurhaliza@smk.sch.id', '08234567890', 'Bahasa Indonesia', 'Jl. Pendidikan No. 5, Surabaya', '2025-12-13 22:27:35', '2025-12-13 22:27:35'),
(6, 'G006', 'Joni', 'budi.santoso02@smk.sch.id', '08345678901', 'Pemrograman Web dan Mobile', 'Jl. Teknologi No. 6, Surabaya', '2025-12-13 22:27:35', '2025-12-14 01:48:13'),
(7, 'G007', 'Dewi Lestari, M.Si', 'dewi.lestari@smk.sch.id', '08456789012', 'Fisika', 'Jl. Sains No. 7, Surabaya', '2025-12-13 22:27:35', '2025-12-13 22:27:35'),
(8, 'G008', 'Eko Prasetyo, S.Pd', 'eko.prasetyo@smk.sch.id', '08567890123', 'Bahasa Inggris', 'Jl. Bahasa No. 8, Surabaya', '2025-12-13 22:27:35', '2025-12-13 22:27:35');

-- --------------------------------------------------------

--
-- Struktur dari tabel `guru_mengajar`
--

CREATE TABLE `guru_mengajar` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tahun_ajaran_id` bigint(20) UNSIGNED NOT NULL,
  `guru_id` bigint(20) UNSIGNED NOT NULL,
  `mapel_id` bigint(20) UNSIGNED NOT NULL,
  `kelas_id` bigint(20) UNSIGNED NOT NULL,
  `jam_per_minggu` int(11) DEFAULT NULL,
  `keterangan` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `guru_pengganti`
--

CREATE TABLE `guru_pengganti` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `kehadiran_guru_id` bigint(20) UNSIGNED DEFAULT NULL,
  `guru_pengganti_id` bigint(20) UNSIGNED NOT NULL,
  `kelas_id` bigint(20) UNSIGNED NOT NULL,
  `mapel_id` bigint(20) UNSIGNED NOT NULL,
  `tanggal` date NOT NULL,
  `jam_mulai` time NOT NULL,
  `jam_selesai` time NOT NULL,
  `assigned_by_kurikulum_id` bigint(20) UNSIGNED DEFAULT NULL,
  `status` enum('pending','aktif','selesai','ditolak') NOT NULL DEFAULT 'pending',
  `catatan` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `guru_penggantis`
--

CREATE TABLE `guru_penggantis` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `kehadiran_guru_id` bigint(20) UNSIGNED NOT NULL,
  `guru_id` bigint(20) UNSIGNED DEFAULT NULL,
  `guru_pengganti_id` bigint(20) UNSIGNED DEFAULT NULL,
  `jadwal_id` bigint(20) UNSIGNED DEFAULT NULL,
  `kelas_id` bigint(20) UNSIGNED DEFAULT NULL,
  `mapel_id` bigint(20) UNSIGNED DEFAULT NULL,
  `tanggal` date NOT NULL,
  `status` enum('pending','aktif','selesai','ditolak') NOT NULL DEFAULT 'pending',
  `keterangan` text DEFAULT NULL,
  `requested_by_siswa_id` bigint(20) UNSIGNED DEFAULT NULL,
  `approved_by_user_id` bigint(20) UNSIGNED DEFAULT NULL,
  `approved_by` bigint(20) UNSIGNED DEFAULT NULL,
  `approved_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `jadwal_kelas`
--

CREATE TABLE `jadwal_kelas` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `kelas_id` bigint(20) UNSIGNED NOT NULL,
  `mapel_id` bigint(20) UNSIGNED NOT NULL,
  `guru_id` bigint(20) UNSIGNED NOT NULL,
  `hari` enum('Senin','Selasa','Rabu','Kamis','Jumat','Sabtu') NOT NULL,
  `jam_mulai` time NOT NULL,
  `jam_selesai` time NOT NULL,
  `tahun_ajaran_id` bigint(20) UNSIGNED NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `jadwal_kelas`
--

INSERT INTO `jadwal_kelas` (`id`, `kelas_id`, `mapel_id`, `guru_id`, `hari`, `jam_mulai`, `jam_selesai`, `tahun_ajaran_id`, `created_at`, `updated_at`) VALUES
(6, 1, 1, 1, 'Senin', '07:00:00', '08:30:00', 15, '2025-12-14 01:39:09', '2025-12-14 01:39:09'),
(7, 1, 2, 2, 'Senin', '08:30:00', '10:00:00', 15, '2025-12-14 01:39:09', '2025-12-14 01:39:09'),
(8, 1, 3, 3, 'Senin', '10:15:00', '11:45:00', 15, '2025-12-14 01:39:09', '2025-12-14 01:39:09'),
(9, 1, 6, 5, 'Selasa', '07:00:00', '08:30:00', 15, '2025-12-14 01:39:09', '2025-12-14 01:39:09'),
(10, 1, 7, 6, 'Selasa', '08:30:00', '10:00:00', 15, '2025-12-14 01:39:09', '2025-12-14 01:39:09'),
(11, 1, 9, 7, 'Selasa', '10:15:00', '11:45:00', 15, '2025-12-14 01:39:09', '2025-12-14 01:39:09'),
(12, 1, 4, 1, 'Rabu', '07:00:00', '08:30:00', 15, '2025-12-14 01:39:09', '2025-12-14 01:39:09'),
(13, 1, 5, 2, 'Rabu', '08:30:00', '10:00:00', 15, '2025-12-14 01:39:09', '2025-12-14 01:39:09'),
(14, 1, 8, 3, 'Rabu', '10:15:00', '11:45:00', 15, '2025-12-14 01:39:09', '2025-12-14 01:39:09'),
(15, 1, 1, 1, 'Kamis', '07:00:00', '08:30:00', 15, '2025-12-14 01:39:09', '2025-12-14 01:39:09'),
(16, 1, 10, 5, 'Kamis', '08:30:00', '10:00:00', 15, '2025-12-14 01:39:09', '2025-12-14 01:39:09'),
(17, 1, 3, 3, 'Kamis', '10:15:00', '11:45:00', 15, '2025-12-14 01:39:09', '2025-12-14 01:39:09'),
(18, 1, 2, 2, 'Jumat', '07:00:00', '08:30:00', 15, '2025-12-14 01:39:09', '2025-12-14 01:39:09'),
(19, 1, 6, 5, 'Jumat', '08:30:00', '10:00:00', 15, '2025-12-14 01:39:09', '2025-12-14 01:39:09'),
(20, 1, 9, 7, 'Sabtu', '07:00:00', '08:30:00', 15, '2025-12-14 01:39:09', '2025-12-14 01:39:09'),
(21, 1, 7, 6, 'Sabtu', '08:30:00', '10:00:00', 15, '2025-12-14 01:39:09', '2025-12-14 01:39:09');

-- --------------------------------------------------------

--
-- Struktur dari tabel `jobs`
--

CREATE TABLE `jobs` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `queue` varchar(255) NOT NULL,
  `payload` longtext NOT NULL,
  `attempts` tinyint(3) UNSIGNED NOT NULL,
  `reserved_at` int(10) UNSIGNED DEFAULT NULL,
  `available_at` int(10) UNSIGNED NOT NULL,
  `created_at` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `job_batches`
--

CREATE TABLE `job_batches` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `total_jobs` int(11) NOT NULL,
  `pending_jobs` int(11) NOT NULL,
  `failed_jobs` int(11) NOT NULL,
  `failed_job_ids` longtext NOT NULL,
  `options` mediumtext DEFAULT NULL,
  `cancelled_at` int(11) DEFAULT NULL,
  `created_at` int(11) NOT NULL,
  `finished_at` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `kehadiran_gurus`
--

CREATE TABLE `kehadiran_gurus` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `jadwal_id` bigint(20) UNSIGNED NOT NULL,
  `guru_id` bigint(20) UNSIGNED NOT NULL,
  `kelas_id` bigint(20) UNSIGNED NOT NULL,
  `mapel_id` bigint(20) UNSIGNED NOT NULL,
  `tanggal` date NOT NULL,
  `status` enum('hadir','tidak_hadir','izin','sakit') NOT NULL DEFAULT 'tidak_hadir',
  `keterangan` text DEFAULT NULL,
  `input_by_siswa_id` bigint(20) UNSIGNED DEFAULT NULL,
  `input_by_kurikulum_id` bigint(20) UNSIGNED DEFAULT NULL,
  `input_by_kurikulum_name` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `kelas`
--

CREATE TABLE `kelas` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `kode_kelas` varchar(255) NOT NULL,
  `nama_kelas` varchar(255) NOT NULL,
  `guru_id` bigint(20) UNSIGNED DEFAULT NULL,
  `tingkat` varchar(255) NOT NULL,
  `jurusan` varchar(255) NOT NULL,
  `kapasitas` int(11) NOT NULL DEFAULT 36,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `kelas`
--

INSERT INTO `kelas` (`id`, `kode_kelas`, `nama_kelas`, `guru_id`, `tingkat`, `jurusan`, `kapasitas`, `created_at`, `updated_at`) VALUES
(1, 'X-RPL', 'X RPL', 1, 'X', 'RPL', 37, '2025-12-13 22:27:35', '2025-12-14 01:04:28'),
(2, 'XI-RPL', 'XI RPL', 2, 'XI', 'RPL', 37, '2025-12-13 22:27:35', '2025-12-14 01:17:39'),
(3, 'X-BD-1', 'X BD 1', 3, 'X', 'BD', 37, '2025-12-13 22:27:35', '2025-12-14 01:06:23'),
(5, 'XII-RPL', 'XII RPL', 8, 'XII', 'RPL', 38, '2025-12-13 22:27:35', '2025-12-14 01:05:38'),
(7, 'XI-LPB', 'XI LPB', 5, 'XI', 'LPB', 38, '2025-12-13 22:27:35', '2025-12-14 01:07:08'),
(8, 'XI-BD-2', 'XI BD 2', 7, 'XI', 'BD', 38, '2025-12-13 22:27:35', '2025-12-14 01:08:43');

-- --------------------------------------------------------

--
-- Struktur dari tabel `mapels`
--

CREATE TABLE `mapels` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `kode_mapel` varchar(255) NOT NULL,
  `mapel` varchar(255) NOT NULL,
  `kategori` enum('Wajib','Peminatan','Muatan Lokal') DEFAULT NULL,
  `jam_pelajaran` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `mapels`
--

INSERT INTO `mapels` (`id`, `kode_mapel`, `mapel`, `kategori`, `jam_pelajaran`, `created_at`, `updated_at`) VALUES
(1, 'MP001', 'Matematika', NULL, NULL, '2025-12-13 22:56:35', '2025-12-13 22:56:35'),
(2, 'MP002', 'Bahasa Indonesia', NULL, NULL, '2025-12-13 22:56:35', '2025-12-13 22:56:35'),
(3, 'MP003', 'Bahasa Inggris', NULL, NULL, '2025-12-13 22:56:35', '2025-12-13 22:56:35'),
(4, 'MP004', 'Bahasa Jawa', 'Muatan Lokal', NULL, '2025-12-13 22:56:35', '2025-12-14 01:00:10'),
(5, 'MP005', 'Pendidikan Agama Islam', 'Muatan Lokal', NULL, '2025-12-13 22:56:35', '2025-12-14 01:01:49'),
(6, 'MP006', 'Bahasa Jerman', 'Peminatan', NULL, '2025-12-13 22:56:35', '2025-12-14 00:59:47'),
(7, 'MP007', 'Sejarah', NULL, NULL, '2025-12-13 22:56:35', '2025-12-13 22:56:35'),
(8, 'MP008', 'Pendidikan Pancasila', 'Wajib', NULL, '2025-12-13 22:56:35', '2025-12-14 01:01:09'),
(9, 'MP009', 'Bahasa Jepang', 'Peminatan', NULL, '2025-12-13 22:56:35', '2025-12-14 01:00:44'),
(10, 'MP010', 'Pjok', 'Peminatan', NULL, '2025-12-13 22:56:35', '2025-12-14 01:03:42');

-- --------------------------------------------------------

--
-- Struktur dari tabel `migrations`
--

CREATE TABLE `migrations` (
  `id` int(10) UNSIGNED NOT NULL,
  `migration` varchar(255) NOT NULL,
  `batch` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `migrations`
--

INSERT INTO `migrations` (`id`, `migration`, `batch`) VALUES
(1, '0001_01_01_000000_create_users_table', 1),
(2, '0001_01_01_000001_create_cache_table', 1),
(3, '0001_01_01_000002_create_jobs_table', 1),
(4, '2025_11_12_020227_create_personal_access_tokens_table', 1),
(5, '2025_11_12_020327_create_gurus_table', 1),
(6, '2025_11_12_022444_create_mapels_table', 1),
(7, '2025_11_12_022509_create_tahun_ajarans_table', 1),
(8, '2025_11_18_000001_create_kelas_table', 1),
(9, '2025_11_18_000002_create_siswas_table', 1),
(10, '2025_11_18_000004_create_absensi_gurus_table', 1),
(11, '2025_11_29_151723_update_gurus_table_for_nullable_fields', 1),
(12, '2025_12_01_150000_create_hendra_wijaya_user', 1),
(13, '2025_12_01_160000_remove_unique_from_absensi_final', 1),
(14, '2025_12_03_000001_create_jadwal_kelas_table', 1),
(15, '2025_12_03_000002_create_kehadiran_gurus_table', 1),
(16, '2025_12_03_000003_create_guru_pengganti_table', 1),
(17, '2025_12_03_000004_create_notifikasi_siswa_table', 1),
(18, '2025_12_06_114300_create_guru_mengajar_table', 1),
(19, '2025_12_06_114306_create_monitoring_siswa_table', 1),
(20, '2025_12_06_114313_create_monitoring_kurikulum_table', 1),
(21, '2025_12_06_114318_create_monitoring_kepala_sekolah_table', 1),
(22, '2025_12_06_123101_update_guru_pengganti_nullable_fields', 1),
(23, '2025_12_07_000000_add_guru_to_users_role_enum', 1),
(24, '2025_12_07_000001_modify_users_role_enum', 1),
(25, '2025_12_09_002852_add_kategori_and_jam_pelajaran_to_mapels_table', 1),
(26, '2025_12_10_010511_add_missing_columns_to_kehadiran_gurus', 1),
(27, '2025_12_10_021200_create_guru_penggantis_table', 1),
(28, '2025_12_10_035512_add_guru_pengganti_id_to_guru_penggantis_table', 1),
(29, '2025_12_10_135129_add_input_by_kurikulum_id_to_kehadiran_gurus_table', 1),
(30, '2025_12_11_000000_add_device_token_to_users_table', 1),
(31, '2025_12_13_000000_add_input_by_kurikulum_name_and_indexes_to_kehadiran_gurus', 1),
(32, '2025_12_13_000001_alter_guru_penggantis_status_enum', 1);

-- --------------------------------------------------------

--
-- Struktur dari tabel `monitoring_kepala_sekolah`
--

CREATE TABLE `monitoring_kepala_sekolah` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `monitoring_kurikulum`
--

CREATE TABLE `monitoring_kurikulum` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `monitoring_siswa`
--

CREATE TABLE `monitoring_siswa` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `notifikasi_siswa`
--

CREATE TABLE `notifikasi_siswa` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `kelas_id` bigint(20) UNSIGNED NOT NULL,
  `guru_id` bigint(20) UNSIGNED NOT NULL,
  `mapel_id` bigint(20) UNSIGNED DEFAULT NULL,
  `tanggal` date NOT NULL,
  `tipe` enum('izin','sakit','guru_pengganti') NOT NULL,
  `pesan` text NOT NULL,
  `guru_pengganti_id` bigint(20) UNSIGNED DEFAULT NULL,
  `created_by_kurikulum_id` bigint(20) UNSIGNED NOT NULL,
  `is_read` tinyint(1) NOT NULL DEFAULT 0,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `password_reset_tokens`
--

CREATE TABLE `password_reset_tokens` (
  `email` varchar(255) NOT NULL,
  `token` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `personal_access_tokens`
--

CREATE TABLE `personal_access_tokens` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tokenable_type` varchar(255) NOT NULL,
  `tokenable_id` bigint(20) UNSIGNED NOT NULL,
  `name` text NOT NULL,
  `token` varchar(64) NOT NULL,
  `abilities` text DEFAULT NULL,
  `last_used_at` timestamp NULL DEFAULT NULL,
  `expires_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `sessions`
--

CREATE TABLE `sessions` (
  `id` varchar(255) NOT NULL,
  `user_id` bigint(20) UNSIGNED DEFAULT NULL,
  `ip_address` varchar(45) DEFAULT NULL,
  `user_agent` text DEFAULT NULL,
  `payload` longtext NOT NULL,
  `last_activity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `sessions`
--

INSERT INTO `sessions` (`id`, `user_id`, `ip_address`, `user_agent`, `payload`, `last_activity`) VALUES
('6NzHOto4wQdOE8oGrWDi7rSFjfOF7KNQ6kqMLuzU', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; en-US) WindowsPowerShell/5.1.26100.7462', 'YTozOntzOjY6Il90b2tlbiI7czo0MDoiUnE0NGgyMDNUTUVxRllJaDRKSmtpN1A3ZFMxNkVLRE5Ud2Z1WXdTeSI7czo5OiJfcHJldmlvdXMiO2E6Mjp7czozOiJ1cmwiO3M6MzM6Imh0dHA6Ly8xMjcuMC4wLjE6ODAwMC9hZG1pbi9sb2dpbiI7czo1OiJyb3V0ZSI7czoxMToiYWRtaW4ubG9naW4iO31zOjY6Il9mbGFzaCI7YToyOntzOjM6Im9sZCI7YTowOnt9czozOiJuZXciO2E6MDp7fX19', 1765701146),
('eAyCdF66UbvKoUJN0FgJ1ZsrgCofFAQXOlJWzCSf', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; en-US) WindowsPowerShell/5.1.26100.7462', 'YTozOntzOjY6Il90b2tlbiI7czo0MDoiZHRhaW5ENUlDZ2lXQ0pPRXNrQjV5cWhxYXB4V1NYMzYySDFPanVpUCI7czo5OiJfcHJldmlvdXMiO2E6Mjp7czozOiJ1cmwiO3M6MzM6Imh0dHA6Ly8xMjcuMC4wLjE6ODAwMC9hZG1pbi9sb2dpbiI7czo1OiJyb3V0ZSI7czoxMToiYWRtaW4ubG9naW4iO31zOjY6Il9mbGFzaCI7YToyOntzOjM6Im9sZCI7YTowOnt9czozOiJuZXciO2E6MDp7fX19', 1765700113),
('mmX7L3WjfrXNFwniITjlaKHqpAi3ebDdBN1F90dq', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 'YTozOntzOjY6Il90b2tlbiI7czo0MDoiV1ZBbWg0U2h1SHlxeFhoYVZ0c1dNNEpZNXlIMEpVU2U3QXFRTXFMciI7czo2OiJfZmxhc2giO2E6Mjp7czozOiJvbGQiO2E6MDp7fXM6MzoibmV3IjthOjA6e319czo5OiJfcHJldmlvdXMiO2E6Mjp7czozOiJ1cmwiO3M6MzM6Imh0dHA6Ly8xMjcuMC4wLjE6ODAwMC9hZG1pbi9sb2dpbiI7czo1OiJyb3V0ZSI7czoxMToiYWRtaW4ubG9naW4iO319', 1765702251),
('o7NnjFxBRpy7YT1NaVPowjbZEO4IOe6H0gQNJDAx', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; en-US) WindowsPowerShell/5.1.26100.7462', 'YTozOntzOjY6Il90b2tlbiI7czo0MDoiMGxtRmpYY0ZEY1c2VDRkY2pjelhMZzEyZVVqNHRzOHpDWmFXaE5rNSI7czo5OiJfcHJldmlvdXMiO2E6Mjp7czozOiJ1cmwiO3M6MzM6Imh0dHA6Ly8xMjcuMC4wLjE6ODAwMC9hZG1pbi9sb2dpbiI7czo1OiJyb3V0ZSI7czoxMToiYWRtaW4ubG9naW4iO31zOjY6Il9mbGFzaCI7YToyOntzOjM6Im9sZCI7YTowOnt9czozOiJuZXciO2E6MDp7fX19', 1765700113),
('QjSWtk7L0AiSqeNBIPFppGpeYwjcaowORryBXHxm', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; en-US) WindowsPowerShell/5.1.26100.7462', 'YTozOntzOjY6Il90b2tlbiI7czo0MDoiWEpCeEJzVXNJQzc0UmZSaVg3aFN6b2U2NExqazBmY1FVamF4R0todyI7czo5OiJfcHJldmlvdXMiO2E6Mjp7czozOiJ1cmwiO3M6MzM6Imh0dHA6Ly8xMjcuMC4wLjE6ODAwMC9hZG1pbi9sb2dpbiI7czo1OiJyb3V0ZSI7czoxMToiYWRtaW4ubG9naW4iO31zOjY6Il9mbGFzaCI7YToyOntzOjM6Im9sZCI7YTowOnt9czozOiJuZXciO2E6MDp7fX19', 1765700085),
('SjHwn3lKnO0ciG9dAvbGSJdEtkT9FxOFJhlOWdBf', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; en-US) WindowsPowerShell/5.1.26100.7462', 'YTozOntzOjY6Il90b2tlbiI7czo0MDoiWXlQVklCc1k4MTNibkp1Q25mZEtrR2dQWnBXU3BKb2xrZFRnOEc3aiI7czo5OiJfcHJldmlvdXMiO2E6Mjp7czozOiJ1cmwiO3M6MzM6Imh0dHA6Ly8xMjcuMC4wLjE6ODAwMC9hZG1pbi9sb2dpbiI7czo1OiJyb3V0ZSI7czoxMToiYWRtaW4ubG9naW4iO31zOjY6Il9mbGFzaCI7YToyOntzOjM6Im9sZCI7YTowOnt9czozOiJuZXciO2E6MDp7fX19', 1765701084),
('ziBWz2OlvSiW6Nz4QpqNZHkATgc4L7v01wUrV2Tu', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; en-US) WindowsPowerShell/5.1.26100.7462', 'YTozOntzOjY6Il90b2tlbiI7czo0MDoiUW8xOFJaS2FZQjlDcGpRZUdXcG5xeVpVWmNFemN6eE1qcnpxSlpQaSI7czo5OiJfcHJldmlvdXMiO2E6Mjp7czozOiJ1cmwiO3M6MzM6Imh0dHA6Ly8xMjcuMC4wLjE6ODAwMC9hZG1pbi9sb2dpbiI7czo1OiJyb3V0ZSI7czoxMToiYWRtaW4ubG9naW4iO31zOjY6Il9mbGFzaCI7YToyOntzOjM6Im9sZCI7YTowOnt9czozOiJuZXciO2E6MDp7fX19', 1765701121);

-- --------------------------------------------------------

--
-- Struktur dari tabel `siswas`
--

CREATE TABLE `siswas` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `nis` varchar(255) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `jenis_kelamin` enum('L','P') NOT NULL,
  `tempat_lahir` varchar(255) DEFAULT NULL,
  `tanggal_lahir` date DEFAULT NULL,
  `alamat` text DEFAULT NULL,
  `telepon` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `kelas_id` bigint(20) UNSIGNED DEFAULT NULL,
  `user_id` bigint(20) UNSIGNED DEFAULT NULL,
  `status` enum('aktif','non-aktif','lulus','pindah') NOT NULL DEFAULT 'aktif',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `siswas`
--

INSERT INTO `siswas` (`id`, `nis`, `nama`, `jenis_kelamin`, `tempat_lahir`, `tanggal_lahir`, `alamat`, `telepon`, `email`, `kelas_id`, `user_id`, `status`, `created_at`, `updated_at`) VALUES
(1, '2021001', 'Ahmad Fauzi', 'L', 'Jakarta', NULL, 'Jl. Merdeka No. 10, Jakarta', '081234567801', 'ahmad.fauzi@example.com', 3, NULL, 'aktif', '2025-12-13 22:27:35', '2025-12-14 01:18:39'),
(2, '2021002', 'Siti Nurhaliza', 'P', 'Bandung', '2005-02-20', 'Jl. Sudirman No. 20, Bandung', '081234567802', 'siti.nurhaliza@example.com', 3, 9, 'aktif', '2025-12-13 22:27:35', '2025-12-13 22:27:35'),
(3, '2021003', 'Budi Santoso', 'L', 'Surabaya', '2005-03-25', 'Jl. Diponegoro No. 30, Surabaya', '081234567803', 'budi.santoso@example.com', 3, NULL, 'aktif', '2025-12-13 22:27:35', '2025-12-13 22:27:35'),
(4, '2021004', 'Citra Dewi', 'P', 'Yogyakarta', '2005-04-10', 'Jl. Malioboro No. 40, Yogyakarta', '081234567804', 'citra.dewi@example.com', 3, NULL, 'aktif', '2025-12-13 22:27:35', '2025-12-13 22:27:35'),
(5, '2021005', 'Dedi Setiawan', 'L', 'Semarang', NULL, 'Jl. Pemuda No. 50, Semarang', '081234567805', 'dedi.setiawan@example.com', 2, NULL, 'aktif', '2025-12-13 22:27:35', '2025-12-14 01:16:39'),
(6, '2022001', 'Eka Putri', 'P', 'Medan', '2006-01-20', 'Jl. Gatot Subroto No. 60, Medan', '081234567806', 'eka.putri@example.com', 1, NULL, 'aktif', '2025-12-13 22:27:36', '2025-12-13 22:27:36'),
(7, '2022002', 'Fajar Rahman', 'L', 'Makassar', '2006-02-25', 'Jl. Ahmad Yani No. 70, Makassar', '081234567807', 'fajar.rahman@example.com', 1, NULL, 'aktif', '2025-12-13 22:27:36', '2025-12-13 22:27:36'),
(8, '2022003', 'Gita Maharani', 'P', 'Palembang', '2006-03-30', 'Jl. Sudirman No. 80, Palembang', '081234567808', 'gita.maharani@example.com', 2, NULL, 'aktif', '2025-12-13 22:27:36', '2025-12-13 22:27:36'),
(9, '2020001', 'Hendra Wijaya', 'L', 'Jakarta', '2004-06-15', 'Jl. Thamrin No. 90, Jakarta', '081234567809', 'hendra.wijaya@example.com', 5, NULL, 'aktif', '2025-12-13 22:27:36', '2025-12-13 22:27:36'),
(10, '2020002', 'Indah Permata', 'P', 'Bandung', '2004-07-20', 'Jl. Asia Afrika No. 100, Bandung', '081234567810', 'indah.permata@example.com', 5, NULL, 'aktif', '2025-12-13 22:27:36', '2025-12-13 22:27:36');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tahun_ajarans`
--

CREATE TABLE `tahun_ajarans` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tahun` varchar(255) NOT NULL,
  `flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0=tidak aktif, 1=aktif',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `tahun_ajarans`
--

INSERT INTO `tahun_ajarans` (`id`, `tahun`, `flag`, `created_at`, `updated_at`) VALUES
(4, '2024/2025', 0, '2025-12-13 22:28:33', '2025-12-14 01:13:56'),
(7, '2025/2026', 1, '2025-12-13 22:30:11', '2025-12-14 01:13:41'),
(15, '2026/2027', 0, '2025-12-13 22:56:35', '2025-12-14 01:13:23');

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `email_verified_at` timestamp NULL DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('admin','kurikulum','kepala_sekolah','siswa','guru') NOT NULL DEFAULT 'siswa',
  `remember_token` varchar(100) DEFAULT NULL,
  `device_token` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `email_verified_at`, `password`, `role`, `remember_token`, `device_token`, `created_at`, `updated_at`) VALUES
(4, 'Administrator', 'admin@monitoring.com', NULL, '$2y$12$PYzsUvnkLeD6pw/IKer6mOUaXPzlN1Sjg3mNhurWw5Ixbg5gXYkjC', 'admin', NULL, NULL, '2025-12-13 21:36:10', '2025-12-13 22:56:35'),
(5, 'Kurikulum User', 'kurikulum@example.com', NULL, '$2y$12$DvA6sBgdbcQY37.0EA4BgevoUuZQ3qmYzotKdh6kmUDjqWWHx7iie', 'kurikulum', NULL, NULL, '2025-12-13 21:36:10', '2025-12-13 22:56:35'),
(7, 'Kepala Sekolah', 'kepsek@example.com', NULL, '$2y$12$GBVlH0xASPORTfDEeO6Jcej1xPEKlnyANTd7.Y/YKtPdkzVDA.aLO', 'kepala_sekolah', NULL, NULL, '2025-12-13 21:36:10', '2025-12-13 22:56:35'),
(9, 'Siswa User', 'siswa@example.com', NULL, '$2y$12$Am/oqB0yIF92Ncvnqy4VWOV6YA8Ma3XOoCQcKWQ3RpzIslU6wIgJG', 'siswa', NULL, NULL, '2025-12-13 21:36:10', '2025-12-13 22:56:35'),
(11, 'Budi Siswa', 'budi.siswa@example.com', NULL, '$2y$12$A2lbWJxiky2tA0L8XVLiu.sUKSg6tEZyqDpNEjzwDAua7qELR8ffK', 'siswa', NULL, NULL, '2025-12-13 21:36:10', '2025-12-13 22:56:35');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `absensi_gurus`
--
ALTER TABLE `absensi_gurus`
  ADD PRIMARY KEY (`id`),
  ADD KEY `absensi_gurus_mapel_id_foreign` (`mapel_id`),
  ADD KEY `absensi_gurus_kelas_id_foreign` (`kelas_id`),
  ADD KEY `absensi_gurus_guru_id_tanggal_index` (`guru_id`,`tanggal`),
  ADD KEY `absensi_gurus_tanggal_index` (`tanggal`);

--
-- Indeks untuk tabel `cache`
--
ALTER TABLE `cache`
  ADD PRIMARY KEY (`key`);

--
-- Indeks untuk tabel `cache_locks`
--
ALTER TABLE `cache_locks`
  ADD PRIMARY KEY (`key`);

--
-- Indeks untuk tabel `failed_jobs`
--
ALTER TABLE `failed_jobs`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `failed_jobs_uuid_unique` (`uuid`);

--
-- Indeks untuk tabel `gurus`
--
ALTER TABLE `gurus`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `gurus_kode_guru_unique` (`kode_guru`),
  ADD UNIQUE KEY `gurus_email_unique` (`email`),
  ADD KEY `gurus_nama_index` (`nama`),
  ADD KEY `gurus_kode_guru_index` (`kode_guru`),
  ADD KEY `gurus_mata_pelajaran_index` (`mata_pelajaran`);

--
-- Indeks untuk tabel `guru_mengajar`
--
ALTER TABLE `guru_mengajar`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `guru_mengajar_unique` (`tahun_ajaran_id`,`guru_id`,`mapel_id`,`kelas_id`),
  ADD KEY `guru_mengajar_guru_id_foreign` (`guru_id`),
  ADD KEY `guru_mengajar_mapel_id_foreign` (`mapel_id`),
  ADD KEY `guru_mengajar_kelas_id_foreign` (`kelas_id`);

--
-- Indeks untuk tabel `guru_pengganti`
--
ALTER TABLE `guru_pengganti`
  ADD PRIMARY KEY (`id`),
  ADD KEY `guru_pengganti_kehadiran_guru_id_foreign` (`kehadiran_guru_id`),
  ADD KEY `guru_pengganti_mapel_id_foreign` (`mapel_id`),
  ADD KEY `guru_pengganti_assigned_by_kurikulum_id_foreign` (`assigned_by_kurikulum_id`),
  ADD KEY `guru_pengganti_guru_pengganti_id_tanggal_index` (`guru_pengganti_id`,`tanggal`),
  ADD KEY `guru_pengganti_kelas_id_tanggal_index` (`kelas_id`,`tanggal`),
  ADD KEY `guru_pengganti_status_index` (`status`);

--
-- Indeks untuk tabel `guru_penggantis`
--
ALTER TABLE `guru_penggantis`
  ADD PRIMARY KEY (`id`),
  ADD KEY `guru_penggantis_kehadiran_guru_id_foreign` (`kehadiran_guru_id`),
  ADD KEY `guru_penggantis_guru_id_foreign` (`guru_id`),
  ADD KEY `guru_penggantis_jadwal_id_foreign` (`jadwal_id`),
  ADD KEY `guru_penggantis_kelas_id_foreign` (`kelas_id`),
  ADD KEY `guru_penggantis_mapel_id_foreign` (`mapel_id`),
  ADD KEY `guru_penggantis_requested_by_siswa_id_foreign` (`requested_by_siswa_id`),
  ADD KEY `guru_penggantis_approved_by_user_id_foreign` (`approved_by_user_id`),
  ADD KEY `guru_penggantis_guru_pengganti_id_foreign` (`guru_pengganti_id`),
  ADD KEY `guru_penggantis_approved_by_foreign` (`approved_by`);

--
-- Indeks untuk tabel `jadwal_kelas`
--
ALTER TABLE `jadwal_kelas`
  ADD PRIMARY KEY (`id`),
  ADD KEY `jadwal_kelas_mapel_id_foreign` (`mapel_id`),
  ADD KEY `jadwal_kelas_kelas_id_hari_index` (`kelas_id`,`hari`),
  ADD KEY `jadwal_kelas_guru_id_hari_index` (`guru_id`,`hari`),
  ADD KEY `jadwal_kelas_tahun_ajaran_id_index` (`tahun_ajaran_id`);

--
-- Indeks untuk tabel `jobs`
--
ALTER TABLE `jobs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `jobs_queue_index` (`queue`);

--
-- Indeks untuk tabel `job_batches`
--
ALTER TABLE `job_batches`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `kehadiran_gurus`
--
ALTER TABLE `kehadiran_gurus`
  ADD PRIMARY KEY (`id`),
  ADD KEY `kehadiran_gurus_mapel_id_foreign` (`mapel_id`),
  ADD KEY `kehadiran_gurus_input_by_siswa_id_foreign` (`input_by_siswa_id`),
  ADD KEY `kehadiran_gurus_input_by_kurikulum_id_foreign` (`input_by_kurikulum_id`),
  ADD KEY `kehadiran_gurus_guru_id_tanggal_index` (`guru_id`,`tanggal`),
  ADD KEY `kehadiran_gurus_kelas_id_tanggal_index` (`kelas_id`,`tanggal`),
  ADD KEY `kehadiran_gurus_tanggal_status_index` (`tanggal`,`status`),
  ADD KEY `kehadiran_gurus_jadwal_id_index` (`jadwal_id`),
  ADD KEY `idx_kehadiran_kelas_tanggal_jadwal` (`kelas_id`,`tanggal`,`jadwal_id`);

--
-- Indeks untuk tabel `kelas`
--
ALTER TABLE `kelas`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `kelas_kode_kelas_unique` (`kode_kelas`),
  ADD KEY `kelas_guru_id_foreign` (`guru_id`);

--
-- Indeks untuk tabel `mapels`
--
ALTER TABLE `mapels`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `mapels_kode_mapel_unique` (`kode_mapel`);

--
-- Indeks untuk tabel `migrations`
--
ALTER TABLE `migrations`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `monitoring_kepala_sekolah`
--
ALTER TABLE `monitoring_kepala_sekolah`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `monitoring_kurikulum`
--
ALTER TABLE `monitoring_kurikulum`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `monitoring_siswa`
--
ALTER TABLE `monitoring_siswa`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `notifikasi_siswa`
--
ALTER TABLE `notifikasi_siswa`
  ADD PRIMARY KEY (`id`),
  ADD KEY `notifikasi_siswa_guru_id_foreign` (`guru_id`),
  ADD KEY `notifikasi_siswa_mapel_id_foreign` (`mapel_id`),
  ADD KEY `notifikasi_siswa_guru_pengganti_id_foreign` (`guru_pengganti_id`),
  ADD KEY `notifikasi_siswa_created_by_kurikulum_id_foreign` (`created_by_kurikulum_id`),
  ADD KEY `notifikasi_siswa_kelas_id_tanggal_index` (`kelas_id`,`tanggal`),
  ADD KEY `notifikasi_siswa_is_read_tanggal_index` (`is_read`,`tanggal`),
  ADD KEY `notifikasi_siswa_tipe_index` (`tipe`);

--
-- Indeks untuk tabel `password_reset_tokens`
--
ALTER TABLE `password_reset_tokens`
  ADD PRIMARY KEY (`email`);

--
-- Indeks untuk tabel `personal_access_tokens`
--
ALTER TABLE `personal_access_tokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `personal_access_tokens_token_unique` (`token`),
  ADD KEY `personal_access_tokens_tokenable_type_tokenable_id_index` (`tokenable_type`,`tokenable_id`),
  ADD KEY `personal_access_tokens_expires_at_index` (`expires_at`);

--
-- Indeks untuk tabel `sessions`
--
ALTER TABLE `sessions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `sessions_user_id_index` (`user_id`),
  ADD KEY `sessions_last_activity_index` (`last_activity`);

--
-- Indeks untuk tabel `siswas`
--
ALTER TABLE `siswas`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `siswas_nis_unique` (`nis`),
  ADD KEY `siswas_kelas_id_foreign` (`kelas_id`),
  ADD KEY `siswas_user_id_foreign` (`user_id`);

--
-- Indeks untuk tabel `tahun_ajarans`
--
ALTER TABLE `tahun_ajarans`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `users_email_unique` (`email`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `absensi_gurus`
--
ALTER TABLE `absensi_gurus`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT untuk tabel `failed_jobs`
--
ALTER TABLE `failed_jobs`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `gurus`
--
ALTER TABLE `gurus`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT untuk tabel `guru_mengajar`
--
ALTER TABLE `guru_mengajar`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `guru_pengganti`
--
ALTER TABLE `guru_pengganti`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `guru_penggantis`
--
ALTER TABLE `guru_penggantis`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT untuk tabel `jadwal_kelas`
--
ALTER TABLE `jadwal_kelas`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT untuk tabel `jobs`
--
ALTER TABLE `jobs`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `kehadiran_gurus`
--
ALTER TABLE `kehadiran_gurus`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT untuk tabel `kelas`
--
ALTER TABLE `kelas`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT untuk tabel `mapels`
--
ALTER TABLE `mapels`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT untuk tabel `migrations`
--
ALTER TABLE `migrations`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT untuk tabel `monitoring_kepala_sekolah`
--
ALTER TABLE `monitoring_kepala_sekolah`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `monitoring_kurikulum`
--
ALTER TABLE `monitoring_kurikulum`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `monitoring_siswa`
--
ALTER TABLE `monitoring_siswa`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `notifikasi_siswa`
--
ALTER TABLE `notifikasi_siswa`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `personal_access_tokens`
--
ALTER TABLE `personal_access_tokens`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `siswas`
--
ALTER TABLE `siswas`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT untuk tabel `tahun_ajarans`
--
ALTER TABLE `tahun_ajarans`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `absensi_gurus`
--
ALTER TABLE `absensi_gurus`
  ADD CONSTRAINT `absensi_gurus_guru_id_foreign` FOREIGN KEY (`guru_id`) REFERENCES `gurus` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `absensi_gurus_kelas_id_foreign` FOREIGN KEY (`kelas_id`) REFERENCES `kelas` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `absensi_gurus_mapel_id_foreign` FOREIGN KEY (`mapel_id`) REFERENCES `mapels` (`id`) ON DELETE SET NULL;

--
-- Ketidakleluasaan untuk tabel `guru_mengajar`
--
ALTER TABLE `guru_mengajar`
  ADD CONSTRAINT `guru_mengajar_guru_id_foreign` FOREIGN KEY (`guru_id`) REFERENCES `gurus` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `guru_mengajar_kelas_id_foreign` FOREIGN KEY (`kelas_id`) REFERENCES `kelas` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `guru_mengajar_mapel_id_foreign` FOREIGN KEY (`mapel_id`) REFERENCES `mapels` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `guru_mengajar_tahun_ajaran_id_foreign` FOREIGN KEY (`tahun_ajaran_id`) REFERENCES `tahun_ajarans` (`id`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `guru_pengganti`
--
ALTER TABLE `guru_pengganti`
  ADD CONSTRAINT `guru_pengganti_assigned_by_kurikulum_id_foreign` FOREIGN KEY (`assigned_by_kurikulum_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `guru_pengganti_guru_pengganti_id_foreign` FOREIGN KEY (`guru_pengganti_id`) REFERENCES `gurus` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `guru_pengganti_kehadiran_guru_id_foreign` FOREIGN KEY (`kehadiran_guru_id`) REFERENCES `kehadiran_gurus` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `guru_pengganti_kelas_id_foreign` FOREIGN KEY (`kelas_id`) REFERENCES `kelas` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `guru_pengganti_mapel_id_foreign` FOREIGN KEY (`mapel_id`) REFERENCES `mapels` (`id`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `guru_penggantis`
--
ALTER TABLE `guru_penggantis`
  ADD CONSTRAINT `guru_penggantis_approved_by_foreign` FOREIGN KEY (`approved_by`) REFERENCES `users` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `guru_penggantis_approved_by_user_id_foreign` FOREIGN KEY (`approved_by_user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `guru_penggantis_guru_id_foreign` FOREIGN KEY (`guru_id`) REFERENCES `gurus` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `guru_penggantis_guru_pengganti_id_foreign` FOREIGN KEY (`guru_pengganti_id`) REFERENCES `gurus` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `guru_penggantis_jadwal_id_foreign` FOREIGN KEY (`jadwal_id`) REFERENCES `jadwal_kelas` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `guru_penggantis_kehadiran_guru_id_foreign` FOREIGN KEY (`kehadiran_guru_id`) REFERENCES `kehadiran_gurus` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `guru_penggantis_kelas_id_foreign` FOREIGN KEY (`kelas_id`) REFERENCES `kelas` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `guru_penggantis_mapel_id_foreign` FOREIGN KEY (`mapel_id`) REFERENCES `mapels` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `guru_penggantis_requested_by_siswa_id_foreign` FOREIGN KEY (`requested_by_siswa_id`) REFERENCES `siswas` (`id`) ON DELETE SET NULL;

--
-- Ketidakleluasaan untuk tabel `jadwal_kelas`
--
ALTER TABLE `jadwal_kelas`
  ADD CONSTRAINT `jadwal_kelas_guru_id_foreign` FOREIGN KEY (`guru_id`) REFERENCES `gurus` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `jadwal_kelas_kelas_id_foreign` FOREIGN KEY (`kelas_id`) REFERENCES `kelas` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `jadwal_kelas_mapel_id_foreign` FOREIGN KEY (`mapel_id`) REFERENCES `mapels` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `jadwal_kelas_tahun_ajaran_id_foreign` FOREIGN KEY (`tahun_ajaran_id`) REFERENCES `tahun_ajarans` (`id`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `kehadiran_gurus`
--
ALTER TABLE `kehadiran_gurus`
  ADD CONSTRAINT `kehadiran_gurus_guru_id_foreign` FOREIGN KEY (`guru_id`) REFERENCES `gurus` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `kehadiran_gurus_input_by_kurikulum_id_foreign` FOREIGN KEY (`input_by_kurikulum_id`) REFERENCES `users` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `kehadiran_gurus_input_by_siswa_id_foreign` FOREIGN KEY (`input_by_siswa_id`) REFERENCES `siswas` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `kehadiran_gurus_jadwal_id_foreign` FOREIGN KEY (`jadwal_id`) REFERENCES `jadwal_kelas` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `kehadiran_gurus_kelas_id_foreign` FOREIGN KEY (`kelas_id`) REFERENCES `kelas` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `kehadiran_gurus_mapel_id_foreign` FOREIGN KEY (`mapel_id`) REFERENCES `mapels` (`id`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `kelas`
--
ALTER TABLE `kelas`
  ADD CONSTRAINT `kelas_guru_id_foreign` FOREIGN KEY (`guru_id`) REFERENCES `gurus` (`id`) ON DELETE SET NULL;

--
-- Ketidakleluasaan untuk tabel `notifikasi_siswa`
--
ALTER TABLE `notifikasi_siswa`
  ADD CONSTRAINT `notifikasi_siswa_created_by_kurikulum_id_foreign` FOREIGN KEY (`created_by_kurikulum_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `notifikasi_siswa_guru_id_foreign` FOREIGN KEY (`guru_id`) REFERENCES `gurus` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `notifikasi_siswa_guru_pengganti_id_foreign` FOREIGN KEY (`guru_pengganti_id`) REFERENCES `gurus` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `notifikasi_siswa_kelas_id_foreign` FOREIGN KEY (`kelas_id`) REFERENCES `kelas` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `notifikasi_siswa_mapel_id_foreign` FOREIGN KEY (`mapel_id`) REFERENCES `mapels` (`id`) ON DELETE SET NULL;

--
-- Ketidakleluasaan untuk tabel `siswas`
--
ALTER TABLE `siswas`
  ADD CONSTRAINT `siswas_kelas_id_foreign` FOREIGN KEY (`kelas_id`) REFERENCES `kelas` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `siswas_user_id_foreign` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
