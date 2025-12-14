@extends('admin.layouts.app')

@section('title', 'Manajemen Jadwal')
@section('page-title', 'Manajemen Jadwal')
@section('page-subtitle', 'Kelola jadwal pelajaran')

@section('content')
    <div class="bg-white rounded-xl shadow-sm border border-gray-100">
        <div class="p-6 border-b border-gray-100">
            <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
                <div>
                    <h3 class="text-lg font-semibold text-gray-800">Daftar Jadwal</h3>
                    <p class="text-sm text-gray-500">Total: {{ $jadwals->total() }} jadwal</p>
                </div>
                <div class="flex flex-wrap gap-2">
                    <button onclick="document.getElementById('importModal').classList.remove('hidden')" class="px-4 py-2 bg-orange-500 hover:bg-orange-600 text-white rounded-lg flex items-center"><i class="fas fa-file-import mr-2"></i>Import Excel</button>
                    <a href="{{ route('admin.jadwals.create') }}" class="px-4 py-2 bg-indigo-500 hover:bg-indigo-600 text-white rounded-lg flex items-center"><i class="fas fa-plus mr-2"></i>Tambah Jadwal</a>
                </div>
            </div>
        </div>
        
        <div class="p-6 border-b border-gray-100 bg-gray-50">
            <form action="{{ route('admin.jadwals.index') }}" method="GET" class="flex flex-col md:flex-row gap-4">
                <select name="hari" class="px-4 py-2 border border-gray-300 rounded-lg">
                    <option value="">Semua Hari</option>
                    @foreach(['Senin', 'Selasa', 'Rabu', 'Kamis', 'Jumat', 'Sabtu'] as $h)
                        <option value="{{ $h }}" {{ request('hari') == $h ? 'selected' : '' }}>{{ $h }}</option>
                    @endforeach
                </select>
                <select name="kelas_id" class="px-4 py-2 border border-gray-300 rounded-lg">
                    <option value="">Semua Kelas</option>
                    @foreach($kelasList as $k)
                        <option value="{{ $k->id }}" {{ request('kelas_id') == $k->id ? 'selected' : '' }}>{{ $k->nama_kelas }}</option>
                    @endforeach
                </select>
                <select name="mapel_id" class="px-4 py-2 border border-gray-300 rounded-lg">
                    <option value="">Semua Mapel</option>
                    @foreach($mapelList as $m)
                        <option value="{{ $m->id }}" {{ request('mapel_id') == $m->id ? 'selected' : '' }}>{{ $m->mapel }}</option>
                    @endforeach
                </select>
                <button type="submit" class="px-6 py-2 bg-gray-800 hover:bg-gray-900 text-white rounded-lg"><i class="fas fa-filter mr-2"></i>Filter</button>
                @if(request('hari') || request('kelas_id') || request('mapel_id'))<a href="{{ route('admin.jadwals.index') }}" class="px-4 py-2 bg-gray-300 text-gray-700 rounded-lg">Reset</a>@endif
            </form>
        </div>
        
        <div class="overflow-x-auto">
            <table class="w-full">
                <thead class="bg-gray-50 border-b border-gray-100">
                    <tr>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase">#</th>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase">Hari</th>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase">Jam</th>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase">Kelas</th>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase">Mata Pelajaran</th>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase">Guru</th>
                        <th class="px-6 py-4 text-center text-xs font-semibold text-gray-500 uppercase">Aksi</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-gray-100">
                    @forelse($jadwals as $index => $jadwal)
                        <tr class="hover:bg-gray-50">
                            <td class="px-6 py-4 text-sm text-gray-500">{{ $jadwals->firstItem() + $index }}</td>
                            <td class="px-6 py-4">
                                <span class="px-3 py-1 text-xs font-medium rounded-full 
                                    {{ $jadwal->hari == 'Senin' ? 'bg-blue-100 text-blue-700' : '' }}
                                    {{ $jadwal->hari == 'Selasa' ? 'bg-green-100 text-green-700' : '' }}
                                    {{ $jadwal->hari == 'Rabu' ? 'bg-yellow-100 text-yellow-700' : '' }}
                                    {{ $jadwal->hari == 'Kamis' ? 'bg-purple-100 text-purple-700' : '' }}
                                    {{ $jadwal->hari == 'Jumat' ? 'bg-pink-100 text-pink-700' : '' }}
                                    {{ $jadwal->hari == 'Sabtu' ? 'bg-orange-100 text-orange-700' : '' }}">
                                    {{ $jadwal->hari }}
                                </span>
                            </td>
                            <td class="px-6 py-4 text-gray-700">{{ $jadwal->jam_mulai }} - {{ $jadwal->jam_selesai }}</td>
                            <td class="px-6 py-4 text-gray-700">{{ $jadwal->kelas->nama_kelas ?? '-' }}</td>
                            <td class="px-6 py-4 text-gray-700">{{ $jadwal->mapel->mapel ?? '-' }}</td>
                            <td class="px-6 py-4 text-gray-700">{{ $jadwal->guru->guru ?? '-' }}</td>
                            <td class="px-6 py-4">
                                <div class="flex items-center justify-center space-x-2">
                                    <a href="{{ route('admin.jadwals.edit', $jadwal) }}" class="p-2 text-blue-500 hover:bg-blue-50 rounded-lg"><i class="fas fa-edit"></i></a>
                                    <form action="{{ route('admin.jadwals.destroy', $jadwal) }}" method="POST" class="inline" onsubmit="return confirm('Yakin?')">
                                        @csrf @method('DELETE')
                                        <button type="submit" class="p-2 text-red-500 hover:bg-red-50 rounded-lg"><i class="fas fa-trash"></i></button>
                                    </form>
                                    <button type="button" onclick="openIzinSakitModal({{ $jadwal->id }}, '{{ $jadwal->guru?->guru }}', '{{ $jadwal->mapel?->mapel }}', '{{ $jadwal->kelas?->nama_kelas }}')" class="p-2 text-yellow-600 hover:bg-yellow-50 rounded-lg" title="Input Izin/Sakit">
                                        <i class="fas fa-notes-medical"></i>
                                    </button>
                                </div>
                            </td>
                        </tr>
                    @empty
                        <tr><td colspan="7" class="px-6 py-12 text-center text-gray-500"><i class="fas fa-inbox text-4xl mb-4 text-gray-300"></i><p>Tidak ada data</p></td></tr>
                    @endforelse
                </tbody>
            </table>
        </div>
        @if($jadwals->hasPages())<div class="p-6 border-t border-gray-100">{{ $jadwals->links() }}</div>@endif
    </div>
    
    <div id="importModal" class="hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white rounded-xl shadow-xl max-w-md w-full mx-4">
            <div class="p-6 border-b border-gray-100 flex items-center justify-between">
                <h3 class="text-lg font-semibold text-gray-800">Import Jadwal</h3>
                <button onclick="document.getElementById('importModal').classList.add('hidden')" class="text-gray-400 hover:text-gray-600"><i class="fas fa-times"></i></button>
            </div>
            <form action="{{ route('admin.jadwals.import') }}" method="POST" enctype="multipart/form-data" class="p-6">
                @csrf
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-2">File Excel</label>
                    <input type="file" name="file" accept=".xlsx,.xls,.csv" required class="w-full px-4 py-2 border border-gray-300 rounded-lg">
                </div>
                <div class="mb-4 p-4 bg-blue-50 rounded-lg"><p class="text-sm text-blue-700"><i class="fas fa-info-circle mr-1"></i>Kolom: hari, jam_mulai, jam_selesai, kelas_id, mapel_id, guru_id</p></div>
                <div class="flex justify-end space-x-2">
                    <button type="button" onclick="document.getElementById('importModal').classList.add('hidden')" class="px-4 py-2 bg-gray-300 text-gray-700 rounded-lg">Batal</button>
                    <button type="submit" class="px-4 py-2 bg-indigo-500 text-white rounded-lg"><i class="fas fa-upload mr-2"></i>Import</button>
                </div>
            </form>
        </div>
    </div>
    
    <!-- Modal Input Izin/Sakit Jadwal -->
    <div id="izinSakitModal" class="hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white rounded-xl shadow-xl max-w-md w-full mx-4">
            <div class="p-6 border-b border-gray-100 flex items-center justify-between">
                <h3 class="text-lg font-semibold text-gray-800">Input Izin/Sakit (Jadwal)</h3>
                <button onclick="closeIzinSakitModal()" class="text-gray-400 hover:text-gray-600"><i class="fas fa-times"></i></button>
            </div>
            <form id="izinSakitForm" method="POST" class="p-6">
                @csrf
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-2">Jadwal</label>
                    <input type="text" id="izinJadwalInfo" class="w-full px-4 py-2 border border-gray-300 rounded-lg bg-gray-100" readonly>
                </div>
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-2">Tanggal</label>
                    <input type="date" name="tanggal" class="w-full px-4 py-2 border border-gray-300 rounded-lg" value="{{ date('Y-m-d') }}" required>
                </div>
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-2">Status</label>
                    <select name="status" class="w-full px-4 py-2 border border-gray-300 rounded-lg" required>
                        <option value="">Pilih Status</option>
                        <option value="izin">Izin</option>
                        <option value="sakit">Sakit</option>
                    </select>
                </div>
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-2">Keterangan</label>
                    <textarea name="keterangan" class="w-full px-4 py-2 border border-gray-300 rounded-lg" rows="2" required></textarea>
                </div>
                <div class="flex justify-end space-x-2">
                    <button type="button" onclick="closeIzinSakitModal()" class="px-4 py-2 bg-gray-300 text-gray-700 rounded-lg">Batal</button>
                    <button type="submit" class="px-4 py-2 bg-yellow-600 hover:bg-yellow-700 text-white rounded-lg">Simpan</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function openIzinSakitModal(jadwalId, guruNama, mapelNama, kelasNama) {
            const form = document.getElementById('izinSakitForm');
            form.action = '{{ url('admin/jadwals') }}/' + jadwalId + '/izin-sakit';
            document.getElementById('izinJadwalInfo').value = (guruNama || '-') + ' | ' + (mapelNama || '-') + ' | ' + (kelasNama || '-');
            document.getElementById('izinSakitModal').classList.remove('hidden');
        }
        function closeIzinSakitModal() {
            document.getElementById('izinSakitModal').classList.add('hidden');
        }
    </script>
@endsection
