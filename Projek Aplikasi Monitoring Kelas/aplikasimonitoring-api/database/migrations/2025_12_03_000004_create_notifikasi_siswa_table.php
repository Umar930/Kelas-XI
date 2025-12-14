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
        Schema::create('notifikasi_siswa', function (Blueprint $table) {
            $table->id();
            $table->foreignId('kelas_id')->constrained('kelas')->onDelete('cascade');
            $table->foreignId('guru_id')->constrained('gurus')->onDelete('cascade');
            $table->foreignId('mapel_id')->nullable()->constrained('mapels')->onDelete('set null');
            $table->date('tanggal');
            $table->enum('tipe', ['izin', 'sakit', 'guru_pengganti']);
            $table->text('pesan');
            $table->foreignId('guru_pengganti_id')->nullable()->constrained('gurus')->onDelete('set null');
            $table->foreignId('created_by_kurikulum_id')->constrained('users')->onDelete('cascade');
            $table->boolean('is_read')->default(false);
            $table->timestamps();

            // Index untuk performa query
            $table->index(['kelas_id', 'tanggal']);
            $table->index(['is_read', 'tanggal']);
            $table->index('tipe');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('notifikasi_siswa');
    }
};
