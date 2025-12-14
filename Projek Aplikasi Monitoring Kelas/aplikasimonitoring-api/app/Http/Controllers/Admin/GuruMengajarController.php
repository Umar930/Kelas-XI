<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\GuruMengajar;
use App\Models\Kelas;
use App\Models\Mapel;
use App\Models\Guru;
use App\Models\TahunAjaran;
use Illuminate\Http\Request;
use Maatwebsite\Excel\Facades\Excel;
use App\Imports\GuruMengajarImport;

class GuruMengajarController extends Controller
{
    public function index(Request $request)
    {
        $query = GuruMengajar::with(['kelas', 'mapel', 'guru', 'tahunAjaran']);

        if ($request->filled('search')) {
            $search = $request->search;
            $query->where(function ($q) use ($search) {
                $q->whereHas('guru', function ($qg) use ($search) {
                    $qg->where('nama', 'like', "%{$search}%");
                })
                ->orWhereHas('kelas', function ($qk) use ($search) {
                    $qk->where('nama_kelas', 'like', "%{$search}%");
                })
                ->orWhereHas('mapel', function ($qm) use ($search) {
                    $qm->where('mapel', 'like', "%{$search}%");
                });
            });
        }

        if ($request->filled('guru_id')) {
            $query->where('guru_id', $request->guru_id);
        }

        $guruMengajars = $query->orderBy('id', 'desc')->paginate(10);
        $guruList = Guru::orderByName()->get();
        $kelasList = Kelas::orderBy('nama_kelas')->get();
        $tahunAjaranList = TahunAjaran::orderBy('tahun', 'desc')->get();

        // Statistics (avoid referencing non-existing 'status' column)
        $stats = [
            'total' => GuruMengajar::count(),
            'aktif' => GuruMengajar::count(),
            'guru' => Guru::count(),
            'kelas' => Kelas::count(),
        ];

        return view('admin.guru-mengajar.index', compact('guruMengajars', 'guruList', 'kelasList', 'tahunAjaranList', 'stats'));
    }

    public function create()
    {
        $kelasList = Kelas::orderBy('nama_kelas')->get();
        $mapelList = Mapel::orderBy('mapel')->get();
        $guruList = Guru::orderByName()->get();
        $tahunAjaranList = TahunAjaran::orderBy('tahun', 'desc')->get();

        return view('admin.guru-mengajar.create', compact('kelasList', 'mapelList', 'guruList', 'tahunAjaranList'));
    }

    public function store(Request $request)
    {
        $validated = $request->validate([
            'tahun_ajaran_id' => 'required|exists:tahun_ajarans,id',
            'guru_id' => 'required|exists:gurus,id',
            'mapel_id' => 'required|exists:mapels,id',
            'kelas_id' => 'required|exists:kelas,id',
            'jam_per_minggu' => 'nullable|integer|min:1',
            'keterangan' => 'nullable|string',
        ]);

        GuruMengajar::create($validated);

        return redirect()->route('admin.guru-mengajar.index')
            ->with('success', 'Data guru mengajar berhasil ditambahkan!');
    }

    public function edit(GuruMengajar $guruMengajar)
    {
        $kelasList = Kelas::orderBy('nama_kelas')->get();
        $mapelList = Mapel::orderBy('mapel')->get();
        $guruList = Guru::orderByName()->get();
        $tahunAjaranList = TahunAjaran::orderBy('tahun', 'desc')->get();

        return view('admin.guru-mengajar.edit', compact('guruMengajar', 'kelasList', 'mapelList', 'guruList', 'tahunAjaranList'));
    }

    public function update(Request $request, GuruMengajar $guruMengajar)
    {
        $validated = $request->validate([
            'tahun_ajaran_id' => 'required|exists:tahun_ajarans,id',
            'guru_id' => 'required|exists:gurus,id',
            'mapel_id' => 'required|exists:mapels,id',
            'kelas_id' => 'required|exists:kelas,id',
            'jam_per_minggu' => 'nullable|integer|min:1',
            'keterangan' => 'nullable|string',
        ]);

        $guruMengajar->update($validated);

        return redirect()->route('admin.guru-mengajar.index')
            ->with('success', 'Data guru mengajar berhasil diupdate!');
    }

    public function destroy(GuruMengajar $guruMengajar)
    {
        $guruMengajar->delete();

        return redirect()->route('admin.guru-mengajar.index')
            ->with('success', 'Data guru mengajar berhasil dihapus!');
    }

    public function import(Request $request)
    {
        $request->validate([
            'file' => 'required|mimes:xlsx,xls,csv|max:10240',
        ]);

        try {
            Excel::import(new GuruMengajarImport, $request->file('file'));
            return redirect()->route('admin.guru-mengajar.index')
                ->with('success', 'Data guru mengajar berhasil diimport!');
        } catch (\Exception $e) {
            return redirect()->route('admin.guru-mengajar.index')
                ->with('error', 'Gagal import data: ' . $e->getMessage());
        }
    }
}
