@extends('admin.layouts.app')

@section('title', 'Detail Kehadiran Guru')
@section('page-title', 'Detail Kehadiran Guru')
@section('page-subtitle', 'Riwayat kehadiran guru')

@section('content')
    <!-- Guru Info -->
    <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100 mb-6">
        <div class="flex items-center justify-between">
            <div class="flex items-center">
                <div class="w-16 h-16 bg-indigo-100 rounded-xl flex items-center justify-center mr-4">
                    <span class="text-indigo-600 font-bold text-2xl">{{ strtoupper(substr($guru->guru ?? 'G', 0, 1)) }}</span>
                </div>
                <div>
                    <h2 class="text-xl font-bold text-gray-800">{{ $guru->guru }}</h2>
                    <p class="text-gray-500">Kode: {{ $guru->kode_guru }}</p>
                    <p class="text-gray-500">Telepon: {{ $guru->telepon ?? '-' }}</p>
                </div>
            </div>
            <a href="{{ route('admin.monitoring.kehadiran-guru') }}" class="px-4 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition">
                <i class="fas fa-arrow-left mr-2"></i>Kembali
            </a>
        </div>
    </div>
    
    <!-- Filter -->
    <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100 mb-6">
        <form method="GET" class="flex flex-wrap gap-4 items-end">
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Bulan</label>
                <select name="bulan" class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500">
                    @for($i = 1; $i <= 12; $i++)
                        <option value="{{ $i }}" {{ $bulan == $i ? 'selected' : '' }}>
                            {{ \Carbon\Carbon::create()->month($i)->format('F') }}
                        </option>
                    @endfor
                </select>
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Tahun</label>
                <select name="tahun" class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500">
                    @for($i = date('Y'); $i >= date('Y') - 2; $i--)
                        <option value="{{ $i }}" {{ $tahun == $i ? 'selected' : '' }}>{{ $i }}</option>
                    @endfor
                </select>
            </div>
            <button type="submit" class="px-6 py-2 bg-indigo-500 text-white rounded-lg hover:bg-indigo-600 transition">
                <i class="fas fa-filter mr-2"></i>Filter
            </button>
        </form>
        <p class="text-sm text-gray-500 mt-3">
            <i class="fas fa-calendar-alt mr-1"></i>
            Periode: {{ $startDate->format('d M Y') }} - {{ $endDate->format('d M Y') }}
        </p>
    </div>
    
    <!-- Stats Cards -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
        <div class="bg-green-50 rounded-xl shadow-sm p-4 border border-green-100">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-green-600">Hadir</p>
                    <p class="text-3xl font-bold text-green-700">{{ $stats['hadir'] }}</p>
                </div>
                <div class="w-12 h-12 bg-green-100 rounded-xl flex items-center justify-center">
                    <i class="fas fa-check text-green-500 text-xl"></i>
                </div>
            </div>
        </div>
        
        <div class="bg-red-50 rounded-xl shadow-sm p-4 border border-red-100">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-red-600">Tidak Hadir</p>
                    <p class="text-3xl font-bold text-red-700">{{ $stats['tidak_hadir'] }}</p>
                </div>
                <div class="w-12 h-12 bg-red-100 rounded-xl flex items-center justify-center">
                    <i class="fas fa-times text-red-500 text-xl"></i>
                </div>
            </div>
        </div>
        
        <div class="bg-yellow-50 rounded-xl shadow-sm p-4 border border-yellow-100">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-yellow-600">Izin</p>
                    <p class="text-3xl font-bold text-yellow-700">{{ $stats['izin'] }}</p>
                </div>
                <div class="w-12 h-12 bg-yellow-100 rounded-xl flex items-center justify-center">
                    <i class="fas fa-envelope text-yellow-500 text-xl"></i>
                </div>
            </div>
        </div>
        
        <div class="bg-orange-50 rounded-xl shadow-sm p-4 border border-orange-100">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-orange-600">Sakit</p>
                    <p class="text-3xl font-bold text-orange-700">{{ $stats['sakit'] }}</p>
                </div>
                <div class="w-12 h-12 bg-orange-100 rounded-xl flex items-center justify-center">
                    <i class="fas fa-medkit text-orange-500 text-xl"></i>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Persentase Kehadiran -->
    @php
        $total = $stats['hadir'] + $stats['tidak_hadir'] + $stats['izin'] + $stats['sakit'];
        $persenHadir = $total > 0 ? round(($stats['hadir'] / $total) * 100) : 0;
    @endphp
    <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100 mb-6">
        <h3 class="text-lg font-semibold text-gray-800 mb-4">Persentase Kehadiran</h3>
        <div class="flex justify-between text-sm mb-2">
            <span class="text-gray-600">Kehadiran bulan {{ \Carbon\Carbon::create()->month($bulan)->format('F') }} {{ $tahun }}</span>
            <span class="font-semibold text-gray-800">{{ $persenHadir }}%</span>
        </div>
        <div class="w-full bg-gray-200 rounded-full h-6">
            <div class="bg-gradient-to-r from-green-400 to-green-500 h-6 rounded-full flex items-center justify-center text-white text-sm font-semibold" style="width: {{ $persenHadir }}%">
                @if($persenHadir > 10)
                    {{ $persenHadir }}%
                @endif
            </div>
        </div>
    </div>
    
    <!-- Data Table -->
    <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        <div class="p-4 border-b border-gray-100">
            <h3 class="text-lg font-semibold text-gray-800">
                <i class="fas fa-history text-indigo-500 mr-2"></i>
                Riwayat Kehadiran
            </h3>
        </div>
        
        <div class="overflow-x-auto">
            <table class="w-full">
                <thead class="bg-gray-50">
                    <tr>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Tanggal</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Kelas</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Mapel</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Status</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Keterangan</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Di-input Oleh</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-gray-100">
                    @forelse($kehadiranList as $kehadiran)
                        <tr class="hover:bg-gray-50">
                            <td class="px-4 py-3 font-medium text-gray-800">
                                {{ \Carbon\Carbon::parse($kehadiran->tanggal)->format('d M Y') }}
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
                                        <span class="text-sm text-blue-600">Siswa: {{ $kehadiran->inputBySiswa->nama ?? '-' }}</span>
                                    </div>
                                @elseif($kehadiran->input_by_kurikulum_id)
                                    <div class="flex items-center">
                                        <span class="w-2 h-2 bg-purple-500 rounded-full mr-2"></span>
                                        <span class="text-sm text-purple-600">Kurikulum: {{ $kehadiran->inputByKurikulum->name ?? '-' }}</span>
                                    </div>
                                @else
                                    <span class="text-gray-400">-</span>
                                @endif
                            </td>
                        </tr>
                    @empty
                        <tr>
                            <td colspan="6" class="px-4 py-8 text-center text-gray-500">
                                <i class="fas fa-inbox text-4xl mb-2"></i>
                                <p>Tidak ada data kehadiran untuk periode ini</p>
                            </td>
                        </tr>
                    @endforelse
                </tbody>
            </table>
        </div>
    </div>
@endsection
