<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Mapel;
use Illuminate\Http\Request;
use Illuminate\Validation\ValidationException;

class MapelController extends Controller
{
    /**
     * Display a listing of the resource.
     * GET /api/mapels
     */
    public function index()
    {
        $mapels = Mapel::all();

        return response()->json([
            'success' => true,
            'message' => 'Data mapel berhasil diambil',
            'data' => $mapels
        ], 200);
    }

    /**
     * Store a newly created resource in storage.
     * POST /api/mapels
     */
    public function store(Request $request)
    {
        try {
            // Validasi input
            $validated = $request->validate([
                'kode_mapel' => 'required|string|unique:mapels,kode_mapel',
                'mapel' => 'required|string|max:255',
            ]);

            // Buat data mapel baru
            $mapel = Mapel::create($validated);

            return response()->json([
                'success' => true,
                'message' => 'Data mapel berhasil ditambahkan',
                'data' => $mapel
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
     * GET /api/mapels/{id}
     */
    public function show(string $id)
    {
        try {
            $mapel = Mapel::findOrFail($id);

            return response()->json([
                'success' => true,
                'message' => 'Data mapel berhasil diambil',
                'data' => $mapel
            ], 200);

        } catch (\Illuminate\Database\Eloquent\ModelNotFoundException $e) {
            return response()->json([
                'success' => false,
                'message' => 'Data mapel tidak ditemukan',
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
     * PUT/PATCH /api/mapels/{id}
     */
    public function update(Request $request, string $id)
    {
        try {
            $mapel = Mapel::findOrFail($id);

            // Validasi input
            $validated = $request->validate([
                'kode_mapel' => 'required|string|unique:mapels,kode_mapel,' . $id,
                'mapel' => 'required|string|max:255',
            ]);

            // Update data mapel
            $mapel->update($validated);

            return response()->json([
                'success' => true,
                'message' => 'Data mapel berhasil diupdate',
                'data' => $mapel
            ], 200);

        } catch (\Illuminate\Database\Eloquent\ModelNotFoundException $e) {
            return response()->json([
                'success' => false,
                'message' => 'Data mapel tidak ditemukan',
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
     * DELETE /api/mapels/{id}
     */
    public function destroy(string $id)
    {
        try {
            $mapel = Mapel::findOrFail($id);
            $mapel->delete();

            return response()->json([
                'success' => true,
                'message' => 'Data mapel berhasil dihapus',
            ], 200);

        } catch (\Illuminate\Database\Eloquent\ModelNotFoundException $e) {
            return response()->json([
                'success' => false,
                'message' => 'Data mapel tidak ditemukan',
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
