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
            // Drop unique constraint - akan di-handle di code (karena soft delete)
            $table->dropUnique(['siswa_id', 'tanggal']);
            
            // Add regular index untuk performance
            $table->index(['siswa_id', 'tanggal']);
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('absensi_siswas', function (Blueprint $table) {
            // Drop index
            $table->dropIndex(['siswa_id', 'tanggal']);
            
            // Re-add unique constraint
            $table->unique(['siswa_id', 'tanggal']);
        });
    }
};
