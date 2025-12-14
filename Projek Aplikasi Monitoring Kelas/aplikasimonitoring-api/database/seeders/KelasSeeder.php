<?php

namespace Database\Seeders;

use App\Models\Kelas;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class KelasSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $kelas = [
            [
                'kode_kelas' => 'X-RPL-1',
                'nama_kelas' => 'X RPL 1',
                'guru_id' => 1,
                'tingkat' => 'X',
                'jurusan' => 'RPL',
                'kapasitas' => 36,
            ],
            [
                'kode_kelas' => 'X-RPL-2',
                'nama_kelas' => 'X RPL 2',
                'guru_id' => 2,
                'tingkat' => 'X',
                'jurusan' => 'RPL',
                'kapasitas' => 36,
            ],
            [
                'kode_kelas' => 'XI-RPL-1',
                'nama_kelas' => 'XI RPL 1',
                'guru_id' => 3,
                'tingkat' => 'XI',
                'jurusan' => 'RPL',
                'kapasitas' => 36,
            ],
            [
                'kode_kelas' => 'XI-RPL-2',
                'nama_kelas' => 'XI RPL 2',
                'guru_id' => 1,
                'tingkat' => 'XI',
                'jurusan' => 'RPL',
                'kapasitas' => 36,
            ],
            [
                'kode_kelas' => 'XII-RPL-1',
                'nama_kelas' => 'XII RPL 1',
                'guru_id' => 2,
                'tingkat' => 'XII',
                'jurusan' => 'RPL',
                'kapasitas' => 36,
            ],
            [
                'kode_kelas' => 'X-TKJ-1',
                'nama_kelas' => 'X TKJ 1',
                'guru_id' => 3,
                'tingkat' => 'X',
                'jurusan' => 'TKJ',
                'kapasitas' => 36,
            ],
            [
                'kode_kelas' => 'XI-TKJ-1',
                'nama_kelas' => 'XI TKJ 1',
                'guru_id' => 1,
                'tingkat' => 'XI',
                'jurusan' => 'TKJ',
                'kapasitas' => 36,
            ],
            [
                'kode_kelas' => 'XII-TKJ-1',
                'nama_kelas' => 'XII TKJ 1',
                'guru_id' => 2,
                'tingkat' => 'XII',
                'jurusan' => 'TKJ',
                'kapasitas' => 36,
            ],
        ];

        foreach ($kelas as $data) {
            // Ensure idempotent seed: update or create by kode_kelas
            Kelas::updateOrCreate(
                ['kode_kelas' => $data['kode_kelas']],
                $data
            );
        }
    }
}
