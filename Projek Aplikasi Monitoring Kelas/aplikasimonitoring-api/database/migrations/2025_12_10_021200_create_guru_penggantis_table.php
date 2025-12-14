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
        Schema::create('guru_penggantis', function (Blueprint $table) {
            $table->id();
            $table->foreignId('kehadiran_guru_id')->constrained('kehadiran_gurus')->onDelete('cascade');
            // guru pengganti (nullable until kurikulum assigns one)
            $table->foreignId('guru_id')->nullable()->constrained('gurus')->nullOnDelete();
            $table->foreignId('jadwal_id')->nullable()->constrained('jadwal_kelas')->nullOnDelete();
            $table->foreignId('kelas_id')->nullable()->constrained('kelas')->nullOnDelete();
            $table->foreignId('mapel_id')->nullable()->constrained('mapels')->nullOnDelete();
            $table->date('tanggal');
            // status: pending (request by siswa/kurikulum), aktif (currently assigned and running), selesai (completed), ditolak (rejected)
            $table->enum('status', ['pending', 'aktif', 'selesai', 'ditolak'])->default('pending');
            $table->text('keterangan')->nullable();
            $table->foreignId('requested_by_siswa_id')->nullable()->constrained('siswas')->nullOnDelete();
            $table->foreignId('approved_by_user_id')->nullable()->constrained('users')->nullOnDelete();
            $table->timestamp('approved_at')->nullable();
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('guru_penggantis');
    }
};
