<?php

namespace Tests\Feature;

use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;
use App\Models\User;
use Illuminate\Support\Facades\Hash;

class AdminDebugTest extends TestCase
{
    use RefreshDatabase;

    public function test_reset_admin_password_route_resets_password_when_debug_enabled()
    {
        // ensure debug mode enabled for this test
        config(['app.debug' => true]);

        $admin = User::factory()->create([
            'email' => 'admin@monitoring.com',
            'password' => Hash::make('some-old-password'),
        ]);

        $response = $this->post('/admin/debug/reset-admin-password');

        $response->assertStatus(200);
        $response->assertJson(['success' => true]);

        $admin->refresh();
        $this->assertTrue(Hash::check('admin123', $admin->password));
    }

    public function test_reset_admin_password_returns_404_when_no_admin()
    {
        config(['app.debug' => true]);

        $response = $this->post('/admin/debug/reset-admin-password');

        $response->assertStatus(404);
        $response->assertJson(['success' => false]);
    }
}
