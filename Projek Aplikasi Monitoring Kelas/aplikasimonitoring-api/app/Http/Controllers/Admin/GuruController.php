<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\Guru;
use Illuminate\Support\Facades\Schema;
use Illuminate\Http\Request;
use Illuminate\Validation\Rule;
use Maatwebsite\Excel\Facades\Excel;
use App\Imports\GurusImport;

class GuruController extends Controller
{
    public function index(Request $request)
    {
        $query = Guru::query();

        if ($request->filled('search')) {
            $search = $request->search;
            $query->where(function ($q) use ($search) {
                if (Schema::hasColumn('gurus', 'guru')) {
                    $q->where('guru', 'like', "%{$search}%");
                }
                if (Schema::hasColumn('gurus', 'nama')) {
                    $q->orWhere('nama', 'like', "%{$search}%");
                }
                $q->orWhere('kode_guru', 'like', "%{$search}%")
                  ->orWhere('telepon', 'like', "%{$search}%");
            });
        }

        $gurus = $query->orderByName()->paginate(10);

        return view('admin.gurus.index', compact('gurus'));
    }

    public function create()
    {
        return view('admin.gurus.create');
    }

    public function store(Request $request)
    {
        $validated = $request->validate([
            'kode_guru' => 'required|string|max:255|unique:gurus,kode_guru',
            'guru' => 'required|string|max:255',
            'telepon' => 'nullable|string|max:255',
        ]);

        // Support databases that use 'nama' instead of 'guru'
        if (Schema::hasColumn('gurus', 'nama')) {
            $data = [
                'kode_guru' => $validated['kode_guru'],
                'nama' => $validated['guru'],
                'telepon' => $validated['telepon'] ?? null,
            ];
        } else {
            $data = $validated;
        }

        Guru::create($data);

        return redirect()->route('admin.gurus.index')
            ->with('success', 'Guru berhasil ditambahkan!');
    }

    public function edit(Guru $guru)
    {
        return view('admin.gurus.edit', compact('guru'));
    }

    public function update(Request $request, Guru $guru)
    {
        $validated = $request->validate([
            'kode_guru' => ['required', 'string', 'max:255', Rule::unique('gurus')->ignore($guru->id)],
            'guru' => 'required|string|max:255',
            'telepon' => 'nullable|string|max:255',
        ]);

        if (Schema::hasColumn('gurus', 'nama')) {
            $data = [
                'kode_guru' => $validated['kode_guru'],
                'nama' => $validated['guru'],
                'telepon' => $validated['telepon'] ?? null,
            ];
        } else {
            $data = $validated;
        }

        $guru->update($data);

        return redirect()->route('admin.gurus.index')
            ->with('success', 'Guru berhasil diupdate!');
    }

    public function destroy(Guru $guru)
    {
        $guru->delete();

        return redirect()->route('admin.gurus.index')
            ->with('success', 'Guru berhasil dihapus!');
    }

    public function import(Request $request)
    {
        $request->validate([
            'file' => 'required|mimes:xlsx,xls,csv|max:10240',
        ]);

        try {
            Excel::import(new GurusImport, $request->file('file'));
            return redirect()->route('admin.gurus.index')
                ->with('success', 'Data guru berhasil diimport!');
        } catch (\Exception $e) {
            return redirect()->route('admin.gurus.index')
                ->with('error', 'Gagal import data: ' . $e->getMessage());
        }
    }
}
