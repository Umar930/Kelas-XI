<?php

namespace Database\Seeders;

use App\Models\Guru;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class GuruSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        // Delete old data with GR prefix (duplicate data)
        DB::table('gurus')->whereIn('kode_guru', ['GR001', 'GR002', 'GR003', 'GR004', 'GR005'])->delete();

        $gurus = [
            [
                'kode_guru' => 'G001',
                'nama' => 'Budi Santoso',
                'email' => 'budi.santoso01@smk.sch.id',
                'no_telepon' => '081234567801',
                'mata_pelajaran' => 'Pemrograman Web',
                'alamat' => 'Jl. Teknologi No. 1, Surabaya',
            ],
            [
                'kode_guru' => 'G002',
                'nama' => 'Ani Wijaya',
                'email' => 'ani.wijaya@smk.sch.id',
                'no_telepon' => '081234567802',
                'mata_pelajaran' => 'Basis Data',
                'alamat' => 'Jl. Teknologi No. 2, Surabaya',
            ],
            [
                'kode_guru' => 'G003',
                'nama' => 'Citra Dewi',
                'email' => 'citra.dewi@smk.sch.id',
                'no_telepon' => '081234567803',
                'mata_pelajaran' => 'Pemrograman Mobile',
                'alamat' => 'Jl. Teknologi No. 3, Surabaya',
            ],
            [
                'kode_guru' => 'G004',
                'nama' => 'Dr. Ahmad Subarjo, M.Pd',
                'email' => 'ahmad.subarjo@smk.sch.id',
                'no_telepon' => '08123456789',
                'mata_pelajaran' => 'Matematika',
                'alamat' => 'Jl. Pendidikan No. 4, Surabaya',
            ],
            [
                'kode_guru' => 'G005',
                'nama' => 'Siti Nurhaliza, S.Pd',
                'email' => 'siti.nurhaliza@smk.sch.id',
                'no_telepon' => '08234567890',
                'mata_pelajaran' => 'Bahasa Indonesia',
                'alamat' => 'Jl. Pendidikan No. 5, Surabaya',
            ],
            [
                'kode_guru' => 'G006',
                'nama' => 'Budi Santoso, S.Kom',
                'email' => 'budi.santoso02@smk.sch.id',
                'no_telepon' => '08345678901',
                'mata_pelajaran' => 'Pemrograman Web dan Mobile',
                'alamat' => 'Jl. Teknologi No. 6, Surabaya',
            ],
            [
                'kode_guru' => 'G007',
                'nama' => 'Dewi Lestari, M.Si',
                'email' => 'dewi.lestari@smk.sch.id',
                'no_telepon' => '08456789012',
                'mata_pelajaran' => 'Fisika',
                'alamat' => 'Jl. Sains No. 7, Surabaya',
            ],
            [
                'kode_guru' => 'G008',
                'nama' => 'Eko Prasetyo, S.Pd',
                'email' => 'eko.prasetyo@smk.sch.id',
                'no_telepon' => '08567890123',
                'mata_pelajaran' => 'Bahasa Inggris',
                'alamat' => 'Jl. Bahasa No. 8, Surabaya',
            ],
        ];

        foreach ($gurus as $guru) {
            Guru::updateOrCreate(
                ['kode_guru' => $guru['kode_guru']], // Find by kode_guru
                $guru // Update or create with this data
            );
        }

        $this->command->info('Cleaned and seeded ' . count($gurus) . ' gurus successfully!');
    }
}
