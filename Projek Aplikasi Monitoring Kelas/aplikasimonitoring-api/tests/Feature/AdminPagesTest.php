<?php

namespace Tests\Feature;

use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;
use App\Models\User;
use Illuminate\Support\Facades\Hash;

class AdminPagesTest extends TestCase
{
    use RefreshDatabase;

    public function setUp(): void
    {
        parent::setUp();
        // seed minimal data
        $this->artisan('db:seed');
    }

    public function test_kelas_page_loads_for_admin()
    {
        $admin = User::where('role', 'admin')->first();
        $this->actingAs($admin);

        $response = $this->get(route('admin.kelas.index'));
        $response->assertStatus(200);
        $response->assertSee('Daftar Kelas');
    }

    public function test_guru_mengajar_page_loads_for_admin()
    {
        $admin = User::where('role', 'admin')->first();
        $this->actingAs($admin);

        $response = $this->get(route('admin.guru-mengajar.index'));
        $response->assertStatus(200);
        $response->assertSee('Daftar Guru Mengajar');
    }

    public function test_gurus_page_shows_seeded_guru()
    {
        $admin = User::where('role', 'admin')->first();
        $this->actingAs($admin);

        $response = $this->get(route('admin.gurus.index'));
        $response->assertStatus(200);
        $response->assertSee('Budi Santoso');
    }
}
