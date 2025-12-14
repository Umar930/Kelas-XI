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
use App\Models\NotifikasiSiswa;
use Carbon\Carbon;

class MonitoringController extends Controller
{
    /**
     * Halaman monitoring utama - kehadiran guru
     */
    public function kehadiranGuru(Request $request)
    {
        $tanggal = $request->input('tanggal', Carbon::today()->toDateString());
        $status = $request->input('status');
        $kelas_id = $request->input('kelas_id');
        
        $query = KehadiranGuru::with(['guru', 'kelas', 'mapel', 'inputBySiswa', 'inputByKurikulum'])
            ->where('tanggal', $tanggal);
        
        if ($status) {
            $query->where('status', $status);
        }
        
        if ($kelas_id) {
            $query->where('kelas_id', $kelas_id);
        }
        
        $kehadiranList = $query->orderBy('created_at', 'desc')->paginate(20);
        
        // Statistik kehadiran
        $statsQuery = KehadiranGuru::where('tanggal', $tanggal);
        $stats = [
            'total' => (clone $statsQuery)->count(),
            'hadir' => (clone $statsQuery)->where('status', 'hadir')->count(),
            'tidak_hadir' => (clone $statsQuery)->where('status', 'tidak_hadir')->count(),
            'izin' => (clone $statsQuery)->where('status', 'izin')->count(),
            'sakit' => (clone $statsQuery)->where('status', 'sakit')->count(),
        ];
        
        // Input oleh siswa vs kurikulum
        $inputStats = [
            'by_siswa' => KehadiranGuru::where('tanggal', $tanggal)->whereNotNull('input_by_siswa_id')->count(),
            'by_kurikulum' => KehadiranGuru::where('tanggal', $tanggal)->whereNotNull('input_by_kurikulum_id')->count(),
        ];
        
        $kelasList = Kelas::orderBy('nama_kelas')->get();
        
        return view('admin.monitoring.kehadiran-guru', compact(
            'kehadiranList', 
            'stats', 
            'inputStats',
            'tanggal', 
            'status', 
            'kelas_id',
            'kelasList'
        ));
    }
    
    /**
     * Monitoring aktivitas role Siswa
     */
    public function monitoringSiswa(Request $request)
    {
        $tanggal = $request->input('tanggal', Carbon::today()->toDateString());
        $kelas_id = $request->input('kelas_id');
        
        // Get semua siswa yang punya user account
        $query = Siswa::with(['kelas', 'user'])
            ->whereHas('user');
        
        if ($kelas_id) {
            $query->where('kelas_id', $kelas_id);
        }
        
        $siswaList = $query->orderBy('nama')->paginate(20);
        
        // Statistik input kehadiran oleh siswa
        $inputKehadiranStats = KehadiranGuru::whereNotNull('input_by_siswa_id')
            ->selectRaw('input_by_siswa_id, count(*) as total_input')
            ->groupBy('input_by_siswa_id')
            ->get()
            ->keyBy('input_by_siswa_id');
        
        // Input kehadiran hari ini oleh siswa
        $inputHariIni = KehadiranGuru::with(['guru', 'kelas', 'mapel', 'inputBySiswa'])
            ->whereNotNull('input_by_siswa_id')
            ->where('tanggal', $tanggal)
            ->orderBy('created_at', 'desc')
            ->get();
        
        $kelasList = Kelas::orderBy('nama_kelas')->get();
        
        // Stats
        $stats = [
            'total_siswa' => User::where('role', 'siswa')->count(),
            'total_input_today' => $inputHariIni->count(),
            'siswa_aktif_today' => $inputHariIni->unique('input_by_siswa_id')->count(),
        ];
        
        return view('admin.monitoring.siswa', compact(
            'siswaList',
            'inputKehadiranStats',
            'inputHariIni',
            'kelasList',
            'tanggal',
            'kelas_id',
            'stats'
        ));
    }
    
    /**
     * Monitoring aktivitas role Kurikulum
     */
    public function monitoringKurikulum(Request $request)
    {
        $tanggal = $request->input('tanggal', Carbon::today()->toDateString());
        
        // Get semua user kurikulum
        $kurikulumUsers = User::where('role', 'kurikulum')
            ->orderBy('name')
            ->get();
        
        // Input izin/sakit oleh kurikulum
        $inputIzinSakit = KehadiranGuru::with(['guru', 'kelas', 'mapel', 'inputByKurikulum'])
            ->whereNotNull('input_by_kurikulum_id')
            ->whereIn('status', ['izin', 'sakit'])
            ->where('tanggal', $tanggal)
            ->orderBy('created_at', 'desc')
            ->get();
        
        // Guru pengganti yang di-assign
        $guruPenggantiList = GuruPengganti::with(['kehadiranGuru.guru', 'kehadiranGuru.kelas', 'kehadiranGuru.mapel', 'guruPengganti'])
            ->whereDate('created_at', $tanggal)
            ->orderBy('created_at', 'desc')
            ->get();
        
        // Notifikasi yang dikirim ke siswa
        $notifikasiList = NotifikasiSiswa::with(['kelas', 'guru', 'mapel'])
            ->where('tanggal', $tanggal)
            ->orderBy('created_at', 'desc')
            ->get();
        
        $stats = [
            'total_kurikulum' => $kurikulumUsers->count(),
            'input_izin_sakit_today' => $inputIzinSakit->count(),
            'guru_pengganti_today' => $guruPenggantiList->count(),
            'notifikasi_today' => $notifikasiList->count(),
        ];
        
        return view('admin.monitoring.kurikulum', compact(
            'kurikulumUsers',
            'inputIzinSakit',
            'guruPenggantiList',
            'notifikasiList',
            'tanggal',
            'stats'
        ));
    }
    
