<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Admin\AuthController;
use App\Http\Controllers\Admin\DashboardController;
use App\Http\Controllers\Admin\UserController;
use App\Http\Controllers\Admin\GuruController;
use App\Http\Controllers\Admin\MapelController;
use App\Http\Controllers\Admin\KelasController;
use App\Http\Controllers\Admin\TahunAjaranController;
use App\Http\Controllers\Admin\SiswaController;
use App\Http\Controllers\Admin\JadwalController;
use App\Http\Controllers\Admin\GuruMengajarController;
use App\Http\Controllers\Admin\GuruPenggantiController;
use App\Http\Controllers\Admin\MonitoringController;

Route::get('/', function () {
    return redirect()->route('admin.login');
});

// Admin Auth Routes
Route::prefix('admin')->name('admin.')->group(function () {
    Route::get('/login', [AuthController::class, 'showLogin'])->name('login');
    Route::post('/login', [AuthController::class, 'login'])->name('login.post');
    Route::post('/logout', [AuthController::class, 'logout'])->name('logout');
});

// Local-only debug route for admin user info (only enabled in app.debug)
Route::get('/admin/debug/user-info', [App\Http\Controllers\Admin\DebugController::class, 'userInfo'])
    ->name('admin.debug.user-info');

Route::post('/admin/debug/reset-admin-password', [App\Http\Controllers\Admin\DebugController::class, 'resetAdminPassword'])
    ->name('admin.debug.reset-admin-password');

// Admin Protected Routes
Route::prefix('admin')->name('admin.')->middleware('admin')->group(function () {
    // Dashboard
    Route::get('/dashboard', [DashboardController::class, 'index'])->name('dashboard');
    
    // Master Data - Users
    Route::resource('users', UserController::class);
    Route::post('users/import', [UserController::class, 'import'])->name('users.import');
    
    // Master Data - Guru
    Route::resource('gurus', GuruController::class);
    Route::post('gurus/import', [GuruController::class, 'import'])->name('gurus.import');

    
    // Master Data - Mata Pelajaran
    Route::resource('mapels', MapelController::class);
    Route::post('mapels/import', [MapelController::class, 'import'])->name('mapels.import');
    
    // Master Data - Kelas
    Route::resource('kelas', KelasController::class);
    Route::post('kelas/import', [KelasController::class, 'import'])->name('kelas.import');
    
    // Master Data - Tahun Ajaran
    Route::resource('tahun-ajaran', TahunAjaranController::class);
    Route::post('tahun-ajaran/import', [TahunAjaranController::class, 'import'])->name('tahun-ajaran.import');
    
    // Master Data - Siswa
    Route::resource('siswas', SiswaController::class);
    Route::post('siswas/import', [SiswaController::class, 'import'])->name('siswas.import');
    
    // Akademik - Jadwal
    Route::resource('jadwals', JadwalController::class);
    Route::post('jadwals/{jadwal}/izin-sakit', [JadwalController::class, 'izinSakit'])->name('jadwals.izin-sakit');
    Route::post('jadwals/import', [JadwalController::class, 'import'])->name('jadwals.import');
    
    // Akademik - Guru Mengajar
    Route::resource('guru-mengajar', GuruMengajarController::class);
    Route::post('guru-mengajar/import', [GuruMengajarController::class, 'import'])->name('guru-mengajar.import');
    
    // Akademik - Guru Pengganti
    Route::resource('guru-pengganti', GuruPenggantiController::class);
    Route::post('guru-pengganti/import', [GuruPenggantiController::class, 'import'])->name('guru-pengganti.import');
    
    // Monitoring - Kehadiran Guru & Aktivitas Role
    Route::prefix('monitoring')->name('monitoring.')->group(function () {
        Route::get('/kehadiran-guru', [MonitoringController::class, 'kehadiranGuru'])->name('kehadiran-guru');
        Route::get('/kehadiran-guru/{guru_id}', [MonitoringController::class, 'detailKehadiranGuru'])->name('kehadiran-guru.detail');
        Route::get('/siswa', [MonitoringController::class, 'monitoringSiswa'])->name('siswa');
        Route::get('/kurikulum', [MonitoringController::class, 'monitoringKurikulum'])->name('kurikulum');
        Route::get('/kepala-sekolah', [MonitoringController::class, 'monitoringKepalaSekolah'])->name('kepala-sekolah');
    });
});
