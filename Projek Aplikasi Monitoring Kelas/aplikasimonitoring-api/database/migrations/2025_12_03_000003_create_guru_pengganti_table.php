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
        Schema::create('guru_pengganti', function (Blueprint $table) {
            $table->id();
            $table->foreignId('kehadiran_guru_id')->nullable()->constrained('kehadiran_gurus')->onDelete('cascade');
            $table->foreignId('guru_pengganti_id')->constrained('gurus')->onDelete('cascade');
            $table->foreignId('kelas_id')->constrained('kelas')->onDelete('cascade');
            $table->foreignId('mapel_id')->constrained('mapels')->onDelete('cascade');
            $table->date('tanggal');
            $table->time('jam_mulai');
            $table->time('jam_selesai');
            $table->foreignId('assigned_by_kurikulum_id')->nullable()->constrained('users')->onDelete('cascade');
            $table->enum('status', ['pending', 'aktif', 'selesai', 'ditolak'])->default('pending');
            $table->text('catatan')->nullable();
            $table->timestamps();

            // Index untuk performa query
            $table->index(['guru_pengganti_id', 'tanggal']);
            $table->index(['kelas_id', 'tanggal']);
            $table->index('status');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('guru_pengganti');
    }
};
