<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\JadwalKelas;
use App\Models\Guru;
use App\Models\Mapel;
use App\Models\Kelas;
use App\Models\TahunAjaran;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\DB;

class JadwalController extends Controller
{
    /**
     * Display a listing of jadwal.
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function index(Request $request)
    {
        $query = JadwalKelas::with(['kelas', 'mapel', 'guru', 'tahunAjaran']);

        // Filter by kelas
        if ($request->has('kelas_id')) {
            $query->where('kelas_id', $request->kelas_id);
        }

        // Filter by guru
        if ($request->has('guru_id')) {
            $query->where('guru_id', $request->guru_id);
        }

        // Filter by hari
        if ($request->has('hari')) {
            $query->where('hari', $request->hari);
        }

        // Filter by tahun ajaran (default: active)
        if ($request->has('tahun_ajaran_id')) {
            $query->where('tahun_ajaran_id', $request->tahun_ajaran_id);
        } else {
            $tahunAjaranAktif = TahunAjaran::where('flag', true)->first();
            if ($tahunAjaranAktif) {
                $query->where('tahun_ajaran_id', $tahunAjaranAktif->id);
            }
        }

        // Pagination
        $perPage = $request->get('per_page', 20);
        $jadwal = $query->orderBy('hari')->orderBy('jam_mulai')->paginate($perPage);

        return response()->json([
            'success' => true,
            'message' => 'Data jadwal berhasil diambil',
            'data' => $jadwal->items(),
            'pagination' => [
                'current_page' => $jadwal->currentPage(),
                'last_page' => $jadwal->lastPage(),
                'per_page' => $jadwal->perPage(),
                'total' => $jadwal->total(),
            ]
        ], 200);
    }

    /**
     * Get timetable view (formatted by hari and jam).
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function timetable(Request $request)
    {
        $tahunAjaranId = $request->get('tahun_ajaran_id');
        
        // Get active tahun ajaran if not specified
        if (!$tahunAjaranId) {
            $tahunAjaranAktif = TahunAjaran::where('flag', true)->first();
            $tahunAjaranId = $tahunAjaranAktif ? $tahunAjaranAktif->id : null;
        }

        if (!$tahunAjaranId) {
            return response()->json([
                'success' => false,
                'message' => 'Tidak ada tahun ajaran aktif'
            ], 400);
        }

        $query = JadwalKelas::with(['kelas', 'mapel', 'guru'])
            ->where('tahun_ajaran_id', $tahunAjaranId);

        // Filter by kelas if specified
        if ($request->has('kelas_id')) {
            $query->where('kelas_id', $request->kelas_id);
        }

        // Filter by guru if specified
        if ($request->has('guru_id')) {
            $query->where('guru_id', $request->guru_id);
        }

        $jadwal = $query->orderBy('hari')->orderBy('jam_mulai')->get();

        // Group by hari
        $timetable = $jadwal->groupBy('hari')->map(function($items, $hari) {
            return [
                'hari' => $hari,
                'jadwal' => $items->map(function($item) {
                    return [
                        'id' => $item->id,
                        'jam_mulai' => $item->jam_mulai,
                        'jam_selesai' => $item->jam_selesai,
                        'kelas' => [
                            'id' => $item->kelas->id,
                            'nama' => $item->kelas->nama,
                        ],
                        'mapel' => [
                            'id' => $item->mapel->id,
                            'mapel' => $item->mapel->mapel,
                            'kode_mapel' => $item->mapel->kode_mapel,
                        ],
                        'guru' => [
                            'id' => $item->guru->id,
                            'nama' => $item->guru->nama,
                            'nip' => $item->guru->nip,
                        ],
                    ];
                })->values()
            ];
        })->values();

        return response()->json([
            'success' => true,
            'message' => 'Timetable berhasil diambil',
            'data' => $timetable
        ], 200);
    }

    /**
     * Store a newly created jadwal.
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'kelas_id' => 'required|exists:kelas,id',
            'mapel_id' => 'required|exists:mapels,id',
            'guru_id' => 'required|exists:gurus,id',
            'tahun_ajaran_id' => 'required|exists:tahun_ajarans,id',
            'hari' => 'required|in:Senin,Selasa,Rabu,Kamis,Jumat,Sabtu',
            'jam_mulai' => 'required|date_format:H:i',
            'jam_selesai' => 'required|date_format:H:i|after:jam_mulai',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Validation error',
                'errors' => $validator->errors()
            ], 422);
        }

        // Check for conflicting schedule (same kelas, hari, overlapping time)
        $conflict = JadwalKelas::where('kelas_id', $request->kelas_id)
            ->where('hari', $request->hari)
            ->where('tahun_ajaran_id', $request->tahun_ajaran_id)
            ->where(function($query) use ($request) {
                $query->whereBetween('jam_mulai', [$request->jam_mulai, $request->jam_selesai])
                    ->orWhereBetween('jam_selesai', [$request->jam_mulai, $request->jam_selesai])
                    ->orWhere(function($q) use ($request) {
                        $q->where('jam_mulai', '<=', $request->jam_mulai)
                          ->where('jam_selesai', '>=', $request->jam_selesai);
                    });
            })
            ->exists();

        if ($conflict) {
            return response()->json([
                'success' => false,
                'message' => 'Jadwal bentrok dengan jadwal yang sudah ada'
            ], 422);
        }

        // Check guru availability (same guru, hari, overlapping time)
        $guruConflict = JadwalKelas::where('guru_id', $request->guru_id)
            ->where('hari', $request->hari)
            ->where('tahun_ajaran_id', $request->tahun_ajaran_id)
            ->where(function($query) use ($request) {
                $query->whereBetween('jam_mulai', [$request->jam_mulai, $request->jam_selesai])
                    ->orWhereBetween('jam_selesai', [$request->jam_mulai, $request->jam_selesai])
                    ->orWhere(function($q) use ($request) {
                        $q->where('jam_mulai', '<=', $request->jam_mulai)
                          ->where('jam_selesai', '>=', $request->jam_selesai);
                    });
            })
            ->exists();

        if ($guruConflict) {
            return response()->json([
                'success' => false,
                'message' => 'Guru sudah mengajar di kelas lain pada waktu yang sama'
            ], 422);
        }

        $jadwal = JadwalKelas::create($request->all());
        $jadwal->load(['kelas', 'mapel', 'guru', 'tahunAjaran']);

        return response()->json([
            'success' => true,
            'message' => 'Jadwal berhasil dibuat',
            'data' => $jadwal
        ], 201);
    }

    /**
     * Display the specified jadwal.
     *
     * @param int $id
     * @return \Illuminate\Http\JsonResponse
     */
    public function show($id)
    {
        $jadwal = JadwalKelas::with(['kelas', 'mapel', 'guru', 'tahunAjaran'])->find($id);

        if (!$jadwal) {
            return response()->json([
                'success' => false,
                'message' => 'Jadwal tidak ditemukan'
            ], 404);
        }

        return response()->json([
            'success' => true,
            'message' => 'Data jadwal berhasil diambil',
            'data' => $jadwal
        ], 200);
    }

