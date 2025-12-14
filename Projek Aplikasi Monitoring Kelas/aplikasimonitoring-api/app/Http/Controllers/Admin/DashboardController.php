<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\User;
use App\Models\Guru;
use App\Models\Siswa;
use App\Models\Kelas;
use App\Models\Mapel;
use App\Models\JadwalKelas;
use App\Models\GuruMengajar;
use App\Models\GuruPengganti;
use App\Models\KehadiranGuru;
use Carbon\Carbon;

class DashboardController extends Controller
{
    public function index()
    {
        $today = Carbon::today()->toDateString();
        
        $stats = [
            'users' => User::count(),
            'gurus' => Guru::count(),
            'siswas' => Siswa::count(),
            'kelas' => Kelas::count(),
            'mapels' => Mapel::count(),
            'jadwals' => JadwalKelas::count(),
            'guru_mengajar' => GuruMengajar::count(),
            'guru_pengganti' => GuruPengganti::count(),
        ];

        $usersByRole = User::selectRaw('role, count(*) as total')
            ->groupBy('role')
            ->get();

        $recentUsers = User::latest()->take(5)->get();
        
        // Statistik kehadiran guru hari ini
        $kehadiranHariIni = [
            'hadir' => KehadiranGuru::where('tanggal', $today)->where('status', 'hadir')->count(),
            'tidak_hadir' => KehadiranGuru::where('tanggal', $today)->where('status', 'tidak_hadir')->count(),
            'izin' => KehadiranGuru::where('tanggal', $today)->where('status', 'izin')->count(),
            'sakit' => KehadiranGuru::where('tanggal', $today)->where('status', 'sakit')->count(),
        ];
        
        // Kehadiran guru terbaru yang di-input siswa (tidak hadir)
        $kehadiranDariSiswa = KehadiranGuru::with(['guru', 'kelas', 'mapel', 'inputBySiswa'])
            ->whereNotNull('input_by_siswa_id')
            ->where('tanggal', $today)
            ->orderBy('created_at', 'desc')
            ->take(10)
            ->get();
        
        // Guru izin/sakit yang di-input kurikulum
        $guruIzinSakitDariKurikulum = KehadiranGuru::with(['guru', 'kelas', 'mapel', 'inputByKurikulum'])
            ->whereNotNull('input_by_kurikulum_id')
            ->whereIn('status', ['izin', 'sakit'])
            ->where('tanggal', $today)
            ->orderBy('created_at', 'desc')
            ->take(10)
            ->get();
        
        // Statistik aktivitas per role
        $roleStats = [
            'siswa' => [
                'total' => User::where('role', 'siswa')->count(),
                'input_kehadiran_today' => KehadiranGuru::whereNotNull('input_by_siswa_id')
                    ->where('tanggal', $today)->count(),
            ],
            'kurikulum' => [
                'total' => User::where('role', 'kurikulum')->count(),
                'input_izin_sakit_today' => KehadiranGuru::whereNotNull('input_by_kurikulum_id')
                    ->where('tanggal', $today)->count(),
                'guru_pengganti_today' => GuruPengganti::whereDate('created_at', $today)->count(),
            ],
            'kepala_sekolah' => [
                'total' => User::where('role', 'kepala_sekolah')->count(),
            ],
        ];

        return view('admin.dashboard', compact(
            'stats', 
            'usersByRole', 
            'recentUsers',
            'kehadiranHariIni',
            'kehadiranDariSiswa',
            'guruIzinSakitDariKurikulum',
            'roleStats'
        ));
    }
}
