<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\Mapel;
use Illuminate\Http\Request;
use Illuminate\Validation\Rule;
use Maatwebsite\Excel\Facades\Excel;
use App\Imports\MapelsImport;

class MapelController extends Controller
{
    public function index(Request $request)
    {
        $query = Mapel::query();

        if ($request->filled('search')) {
            $search = $request->search;
            $query->where(function ($q) use ($search) {
                $q->where('mapel', 'like', "%{$search}%")
                  ->orWhere('kode_mapel', 'like', "%{$search}%");
            });
        }

        $mapels = $query->orderBy('mapel')->paginate(10);

        return view('admin.mapels.index', compact('mapels'));
    }

    public function create()
    {
        return view('admin.mapels.create');
    }

    public function store(Request $request)
    {
        $validated = $request->validate([
            'kode_mapel' => 'required|string|max:20|unique:mapels,kode_mapel',
            'mapel' => 'required|string|max:100',
            'kategori' => 'nullable|string|max:50',
            'jam_pelajaran' => 'nullable|integer|min:1',
        ]);

        Mapel::create($validated);

        return redirect()->route('admin.mapels.index')
            ->with('success', 'Mata pelajaran berhasil ditambahkan!');
    }

    public function edit(Mapel $mapel)
    {
        return view('admin.mapels.edit', compact('mapel'));
    }

    public function update(Request $request, Mapel $mapel)
    {
        $validated = $request->validate([
            'kode_mapel' => ['required', 'string', 'max:20', Rule::unique('mapels')->ignore($mapel->id)],
            'mapel' => 'required|string|max:100',
            'kategori' => 'nullable|string|max:50',
            'jam_pelajaran' => 'nullable|integer|min:1',
        ]);

        $mapel->update($validated);

        return redirect()->route('admin.mapels.index')
            ->with('success', 'Mata pelajaran berhasil diupdate!');
    }

    public function destroy(Mapel $mapel)
    {
        $mapel->delete();

        return redirect()->route('admin.mapels.index')
            ->with('success', 'Mata pelajaran berhasil dihapus!');
    }

    public function import(Request $request)
    {
        $request->validate([
            'file' => 'required|mimes:xlsx,xls,csv|max:10240',
        ]);

        try {
            Excel::import(new MapelsImport, $request->file('file'));
            return redirect()->route('admin.mapels.index')
                ->with('success', 'Data mata pelajaran berhasil diimport!');
        } catch (\Exception $e) {
            return redirect()->route('admin.mapels.index')
                ->with('error', 'Gagal import data: ' . $e->getMessage());
        }
    }
}
