<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\KehadiranGuru;
use App\Models\GuruPengganti;
use App\Models\NotifikasiSiswa;
use App\Models\Kelas;
use App\Models\Siswa;
use App\Models\JadwalKelas;
use App\Models\Guru;
use Illuminate\Http\Request;
use Illuminate\Validation\ValidationException;
use Carbon\Carbon;

class SiswaFiturController extends Controller
{
    /**
     * 1. Input kehadiran guru di kelas
     * POST /api/siswa/kehadiran-guru
     */
    public function inputKehadiranGuru(Request $request)
    {
        try {
            $validated = $request->validate([
                'jadwal_id' => 'required|exists:jadwal_kelas,id',
                'status' => 'required|in:hadir,tidak_hadir',
                'keterangan' => 'nullable|string',
            ]);

            // Get jadwal untuk ambil data guru, kelas, mapel
            $jadwal = JadwalKelas::with(['guru', 'kelas', 'mapel'])->find($validated['jadwal_id']);
            
            if (!$jadwal) {
                return response()->json([
                    'success' => false,
                    'message' => 'Jadwal tidak ditemukan',
                ], 404);
            }

            // Get siswa_id dari user yang login (optional)
            $user = $request->user();
            $siswa = Siswa::where('user_id', $user->id)->first();
            $siswaId = $siswa ? $siswa->id : null;

            // Gunakan tanggal hari ini
            $tanggal = now()->format('Y-m-d');

            // Cek apakah sudah ada input kehadiran untuk jadwal ini hari ini
            $existing = KehadiranGuru::where('jadwal_id', $validated['jadwal_id'])
                ->where('tanggal', $tanggal)
                ->first();

            if ($existing) {
                // If there is an active replacement assigned for this kehadiran, record kehadiran for the replacement teacher
                $activeReplacement = GuruPengganti::where('kehadiran_guru_id', $existing->id)
                    ->where('status', 'aktif')
                    ->first();

                if ($activeReplacement && $activeReplacement->guru_pengganti_id) {
                    // Create or update kehadiran record for replacement teacher
                    $repKehadiran = KehadiranGuru::where('jadwal_id', $validated['jadwal_id'])
                        ->where('tanggal', $tanggal)
                        ->where('guru_id', $activeReplacement->guru_pengganti_id)
                        ->first();

                    if ($repKehadiran) {
                        $repKehadiran->update([
                            'status' => $validated['status'],
                            'keterangan' => $validated['keterangan'] ?? null,
                            'input_by_siswa_id' => $siswaId,
                        ]);

                        return response()->json([
                            'success' => true,
                            'message' => 'Status kehadiran untuk guru pengganti berhasil diupdate',
                            'data' => [
                                'id' => $repKehadiran->id,
                                'jadwal_id' => $repKehadiran->jadwal_id,
                                'guru_id' => $repKehadiran->guru_id,
                                'kode_guru' => optional($repKehadiran->guru)->kode_guru ?? null,
                                'guru' => optional($repKehadiran->guru)->guru ?? null,
                                'kelas_id' => $repKehadiran->kelas_id,
                                'kelas' => $jadwal->kelas ? $jadwal->kelas->nama_kelas : null,
                                'mapel_id' => $repKehadiran->mapel_id,
                                'mapel' => $jadwal->mapel ? $jadwal->mapel->mapel : null,
                                'tanggal' => $repKehadiran->tanggal,
                                'status' => $repKehadiran->status,
                            ],
                        ], 200);
                    }

                    // Create new kehadiran for replacement teacher
                    $repKehadiran = KehadiranGuru::create([
                        'jadwal_id' => $validated['jadwal_id'],
                        'guru_id' => $activeReplacement->guru_pengganti_id,
                        'kelas_id' => $jadwal->kelas_id,
                        'mapel_id' => $jadwal->mapel_id,
                        'tanggal' => $tanggal,
                        'status' => $validated['status'],
                        'keterangan' => $validated['keterangan'] ?? null,
                        'input_by_siswa_id' => $siswaId,
                    ]);

                    return response()->json([
                        'success' => true,
                        'message' => 'Kehadiran guru pengganti berhasil diinput',
                        'data' => [
                            'id' => $repKehadiran->id,
                            'jadwal_id' => $repKehadiran->jadwal_id,
                            'guru_id' => $repKehadiran->guru_id,
                            'kode_guru' => optional($repKehadiran->guru)->kode_guru ?? null,
                            'guru' => optional($repKehadiran->guru)->guru ?? null,
                            'kelas_id' => $repKehadiran->kelas_id,
                            'kelas' => $jadwal->kelas ? $jadwal->kelas->nama_kelas : null,
                            'mapel_id' => $repKehadiran->mapel_id,
                            'mapel' => $jadwal->mapel ? $jadwal->mapel->mapel : null,
                            'tanggal' => $repKehadiran->tanggal,
                            'status' => $repKehadiran->status,
                        ],
                    ], 201);
                }

                // No active replacement, update existing kehadiran (original teacher)
                $existing->update([
                    'status' => $validated['status'],
                    'keterangan' => $validated['keterangan'] ?? null,
                    'input_by_siswa_id' => $siswaId,
                ]);

                return response()->json([
                    'success' => true,
                    'message' => 'Status kehadiran guru berhasil diupdate',
                    'data' => [
                        'id' => $existing->id,
                        'jadwal_id' => $existing->jadwal_id,
                        'guru_id' => $jadwal->guru_id,
                        'kode_guru' => $jadwal->guru?->kode_guru ?? null,
                        'guru' => $jadwal->guru ? $jadwal->guru->guru : null,
                        'kelas_id' => $jadwal->kelas_id,
                        'kelas' => $jadwal->kelas ? $jadwal->kelas->nama_kelas : null,
                        'mapel_id' => $jadwal->mapel_id,
                        'mapel' => $jadwal->mapel ? $jadwal->mapel->mapel : null,
                        'tanggal' => $existing->tanggal,
                        'status' => $existing->status,
                    ],
                ], 200);
            }

            // Create new kehadiran
            $kehadiran = KehadiranGuru::create([
                'jadwal_id' => $validated['jadwal_id'],
                'guru_id' => $jadwal->guru_id,
                'kelas_id' => $jadwal->kelas_id,
                'mapel_id' => $jadwal->mapel_id,
                'tanggal' => $tanggal,
                'status' => $validated['status'],
                'keterangan' => $validated['keterangan'] ?? null,
                'input_by_siswa_id' => $siswaId,
            ]);

            return response()->json([
                'success' => true,
                'message' => 'Kehadiran guru berhasil diinput',
                'data' => [
                    'id' => $kehadiran->id,
                    'jadwal_id' => $kehadiran->jadwal_id,
                    'guru_id' => $jadwal->guru_id,
                    'kode_guru' => $jadwal->guru?->kode_guru ?? null,
                    'guru' => $jadwal->guru ? $jadwal->guru->guru : null,
                    'kelas_id' => $jadwal->kelas_id,
                    'kelas' => $jadwal->kelas ? $jadwal->kelas->nama_kelas : null,
                    'mapel_id' => $jadwal->mapel_id,
                    'mapel' => $jadwal->mapel ? $jadwal->mapel->mapel : null,
                    'tanggal' => $kehadiran->tanggal,
                    'status' => $kehadiran->status,
                ],
            ], 201);

        } catch (ValidationException $e) {
            return response()->json([
                'success' => false,
                'message' => 'Validasi gagal',
                'errors' => $e->errors(),
            ], 422);
        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan: ' . $e->getMessage(),
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    /**
     * 2. Request guru pengganti (kirim pesan ke kurikulum)
     * POST /api/siswa/request-guru-pengganti
     * Siswa hanya mengirim pesan/keterangan, kurikulum yang memilih guru pengganti
     */
    public function requestGuruPengganti(Request $request)
    {
        try {
            $validated = $request->validate([
                'kehadiran_guru_id' => 'required|exists:kehadiran_gurus,id',
                'pesan' => 'required|string|max:500', // Pesan dari siswa ke kurikulum
            ]);

            // Get siswa_id dari user yang login
            $user = $request->user();
            $siswa = Siswa::where('user_id', $user->id)->first();

            if (!$siswa) {
                return response()->json([
                    'success' => false,
                    'message' => 'Data siswa tidak ditemukan',
                ], 404);
            }

            // Get kehadiran guru data
            $kehadiran = KehadiranGuru::with(['kelas', 'guru', 'mapel', 'jadwal'])->find($validated['kehadiran_guru_id']);

            if (!$kehadiran) {
                return response()->json([
                    'success' => false,
                    'message' => 'Data kehadiran tidak ditemukan',
                ], 404);
            }

            // Validasi status harus tidak_hadir
            if ($kehadiran->status != 'tidak_hadir') {
                return response()->json([
                    'success' => false,
                    'message' => 'Request guru pengganti hanya untuk guru yang tidak hadir',
                ], 422);
            }

            // Cek apakah sudah ada request guru pengganti untuk kehadiran ini
            $existingRequest = GuruPengganti::where('kehadiran_guru_id', $kehadiran->id)->first();
            
            if ($existingRequest) {
                return response()->json([
                    'success' => false,
                    'message' => 'Sudah ada request guru pengganti untuk jadwal ini',
                    'data' => [
                        'status_request' => $existingRequest->status,
                        'pesan' => $existingRequest->keterangan,
                    ],
                ], 422);
            }

            // Create request guru pengganti (tanpa pilih guru - kurikulum yang pilih)
            $requestPengganti = GuruPengganti::create([
                'kehadiran_guru_id' => $kehadiran->id,
                'guru_id' => null, // Siswa tidak pilih guru
                'guru_pengganti_id' => null, // Akan diisi oleh kurikulum
                'jadwal_id' => $kehadiran->jadwal_id,
                'kelas_id' => $kehadiran->kelas_id,
                'mapel_id' => $kehadiran->mapel_id,
                'tanggal' => $kehadiran->tanggal,
                'status' => 'pending', // pending, aktif, ditolak
                'keterangan' => $validated['pesan'],
                'requested_by_siswa_id' => $siswa->id,
            ]);

            return response()->json([
                'success' => true,
                'message' => 'Pesan request guru pengganti berhasil dikirim ke kurikulum',
                'data' => [
                    'id' => $requestPengganti->id,
                    'kehadiran_guru_id' => $kehadiran->id,
                    'guru_tidak_hadir' => $kehadiran->guru ? $kehadiran->guru->guru : null,
                    'kelas' => $kehadiran->kelas ? $kehadiran->kelas->nama_kelas : null,
                    'mapel' => $kehadiran->mapel ? $kehadiran->mapel->mapel : null,
                    'hari' => $kehadiran->jadwal ? $kehadiran->jadwal->hari : null,
                    'jam' => $kehadiran->jadwal ? date('H:i', strtotime($kehadiran->jadwal->jam_mulai)) . ' - ' . date('H:i', strtotime($kehadiran->jadwal->jam_selesai)) : null,
                    'tanggal' => $kehadiran->tanggal,
                    'pesan' => $validated['pesan'],
                    'status' => 'pending',
                    'keterangan_status' => 'Menunggu kurikulum memilih guru pengganti',
                ],
            ], 201);

        } catch (ValidationException $e) {
            return response()->json([
                'success' => false,
                'message' => 'Validasi gagal',
                'errors' => $e->errors(),
            ], 422);
        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan: ' . $e->getMessage(),
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    /**
     * 3. Get notifikasi siswa (guru izin/sakit, guru pengganti)
     * GET /api/siswa/notifikasi
     */
    public function getNotifikasi(Request $request)
    {
        try {
            // Get siswa dari user yang login
            $user = $request->user();
            $siswa = Siswa::where('user_id', $user->id)->first();

            if (!$siswa) {
                return response()->json([
                    'success' => false,
                    'message' => 'Data siswa tidak ditemukan',
                ], 404);
            }

            // Get notifikasi untuk kelas siswa
            $notifikasi = NotifikasiSiswa::with(['guru', 'kelas', 'mapel', 'guruPengganti'])
                ->where('kelas_id', $siswa->kelas_id)
                ->orderBy('created_at', 'desc')
                ->get();

            // Hitung unread
            $unreadCount = $notifikasi->where('is_read', false)->count();

            // Map notifikasi supaya front-end mudah menampilkan guru+mapel seperti di admin
            $notifikasiData = $notifikasi->map(function($n) {
                return [
                    'id' => $n->id,
                    'guru_name' => $n->guru?->guru ?? null,
                    'kode_guru' => $n->guru?->kode_guru ?? null,
                    'mapel_name' => $n->mapel?->mapel ?? null,
                    'kelas_name' => $n->kelas?->nama_kelas ?? null,
                    'tanggal' => $n->tanggal ? $n->tanggal->toDateString() : null,
                    'tipe' => $n->tipe,
                    'pesan' => $n->pesan,
                    'is_read' => (bool) $n->is_read,
                    'created_at' => $n->created_at ? $n->created_at->toDateTimeString() : null,
                    'guru_pengganti' => $n->guruPengganti ? [
                        'id' => $n->guru_pengganti_id,
                        'name' => $n->guruPengganti?->guru ?? null,
                    ] : null,
                ];
            })->values();

            return response()->json([
                'success' => true,
                'message' => 'Data notifikasi berhasil diambil',
                'data' => [
                    'notifikasi' => $notifikasiData,
                    'unread_count' => $unreadCount,
                ],
            ], 200);

        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    /**
     * Mark notifikasi as read
     * PUT /api/siswa/notifikasi/{id}/read
     */
    public function markNotifikasiAsRead(Request $request, $id)
    {
        try {
            $notifikasi = NotifikasiSiswa::find($id);

            if (!$notifikasi) {
                return response()->json([
                    'success' => false,
                    'message' => 'Notifikasi tidak ditemukan',
                ], 404);
            }

            $notifikasi->update(['is_read' => true]);

            return response()->json([
                'success' => true,
                'message' => 'Notifikasi ditandai sudah dibaca',
                'data' => $notifikasi,
            ], 200);

        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    /**
     * 4. Filter semua kelas dengan siswa di dalamnya
     * GET /api/siswa/kelas-semua
     */
    public function getSemuaKelas(Request $request)
    {
        try {
            $kelas = Kelas::with(['siswas', 'guru'])
                ->withCount('siswas')
                ->orderBy('nama_kelas', 'asc')
                ->get();

            // Format data dengan detail siswa
            $kelasData = $kelas->map(function($k) {
                return [
                    'id' => $k->id,
                    'kode_kelas' => $k->kode_kelas,
                    'nama_kelas' => $k->nama_kelas,
                    'tingkat' => $k->tingkat,
                    'jurusan' => $k->jurusan,
                    'kapasitas' => $k->kapasitas,
                    'wali_kelas' => $k->guru ? $k->guru->nama : null,
                    'jumlah_siswa' => $k->siswas_count,
                    'siswa' => $k->siswas->map(function($s) {
                        return [
                            'id' => $s->id,
                            'nis' => $s->nis,
                            'nama' => $s->nama,
                            'jenis_kelamin' => $s->jenis_kelamin,
                            'email' => $s->email,
                            'status' => $s->status,
                        ];
                    }),
                ];
            });

            return response()->json([
                'success' => true,
                'message' => 'Data semua kelas berhasil diambil',
                'data' => $kelasData,
            ], 200);

        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    /**
     * Get kelas siswa sendiri dengan jadwal hari ini
     * GET /api/siswa/kelas-saya
     */
    public function getKelasSaya(Request $request)
    {
        try {
            // Get siswa dari user yang login
            $user = $request->user();
            $siswa = Siswa::where('user_id', $user->id)->first();

            if (!$siswa) {
                return response()->json([
                    'success' => false,
                    'message' => 'Data siswa tidak ditemukan',
                ], 404);
            }

            $kelas = Kelas::with(['siswas', 'guru'])
                ->withCount('siswas')
                ->find($siswa->kelas_id);

            if (!$kelas) {
                return response()->json([
                    'success' => false,
                    'message' => 'Data kelas tidak ditemukan',
                ], 404);
            }

            // Get jadwal hari ini
            $hariMapping = [
                'Monday' => 'Senin',
                'Tuesday' => 'Selasa',
                'Wednesday' => 'Rabu',
                'Thursday' => 'Kamis',
                'Friday' => 'Jumat',
                'Saturday' => 'Sabtu',
            ];
            $hariInggris = now()->format('l');
            $hari = $hariMapping[$hariInggris] ?? 'Senin';

            $jadwalHariIni = JadwalKelas::with(['guru', 'mapel'])
                ->where('kelas_id', $kelas->id)
                ->where('hari', $hari)
                ->aktif()
                ->orderBy('jam_mulai', 'asc')
                ->get();

            return response()->json([
                'success' => true,
                'message' => 'Data kelas Anda berhasil diambil',
                'data' => [
                    'kelas' => [
                        'id' => $kelas->id,
                        'kode_kelas' => $kelas->kode_kelas,
                        'nama_kelas' => $kelas->nama_kelas,
                        'tingkat' => $kelas->tingkat,
                        'jurusan' => $kelas->jurusan,
                        'wali_kelas' => $kelas->guru ? $kelas->guru->nama : null,
                        'jumlah_siswa' => $kelas->siswas_count,
                    ],
                    'jadwal_hari_ini' => $jadwalHariIni,
                    'hari' => $hari,
                ],
            ], 200);

        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    /**
     * Get jadwal kelas berdasarkan hari (untuk Tab Jadwal di Android)
     * GET /api/siswa/jadwal?hari=Senin&kelas_id=1
     */
    public function getJadwalByHari(Request $request)
    {
        try {
            // Mapping hari bahasa Inggris ke Indonesia
            $hariMapping = [
                'Monday' => 'Senin',
                'Tuesday' => 'Selasa',
                'Wednesday' => 'Rabu',
                'Thursday' => 'Kamis',
                'Friday' => 'Jumat',
                'Saturday' => 'Sabtu',
                'Sunday' => 'Minggu',
            ];

            // Gunakan hari dari request atau hari ini
            $hari = $request->input('hari');
            if (!$hari) {
                $hariInggris = now()->format('l');
                $hari = $hariMapping[$hariInggris] ?? 'Senin';
            }

            // Gunakan kelas_id dari request (wajib) atau default ke kelas pertama
            $kelasId = $request->input('kelas_id');
            if (!$kelasId) {
                // Coba ambil dari data siswa yang login
                $user = $request->user();
                $siswa = Siswa::where('user_id', $user->id)->first();
                $kelasId = $siswa ? $siswa->kelas_id : Kelas::first()?->id;
            }

            // Get jadwal berdasarkan hari dan kelas
            $jadwal = JadwalKelas::with(['guru', 'mapel', 'kelas'])
                ->where('kelas_id', $kelasId)
                ->where('hari', $hari)
                ->aktif()
                ->orderBy('jam_mulai', 'asc')
                ->get();

            // Tanggal hari ini untuk cek status kehadiran
            $tanggalHariIni = now()->format('Y-m-d');

            // Format jadwal untuk Android
            $jadwalFormatted = $jadwal->map(function($j) use ($tanggalHariIni) {
                // Format jam ke HH:mm
                $jamMulai = $j->jam_mulai;
                $jamSelesai = $j->jam_selesai;
                
                // Jika jam_mulai adalah datetime/timestamp, format ke HH:mm
                if ($j->jam_mulai instanceof \Carbon\Carbon) {
                    $jamMulai = $j->jam_mulai->format('H:i');
                    $jamSelesai = $j->jam_selesai->format('H:i');
                } elseif (strlen($j->jam_mulai) > 5) {
                    // Jika string panjang (datetime), ambil hanya jam:menit
                    $jamMulai = substr($j->jam_mulai, 0, 5);
                    $jamSelesai = substr($j->jam_selesai, 0, 5);
                }
                
                // Cek status kehadiran hari ini
                $kehadiran = KehadiranGuru::where('jadwal_id', $j->id)
                    ->where('tanggal', $tanggalHariIni)
                    ->first();
                
                return [
                    'id' => $j->id,
                    'jadwal_id' => $j->id,
                    'kelas_id' => $j->kelas_id,
                    'kelas' => $j->kelas ? $j->kelas->nama_kelas : null,
                    'guru_id' => $j->guru_id,
                    'guru' => $j->guru ? $j->guru->guru : null,
                    'nama_guru' => $j->guru ? $j->guru->guru : null,
                    'mapel_id' => $j->mapel_id,
                    'mapel' => $j->mapel ? $j->mapel->mapel : null,
                    'nama_mapel' => $j->mapel ? $j->mapel->mapel : null,
                    'hari' => $j->hari,
                    'jam_mulai' => $jamMulai,
                    'jam_selesai' => $jamSelesai,
                    'jam' => $jamMulai . ' - ' . $jamSelesai,
                    // Status kehadiran
                    'kehadiran_id' => $kehadiran ? $kehadiran->id : null,
                    'status_kehadiran' => $kehadiran ? $kehadiran->status : 'belum_diisi',
                    'sudah_diisi' => $kehadiran ? true : false,
                ];
            });

            // Get nama kelas
            $kelas = Kelas::find($kelasId);

            return response()->json([
                'success' => true,
                'message' => 'Data jadwal berhasil diambil',
                'hari' => $hari,
                'kelas_id' => (int) $kelasId,
                'kelas' => $kelas ? $kelas->nama_kelas : null,
                'total' => $jadwalFormatted->count(),
                'data' => $jadwalFormatted,  // data langsung berisi array jadwal
            ], 200);

        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    /**
     * Get list kehadiran guru hari ini (untuk Tab List di Android)
     * GET /api/siswa/list-kehadiran?tanggal=2025-01-08
     */
    public function getListKehadiran(Request $request)
    {
        try {
            // Gunakan kelas_id dari request atau ambil dari data siswa yang login
            $kelasId = $request->input('kelas_id');
            if (!$kelasId) {
                $user = $request->user();
                $siswa = Siswa::where('user_id', $user->id)->first();
                $kelasId = $siswa ? $siswa->kelas_id : Kelas::first()?->id;
            }

            if (!$kelasId) {
                return response()->json([
                    'success' => false,
                    'message' => 'Kelas tidak ditemukan',
                ], 404);
            }

            $tanggal = $request->input('tanggal', now()->format('Y-m-d'));

            // Get kehadiran guru di kelas hari ini
            $kehadiran = KehadiranGuru::with(['guru', 'mapel', 'kelas', 'guruPengganti'])
                ->where('kelas_id', $kelasId)
                ->where('tanggal', $tanggal)
                ->orderBy('created_at', 'desc')
                ->get();

            // Hitung statistik
            $hadir = $kehadiran->where('status', 'hadir')->count();
            $tidakHadir = $kehadiran->where('status', 'tidak_hadir')->count();
            $izin = $kehadiran->where('status', 'izin')->count();
            $sakit = $kehadiran->where('status', 'sakit')->count();

            // Format data kehadiran
            $kehadiranFormatted = $kehadiran->map(function($k) {
                // Prefer active guru pengganti, otherwise the latest pending request
                $requestPengganti = GuruPengganti::with('guruPengganti')
                    ->where('kehadiran_guru_id', $k->id)
                    ->orderByRaw("(CASE WHEN status = 'aktif' THEN 1 ELSE 0 END) DESC")
                    ->orderBy('created_at', 'desc')
                    ->first();
                
                return [
                    'id' => $k->id,
                    'kehadiran_id' => $k->id,
                    'jadwal_id' => $k->jadwal_id,
                    'guru_id' => $k->guru_id,
                    'guru' => $k->guru ? $k->guru->guru : null,
                    'kode_guru' => $k->guru ? $k->guru->kode_guru : null,
                    'nama_guru' => $k->guru ? $k->guru->guru : null,
                    'mapel_id' => $k->mapel_id,
                    'mapel' => $k->mapel ? $k->mapel->mapel : null,
                    'nama_mapel' => $k->mapel ? $k->mapel->mapel : null,
                    'kelas_id' => $k->kelas_id,
                    'kelas' => $k->kelas ? $k->kelas->nama_kelas : null,
                    'tanggal' => $k->tanggal ? $k->tanggal->format('Y-m-d') : null,
                    'status' => $k->status,
                    'keterangan' => $k->keterangan,
                    // Request guru pengganti info
                    'has_request_pengganti' => $requestPengganti ? true : false,
                    'request_pengganti' => $requestPengganti ? [
                        'id' => $requestPengganti->id,
                        'guru_pengganti_id' => $requestPengganti->guru_pengganti_id,
                        'guru_pengganti' => $requestPengganti->guruPengganti ? ($requestPengganti->guruPengganti->guru ?? $requestPengganti->guruPengganti->nama) : null,
                        'kode_guru' => $requestPengganti->guruPengganti?->kode_guru,
                        'status' => $requestPengganti->status, // pending, aktif, ditolak
                    ] : null,
                    // If replacement is active, surface replacement teacher as the display teacher for students
                    'display_guru' => ($requestPengganti && $requestPengganti->status == 'aktif') ? ($requestPengganti->guruPengganti?->guru ?? $requestPengganti->guruPengganti?->nama) : ($k->guru?->guru ?? null),
                    'display_kode_guru' => ($requestPengganti && $requestPengganti->status == 'aktif') ? ($requestPengganti->guruPengganti?->kode_guru) : ($k->guru?->kode_guru ?? null),
                    'created_at' => $k->created_at,
                ];
            });

            return response()->json([
                'success' => true,
                'message' => 'Data kehadiran guru berhasil diambil',
                'tanggal' => $tanggal,
                'kelas_id' => (int) $kelasId,
                'statistik' => [
                    'total' => $kehadiran->count(),
                    'hadir' => $hadir,
                    'tidak_hadir' => $tidakHadir,
                    'izin' => $izin,
                    'sakit' => $sakit,
                ],
                'data' => $kehadiranFormatted,  // data langsung berisi array kehadiran
            ], 200);

        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    /**
     * Get daftar semua guru (untuk dropdown pemilihan guru pengganti)
     * GET /api/siswa/list-guru
     */
    public function getListGuru(Request $request)
    {
        try {
            $guru = Guru::orderByRaw("COALESCE(guru, nama) ASC")
                ->get(['id', 'kode_guru', 'guru', 'nama', 'telepon']);

            // Format untuk kompatibilitas Android
            $guruFormatted = $guru->map(function($g) {
                return [
                    'id' => $g->id,
                    'kode_guru' => $g->kode_guru,
                    'nama' => $g->guru ?? $g->nama,
                    'guru' => $g->guru ?? $g->nama,
                    'telepon' => $g->telepon,
                ];
            });

            return response()->json([
                'success' => true,
                'message' => 'Data guru berhasil diambil',
                'data' => $guruFormatted,
                'total' => $guruFormatted->count(),
            ], 200);

        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    /**
     * Get info profil siswa yang login
     * GET /api/siswa/profil
     */
    public function getProfil(Request $request)
    {
        try {
            $user = $request->user();
            $siswa = Siswa::with(['kelas'])->where('user_id', $user->id)->first();

            if (!$siswa) {
                return response()->json([
                    'success' => false,
                    'message' => 'Data siswa tidak ditemukan',
                ], 404);
            }

            return response()->json([
                'success' => true,
                'message' => 'Data profil berhasil diambil',
                'data' => [
                    'id' => $siswa->id,
                    'user_id' => $user->id,
                    'nis' => $siswa->nis,
                    'nama' => $siswa->nama,
                    'email' => $siswa->email ?? $user->email,
                    'jenis_kelamin' => $siswa->jenis_kelamin,
                    'alamat' => $siswa->alamat,
                    'telepon' => $siswa->telepon,
                    'status' => $siswa->status,
                    'kelas' => $siswa->kelas ? [
                        'id' => $siswa->kelas->id,
                        'nama_kelas' => $siswa->kelas->nama_kelas,
                        'tingkat' => $siswa->kelas->tingkat,
                        'jurusan' => $siswa->kelas->jurusan,
                    ] : null,
                    'username' => $user->name,
                    'role' => $user->role,
                ],
            ], 200);

        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    /**
     * Get daftar semua hari untuk filter jadwal
     * GET /api/siswa/list-hari
     */
    public function getListHari()
    {
        return response()->json([
            'success' => true,
            'message' => 'Daftar hari berhasil diambil',
            'data' => [
                ['id' => 1, 'nama' => 'Senin'],
                ['id' => 2, 'nama' => 'Selasa'],
                ['id' => 3, 'nama' => 'Rabu'],
                ['id' => 4, 'nama' => 'Kamis'],
                ['id' => 5, 'nama' => 'Jumat'],
                ['id' => 6, 'nama' => 'Sabtu'],
            ],
        ], 200);
    }

    /**
     * Get daftar semua kelas untuk filter
     * GET /api/siswa/list-kelas
     */
    public function getListKelas()
    {
        try {
            $kelas = Kelas::orderBy('nama_kelas', 'asc')
                ->get(['id', 'kode_kelas', 'nama_kelas', 'tingkat', 'jurusan']);

            return response()->json([
                'success' => true,
                'message' => 'Data kelas berhasil diambil',
                'data' => $kelas,
                'total' => $kelas->count(),
            ], 200);

        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    /**
     * Tambah jadwal kelas baru
     * POST /api/siswa/jadwal
     */
    public function tambahJadwal(Request $request)
    {
        try {
            $validated = $request->validate([
                'kelas_id' => 'required|exists:kelas,id',
                'mapel_id' => 'required|exists:mapels,id',
                'guru_id' => 'required|exists:gurus,id',
                'hari' => 'required|in:Senin,Selasa,Rabu,Kamis,Jumat,Sabtu',
                'jam_mulai' => 'required|date_format:H:i',
                'jam_selesai' => 'required|date_format:H:i|after:jam_mulai',
            ]);

            // Get tahun ajaran aktif (dipakai untuk cek konflik jadwal)
            $tahunAjaranAktif = \App\Models\TahunAjaran::where('flag', true)->first();

            // Normalisasi waktu ke format H:i:s agar perbandingan konsisten dengan kolom time
            $jamMulai = \Carbon\Carbon::createFromFormat('H:i', $validated['jam_mulai'])->format('H:i:s');
            $jamSelesai = \Carbon\Carbon::createFromFormat('H:i', $validated['jam_selesai'])->format('H:i:s');

            // Cek apakah jadwal sudah ada di jam yang sama untuk tahun ajaran yang sama
            // Overlap detection: existing.jam_mulai < new.jam_selesai AND existing.jam_selesai > new.jam_mulai
            // Use strict comparisons so adjacent schedules (end == start) are not considered overlap
            $existingJadwalsQuery = JadwalKelas::where('kelas_id', $validated['kelas_id'])
                ->when($tahunAjaranAktif, function($q) use ($tahunAjaranAktif) { $q->where('tahun_ajaran_id', $tahunAjaranAktif->id); })
                ->when(!$tahunAjaranAktif, function($q) { $q->whereNull('tahun_ajaran_id'); })
                ->where('hari', $validated['hari'])
                ->where('jam_mulai', '<', $jamSelesai)
                ->where('jam_selesai', '>', $jamMulai);

            $existingJadwals = $existingJadwalsQuery->get();
            $replaced = [];

            if (!$existingJadwals->isEmpty()) {
                // if client asks to force and user is admin/kurikulum, delete conflicting jadwals and proceed
                $force = (bool) ($request->input('force') ?? false);
                $user = $request->user();
                $canForce = $user && in_array($user->role, ['admin', 'kurikulum']);

                if ($force && $canForce) {
                        $replaced = [];
                        $cannotDelete = [];
                    foreach ($existingJadwals as $ej) {
                            // Do not delete jadwal which already has kehadiran data (prevent accidental data loss)
                            if ($ej->kehadiranGuru()->exists()) {
                                $cannotDelete[] = [
                                    'jadwal_id' => $ej->id,
                                    'jam' => $ej->jam_mulai . ' - ' . $ej->jam_selesai,
                                    'kehadiran_count' => $ej->kehadiranGuru()->count(),
                                ];
                                continue;
                            }
                        $replaced[] = [
                            'jadwal_id' => $ej->id,
                            'mapel' => $ej->mapel ? $ej->mapel->mapel : null,
                            'guru' => $ej->guru ? $ej->guru->guru : null,
                            'tahun_ajaran_id' => $ej->tahun_ajaran_id,
                            'jam' => $ej->jam_mulai . ' - ' . $ej->jam_selesai,
                        ];
                        $ej->delete();
                    }
                        if (!empty($cannotDelete)) {
                            return response()->json([
                                'success' => false,
                                'message' => 'Beberapa jadwal konflik memiliki data kehadiran dan tidak dapat dihapus',
                                'cannot_delete' => $cannotDelete,
                            ], 422);
                        }
                    // continue to create new jadwal below
                    // we also log replaced jadwal in response after creation
                } else {
                    // return detailed list of conflicts
                    $conflicts = $existingJadwals->map(function($ej) {
                        return [
                            'jadwal_id' => $ej->id,
                            'mapel' => $ej->mapel ? $ej->mapel->mapel : null,
                            'guru' => $ej->guru ? $ej->guru->guru : null,
                            'tahun_ajaran_id' => $ej->tahun_ajaran_id,
                            'jam' => $ej->jam_mulai . ' - ' . $ej->jam_selesai,
                        ];
                    })->values();

                    // Build suggestions: find free slots between day start/end that fit requested duration
                    $existingSorted = $existingJadwalsQuery->orderBy('jam_mulai')->get();
                    $requestedDuration = \Carbon\Carbon::createFromFormat('H:i:s', $jamSelesai)->diffInMinutes(\Carbon\Carbon::createFromFormat('H:i:s', $jamMulai));
                    $suggestions = [];
                    $dayStart = \Carbon\Carbon::createFromTimeString('06:00:00');
                    $dayEnd = \Carbon\Carbon::createFromTimeString('18:00:00');
                    $prevEnd = $dayStart->copy();
                    foreach ($existingSorted as $exs) {
                        $startEx = \Carbon\Carbon::createFromTimeString($exs->jam_mulai);
                        $gapMinutes = $startEx->diffInMinutes($prevEnd);
                        if ($gapMinutes >= $requestedDuration) {
                            $slotStart = $prevEnd->format('H:i');
                            $slotEnd = $prevEnd->copy()->addMinutes($requestedDuration)->format('H:i');
                            $suggestions[] = ['start' => $slotStart, 'end' => $slotEnd];
                        }
                        $prevEnd = \Carbon\Carbon::createFromTimeString($exs->jam_selesai);
                    }
                    $gapMinutes = $dayEnd->diffInMinutes($prevEnd);
                    if ($gapMinutes >= $requestedDuration) {
                        $slotStart = $prevEnd->format('H:i');
                        $slotEnd = $prevEnd->copy()->addMinutes($requestedDuration)->format('H:i');
                        $suggestions[] = ['start' => $slotStart, 'end' => $slotEnd];
                    }

                    return response()->json([
                        'success' => false,
                        'message' => 'Sudah ada jadwal di waktu tersebut',
                        'conflicts' => $conflicts,
                        'suggestions' => $suggestions,
                    ], 422);
                }
            }

            // (tahun ajaran aktif sudah diambil di atas)

            // Create jadwal baru
            $jadwal = JadwalKelas::create([
                'kelas_id' => $validated['kelas_id'],
                'mapel_id' => $validated['mapel_id'],
                'guru_id' => $validated['guru_id'],
                'hari' => $validated['hari'],
                'jam_mulai' => $validated['jam_mulai'],
                'jam_selesai' => $validated['jam_selesai'],
                'tahun_ajaran_id' => $tahunAjaranAktif ? $tahunAjaranAktif->id : null,
            ]);

            // Load relations
            $jadwal->load(['kelas', 'mapel', 'guru']);

            return response()->json([
                'success' => true,
                'message' => 'Jadwal berhasil ditambahkan',
                'data' => [
                    'id' => $jadwal->id,
                    'jadwal_id' => $jadwal->id,
                    'kelas_id' => $jadwal->kelas_id,
                    'kelas' => $jadwal->kelas ? $jadwal->kelas->nama_kelas : null,
                    'mapel_id' => $jadwal->mapel_id,
                    'mapel' => $jadwal->mapel ? $jadwal->mapel->mapel : null,
                    'guru_id' => $jadwal->guru_id,
                    'kode_guru' => $jadwal->guru?->kode_guru ?? null,
                    'guru' => $jadwal->guru ? $jadwal->guru->guru : null,
                    'hari' => $jadwal->hari,
                    'jam_mulai' => $jadwal->jam_mulai,
                    'jam_selesai' => $jadwal->jam_selesai,
                    'jam' => $jadwal->jam_mulai . ' - ' . $jadwal->jam_selesai,
                ],
                'replaced' => $replaced,
            ], 201);

        } catch (ValidationException $e) {
            return response()->json([
                'success' => false,
                'message' => 'Validasi gagal',
                'errors' => $e->errors(),
            ], 422);
        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan: ' . $e->getMessage(),
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    /**
     * Hapus jadwal kelas
     * DELETE /api/siswa/jadwal/{id}
     */
    public function hapusJadwal($id)
    {
        try {
            $jadwal = JadwalKelas::find($id);

            if (!$jadwal) {
                return response()->json([
                    'success' => false,
                    'message' => 'Jadwal tidak ditemukan',
                ], 404);
            }

            // Cek apakah jadwal sudah ada kehadiran hari ini
            $kehadiranHariIni = KehadiranGuru::where('jadwal_id', $id)
                ->where('tanggal', now()->format('Y-m-d'))
                ->first();

            if ($kehadiranHariIni) {
                return response()->json([
                    'success' => false,
                    'message' => 'Tidak dapat menghapus jadwal yang sudah memiliki data kehadiran hari ini',
                ], 422);
            }

            $jadwal->delete();

            return response()->json([
                'success' => true,
                'message' => 'Jadwal berhasil dihapus',
            ], 200);

        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan: ' . $e->getMessage(),
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    /**
     * Get daftar mapel untuk dropdown
     * GET /api/siswa/list-mapel
     */
    public function getListMapel()
    {
        try {
            $mapels = \App\Models\Mapel::orderBy('mapel', 'asc')
                ->get(['id', 'kode_mapel', 'mapel']);

            // Format untuk kompatibilitas
            $mapelFormatted = $mapels->map(function($m) {
                return [
                    'id' => $m->id,
                    'kode_mapel' => $m->kode_mapel,
                    'nama' => $m->mapel,
                    'mapel' => $m->mapel,
                ];
            });

            return response()->json([
                'success' => true,
                'message' => 'Data mapel berhasil diambil',
                'data' => $mapelFormatted,
                'total' => $mapelFormatted->count(),
            ], 200);

        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage(),
            ], 500);
        }
    }
}
