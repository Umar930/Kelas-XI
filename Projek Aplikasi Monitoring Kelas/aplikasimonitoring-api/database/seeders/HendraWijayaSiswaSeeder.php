<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class HendraWijayaSiswaSeeder extends Seeder
{
    public function run()
    {
        // Cek apakah sudah ada
        $existing = DB::table('siswas')->where('user_id', 11)->first();
        
        if ($existing) {
            echo "⚠️  Data siswa untuk user_id=11 sudah ada!\n";
            return;
        }

        // Insert data siswa
        DB::table('siswas')->insert([
            'user_id' => 11,
            'nis' => '2021010',
            'nama' => 'Hendra Wijaya',
            'kelas_id' => 3,
            'created_at' => now(),
            'updated_at' => now(),
        ]);

        echo "✅ Data siswa 'Hendra Wijaya' berhasil ditambahkan!\n";
        echo "   NIS: 2021010\n";
        echo "   Kelas: XII-RPL (kelas_id=3)\n";
    }
}
