<?php

namespace Database\Seeders;

use App\Models\User;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\Hash;

class UserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        // Buat user untuk testing dengan berbagai role dan email yang berbeda
        $users = [
            // Admin Users
            [
                'name' => 'Admin User',
                'email' => 'admin@example.com',
                // Default admin password is now 'admin123' per project defaults
                'password' => Hash::make('admin123'),
                'role' => 'admin',
            ],
            [
                'name' => 'Administrator',
                'email' => 'admin@monitoring.com',
                // Default admin password is now 'admin123' per project defaults
                'password' => Hash::make('admin123'),
                'role' => 'admin',
            ],
            // Kurikulum Users
            [
                'name' => 'Kurikulum User',
                'email' => 'kurikulum@example.com',
                'password' => Hash::make('password'),
                'role' => 'kurikulum',
            ],
            [
                'name' => 'Staff Kurikulum',
                'email' => 'kurikulum@monitoring.com',
                'password' => Hash::make('password'),
                'role' => 'kurikulum',
            ],
            // Kepala Sekolah Users
            [
                'name' => 'Kepala Sekolah',
                'email' => 'kepsek@example.com',
                'password' => Hash::make('password'),
                'role' => 'kepala_sekolah',
            ],
            [
                'name' => 'Kepala Sekolah Utama',
                'email' => 'kepsek@monitoring.com',
                'password' => Hash::make('password'),
                'role' => 'kepala_sekolah',
            ],
            // Siswa Users
            [
                'name' => 'Siswa User',
                'email' => 'siswa@example.com',
                'password' => Hash::make('password'),
                'role' => 'siswa',
            ],
            [
                'name' => 'Ahmad Siswa',
                'email' => 'siswa@monitoring.com',
                'password' => Hash::make('password'),
                'role' => 'siswa',
            ],
            [
                'name' => 'Budi Siswa',
                'email' => 'budi.siswa@example.com',
                'password' => Hash::make('password'),
                'role' => 'siswa',
            ],
            [
                'name' => 'Citra Siswa',
                'email' => 'citra.siswa@example.com',
                'password' => Hash::make('password'),
                'role' => 'siswa',
            ],
        ];

        foreach ($users as $user) {
            // Use updateOrCreate to avoid duplicate entries when seeding repeatedly
            User::updateOrCreate(
                ['email' => $user['email']],
                $user
            );
        }
    }
}
