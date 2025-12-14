<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Validation\ValidationException;

class AuthController extends Controller
{
    /**
     * Login user dan generate token
     * 
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function login(Request $request)
    {
        // Validasi input - role sekarang OPSIONAL
        // Note: we accept either an email address or username (name) in the `email` field to keep UI backward compatibility
        $request->validate([
            'email' => 'required|string',
            'password' => 'required',
            'role' => 'nullable|in:admin,kurikulum,kepala_sekolah,siswa',
        ]);

        // Cari user berdasarkan email (jika input valid email) atau berdasarkan name (untuk username)
        $identifier = trim($request->email);
        if (filter_var($identifier, FILTER_VALIDATE_EMAIL)) {
            $user = User::where('email', $identifier)->first();
        } else {
            // fallback: search by name (username). Use case-insensitive partial match
            $user = User::where('name', 'like', "%{$identifier}%")->first();
        }

        // Cek apakah user ada dan password benar
        if (!$user || !Hash::check($request->password, $user->password)) {
            throw ValidationException::withMessages([
                'email' => ['The provided credentials are incorrect.'],
            ]);
        }

        // Jika role dikirim, cek apakah sesuai
        if ($request->has('role') && strtolower($user->role) !== strtolower($request->role)) {
            throw ValidationException::withMessages([
                'role' => ['The role does not match with your account.'],
            ]);
        }

        // Hapus token lama jika ada
        $user->tokens()->delete();

        // Buat token baru
        $token = $user->createToken('auth_token')->plainTextToken;

        // Prepare user data
        $userData = [
            'id' => $user->id,
            'name' => $user->name,
            'email' => $user->email,
            'role' => $user->role,
        ];

        // Ambil siswa_id jika role siswa
        if (strtolower($user->role) === 'siswa') {
            $siswa = \App\Models\Siswa::where('user_id', $user->id)->first();
            if ($siswa) {
                $userData['siswa_id'] = $siswa->id;
                $userData['siswa'] = [
                    'id' => $siswa->id,
                    'nis' => $siswa->nis,
                    'nama' => $siswa->nama,
                    'kelas_id' => $siswa->kelas_id,
                ];
                \Illuminate\Support\Facades\Log::info("Login siswa - user_id: {$user->id}, siswa_id: {$siswa->id}");
            } else {
                \Illuminate\Support\Facades\Log::warning("User siswa tidak memiliki data di tabel siswas - user_id: {$user->id}");
            }
        }

        // Ambil guru_id jika role guru
        if (strtolower($user->role) === 'guru') {
            $guru = \App\Models\Guru::where('user_id', $user->id)->first();
            if ($guru) {
                $userData['guru_id'] = $guru->id;
                \Illuminate\Support\Facades\Log::info("Login guru - user_id: {$user->id}, guru_id: {$guru->id}");
            }
        }

        return response()->json([
            'success' => true,
            'message' => 'Login successful',
            'data' => [
                'user' => $userData,
                'token' => $token,
                'token_type' => 'Bearer',
            ]
        ], 200);
    }

    /**
     * Logout user dan hapus token
     * 
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function logout(Request $request)
    {
        // Hapus token yang sedang digunakan
        $request->user()->currentAccessToken()->delete();

        return response()->json([
            'success' => true,
            'message' => 'Logout successful',
        ], 200);
    }

    /**
     * Check authenticated user
     * 
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function check(Request $request)
    {
        return response()->json([
            'success' => true,
            'message' => 'Authenticated',
            'data' => [
                'id' => $request->user()->id,
                'name' => $request->user()->name,
                'email' => $request->user()->email,
                'role' => $request->user()->role,
            ]
        ], 200);
    }

    /**
     * Save device token (FCM) for authenticated user
     * POST /api/device-token
     */
    public function saveDeviceToken(Request $request)
    {
        $request->validate([
            'token' => 'required|string',
        ]);

        $user = $request->user();
        $user->update(['device_token' => $request->token]);

        return response()->json([
            'success' => true,
            'message' => 'Device token saved',
        ], 200);
    }
}
