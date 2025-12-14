<?php

namespace Database\Seeders;

use App\Models\User;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    use WithoutModelEvents;

    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        $seeders = [
            UserSeeder::class,
            GuruSeeder::class,
            MapelSeeder::class,
            TahunAjaranSeeder::class,
            KelasSeeder::class,
            SiswaSeeder::class,
            AbsensiGuruSeeder::class,
            JadwalSeeder::class,
            GuruMengajarSeeder::class,
            GuruPenggantiSeeder::class,
            KehadiranGuruSeeder::class,
        ];

        // Only call AbsensiSiswaSeeder if it exists (it's in a backup directory in some branches)
        if (class_exists('\Database\Seeders\AbsensiSiswaSeeder')) {
            $seeders[] = \Database\Seeders\AbsensiSiswaSeeder::class;
        }

        $this->call($seeders);
    }
}
