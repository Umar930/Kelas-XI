<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;
use Illuminate\Support\Facades\DB;

return new class extends Migration
{
    public function up()
    {
        // Cek apakah tabel absensi_siswas ada
        if (!Schema::hasTable('absensi_siswas')) {
            echo "⚠️ Tabel absensi_siswas belum ada, skip migration\n";
            return;
        }

        // Hapus unique constraint agar siswa bisa absen berkali-kali di hari yang sama
        Schema::table('absensi_siswas', function (Blueprint $table) {
            try {
                $table->dropUnique(['siswa_id', 'tanggal']);
                echo "✅ Unique constraint berhasil dihapus!\n";
                echo "   Siswa sekarang bisa absen berkali-kali di hari yang sama.\n";
            } catch (\Exception $e) {
                echo "ℹ️  Constraint mungkin sudah dihapus sebelumnya: " . $e->getMessage() . "\n";
            }
        });
    }

    public function down()
    {
        Schema::table('absensi_siswas', function (Blueprint $table) {
            $table->unique(['siswa_id', 'tanggal']);
        });
    }
};
