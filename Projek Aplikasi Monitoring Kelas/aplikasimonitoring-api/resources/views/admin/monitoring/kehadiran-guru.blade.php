@extends('admin.layouts.app')

@section('title', 'Monitoring Kehadiran Guru')
@section('page-title', 'Monitoring Kehadiran Guru')
@section('page-subtitle', 'Pantau kehadiran guru yang di-input dari aplikasi mobile')

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
                <label class="block text-sm font-medium text-gray-700 mb-1">Status</label>
                <select name="status" class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500">
                    <option value="">Semua Status</option>
                    <option value="hadir" {{ $status == 'hadir' ? 'selected' : '' }}>Hadir</option>
                    <option value="tidak_hadir" {{ $status == 'tidak_hadir' ? 'selected' : '' }}>Tidak Hadir</option>
                    <option value="izin" {{ $status == 'izin' ? 'selected' : '' }}>Izin</option>
                    <option value="sakit" {{ $status == 'sakit' ? 'selected' : '' }}>Sakit</option>
                </select>
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
            <a href="{{ route('admin.monitoring.kehadiran-guru') }}" class="px-6 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition">
                <i class="fas fa-sync mr-2"></i>Reset
            </a>
        </form>
    </div>
    
    <!-- Stats Cards -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4 mb-6">
        <div class="bg-white rounded-xl shadow-sm p-4 border border-gray-100">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-gray-500">Total Record</p>
                    <p class="text-2xl font-bold text-gray-800">{{ $stats['total'] }}</p>
                </div>
                <div class="w-12 h-12 bg-gray-100 rounded-xl flex items-center justify-center">
                    <i class="fas fa-list text-gray-500 text-xl"></i>
                </div>
            </div>
        </div>
        
        <div class="bg-white rounded-xl shadow-sm p-4 border border-gray-100">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-gray-500">Hadir</p>
                    <p class="text-2xl font-bold text-green-600">{{ $stats['hadir'] }}</p>
                </div>
                <div class="w-12 h-12 bg-green-100 rounded-xl flex items-center justify-center">
                    <i class="fas fa-check text-green-500 text-xl"></i>
                </div>
            </div>
        </div>
        
        <div class="bg-white rounded-xl shadow-sm p-4 border border-gray-100">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-gray-500">Tidak Hadir</p>
                    <p class="text-2xl font-bold text-red-600">{{ $stats['tidak_hadir'] }}</p>
                </div>
                <div class="w-12 h-12 bg-red-100 rounded-xl flex items-center justify-center">
                    <i class="fas fa-times text-red-500 text-xl"></i>
                </div>
            </div>
        </div>
        
        <div class="bg-white rounded-xl shadow-sm p-4 border border-gray-100">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-gray-500">Izin</p>
                    <p class="text-2xl font-bold text-yellow-600">{{ $stats['izin'] }}</p>
                </div>
                <div class="w-12 h-12 bg-yellow-100 rounded-xl flex items-center justify-center">
                    <i class="fas fa-envelope text-yellow-500 text-xl"></i>
                </div>
            </div>
        </div>
        
        <div class="bg-white rounded-xl shadow-sm p-4 border border-gray-100">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-gray-500">Sakit</p>
                    <p class="text-2xl font-bold text-orange-600">{{ $stats['sakit'] }}</p>
                </div>
                <div class="w-12 h-12 bg-orange-100 rounded-xl flex items-center justify-center">
                    <i class="fas fa-medkit text-orange-500 text-xl"></i>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Input By Stats -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
        <div class="bg-blue-50 rounded-xl shadow-sm p-4 border border-blue-100">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-blue-600 font-medium">Input oleh Siswa (Aplikasi)</p>
                    <p class="text-2xl font-bold text-blue-800">{{ $inputStats['by_siswa'] }}</p>
                    <p class="text-xs text-blue-500 mt-1">Kehadiran guru yang di-input siswa dari kelas</p>
                </div>
                <div class="w-14 h-14 bg-blue-200 rounded-xl flex items-center justify-center">
                    <i class="fas fa-user-graduate text-blue-600 text-2xl"></i>
                </div>
            </div>
        </div>
        
        <div class="bg-purple-50 rounded-xl shadow-sm p-4 border border-purple-100">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-purple-600 font-medium">Input oleh Kurikulum (Aplikasi)</p>
                    <p class="text-2xl font-bold text-purple-800">{{ $inputStats['by_kurikulum'] }}</p>
                    <p class="text-xs text-purple-500 mt-1">Izin/Sakit guru yang di-input kurikulum</p>
                </div>
                <div class="w-14 h-14 bg-purple-200 rounded-xl flex items-center justify-center">
                    <i class="fas fa-user-tie text-purple-600 text-2xl"></i>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Data Table -->
    <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        <div class="p-4 border-b border-gray-100">
            <h3 class="text-lg font-semibold text-gray-800">
                <i class="fas fa-clipboard-list text-indigo-500 mr-2"></i>
                Data Kehadiran Guru - {{ \Carbon\Carbon::parse($tanggal)->format('d F Y') }}
            </h3>
        </div>
        
        <div class="overflow-x-auto">
            <table class="w-full">
                <thead class="bg-gray-50">
                    <tr>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Guru</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Kelas</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Mapel</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Status</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Keterangan</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Di-input Oleh</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Waktu</th>
                        <th class="px-4 py-3 text-center text-xs font-semibold text-gray-600 uppercase">Aksi</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-gray-100">
                    @forelse($kehadiranList as $kehadiran)
                        <tr class="hover:bg-gray-50">
                            <td class="px-4 py-3">
                                <div class="flex items-center">
                                    <div class="w-8 h-8 bg-indigo-100 rounded-full flex items-center justify-center mr-3">
                                        <span class="text-indigo-600 font-semibold text-sm">{{ strtoupper(substr($kehadiran->guru->guru ?? 'G', 0, 1)) }}</span>
                                    </div>
                                    <div>
                                        <p class="font-medium text-gray-800">{{ $kehadiran->guru->guru ?? '-' }}</p>
                                        <p class="text-xs text-gray-500">{{ $kehadiran->guru->kode_guru ?? '-' }}</p>
                                    </div>
                                </div>
                            </td>
                            <td class="px-4 py-3">
                                <span class="px-2 py-1 bg-gray-100 rounded text-sm">{{ $kehadiran->kelas->nama_kelas ?? '-' }}</span>
                            </td>
                            <td class="px-4 py-3 text-sm text-gray-600">{{ $kehadiran->mapel->mapel ?? '-' }}</td>
                            <td class="px-4 py-3">
                                @if($kehadiran->status == 'hadir')
                                    <span class="px-2 py-1 bg-green-100 text-green-700 rounded-full text-xs font-medium">Hadir</span>
                                @elseif($kehadiran->status == 'tidak_hadir')
                                    <span class="px-2 py-1 bg-red-100 text-red-700 rounded-full text-xs font-medium">Tidak Hadir</span>
                                @elseif($kehadiran->status == 'izin')
                                    <span class="px-2 py-1 bg-yellow-100 text-yellow-700 rounded-full text-xs font-medium">Izin</span>
                                @elseif($kehadiran->status == 'sakit')
                                    <span class="px-2 py-1 bg-orange-100 text-orange-700 rounded-full text-xs font-medium">Sakit</span>
                                @endif
                            </td>
                            <td class="px-4 py-3 text-sm text-gray-600 max-w-xs truncate">{{ $kehadiran->keterangan ?? '-' }}</td>
                            <td class="px-4 py-3">
                                @if($kehadiran->input_by_siswa_id)
                                    <div class="flex items-center">
                                        <span class="w-2 h-2 bg-blue-500 rounded-full mr-2"></span>
                                        <div>
                                            <p class="text-sm font-medium text-blue-600">Siswa</p>
                                            <p class="text-xs text-gray-500">{{ $kehadiran->inputBySiswa->nama ?? '-' }}</p>
                                        </div>
                                    </div>
                                @elseif($kehadiran->input_by_kurikulum_id)
                                    <div class="flex items-center">
                                        <span class="w-2 h-2 bg-purple-500 rounded-full mr-2"></span>
                                        <div>
                                            <p class="text-sm font-medium text-purple-600">Kurikulum</p>
                                            <p class="text-xs text-gray-500">{{ $kehadiran->inputByKurikulum->name ?? '-' }}</p>
                                        </div>
                                    </div>
                                @else
                                    <span class="text-gray-400">-</span>
                                @endif
                            </td>
                            <td class="px-4 py-3 text-sm text-gray-500">
                                {{ $kehadiran->created_at->format('H:i') }}
                            </td>
                            <td class="px-4 py-3 text-center">
                                <a href="{{ route('admin.monitoring.kehadiran-guru.detail', $kehadiran->guru_id) }}" 
                                   class="text-indigo-500 hover:text-indigo-700" title="Detail Guru">
                                    <i class="fas fa-eye"></i>
                                </a>
                            </td>
                        </tr>
                    @empty
                        <tr>
                            <td colspan="8" class="px-4 py-8 text-center text-gray-500">
                                <i class="fas fa-inbox text-4xl mb-2"></i>
                                <p>Tidak ada data kehadiran untuk tanggal ini</p>
                            </td>
                        </tr>
                    @endforelse
                </tbody>
            </table>
        </div>
        
        @if($kehadiranList->hasPages())
            <div class="p-4 border-t border-gray-100">
                {{ $kehadiranList->withQueryString()->links() }}
            </div>
        @endif
    </div>
@endsection
