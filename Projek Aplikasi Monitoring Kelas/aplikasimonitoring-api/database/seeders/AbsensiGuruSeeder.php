<?php

namespace Database\Seeders;

use App\Models\AbsensiGuru;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class AbsensiGuruSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $today = now()->format('Y-m-d');
        $yesterday = now()->subDay()->format('Y-m-d');

        $absensi = [
            // Absensi hari ini
            [
                'guru_id' => 1,
                'mapel_id' => 1,
                'kelas_id' => 3,
                'tanggal' => $today,
                'status' => 'hadir',
                'jam_masuk' => '06:45',
                'jam_keluar' => '14:30',
            ],
            [
                'guru_id' => 2,
                'mapel_id' => 2,
                'kelas_id' => 4,
                'tanggal' => $today,
                'status' => 'hadir',
                'jam_masuk' => '06:50',
                'jam_keluar' => '14:30',
            ],
            [
                'guru_id' => 3,
                'mapel_id' => 3,
                'kelas_id' => 1,
                'tanggal' => $today,
                'status' => 'hadir',
                'jam_masuk' => '07:00',
                'jam_keluar' => '14:30',
            ],
            
            // Absensi kemarin
            [
                'guru_id' => 1,
                'mapel_id' => 1,
                'kelas_id' => 3,
                'tanggal' => $yesterday,
                'status' => 'hadir',
                'jam_masuk' => '06:45',
                'jam_keluar' => '14:30',
            ],
            [
                'guru_id' => 2,
                'mapel_id' => 2,
                'kelas_id' => 4,
                'tanggal' => $yesterday,
                'status' => 'sakit',
                'keterangan' => 'Flu',
            ],
            [
                'guru_id' => 3,
                'mapel_id' => 3,
                'kelas_id' => 1,
                'tanggal' => $yesterday,
                'status' => 'dinas',
                'keterangan' => 'Pelatihan di kantor dinas',
            ],
        ];

        foreach ($absensi as $data) {
            AbsensiGuru::create($data);
        }
    }
}
