<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\Kelas;
use App\Models\Guru;
use Illuminate\Http\Request;
use Illuminate\Validation\Rule;
use Maatwebsite\Excel\Facades\Excel;
use App\Imports\KelasImport;

class KelasController extends Controller
{
    public function index(Request $request)
    {
        $query = Kelas::with('guru');

        if ($request->filled('search')) {
            $search = $request->search;
            $query->where(function ($q) use ($search) {
                $q->where('nama_kelas', 'like', "%{$search}%")
                  ->orWhere('kode_kelas', 'like', "%{$search}%");
            });
        }

        if ($request->filled('tingkat')) {
            $query->where('tingkat', $request->tingkat);
        }

        $kelas = $query->orderBy('nama_kelas')->paginate(10);
        $gurus = Guru::orderByName()->get();

        return view('admin.kelas.index', compact('kelas', 'gurus'));
    }

    public function create()
    {
        $gurus = Guru::orderByName()->get();
        return view('admin.kelas.create', compact('gurus'));
    }

    public function store(Request $request)
    {
        $validated = $request->validate([
            'kode_kelas' => 'required|string|max:20|unique:kelas,kode_kelas',
            'nama_kelas' => 'required|string|max:100',
            'guru_id' => 'nullable|exists:gurus,id',
            'tingkat' => 'required|string|max:10',
            'jurusan' => 'required|string|max:50',
            'kapasitas' => 'nullable|integer|min:1',
        ]);

        Kelas::create($validated);

        return redirect()->route('admin.kelas.index')
            ->with('success', 'Kelas berhasil ditambahkan!');
    }

    public function edit(Kelas $kela)
    {
        $gurus = Guru::orderByName()->get();
        return view('admin.kelas.edit', compact('kela', 'gurus'));
    }

    public function update(Request $request, Kelas $kela)
    {
        $validated = $request->validate([
            'kode_kelas' => ['required', 'string', 'max:20', Rule::unique('kelas')->ignore($kela->id)],
            'nama_kelas' => 'required|string|max:100',
            'guru_id' => 'nullable|exists:gurus,id',
            'tingkat' => 'required|string|max:10',
            'jurusan' => 'required|string|max:50',
            'kapasitas' => 'nullable|integer|min:1',
        ]);

        $kela->update($validated);

        return redirect()->route('admin.kelas.index')
            ->with('success', 'Kelas berhasil diupdate!');
    }

    public function destroy(Kelas $kela)
    {
        $kela->delete();

        return redirect()->route('admin.kelas.index')
            ->with('success', 'Kelas berhasil dihapus!');
    }

    public function import(Request $request)
    {
        $request->validate([
            'file' => 'required|mimes:xlsx,xls,csv|max:10240',
        ]);

        try {
            Excel::import(new KelasImport, $request->file('file'));
            return redirect()->route('admin.kelas.index')
                ->with('success', 'Data kelas berhasil diimport!');
        } catch (\Exception $e) {
            return redirect()->route('admin.kelas.index')
                ->with('error', 'Gagal import data: ' . $e->getMessage());
        }
    }
}
