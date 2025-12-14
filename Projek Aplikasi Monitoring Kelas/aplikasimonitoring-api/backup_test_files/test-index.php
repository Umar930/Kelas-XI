<?php

require __DIR__ . '/vendor/autoload.php';

$app = require_once __DIR__ . '/bootstrap/app.php';
$app->make(\Illuminate\Contracts\Console\Kernel::class)->bootstrap();

use App\Models\AbsensiSiswa;
use App\Models\Siswa;
use App\Models\User;

try {
    $user = User::find(7);
    echo "User found: " . $user->name . "\n";
    
    $query = AbsensiSiswa::with(['siswa.kelas']);
    
    if ($user->role === 'siswa') {
        $siswa = Siswa::where('user_id', $user->id)->first();
        if ($siswa) {
            echo "Siswa found: " . $siswa->nama . " (ID: {$siswa->id})\n";
            $query->where('siswa_id', $siswa->id);
        }
    }
    
    $absensiList = $query->orderBy('tanggal', 'desc')
                         ->orderBy('created_at', 'desc')
                         ->get();
    
    echo "Total absensi: " . $absensiList->count() . "\n";
    
} catch (\Exception $e) {
    echo "ERROR: " . $e->getMessage() . "\n";
    echo "File: " . $e->getFile() . "\n";
    echo "Line: " . $e->getLine() . "\n";
}
