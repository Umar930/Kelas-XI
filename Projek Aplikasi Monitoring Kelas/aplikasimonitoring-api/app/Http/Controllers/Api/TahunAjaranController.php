<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\TahunAjaran;
use Illuminate\Http\Request;
use Illuminate\Validation\ValidationException;

class TahunAjaranController extends Controller
{
    /**
     * Display a listing of the resource.
     * GET /api/tahun-ajarans
     */
    public function index(Request $request)
    {
        $query = TahunAjaran::query();

        // Search
        if ($request->has('search')) {
            $search = $request->search;
            $query->where('tahun', 'like', "%{$search}%");
        }

        // Filter by flag (active/inactive)
        if ($request->has('flag')) {
            $query->where('flag', $request->flag);
        }

        // Order by
        $query->orderBy('tahun', 'desc');

        // Check if request wants all data (no pagination)
        if ($request->get('all') === 'true' || $request->get('per_page') === 'all') {
            $tahunAjarans = $query->get();
            
            return response()->json([
                'success' => true,
                'message' => 'Data tahun ajaran berhasil diambil',
                'data' => $tahunAjarans,
                'pagination' => [
                    'current_page' => 1,
                    'last_page' => 1,
                    'per_page' => $tahunAjarans->count(),
                    'total' => $tahunAjarans->count(),
                ]
            ], 200);
        }

        // Pagination (default 10, max 100)
        $perPage = min((int)$request->get('per_page', 10), 100);
        $tahunAjarans = $query->paginate($perPage);

        return response()->json([
            'success' => true,
            'message' => 'Data tahun ajaran berhasil diambil',
            'data' => $tahunAjarans->items(),
            'pagination' => [
                'current_page' => $tahunAjarans->currentPage(),
                'last_page' => $tahunAjarans->lastPage(),
                'per_page' => $tahunAjarans->perPage(),
                'total' => $tahunAjarans->total(),
                'from' => $tahunAjarans->firstItem(),
                'to' => $tahunAjarans->lastItem(),
            ]
        ], 200);
    }

    /**
     * Store a newly created resource in storage.
     * POST /api/tahun-ajarans
     */
    public function store(Request $request)
    {
        try {
            // Validasi input
            $validated = $request->validate([
                'tahun' => 'required|string|max:255',
                'flag' => 'required|boolean',
            ]);

            // Jika flag = 1 (aktif), set semua tahun ajaran lain menjadi tidak aktif
            if ($validated['flag'] == 1) {
                TahunAjaran::where('flag', 1)->update(['flag' => 0]);
            }

            // Buat data tahun ajaran baru
            $tahunAjaran = TahunAjaran::create($validated);

            return response()->json([
                'success' => true,
                'message' => 'Data tahun ajaran berhasil ditambahkan',
                'data' => $tahunAjaran
            ], 201);

        } catch (ValidationException $e) {
            return response()->json([
                'success' => false,
                'message' => 'Validasi gagal',
                'errors' => $e->errors()
            ], 422);
        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * Display the specified resource.
     * GET /api/tahun-ajarans/{id}
     */
    public function show(string $id)
    {
        try {
            $tahunAjaran = TahunAjaran::findOrFail($id);

            return response()->json([
                'success' => true,
                'message' => 'Data tahun ajaran berhasil diambil',
                'data' => $tahunAjaran
            ], 200);

        } catch (\Illuminate\Database\Eloquent\ModelNotFoundException $e) {
            return response()->json([
                'success' => false,
                'message' => 'Data tahun ajaran tidak ditemukan',
            ], 404);
        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * Update the specified resource in storage.
     * PUT/PATCH /api/tahun-ajarans/{id}
     */
    public function update(Request $request, string $id)
    {
        try {
            $tahunAjaran = TahunAjaran::findOrFail($id);

            // Validasi input
            $validated = $request->validate([
                'tahun' => 'required|string|max:255',
                'flag' => 'required|boolean',
            ]);

            // Jika flag = 1 (aktif), set semua tahun ajaran lain menjadi tidak aktif
            if ($validated['flag'] == 1) {
                TahunAjaran::where('id', '!=', $id)
                    ->where('flag', 1)
                    ->update(['flag' => 0]);
            }

            // Update data tahun ajaran
            $tahunAjaran->update($validated);

            return response()->json([
                'success' => true,
                'message' => 'Data tahun ajaran berhasil diupdate',
                'data' => $tahunAjaran
            ], 200);

        } catch (\Illuminate\Database\Eloquent\ModelNotFoundException $e) {
            return response()->json([
                'success' => false,
                'message' => 'Data tahun ajaran tidak ditemukan',
            ], 404);
        } catch (ValidationException $e) {
            return response()->json([
                'success' => false,
                'message' => 'Validasi gagal',
                'errors' => $e->errors()
            ], 422);
        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * Remove the specified resource from storage.
     * DELETE /api/tahun-ajarans/{id}
     */
    public function destroy(string $id)
    {
        try {
            $tahunAjaran = TahunAjaran::findOrFail($id);
            $tahunAjaran->delete();

            return response()->json([
                'success' => true,
                'message' => 'Data tahun ajaran berhasil dihapus',
            ], 200);

        } catch (\Illuminate\Database\Eloquent\ModelNotFoundException $e) {
            return response()->json([
                'success' => false,
                'message' => 'Data tahun ajaran tidak ditemukan',
            ], 404);
        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * Get active tahun ajaran
     * GET /api/tahun-ajarans/active
     */
    public function getActive()
    {
        try {
            $tahunAjaran = TahunAjaran::where('flag', 1)->first();

            if (!$tahunAjaran) {
                return response()->json([
                    'success' => false,
                    'message' => 'Tidak ada tahun ajaran aktif',
                ], 404);
            }

            return response()->json([
                'success' => true,
                'message' => 'Data tahun ajaran aktif berhasil diambil',
                'data' => $tahunAjaran
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
     * Activate specific tahun ajaran
     * POST /api/tahun-ajarans/{id}/activate
     */
    public function activate(string $id)
    {
        try {
            $tahunAjaran = TahunAjaran::findOrFail($id);

            // Set semua tahun ajaran lain menjadi tidak aktif
            TahunAjaran::where('id', '!=', $id)
                ->where('flag', 1)
                ->update(['flag' => 0]);

            // Aktifkan tahun ajaran yang dipilih
            $tahunAjaran->flag = 1;
            $tahunAjaran->save();

            return response()->json([
                'success' => true,
                'message' => 'Tahun ajaran berhasil diaktifkan',
                'data' => $tahunAjaran
            ], 200);

        } catch (\Illuminate\Database\Eloquent\ModelNotFoundException $e) {
            return response()->json([
                'success' => false,
                'message' => 'Data tahun ajaran tidak ditemukan',
            ], 404);
        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan pada server',
                'error' => $e->getMessage()
            ], 500);
        }
    }
}
