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
        Schema::create('kehadiran_gurus', function (Blueprint $table) {
            $table->id();
            $table->foreignId('jadwal_id')->constrained('jadwal_kelas')->onDelete('cascade');
            $table->foreignId('guru_id')->constrained('gurus')->onDelete('cascade');
            $table->foreignId('kelas_id')->constrained('kelas')->onDelete('cascade');
            $table->foreignId('mapel_id')->constrained('mapels')->onDelete('cascade');
            $table->date('tanggal');
            $table->enum('status', ['hadir', 'tidak_hadir', 'izin', 'sakit'])->default('tidak_hadir');
            $table->text('keterangan')->nullable();
            $table->foreignId('input_by_siswa_id')->nullable()->constrained('siswas')->onDelete('set null');
            $table->foreignId('input_by_kurikulum_id')->nullable()->constrained('users')->onDelete('set null');
            $table->timestamps();

            // Index untuk performa query
            $table->index(['guru_id', 'tanggal']);
            $table->index(['kelas_id', 'tanggal']);
            $table->index(['tanggal', 'status']);
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('kehadiran_gurus');
    }
};
