<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\TahunAjaran;
use Illuminate\Http\Request;
use Illuminate\Validation\Rule;
use Maatwebsite\Excel\Facades\Excel;
use App\Imports\TahunAjaranImport;

class TahunAjaranController extends Controller
{
    public function index(Request $request)
    {
        $query = TahunAjaran::query();

        if ($request->filled('search')) {
            $query->where('tahun', 'like', "%{$request->search}%");
        }

        $tahunAjarans = $query->orderBy('tahun', 'desc')->paginate(10);

        return view('admin.tahun-ajaran.index', compact('tahunAjarans'));
    }

    public function create()
    {
        return view('admin.tahun-ajaran.create');
    }

    public function store(Request $request)
    {
        $validated = $request->validate([
            'tahun' => 'required|string|max:20|unique:tahun_ajarans,tahun',
            'flag' => 'boolean',
        ]);

        $validated['flag'] = $request->has('flag');

        // Jika flag aktif, nonaktifkan tahun ajaran lain
        if ($validated['flag']) {
            TahunAjaran::where('flag', true)->update(['flag' => false]);
        }

        TahunAjaran::create($validated);

        return redirect()->route('admin.tahun-ajaran.index')
            ->with('success', 'Tahun ajaran berhasil ditambahkan!');
    }

    public function edit(TahunAjaran $tahunAjaran)
    {
        return view('admin.tahun-ajaran.edit', compact('tahunAjaran'));
    }

    public function update(Request $request, TahunAjaran $tahunAjaran)
    {
        $validated = $request->validate([
            'tahun' => ['required', 'string', 'max:20', Rule::unique('tahun_ajarans')->ignore($tahunAjaran->id)],
            'flag' => 'boolean',
        ]);

        $validated['flag'] = $request->has('flag');

        // Jika flag aktif, nonaktifkan tahun ajaran lain
        if ($validated['flag']) {
            TahunAjaran::where('flag', true)->where('id', '!=', $tahunAjaran->id)->update(['flag' => false]);
        }

        $tahunAjaran->update($validated);

        return redirect()->route('admin.tahun-ajaran.index')
            ->with('success', 'Tahun ajaran berhasil diupdate!');
    }

    public function destroy(TahunAjaran $tahunAjaran)
    {
        $tahunAjaran->delete();

        return redirect()->route('admin.tahun-ajaran.index')
            ->with('success', 'Tahun ajaran berhasil dihapus!');
    }

    public function import(Request $request)
    {
        $request->validate([
            'file' => 'required|mimes:xlsx,xls,csv|max:10240',
        ]);

        try {
            Excel::import(new TahunAjaranImport, $request->file('file'));
            return redirect()->route('admin.tahun-ajaran.index')
                ->with('success', 'Data tahun ajaran berhasil diimport!');
        } catch (\Exception $e) {
            return redirect()->route('admin.tahun-ajaran.index')
                ->with('error', 'Gagal import data: ' . $e->getMessage());
        }
    }
}
