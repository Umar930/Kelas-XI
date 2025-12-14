<?php

namespace Tests\Feature;

use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;
use App\Models\User;
use App\Models\Guru;
use App\Models\Kelas;
use App\Models\Mapel;
use App\Models\JadwalKelas;
use App\Models\KehadiranGuru;
use App\Models\GuruPengganti;
use App\Models\Siswa;

class GuruPenggantiFlowTest extends TestCase
{
    use RefreshDatabase;

    /** @test */
    public function admin_input_izin_creates_kehadiran_and_pending_guru_pengganti()
    {
        $admin = User::factory()->create(['role' => 'admin']);

        $guru = Guru::create(['kode_guru' => 'G01', 'nama' => 'Pak A']);
        $mapel = Mapel::create(['kode_mapel' => 'M01', 'mapel' => 'Matematika']);
        $kelas = Kelas::create(['kode_kelas' => 'K01', 'nama_kelas' => 'XI RPL', 'tingkat' => 'XI', 'jurusan' => 'RPL']);

        $tahun = \App\Models\TahunAjaran::create(['tahun' => '2025/2026', 'flag' => 1]);

        $jadwal = JadwalKelas::create([
            'kelas_id' => $kelas->id,
            'mapel_id' => $mapel->id,
            'guru_id' => $guru->id,
            'hari' => 'Senin',
            'jam_mulai' => '07:00',
            'jam_selesai' => '08:30',
            'tahun_ajaran_id' => $tahun->id,
        ]);

        $this->withoutMiddleware();

        $response = $this->actingAs($admin)
            ->post(route('admin.jadwals.izin-sakit', $jadwal), [
                'tanggal' => now()->toDateString(),
                'status' => 'izin',
                'keterangan' => 'Sakit mendadak',
            ]);

        fwrite(STDOUT, "ADMIN IZIN SAKIT RESPONSE: " . $response->getContent() . PHP_EOL);

        $response->assertRedirect();

        $this->assertDatabaseHas('kehadiran_gurus', [
            'jadwal_id' => $jadwal->id,
            'status' => 'izin',
            'input_by_kurikulum_id' => $admin->id,
        ]);

        $kehadiran = KehadiranGuru::where('jadwal_id', $jadwal->id)->first();
        $this->assertNotNull($kehadiran);

        $this->assertDatabaseHas('guru_penggantis', [
            'kehadiran_guru_id' => $kehadiran->id,
            'status' => 'pending',
        ]);
    }

    /** @test */
    public function student_can_request_guru_pengganti_and_kurikulum_can_assign_and_students_see_replacement_and_submit_attendance()
    {
        // Setup users and data
        $siswaUser = User::factory()->create(['role' => 'siswa']);
        $siswa = Siswa::create(['user_id' => $siswaUser->id, 'nis' => '1001', 'nama' => 'Siswa 1', 'kelas_id' => null]);

        $kurikulumUser = User::factory()->create(['role' => 'kurikulum']);

        $tahun = \App\Models\TahunAjaran::create(['tahun' => '2025/2026', 'flag' => 1]);

        $guru = Guru::create(['kode_guru' => 'G02', 'nama' => 'Bu B']);
        $guruReplacement = Guru::create(['kode_guru' => 'G99', 'nama' => 'Pak Pengganti']);
        $mapel = Mapel::create(['kode_mapel' => 'M02', 'mapel' => 'Bahasa']);
        $kelas = Kelas::create(['kode_kelas' => 'K02', 'nama_kelas' => 'XI TKJ', 'tingkat' => 'XI', 'jurusan' => 'TKJ']);

        // assign student to class
        $siswa->kelas_id = $kelas->id;
        $siswa->save();

        $jadwal = JadwalKelas::create([
            'kelas_id' => $kelas->id,
            'mapel_id' => $mapel->id,
            'guru_id' => $guru->id,
            'hari' => 'Senin',
            'jam_mulai' => '08:30',
            'jam_selesai' => '10:00',
            'tahun_ajaran_id' => $tahun->id,
        ]);

        // Create a kehadiran (teacher absent)
        $kehadiran = KehadiranGuru::create([
            'jadwal_id' => $jadwal->id,
            'guru_id' => $guru->id,
            'kelas_id' => $kelas->id,
            'mapel_id' => $mapel->id,
            'tanggal' => now()->toDateString(),
            'status' => 'tidak_hadir',
            'keterangan' => 'Tidak masuk',
        ]);

        // Student requests replacement
        $response = $this->actingAs($siswaUser, 'sanctum')
            ->postJson('/api/siswa/request-guru-pengganti', [
                'kehadiran_guru_id' => $kehadiran->id,
                'pesan' => 'Mohon pengganti',
            ]);

        // Debug: print response content to help diagnose failures
        fwrite(STDOUT, "REQUEST GURU PENGGANTI RESPONSE: " . $response->getContent() . PHP_EOL);

        $response->assertStatus(201)
            ->assertJson(['success' => true]);

        $this->assertDatabaseHas('guru_penggantis', [
            'kehadiran_guru_id' => $kehadiran->id,
            'status' => 'pending',
            'requested_by_siswa_id' => $siswa->id,
        ]);

        $request = GuruPengganti::where('kehadiran_guru_id', $kehadiran->id)->first();
        $this->assertNotNull($request);

        // Kurikulum assigns replacement
        $this->actingAs($kurikulumUser, 'sanctum')
            ->postJson('/api/kurikulum/pilih-guru-pengganti', [
                'request_id' => $request->id,
                'guru_pengganti_id' => $guruReplacement->id,
            ])
            ->assertStatus(200)
            ->assertJson(['success' => true]);

        $request->refresh();
        $this->assertEquals('aktif', $request->status);
        $this->assertEquals($guruReplacement->id, $request->guru_pengganti_id);

        // Student views list kehadiran and sees display_guru as replacement
        $resp = $this->actingAs($siswaUser, 'sanctum')->getJson('/api/siswa/list-kehadiran?kelas_id=' . $kelas->id . '&tanggal=' . now()->toDateString());
        $resp->assertStatus(200);
        $data = $resp->json('data');
        $this->assertNotEmpty($data);
        $found = collect($data)->firstWhere('kehadiran_id', $kehadiran->id);
        $this->assertNotNull($found);
        $this->assertEquals($guruReplacement->guru, $found['display_guru']);
        $this->assertEquals($guruReplacement->kode_guru, $found['display_kode_guru']);

        // Student submits attendance for replacement: should create kehadiran for replacement
        $this->actingAs($siswaUser, 'sanctum')
            ->postJson('/api/siswa/kehadiran-guru', [
                'jadwal_id' => $jadwal->id,
                'status' => 'hadir',
            ])
            ->assertStatus(201)
            ->assertJson(['success' => true]);

        $this->assertDatabaseHas('kehadiran_gurus', [
            'jadwal_id' => $jadwal->id,
            'guru_id' => $guruReplacement->id,
            'status' => 'hadir',
        ]);
    }
}
