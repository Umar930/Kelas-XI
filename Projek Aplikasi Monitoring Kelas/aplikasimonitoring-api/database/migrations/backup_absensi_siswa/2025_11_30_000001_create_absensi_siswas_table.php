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
        Schema::create('absensi_siswas', function (Blueprint $table) {
            $table->id();
            
            // Foreign key ke tabel siswas
            $table->foreignId('siswa_id')
                  ->constrained('siswas')
                  ->onDelete('cascade');
            
            // Data absensi
            $table->date('tanggal');
            $table->enum('status', ['hadir', 'sakit', 'izin']);
            $table->text('keterangan')->nullable(); // Wajib untuk sakit/izin
            
            // Soft delete untuk fitur reset
            $table->softDeletes();
            
            $table->timestamps();
            
            // Constraints & Indexes
            $table->unique(['siswa_id', 'tanggal']); // Satu siswa hanya bisa absen 1x per hari
            $table->index('tanggal');
            $table->index('status');
            $table->index('deleted_at'); // Untuk query soft delete
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('absensi_siswas');
    }
};
