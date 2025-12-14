<?php

namespace Database\Seeders;

use App\Models\AbsensiSiswa;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class AbsensiSiswaSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $today = now()->format('Y-m-d');
        $yesterday = now()->subDay()->format('Y-m-d');

        $absensi = [
            // Absensi hari ini - XI RPL 1
            [
                'siswa_id' => 1,
                'kelas_id' => 3,
                'mapel_id' => 1,
                'tanggal' => $today,
                'status' => 'hadir',
                'jam_masuk' => '07:00',
                'jam_keluar' => '14:00',
            ],
            [
                'siswa_id' => 2,
                'kelas_id' => 3,
                'mapel_id' => 1,
                'tanggal' => $today,
                'status' => 'hadir',
                'jam_masuk' => '07:05',
                'jam_keluar' => '14:00',
            ],
            [
                'siswa_id' => 3,
                'kelas_id' => 3,
                'mapel_id' => 1,
                'tanggal' => $today,
                'status' => 'sakit',
                'keterangan' => 'Demam',
            ],
            [
                'siswa_id' => 4,
                'kelas_id' => 3,
                'mapel_id' => 1,
                'tanggal' => $today,
                'status' => 'hadir',
                'jam_masuk' => '07:10',
                'jam_keluar' => '14:00',
            ],
            
            // Absensi kemarin - XI RPL 1
            [
                'siswa_id' => 1,
                'kelas_id' => 3,
                'mapel_id' => 2,
                'tanggal' => $yesterday,
                'status' => 'hadir',
                'jam_masuk' => '07:00',
                'jam_keluar' => '14:00',
            ],
            [
                'siswa_id' => 2,
                'kelas_id' => 3,
                'mapel_id' => 2,
                'tanggal' => $yesterday,
                'status' => 'hadir',
                'jam_masuk' => '07:00',
                'jam_keluar' => '14:00',
            ],
            [
                'siswa_id' => 3,
                'kelas_id' => 3,
                'mapel_id' => 2,
                'tanggal' => $yesterday,
                'status' => 'izin',
                'keterangan' => 'Keperluan keluarga',
            ],
            [
                'siswa_id' => 4,
                'kelas_id' => 3,
                'mapel_id' => 2,
                'tanggal' => $yesterday,
                'status' => 'alpha',
            ],
            
            // Absensi hari ini - XI RPL 2
            [
                'siswa_id' => 5,
                'kelas_id' => 4,
                'mapel_id' => 1,
                'tanggal' => $today,
                'status' => 'hadir',
                'jam_masuk' => '07:00',
                'jam_keluar' => '14:00',
            ],
            
            // Absensi hari ini - X RPL 1
            [
                'siswa_id' => 6,
                'kelas_id' => 1,
                'mapel_id' => 1,
                'tanggal' => $today,
                'status' => 'hadir',
                'jam_masuk' => '07:15',
                'jam_keluar' => '14:00',
            ],
            [
                'siswa_id' => 7,
                'kelas_id' => 1,
                'mapel_id' => 1,
                'tanggal' => $today,
                'status' => 'hadir',
                'jam_masuk' => '07:00',
                'jam_keluar' => '14:00',
            ],
        ];

        foreach ($absensi as $data) {
            AbsensiSiswa::create($data);
        }
    }
}
