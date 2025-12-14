<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class MapelSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $mapels = [
            ['kode_mapel' => 'MP001', 'mapel' => 'Matematika'],
            ['kode_mapel' => 'MP002', 'mapel' => 'Bahasa Indonesia'],
            ['kode_mapel' => 'MP003', 'mapel' => 'Bahasa Inggris'],
            ['kode_mapel' => 'MP004', 'mapel' => 'Fisika'],
            ['kode_mapel' => 'MP005', 'mapel' => 'Kimia'],
            ['kode_mapel' => 'MP006', 'mapel' => 'Biologi'],
            ['kode_mapel' => 'MP007', 'mapel' => 'Sejarah'],
            ['kode_mapel' => 'MP008', 'mapel' => 'Geografi'],
            ['kode_mapel' => 'MP009', 'mapel' => 'Ekonomi'],
            ['kode_mapel' => 'MP010', 'mapel' => 'Sosiologi'],
        ];

        foreach ($mapels as $mapel) {
            // Use updateOrInsert to avoid duplicate insert errors
            DB::table('mapels')->updateOrInsert(
                ['kode_mapel' => $mapel['kode_mapel']],
                [
                    'mapel' => $mapel['mapel'],
                    'updated_at' => now(),
                    'created_at' => now(),
                ]
            );
        }
    }
}
