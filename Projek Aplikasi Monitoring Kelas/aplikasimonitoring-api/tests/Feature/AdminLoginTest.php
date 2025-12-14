<?php

namespace Tests\Feature;

use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;
use App\Models\User;
use Illuminate\Support\Facades\Hash;

class AdminLoginTest extends TestCase
{
    use RefreshDatabase;

    /** @test */
    public function admin_can_login_with_admin123_credentials()
    {
        $admin = User::create([
            'name' => 'Administrator',
            'email' => 'admin@monitoring.com',
            'password' => Hash::make('admin123'),
            'role' => 'admin',
        ]);

        // Only disable CSRF middleware for test POST to admin web route to keep session middleware enabled
        $this->withoutMiddleware(\App\Http\Middleware\VerifyCsrfToken::class);

        // Ensure exceptions are displayed for debugging
        $this->app['config']->set('app.debug', true);

        $response = $this->post(route('admin.login.post'), [
            'email' => 'admin@monitoring.com',
            'password' => 'admin123',
        ]);

        // Debug output
        fwrite(STDOUT, "ADMIN LOGIN RESPONSE: " . $response->getContent() . PHP_EOL);

        $response->assertRedirect(route('admin.dashboard'));
        $this->assertAuthenticatedAs($admin);
    }
}
