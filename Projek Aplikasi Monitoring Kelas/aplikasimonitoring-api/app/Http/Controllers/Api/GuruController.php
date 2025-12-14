<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Guru;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Validator;

class GuruController extends Controller
{
    /**
     * Display a listing of the resource.
     * GET /api/gurus
     */
    public function index(Request $request)
    {
        Log::info('API: Fetching gurus', ['params' => $request->all()]);

        try {
            $query = Guru::query();

            // Search
            if ($request->has('search')) {
                $search = $request->search;
                $query->where(function($q) use ($search) {
                    $q->where('nama', 'like', "%{$search}%")
                      ->orWhere('kode_guru', 'like', "%{$search}%")
                      ->orWhere('email', 'like', "%{$search}%");
                });
            }

            // Filter by mata_pelajaran
            if ($request->has('mata_pelajaran')) {
                $query->where('mata_pelajaran', 'like', "%{$request->mata_pelajaran}%");
            }

            // Order by - field nama guru adalah 'guru' bukan 'nama'
            $query->orderByRaw("COALESCE(guru, nama) ASC");

            // Check if request wants all data (no pagination)
            if ($request->get('all') === 'true' || $request->get('per_page') === 'all') {
                $gurus = $query->get();
                
                Log::info('API: Found ' . $gurus->count() . ' gurus (all)');
                
                return response()->json([
                    'success' => true,
                    'message' => 'Data guru berhasil diambil',
                    'data' => $gurus,
                    'pagination' => [
                        'current_page' => 1,
                        'last_page' => 1,
                        'per_page' => $gurus->count(),
                        'total' => $gurus->count(),
                    ]
                ], 200);
            }

            // Pagination (default 10, max 100)
            $perPage = min((int)$request->get('per_page', 10), 100);
            $gurus = $query->paginate($perPage);

            Log::info('API: Found ' . $gurus->total() . ' gurus');

            return response()->json([
                'success' => true,
                'message' => 'Data guru berhasil diambil',
                'data' => $gurus->items(),
                'pagination' => [
                    'current_page' => $gurus->currentPage(),
                    'last_page' => $gurus->lastPage(),
                    'per_page' => $gurus->perPage(),
                    'total' => $gurus->total(),
                    'from' => $gurus->firstItem(),
                    'to' => $gurus->lastItem(),
                ]
            ], 200);

        } catch (\Exception $e) {
            Log::error('API: Error fetching gurus', [
                'error' => $e->getMessage(),
                'file' => $e->getFile(),
                'line' => $e->getLine()
            ]);

            return response()->json([
                'success' => false,
                'message' => 'Gagal mengambil data guru: ' . $e->getMessage(),
                'data' => []
            ], 500);
        }
    }

    /**
     * Store a newly created resource in storage.
     * POST /api/gurus
     */
    public function store(Request $request)
    {
        Log::info('API: Creating new guru', $request->all());

        // Validasi input
        $validator = Validator::make($request->all(), [
            'kode_guru' => 'required|string|max:50|unique:gurus,kode_guru',
            'nama' => 'required|string|max:255',
            'email' => 'nullable|email|max:255',
            'no_telepon' => 'nullable|string|max:20',
            'mata_pelajaran' => 'nullable|string|max:100',
            'alamat' => 'nullable|string|max:500',
        ]);

        if ($validator->fails()) {
            Log::warning('API: Validation failed for guru creation', $validator->errors()->toArray());

            return response()->json([
                'success' => false,
                'message' => 'Validasi gagal',
                'errors' => $validator->errors(),
                'data' => null
            ], 422);
        }

        try {
            $guru = Guru::create($validator->validated());

            Log::info('API: Guru created successfully', ['id' => $guru->id]);

            return response()->json([
                'success' => true,
                'message' => 'Guru berhasil ditambahkan',
                'data' => $guru
            ], 201);

        } catch (\Exception $e) {
            Log::error('API: Error creating guru', [
                'error' => $e->getMessage(),
                'data' => $request->all()
            ]);

            return response()->json([
                'success' => false,
                'message' => 'Gagal menambahkan guru: ' . $e->getMessage(),
                'data' => null
            ], 500);
        }
    }

