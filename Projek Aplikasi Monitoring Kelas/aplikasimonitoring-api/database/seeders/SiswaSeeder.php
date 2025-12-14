<?php

namespace Database\Seeders;

use App\Models\Siswa;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class SiswaSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $siswas = [
            [
                'nis' => '2021001',
                'nama' => 'Ahmad Fauzi',
                'jenis_kelamin' => 'L',
                'tempat_lahir' => 'Jakarta',
                'tanggal_lahir' => '2005-01-15',
                'alamat' => 'Jl. Merdeka No. 10, Jakarta',
                'telepon' => '081234567801',
                'email' => 'ahmad.fauzi@example.com',
                'kelas_id' => 3, // XI RPL 1
                'user_id' => 8, // User siswa
                'status' => 'aktif',
            ],
            [
                'nis' => '2021002',
                'nama' => 'Siti Nurhaliza',
                'jenis_kelamin' => 'P',
                'tempat_lahir' => 'Bandung',
                'tanggal_lahir' => '2005-02-20',
                'alamat' => 'Jl. Sudirman No. 20, Bandung',
                'telepon' => '081234567802',
                'email' => 'siti.nurhaliza@example.com',
                'kelas_id' => 3, // XI RPL 1
                'user_id' => 9,
                'status' => 'aktif',
            ],
            [
                'nis' => '2021003',
                'nama' => 'Budi Santoso',
                'jenis_kelamin' => 'L',
                'tempat_lahir' => 'Surabaya',
                'tanggal_lahir' => '2005-03-25',
                'alamat' => 'Jl. Diponegoro No. 30, Surabaya',
                'telepon' => '081234567803',
                'email' => 'budi.santoso@example.com',
                'kelas_id' => 3, // XI RPL 1
                'user_id' => 10,
                'status' => 'aktif',
            ],
            [
                'nis' => '2021004',
                'nama' => 'Citra Dewi',
                'jenis_kelamin' => 'P',
                'tempat_lahir' => 'Yogyakarta',
                'tanggal_lahir' => '2005-04-10',
                'alamat' => 'Jl. Malioboro No. 40, Yogyakarta',
                'telepon' => '081234567804',
                'email' => 'citra.dewi@example.com',
                'kelas_id' => 3, // XI RPL 1
                'status' => 'aktif',
            ],
            [
                'nis' => '2021005',
                'nama' => 'Dedi Setiawan',
                'jenis_kelamin' => 'L',
                'tempat_lahir' => 'Semarang',
                'tanggal_lahir' => '2005-05-15',
                'alamat' => 'Jl. Pemuda No. 50, Semarang',
                'telepon' => '081234567805',
                'email' => 'dedi.setiawan@example.com',
                'kelas_id' => 4, // XI RPL 2
                'status' => 'aktif',
            ],
            [
                'nis' => '2022001',
                'nama' => 'Eka Putri',
                'jenis_kelamin' => 'P',
                'tempat_lahir' => 'Medan',
                'tanggal_lahir' => '2006-01-20',
                'alamat' => 'Jl. Gatot Subroto No. 60, Medan',
                'telepon' => '081234567806',
                'email' => 'eka.putri@example.com',
                'kelas_id' => 1, // X RPL 1
                'status' => 'aktif',
            ],
            [
                'nis' => '2022002',
                'nama' => 'Fajar Rahman',
                'jenis_kelamin' => 'L',
                'tempat_lahir' => 'Makassar',
                'tanggal_lahir' => '2006-02-25',
                'alamat' => 'Jl. Ahmad Yani No. 70, Makassar',
                'telepon' => '081234567807',
                'email' => 'fajar.rahman@example.com',
                'kelas_id' => 1, // X RPL 1
                'status' => 'aktif',
            ],
            [
                'nis' => '2022003',
                'nama' => 'Gita Maharani',
                'jenis_kelamin' => 'P',
                'tempat_lahir' => 'Palembang',
                'tanggal_lahir' => '2006-03-30',
                'alamat' => 'Jl. Sudirman No. 80, Palembang',
                'telepon' => '081234567808',
                'email' => 'gita.maharani@example.com',
                'kelas_id' => 2, // X RPL 2
                'status' => 'aktif',
            ],
            [
                'nis' => '2020001',
                'nama' => 'Hendra Wijaya',
                'jenis_kelamin' => 'L',
                'tempat_lahir' => 'Jakarta',
                'tanggal_lahir' => '2004-06-15',
                'alamat' => 'Jl. Thamrin No. 90, Jakarta',
                'telepon' => '081234567809',
                'email' => 'hendra.wijaya@example.com',
                'kelas_id' => 5, // XII RPL 1
                'status' => 'aktif',
            ],
            [
                'nis' => '2020002',
                'nama' => 'Indah Permata',
                'jenis_kelamin' => 'P',
                'tempat_lahir' => 'Bandung',
                'tanggal_lahir' => '2004-07-20',
                'alamat' => 'Jl. Asia Afrika No. 100, Bandung',
                'telepon' => '081234567810',
                'email' => 'indah.permata@example.com',
                'kelas_id' => 5, // XII RPL 1
                'status' => 'aktif',
            ],
        ];

        foreach ($siswas as $data) {
            // Ensure idempotent seeding using nis as unique key
            Siswa::updateOrCreate(
                ['nis' => $data['nis']],
                $data
            );
        }
    }
}
