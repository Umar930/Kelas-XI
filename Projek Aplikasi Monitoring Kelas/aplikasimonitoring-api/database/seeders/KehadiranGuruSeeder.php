<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use App\Models\KehadiranGuru;

class KehadiranGuruSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $jadwal = \DB::table('jadwal_kelas')->first();
        if (!$jadwal) {
            return;
        }

        $exists = KehadiranGuru::where('jadwal_id', $jadwal->id)
            ->whereDate('tanggal', now()->toDateString())
            ->exists();

        if (!$exists) {
            KehadiranGuru::create([
                'jadwal_id' => $jadwal->id,
                'kelas_id' => $jadwal->kelas_id,
                'guru_id' => $jadwal->guru_id,
                'mapel_id' => $jadwal->mapel_id,
                'tanggal' => now()->toDateString(),
                'status' => 'sakit',
                'keterangan' => 'Contoh: demam',
                'input_by_kurikulum_id' => 1,
            ]);
        }
    }
}