    /**
     * Update the specified jadwal.
     *
     * @param Request $request
     * @param int $id
     * @return \Illuminate\Http\JsonResponse
     */
    public function update(Request $request, $id)
    {
        $jadwal = JadwalKelas::find($id);

        if (!$jadwal) {
            return response()->json([
                'success' => false,
                'message' => 'Jadwal tidak ditemukan'
            ], 404);
        }

        $validator = Validator::make($request->all(), [
            'kelas_id' => 'sometimes|required|exists:kelas,id',
            'mapel_id' => 'sometimes|required|exists:mapels,id',
            'guru_id' => 'sometimes|required|exists:gurus,id',
            'tahun_ajaran_id' => 'sometimes|required|exists:tahun_ajarans,id',
            'hari' => 'sometimes|required|in:Senin,Selasa,Rabu,Kamis,Jumat,Sabtu',
            'jam_mulai' => 'sometimes|required|date_format:H:i',
            'jam_selesai' => 'sometimes|required|date_format:H:i|after:jam_mulai',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Validation error',
                'errors' => $validator->errors()
            ], 422);
        }

        // Check for conflicts (excluding current jadwal)
        $kelasId = $request->get('kelas_id', $jadwal->kelas_id);
        $hari = $request->get('hari', $jadwal->hari);
        $tahunAjaranId = $request->get('tahun_ajaran_id', $jadwal->tahun_ajaran_id);
        $jamMulai = $request->get('jam_mulai', $jadwal->jam_mulai);
        $jamSelesai = $request->get('jam_selesai', $jadwal->jam_selesai);

        $conflict = JadwalKelas::where('kelas_id', $kelasId)
            ->where('hari', $hari)
            ->where('tahun_ajaran_id', $tahunAjaranId)
            ->where('id', '!=', $id)
            ->where(function($query) use ($jamMulai, $jamSelesai) {
                $query->whereBetween('jam_mulai', [$jamMulai, $jamSelesai])
                    ->orWhereBetween('jam_selesai', [$jamMulai, $jamSelesai])
                    ->orWhere(function($q) use ($jamMulai, $jamSelesai) {
                        $q->where('jam_mulai', '<=', $jamMulai)
                          ->where('jam_selesai', '>=', $jamSelesai);
                    });
            })
            ->exists();

        if ($conflict) {
            return response()->json([
                'success' => false,
                'message' => 'Jadwal bentrok dengan jadwal yang sudah ada'
            ], 422);
        }

        $jadwal->update($request->all());
        $jadwal->load(['kelas', 'mapel', 'guru', 'tahunAjaran']);

        return response()->json([
            'success' => true,
            'message' => 'Jadwal berhasil diupdate',
            'data' => $jadwal
        ], 200);
    }

    /**
     * Remove the specified jadwal.
     *
     * @param int $id
     * @return \Illuminate\Http\JsonResponse
     */
    public function destroy($id)
    {
        $jadwal = JadwalKelas::find($id);

        if (!$jadwal) {
            return response()->json([
                'success' => false,
                'message' => 'Jadwal tidak ditemukan'
            ], 404);
        }

        $jadwal->delete();

        return response()->json([
            'success' => true,
            'message' => 'Jadwal berhasil dihapus'
        ], 200);
    }
}
