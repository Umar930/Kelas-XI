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
            // Add column to track which kurikulum user input the record
            if (!Schema::hasColumn('kehadiran_gurus', 'input_by_kurikulum_id')) {
                $table->foreignId('input_by_kurikulum_id')->nullable()->after('input_by_siswa_id')->constrained('users')->onDelete('set null');
            }
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('kehadiran_gurus', function (Blueprint $table) {
            if (Schema::hasColumn('kehadiran_gurus', 'input_by_kurikulum_id')) {
                $table->dropForeign(['input_by_kurikulum_id']);
                $table->dropColumn('input_by_kurikulum_id');
            }
        });
    }
};
