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
        Schema::table('kehadiran_gurus', function (Blueprint $table) {
            if (!Schema::hasColumn('kehadiran_gurus', 'input_by_kurikulum_name')) {
                $table->string('input_by_kurikulum_name')->nullable()->after('input_by_kurikulum_id');
            }

            // Ensure indices for queries
            if (!Schema::hasColumn('kehadiran_gurus', 'jadwal_id')) {
                // nothing
            } else {
                $table->index('jadwal_id');
            }

            // Composite index: kelas + tanggal + jadwal
            $table->index(['kelas_id', 'tanggal', 'jadwal_id'], 'idx_kehadiran_kelas_tanggal_jadwal');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('kehadiran_gurus', function (Blueprint $table) {
            if (Schema::hasColumn('kehadiran_gurus', 'input_by_kurikulum_name')) {
                $table->dropColumn('input_by_kurikulum_name');
            }
            // drop indexes safely
            $sm = Schema::getConnection()->getDoctrineSchemaManager();
            $indexes = array_keys($sm->listTableIndexes('kehadiran_gurus'));
            if (in_array('idx_kehadiran_kelas_tanggal_jadwal', $indexes)) {
                $table->dropIndex('idx_kehadiran_kelas_tanggal_jadwal');
            }
            if (in_array('kehadiran_gurus_jadwal_id_index', $indexes)) {
                $table->dropIndex('kehadiran_gurus_jadwal_id_index');
            }
        });
    }
};
