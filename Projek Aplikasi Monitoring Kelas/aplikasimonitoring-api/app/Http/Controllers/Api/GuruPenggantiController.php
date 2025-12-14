<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\GuruPengganti;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class GuruPenggantiController extends Controller
{
    /**
     * Display a listing of guru pengganti.
     */
    public function index(Request $request)
    {
        $query = GuruPengganti::with(['guruPengganti', 'kelas', 'mapel', 'assignedByKurikulum']);

        // Filter by status
        if ($request->has('status')) {
            $query->where('status', $request->status);
        }

        // Filter by tanggal
        if ($request->has('tanggal')) {
            $query->where('tanggal', $request->tanggal);
        }

        // Filter by kelas
        if ($request->has('kelas_id')) {
            $query->where('kelas_id', $request->kelas_id);
        }

        // Filter by guru pengganti
        if ($request->has('guru_pengganti_id')) {
            $query->where('guru_pengganti_id', $request->guru_pengganti_id);
        }

        // Pagination
        $perPage = $request->get('per_page', 10);
        $guruPengganti = $query->orderBy('tanggal', 'desc')->paginate($perPage);

        return response()->json([
            'success' => true,
            'message' => 'Data guru pengganti berhasil diambil',
            'data' => $guruPengganti->items(),
            'pagination' => [
                'current_page' => $guruPengganti->currentPage(),
                'last_page' => $guruPengganti->lastPage(),
                'per_page' => $guruPengganti->perPage(),
                'total' => $guruPengganti->total(),
            ]
        ], 200);
    }

    /**
     * Store a newly created guru pengganti.
     */
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'kehadiran_guru_id' => 'nullable|exists:kehadiran_gurus,id',
            'guru_pengganti_id' => 'required|exists:gurus,id',
            'kelas_id' => 'required|exists:kelas,id',
            'mapel_id' => 'required|exists:mapels,id',
            'tanggal' => 'required|date',
            'jam_mulai' => 'required|date_format:H:i',
            'jam_selesai' => 'required|date_format:H:i|after:jam_mulai',
            'status' => 'nullable|in:pending,aktif,selesai,ditolak',
            'catatan' => 'nullable|string',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Validation error',
                'errors' => $validator->errors()
            ], 422);
        }

        $guruPengganti = GuruPengganti::create([
            'kehadiran_guru_id' => $request->kehadiran_guru_id,
            'guru_pengganti_id' => $request->guru_pengganti_id,
            'kelas_id' => $request->kelas_id,
            'mapel_id' => $request->mapel_id,
            'tanggal' => $request->tanggal,
            'jam_mulai' => $request->jam_mulai,
            'jam_selesai' => $request->jam_selesai,
            'assigned_by_kurikulum_id' => auth()->user() ? auth()->user()->id : null,
            'status' => $request->status ?? 'pending',
            'catatan' => $request->catatan,
        ]);

        $guruPengganti->load(['guruPengganti', 'kelas', 'mapel']);

        return response()->json([
            'success' => true,
            'message' => 'Guru pengganti berhasil dibuat',
            'data' => $guruPengganti
        ], 201);
    }

    /**
     * Display the specified guru pengganti.
     */
    public function show(string $id)
    {
        $guruPengganti = GuruPengganti::with(['guruPengganti', 'kelas', 'mapel', 'assignedByKurikulum'])->find($id);

        if (!$guruPengganti) {
            return response()->json([
                'success' => false,
                'message' => 'Guru pengganti tidak ditemukan'
            ], 404);
        }

        return response()->json([
            'success' => true,
            'message' => 'Data guru pengganti berhasil diambil',
            'data' => $guruPengganti
        ], 200);
    }

    /**
     * Update the specified guru pengganti.
     */
    public function update(Request $request, string $id)
    {
        $guruPengganti = GuruPengganti::find($id);

        if (!$guruPengganti) {
            return response()->json([
                'success' => false,
                'message' => 'Guru pengganti tidak ditemukan'
            ], 404);
        }

        $validator = Validator::make($request->all(), [
            'guru_pengganti_id' => 'sometimes|required|exists:gurus,id',
            'kelas_id' => 'sometimes|required|exists:kelas,id',
            'mapel_id' => 'sometimes|required|exists:mapels,id',
            'tanggal' => 'sometimes|required|date',
            'jam_mulai' => 'sometimes|required|date_format:H:i',
            'jam_selesai' => 'sometimes|required|date_format:H:i',
            'status' => 'sometimes|in:pending,aktif,selesai,ditolak',
            'catatan' => 'nullable|string',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Validation error',
                'errors' => $validator->errors()
            ], 422);
        }

        $guruPengganti->update($request->all());
        $guruPengganti->load(['guruPengganti', 'kelas', 'mapel', 'assignedByKurikulum']);

        return response()->json([
            'success' => true,
            'message' => 'Guru pengganti berhasil diupdate',
            'data' => $guruPengganti
        ], 200);
    }

    /**
     * Remove the specified guru pengganti.
     */
    public function destroy(string $id)
    {
        $guruPengganti = GuruPengganti::find($id);

        if (!$guruPengganti) {
            return response()->json([
                'success' => false,
                'message' => 'Guru pengganti tidak ditemukan'
            ], 404);
        }

        $guruPengganti->delete();

        return response()->json([
            'success' => true,
            'message' => 'Guru pengganti berhasil dihapus'
        ], 200);
    }

    /**
     * Approve guru pengganti request.
     * POST /api/guru-pengganti/{id}/approve
     */
    public function approve(string $id)
    {
        $guruPengganti = GuruPengganti::find($id);

        if (!$guruPengganti) {
            return response()->json([
                'success' => false,
                'message' => 'Guru pengganti tidak ditemukan'
            ], 404);
        }

        $guruPengganti->status = 'aktif';
        $guruPengganti->save();
        $guruPengganti->load(['guruPengganti', 'kelas', 'mapel']);

        return response()->json([
            'success' => true,
            'message' => 'Guru pengganti berhasil disetujui',
            'data' => $guruPengganti
        ], 200);
    }

    /**
     * Reject guru pengganti request.
     * POST /api/guru-pengganti/{id}/reject
     */
    public function reject(Request $request, string $id)
    {
        $guruPengganti = GuruPengganti::find($id);

        if (!$guruPengganti) {
            return response()->json([
                'success' => false,
                'message' => 'Guru pengganti tidak ditemukan'
            ], 404);
        }

        $guruPengganti->status = 'ditolak';
        if ($request->has('catatan')) {
            $guruPengganti->catatan = $request->catatan;
        }
        $guruPengganti->save();
        $guruPengganti->load(['guruPengganti', 'kelas', 'mapel']);

        return response()->json([
            'success' => true,
            'message' => 'Guru pengganti berhasil ditolak',
            'data' => $guruPengganti
        ], 200);
    }
}
