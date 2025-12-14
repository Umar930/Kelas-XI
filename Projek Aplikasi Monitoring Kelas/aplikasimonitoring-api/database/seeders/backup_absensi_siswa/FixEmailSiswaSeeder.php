<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use App\Models\User;
use App\Models\Siswa;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\DB;

class FixEmailSiswaSeeder extends Seeder
{
    /**
     * Perbaiki email siswa agar sesuai dengan nama
     */
    public function run(): void
    {
        echo "🔧 Memperbaiki email siswa...\n\n";
        
        // Data siswa yang perlu diperbaiki
        $updates = [
            [
                'siswa_id' => 1,
                'nama' => 'Ahmad Fauzi',
                'old_email' => 'siswa@monitoring.com',
                'new_email' => 'ahmad.fauzi@example.com'
            ],
            [
                'siswa_id' => 2,
                'nama' => 'Siti Nurhaliza',
                'old_email' => 'budi.siswa@example.com',
                'new_email' => 'siti.nurhaliza@example.com'
            ],
            [
                'siswa_id' => 3,
                'nama' => 'Budi Santoso',
                'old_email' => 'citra.siswa@example.com',
                'new_email' => 'budi.santoso@example.com'
            ],
            [
                'siswa_id' => 4,
                'nama' => 'Citra Dewi',
                'old_email' => 'siswa@example.com',
                'new_email' => 'citra.dewi@example.com'
            ],
        ];
        
        foreach ($updates as $update) {
            echo "📝 Memperbaiki: {$update['nama']}\n";
            echo "   Old: {$update['old_email']}\n";
            echo "   New: {$update['new_email']}\n";
            
            // Cari siswa
            $siswa = Siswa::find($update['siswa_id']);
            
            if (!$siswa) {
                echo "   ❌ Siswa ID {$update['siswa_id']} tidak ditemukan\n\n";
                continue;
            }
            
            // Cari user dengan email lama
            $user = User::where('email', $update['old_email'])->first();
            
            if ($user) {
                // Update email di tabel users
                $user->email = $update['new_email'];
                $user->save();
                echo "   ✅ Email user updated\n";
            } else {
                echo "   ⚠️ User dengan email {$update['old_email']} tidak ditemukan\n";
            }
            
            echo "\n";
        }
        
        echo "✅ Selesai memperbaiki email siswa!\n";
    }
}
