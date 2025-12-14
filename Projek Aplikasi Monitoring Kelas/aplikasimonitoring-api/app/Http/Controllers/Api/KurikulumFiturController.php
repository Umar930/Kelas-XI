<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\KehadiranGuru;
use App\Models\GuruPengganti;
use App\Models\NotifikasiSiswa;
use App\Models\Kelas;
use App\Models\Guru;
use App\Models\JadwalKelas;
use Illuminate\Http\Request;
use Illuminate\Validation\ValidationException;

class KurikulumFiturController extends Controller
{
    /**
     * 1. Filter semua kelas dengan status kehadiran guru
     * GET /api/kurikulum/kelas-kehadiran
     */
    public function getKelasWithKehadiran(Request $request)
    {
        try {
            $tanggal = $request->input('tanggal', now()->toDateString());
            $kelas_id = $request->input('kelas_id');

            $query = Kelas::with(['siswas', 'guru']);

            if ($kelas_id) {
                $query->where('id', $kelas_id);
            }

            $kelas = $query->orderBy('nama_kelas', 'asc')->get();

            // Get kehadiran guru untuk tanggal tersebut
            $kelasData = $kelas->map(function($k) use ($tanggal) {
                // Get jadwal kelas hari ini
                $hariMapping = [
                    'Monday' => 'Senin',
                    'Tuesday' => 'Selasa',
                    'Wednesday' => 'Rabu',
                    'Thursday' => 'Kamis',
                    'Friday' => 'Jumat',
                    'Saturday' => 'Sabtu',
                ];
                $hariInggris = date('l', strtotime($tanggal));
                $hari = $hariMapping[$hariInggris] ?? 'Senin';

                $jadwal = JadwalKelas::with(['guru', 'mapel'])
                    ->where('kelas_id', $k->id)
                    ->where('hari', $hari)
                    ->aktif()
                    ->orderBy('jam_mulai', 'asc')
                    ->get();

                // Get kehadiran guru untuk jadwal tersebut
                $jadwalWithKehadiran = $jadwal->map(function($j) use ($tanggal) {
                    $kehadiran = KehadiranGuru::where('jadwal_id', $j->id)
                        ->where('tanggal', $tanggal)
                        ->first();

                    return [
                        'jadwal_id' => $j->id,
                        'jam_mulai' => $j->jam_mulai,
                        'jam_selesai' => $j->jam_selesai,
                        'guru' => [
                            'id' => $j->guru->id,
                            'nama' => $j->guru->nama,
                            'kode_guru' => $j->guru->kode_guru,
                        ],
                        'mapel' => [
                            'id' => $j->mapel->id,
                            'nama' => $j->mapel->mapel,
                            'kode_mapel' => $j->mapel->kode_mapel,
                        ],
                        'kehadiran' => $kehadiran ? [
                            'id' => $kehadiran->id,
                            'status' => $kehadiran->status,
                            'keterangan' => $kehadiran->keterangan,
                            'input_by' => $kehadiran->input_by_siswa_id ? 'siswa' : 'kurikulum',
                        ] : null,
                    ];
                });

                return [
                    'id' => $k->id,
                    'kode_kelas' => $k->kode_kelas,
                    'nama_kelas' => $k->nama_kelas,
                    'tingkat' => $k->tingkat,
                    'jurusan' => $k->jurusan,
                    'wali_kelas' => $k->guru ? $k->guru->nama : null,
                    'jumlah_siswa' => $k->siswas->count(),
                    'siswa' => $k->siswas->map(function($s) {
                        return [
                            'id' => $s->id,
                            'nis' => $s->nis,
                            'nama' => $s->nama,
                            'jenis_kelamin' => $s->jenis_kelamin,
                            'email' => $s->email,
                        ];
                    }),
                    'jadwal_hari_ini' => $jadwalWithKehadiran,
                    'hari' => $hari,
                ];
            });

            return response()->json([
                'success' => true,
                'message' => 'Data kelas dengan kehadiran guru berhasil diambil',
                'data' => [
                    'tanggal' => $tanggal,
                    'kelas' => $kelasData,
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
     * 2. Input izin/sakit guru (akan mengirim notifikasi ke siswa)
     * POST /api/kurikulum/guru-izin-sakit
     */
    public function inputGuruIzinSakit(Request $request)
    {
        try {
            $validated = $request->validate([
                'guru_id' => 'required|exists:gurus,id',
                'tanggal' => 'required|date',
                'status' => 'required|in:izin,sakit',
                'keterangan' => 'required|string',
                'jadwal_ids' => 'required|array', // Array of jadwal_id yang terdampak
                'jadwal_ids.*' => 'exists:jadwal_kelas,id',
            ]);

            $user = $request->user();
            $createdKehadiran = [];
            $createdNotifikasi = [];

            foreach ($validated['jadwal_ids'] as $jadwalId) {
                $jadwal = JadwalKelas::with(['kelas', 'mapel'])->find($jadwalId);

                // Create kehadiran guru record
                $kehadiran = KehadiranGuru::create([
                    'jadwal_id' => $jadwalId,
                    'guru_id' => $validated['guru_id'],
                    'kelas_id' => $jadwal->kelas_id,
                    'mapel_id' => $jadwal->mapel_id,
                    'tanggal' => $validated['tanggal'],
                    'status' => $validated['status'],
                    'keterangan' => $validated['keterangan'],
                    'input_by_kurikulum_id' => $user->id,
                ]);

                $createdKehadiran[] = $kehadiran;

                // Create notifikasi untuk siswa di kelas tersebut
                $guru = Guru::find($validated['guru_id']);
                $pesan = "Guru {$guru->nama} ({$jadwal->mapel->mapel}) {$validated['status']} pada {$validated['tanggal']}. Keterangan: {$validated['keterangan']}";

                $notifikasi = NotifikasiSiswa::create([
                    'kelas_id' => $jadwal->kelas_id,
                    'guru_id' => $validated['guru_id'],
                    'mapel_id' => $jadwal->mapel_id,
                    'tanggal' => $validated['tanggal'],
                    'tipe' => $validated['status'],
                    'pesan' => $pesan,
                    'created_by_kurikulum_id' => $user->id,
                ]);

                $createdNotifikasi[] = $notifikasi;
            }

            return response()->json([
                'success' => true,
                'message' => "Data guru {$validated['status']} berhasil diinput dan notifikasi dikirim ke siswa",
                'data' => [
                    'kehadiran' => $createdKehadiran,
                    'notifikasi_terkirim' => count($createdNotifikasi),
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
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    /**
     * 3. Assign guru pengganti untuk kelas yang gurunya tidak hadir
     * POST /api/kurikulum/assign-guru-pengganti
     */
    public function assignGuruPengganti(Request $request)
    {
        try {
            $validated = $request->validate([
                'kehadiran_guru_id' => 'required|exists:kehadiran_gurus,id',
                'guru_pengganti_id' => 'required|exists:gurus,id',
                'catatan' => 'nullable|string',
            ]);

            $user = $request->user();

            // Get kehadiran guru data
            $kehadiran = KehadiranGuru::with(['jadwal', 'kelas', 'mapel', 'guru'])->find($validated['kehadiran_guru_id']);

            // Validasi guru harus tidak hadir
            if (!in_array($kehadiran->status, ['tidak_hadir', 'izin', 'sakit'])) {
                return response()->json([
                    'success' => false,
                    'message' => 'Guru pengganti hanya bisa diassign untuk guru yang tidak hadir/izin/sakit',
                ], 422);
            }

            // Cek apakah guru pengganti sudah mengajar di jam yang sama
            $jadwal = $kehadiran->jadwal;
            $bentrokJadwal = JadwalKelas::where('guru_id', $validated['guru_pengganti_id'])
                ->where('hari', $jadwal->hari)
                ->where(function($q) use ($jadwal) {
                    $q->whereBetween('jam_mulai', [$jadwal->jam_mulai, $jadwal->jam_selesai])
                      ->orWhereBetween('jam_selesai', [$jadwal->jam_mulai, $jadwal->jam_selesai]);
                })
                ->aktif()
                ->exists();

            if ($bentrokJadwal) {
                return response()->json([
                    'success' => false,
                    'message' => 'Guru pengganti bentrok dengan jadwal mengajar lain di jam yang sama',
                ], 422);
            }

            // Create guru pengganti record
            $guruPengganti = GuruPengganti::create([
                'kehadiran_guru_id' => $validated['kehadiran_guru_id'],
                'guru_pengganti_id' => $validated['guru_pengganti_id'],
                'kelas_id' => $kehadiran->kelas_id,
                'mapel_id' => $kehadiran->mapel_id,
                'tanggal' => $kehadiran->tanggal,
                'jam_mulai' => $jadwal->jam_mulai,
                'jam_selesai' => $jadwal->jam_selesai,
                'assigned_by_kurikulum_id' => $user->id,
                'status' => 'aktif',
                'catatan' => $validated['catatan'],
            ]);

            // Create notifikasi untuk siswa
            $guruPenggantiData = Guru::find($validated['guru_pengganti_id']);
            $pesan = "Guru pengganti untuk {$kehadiran->mapel->mapel}: {$guruPenggantiData->nama}. Jadwal: {$jadwal->jam_mulai} - {$jadwal->jam_selesai}";

            $notifikasi = NotifikasiSiswa::create([
                'kelas_id' => $kehadiran->kelas_id,
                'guru_id' => $kehadiran->guru_id,
                'mapel_id' => $kehadiran->mapel_id,
                'tanggal' => $kehadiran->tanggal,
                'tipe' => 'guru_pengganti',
                'pesan' => $pesan,
                'guru_pengganti_id' => $validated['guru_pengganti_id'],
                'created_by_kurikulum_id' => $user->id,
            ]);

            return response()->json([
                'success' => true,
                'message' => 'Guru pengganti berhasil diassign dan notifikasi dikirim ke siswa',
                'data' => [
                    'guru_pengganti' => $guruPengganti->load(['guruPengganti', 'kelas', 'mapel']),
                    'notifikasi' => $notifikasi,
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
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    /**
     * Get guru yang tersedia untuk pengganti (tidak mengajar di jam tersebut)
     * GET /api/kurikulum/guru-tersedia
     */
    public function getGuruTersedia(Request $request)
    {
        try {
            $validated = $request->validate([
                'hari' => 'required|in:Senin,Selasa,Rabu,Kamis,Jumat,Sabtu',
                'jam_mulai' => 'required|date_format:H:i',
                'jam_selesai' => 'required|date_format:H:i',
                'tanggal' => 'required|date',
            ]);

            // Get semua guru
            $semuaGuru = Guru::all();

            // Get guru yang mengajar di jam tersebut
            $guruMengajar = JadwalKelas::where('hari', $validated['hari'])
                ->where(function($q) use ($validated) {
                    $q->whereBetween('jam_mulai', [$validated['jam_mulai'], $validated['jam_selesai']])
                      ->orWhereBetween('jam_selesai', [$validated['jam_mulai'], $validated['jam_selesai']])
                      ->orWhere(function($q2) use ($validated) {
                          $q2->where('jam_mulai', '<=', $validated['jam_mulai'])
                             ->where('jam_selesai', '>=', $validated['jam_selesai']);
                      });
                })
                ->aktif()
                ->pluck('guru_id')
                ->toArray();

            // Get guru yang sudah diassign sebagai pengganti di tanggal tersebut
            $guruPengganti = GuruPengganti::where('tanggal', $validated['tanggal'])
                ->where(function($q) use ($validated) {
                    $q->whereBetween('jam_mulai', [$validated['jam_mulai'], $validated['jam_selesai']])
                      ->orWhereBetween('jam_selesai', [$validated['jam_mulai'], $validated['jam_selesai']]);
                })
                ->pluck('guru_pengganti_id')
                ->toArray();

            // Guru yang tidak tersedia = gabungan guruMengajar dan guruPengganti
            $guruTidakTersedia = array_unique(array_merge($guruMengajar, $guruPengganti));

            // Filter guru yang tersedia
            $guruTersedia = $semuaGuru->whereNotIn('id', $guruTidakTersedia)->values();

            return response()->json([
                'success' => true,
                'message' => 'Data guru tersedia berhasil diambil',
                'data' => [
                    'guru_tersedia' => $guruTersedia,
                    'jumlah_tersedia' => $guruTersedia->count(),
                    'hari' => $validated['hari'],
                    'jam' => $validated['jam_mulai'] . ' - ' . $validated['jam_selesai'],
                ],
            ], 200);

        } catch (ValidationException $e) {
            return response()->json([
                'success' => false,
                'message' => 'Validasi gagal',
                'errors' => $e->errors(),
            ], 422);
        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    // ============================================
    // FITUR BARU UNTUK TAB KURIKULUM
    // ============================================

    /**
     * TAB 1: GURU PENGGANTI
     * Daftar request guru pengganti dari siswa yang perlu dipilihkan guru
     * GET /api/kurikulum/request-pengganti
     */
    public function getRequestPengganti(Request $request)
    {
        try {
            $status = $request->input('status', 'pending'); // pending, aktif, ditolak, all
            $tanggal = $request->input('tanggal');
            $kelas_id = $request->input('kelas_id');

            $query = GuruPengganti::with([
                'kehadiranGuru.guru',
                'kehadiranGuru.jadwal.mapel',
                'kehadiranGuru.jadwal.kelas',
                'guruPengganti',
                'requestedBySiswa.kelas'
            ]);

            // Filter by status
            if ($status !== 'all') {
                $query->where('status', $status);
            }

            // Filter by tanggal
            if ($tanggal) {
                $query->whereDate('tanggal', $tanggal);
            }

            // Filter by kelas
            if ($kelas_id) {
                $query->whereHas('kehadiranGuru.jadwal', function($q) use ($kelas_id) {
                    $q->where('kelas_id', $kelas_id);
                });
            }

            $requests = $query->orderBy('created_at', 'desc')->get();

            $data = $requests->map(function($req) {
                $kehadiran = $req->kehadiranGuru;
                $jadwal = $kehadiran ? $kehadiran->jadwal : null;
                
                return [
                    'id' => $req->id,
                    'kehadiran_guru_id' => $req->kehadiran_guru_id,
                    'tanggal' => $req->tanggal,
                    'status' => $req->status,
                    'keterangan' => $req->keterangan,
                    'created_at' => $req->created_at->format('Y-m-d H:i:s'),
                    
                    // Info guru yang tidak hadir
                    'guru_tidak_hadir' => $kehadiran && $kehadiran->guru ? [
                        'id' => $kehadiran->guru->id,
                        'nama' => $kehadiran->guru->guru ?? $kehadiran->guru->nama,
                        'status_kehadiran' => $kehadiran->status,
                    ] : null,
                    
                    // Info jadwal
                    'jadwal' => $jadwal ? [
                        'id' => $jadwal->id,
                        'hari' => $jadwal->hari,
                        'jam_mulai' => $jadwal->jam_mulai,
                        'jam_selesai' => $jadwal->jam_selesai,
                        'mapel' => $jadwal->mapel ? $jadwal->mapel->mapel : null,
                        'kelas' => $jadwal->kelas ? $jadwal->kelas->nama_kelas : null,
                        'kelas_id' => $jadwal->kelas_id,
                    ] : null,
                    
                    // Guru pengganti yang dipilih (jika sudah)
                    'guru_pengganti' => $req->guruPengganti ? [
                        'id' => $req->guruPengganti->id,
                        'nama' => $req->guruPengganti->guru ?? $req->guruPengganti->nama,
                    ] : null,
                    
                    // Info siswa yang request
                    'requested_by' => $req->requestedBySiswa ? [
                        'id' => $req->requestedBySiswa->id,
                        'nama' => $req->requestedBySiswa->nama,
                        'kelas' => $req->requestedBySiswa->kelas ? $req->requestedBySiswa->kelas->nama_kelas : null,
                    ] : null,
                ];
            });

            return response()->json([
                'success' => true,
                'message' => 'Data request guru pengganti berhasil diambil',
                'data' => $data,
                'total' => $data->count(),
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
     * TAB 1: GURU PENGGANTI
     * Pilih/Assign guru pengganti untuk request dari siswa
     * POST /api/kurikulum/pilih-guru-pengganti
     */
    public function pilihGuruPengganti(Request $request)
    {
        try {
            $validated = $request->validate([
                'request_id' => 'required|exists:guru_penggantis,id',
                'guru_pengganti_id' => 'required|exists:gurus,id',
                'catatan' => 'nullable|string',
            ]);

            $user = $request->user();

            // Get request data
            $requestPengganti = GuruPengganti::with(['kehadiranGuru.jadwal'])->find($validated['request_id']);

            if ($requestPengganti->status !== 'pending') {
                return response()->json([
                    'success' => false,
                    'message' => 'Request ini sudah diproses sebelumnya',
                ], 422);
            }

            $jadwal = $requestPengganti->kehadiranGuru->jadwal;

            // Cek bentrok jadwal
            $bentrok = JadwalKelas::where('guru_id', $validated['guru_pengganti_id'])
                ->where('hari', $jadwal->hari)
                ->where(function($q) use ($jadwal) {
                    $q->whereBetween('jam_mulai', [$jadwal->jam_mulai, $jadwal->jam_selesai])
                      ->orWhereBetween('jam_selesai', [$jadwal->jam_mulai, $jadwal->jam_selesai]);
                })
                ->aktif()
                ->exists();

            if ($bentrok) {
                return response()->json([
                    'success' => false,
                    'message' => 'Guru pengganti bentrok dengan jadwal mengajar lain',
                ], 422);
            }

            // Update request
            $requestPengganti->update([
                'guru_pengganti_id' => $validated['guru_pengganti_id'],
                'status' => 'aktif',
                'keterangan' => $validated['catatan'] ?? $requestPengganti->keterangan,
                'approved_by' => $user->id,
                'approved_at' => now(),
            ]);

            // Kirim notifikasi ke siswa
            $guruPengganti = Guru::find($validated['guru_pengganti_id']);
            $pesan = "Kurikulum telah memilih guru pengganti: {$guruPengganti->guru} untuk mengajar di kelas Anda.";

            NotifikasiSiswa::create([
                'kelas_id' => $jadwal->kelas_id,
                'guru_id' => $requestPengganti->kehadiranGuru->guru_id,
                'mapel_id' => $jadwal->mapel_id,
                'tanggal' => $requestPengganti->tanggal,
                'tipe' => 'guru_pengganti',
                'pesan' => $pesan,
                'guru_pengganti_id' => $validated['guru_pengganti_id'],
                'created_by_kurikulum_id' => $user->id,
            ]);

            return response()->json([
                'success' => true,
                'message' => 'Guru pengganti berhasil dipilih',
                'data' => $requestPengganti->load(['guruPengganti', 'kehadiranGuru.jadwal.kelas', 'kehadiranGuru.jadwal.mapel']),
            ], 200);

        } catch (ValidationException $e) {
            return response()->json([
                'success' => false,
                'message' => 'Validasi gagal',
                'errors' => $e->errors(),
            ], 422);
        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    /**
     * TAB 1: GURU PENGGANTI
     * Tolak request guru pengganti dari siswa
     * POST /api/kurikulum/tolak-request-pengganti
     */
    public function tolakRequestPengganti(Request $request)
    {
        try {
            $validated = $request->validate([
                'request_id' => 'required|exists:guru_penggantis,id',
                'alasan' => 'nullable|string',
            ]);

            $user = $request->user();
            $requestPengganti = GuruPengganti::find($validated['request_id']);

            if ($requestPengganti->status !== 'pending') {
                return response()->json([
                    'success' => false,
                    'message' => 'Request ini sudah diproses sebelumnya',
                ], 422);
            }

            $requestPengganti->update([
                'status' => 'ditolak',
                'keterangan' => $validated['alasan'] ?? 'Ditolak oleh kurikulum',
                'approved_by' => $user->id,
                'approved_at' => now(),
            ]);

            return response()->json([
                'success' => true,
                'message' => 'Request guru pengganti ditolak',
            ], 200);

        } catch (ValidationException $e) {
            return response()->json([
                'success' => false,
                'message' => 'Validasi gagal',
                'errors' => $e->errors(),
            ], 422);
        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    /**
     * TAB 2: DAFTAR GURU
     * Daftar guru dengan status kehadiran berdasarkan hari dan kelas
     * GET /api/kurikulum/daftar-guru
     */
    public function getDaftarGuru(Request $request)
    {
        try {
            $hari = $request->input('hari');
            $kelas_id = $request->input('kelas_id');
            $tanggal = $request->input('tanggal', now()->toDateString());

            // Jika hari tidak diberikan, ambil dari tanggal
            if (!$hari) {
                $hariMapping = [
                    'Monday' => 'Senin',
                    'Tuesday' => 'Selasa',
                    'Wednesday' => 'Rabu',
                    'Thursday' => 'Kamis',
                    'Friday' => 'Jumat',
                    'Saturday' => 'Sabtu',
                    'Sunday' => 'Minggu',
                ];
                $hariInggris = date('l', strtotime($tanggal));
                $hari = $hariMapping[$hariInggris] ?? 'Senin';
            }

            // Query jadwal berdasarkan filter
            $query = JadwalKelas::with(['guru', 'mapel', 'kelas'])
                ->where('hari', $hari)
                ->aktif();

            if ($kelas_id) {
                $query->where('kelas_id', $kelas_id);
            }

            $jadwalList = $query->orderBy('jam_mulai', 'asc')->get();

            // Get kehadiran guru
            $data = $jadwalList->map(function($jadwal) use ($tanggal) {
                // Prefer admin-entered (kurikulum) kehadiran if exists; otherwise use latest
                $kehadiran = KehadiranGuru::where('jadwal_id', $jadwal->id)
                    ->whereDate('tanggal', $tanggal)
                    ->orderByRaw("(CASE WHEN input_by_kurikulum_id IS NOT NULL THEN 1 ELSE 0 END) DESC")
                    ->orderBy('created_at', 'desc')
                    ->first();

                return [
                    'jadwal_id' => $jadwal->id,
                    'hari' => $jadwal->hari,
                    'jam_mulai' => $jadwal->jam_mulai,
                    'jam_selesai' => $jadwal->jam_selesai,
                    'jam' => date('H:i', strtotime($jadwal->jam_mulai)) . ' - ' . date('H:i', strtotime($jadwal->jam_selesai)),
                    
                    'guru' => [
                        'id' => $jadwal->guru->id,
                        'nama' => $jadwal->guru->guru ?? $jadwal->guru->nama,
                        'kode_guru' => $jadwal->guru->kode_guru,
                    ],
                    
                    'mapel' => [
                        'id' => $jadwal->mapel->id,
                        'nama' => $jadwal->mapel->mapel,
                    ],
                    
                    'kelas' => [
                        'id' => $jadwal->kelas->id,
                        'nama' => $jadwal->kelas->nama_kelas,
                    ],
                    
                    'status_kehadiran' => $kehadiran ? $kehadiran->status : 'belum_diisi',
                    'kehadiran_id' => $kehadiran ? $kehadiran->id : null,
                    'keterangan' => $kehadiran ? $kehadiran->keterangan : null,
                    'input_by_kurikulum_id' => $kehadiran ? $kehadiran->input_by_kurikulum_id : null,
                    'input_by_kurikulum_name' => $kehadiran && $kehadiran->inputByKurikulum ? $kehadiran->inputByKurikulum->name : null,
                    'input_by' => $kehadiran ? ($kehadiran->input_by_siswa_id ? 'siswa' : 'kurikulum') : null,
                ];
            });

            // Statistik (will compute after combining extra admin entries)

            // Now additionally return admin-extra kehadiran (kehadiran items not tied to jadwal)
            $extraKehadiranQuery = KehadiranGuru::with(['guru','mapel','inputByKurikulum'])
                ->whereDate('tanggal', $tanggal)
                ->whereNull('jadwal_id')
                ->whereNotNull('input_by_kurikulum_id');

            if ($kelas_id) {
                $extraKehadiranQuery->where('kelas_id', $kelas_id);
            }

            $extraKehadiran = $extraKehadiranQuery->get()
                ->map(function($k) {
                    return [
                        'jadwal_id' => $k->jadwal_id,
                        'hari' => null,
                        'jam_mulai' => null,
                        'jam_selesai' => null,
                        'jam' => null,
                        'guru' => [
                            'id' => $k->guru?->id,
                            'nama' => $k->guru?->guru ?? $k->guru?->nama,
                            'kode_guru' => $k->guru?->kode_guru,
                        ],
                        'mapel' => [
                            'id' => $k->mapel?->id,
                            'nama' => $k->mapel?->mapel,
                        ],
                        'kelas' => [
                            'id' => $k->kelas_id,
                            'nama' => optional($k->kelas)->nama_kelas,
                        ],
                        'status_kehadiran' => $k->status,
                        'kehadiran_id' => $k->id,
                        'keterangan' => $k->keterangan,
                        'input_by' => 'kurikulum',
                        'input_by_kurikulum_id' => $k->input_by_kurikulum_id,
                        'input_by_kurikulum_name' => $k->inputByKurikulum?->name,
                    ];
                });

            $combined = $data->merge($extraKehadiran);

            // Recompute statistik including extra admin entries
            $statistik = [
                'total' => $combined->count(),
                'hadir' => $combined->where('status_kehadiran', 'hadir')->count(),
                'tidak_hadir' => $combined->where('status_kehadiran', 'tidak_hadir')->count(),
                'izin' => $combined->where('status_kehadiran', 'izin')->count(),
                'sakit' => $combined->where('status_kehadiran', 'sakit')->count(),
                'belum_diisi' => $combined->where('status_kehadiran', 'belum_diisi')->count(),
            ];

            return response()->json([
                'success' => true,
                'message' => 'Data daftar guru berhasil diambil',
                'data' => $combined,
                'statistik' => $statistik,
                'filter' => [
                    'hari' => $hari,
                    'kelas_id' => $kelas_id,
                    'tanggal' => $tanggal,
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
     * TAB 3: LAPORAN
     * Daftar pesan/request guru pengganti dari siswa
     * GET /api/kurikulum/laporan-request
     */
    public function getLaporanRequest(Request $request)
    {
        try {
            $tanggal_dari = $request->input('tanggal_dari');
            $tanggal_sampai = $request->input('tanggal_sampai');
            $kelas_id = $request->input('kelas_id');
            $status = $request->input('status'); // pending, approved, rejected

            $query = GuruPengganti::with([
                'kehadiranGuru.guru',
                'kehadiranGuru.jadwal.mapel',
                'kehadiranGuru.jadwal.kelas',
                'guruPengganti',
                'requestedBySiswa.kelas'
            ])->whereNotNull('requested_by_siswa_id'); // Hanya yang direquest siswa

            // Filter tanggal
            if ($tanggal_dari) {
                $query->whereDate('tanggal', '>=', $tanggal_dari);
            }
            if ($tanggal_sampai) {
                $query->whereDate('tanggal', '<=', $tanggal_sampai);
            }

            // Filter kelas
            if ($kelas_id) {
                $query->whereHas('kehadiranGuru.jadwal', function($q) use ($kelas_id) {
                    $q->where('kelas_id', $kelas_id);
                });
            }

            // Filter status
            if ($status) {
                $query->where('status', $status);
            }

            $laporan = $query->orderBy('created_at', 'desc')->get();

            $data = $laporan->map(function($req) {
                $kehadiran = $req->kehadiranGuru;
                $jadwal = $kehadiran ? $kehadiran->jadwal : null;
                
                return [
                    'id' => $req->id,
                    'tanggal' => $req->tanggal,
                    'status' => $req->status,
                    'keterangan' => $req->keterangan,
                    'created_at' => $req->created_at->format('Y-m-d H:i:s'),
                    
                    // Info guru tidak hadir
                    'guru_tidak_hadir' => $kehadiran && $kehadiran->guru ? [
                        'id' => $kehadiran->guru->id,
                        'nama' => $kehadiran->guru->guru ?? $kehadiran->guru->nama,
                        'status' => $kehadiran->status,
                    ] : null,
                    
                    // Info jadwal
                    'jadwal' => $jadwal ? [
                        'hari' => $jadwal->hari,
                        'jam' => date('H:i', strtotime($jadwal->jam_mulai)) . ' - ' . date('H:i', strtotime($jadwal->jam_selesai)),
                        'mapel' => $jadwal->mapel ? $jadwal->mapel->mapel : null,
                        'kelas' => $jadwal->kelas ? $jadwal->kelas->nama_kelas : null,
                    ] : null,
                    
                    // Guru pengganti
                    'guru_pengganti' => $req->guruPengganti ? [
                        'id' => $req->guruPengganti->id,
                        'nama' => $req->guruPengganti->guru ?? $req->guruPengganti->nama,
                    ] : null,
                    
                    // Siswa yang request
                    'siswa_request' => $req->requestedBySiswa ? [
                        'id' => $req->requestedBySiswa->id,
                        'nama' => $req->requestedBySiswa->nama,
                        'nis' => $req->requestedBySiswa->nis,
                        'kelas' => $req->requestedBySiswa->kelas ? $req->requestedBySiswa->kelas->nama_kelas : null,
                    ] : null,
                ];
            });

            // Statistik
            $statistik = [
                'total' => $data->count(),
                'pending' => $data->where('status', 'pending')->count(),
                'aktif' => $data->where('status', 'aktif')->count(),
                'ditolak' => $data->where('status', 'ditolak')->count(),
            ];

            return response()->json([
                'success' => true,
                'message' => 'Data laporan request guru pengganti berhasil diambil',
                'data' => $data,
                'statistik' => $statistik,
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
     * TAB 3: LAPORAN
     * Hapus/Clear pesan request dari siswa
     * DELETE /api/kurikulum/laporan-request/{id}
     */
    public function hapusLaporanRequest($id)
    {
        try {
            $request = GuruPengganti::find($id);

            if (!$request) {
                return response()->json([
                    'success' => false,
                    'message' => 'Data tidak ditemukan',
                ], 404);
            }

            // Hapus request
            $request->delete();

            return response()->json([
                'success' => true,
                'message' => 'Data request berhasil dihapus',
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
     * TAB 3: LAPORAN
     * Hapus semua pesan request dari siswa
     * DELETE /api/kurikulum/clear-laporan
     */
    public function clearLaporan(Request $request)
    {
        try {
            $validated = $request->validate([
                'status' => 'nullable|in:pending,approved,rejected,all',
                'tanggal_dari' => 'nullable|date',
                'tanggal_sampai' => 'nullable|date',
            ]);

            $query = GuruPengganti::whereNotNull('requested_by_siswa_id');

            // Filter status
            if (isset($validated['status']) && $validated['status'] !== 'all') {
                $query->where('status', $validated['status']);
            }

            // Filter tanggal
            if (isset($validated['tanggal_dari'])) {
                $query->whereDate('tanggal', '>=', $validated['tanggal_dari']);
            }
            if (isset($validated['tanggal_sampai'])) {
                $query->whereDate('tanggal', '<=', $validated['tanggal_sampai']);
            }

            $deleted = $query->delete();

            return response()->json([
                'success' => true,
                'message' => "Berhasil menghapus {$deleted} data laporan",
                'deleted_count' => $deleted,
            ], 200);

        } catch (ValidationException $e) {
            return response()->json([
                'success' => false,
                'message' => 'Validasi gagal',
                'errors' => $e->errors(),
            ], 422);
        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    /**
     * Helper: Daftar kelas untuk dropdown filter
     * GET /api/kurikulum/list-kelas
     */
    public function getListKelas()
    {
        try {
            $kelas = Kelas::orderBy('nama_kelas', 'asc')->get(['id', 'kode_kelas', 'nama_kelas', 'tingkat', 'jurusan']);

            return response()->json([
                'success' => true,
                'message' => 'Data kelas berhasil diambil',
                'data' => $kelas,
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
     * Helper: Daftar hari untuk dropdown filter
     * GET /api/kurikulum/list-hari
     */
    public function getListHari()
    {
        return response()->json([
            'success' => true,
            'message' => 'Data hari berhasil diambil',
            'data' => ['Senin', 'Selasa', 'Rabu', 'Kamis', 'Jumat', 'Sabtu'],
        ], 200);
    }
}
