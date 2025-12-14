<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\AbsensiGuru;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class AbsensiGuruController extends Controller
{
    /**
     * Display a listing of absensi guru with pagination, search, and filter
     */
    public function index(Request $request)
    {
        try {
            $perPage = $request->input('per_page', 10);
            $guru_id = $request->input('guru_id');
            $mapel_id = $request->input('mapel_id');
            $kelas_id = $request->input('kelas_id');
            $tanggal_from = $request->input('tanggal_from');
            $tanggal_to = $request->input('tanggal_to');
            $status = $request->input('status');

            $query = AbsensiGuru::with(['guru', 'mapel', 'kelas']);

            // Filter by guru_id
            if ($guru_id) {
                $query->where('guru_id', $guru_id);
            }

            // Filter by mapel_id
            if ($mapel_id) {
                $query->where('mapel_id', $mapel_id);
            }

            // Filter by kelas_id
            if ($kelas_id) {
                $query->where('kelas_id', $kelas_id);
            }

            // Filter by date range
            if ($tanggal_from) {
                $query->whereDate('tanggal', '>=', $tanggal_from);
            }
            if ($tanggal_to) {
                $query->whereDate('tanggal', '<=', $tanggal_to);
            }

            // Filter by status
            if ($status) {
                $query->where('status', $status);
            }

            $absensi = $query->orderBy('tanggal', 'desc')
                             ->orderBy('id', 'desc')
                             ->paginate($perPage);

            return response()->json([
                'success' => true,
                'message' => 'Data absensi guru berhasil diambil',
                'data' => $absensi->items(),
                'pagination' => [
                    'total' => $absensi->total(),
                    'per_page' => $absensi->perPage(),
                    'current_page' => $absensi->currentPage(),
                    'last_page' => $absensi->lastPage(),
                    'from' => $absensi->firstItem(),
                    'to' => $absensi->lastItem(),
                ]
            ], 200);
        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * Store a newly created absensi guru
     */
    public function store(Request $request)
    {
        try {
            $validator = Validator::make($request->all(), [
                'guru_id' => 'required|exists:gurus,id',
                'mapel_id' => 'nullable|exists:mapels,id',
                'kelas_id' => 'nullable|exists:kelas,id',
                'tanggal' => 'required|date',
                'status' => 'required|in:hadir,sakit,izin,alpha,dinas',
                'keterangan' => 'nullable|string',
                'jam_masuk' => 'nullable|date_format:H:i',
                'jam_keluar' => 'nullable|date_format:H:i',
            ]);

            if ($validator->fails()) {
                return response()->json([
                    'success' => false,
                    'message' => 'Validasi gagal',
                    'errors' => $validator->errors()
                ], 422);
            }

            $absensi = AbsensiGuru::create($request->all());
            $absensi->load(['guru', 'mapel', 'kelas']);

            return response()->json([
                'success' => true,
                'message' => 'Data absensi guru berhasil ditambahkan',
                'data' => $absensi
            ], 201);
        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * Display the specified absensi guru
     */
    public function show($id)
    {
        try {
            $absensi = AbsensiGuru::with(['guru', 'mapel', 'kelas'])->find($id);

            if (!$absensi) {
                return response()->json([
                    'success' => false,
                    'message' => 'Data absensi guru tidak ditemukan'
                ], 404);
            }

            return response()->json([
                'success' => true,
                'message' => 'Data absensi guru berhasil diambil',
                'data' => $absensi
            ], 200);
        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * Update the specified absensi guru
     */
    public function update(Request $request, $id)
    {
        try {
            $absensi = AbsensiGuru::find($id);

            if (!$absensi) {
                return response()->json([
                    'success' => false,
                    'message' => 'Data absensi guru tidak ditemukan'
                ], 404);
            }

            $validator = Validator::make($request->all(), [
                'guru_id' => 'required|exists:gurus,id',
                'mapel_id' => 'nullable|exists:mapels,id',
                'kelas_id' => 'nullable|exists:kelas,id',
                'tanggal' => 'required|date',
                'status' => 'required|in:hadir,sakit,izin,alpha,dinas',
                'keterangan' => 'nullable|string',
                'jam_masuk' => 'nullable|date_format:H:i',
                'jam_keluar' => 'nullable|date_format:H:i',
            ]);

            if ($validator->fails()) {
                return response()->json([
                    'success' => false,
                    'message' => 'Validasi gagal',
                    'errors' => $validator->errors()
                ], 422);
            }

            $absensi->update($request->all());
            $absensi->load(['guru', 'mapel', 'kelas']);

            return response()->json([
                'success' => true,
                'message' => 'Data absensi guru berhasil diupdate',
                'data' => $absensi
            ], 200);
        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * Remove the specified absensi guru
     */
    public function destroy($id)
    {
        try {
            $absensi = AbsensiGuru::find($id);

            if (!$absensi) {
                return response()->json([
                    'success' => false,
                    'message' => 'Data absensi guru tidak ditemukan'
                ], 404);
            }

            $absensi->delete();

            return response()->json([
                'success' => true,
                'message' => 'Data absensi guru berhasil dihapus'
            ], 200);
        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage()
            ], 500);
        }
    }
}
