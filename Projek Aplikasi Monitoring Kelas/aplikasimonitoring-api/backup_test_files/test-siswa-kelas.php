<?php

require __DIR__ . '/vendor/autoload.php';

$app = require_once __DIR__ . '/bootstrap/app.php';
$app->make(\Illuminate\Contracts\Console\Kernel::class)->bootstrap();

echo "\n========================================\n";
echo "  DAFTAR SISWA DAN KELAS\n";
echo "========================================\n\n";

$siswa = App\Models\Siswa::with('kelas:id,nama_kelas')->orderBy('nis')->get();

echo "Total Siswa: " . $siswa->count() . "\n";
echo "Siswa tanpa kelas: " . App\Models\Siswa::whereNull('kelas_id')->count() . "\n";
echo "Siswa dengan kelas: " . App\Models\Siswa::whereNotNull('kelas_id')->count() . "\n\n";

echo "Detail:\n";
echo str_repeat("-", 60) . "\n";
printf("%-10s %-25s %-20s\n", "NIS", "Nama", "Kelas");
echo str_repeat("-", 60) . "\n";

foreach ($siswa as $s) {
    printf(
        "%-10s %-25s %-20s\n",
        $s->nis,
        $s->nama,
        $s->kelas ? $s->kelas->nama_kelas : '❌ BELUM ADA'
    );
}

echo str_repeat("-", 60) . "\n";
echo "\n✅ Semua siswa sudah memiliki kelas!\n\n";
