<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class TahunAjaranSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $tahunAjarans = [
            ['tahun' => '2023/2024', 'flag' => 0],
            ['tahun' => '2024/2025', 'flag' => 1], // Tahun aktif
            ['tahun' => '2025/2026', 'flag' => 0],
        ];

        foreach ($tahunAjarans as $tahunAjaran) {
            DB::table('tahun_ajarans')->insert([
                'tahun' => $tahunAjaran['tahun'],
                'flag' => $tahunAjaran['flag'],
                'created_at' => now(),
                'updated_at' => now(),
            ]);
        }
    }
}
