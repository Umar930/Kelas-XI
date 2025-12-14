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
        // Change enum values to include Bahasa Indonesia variants used in controllers and views
        if (DB::getDriverName() !== 'sqlite') {
            DB::statement("ALTER TABLE `guru_penggantis` MODIFY COLUMN `status` ENUM('pending','aktif','selesai','ditolak') NOT NULL DEFAULT 'pending';");
        }
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        // Back to previous values - original migration used english values
        if (DB::getDriverName() !== 'sqlite') {
            DB::statement("ALTER TABLE `guru_penggantis` MODIFY COLUMN `status` ENUM('pending','approved','rejected') NOT NULL DEFAULT 'pending';");
        }
    }
};
