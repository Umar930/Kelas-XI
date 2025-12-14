<?php
require __DIR__ . '/../vendor/autoload.php';
$app = require __DIR__ . '/../bootstrap/app.php';
$kernel = $app->make(Illuminate\Contracts\Console\Kernel::class);
$kernel->bootstrap();

use App\Models\Kelas;
use App\Models\JadwalKelas;

$kelas = Kelas::where('nama_kelas','X RPL')->first();
if(!$kelas){ echo "kelas X RPL not found\n"; exit(0); }
echo "kelas_id=".$kelas->id."\n";
$jadwals = JadwalKelas::where('kelas_id',$kelas->id)->where('hari','Senin')->get();
echo "jumlah_jadwal_senin=".$jadwals->count()."\n";
foreach ($jadwals as $j) {
    echo 'ID:'.$j->id.' jam '.$j->jam_mulai.' - '.$j->jam_selesai.' guru_id='.$j->guru_id.' mapel_id='.$j->mapel_id."\n";
}
