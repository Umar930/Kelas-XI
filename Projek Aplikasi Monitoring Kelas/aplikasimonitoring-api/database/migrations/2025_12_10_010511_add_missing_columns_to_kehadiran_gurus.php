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
        if (!Schema::hasTable('kehadiran_gurus')) {
            return;
        }

        Schema::table('kehadiran_gurus', function (Blueprint $table) {
            // Tambahkan kolom yang diperlukan jika belum ada
            if (!Schema::hasColumn('kehadiran_gurus', 'guru_id')) {
                $table->foreignId('guru_id')->nullable()->after('jadwal_id')->constrained('gurus')->nullOnDelete();
            }

            if (!Schema::hasColumn('kehadiran_gurus', 'kelas_id')) {
                $table->foreignId('kelas_id')->nullable()->after('guru_id')->constrained('kelas')->nullOnDelete();
            }

            if (!Schema::hasColumn('kehadiran_gurus', 'mapel_id')) {
                $table->foreignId('mapel_id')->nullable()->after('kelas_id')->constrained('mapels')->nullOnDelete();
            }

            if (!Schema::hasColumn('kehadiran_gurus', 'input_by_siswa_id')) {
                $table->foreignId('input_by_siswa_id')->nullable()->after('keterangan')->constrained('siswas')->nullOnDelete();
            }
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('kehadiran_gurus', function (Blueprint $table) {
            $table->dropForeign(['guru_id']);
            $table->dropForeign(['kelas_id']);
            $table->dropForeign(['mapel_id']);
            $table->dropForeign(['input_by_siswa_id']);
            $table->dropColumn(['guru_id', 'kelas_id', 'mapel_id', 'input_by_siswa_id']);
        });
    }
};
