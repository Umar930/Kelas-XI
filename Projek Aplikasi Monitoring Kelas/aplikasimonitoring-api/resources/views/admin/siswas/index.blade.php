@extends('admin.layouts.app')

@section('title', 'Manajemen Siswa')
@section('page-title', 'Manajemen Siswa')
@section('page-subtitle', 'Kelola data siswa')

@section('content')
    <div class="bg-white rounded-xl shadow-sm border border-gray-100">
        <div class="p-6 border-b border-gray-100">
            <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
                <div>
                    <h3 class="text-lg font-semibold text-gray-800">Daftar Siswa</h3>
                    <p class="text-sm text-gray-500">Total: {{ $siswas->total() }} siswa</p>
                </div>
                <div class="flex flex-wrap gap-2">
                    <button onclick="document.getElementById('importModal').classList.remove('hidden')" class="px-4 py-2 bg-orange-500 hover:bg-orange-600 text-white rounded-lg flex items-center"><i class="fas fa-file-import mr-2"></i>Import Excel</button>
                    <a href="{{ route('admin.siswas.create') }}" class="px-4 py-2 bg-indigo-500 hover:bg-indigo-600 text-white rounded-lg flex items-center"><i class="fas fa-plus mr-2"></i>Tambah Siswa</a>
                </div>
            </div>
        </div>
        
        <div class="p-6 border-b border-gray-100 bg-gray-50">
            <form action="{{ route('admin.siswas.index') }}" method="GET" class="flex flex-col md:flex-row gap-4">
                <div class="flex-1">
                    <input type="text" name="search" value="{{ request('search') }}" placeholder="Cari NIS atau nama..." class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500">
                </div>
                <select name="kelas_id" class="px-4 py-2 border border-gray-300 rounded-lg">
                    <option value="">Semua Kelas</option>
                    @foreach($kelasList as $k)
                        <option value="{{ $k->id }}" {{ request('kelas_id') == $k->id ? 'selected' : '' }}>{{ $k->nama_kelas }}</option>
                    @endforeach
                </select>
                <select name="status" class="px-4 py-2 border border-gray-300 rounded-lg">
                    <option value="">Semua Status</option>
                    <option value="aktif" {{ request('status') == 'aktif' ? 'selected' : '' }}>Aktif</option>
                    <option value="non-aktif" {{ request('status') == 'non-aktif' ? 'selected' : '' }}>Non-Aktif</option>
                    <option value="lulus" {{ request('status') == 'lulus' ? 'selected' : '' }}>Lulus</option>
                    <option value="pindah" {{ request('status') == 'pindah' ? 'selected' : '' }}>Pindah</option>
                </select>
                <button type="submit" class="px-6 py-2 bg-gray-800 hover:bg-gray-900 text-white rounded-lg"><i class="fas fa-search mr-2"></i>Cari</button>
                @if(request('search') || request('kelas_id') || request('status'))<a href="{{ route('admin.siswas.index') }}" class="px-4 py-2 bg-gray-300 text-gray-700 rounded-lg">Reset</a>@endif
            </form>
        </div>
        
        <div class="overflow-x-auto">
            <table class="w-full">
                <thead class="bg-gray-50 border-b border-gray-100">
                    <tr>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase">#</th>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase">NIS</th>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase">Nama</th>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase">Kelas</th>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase">JK</th>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase">Status</th>
                        <th class="px-6 py-4 text-center text-xs font-semibold text-gray-500 uppercase">Aksi</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-gray-100">
                    @forelse($siswas as $index => $siswa)
                        <tr class="hover:bg-gray-50">
                            <td class="px-6 py-4 text-sm text-gray-500">{{ $siswas->firstItem() + $index }}</td>
                            <td class="px-6 py-4 font-semibold text-gray-800">{{ $siswa->nis }}</td>
                            <td class="px-6 py-4 text-gray-700">{{ $siswa->nama }}</td>
                            <td class="px-6 py-4 text-gray-600">{{ $siswa->kelas->nama_kelas ?? '-' }}</td>
                            <td class="px-6 py-4">
                                <span class="px-2 py-1 text-xs rounded-full {{ $siswa->jenis_kelamin == 'L' ? 'bg-blue-100 text-blue-700' : 'bg-pink-100 text-pink-700' }}">
                                    {{ $siswa->jenis_kelamin == 'L' ? 'Laki-laki' : 'Perempuan' }}
                                </span>
                            </td>
                            <td class="px-6 py-4">
                                <span class="px-2 py-1 text-xs rounded-full 
                                    {{ $siswa->status == 'aktif' ? 'bg-green-100 text-green-700' : '' }}
                                    {{ $siswa->status == 'non-aktif' ? 'bg-gray-100 text-gray-700' : '' }}
                                    {{ $siswa->status == 'lulus' ? 'bg-blue-100 text-blue-700' : '' }}
                                    {{ $siswa->status == 'pindah' ? 'bg-orange-100 text-orange-700' : '' }}">
                                    {{ ucfirst($siswa->status) }}
                                </span>
                            </td>
                            <td class="px-6 py-4">
                                <div class="flex items-center justify-center space-x-2">
                                    <a href="{{ route('admin.siswas.edit', $siswa) }}" class="p-2 text-blue-500 hover:bg-blue-50 rounded-lg"><i class="fas fa-edit"></i></a>
                                    <form action="{{ route('admin.siswas.destroy', $siswa) }}" method="POST" class="inline" onsubmit="return confirm('Yakin?')">
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
        @if($siswas->hasPages())<div class="p-6 border-t border-gray-100">{{ $siswas->links() }}</div>@endif
    </div>
    
    <div id="importModal" class="hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white rounded-xl shadow-xl max-w-md w-full mx-4">
            <div class="p-6 border-b border-gray-100 flex items-center justify-between">
                <h3 class="text-lg font-semibold text-gray-800">Import Siswa</h3>
                <button onclick="document.getElementById('importModal').classList.add('hidden')" class="text-gray-400 hover:text-gray-600"><i class="fas fa-times"></i></button>
            </div>
            <form action="{{ route('admin.siswas.import') }}" method="POST" enctype="multipart/form-data" class="p-6">
                @csrf
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-2">File Excel</label>
                    <input type="file" name="file" accept=".xlsx,.xls,.csv" required class="w-full px-4 py-2 border border-gray-300 rounded-lg">
                </div>
                <div class="mb-4 p-4 bg-blue-50 rounded-lg"><p class="text-sm text-blue-700"><i class="fas fa-info-circle mr-1"></i>Kolom: nis, nama, jenis_kelamin, kelas_id, status</p></div>
                <div class="flex justify-end space-x-2">
                    <button type="button" onclick="document.getElementById('importModal').classList.add('hidden')" class="px-4 py-2 bg-gray-300 text-gray-700 rounded-lg">Batal</button>
                    <button type="submit" class="px-4 py-2 bg-indigo-500 text-white rounded-lg"><i class="fas fa-upload mr-2"></i>Import</button>
                </div>
            </form>
        </div>
    </div>
@endsection
