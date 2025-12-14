<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class JadwalSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $jadwals = [
            ['kelas_id'=>1, 'mapel_id'=>1, 'guru_id'=>1, 'hari'=>'Senin', 'jam_mulai'=>'07:00', 'jam_selesai'=>'08:30', 'tahun_ajaran_id'=>1],
            ['kelas_id'=>1, 'mapel_id'=>2, 'guru_id'=>2, 'hari'=>'Senin', 'jam_mulai'=>'08:30', 'jam_selesai'=>'10:00', 'tahun_ajaran_id'=>1],
            ['kelas_id'=>2, 'mapel_id'=>3, 'guru_id'=>3, 'hari'=>'Selasa', 'jam_mulai'=>'07:00', 'jam_selesai'=>'08:30', 'tahun_ajaran_id'=>1],
            ['kelas_id'=>3, 'mapel_id'=>4, 'guru_id'=>1, 'hari'=>'Rabu', 'jam_mulai'=>'07:00', 'jam_selesai'=>'08:30', 'tahun_ajaran_id'=>1],
        ];

        foreach ($jadwals as $jadwal) {
            DB::table('jadwal_kelas')->updateOrInsert(
                [
                    'kelas_id' => $jadwal['kelas_id'],
                    'mapel_id' => $jadwal['mapel_id'],
                    'hari' => $jadwal['hari'],
                    'jam_mulai' => $jadwal['jam_mulai'],
                ],
                array_merge($jadwal, ['created_at' => now(), 'updated_at' => now()])
            );
        }
    }
}
