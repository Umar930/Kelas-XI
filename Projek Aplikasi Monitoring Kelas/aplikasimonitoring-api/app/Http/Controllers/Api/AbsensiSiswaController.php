<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\AbsensiSiswa;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class AbsensiSiswaController extends Controller
{
    /**
     * Display a listing of absensi siswa with pagination, search, and filter
     */
    public function index(Request $request)
    {
        try {
            $perPage = $request->input('per_page', 10);
            $siswa_id = $request->input('siswa_id');
            $kelas_id = $request->input('kelas_id');
            $mapel_id = $request->input('mapel_id');
            $tanggal_from = $request->input('tanggal_from');
            $tanggal_to = $request->input('tanggal_to');
            $status = $request->input('status');

            $query = AbsensiSiswa::with(['siswa', 'kelas', 'mapel']);

            // Filter by siswa_id
            if ($siswa_id) {
                $query->where('siswa_id', $siswa_id);
            }

            // Filter by kelas_id
            if ($kelas_id) {
                $query->where('kelas_id', $kelas_id);
            }

            // Filter by mapel_id
            if ($mapel_id) {
                $query->where('mapel_id', $mapel_id);
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
                'message' => 'Data absensi siswa berhasil diambil',
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
     * Store a newly created absensi siswa
     */
    public function store(Request $request)
    {
        try {
            $validator = Validator::make($request->all(), [
                'siswa_id' => 'required|exists:siswas,id',
                'kelas_id' => 'required|exists:kelas,id',
                'mapel_id' => 'nullable|exists:mapels,id',
                'tanggal' => 'required|date',
                'status' => 'required|in:hadir,sakit,izin,alpha',
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

            $absensi = AbsensiSiswa::create($request->all());
            $absensi->load(['siswa', 'kelas', 'mapel']);

            return response()->json([
                'success' => true,
                'message' => 'Data absensi siswa berhasil ditambahkan',
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
     * Display the specified absensi siswa
     */
    public function show($id)
    {
        try {
            $absensi = AbsensiSiswa::with(['siswa', 'kelas', 'mapel'])->find($id);

            if (!$absensi) {
                return response()->json([
                    'success' => false,
                    'message' => 'Data absensi siswa tidak ditemukan'
                ], 404);
            }

            return response()->json([
                'success' => true,
                'message' => 'Data absensi siswa berhasil diambil',
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
     * Update the specified absensi siswa
     */
    public function update(Request $request, $id)
    {
        try {
            $absensi = AbsensiSiswa::find($id);

            if (!$absensi) {
                return response()->json([
                    'success' => false,
                    'message' => 'Data absensi siswa tidak ditemukan'
                ], 404);
            }

            $validator = Validator::make($request->all(), [
                'siswa_id' => 'required|exists:siswas,id',
                'kelas_id' => 'required|exists:kelas,id',
                'mapel_id' => 'nullable|exists:mapels,id',
                'tanggal' => 'required|date',
                'status' => 'required|in:hadir,sakit,izin,alpha',
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
            $absensi->load(['siswa', 'kelas', 'mapel']);

            return response()->json([
                'success' => true,
                'message' => 'Data absensi siswa berhasil diupdate',
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
     * Remove the specified absensi siswa
     */
    public function destroy($id)
    {
        try {
            $absensi = AbsensiSiswa::find($id);

            if (!$absensi) {
                return response()->json([
                    'success' => false,
                    'message' => 'Data absensi siswa tidak ditemukan'
                ], 404);
            }

            $absensi->delete();

            return response()->json([
                'success' => true,
                'message' => 'Data absensi siswa berhasil dihapus'
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
