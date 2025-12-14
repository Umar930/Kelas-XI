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
        Schema::table('guru_penggantis', function (Blueprint $table) {
            // Kolom untuk guru pengganti yang dipilih kurikulum
            $table->foreignId('guru_pengganti_id')->nullable()->after('guru_id')->constrained('gurus')->onDelete('set null');
            // Kolom approved_by untuk tracking siapa yang approve
            $table->foreignId('approved_by')->nullable()->after('approved_by_user_id')->constrained('users')->onDelete('set null');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('guru_penggantis', function (Blueprint $table) {
            $table->dropForeign(['guru_pengganti_id']);
            $table->dropColumn('guru_pengganti_id');
            $table->dropForeign(['approved_by']);
            $table->dropColumn('approved_by');
        });
    }
};
