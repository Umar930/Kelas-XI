<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;
use App\Models\User;

class DebugController extends Controller
{
    /**
     * Local-only debug endpoint for checking DB and admin user status
     * GET /admin/debug/user-info
     */
    public function userInfo(Request $request)
    {
        if (!config('app.debug')) {
            abort(404);
        }

        $out = ['db' => ['connected' => false, 'error' => null], 'user' => null];

        // Check DB connection
        try {
            DB::connection()->getPdo();
            $out['db']['connected'] = true;
        } catch (\Exception $e) {
            $out['db']['error'] = $e->getMessage();
        }

        // Return information about the admin user if present
        try {
            $user = User::where('email', 'admin@monitoring.com')->first();
            if ($user) {
                $out['user'] = [
                    'exists' => true,
                    'id' => $user->id,
                    'email' => $user->email,
                    'role' => $user->role,
                    'password_is_admin123' => Hash::check('admin123', $user->password),
                ];
            } else {
                $out['user'] = ['exists' => false];
            }
        } catch (\Exception $e) {
            $out['user'] = ['error' => $e->getMessage()];
        }

        return response()->json($out);
    }

    /**
     * Local-only endpoint to reset admin password to 'admin123'
     * POST /admin/debug/reset-admin-password
     */
    public function resetAdminPassword(Request $request)
    {
        if (!config('app.debug')) {
            abort(404);
        }

        $user = User::where('email', 'admin@monitoring.com')->first();
        if (!$user) {
            return response()->json(['success' => false, 'message' => 'Admin user not found'], 404);
        }

        $user->password = Hash::make('admin123');
        $user->save();

        return response()->json(['success' => true, 'message' => 'Admin password reset to admin123 and saved']);
    }
}
