<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class AuthController extends Controller
{
    /**
     * Show login form
     */
    public function showLogin()
    {
        if (Auth::check() && Auth::user()->role === 'admin') {
            return redirect()->route('admin.dashboard');
        }
        return view('admin.auth.login');
    }

    /**
     * Handle login
     */
    public function login(Request $request)
    {
        $credentials = $request->validate([
            'email' => 'required|email',
            'password' => 'required',
        ]);
        // Normalize email (trim + lowercase) to avoid login fails due to whitespace/casing
        $credentials['email'] = strtolower(trim($credentials['email']));

        if (Auth::attempt($credentials)) {
            $user = Auth::user();
            
            // Check if user is admin
            if ($user->role !== 'admin') {
                Auth::logout();
                return back()->withErrors([
                    'email' => 'Anda tidak memiliki akses ke admin panel.',
                ])->withInput($request->only('email'));
            }

            $request->session()->regenerate();
            return redirect()->intended(route('admin.dashboard'));
        }

        // Log more details for debugging in dev/local environment
        try {
            $user = \App\Models\User::where('email', $credentials['email'])->first();
            if ($user) {
                // Record a short log: user exists but password mismatch
                \Log::warning('Login gagal: user ditemukan namun password tidak cocok', [
                    'email' => $credentials['email'],
                    'user_id' => $user->id,
                    'role' => $user->role,
                ]);
            } else {
                \Log::info('Login gagal: tidak ditemukan user dengan email ini', ['email' => $credentials['email']]);
            }
        } catch (\Exception $e) {
            // don't break login flow if logging fails
            \Log::error('Gagal menulis log login gagal: ' . $e->getMessage());
        }

        return back()->withErrors([
            'email' => 'Email atau password salah.',
        ])->withInput($request->only('email'));
    }

    /**
     * Handle logout
     */
    public function logout(Request $request)
    {
        Auth::logout();
        $request->session()->invalidate();
        $request->session()->regenerateToken();
        return redirect()->route('admin.login');
    }
}
