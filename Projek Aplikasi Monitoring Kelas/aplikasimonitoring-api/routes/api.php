<?php

use App\Http\Controllers\Api\AuthController;
use App\Http\Controllers\Api\GuruController;
use App\Http\Controllers\Api\MapelController;
use App\Http\Controllers\Api\TahunAjaranController;
use App\Http\Controllers\Api\KelasController;
use App\Http\Controllers\Api\SiswaController;
use App\Http\Controllers\Api\AbsensiGuruController;
use App\Http\Controllers\Api\SiswaFiturController;
use App\Http\Controllers\Api\KurikulumFiturController;
use App\Http\Controllers\Api\KepalaSekolahFiturController;
use App\Http\Controllers\Api\UserController;
use App\Http\Controllers\Api\MataPelajaranController;
use App\Http\Controllers\Api\JadwalController;
use App\Http\Controllers\Api\GuruMengajarController;
use App\Http\Controllers\Api\GuruPenggantiController;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "api" middleware group. Make something great!
|
*/

// ==================== PUBLIC ROUTES - TIDAK MEMERLUKAN AUTENTIKASI ====================

// Login endpoint - untuk autentikasi user
Route::post('/login', [AuthController::class, 'login'])->name('login');
Route::post('/auth/login', [AuthController::class, 'login'])->name('auth.login'); // Alias

// Test endpoint - untuk cek koneksi server
Route::get('/test', function() {
    return response()->json([
        'success' => true,
        'message' => 'API Server is running',
        'server_time' => now()->toDateTimeString(),
        'php_version' => phpversion(),
        'laravel_version' => app()->version()
    ]);
});

// Health check endpoint
Route::get('/health', function() {
    return response()->json([
        'status' => 'ok',
        'timestamp' => now()->toIso8601String()
    ]);
});

// Debug endpoint untuk test request dari frontend
Route::any('/debug-request', function(\Illuminate\Http\Request $request) {
    \Log::info('DEBUG Request:', [
        'method' => $request->method(),
        'url' => $request->fullUrl(),
        'headers' => $request->headers->all(),
        'body' => $request->all(),
        'json' => $request->json()->all()
    ]);
    
    return response()->json([
        'success' => true,
        'message' => 'Request logged',
        'received' => [
            'method' => $request->method(),
            'all' => $request->all(),
            'json' => $request->json()->all()
        ]
    ]);
});

// Public routes untuk testing (HAPUS atau COMMENT di production!)
Route::get('/public/gurus', [GuruController::class, 'index']); // Tanpa auth untuk testing
Route::get('/public/mapels', [MapelController::class, 'index']);
Route::get('/public/kelas', [KelasController::class, 'index']);
Route::get('/public/siswas', [SiswaController::class, 'index']);

// ==================== PROTECTED ROUTES - MEMERLUKAN AUTENTIKASI ====================

