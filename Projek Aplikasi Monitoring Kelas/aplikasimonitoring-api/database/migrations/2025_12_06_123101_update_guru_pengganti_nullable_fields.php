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
        Schema::table('guru_pengganti', function (Blueprint $table) {
            $table->foreignId('kehadiran_guru_id')->nullable()->change();
            $table->foreignId('assigned_by_kurikulum_id')->nullable()->change();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('guru_pengganti', function (Blueprint $table) {
            $table->foreignId('kehadiran_guru_id')->nullable(false)->change();
            $table->foreignId('assigned_by_kurikulum_id')->nullable(false)->change();
        });
    }
};
