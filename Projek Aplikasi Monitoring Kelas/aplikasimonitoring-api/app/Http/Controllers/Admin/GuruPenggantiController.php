<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\GuruPengganti;
use App\Models\Kelas;
use App\Models\Mapel;
use App\Models\Guru;
use App\Models\KehadiranGuru;
use App\Models\User;
use Illuminate\Http\Request;
use Maatwebsite\Excel\Facades\Excel;
use App\Imports\GuruPenggantiImport;

class GuruPenggantiController extends Controller
{
    public function index(Request $request)
    {
        $query = GuruPengganti::with(['guruPengganti', 'kelas', 'mapel', 'kehadiranGuru.jadwal.guru', 'assignedByKurikulum']);

        if ($request->filled('search')) {
            $search = $request->search;
            $query->where(function ($q) use ($search) {
                $q->whereHas('guruPengganti', function ($qg) use ($search) {
                    $qg->where('nama', 'like', "%{$search}%");
                })
                ->orWhereHas('kelas', function ($qk) use ($search) {
                    $qk->where('nama_kelas', 'like', "%{$search}%");
                });
            });
        }

        if ($request->filled('status')) {
            $query->where('status', $request->status);
        }

        $guruPenggantis = $query->orderBy('id', 'desc')->paginate(10);

        // Statistics
        $stats = [
            'total' => GuruPengganti::count(),
            'aktif' => GuruPengganti::where('status', 'aktif')->count(),
            'selesai' => GuruPengganti::where('status', 'selesai')->count(),
            'hariIni' => GuruPengganti::whereDate('tanggal', today())->count(),
        ];

        return view('admin.guru-pengganti.index', compact('guruPenggantis', 'stats'));
    }

    public function create()
    {
        $kelasList = Kelas::orderBy('nama_kelas')->get();
        $mapels = Mapel::orderBy('mapel')->get();
        $gurus = Guru::orderByName()->get();
        $kehadiranGurus = KehadiranGuru::with('jadwal.guru')
            ->where('status', '!=', 'hadir')
            ->orderBy('id', 'desc')
            ->get();
        $kurikulums = User::where('role', 'kurikulum')->orderBy('name')->get();

        return view('admin.guru-pengganti.create', compact('kelasList', 'mapels', 'gurus', 'kehadiranGurus', 'kurikulums'));
    }

    public function store(Request $request)
    {
        $validated = $request->validate([
            'kehadiran_guru_id' => 'nullable|exists:kehadiran_gurus,id',
            'guru_pengganti_id' => 'required|exists:gurus,id',
            'kelas_id' => 'required|exists:kelas,id',
            'mapel_id' => 'required|exists:mapels,id',
            'tanggal' => 'required|date',
            'jam_mulai' => 'nullable|string',
            'jam_selesai' => 'nullable|string',
            'assigned_by_kurikulum_id' => 'nullable|exists:users,id',
            'status' => 'required|in:pending,aktif,selesai,ditolak',
            'catatan' => 'nullable|string',
        ]);

        GuruPengganti::create($validated);

        return redirect()->route('admin.guru-pengganti.index')
            ->with('success', 'Data guru pengganti berhasil ditambahkan!');
    }

    public function edit(GuruPengganti $guruPengganti)
    {
        $kelasList = Kelas::orderBy('nama_kelas')->get();
        $mapels = Mapel::orderBy('mapel')->get();
        $gurus = Guru::orderByName()->get();
        $kehadiranGurus = KehadiranGuru::with('jadwal.guru')->orderBy('id', 'desc')->get();
        $kurikulums = User::where('role', 'kurikulum')->orderBy('name')->get();

        return view('admin.guru-pengganti.edit', compact('guruPengganti', 'kelasList', 'mapels', 'gurus', 'kehadiranGurus', 'kurikulums'));
    }

    public function update(Request $request, GuruPengganti $guruPengganti)
    {
        $validated = $request->validate([
            'kehadiran_guru_id' => 'nullable|exists:kehadiran_gurus,id',
            'guru_pengganti_id' => 'required|exists:gurus,id',
            'kelas_id' => 'required|exists:kelas,id',
            'mapel_id' => 'required|exists:mapels,id',
            'tanggal' => 'required|date',
            'jam_mulai' => 'nullable|string',
            'jam_selesai' => 'nullable|string',
            'assigned_by_kurikulum_id' => 'nullable|exists:users,id',
            'status' => 'required|in:pending,aktif,selesai,ditolak',
            'catatan' => 'nullable|string',
        ]);

        $guruPengganti->update($validated);

        return redirect()->route('admin.guru-pengganti.index')
            ->with('success', 'Data guru pengganti berhasil diupdate!');
    }

    public function destroy(GuruPengganti $guruPengganti)
    {
        $guruPengganti->delete();

        return redirect()->route('admin.guru-pengganti.index')
            ->with('success', 'Data guru pengganti berhasil dihapus!');
    }

    public function import(Request $request)
    {
        $request->validate([
            'file' => 'required|mimes:xlsx,xls,csv|max:10240',
        ]);

        try {
            Excel::import(new GuruPenggantiImport, $request->file('file'));
            return redirect()->route('admin.guru-pengganti.index')
                ->with('success', 'Data guru pengganti berhasil diimport!');
        } catch (\Exception $e) {
            return redirect()->route('admin.guru-pengganti.index')
                ->with('error', 'Gagal import data: ' . $e->getMessage());
        }
    }
}