Route::middleware('auth:sanctum')->group(function () {
    // ==================== AUTH ROUTES ====================
    Route::get('/check', [AuthController::class, 'check']);
    Route::get('/auth/check', [AuthController::class, 'check']); // Alias
    Route::post('/logout', [AuthController::class, 'logout']);
    Route::post('/auth/logout', [AuthController::class, 'logout']); // Alias
    // Save device token for push notifications (FCM)
    Route::post('/device-token', [AuthController::class, 'saveDeviceToken']);
    
    // ==================== ADMIN PANEL - USER MANAGEMENT ====================
    Route::get('/users/guru', [UserController::class, 'guru']);
    Route::get('/users/siswa', [UserController::class, 'siswa']);
    Route::resource('users', UserController::class);
    
    // ==================== ADMIN PANEL - MATA PELAJARAN ====================
    // Frontend memanggil: /api/mata-pelajaran
    Route::get('/mata-pelajaran', [MataPelajaranController::class, 'index']);
    Route::post('/mata-pelajaran', [MataPelajaranController::class, 'store']);
    Route::get('/mata-pelajaran/{id}', [MataPelajaranController::class, 'show']);
    Route::put('/mata-pelajaran/{id}', [MataPelajaranController::class, 'update']);
    Route::patch('/mata-pelajaran/{id}', [MataPelajaranController::class, 'update']);
    Route::delete('/mata-pelajaran/{id}', [MataPelajaranController::class, 'destroy']);
    
    // Alias untuk kompatibilitas
    Route::get('/mapels', [MataPelajaranController::class, 'index']);
    Route::resource('mapels', MapelController::class);
    
    // ==================== ADMIN PANEL - KELAS ====================
    // Frontend memanggil: /api/kelas
    Route::get('/kelas', [KelasController::class, 'index']);
    Route::post('/kelas', [KelasController::class, 'store']);
    Route::get('/kelas/{id}', [KelasController::class, 'show']);
    Route::put('/kelas/{id}', [KelasController::class, 'update']);
    Route::patch('/kelas/{id}', [KelasController::class, 'update']);
    Route::delete('/kelas/{id}', [KelasController::class, 'destroy']);
    
    // ==================== ADMIN PANEL - TAHUN AJARAN ====================
    // Frontend memanggil: /api/tahun-ajaran
    Route::get('/tahun-ajaran', [TahunAjaranController::class, 'index']);
    Route::post('/tahun-ajaran', [TahunAjaranController::class, 'store']);
    Route::get('/tahun-ajaran/{id}', [TahunAjaranController::class, 'show']);
    Route::put('/tahun-ajaran/{id}', [TahunAjaranController::class, 'update']);
    Route::patch('/tahun-ajaran/{id}', [TahunAjaranController::class, 'update']);
    Route::delete('/tahun-ajaran/{id}', [TahunAjaranController::class, 'destroy']);
    
    // Alias untuk kompatibilitas (jika ada yang masih pakai plural)
    Route::get('/tahun-ajarans', [TahunAjaranController::class, 'index']);
    Route::get('/tahun-ajarans/active', [TahunAjaranController::class, 'getActive']);
    Route::post('/tahun-ajarans/{id}/activate', [TahunAjaranController::class, 'activate']);
    
    // ==================== ADMIN PANEL - GURU ====================
    // Frontend memanggil: /api/guru
    Route::get('/guru', [GuruController::class, 'index']);
    Route::post('/guru', [GuruController::class, 'store']);
    Route::get('/guru/{id}', [GuruController::class, 'show']);
    Route::put('/guru/{id}', [GuruController::class, 'update']);
    Route::patch('/guru/{id}', [GuruController::class, 'update']);
    Route::delete('/guru/{id}', [GuruController::class, 'destroy']);
    
    // Alias untuk kompatibilitas (jika ada yang masih pakai plural)
    Route::get('/gurus', [GuruController::class, 'index']);
    
    // ==================== ADMIN PANEL - JADWAL ====================
    Route::get('/jadwal/timetable', [JadwalController::class, 'timetable']);
    Route::resource('jadwal', JadwalController::class);
    
    // ==================== ADMIN PANEL - GURU MENGAJAR ====================
    Route::resource('guru-mengajar', GuruMengajarController::class);
    
    // ==================== ADMIN PANEL - GURU PENGGANTI ====================
    Route::post('/guru-pengganti/{id}/approve', [GuruPenggantiController::class, 'approve']);
    Route::post('/guru-pengganti/{id}/reject', [GuruPenggantiController::class, 'reject']);
    Route::resource('guru-pengganti', GuruPenggantiController::class);
    
    // ==================== ADMIN PANEL - SISWA ====================
    Route::resource('siswas', SiswaController::class);
    
    // ==================== ADMIN PANEL - ABSENSI GURU ====================
    Route::resource('absensi-gurus', AbsensiGuruController::class);
});

