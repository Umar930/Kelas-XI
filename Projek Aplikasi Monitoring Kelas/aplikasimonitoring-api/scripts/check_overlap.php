<?php
require __DIR__ . '/../vendor/autoload.php';
$app = require __DIR__ . '/../bootstrap/app.php';
$kernel = $app->make(Illuminate\Contracts\Console\Kernel::class);
$kernel->bootstrap();

use App\Models\JadwalKelas;
use Carbon\Carbon;

$start = $argv[1] ?? '07:00';
$end = $argv[2] ?? '09:20';
$startTime = Carbon::createFromFormat('H:i', $start)->format('H:i:s');
$endTime = Carbon::createFromFormat('H:i', $end)->format('H:i:s');
print("new interval $startTime - $endTime\n");
$ex = JadwalKelas::where('kelas_id',1)->where('hari','Senin')->where('jam_mulai','<',$endTime)->where('jam_selesai','>',$startTime)->get();
foreach($ex as $e){ print('conflict ID '.$e->id.' jam '.$e->jam_mulai.' - '.$e->jam_selesai."\n"); }
print('found: '.count($ex)."\n");
