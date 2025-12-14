<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;

return new class extends Migration
{
    public function up()
    {
        // Cek apakah user sudah ada
        $existingUser = DB::table('users')->where('email', 'hendra.wijaya@example.com')->first();
        
        if ($existingUser) {
            echo "⚠️ User 'Hendra Wijaya' sudah ada, skip migration\n";
            return;
        }

        // Buat user Hendra Wijaya
        $userId = DB::table('users')->insertGetId([
            'name' => 'Hendra Wijaya',
            'email' => 'hendra.wijaya@example.com',
            'password' => Hash::make('password'),
            'role' => 'siswa',
            'created_at' => now(),
            'updated_at' => now(),
        ]);

        // Cek apakah kelas dengan ID 1 ada
        $kelasId = DB::table('kelas')->first()?->id;
        
        if ($kelasId) {
            // Buat siswa Hendra Wijaya
            DB::table('siswas')->insert([
                'user_id' => $userId,
                'nis' => '2021010',
                'nama' => 'Hendra Wijaya',
                'kelas_id' => $kelasId,
                'created_at' => now(),
                'updated_at' => now(),
            ]);
            echo "✅ User & Siswa 'Hendra Wijaya' berhasil dibuat!\n";
        } else {
            echo "⚠️ User 'Hendra Wijaya' dibuat tanpa siswa (kelas belum ada)\n";
        }
        
        echo "   Email: hendra.wijaya@example.com\n";
        echo "   Password: password\n";
    }

    public function down()
    {
        DB::table('users')->where('email', 'hendra.wijaya@example.com')->delete();
    }
};
