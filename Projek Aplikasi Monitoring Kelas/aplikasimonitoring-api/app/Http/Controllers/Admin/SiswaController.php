<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\Siswa;
use App\Models\Kelas;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Validation\Rule;
use Maatwebsite\Excel\Facades\Excel;
use App\Imports\SiswasImport;

class SiswaController extends Controller
{
    public function index(Request $request)
    {
        $query = Siswa::with(['kelas', 'user']);

        if ($request->filled('search')) {
            $search = $request->search;
            $query->where(function ($q) use ($search) {
                $q->where('nama', 'like', "%{$search}%")
                  ->orWhere('nis', 'like', "%{$search}%");
            });
        }

        if ($request->filled('kelas_id')) {
            $query->where('kelas_id', $request->kelas_id);
        }

        if ($request->filled('status')) {
            $query->where('status', $request->status);
        }

        $siswas = $query->orderBy('nama')->paginate(10);
        $kelasList = Kelas::orderBy('nama_kelas')->get();

        return view('admin.siswas.index', compact('siswas', 'kelasList'));
    }

    public function create()
    {
        $kelasList = Kelas::orderBy('nama_kelas')->get();
        $users = User::where('role', 'siswa')->doesntHave('siswa')->orderBy('name')->get();
        return view('admin.siswas.create', compact('kelasList', 'users'));
    }

    public function store(Request $request)
    {
        $validated = $request->validate([
            'nis' => 'required|string|max:20|unique:siswas,nis',
            'nama' => 'required|string|max:100',
            'jenis_kelamin' => 'required|in:L,P',
            'tempat_lahir' => 'nullable|string|max:100',
            'tanggal_lahir' => 'nullable|date',
            'alamat' => 'nullable|string',
            'telepon' => 'nullable|string|max:20',
            'email' => 'nullable|email|max:100',
            'kelas_id' => 'nullable|exists:kelas,id',
            'user_id' => 'nullable|exists:users,id',
            'status' => 'required|in:aktif,non-aktif,lulus,pindah',
        ]);

        Siswa::create($validated);

        return redirect()->route('admin.siswas.index')
            ->with('success', 'Siswa berhasil ditambahkan!');
    }

    public function edit(Siswa $siswa)
    {
        $kelasList = Kelas::orderBy('nama_kelas')->get();
        $users = User::where('role', 'siswa')
            ->where(function ($q) use ($siswa) {
                $q->doesntHave('siswa')
                  ->orWhere('id', $siswa->user_id);
            })
            ->orderBy('name')->get();
        return view('admin.siswas.edit', compact('siswa', 'kelasList', 'users'));
    }

    public function update(Request $request, Siswa $siswa)
    {
        $validated = $request->validate([
            'nis' => ['required', 'string', 'max:20', Rule::unique('siswas')->ignore($siswa->id)],
            'nama' => 'required|string|max:100',
            'jenis_kelamin' => 'required|in:L,P',
            'tempat_lahir' => 'nullable|string|max:100',
            'tanggal_lahir' => 'nullable|date',
            'alamat' => 'nullable|string',
            'telepon' => 'nullable|string|max:20',
            'email' => 'nullable|email|max:100',
            'kelas_id' => 'nullable|exists:kelas,id',
            'user_id' => 'nullable|exists:users,id',
            'status' => 'required|in:aktif,non-aktif,lulus,pindah',
        ]);

        $siswa->update($validated);

        return redirect()->route('admin.siswas.index')
            ->with('success', 'Siswa berhasil diupdate!');
    }

    public function destroy(Siswa $siswa)
    {
        $siswa->delete();

        return redirect()->route('admin.siswas.index')
            ->with('success', 'Siswa berhasil dihapus!');
    }

    public function import(Request $request)
    {
        $request->validate([
            'file' => 'required|mimes:xlsx,xls,csv|max:10240',
        ]);

        try {
            Excel::import(new SiswasImport, $request->file('file'));
            return redirect()->route('admin.siswas.index')
                ->with('success', 'Data siswa berhasil diimport!');
        } catch (\Exception $e) {
            return redirect()->route('admin.siswas.index')
                ->with('error', 'Gagal import data: ' . $e->getMessage());
        }
    }
}
