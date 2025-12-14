<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\JadwalKelas;
use App\Models\KehadiranGuru;
use App\Models\Kelas;
use App\Models\Mapel;
use App\Models\Guru;
use App\Models\TahunAjaran;
use App\Models\Siswa;
use Illuminate\Support\Facades\Http;
use Illuminate\Http\Request;
use Maatwebsite\Excel\Facades\Excel;
use App\Imports\JadwalImport;

class JadwalController extends Controller
{
    public function index(Request $request)
    {
        $query = JadwalKelas::with(['kelas', 'mapel', 'guru', 'tahunAjaran']);

        if ($request->filled('search')) {
            $search = $request->search;
            $query->where(function ($q) use ($search) {
                $q->whereHas('kelas', function ($qk) use ($search) {
                    $qk->where('nama_kelas', 'like', "%{$search}%");
                })
                ->orWhereHas('mapel', function ($qm) use ($search) {
                    $qm->where('mapel', 'like', "%{$search}%");
                })
                ->orWhereHas('guru', function ($qg) use ($search) {
                    $qg->where('guru', 'like', "%{$search}%");
                });
            });
        }

        if ($request->filled('hari')) {
            $query->where('hari', $request->hari);
        }

        if ($request->filled('kelas_id')) {
            $query->where('kelas_id', $request->kelas_id);
        }

        $jadwals = $query->orderBy('hari')->orderBy('jam_mulai')->paginate(10);
        $kelasList = Kelas::orderBy('nama_kelas')->get();
        $mapelList = Mapel::orderBy('mapel')->get();
        $hariList = ['Senin', 'Selasa', 'Rabu', 'Kamis', 'Jumat', 'Sabtu'];

        return view('admin.jadwals.index', compact('jadwals', 'kelasList', 'mapelList', 'hariList'));
    }

    public function create()
    {
        $kelasList = Kelas::orderBy('nama_kelas')->get();
        $mapelList = Mapel::orderBy('mapel')->get();
        $guruList = Guru::orderByName()->get();
        $tahunAjaranList = TahunAjaran::orderBy('tahun', 'desc')->get();
        $hariList = ['Senin', 'Selasa', 'Rabu', 'Kamis', 'Jumat', 'Sabtu'];

        return view('admin.jadwals.create', compact('kelasList', 'mapelList', 'guruList', 'tahunAjaranList', 'hariList'));
    }

    public function store(Request $request)
    {
        $validated = $request->validate([
            'kelas_id' => 'required|exists:kelas,id',
            'mapel_id' => 'required|exists:mapels,id',
            'guru_id' => 'required|exists:gurus,id',
            'hari' => 'required|string|in:Senin,Selasa,Rabu,Kamis,Jumat,Sabtu',
            'jam_mulai' => 'required',
            'jam_selesai' => 'required',
            'tahun_ajaran_id' => 'required|exists:tahun_ajarans,id',
        ]);

        JadwalKelas::create($validated);

        return redirect()->route('admin.jadwals.index')
            ->with('success', 'Jadwal berhasil ditambahkan!');
    }

    public function edit(JadwalKelas $jadwal)
    {
        $kelasList = Kelas::orderBy('nama_kelas')->get();
        $mapelList = Mapel::orderBy('mapel')->get();
        $guruList = Guru::orderByName()->get();
        $tahunAjaranList = TahunAjaran::orderBy('tahun', 'desc')->get();
        $hariList = ['Senin', 'Selasa', 'Rabu', 'Kamis', 'Jumat', 'Sabtu'];

        return view('admin.jadwals.edit', compact('jadwal', 'kelasList', 'mapelList', 'guruList', 'tahunAjaranList', 'hariList'));
    }

    public function update(Request $request, JadwalKelas $jadwal)
    {
        $validated = $request->validate([
            'kelas_id' => 'required|exists:kelas,id',
            'mapel_id' => 'required|exists:mapels,id',
            'guru_id' => 'required|exists:gurus,id',
            'hari' => 'required|string|in:Senin,Selasa,Rabu,Kamis,Jumat,Sabtu',
            'jam_mulai' => 'required',
            'jam_selesai' => 'required',
            'tahun_ajaran_id' => 'required|exists:tahun_ajarans,id',
        ]);

        $jadwal->update($validated);

        return redirect()->route('admin.jadwals.index')
            ->with('success', 'Jadwal berhasil diupdate!');
    }

    public function destroy(JadwalKelas $jadwal)
    {
        $jadwal->delete();

        return redirect()->route('admin.jadwals.index')
            ->with('success', 'Jadwal berhasil dihapus!');
    }

    public function import(Request $request)
    {
        $request->validate([
            'file' => 'required|mimes:xlsx,xls,csv|max:10240',
        ]);

        try {
            Excel::import(new JadwalImport, $request->file('file'));
            return redirect()->route('admin.jadwals.index')
                ->with('success', 'Data jadwal berhasil diimport!');
        } catch (\Exception $e) {
            return redirect()->route('admin.jadwals.index')
                ->with('error', 'Gagal import data: ' . $e->getMessage());
        }
    }

