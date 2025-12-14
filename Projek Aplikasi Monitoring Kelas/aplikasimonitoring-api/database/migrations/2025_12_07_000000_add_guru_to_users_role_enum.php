<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;
use Illuminate\Support\Facades\DB;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        // Add 'guru' to role enum - run only on MySQL to avoid SQLite syntax issues
        if (DB::getDriverName() !== 'sqlite') {
            DB::statement("ALTER TABLE users MODIFY COLUMN role ENUM('admin', 'kurikulum', 'kepala_sekolah', 'siswa', 'guru') NOT NULL DEFAULT 'siswa'");
        }
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        // Remove 'guru' from role enum - only on MySQL
        if (DB::getDriverName() !== 'sqlite') {
            DB::statement("ALTER TABLE users MODIFY COLUMN role ENUM('admin', 'kurikulum', 'kepala_sekolah', 'siswa') NOT NULL DEFAULT 'siswa'");
        }
    }
};
