<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use App\Models\GuruMengajar;

class GuruMengajarSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $data = [
            ['tahun_ajaran_id' => 1, 'guru_id' => 1, 'mapel_id' => 1, 'kelas_id' => 1, 'jam_per_minggu' => 4],
            ['tahun_ajaran_id' => 1, 'guru_id' => 2, 'mapel_id' => 2, 'kelas_id' => 1, 'jam_per_minggu' => 3],
            ['tahun_ajaran_id' => 1, 'guru_id' => 3, 'mapel_id' => 3, 'kelas_id' => 2, 'jam_per_minggu' => 4],
        ];

        foreach ($data as $d) {
            GuruMengajar::updateOrCreate(
                [
                    'tahun_ajaran_id' => $d['tahun_ajaran_id'],
                    'guru_id' => $d['guru_id'],
                    'mapel_id' => $d['mapel_id'],
                    'kelas_id' => $d['kelas_id'],
                ],
                $d
            );
        }
    }
}
