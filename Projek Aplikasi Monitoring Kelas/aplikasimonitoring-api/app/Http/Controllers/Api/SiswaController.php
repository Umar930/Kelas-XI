<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Siswa;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class SiswaController extends Controller
{
    /**
     * Display a listing of siswa with pagination, search, and filter
     */
    public function index(Request $request)
    {
        try {
            $perPage = $request->input('per_page', 10);
            $search = $request->input('search');
            $kelas_id = $request->input('kelas_id');
            $status = $request->input('status');
            $jenis_kelamin = $request->input('jenis_kelamin');

            $query = Siswa::with(['kelas', 'user']);

            // Search by NIS or nama
            if ($search) {
                $query->where(function ($q) use ($search) {
                    $q->where('nis', 'like', "%{$search}%")
                      ->orWhere('nama', 'like', "%{$search}%")
                      ->orWhere('email', 'like', "%{$search}%");
                });
            }

            // Filter by kelas_id
            if ($kelas_id) {
                $query->where('kelas_id', $kelas_id);
            }

            // Filter by status
            if ($status) {
                $query->where('status', $status);
            }

            // Filter by jenis_kelamin
            if ($jenis_kelamin) {
                $query->where('jenis_kelamin', $jenis_kelamin);
            }

            $siswas = $query->orderBy('nama')->paginate($perPage);

            return response()->json([
                'success' => true,
                'message' => 'Data siswa berhasil diambil',
                'data' => $siswas->items(),
                'pagination' => [
                    'total' => $siswas->total(),
                    'per_page' => $siswas->perPage(),
                    'current_page' => $siswas->currentPage(),
                    'last_page' => $siswas->lastPage(),
                    'from' => $siswas->firstItem(),
                    'to' => $siswas->lastItem(),
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
     * Store a newly created siswa
     */
    public function store(Request $request)
    {
        try {
            $validator = Validator::make($request->all(), [
                'nis' => 'required|string|unique:siswas,nis|max:20',
                'nama' => 'required|string|max:100',
                'jenis_kelamin' => 'required|in:L,P',
                'tempat_lahir' => 'nullable|string|max:100',
                'tanggal_lahir' => 'nullable|date',
                'alamat' => 'nullable|string',
                'telepon' => 'nullable|string|max:20',
                'email' => 'nullable|email|max:100',
                'kelas_id' => 'nullable|exists:kelas,id',
                'user_id' => 'nullable|exists:users,id',
                'status' => 'nullable|in:aktif,non-aktif,lulus,pindah',
            ]);

            if ($validator->fails()) {
                return response()->json([
                    'success' => false,
                    'message' => 'Validasi gagal',
                    'errors' => $validator->errors()
                ], 422);
            }

            $siswa = Siswa::create($request->all());
            $siswa->load(['kelas', 'user']);

            return response()->json([
                'success' => true,
                'message' => 'Data siswa berhasil ditambahkan',
                'data' => $siswa
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
     * Display the specified siswa
     */
    public function show($id)
    {
        try {
            $siswa = Siswa::with(['kelas', 'user', 'absensiSiswas'])->find($id);

            if (!$siswa) {
                return response()->json([
                    'success' => false,
                    'message' => 'Data siswa tidak ditemukan'
                ], 404);
            }

            return response()->json([
                'success' => true,
                'message' => 'Data siswa berhasil diambil',
                'data' => $siswa
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
     * Update the specified siswa
     */
    public function update(Request $request, $id)
    {
        try {
            $siswa = Siswa::find($id);

            if (!$siswa) {
                return response()->json([
                    'success' => false,
                    'message' => 'Data siswa tidak ditemukan'
                ], 404);
            }

            $validator = Validator::make($request->all(), [
                'nis' => 'required|string|max:20|unique:siswas,nis,' . $id,
                'nama' => 'required|string|max:100',
                'jenis_kelamin' => 'required|in:L,P',
                'tempat_lahir' => 'nullable|string|max:100',
                'tanggal_lahir' => 'nullable|date',
                'alamat' => 'nullable|string',
                'telepon' => 'nullable|string|max:20',
                'email' => 'nullable|email|max:100',
                'kelas_id' => 'nullable|exists:kelas,id',
                'user_id' => 'nullable|exists:users,id',
                'status' => 'nullable|in:aktif,non-aktif,lulus,pindah',
            ]);

            if ($validator->fails()) {
                return response()->json([
                    'success' => false,
                    'message' => 'Validasi gagal',
                    'errors' => $validator->errors()
                ], 422);
            }

            $siswa->update($request->all());
            $siswa->load(['kelas', 'user']);

            return response()->json([
                'success' => true,
                'message' => 'Data siswa berhasil diupdate',
                'data' => $siswa
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
     * Remove the specified siswa
     */
    public function destroy($id)
    {
        try {
            $siswa = Siswa::find($id);

            if (!$siswa) {
                return response()->json([
                    'success' => false,
                    'message' => 'Data siswa tidak ditemukan'
                ], 404);
            }

            $siswa->delete();

            return response()->json([
                'success' => true,
                'message' => 'Data siswa berhasil dihapus'
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
