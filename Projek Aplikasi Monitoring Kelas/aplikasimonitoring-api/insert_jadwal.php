<?php

require __DIR__ . '/vendor/autoload.php';

$app = require_once __DIR__ . '/bootstrap/app.php';
$app->make('Illuminate\Contracts\Console\Kernel')->bootstrap();

use Illuminate\Support\Facades\DB;

$jadwals = [
    // SENIN - X RPL
    ['kelas_id'=>1, 'mapel_id'=>1, 'guru_id'=>1, 'hari'=>'Senin', 'jam_mulai'=>'07:00', 'jam_selesai'=>'08:30', 'tahun_ajaran_id'=>3],
    ['kelas_id'=>1, 'mapel_id'=>2, 'guru_id'=>2, 'hari'=>'Senin', 'jam_mulai'=>'08:30', 'jam_selesai'=>'10:00', 'tahun_ajaran_id'=>3],
    ['kelas_id'=>1, 'mapel_id'=>3, 'guru_id'=>3, 'hari'=>'Senin', 'jam_mulai'=>'10:15', 'jam_selesai'=>'11:45', 'tahun_ajaran_id'=>3],
    
    // SELASA - X RPL
    ['kelas_id'=>1, 'mapel_id'=>6, 'guru_id'=>5, 'hari'=>'Selasa', 'jam_mulai'=>'07:00', 'jam_selesai'=>'08:30', 'tahun_ajaran_id'=>3],
    ['kelas_id'=>1, 'mapel_id'=>7, 'guru_id'=>6, 'hari'=>'Selasa', 'jam_mulai'=>'08:30', 'jam_selesai'=>'10:00', 'tahun_ajaran_id'=>3],
    ['kelas_id'=>1, 'mapel_id'=>9, 'guru_id'=>7, 'hari'=>'Selasa', 'jam_mulai'=>'10:15', 'jam_selesai'=>'11:45', 'tahun_ajaran_id'=>3],
    
    // RABU - X RPL
    ['kelas_id'=>1, 'mapel_id'=>4, 'guru_id'=>1, 'hari'=>'Rabu', 'jam_mulai'=>'07:00', 'jam_selesai'=>'08:30', 'tahun_ajaran_id'=>3],
    ['kelas_id'=>1, 'mapel_id'=>5, 'guru_id'=>2, 'hari'=>'Rabu', 'jam_mulai'=>'08:30', 'jam_selesai'=>'10:00', 'tahun_ajaran_id'=>3],
    ['kelas_id'=>1, 'mapel_id'=>8, 'guru_id'=>3, 'hari'=>'Rabu', 'jam_mulai'=>'10:15', 'jam_selesai'=>'11:45', 'tahun_ajaran_id'=>3],
    
    // KAMIS - X RPL
    ['kelas_id'=>1, 'mapel_id'=>1, 'guru_id'=>1, 'hari'=>'Kamis', 'jam_mulai'=>'07:00', 'jam_selesai'=>'08:30', 'tahun_ajaran_id'=>3],
    ['kelas_id'=>1, 'mapel_id'=>10, 'guru_id'=>5, 'hari'=>'Kamis', 'jam_mulai'=>'08:30', 'jam_selesai'=>'10:00', 'tahun_ajaran_id'=>3],
    ['kelas_id'=>1, 'mapel_id'=>3, 'guru_id'=>3, 'hari'=>'Kamis', 'jam_mulai'=>'10:15', 'jam_selesai'=>'11:45', 'tahun_ajaran_id'=>3],
    
    // JUMAT - X RPL
    ['kelas_id'=>1, 'mapel_id'=>2, 'guru_id'=>2, 'hari'=>'Jumat', 'jam_mulai'=>'07:00', 'jam_selesai'=>'08:30', 'tahun_ajaran_id'=>3],
    ['kelas_id'=>1, 'mapel_id'=>6, 'guru_id'=>5, 'hari'=>'Jumat', 'jam_mulai'=>'08:30', 'jam_selesai'=>'10:00', 'tahun_ajaran_id'=>3],
    
    // SABTU - X RPL
    ['kelas_id'=>1, 'mapel_id'=>9, 'guru_id'=>7, 'hari'=>'Sabtu', 'jam_mulai'=>'07:00', 'jam_selesai'=>'08:30', 'tahun_ajaran_id'=>3],
    ['kelas_id'=>1, 'mapel_id'=>7, 'guru_id'=>6, 'hari'=>'Sabtu', 'jam_mulai'=>'08:30', 'jam_selesai'=>'10:00', 'tahun_ajaran_id'=>3],
];

$count = 0;
foreach($jadwals as $j) {
    // ensure tahun_ajaran_id points to an existing tahun_ajaran (use latest)
    $tahunId = DB::table('tahun_ajarans')->orderBy('id', 'desc')->value('id') ?: 1;
    $j['tahun_ajaran_id'] = $tahunId;
    $j['created_at'] = now();
    $j['updated_at'] = now();
    DB::table('jadwal_kelas')->updateOrInsert(
        [
            'kelas_id' => $j['kelas_id'],
            'mapel_id' => $j['mapel_id'],
            'hari' => $j['hari'],
            'jam_mulai' => $j['jam_mulai'],
        ],
        $j
    );
    $count++;
}

echo "Berhasil menambahkan {$count} jadwal untuk kelas X RPL (Senin-Sabtu)!\n";
