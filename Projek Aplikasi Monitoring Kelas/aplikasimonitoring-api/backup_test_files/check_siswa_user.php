<?php

require __DIR__.'/vendor/autoload.php';

$app = require_once __DIR__.'/bootstrap/app.php';
$kernel = $app->make(Illuminate\Contracts\Console\Kernel::class);
$kernel->bootstrap();

use Illuminate\Support\Facades\DB;

echo "=== Checking siswa@example.com ===\n\n";

$user = DB::table('users')->where('email', 'siswa@example.com')->first();

if (!$user) {
    echo "❌ User siswa@example.com tidak ditemukan!\n";
    exit;
}

echo "User siswa@example.com:\n";
echo "  ID: {$user->id}\n";
echo "  Name: {$user->name}\n";
echo "  Role: {$user->role}\n\n";

$siswa = DB::table('siswas')->where('user_id', $user->id)->first();

if ($siswa) {
    echo "✅ Siswa record FOUND:\n";
    echo "  Siswa ID: {$siswa->id}\n";
    echo "  Nama: {$siswa->nama}\n";
    echo "  NIS: {$siswa->nis}\n";
    echo "  Kelas ID: {$siswa->kelas_id}\n";
} else {
    echo "❌ TIDAK ADA di tabel siswas!\n";
    echo "⚠️  User ini tidak bisa submit absensi karena tidak punya siswa_id\n";
}
