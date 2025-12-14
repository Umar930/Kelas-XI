<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;

class UserController extends Controller
{
    /**
     * Display a listing of users.
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function index(Request $request)
    {
        $query = User::query();

        // Search
        if ($request->has('search')) {
            $search = $request->search;
            $query->where(function($q) use ($search) {
                $q->where('name', 'like', "%{$search}%")
                  ->orWhere('email', 'like', "%{$search}%");
            });
        }

        // Filter by role
        if ($request->has('role')) {
            $query->where('role', $request->role);
        }

        // Order by
        $query->orderBy('created_at', 'desc');

        // Check if request wants all data (no pagination)
        if ($request->get('all') === 'true' || $request->get('per_page') === 'all') {
            $users = $query->get();
            
            return response()->json([
                'success' => true,
                'message' => 'Data users berhasil diambil',
                'data' => $users,
                'pagination' => [
                    'current_page' => 1,
                    'last_page' => 1,
                    'per_page' => $users->count(),
                    'total' => $users->count(),
                ]
            ], 200);
        }

        // Pagination (default 10, max 100)
        $perPage = min((int)$request->get('per_page', 10), 100);
        $users = $query->paginate($perPage);

        return response()->json([
            'success' => true,
            'message' => 'Data users berhasil diambil',
            'data' => $users->items(),
            'pagination' => [
                'current_page' => $users->currentPage(),
                'last_page' => $users->lastPage(),
                'per_page' => $users->perPage(),
                'total' => $users->total(),
                'from' => $users->firstItem(),
                'to' => $users->lastItem(),
            ]
        ], 200);
    }

    /**
     * Store a newly created user.
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function store(Request $request)
    {
        // Log request data untuk debugging
        \Log::info('UserController@store - Request data:', $request->all());
        
        // Support both 'name' and 'nama' from frontend
        $requestData = $request->all();
        if (isset($requestData['nama']) && !isset($requestData['name'])) {
            $requestData['name'] = $requestData['nama'];
        }
        
        $validator = Validator::make($requestData, [
            'name' => 'required|string|max:255',
            'email' => 'required|email|unique:users,email',
            'password' => 'required|string|min:6',
            'role' => 'required|in:admin,kurikulum,kepala_sekolah,siswa',
        ]);

        if ($validator->fails()) {
            \Log::warning('UserController@store - Validation failed:', [
                'errors' => $validator->errors()->toArray(),
                'input' => $requestData
            ]);
            
            // Get first error message for better UX
            $firstError = $validator->errors()->first();
            
            return response()->json([
                'success' => false,
                'message' => $firstError, // Show specific error instead of generic "Validasi gagal"
                'errors' => $validator->errors(),
                'data' => null
            ], 422);
        }

        try {
            $user = User::create([
                'name' => $requestData['name'],
                'email' => $requestData['email'],
                'password' => Hash::make($requestData['password']),
                'role' => $requestData['role'],
                'email_verified_at' => now(),
            ]);

            \Log::info('UserController@store - User created successfully', ['id' => $user->id]);

            return response()->json([
                'success' => true,
                'message' => 'User berhasil dibuat',
                'data' => $user
            ], 201);
            
        } catch (\Exception $e) {
            \Log::error('UserController@store - Error:', [
                'message' => $e->getMessage(),
                'file' => $e->getFile(),
                'line' => $e->getLine()
            ]);
            
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan: ' . $e->getMessage(),
                'data' => null
            ], 500);
        }
    }

    /**
     * Display the specified user.
     *
     * @param int $id
     * @return \Illuminate\Http\JsonResponse
     */
    public function show($id)
    {
        $user = User::find($id);

        if (!$user) {
            return response()->json([
                'success' => false,
                'message' => 'User tidak ditemukan'
            ], 404);
        }

        return response()->json([
            'success' => true,
            'message' => 'Data user berhasil diambil',
            'data' => $user
        ], 200);
    }

    /**
     * Update the specified user.
     *
     * @param Request $request
     * @param int $id
     * @return \Illuminate\Http\JsonResponse
     */
    public function update(Request $request, $id)
    {
        $user = User::find($id);

        if (!$user) {
            return response()->json([
                'success' => false,
                'message' => 'User tidak ditemukan',
                'data' => null
            ], 404);
        }

        // Support both 'name' and 'nama' from frontend
        $requestData = $request->all();
        if (isset($requestData['nama']) && !isset($requestData['name'])) {
            $requestData['name'] = $requestData['nama'];
        }

        $validator = Validator::make($requestData, [
            'name' => 'sometimes|required|string|max:255',
            'email' => 'sometimes|required|email|unique:users,email,' . $id,
            'password' => 'nullable|string|min:6',
            'role' => 'sometimes|required|in:admin,kurikulum,kepala_sekolah,siswa',
        ]);

        if ($validator->fails()) {
            \Log::warning('UserController@update - Validation failed:', [
                'errors' => $validator->errors()->toArray(),
                'input' => $requestData
            ]);
            
            // Get first error message for better UX
            $firstError = $validator->errors()->first();
            
            return response()->json([
                'success' => false,
                'message' => $firstError,
                'errors' => $validator->errors(),
                'data' => null
            ], 422);
        }

        try {
            // Update data
            if (isset($requestData['name'])) {
                $user->name = $requestData['name'];
            }
            if (isset($requestData['email'])) {
                $user->email = $requestData['email'];
            }
            if (isset($requestData['password']) && $requestData['password']) {
                $user->password = Hash::make($requestData['password']);
            }
            if (isset($requestData['role'])) {
                $user->role = $requestData['role'];
            }

            $user->save();

            return response()->json([
                'success' => true,
                'message' => 'User berhasil diupdate',
                'data' => $user
            ], 200);
            
        } catch (\Exception $e) {
            \Log::error('UserController@update - Error:', [
                'message' => $e->getMessage()
            ]);
            
            return response()->json([
                'success' => false,
                'message' => 'Terjadi kesalahan: ' . $e->getMessage(),
                'data' => null
            ], 500);
        }
    }

    /**
     * Remove the specified user.
     *
     * @param int $id
     * @return \Illuminate\Http\JsonResponse
     */
    public function destroy($id)
    {
        $user = User::find($id);

        if (!$user) {
            return response()->json([
                'success' => false,
                'message' => 'User tidak ditemukan'
            ], 404);
        }

        // Prevent deleting own account
        if ($user->id === auth()->id()) {
            return response()->json([
                'success' => false,
                'message' => 'Tidak dapat menghapus akun sendiri'
            ], 403);
        }

        // Delete user
        $user->delete();

        return response()->json([
            'success' => true,
            'message' => 'User berhasil dihapus'
        ], 200);
    }

    /**
     * Get all guru users.
     *
     * @return \Illuminate\Http\JsonResponse
     */
    public function guru()
    {
        $gurus = User::where('role', 'guru')
            ->orderBy('name', 'asc')
            ->get();

        return response()->json([
            'success' => true,
            'message' => 'Data guru berhasil diambil',
            'data' => $gurus
        ], 200);
    }

    /**
     * Get all siswa users.
     *
     * @return \Illuminate\Http\JsonResponse
     */
    public function siswa()
    {
        $siswas = User::where('role', 'siswa')
            ->orderBy('name', 'asc')
            ->get();

        return response()->json([
            'success' => true,
            'message' => 'Data siswa berhasil diambil',
            'data' => $siswas
        ], 200);
    }
}
