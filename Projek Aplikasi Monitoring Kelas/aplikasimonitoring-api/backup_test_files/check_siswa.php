<?php

require __DIR__.'/vendor/autoload.php';

$app = require_once __DIR__.'/bootstrap/app.php';
$app->make('Illuminate\Contracts\Console\Kernel')->bootstrap();

use App\Models\Siswa;

echo "===== DAFTAR SISWA & EMAIL DI DATABASE =====\n\n";
echo str_pad("ID", 5) . "| " . str_pad("Nama", 25) . "| " . str_pad("Email", 35) . "| Kelas\n";
echo str_repeat("-", 90) . "\n";

Siswa::with(['user', 'kelas'])->get()->each(function($siswa) {
    $email = $siswa->user->email ?? 'N/A';
    $kelas = $siswa->kelas->nama_kelas ?? 'N/A';
    
    echo str_pad($siswa->id, 5) . "| " . 
         str_pad($siswa->nama, 25) . "| " . 
         str_pad($email, 35) . "| " . 
         $kelas . "\n";
});

echo "\n";
