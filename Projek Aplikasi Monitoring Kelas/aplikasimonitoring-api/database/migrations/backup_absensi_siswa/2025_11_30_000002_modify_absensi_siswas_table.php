<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::table('absensi_siswas', function (Blueprint $table) {
            // Drop kolom yang tidak diperlukan
            $table->dropForeign(['kelas_id']);
            $table->dropForeign(['mapel_id']);
            $table->dropColumn(['kelas_id', 'mapel_id', 'jam_masuk', 'jam_keluar']);
            
            // Ubah enum status (hapus 'alpha', tambahkan default)
            DB::statement("ALTER TABLE absensi_siswas MODIFY status ENUM('hadir', 'sakit', 'izin') NOT NULL");
            
            // Tambahkan soft deletes
            $table->softDeletes();
            
            // Tambahkan unique constraint dan indexes
            $table->unique(['siswa_id', 'tanggal']);
            $table->index('tanggal');
            $table->index('status');
            $table->index('deleted_at');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('absensi_siswas', function (Blueprint $table) {
            // Kembalikan kolom
            $table->foreignId('kelas_id')->constrained('kelas');
            $table->foreignId('mapel_id')->nullable()->constrained('mapels');
            $table->time('jam_masuk')->nullable();
            $table->time('jam_keluar')->nullable();
            
            // Hapus soft deletes
            $table->dropSoftDeletes();
            
            // Hapus indexes
            $table->dropUnique(['siswa_id', 'tanggal']);
            $table->dropIndex(['tanggal']);
            $table->dropIndex(['status']);
            
            // Kembalikan enum status
            DB::statement("ALTER TABLE absensi_siswas MODIFY status ENUM('hadir', 'sakit', 'izin', 'alpha') NOT NULL DEFAULT 'hadir'");
        });
    }
};
