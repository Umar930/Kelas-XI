<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\JadwalKelas;
use App\Models\KehadiranGuru;
use App\Models\Kelas;
use Illuminate\Http\Request;

class KepalaSekolahFiturController extends Controller
{
    /**
     * 1. Lihat jadwal semua kelas
     * GET /api/kepala-sekolah/jadwal
     */
    public function getJadwalKelas(Request $request)
    {
        try {
            $kelas_id = $request->input('kelas_id');
            $hari = $request->input('hari');

            $query = JadwalKelas::with(['kelas', 'guru', 'mapel', 'tahunAjaran'])
                ->aktif();

            if ($kelas_id) {
                $query->where('kelas_id', $kelas_id);
            }

            if ($hari) {
                $query->where('hari', $hari);
            }

            $jadwal = $query->orderBy('hari', 'asc')
                ->orderBy('jam_mulai', 'asc')
                ->get();

            // Group by kelas
            $jadwalGrouped = $jadwal->groupBy('kelas_id')->map(function($items, $kelasId) {
                $kelas = Kelas::find($kelasId);
                
                return [
                    'kelas' => [
                        'id' => $kelas->id,
                        'kode_kelas' => $kelas->kode_kelas,
                        'nama_kelas' => $kelas->nama_kelas,
                        'tingkat' => $kelas->tingkat,
                        'jurusan' => $kelas->jurusan,
                    ],
                    'jadwal' => $items->groupBy('hari')->map(function($hariItems, $hari) {
                        return [
                            'hari' => $hari,
                            'mata_pelajaran' => $hariItems->map(function($j) {
                                return [
                                    'id' => $j->id,
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
                                ];
                            })->values(),
                        ];
                    })->values(),
                ];
            })->values();

            return response()->json([
                'success' => true,
                'message' => 'Data jadwal kelas berhasil diambil',
                'data' => $jadwalGrouped,
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
     * 2. Lihat kehadiran guru yang diinput siswa
     * GET /api/kepala-sekolah/kehadiran-guru
     */
    public function getKehadiranGuru(Request $request)
    {
        try {
            $tanggal = $request->input('tanggal', now()->toDateString());
            $kelas_id = $request->input('kelas_id');
            $guru_id = $request->input('guru_id');
            $status = $request->input('status');

            $query = KehadiranGuru::with(['guru', 'kelas', 'mapel', 'inputBySiswa', 'inputByKurikulum'])
                ->where('tanggal', $tanggal);

            if ($kelas_id) {
                $query->where('kelas_id', $kelas_id);
            }

            if ($guru_id) {
                $query->where('guru_id', $guru_id);
            }

            if ($status) {
                $query->where('status', $status);
            }

            $kehadiran = $query->orderBy('kelas_id', 'asc')
                ->get();

            // Group by kelas
            $kehadiranGrouped = $kehadiran->groupBy('kelas_id')->map(function($items, $kelasId) {
                $kelas = Kelas::find($kelasId);
                
                return [
                    'kelas' => [
                        'id' => $kelas->id,
                        'kode_kelas' => $kelas->kode_kelas,
                        'nama_kelas' => $kelas->nama_kelas,
                        'tingkat' => $kelas->tingkat,
                        'jurusan' => $kelas->jurusan,
                    ],
                    'kehadiran' => $items->map(function($k) {
                        return [
                            'id' => $k->id,
                            'tanggal' => $k->tanggal,
                            'guru' => [
                                'id' => $k->guru->id,
                                'nama' => $k->guru->nama,
                                'kode_guru' => $k->guru->kode_guru,
                            ],
                            'mapel' => [
                                'id' => $k->mapel->id,
                                'nama' => $k->mapel->mapel,
                                'kode_mapel' => $k->mapel->kode_mapel,
                            ],
                            'status' => $k->status,
                            'keterangan' => $k->keterangan,
                            'input_by' => $k->input_by_siswa_id ? [
                                'type' => 'siswa',
                                'nama' => $k->inputBySiswa->nama,
                            ] : [
                                'type' => 'kurikulum',
                                'nama' => $k->inputByKurikulum->name,
                            ],
                        ];
                    })->values(),
                    'summary' => [
                        'total' => $items->count(),
                        'hadir' => $items->where('status', 'hadir')->count(),
                        'tidak_hadir' => $items->where('status', 'tidak_hadir')->count(),
                        'izin' => $items->where('status', 'izin')->count(),
                        'sakit' => $items->where('status', 'sakit')->count(),
                    ],
                ];
            })->values();

            // Overall summary
            $overallSummary = [
                'total_kelas' => $kehadiranGrouped->count(),
                'total_kehadiran' => $kehadiran->count(),
                'total_hadir' => $kehadiran->where('status', 'hadir')->count(),
                'total_tidak_hadir' => $kehadiran->where('status', 'tidak_hadir')->count(),
                'total_izin' => $kehadiran->where('status', 'izin')->count(),
                'total_sakit' => $kehadiran->where('status', 'sakit')->count(),
            ];

            return response()->json([
                'success' => true,
                'message' => 'Data kehadiran guru berhasil diambil',
                'data' => [
                    'tanggal' => $tanggal,
                    'kehadiran' => $kehadiranGrouped,
                    'summary' => $overallSummary,
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
     * Get statistik kehadiran guru (range tanggal)
     * GET /api/kepala-sekolah/statistik-kehadiran
     */
    public function getStatistikKehadiran(Request $request)
    {
        try {
            $tanggal_mulai = $request->input('tanggal_mulai', now()->startOfMonth()->toDateString());
            $tanggal_selesai = $request->input('tanggal_selesai', now()->endOfMonth()->toDateString());

            $kehadiran = KehadiranGuru::with(['guru'])
                ->whereBetween('tanggal', [$tanggal_mulai, $tanggal_selesai])
                ->get();

            // Statistik per guru
            $statistikPerGuru = $kehadiran->groupBy('guru_id')->map(function($items, $guruId) {
                $guru = $items->first()->guru;
                
                return [
                    'guru' => [
                        'id' => $guru->id,
                        'nama' => $guru->nama,
                        'kode_guru' => $guru->kode_guru,
                    ],
                    'statistik' => [
                        'total' => $items->count(),
                        'hadir' => $items->where('status', 'hadir')->count(),
                        'tidak_hadir' => $items->where('status', 'tidak_hadir')->count(),
                        'izin' => $items->where('status', 'izin')->count(),
                        'sakit' => $items->where('status', 'sakit')->count(),
                        'persentase_hadir' => $items->count() > 0 
                            ? round(($items->where('status', 'hadir')->count() / $items->count()) * 100, 2) 
                            : 0,
                    ],
                ];
            })->values();

            // Overall statistik
            $overallStatistik = [
                'total_kehadiran' => $kehadiran->count(),
                'total_hadir' => $kehadiran->where('status', 'hadir')->count(),
                'total_tidak_hadir' => $kehadiran->where('status', 'tidak_hadir')->count(),
                'total_izin' => $kehadiran->where('status', 'izin')->count(),
                'total_sakit' => $kehadiran->where('status', 'sakit')->count(),
                'persentase_hadir' => $kehadiran->count() > 0 
                    ? round(($kehadiran->where('status', 'hadir')->count() / $kehadiran->count()) * 100, 2) 
                    : 0,
            ];

            return response()->json([
                'success' => true,
                'message' => 'Statistik kehadiran guru berhasil diambil',
                'data' => [
                    'periode' => [
                        'tanggal_mulai' => $tanggal_mulai,
                        'tanggal_selesai' => $tanggal_selesai,
                    ],
                    'statistik_per_guru' => $statistikPerGuru,
                    'statistik_overall' => $overallStatistik,
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
}
