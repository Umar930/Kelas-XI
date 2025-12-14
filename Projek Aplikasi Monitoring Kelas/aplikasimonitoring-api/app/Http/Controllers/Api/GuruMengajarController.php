<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\GuruMengajar;
use App\Models\TahunAjaran;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class GuruMengajarController extends Controller
{
    /**
     * Display a listing of guru mengajar.
     */
    public function index(Request $request)
    {
        $query = GuruMengajar::with(['guru', 'mapel', 'kelas', 'tahunAjaran']);

        // Filter by guru
        if ($request->has('guru_id')) {
            $query->where('guru_id', $request->guru_id);
        }

        // Filter by kelas
        if ($request->has('kelas_id')) {
            $query->where('kelas_id', $request->kelas_id);
        }

        // Filter by tahun ajaran (default: active)
        if ($request->has('tahun_ajaran_id')) {
            $query->where('tahun_ajaran_id', $request->tahun_ajaran_id);
        } else {
            $tahunAjaranAktif = TahunAjaran::where('flag', 1)->first();
            if ($tahunAjaranAktif) {
                $query->where('tahun_ajaran_id', $tahunAjaranAktif->id);
            }
        }

        // Pagination
        $perPage = $request->get('per_page', 10);
        $guruMengajar = $query->orderBy('created_at', 'desc')->paginate($perPage);

        return response()->json([
            'success' => true,
            'message' => 'Data guru mengajar berhasil diambil',
            'data' => $guruMengajar->items(),
            'pagination' => [
                'current_page' => $guruMengajar->currentPage(),
                'last_page' => $guruMengajar->lastPage(),
                'per_page' => $guruMengajar->perPage(),
                'total' => $guruMengajar->total(),
            ]
        ], 200);
    }

    /**
     * Store a newly created guru mengajar.
     */
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'tahun_ajaran_id' => 'required|exists:tahun_ajarans,id',
            'guru_id' => 'required|exists:gurus,id',
            'mapel_id' => 'required|exists:mapels,id',
            'kelas_id' => 'required|exists:kelas,id',
            'jam_per_minggu' => 'nullable|integer|min:1',
            'keterangan' => 'nullable|string',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Validation error',
                'errors' => $validator->errors()
            ], 422);
        }

        // Check duplicate
        $exists = GuruMengajar::where('tahun_ajaran_id', $request->tahun_ajaran_id)
            ->where('guru_id', $request->guru_id)
            ->where('mapel_id', $request->mapel_id)
            ->where('kelas_id', $request->kelas_id)
            ->exists();

        if ($exists) {
            return response()->json([
                'success' => false,
                'message' => 'Guru sudah mengajar mata pelajaran ini di kelas ini pada tahun ajaran yang sama'
            ], 422);
        }

        $guruMengajar = GuruMengajar::create($request->all());
        $guruMengajar->load(['guru', 'mapel', 'kelas', 'tahunAjaran']);

        return response()->json([
            'success' => true,
            'message' => 'Guru mengajar berhasil dibuat',
            'data' => $guruMengajar
        ], 201);
    }

    /**
     * Display the specified guru mengajar.
     */
    public function show(string $id)
    {
        $guruMengajar = GuruMengajar::with(['guru', 'mapel', 'kelas', 'tahunAjaran'])->find($id);

        if (!$guruMengajar) {
            return response()->json([
                'success' => false,
                'message' => 'Guru mengajar tidak ditemukan'
            ], 404);
        }

        return response()->json([
            'success' => true,
            'message' => 'Data guru mengajar berhasil diambil',
            'data' => $guruMengajar
        ], 200);
    }

    /**
     * Update the specified guru mengajar.
     */
    public function update(Request $request, string $id)
    {
        $guruMengajar = GuruMengajar::find($id);

        if (!$guruMengajar) {
            return response()->json([
                'success' => false,
                'message' => 'Guru mengajar tidak ditemukan'
            ], 404);
        }

        $validator = Validator::make($request->all(), [
            'tahun_ajaran_id' => 'sometimes|required|exists:tahun_ajarans,id',
            'guru_id' => 'sometimes|required|exists:gurus,id',
            'mapel_id' => 'sometimes|required|exists:mapels,id',
            'kelas_id' => 'sometimes|required|exists:kelas,id',
            'jam_per_minggu' => 'nullable|integer|min:1',
            'keterangan' => 'nullable|string',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Validation error',
                'errors' => $validator->errors()
            ], 422);
        }

        $guruMengajar->update($request->all());
        $guruMengajar->load(['guru', 'mapel', 'kelas', 'tahunAjaran']);

        return response()->json([
            'success' => true,
            'message' => 'Guru mengajar berhasil diupdate',
            'data' => $guruMengajar
        ], 200);
    }

    /**
     * Remove the specified guru mengajar.
     */
    public function destroy(string $id)
    {
        $guruMengajar = GuruMengajar::find($id);

        if (!$guruMengajar) {
            return response()->json([
                'success' => false,
                'message' => 'Guru mengajar tidak ditemukan'
            ], 404);
        }

        $guruMengajar->delete();

        return response()->json([
            'success' => true,
            'message' => 'Guru mengajar berhasil dihapus'
        ], 200);
    }
}
