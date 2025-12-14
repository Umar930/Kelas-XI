<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Kelas;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class KelasController extends Controller
{
    /**
     * Display a listing of kelas with pagination, search, and filter
     */
    public function index(Request $request)
    {
        try {
            $search = $request->input('search');
            $tingkat = $request->input('tingkat');
            $jurusan = $request->input('jurusan');

            $query = Kelas::with('guru');

            // Search by kode_kelas or nama_kelas
            if ($search) {
                $query->where(function ($q) use ($search) {
                    $q->where('kode_kelas', 'like', "%{$search}%")
                      ->orWhere('nama_kelas', 'like', "%{$search}%");
                });
            }

            // Filter by tingkat
            if ($tingkat) {
                $query->where('tingkat', $tingkat);
            }

            // Filter by jurusan
            if ($jurusan) {
                $query->where('jurusan', 'like', "%{$jurusan}%");
            }

            // Order by
            $query->orderBy('kode_kelas', 'asc');

            // Check if request wants all data (no pagination)
            if ($request->get('all') === 'true' || $request->get('per_page') === 'all') {
                $kelas = $query->get();
                
                return response()->json([
                    'success' => true,
                    'message' => 'Data kelas berhasil diambil',
                    'data' => $kelas,
                    'pagination' => [
                        'current_page' => 1,
                        'last_page' => 1,
                        'per_page' => $kelas->count(),
                        'total' => $kelas->count(),
                    ]
                ], 200);
            }

            // Pagination (default 10, max 100)
            $perPage = min((int)$request->get('per_page', 10), 100);
            $kelas = $query->paginate($perPage);

            return response()->json([
                'success' => true,
                'message' => 'Data kelas berhasil diambil',
                'data' => $kelas->items(),
                'pagination' => [
                    'total' => $kelas->total(),
                    'per_page' => $kelas->perPage(),
                    'current_page' => $kelas->currentPage(),
                    'last_page' => $kelas->lastPage(),
                    'from' => $kelas->firstItem(),
                    'to' => $kelas->lastItem(),
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
     * Store a newly created kelas
     */
    public function store(Request $request)
    {
        try {
            $validator = Validator::make($request->all(), [
                'kode_kelas' => 'required|string|unique:kelas,kode_kelas|max:50',
                'nama_kelas' => 'required|string|max:100',
                'guru_id' => 'nullable|exists:gurus,id',
                'tingkat' => 'required|string|max:10',
                'jurusan' => 'required|string|max:50',
                'kapasitas' => 'nullable|integer|min:1',
            ]);

            if ($validator->fails()) {
                return response()->json([
                    'success' => false,
                    'message' => 'Validasi gagal',
                    'errors' => $validator->errors()
                ], 422);
            }

            $kelas = Kelas::create($request->all());
            $kelas->load('guru');

            return response()->json([
                'success' => true,
                'message' => 'Data kelas berhasil ditambahkan',
                'data' => $kelas
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
     * Display the specified kelas
     */
    public function show($id)
    {
        try {
            $kelas = Kelas::with(['guru', 'siswas'])->find($id);

            if (!$kelas) {
                return response()->json([
                    'success' => false,
                    'message' => 'Data kelas tidak ditemukan'
                ], 404);
            }

            return response()->json([
                'success' => true,
                'message' => 'Data kelas berhasil diambil',
                'data' => $kelas
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
     * Update the specified kelas
     */
    public function update(Request $request, $id)
    {
        try {
            $kelas = Kelas::find($id);

            if (!$kelas) {
                return response()->json([
                    'success' => false,
                    'message' => 'Data kelas tidak ditemukan'
                ], 404);
            }

            $validator = Validator::make($request->all(), [
                'kode_kelas' => 'required|string|max:50|unique:kelas,kode_kelas,' . $id,
                'nama_kelas' => 'required|string|max:100',
                'guru_id' => 'nullable|exists:gurus,id',
                'tingkat' => 'required|string|max:10',
                'jurusan' => 'required|string|max:50',
                'kapasitas' => 'nullable|integer|min:1',
            ]);

            if ($validator->fails()) {
                return response()->json([
                    'success' => false,
                    'message' => 'Validasi gagal',
                    'errors' => $validator->errors()
                ], 422);
            }

            $kelas->update($request->all());
            $kelas->load('guru');

            return response()->json([
                'success' => true,
                'message' => 'Data kelas berhasil diupdate',
                'data' => $kelas
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
     * Remove the specified kelas
     */
    public function destroy($id)
    {
        try {
            $kelas = Kelas::find($id);

            if (!$kelas) {
                return response()->json([
                    'success' => false,
                    'message' => 'Data kelas tidak ditemukan'
                ], 404);
            }

            $kelas->delete();

            return response()->json([
                'success' => true,
                'message' => 'Data kelas berhasil dihapus'
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