    /**
     * Display the specified resource.
     * GET /api/gurus/{id}
     */
    public function show($id)
    {
        Log::info('API: Fetching guru', ['id' => $id]);

        try {
            $guru = Guru::findOrFail($id);

            return response()->json([
                'success' => true,
                'message' => 'Data guru ditemukan',
                'data' => [
                    'id' => $guru->id,
                    'kode_guru' => $guru->kode_guru ?? '',
                    'nama' => $guru->nama ?? 'Tidak ada nama',
                    'email' => $guru->email,
                    'no_telepon' => $guru->no_telepon,
                    'mata_pelajaran' => $guru->mata_pelajaran,
                    'alamat' => $guru->alamat,
                    'created_at' => $guru->created_at?->toISOString(),
                    'updated_at' => $guru->updated_at?->toISOString(),
                ]
            ], 200);

        } catch (\Illuminate\Database\Eloquent\ModelNotFoundException $e) {
            Log::warning('API: Guru not found', ['id' => $id]);

            return response()->json([
                'success' => false,
                'message' => 'Guru dengan ID ' . $id . ' tidak ditemukan',
                'data' => null
            ], 404);

        } catch (\Exception $e) {
            Log::error('API: Error fetching guru', [
                'id' => $id,
                'error' => $e->getMessage()
            ]);

            return response()->json([
                'success' => false,
                'message' => 'Gagal mengambil data guru',
                'data' => null
            ], 500);
        }
    }

    /**
     * Update the specified resource in storage.
     * PUT/PATCH /api/gurus/{id}
     */
    public function update(Request $request, $id)
    {
        Log::info('API: Updating guru', ['id' => $id, 'data' => $request->all()]);

        // Validasi input
        $validator = Validator::make($request->all(), [
            'kode_guru' => 'sometimes|required|string|max:50|unique:gurus,kode_guru,' . $id,
            'nama' => 'sometimes|required|string|max:255',
            'email' => 'nullable|email|max:255',
            'no_telepon' => 'nullable|string|max:20',
            'mata_pelajaran' => 'nullable|string|max:100',
            'alamat' => 'nullable|string|max:500',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Validasi gagal',
                'errors' => $validator->errors(),
                'data' => null
            ], 422);
        }

        try {
            $guru = Guru::findOrFail($id);
            $guru->update($validator->validated());

            Log::info('API: Guru updated successfully', ['id' => $id]);

            return response()->json([
                'success' => true,
                'message' => 'Data guru berhasil diperbarui',
                'data' => $guru->fresh()
            ], 200);

        } catch (\Illuminate\Database\Eloquent\ModelNotFoundException $e) {
            return response()->json([
                'success' => false,
                'message' => 'Guru dengan ID ' . $id . ' tidak ditemukan',
                'data' => null
            ], 404);

        } catch (\Exception $e) {
            Log::error('API: Error updating guru', [
                'id' => $id,
                'error' => $e->getMessage()
            ]);

            return response()->json([
                'success' => false,
                'message' => 'Gagal memperbarui data guru',
                'data' => null
            ], 500);
        }
    }

    /**
     * Remove the specified resource from storage.
     * DELETE /api/gurus/{id}
     */
    public function destroy($id)
    {
        Log::info('API: Deleting guru', ['id' => $id]);

        try {
            $guru = Guru::findOrFail($id);
            $guruName = $guru->nama;
            $guru->delete();

            Log::info('API: Guru deleted successfully', ['id' => $id, 'name' => $guruName]);

            return response()->json([
                'success' => true,
                'message' => 'Guru "' . $guruName . '" berhasil dihapus',
                'data' => null
            ], 200);

        } catch (\Illuminate\Database\Eloquent\ModelNotFoundException $e) {
            return response()->json([
                'success' => false,
                'message' => 'Guru dengan ID ' . $id . ' tidak ditemukan',
                'data' => null
            ], 404);

        } catch (\Exception $e) {
            Log::error('API: Error deleting guru', [
                'id' => $id,
                'error' => $e->getMessage()
            ]);

            return response()->json([
                'success' => false,
                'message' => 'Gagal menghapus data guru',
                'data' => null
            ], 500);
        }
    }
}
