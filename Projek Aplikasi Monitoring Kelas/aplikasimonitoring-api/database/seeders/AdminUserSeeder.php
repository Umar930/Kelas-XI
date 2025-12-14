<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use App\Models\User;
use Illuminate\Support\Facades\Hash;

class AdminUserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        // Cek apakah admin sudah ada
        $adminExists = User::where('email', 'admin@sekolah.sch.id')->exists();
        
        if (!$adminExists) {
            User::create([
                'name' => 'Administrator',
                'email' => 'admin@sekolah.sch.id',
                'password' => Hash::make('admin123'),
                'role' => 'admin',
                'email_verified_at' => now(),
            ]);
            
            echo "✅ Admin user created: admin@sekolah.sch.id / admin123\n";
        } else {
            echo "ℹ️ Admin user already exists\n";
        }
        
        // Cek apakah kurikulum sudah ada
        $kurikulumExists = User::where('email', 'kurikulum@example.com')->exists();
        
        if (!$kurikulumExists) {
            User::create([
                'name' => 'Tim Kurikulum',
                'email' => 'kurikulum@example.com',
                'password' => Hash::make('password'),
                'role' => 'kurikulum',
                'email_verified_at' => now(),
            ]);
            
            echo "✅ Kurikulum user created: kurikulum@example.com / password\n";
        } else {
            echo "ℹ️ Kurikulum user already exists\n";
        }
    }
}
