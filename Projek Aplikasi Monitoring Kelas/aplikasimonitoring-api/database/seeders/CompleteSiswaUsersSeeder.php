<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use App\Models\User;
use App\Models\Siswa;
use Illuminate\Support\Facades\Hash;

class CompleteSiswaUsersSeeder extends Seeder
{
    /**
     * Buat user untuk semua siswa yang belum punya email
     */
    public function run(): void
    {
        echo "=== MEMBUAT USER UNTUK SISWA YANG BELUM PUNYA EMAIL ===\n\n";
        
        // Data siswa yang perlu dibuatkan user
        $siswaData = [
            ['siswa_id' => 5, 'nama' => 'Dedi Setiawan', 'email' => 'dedi.setiawan@example.com'],
            ['siswa_id' => 6, 'nama' => 'Eka Putri', 'email' => 'eka.putri@example.com'],
            ['siswa_id' => 7, 'nama' => 'Fajar Rahman', 'email' => 'fajar.rahman@example.com'],
            ['siswa_id' => 8, 'nama' => 'Gita Maharani', 'email' => 'gita.maharani@example.com'],
            ['siswa_id' => 9, 'nama' => 'Hendra Wijaya', 'email' => 'hendra.wijaya2@example.com'], // ID 9 - beda dari ID 19
            ['siswa_id' => 10, 'nama' => 'Indah Permata', 'email' => 'indah.permata@example.com'],
        ];
        
        foreach ($siswaData as $data) {
            $siswa = Siswa::find($data['siswa_id']);
            
            if (!$siswa) {
                echo "❌ Siswa ID {$data['siswa_id']} tidak ditemukan\n";
                continue;
            }
            
            // Cek apakah siswa sudah punya user
            if ($siswa->user_id) {
                echo "ℹ️  Siswa {$data['nama']} (ID: {$data['siswa_id']}) sudah punya user\n";
                continue;
            }
            
            // Cek apakah email sudah digunakan
            $emailExists = User::where('email', $data['email'])->exists();
            if ($emailExists) {
                echo "⚠️  Email {$data['email']} sudah digunakan\n";
                continue;
            }
            
            // Buat user baru
            $user = User::create([
                'name' => $data['nama'],
                'email' => $data['email'],
                'password' => Hash::make('password'), // Password: "password"
                'role' => 'siswa',
                'email_verified_at' => now(),
            ]);
            
            // Update siswa dengan user_id
            $siswa->user_id = $user->id;
            $siswa->save();
            
            echo "✅ User dibuat: {$data['nama']} | {$data['email']} | Password: password\n";
        }
        
        echo "\n=== SELESAI ===\n";
    }
}