    /**
     * Monitoring aktivitas role Kepala Sekolah
     */
    public function monitoringKepalaSekolah(Request $request)
    {
        $tanggal = $request->input('tanggal', Carbon::today()->toDateString());
        $periode = $request->input('periode', 'minggu'); // minggu, bulan
        
        // Get semua user kepala sekolah
        $kepalaSekolahUsers = User::where('role', 'kepala_sekolah')
            ->orderBy('name')
            ->get();
        
        // Tentukan range tanggal berdasarkan periode
        if ($periode === 'bulan') {
            $startDate = Carbon::parse($tanggal)->startOfMonth();
            $endDate = Carbon::parse($tanggal)->endOfMonth();
        } else {
            $startDate = Carbon::parse($tanggal)->startOfWeek();
            $endDate = Carbon::parse($tanggal)->endOfWeek();
        }
        
        // Statistik kehadiran guru selama periode
        $kehadiranPeriode = KehadiranGuru::whereBetween('tanggal', [$startDate, $endDate])
            ->selectRaw('status, count(*) as total')
            ->groupBy('status')
            ->get()
            ->keyBy('status');
        
        // Guru dengan ketidakhadiran terbanyak
        $guruTidakHadirTerbanyak = KehadiranGuru::with('guru')
            ->whereBetween('tanggal', [$startDate, $endDate])
            ->whereIn('status', ['tidak_hadir', 'izin', 'sakit'])
            ->selectRaw('guru_id, count(*) as total_tidak_hadir')
            ->groupBy('guru_id')
            ->orderBy('total_tidak_hadir', 'desc')
            ->take(10)
            ->get();
        
        // Kelas dengan masalah kehadiran terbanyak
        $kelasProblema = KehadiranGuru::with('kelas')
            ->whereBetween('tanggal', [$startDate, $endDate])
            ->whereIn('status', ['tidak_hadir', 'izin', 'sakit'])
            ->selectRaw('kelas_id, count(*) as total_masalah')
            ->groupBy('kelas_id')
            ->orderBy('total_masalah', 'desc')
            ->take(10)
            ->get();
        
        // Ringkasan harian
        $ringkasanHarian = KehadiranGuru::whereBetween('tanggal', [$startDate, $endDate])
            ->selectRaw('tanggal, status, count(*) as total')
            ->groupBy('tanggal', 'status')
            ->orderBy('tanggal')
            ->get()
            ->groupBy('tanggal');
        
        $stats = [
            'total_kepala_sekolah' => $kepalaSekolahUsers->count(),
            'total_kehadiran_periode' => KehadiranGuru::whereBetween('tanggal', [$startDate, $endDate])->count(),
            'total_hadir' => $kehadiranPeriode->get('hadir')->total ?? 0,
            'total_tidak_hadir' => ($kehadiranPeriode->get('tidak_hadir')->total ?? 0) + 
                                   ($kehadiranPeriode->get('izin')->total ?? 0) + 
                                   ($kehadiranPeriode->get('sakit')->total ?? 0),
        ];
        
        return view('admin.monitoring.kepala-sekolah', compact(
            'kepalaSekolahUsers',
            'kehadiranPeriode',
            'guruTidakHadirTerbanyak',
            'kelasProblema',
            'ringkasanHarian',
            'tanggal',
            'periode',
            'startDate',
            'endDate',
            'stats'
        ));
    }
    
    /**
     * Detail kehadiran per guru
     */
    public function detailKehadiranGuru(Request $request, $guru_id)
    {
        $guru = Guru::findOrFail($guru_id);
        $bulan = $request->input('bulan', Carbon::now()->month);
        $tahun = $request->input('tahun', Carbon::now()->year);
        
        $startDate = Carbon::create($tahun, $bulan, 1)->startOfMonth();
        $endDate = Carbon::create($tahun, $bulan, 1)->endOfMonth();
        
        $kehadiranList = KehadiranGuru::with(['kelas', 'mapel', 'inputBySiswa', 'inputByKurikulum'])
            ->where('guru_id', $guru_id)
            ->whereBetween('tanggal', [$startDate, $endDate])
            ->orderBy('tanggal', 'desc')
            ->get();
        
        $stats = [
            'hadir' => $kehadiranList->where('status', 'hadir')->count(),
            'tidak_hadir' => $kehadiranList->where('status', 'tidak_hadir')->count(),
            'izin' => $kehadiranList->where('status', 'izin')->count(),
            'sakit' => $kehadiranList->where('status', 'sakit')->count(),
        ];
        
        return view('admin.monitoring.detail-guru', compact(
            'guru',
            'kehadiranList',
            'stats',
            'bulan',
            'tahun',
            'startDate',
            'endDate'
        ));
    }
}
