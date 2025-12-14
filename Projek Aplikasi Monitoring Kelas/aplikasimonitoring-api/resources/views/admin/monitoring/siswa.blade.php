@extends('admin.layouts.app')

@section('title', 'Monitoring Role Siswa')
@section('page-title', 'Monitoring Role Siswa')
@section('page-subtitle', 'Pantau aktivitas siswa yang input kehadiran guru dari aplikasi')

@section('content')
    <!-- Filter -->
    <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100 mb-6">
        <form method="GET" class="flex flex-wrap gap-4 items-end">
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Tanggal</label>
                <input type="date" name="tanggal" value="{{ $tanggal }}" 
                    class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500">
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Kelas</label>
                <select name="kelas_id" class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500">
                    <option value="">Semua Kelas</option>
                    @foreach($kelasList as $kelas)
                        <option value="{{ $kelas->id }}" {{ $kelas_id == $kelas->id ? 'selected' : '' }}>{{ $kelas->nama_kelas }}</option>
                    @endforeach
                </select>
            </div>
            <button type="submit" class="px-6 py-2 bg-indigo-500 text-white rounded-lg hover:bg-indigo-600 transition">
                <i class="fas fa-filter mr-2"></i>Filter
            </button>
            <a href="{{ route('admin.monitoring.siswa') }}" class="px-6 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition">
                <i class="fas fa-sync mr-2"></i>Reset
            </a>
        </form>
    </div>
    
    <!-- Stats Cards -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
        <div class="bg-gradient-to-r from-blue-500 to-blue-600 rounded-xl shadow-lg p-6 text-white">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-blue-100 text-sm">Total User Siswa</p>
                    <p class="text-4xl font-bold">{{ $stats['total_siswa'] }}</p>
                    <p class="text-blue-200 text-xs mt-2">Siswa dengan akses aplikasi</p>
                </div>
                <div class="w-16 h-16 bg-white/20 rounded-xl flex items-center justify-center">
                    <i class="fas fa-user-graduate text-3xl"></i>
                </div>
            </div>
        </div>
        
        <div class="bg-gradient-to-r from-green-500 to-green-600 rounded-xl shadow-lg p-6 text-white">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-green-100 text-sm">Input Kehadiran Hari Ini</p>
                    <p class="text-4xl font-bold">{{ $stats['total_input_today'] }}</p>
                    <p class="text-green-200 text-xs mt-2">Total input kehadiran guru</p>
                </div>
                <div class="w-16 h-16 bg-white/20 rounded-xl flex items-center justify-center">
                    <i class="fas fa-clipboard-check text-3xl"></i>
                </div>
            </div>
        </div>
        
        <div class="bg-gradient-to-r from-purple-500 to-purple-600 rounded-xl shadow-lg p-6 text-white">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-purple-100 text-sm">Siswa Aktif Hari Ini</p>
                    <p class="text-4xl font-bold">{{ $stats['siswa_aktif_today'] }}</p>
                    <p class="text-purple-200 text-xs mt-2">Siswa yang sudah input</p>
                </div>
                <div class="w-16 h-16 bg-white/20 rounded-xl flex items-center justify-center">
                    <i class="fas fa-user-check text-3xl"></i>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Input Kehadiran Hari Ini -->
    <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden mb-6">
        <div class="p-4 border-b border-gray-100 bg-blue-50">
            <h3 class="text-lg font-semibold text-blue-800">
                <i class="fas fa-clipboard-list text-blue-500 mr-2"></i>
                Input Kehadiran oleh Siswa - {{ \Carbon\Carbon::parse($tanggal)->format('d F Y') }}
            </h3>
            <p class="text-sm text-blue-600 mt-1">Data kehadiran guru yang di-input siswa dari aplikasi mobile</p>
        </div>
        
        <div class="overflow-x-auto">
            <table class="w-full">
                <thead class="bg-gray-50">
                    <tr>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Siswa</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Kelas</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Guru</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Mapel</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Status</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Keterangan</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Waktu Input</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-gray-100">
                    @forelse($inputHariIni as $input)
                        <tr class="hover:bg-gray-50">
                            <td class="px-4 py-3">
                                <div class="flex items-center">
                                    <div class="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center mr-3">
                                        <span class="text-blue-600 font-semibold text-sm">{{ strtoupper(substr($input->inputBySiswa->nama ?? 'S', 0, 1)) }}</span>
                                    </div>
                                    <div>
                                        <p class="font-medium text-gray-800">{{ $input->inputBySiswa->nama ?? '-' }}</p>
                                        <p class="text-xs text-gray-500">{{ $input->inputBySiswa->nis ?? '-' }}</p>
                                    </div>
                                </div>
                            </td>
                            <td class="px-4 py-3">
                                <span class="px-2 py-1 bg-gray-100 rounded text-sm">{{ $input->kelas->nama_kelas ?? '-' }}</span>
                            </td>
                            <td class="px-4 py-3">
                                <p class="font-medium text-gray-800">{{ $input->guru->guru ?? '-' }}</p>
                                <p class="text-xs text-gray-500">{{ $input->guru->kode_guru ?? '-' }}</p>
                            </td>
                            <td class="px-4 py-3 text-sm text-gray-600">{{ $input->mapel->mapel ?? '-' }}</td>
                            <td class="px-4 py-3">
                                @if($input->status == 'hadir')
                                    <span class="px-2 py-1 bg-green-100 text-green-700 rounded-full text-xs font-medium">
                                        <i class="fas fa-check mr-1"></i>Hadir
                                    </span>
                                @elseif($input->status == 'tidak_hadir')
                                    <span class="px-2 py-1 bg-red-100 text-red-700 rounded-full text-xs font-medium">
                                        <i class="fas fa-times mr-1"></i>Tidak Hadir
                                    </span>
                                @endif
                            </td>
                            <td class="px-4 py-3 text-sm text-gray-600 max-w-xs truncate">{{ $input->keterangan ?? '-' }}</td>
                            <td class="px-4 py-3 text-sm text-gray-500">
                                {{ $input->created_at->format('H:i:s') }}
                            </td>
                        </tr>
                    @empty
                        <tr>
                            <td colspan="7" class="px-4 py-8 text-center text-gray-500">
                                <i class="fas fa-inbox text-4xl mb-2"></i>
                                <p>Belum ada input kehadiran dari siswa hari ini</p>
                            </td>
                        </tr>
                    @endforelse
                </tbody>
            </table>
        </div>
    </div>
    
    <!-- Daftar Siswa -->
    <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        <div class="p-4 border-b border-gray-100">
            <h3 class="text-lg font-semibold text-gray-800">
                <i class="fas fa-users text-indigo-500 mr-2"></i>
                Daftar Siswa dengan Akun Aplikasi
            </h3>
        </div>
        
        <div class="overflow-x-auto">
            <table class="w-full">
                <thead class="bg-gray-50">
                    <tr>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Siswa</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">NIS</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Kelas</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Email</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Total Input</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-gray-100">
                    @forelse($siswaList as $siswa)
                        <tr class="hover:bg-gray-50">
                            <td class="px-4 py-3">
                                <div class="flex items-center">
                                    <div class="w-8 h-8 bg-indigo-100 rounded-full flex items-center justify-center mr-3">
                                        <span class="text-indigo-600 font-semibold text-sm">{{ strtoupper(substr($siswa->nama ?? 'S', 0, 1)) }}</span>
                                    </div>
                                    <p class="font-medium text-gray-800">{{ $siswa->nama }}</p>
                                </div>
                            </td>
                            <td class="px-4 py-3 text-sm text-gray-600">{{ $siswa->nis }}</td>
                            <td class="px-4 py-3">
                                <span class="px-2 py-1 bg-gray-100 rounded text-sm">{{ $siswa->kelas->nama_kelas ?? '-' }}</span>
                            </td>
                            <td class="px-4 py-3 text-sm text-gray-600">{{ $siswa->user->email ?? '-' }}</td>
                            <td class="px-4 py-3">
                                @php
                                    $totalInput = $inputKehadiranStats->get($siswa->id)->total_input ?? 0;
                                @endphp
                                <span class="px-3 py-1 bg-{{ $totalInput > 0 ? 'green' : 'gray' }}-100 text-{{ $totalInput > 0 ? 'green' : 'gray' }}-700 rounded-full text-sm font-medium">
                                    {{ $totalInput }} input
                                </span>
                            </td>
                        </tr>
                    @empty
                        <tr>
                            <td colspan="5" class="px-4 py-8 text-center text-gray-500">
                                <i class="fas fa-inbox text-4xl mb-2"></i>
                                <p>Tidak ada data siswa</p>
                            </td>
                        </tr>
                    @endforelse
                </tbody>
            </table>
        </div>
        
        @if($siswaList->hasPages())
            <div class="p-4 border-t border-gray-100">
                {{ $siswaList->withQueryString()->links() }}
            </div>
        @endif
    </div>
@endsection