    /**
     * Input izin/sakit untuk jadwal tertentu
     * POST /admin/jadwals/{jadwal}/izin-sakit
     */
    public function izinSakit(Request $request, JadwalKelas $jadwal)
    {
        $validated = $request->validate([
            'tanggal' => 'required|date',
            'status' => 'required|in:izin,sakit',
            'keterangan' => 'nullable|string|max:500',
        ]);

        $adminId = auth()->id();

        // normalisasikan tanggal
        $tanggal = \Carbon\Carbon::parse($validated['tanggal'])->toDateString();

        // Validasi: izinkan lebih dari 1 input untuk jadwal+tanggal,
        // namun cegah duplikasi persis (status + keterangan sama) agar tidak ter-input dua kali secara tidak sengaja.
        $existing = KehadiranGuru::where('jadwal_id', $jadwal->id)
            ->whereDate('tanggal', $tanggal)
            ->where('status', $validated['status'])
            ->where(function($q) use ($validated) {
                if (isset($validated['keterangan']) && $validated['keterangan'] !== null) {
                    $q->where('keterangan', $validated['keterangan']);
                } else {
                    $q->whereNull('keterangan');
                }
            })
            ->first();

        if ($existing) {
            return redirect()->back()->with('error', 'Sudah ada input kehadiran dengan status dan keterangan yang sama untuk jadwal ini pada tanggal ' . $tanggal);
        }

        // Simpan ke tabel kehadiran_gurus dengan informasi jadwal
        $kehadiran = KehadiranGuru::create([
            'jadwal_id' => $jadwal->id,
            'kelas_id' => $jadwal->kelas_id,
            'guru_id' => $jadwal->guru_id,
            'mapel_id' => $jadwal->mapel_id ?? null,
            'tanggal' => $tanggal,
            'status' => $validated['status'],
            'keterangan' => $validated['keterangan'] ?? null,
            'input_by_kurikulum_id' => $adminId,
        ]);

        // Jika belum ada request guru pengganti untuk kehadiran ini, buat request otomatis agar kurikulum dapat memilih
        try {
            if (!\App\Models\GuruPengganti::where('kehadiran_guru_id', $kehadiran->id)->exists()) {
                \App\Models\GuruPengganti::create([
                    'kehadiran_guru_id' => $kehadiran->id,
                    'guru_pengganti_id' => null,
                    'jadwal_id' => $jadwal->id,
                    'kelas_id' => $jadwal->kelas_id,
                    'mapel_id' => $jadwal->mapel_id ?? null,
                    'tanggal' => $tanggal,
                    'status' => 'pending',
                    'keterangan' => 'Request otomatis: guru ' . ($jadwal->guru?->guru ?? 'guru') . ' ' . $validated['status'] . ' pada ' . $tanggal . '. ' . ($validated['keterangan'] ?? ''),
                ]);
            }
        } catch (\Exception $e) {
            \Illuminate\Support\Facades\Log::error('Gagal membuat request guru pengganti otomatis: ' . $e->getMessage());
        }

        // Buat notifikasi untuk siswa di kelas terkait agar langsung muncul di tab Izin Guru
        try {
            $guruName = $jadwal->guru?->guru ?? 'Guru';
            $mapelName = $jadwal->mapel?->mapel ?? '';
            $pesan = "Guru {$guruName} ({$mapelName}) {$validated['status']} pada {$tanggal}. Keterangan: " . ($validated['keterangan'] ?? '-');

            \App\Models\NotifikasiSiswa::create([
                'kelas_id' => $jadwal->kelas_id,
                'guru_id' => $jadwal->guru_id,
                'mapel_id' => $jadwal->mapel_id ?? null,
                'tanggal' => $tanggal,
                'tipe' => $validated['status'],
                'pesan' => $pesan,
                'created_by_kurikulum_id' => $adminId,
                'is_read' => false,
            ]);
            // Send push notification (FCM) to students in the class if they have device tokens
            try {
                $tokens = Siswa::where('kelas_id', $jadwal->kelas_id)->with('user')->get()
                    ->pluck('user.device_token')
                    ->filter()
                    ->unique()
                    ->values()
                    ->toArray();

                if (!empty($tokens)) {
                    $serverKey = env('FCM_SERVER_KEY');
                    if ($serverKey) {
                        $payload = [
                            'registration_ids' => $tokens,
                            'notification' => [
                                'title' => 'Izin/Sakit Guru',
                                'body' => $pesan,
                                'sound' => 'default'
                            ],
                            'data' => [
                                'type' => 'izin_sakit',
                                'kehadiran_id' => $kehadiran->id,
                            ],
                        ];

                        Http::withHeaders([
                            'Authorization' => 'key ' . $serverKey,
                            'Content-Type' => 'application/json',
                        ])->post('https://fcm.googleapis.com/fcm/send', $payload);
                    }
                }
            } catch (\Exception $e) {
                \Illuminate\Support\Facades\Log::error('Gagal mengirim FCM: ' . $e->getMessage());
            }
        } catch (\Exception $e) {
            // jangan gagal total jika notifikasi gagal, simpan log saja
            \Illuminate\Support\Facades\Log::error('Gagal membuat notifikasi siswa: ' . $e->getMessage());
        }

        return redirect()->back()->with('success', 'Input izin/sakit untuk jadwal berhasil disimpan.');
    }
}
