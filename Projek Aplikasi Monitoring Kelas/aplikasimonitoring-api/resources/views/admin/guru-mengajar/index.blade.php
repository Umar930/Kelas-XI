@extends('admin.layouts.app')

@section('title', 'Manajemen Guru Mengajar')
@section('page-title', 'Manajemen Guru Mengajar')
@section('page-subtitle', 'Kelola data guru mengajar')

@section('content')
    <!-- Stats Cards -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-6">
        <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
            <div class="flex items-center">
                <div class="p-3 bg-indigo-100 rounded-xl"><i class="fas fa-chalkboard-teacher text-2xl text-indigo-600"></i></div>
                <div class="ml-4">
                    <p class="text-sm text-gray-500">Total</p>
                    <p class="text-2xl font-bold text-gray-800">{{ $stats['total'] ?? 0 }}</p>
                </div>
            </div>
        </div>
        <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
            <div class="flex items-center">
                <div class="p-3 bg-green-100 rounded-xl"><i class="fas fa-check-circle text-2xl text-green-600"></i></div>
                <div class="ml-4">
                    <p class="text-sm text-gray-500">Aktif</p>
                    <p class="text-2xl font-bold text-gray-800">{{ $stats['aktif'] ?? 0 }}</p>
                </div>
            </div>
        </div>
        <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
            <div class="flex items-center">
                <div class="p-3 bg-blue-100 rounded-xl"><i class="fas fa-users text-2xl text-blue-600"></i></div>
                <div class="ml-4">
                    <p class="text-sm text-gray-500">Guru</p>
                    <p class="text-2xl font-bold text-gray-800">{{ $stats['guru'] ?? 0 }}</p>
                </div>
            </div>
        </div>
        <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
            <div class="flex items-center">
                <div class="p-3 bg-purple-100 rounded-xl"><i class="fas fa-school text-2xl text-purple-600"></i></div>
                <div class="ml-4">
                    <p class="text-sm text-gray-500">Kelas</p>
                    <p class="text-2xl font-bold text-gray-800">{{ $stats['kelas'] ?? 0 }}</p>
                </div>
            </div>
        </div>
    </div>

    <div class="bg-white rounded-xl shadow-sm border border-gray-100">
        <div class="p-6 border-b border-gray-100">
            <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
                <div>
                    <h3 class="text-lg font-semibold text-gray-800">Daftar Guru Mengajar</h3>
                    <p class="text-sm text-gray-500">Total: {{ $guruMengajars->total() }} data</p>
                </div>
                <div class="flex flex-wrap gap-2">
                    <button onclick="document.getElementById('importModal').classList.remove('hidden')" class="px-4 py-2 bg-orange-500 hover:bg-orange-600 text-white rounded-lg flex items-center"><i class="fas fa-file-import mr-2"></i>Import Excel</button>
                    <a href="{{ route('admin.guru-mengajar.create') }}" class="px-4 py-2 bg-indigo-500 hover:bg-indigo-600 text-white rounded-lg flex items-center"><i class="fas fa-plus mr-2"></i>Tambah Data</a>
                </div>
            </div>
        </div>
        
        <div class="p-6 border-b border-gray-100 bg-gray-50">
            <form action="{{ route('admin.guru-mengajar.index') }}" method="GET" class="flex flex-col md:flex-row gap-4">
                <select name="guru_id" class="px-4 py-2 border border-gray-300 rounded-lg">
                    <option value="">Semua Guru</option>
                    @foreach($guruList as $g)
                        <option value="{{ $g->id }}" {{ request('guru_id') == $g->id ? 'selected' : '' }}>{{ $g->guru }}</option>
                    @endforeach
                </select>
                <select name="kelas_id" class="px-4 py-2 border border-gray-300 rounded-lg">
                    <option value="">Semua Kelas</option>
                    @foreach($kelasList as $k)
                        <option value="{{ $k->id }}" {{ request('kelas_id') == $k->id ? 'selected' : '' }}>{{ $k->nama_kelas }}</option>
                    @endforeach
                </select>
                <select name="tahun_ajaran_id" class="px-4 py-2 border border-gray-300 rounded-lg">
                    <option value="">Semua Tahun Ajaran</option>
                    @foreach($tahunAjaranList as $t)
                        <option value="{{ $t->id }}" {{ request('tahun_ajaran_id') == $t->id ? 'selected' : '' }}>{{ $t->tahun }}</option>
                    @endforeach
                </select>
                <button type="submit" class="px-6 py-2 bg-gray-800 hover:bg-gray-900 text-white rounded-lg"><i class="fas fa-filter mr-2"></i>Filter</button>
                @if(request('guru_id') || request('kelas_id') || request('tahun_ajaran_id'))<a href="{{ route('admin.guru-mengajar.index') }}" class="px-4 py-2 bg-gray-300 text-gray-700 rounded-lg">Reset</a>@endif
            </form>
        </div>
        
        <div class="overflow-x-auto">
            <table class="w-full">
                <thead class="bg-gray-50 border-b border-gray-100">
                    <tr>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase">#</th>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase">Guru</th>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase">Mata Pelajaran</th>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase">Kelas</th>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase">Tahun Ajaran</th>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase">Status</th>
                        <th class="px-6 py-4 text-center text-xs font-semibold text-gray-500 uppercase">Aksi</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-gray-100">
                    @forelse($guruMengajars as $index => $gm)
                        <tr class="hover:bg-gray-50">
                            <td class="px-6 py-4 text-sm text-gray-500">{{ $guruMengajars->firstItem() + $index }}</td>
                            <td class="px-6 py-4">
                                <div class="flex items-center">
                                    <div class="w-10 h-10 bg-indigo-100 rounded-full flex items-center justify-center mr-3">
                                        <span class="text-indigo-600 font-semibold">{{ substr($gm->guru->guru ?? 'G', 0, 1) }}</span>
                                    </div>
                                    <div>
                                        <p class="font-semibold text-gray-800">{{ $gm->guru->guru ?? '-' }}</p>
                                        <p class="text-sm text-gray-500">{{ $gm->guru->nip ?? '-' }}</p>
                                    </div>
                                </div>
                            </td>
                            <td class="px-6 py-4 text-gray-700">{{ $gm->mapel->mapel ?? '-' }}</td>
                            <td class="px-6 py-4 text-gray-700">{{ $gm->kelas->nama_kelas ?? '-' }}</td>
                            <td class="px-6 py-4 text-gray-700">{{ $gm->tahunAjaran->tahun ?? '-' }}</td>
                            <td class="px-6 py-4">
                                <span class="px-2 py-1 text-xs rounded-full {{ $gm->status == 'aktif' ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-700' }}">
                                    {{ ucfirst($gm->status ?? 'aktif') }}
                                </span>
                            </td>
                            <td class="px-6 py-4">
                                <div class="flex items-center justify-center space-x-2">
                                    <a href="{{ route('admin.guru-mengajar.edit', $gm) }}" class="p-2 text-blue-500 hover:bg-blue-50 rounded-lg"><i class="fas fa-edit"></i></a>
                                    <form action="{{ route('admin.guru-mengajar.destroy', $gm) }}" method="POST" class="inline" onsubmit="return confirm('Yakin?')">
                                        @csrf @method('DELETE')
                                        <button type="submit" class="p-2 text-red-500 hover:bg-red-50 rounded-lg"><i class="fas fa-trash"></i></button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    @empty
                        <tr><td colspan="7" class="px-6 py-12 text-center text-gray-500"><i class="fas fa-inbox text-4xl mb-4 text-gray-300"></i><p>Tidak ada data</p></td></tr>
                    @endforelse
                </tbody>
            </table>
        </div>
        @if($guruMengajars->hasPages())<div class="p-6 border-t border-gray-100">{{ $guruMengajars->links() }}</div>@endif
    </div>
    
    <div id="importModal" class="hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white rounded-xl shadow-xl max-w-md w-full mx-4">
            <div class="p-6 border-b border-gray-100 flex items-center justify-between">
                <h3 class="text-lg font-semibold text-gray-800">Import Guru Mengajar</h3>
                <button onclick="document.getElementById('importModal').classList.add('hidden')" class="text-gray-400 hover:text-gray-600"><i class="fas fa-times"></i></button>
            </div>
            <form action="{{ route('admin.guru-mengajar.import') }}" method="POST" enctype="multipart/form-data" class="p-6">
                @csrf
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-2">File Excel</label>
                    <input type="file" name="file" accept=".xlsx,.xls,.csv" required class="w-full px-4 py-2 border border-gray-300 rounded-lg">
                </div>
                <div class="mb-4 p-4 bg-blue-50 rounded-lg"><p class="text-sm text-blue-700"><i class="fas fa-info-circle mr-1"></i>Kolom: guru_id, mapel_id, kelas_id, tahun_ajaran_id, status</p></div>
                <div class="flex justify-end space-x-2">
                    <button type="button" onclick="document.getElementById('importModal').classList.add('hidden')" class="px-4 py-2 bg-gray-300 text-gray-700 rounded-lg">Batal</button>
                    <button type="submit" class="px-4 py-2 bg-indigo-500 text-white rounded-lg"><i class="fas fa-upload mr-2"></i>Import</button>
                </div>
            </form>
        </div>
    </div>
@endsection
