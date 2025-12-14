<?php

require __DIR__.'/vendor/autoload.php';
$app = require_once __DIR__.'/bootstrap/app.php';
$app->make('Illuminate\Contracts\Console\Kernel')->bootstrap();

use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Schema;

// Create kelas table
if (!Schema::hasTable('kelas')) {
    DB::statement("CREATE TABLE kelas (
        id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        nama_kelas VARCHAR(50) NOT NULL,
        tingkat VARCHAR(20),
        jurusan VARCHAR(50),
        tahun_ajaran_id BIGINT UNSIGNED,
        created_at TIMESTAMP NULL,
        updated_at TIMESTAMP NULL
    )");
    echo "Table 'kelas' created.\n";
} else {
    echo "Table 'kelas' already exists.\n";
}

// Create siswas table
if (!Schema::hasTable('siswas')) {
    DB::statement("CREATE TABLE siswas (
        id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        nis VARCHAR(20) NOT NULL,
        nama VARCHAR(100) NOT NULL,
        jenis_kelamin ENUM('L','P') NOT NULL DEFAULT 'L',
        tempat_lahir VARCHAR(100),
        tanggal_lahir DATE,
        alamat TEXT,
        telepon VARCHAR(20),
        email VARCHAR(100),
        kelas_id BIGINT UNSIGNED,
        user_id BIGINT UNSIGNED,
        status ENUM('aktif','non-aktif','lulus','pindah') DEFAULT 'aktif',
        created_at TIMESTAMP NULL,
        updated_at TIMESTAMP NULL
    )");
    echo "Table 'siswas' created.\n";
} else {
    echo "Table 'siswas' already exists.\n";
}

// Create jadwal_kelas table
if (!Schema::hasTable('jadwal_kelas')) {
    DB::statement("CREATE TABLE jadwal_kelas (
        id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        kelas_id BIGINT UNSIGNED NOT NULL,
        mapel_id BIGINT UNSIGNED NOT NULL,
        guru_id BIGINT UNSIGNED NOT NULL,
        hari ENUM('Senin','Selasa','Rabu','Kamis','Jumat','Sabtu') NOT NULL,
        jam_mulai TIME NOT NULL,
        jam_selesai TIME NOT NULL,
        tahun_ajaran_id BIGINT UNSIGNED,
        created_at TIMESTAMP NULL,
        updated_at TIMESTAMP NULL
    )");
    echo "Table 'jadwal_kelas' created.\n";
} else {
    echo "Table 'jadwal_kelas' already exists.\n";
}

// Create guru_mengajar table
if (!Schema::hasTable('guru_mengajar')) {
    DB::statement("CREATE TABLE guru_mengajar (
        id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        guru_id BIGINT UNSIGNED NOT NULL,
        mapel_id BIGINT UNSIGNED NOT NULL,
        kelas_id BIGINT UNSIGNED NOT NULL,
        tahun_ajaran_id BIGINT UNSIGNED NOT NULL,
        jam_per_minggu INT,
        keterangan TEXT,
        status ENUM('aktif','non-aktif') DEFAULT 'aktif',
        created_at TIMESTAMP NULL,
        updated_at TIMESTAMP NULL
    )");
    echo "Table 'guru_mengajar' created.\n";
} else {
    echo "Table 'guru_mengajar' already exists.\n";
}

// Create kehadiran_gurus table
if (!Schema::hasTable('kehadiran_gurus')) {
    DB::statement("CREATE TABLE kehadiran_gurus (
        id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        jadwal_id BIGINT UNSIGNED NOT NULL,
        tanggal DATE NOT NULL,
        status ENUM('hadir','izin','sakit','alpha','terlambat') NOT NULL DEFAULT 'hadir',
        jam_masuk TIME,
        jam_keluar TIME,
        keterangan TEXT,
        created_at TIMESTAMP NULL,
        updated_at TIMESTAMP NULL
    )");
    echo "Table 'kehadiran_gurus' created.\n";
} else {
    echo "Table 'kehadiran_gurus' already exists.\n";
}

// Create guru_pengganti table
if (!Schema::hasTable('guru_pengganti')) {
    DB::statement("CREATE TABLE guru_pengganti (
        id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        kehadiran_guru_id BIGINT UNSIGNED,
        guru_pengganti_id BIGINT UNSIGNED NOT NULL,
        kelas_id BIGINT UNSIGNED NOT NULL,
        mapel_id BIGINT UNSIGNED NOT NULL,
        tanggal DATE NOT NULL,
        jam_mulai TIME,
        jam_selesai TIME,
        assigned_by_kurikulum_id BIGINT UNSIGNED,
        status ENUM('pending','aktif','selesai','ditolak') DEFAULT 'pending',
        catatan TEXT,
        created_at TIMESTAMP NULL,
        updated_at TIMESTAMP NULL
    )");
    echo "Table 'guru_pengganti' created.\n";
} else {
    echo "Table 'guru_pengganti' already exists.\n";
}

echo "\nAll tables checked/created successfully!\n";
