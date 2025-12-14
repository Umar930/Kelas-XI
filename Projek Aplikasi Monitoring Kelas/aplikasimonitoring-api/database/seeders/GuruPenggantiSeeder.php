<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use App\Models\GuruPengganti;
use App\Models\KehadiranGuru;

class GuruPenggantiSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        // Create a sample kehadiran + guru pengganti request if none exists
        $kehadiran = KehadiranGuru::first();
        if ($kehadiran && !GuruPengganti::where('kehadiran_guru_id', $kehadiran->id)->exists()) {
            GuruPengganti::create([
                'kehadiran_guru_id' => $kehadiran->id,
                'guru_pengganti_id' => null,
                'jadwal_id' => $kehadiran->jadwal_id,
                'kelas_id' => $kehadiran->kelas_id,
                'mapel_id' => $kehadiran->mapel_id,
                'tanggal' => $kehadiran->tanggal,
                'status' => 'pending',
                'keterangan' => 'Contoh request otomatis',
            ]);
        }
    }
}
