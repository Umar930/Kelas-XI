<?php

require __DIR__.'/vendor/autoload.php';

$app = require_once __DIR__.'/bootstrap/app.php';
$app->make('Illuminate\Contracts\Console\Kernel')->bootstrap();

use App\Models\User;
use App\Models\Siswa;
use App\Models\AbsensiSiswa;

echo "===== CREATE ABSENSI DARI BERBAGAI SISWA =====\n\n";

// Login dan create absensi untuk setiap siswa
$siswaList = Siswa::whereHas('user')->with('user')->limit(5)->get();

foreach ($siswaList as $siswa) {
    echo "Siswa: {$siswa->nama} ({$siswa->user->email})\n";
    
    // Create absensi
    $absensi = AbsensiSiswa::create([
        'siswa_id' => $siswa->id,
        'kelas_id' => $siswa->kelas_id,
        'tanggal' => now()->toDateString(),
        'status' => 'hadir',
        'keterangan' => 'Test dari script - ' . $siswa->nama,
    ]);
    
    echo "  ✅ Created absensi ID: {$absensi->id}\n\n";
}

echo "===== SELESAI =====\n";
