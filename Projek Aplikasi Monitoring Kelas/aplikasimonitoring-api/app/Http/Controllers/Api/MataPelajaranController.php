<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Mapel;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class MataPelajaranController extends Controller
{
    /**
     * Display a listing of mata pelajaran.
     */
    public function index(Request $request)
    {
        $query = Mapel::query();

        // Search
        if ($request->has('search')) {
            $search = $request->search;
            $query->where(function($q) use ($search) {
                $q->where('mapel', 'like', "%{$search}%")
                  ->orWhere('kode_mapel', 'like', "%{$search}%");
            });
        }

        // Order by
        $query->orderBy('mapel', 'asc');

        // Check if request wants all data (no pagination)
        if ($request->get('all') === 'true' || $request->get('per_page') === 'all') {
            $mataPelajaran = $query->get();
            
            return response()->json([
                'success' => true,
                'message' => 'Data mata pelajaran berhasil diambil',
                'data' => $mataPelajaran,
                'pagination' => [
                    'current_page' => 1,
                    'last_page' => 1,
                    'per_page' => $mataPelajaran->count(),
                    'total' => $mataPelajaran->count(),
                ]
            ], 200);
        }

        // Pagination (default 10, max 100)
        $perPage = min((int)$request->get('per_page', 10), 100);
        $mataPelajaran = $query->paginate($perPage);

        return response()->json([
            'success' => true,
            'message' => 'Data mata pelajaran berhasil diambil',
            'data' => $mataPelajaran->items(),
            'pagination' => [
                'current_page' => $mataPelajaran->currentPage(),
                'last_page' => $mataPelajaran->lastPage(),
                'per_page' => $mataPelajaran->perPage(),
                'total' => $mataPelajaran->total(),
                'from' => $mataPelajaran->firstItem(),
                'to' => $mataPelajaran->lastItem(),
            ]
        ], 200);
    }

    /**
     * Store a newly created mata pelajaran.
     */
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'kode_mapel' => 'required|string|max:20|unique:mapels,kode_mapel',
            'mapel' => 'required|string|max:255',
            'kategori' => 'nullable|string|in:Wajib,Peminatan,Muatan Lokal',
            'jam_pelajaran' => 'nullable|integer|min:1',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Validation error',
                'errors' => $validator->errors()
            ], 422);
        }

        $mataPelajaran = Mapel::create([
            'kode_mapel' => $request->kode_mapel,
            'mapel' => $request->mapel,
            'kategori' => $request->kategori,
            'jam_pelajaran' => $request->jam_pelajaran,
        ]);

        return response()->json([
            'success' => true,
            'message' => 'Mata pelajaran berhasil dibuat',
            'data' => $mataPelajaran
        ], 201);
    }

    /**
     * Display the specified mata pelajaran.
     */
    public function show(string $id)
    {
        $mataPelajaran = Mapel::find($id);

        if (!$mataPelajaran) {
            return response()->json([
                'success' => false,
                'message' => 'Mata pelajaran tidak ditemukan'
            ], 404);
        }

        return response()->json([
            'success' => true,
            'message' => 'Data mata pelajaran berhasil diambil',
            'data' => $mataPelajaran
        ], 200);
    }

    /**
     * Update the specified mata pelajaran.
     */
    public function update(Request $request, string $id)
    {
        $mataPelajaran = Mapel::find($id);

        if (!$mataPelajaran) {
            return response()->json([
                'success' => false,
                'message' => 'Mata pelajaran tidak ditemukan'
            ], 404);
        }

        $validator = Validator::make($request->all(), [
            'kode_mapel' => 'sometimes|required|string|max:20|unique:mapels,kode_mapel,' . $id,
            'mapel' => 'sometimes|required|string|max:255',
            'kategori' => 'nullable|string|in:Wajib,Peminatan,Muatan Lokal',
            'jam_pelajaran' => 'nullable|integer|min:1',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Validation error',
                'errors' => $validator->errors()
            ], 422);
        }

        $mataPelajaran->update($request->only(['kode_mapel', 'mapel', 'kategori', 'jam_pelajaran']));

        return response()->json([
            'success' => true,
            'message' => 'Mata pelajaran berhasil diupdate',
            'data' => $mataPelajaran
        ], 200);
    }

    /**
     * Remove the specified mata pelajaran.
     */
    public function destroy(string $id)
    {
        $mataPelajaran = Mapel::find($id);

        if (!$mataPelajaran) {
            return response()->json([
                'success' => false,
                'message' => 'Mata pelajaran tidak ditemukan'
            ], 404);
        }

        $mataPelajaran->delete();

        return response()->json([
            'success' => true,
            'message' => 'Mata pelajaran berhasil dihapus'
        ], 200);
    }
}