// ============================================
// FITUR ROLE SISWA
// ============================================
Route::middleware(['auth:sanctum', 'role:siswa,kurikulum,admin'])->prefix('siswa')->group(function() {
    // 1. Input kehadiran guru
    Route::post('/kehadiran-guru', [SiswaFiturController::class, 'inputKehadiranGuru']);
    
    // 2. Request guru pengganti
    Route::post('/request-guru-pengganti', [SiswaFiturController::class, 'requestGuruPengganti']);
    
    // 3. Get notifikasi (izin/sakit, guru pengganti)
    Route::get('/notifikasi', [SiswaFiturController::class, 'getNotifikasi']);
    Route::put('/notifikasi/{id}/read', [SiswaFiturController::class, 'markNotifikasiAsRead']);
    
    // 4. Filter semua kelas dengan siswa
    Route::get('/kelas-semua', [SiswaFiturController::class, 'getSemuaKelas']);
    Route::get('/kelas-saya', [SiswaFiturController::class, 'getKelasSaya']);
    
    // 5. Jadwal berdasarkan hari dan kelas (untuk Tab Jadwal)
    Route::get('/jadwal', [SiswaFiturController::class, 'getJadwalByHari']);
    
    // 6. List kehadiran guru hari ini (untuk Tab List)
    Route::get('/list-kehadiran', [SiswaFiturController::class, 'getListKehadiran']);
    
    // 7. Daftar guru (untuk dropdown pilih guru pengganti)
    Route::get('/list-guru', [SiswaFiturController::class, 'getListGuru']);
    
    // 8. Profil siswa yang login
    Route::get('/profil', [SiswaFiturController::class, 'getProfil']);
    
    // 9. Daftar hari dan kelas (untuk filter dropdown)
    Route::get('/list-hari', [SiswaFiturController::class, 'getListHari']);
    Route::get('/list-kelas', [SiswaFiturController::class, 'getListKelas']);
    
    // 10. Tambah/Hapus jadwal kelas
    Route::post('/jadwal', [SiswaFiturController::class, 'tambahJadwal']);
    Route::delete('/jadwal/{id}', [SiswaFiturController::class, 'hapusJadwal']);
    
    // 11. Daftar mapel (untuk dropdown tambah jadwal)
    Route::get('/list-mapel', [SiswaFiturController::class, 'getListMapel']);
});

// ============================================
// FITUR ROLE KURIKULUM
// ============================================
Route::middleware(['auth:sanctum', 'role:kurikulum,admin'])->prefix('kurikulum')->group(function() {
    // 1. Filter kelas dengan status kehadiran guru
    Route::get('/kelas-kehadiran', [KurikulumFiturController::class, 'getKelasWithKehadiran']);
    
    // 2. Input izin/sakit guru (otomatis kirim notifikasi ke siswa)
    Route::post('/guru-izin-sakit', [KurikulumFiturController::class, 'inputGuruIzinSakit']);
    
    // 3. Assign guru pengganti
    Route::post('/assign-guru-pengganti', [KurikulumFiturController::class, 'assignGuruPengganti']);
    
    // Bonus: Get guru yang tersedia untuk pengganti
    Route::get('/guru-tersedia', [KurikulumFiturController::class, 'getGuruTersedia']);

    // ==================== TAB 1: GURU PENGGANTI ====================
    // Daftar request guru pengganti dari siswa
    Route::get('/request-pengganti', [KurikulumFiturController::class, 'getRequestPengganti']);
    // Pilih guru pengganti untuk request siswa
    Route::post('/pilih-guru-pengganti', [KurikulumFiturController::class, 'pilihGuruPengganti']);
    // Tolak request guru pengganti
    Route::post('/tolak-request-pengganti', [KurikulumFiturController::class, 'tolakRequestPengganti']);

    // ==================== TAB 2: DAFTAR GURU ====================
    // Daftar guru dengan status kehadiran (filter hari & kelas)
    Route::get('/daftar-guru', [KurikulumFiturController::class, 'getDaftarGuru']);

    // ==================== TAB 3: LAPORAN ====================
    // Laporan request guru pengganti dari siswa
    Route::get('/laporan-request', [KurikulumFiturController::class, 'getLaporanRequest']);
    // Hapus satu laporan
    Route::delete('/laporan-request/{id}', [KurikulumFiturController::class, 'hapusLaporanRequest']);
    // Clear/hapus semua laporan
    Route::delete('/clear-laporan', [KurikulumFiturController::class, 'clearLaporan']);

    // ==================== HELPER ENDPOINTS ====================
    Route::get('/list-kelas', [KurikulumFiturController::class, 'getListKelas']);
    Route::get('/list-hari', [KurikulumFiturController::class, 'getListHari']);
});

// ============================================
// FITUR ROLE KEPALA SEKOLAH
// ============================================
Route::middleware(['auth:sanctum', 'role:kepala_sekolah,admin'])->prefix('kepala-sekolah')->group(function() {
    // 1. Lihat jadwal semua kelas
    Route::get('/jadwal', [KepalaSekolahFiturController::class, 'getJadwalKelas']);
    
    // 2. Lihat kehadiran guru yang diinput siswa
    Route::get('/kehadiran-guru', [KepalaSekolahFiturController::class, 'getKehadiranGuru']);
    
    // Bonus: Statistik kehadiran guru
    Route::get('/statistik-kehadiran', [KepalaSekolahFiturController::class, 'getStatistikKehadiran']);
});