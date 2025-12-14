<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class CreateSiswaRecordForDefaultUser extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        echo "=== Creating Siswa Record for siswa@example.com ===\n";

        // Get user siswa@example.com
        $user = DB::table('users')->where('email', 'siswa@example.com')->first();

        if (!$user) {
            echo "❌ User siswa@example.com tidak ditemukan!\n";
            return;
        }

        // Check if siswa record already exists
        $existingSiswa = DB::table('siswas')->where('user_id', $user->id)->first();

        if ($existingSiswa) {
            echo "✅ Siswa record sudah ada (ID: {$existingSiswa->id})\n";
            return;
        }

        // Get kelas XI-RPL
        $kelas = DB::table('kelas')->where('nama_kelas', 'XI-RPL')->first();

        if (!$kelas) {
            echo "❌ Kelas XI-RPL tidak ditemukan!\n";
            return;
        }

        // Create siswa record
        $siswaId = DB::table('siswas')->insertGetId([
            'user_id' => $user->id,
            'nama' => 'Default Siswa',
            'nis' => '2025001',
            'kelas_id' => $kelas->id,
            'created_at' => now(),
            'updated_at' => now(),
        ]);

        echo "✅ Siswa record berhasil dibuat!\n";
        echo "   Siswa ID: {$siswaId}\n";
        echo "   User ID: {$user->id}\n";
        echo "   Nama: Default Siswa\n";
        echo "   NIS: 2025001\n";
        echo "   Kelas: {$kelas->nama_kelas} (ID: {$kelas->id})\n";
        echo "   Email: {$user->email}\n";
        echo "\n✅ Sekarang siswa@example.com bisa submit absensi!\n";
    }
}
