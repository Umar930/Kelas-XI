<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;

class CreateSiswaDefaultUserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        echo "=== Creating Default Siswa User ===\n";

        // Check if siswa@example.com already exists
        $existingUser = DB::table('users')->where('email', 'siswa@example.com')->first();

        if ($existingUser) {
            echo "✅ User siswa@example.com sudah ada (ID: {$existingUser->id})\n";
            return;
        }

        // Create default siswa user for dashboard login
        $userId = DB::table('users')->insertGetId([
            'name' => 'Siswa User',
            'email' => 'siswa@example.com',
            'password' => Hash::make('password'),
            'role' => 'siswa',
            'created_at' => now(),
            'updated_at' => now(),
        ]);

        echo "✅ User siswa@example.com berhasil dibuat (ID: {$userId})\n";
        echo "   Email: siswa@example.com\n";
        echo "   Password: password\n";
        echo "   Role: siswa\n";
        echo "   Name: Siswa User\n";

        // Note: This is a generic account for dashboard access
        // Not linked to any specific siswa record in siswas table
        echo "\n⚠️  CATATAN: User ini untuk login dashboard saja, tidak terkait dengan data siswa tertentu.\n";
    }
}
