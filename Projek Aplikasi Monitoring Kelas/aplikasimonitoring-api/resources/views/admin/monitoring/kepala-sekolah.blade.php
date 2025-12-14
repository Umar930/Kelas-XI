@extends('admin.layouts.app')

@section('title', 'Monitoring Role Kepala Sekolah')
@section('page-title', 'Monitoring Role Kepala Sekolah')
@section('page-subtitle', 'Laporan & statistik kehadiran guru untuk kepala sekolah')

@section('content')
    <!-- Filter -->
    <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100 mb-6">
        <form method="GET" class="flex flex-wrap gap-4 items-end">
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Tanggal Acuan</label>
                <input type="date" name="tanggal" value="{{ $tanggal }}" 
                    class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500">
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Periode</label>
                <select name="periode" class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500">
                    <option value="minggu" {{ $periode == 'minggu' ? 'selected' : '' }}>Minggu Ini</option>
                    <option value="bulan" {{ $periode == 'bulan' ? 'selected' : '' }}>Bulan Ini</option>
                </select>
            </div>
            <button type="submit" class="px-6 py-2 bg-indigo-500 text-white rounded-lg hover:bg-indigo-600 transition">
                <i class="fas fa-filter mr-2"></i>Filter
            </button>
            <a href="{{ route('admin.monitoring.kepala-sekolah') }}" class="px-6 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition">
                <i class="fas fa-sync mr-2"></i>Reset
            </a>
        </form>
        <p class="text-sm text-gray-500 mt-3">
            <i class="fas fa-calendar-alt mr-1"></i>
            Periode: {{ $startDate->format('d M Y') }} - {{ $endDate->format('d M Y') }}
        </p>
    </div>
    
    <!-- Stats Cards -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-6">
        <div class="bg-gradient-to-r from-indigo-500 to-indigo-600 rounded-xl shadow-lg p-6 text-white">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-indigo-100 text-sm">User Kepala Sekolah</p>
                    <p class="text-4xl font-bold">{{ $stats['total_kepala_sekolah'] }}</p>
                </div>
                <div class="w-14 h-14 bg-white/20 rounded-xl flex items-center justify-center">
                    <i class="fas fa-user-shield text-2xl"></i>
                </div>
            </div>
        </div>
        
        <div class="bg-gradient-to-r from-blue-500 to-blue-600 rounded-xl shadow-lg p-6 text-white">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-blue-100 text-sm">Total Kehadiran</p>
                    <p class="text-4xl font-bold">{{ $stats['total_kehadiran_periode'] }}</p>
                    <p class="text-blue-200 text-xs mt-1">Record periode ini</p>
                </div>
                <div class="w-14 h-14 bg-white/20 rounded-xl flex items-center justify-center">
                    <i class="fas fa-clipboard-list text-2xl"></i>
                </div>
            </div>
        </div>
        
        <div class="bg-gradient-to-r from-green-500 to-green-600 rounded-xl shadow-lg p-6 text-white">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-green-100 text-sm">Total Hadir</p>
                    <p class="text-4xl font-bold">{{ $stats['total_hadir'] }}</p>
                    <p class="text-green-200 text-xs mt-1">Periode ini</p>
                </div>
                <div class="w-14 h-14 bg-white/20 rounded-xl flex items-center justify-center">
                    <i class="fas fa-check-circle text-2xl"></i>
                </div>
            </div>
        </div>
        
        <div class="bg-gradient-to-r from-red-500 to-red-600 rounded-xl shadow-lg p-6 text-white">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-red-100 text-sm">Total Tidak Hadir</p>
                    <p class="text-4xl font-bold">{{ $stats['total_tidak_hadir'] }}</p>
                    <p class="text-red-200 text-xs mt-1">Termasuk izin & sakit</p>
                </div>
                <div class="w-14 h-14 bg-white/20 rounded-xl flex items-center justify-center">
                    <i class="fas fa-times-circle text-2xl"></i>
                </div>
            </div>
        </div>
    </div>
    
    <!-- User Kepala Sekolah & Chart Kehadiran -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-6">
        <!-- Daftar User Kepala Sekolah -->
        <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
            <div class="p-4 border-b border-gray-100 bg-indigo-50">
                <h3 class="text-lg font-semibold text-indigo-800">
                    <i class="fas fa-user-shield text-indigo-500 mr-2"></i>
                    User Kepala Sekolah
                </h3>
            </div>
            <div class="p-4">
                @forelse($kepalaSekolahUsers as $user)
                    <div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg mb-2">
                        <div class="flex items-center">
                            <div class="w-10 h-10 bg-indigo-100 rounded-full flex items-center justify-center mr-3">
                                <span class="text-indigo-600 font-bold">{{ strtoupper(substr($user->name, 0, 1)) }}</span>
                            </div>
                            <div>
                                <p class="font-medium text-gray-800">{{ $user->name }}</p>
                                <p class="text-sm text-gray-500">{{ $user->email }}</p>
                            </div>
                        </div>
                    </div>
                @empty
                    <p class="text-center text-gray-500 py-4">Tidak ada user kepala sekolah</p>
                @endforelse
            </div>
        </div>
        
        <!-- Statistik Kehadiran Periode -->
        <div class="lg:col-span-2 bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
            <div class="p-4 border-b border-gray-100">
                <h3 class="text-lg font-semibold text-gray-800">
                    <i class="fas fa-chart-pie text-indigo-500 mr-2"></i>
                    Statistik Kehadiran Periode
                </h3>
            </div>
            <div class="p-6">
                <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
                    <div class="text-center p-4 bg-green-50 rounded-lg">
                        <p class="text-3xl font-bold text-green-600">{{ $kehadiranPeriode->get('hadir')->total ?? 0 }}</p>
                        <p class="text-sm text-green-700 mt-1">Hadir</p>
                    </div>
                    <div class="text-center p-4 bg-red-50 rounded-lg">
                        <p class="text-3xl font-bold text-red-600">{{ $kehadiranPeriode->get('tidak_hadir')->total ?? 0 }}</p>
                        <p class="text-sm text-red-700 mt-1">Tidak Hadir</p>
                    </div>
                    <div class="text-center p-4 bg-yellow-50 rounded-lg">
                        <p class="text-3xl font-bold text-yellow-600">{{ $kehadiranPeriode->get('izin')->total ?? 0 }}</p>
                        <p class="text-sm text-yellow-700 mt-1">Izin</p>
                    </div>
                    <div class="text-center p-4 bg-orange-50 rounded-lg">
                        <p class="text-3xl font-bold text-orange-600">{{ $kehadiranPeriode->get('sakit')->total ?? 0 }}</p>
                        <p class="text-sm text-orange-700 mt-1">Sakit</p>
                    </div>
                </div>
                
                @php
                    $total = ($kehadiranPeriode->get('hadir')->total ?? 0) + 
                             ($kehadiranPeriode->get('tidak_hadir')->total ?? 0) + 
                             ($kehadiranPeriode->get('izin')->total ?? 0) + 
                             ($kehadiranPeriode->get('sakit')->total ?? 0);
                    $persenHadir = $total > 0 ? round((($kehadiranPeriode->get('hadir')->total ?? 0) / $total) * 100) : 0;
                @endphp
                
                <div class="mt-6">
                    <div class="flex justify-between text-sm mb-1">
                        <span class="text-gray-600">Persentase Kehadiran</span>
                        <span class="font-semibold text-gray-800">{{ $persenHadir }}%</span>
                    </div>
                    <div class="w-full bg-gray-200 rounded-full h-4">
                        <div class="bg-green-500 h-4 rounded-full" style="width: {{ $persenHadir }}%"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Guru dengan Ketidakhadiran Terbanyak & Kelas Problema -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">
        <!-- Guru dengan Ketidakhadiran Terbanyak -->
        <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
            <div class="p-4 border-b border-gray-100 bg-red-50">
                <h3 class="text-lg font-semibold text-red-800">
                    <i class="fas fa-exclamation-triangle text-red-500 mr-2"></i>
                    Guru dengan Ketidakhadiran Terbanyak
                </h3>
                <p class="text-sm text-red-600 mt-1">Periode: {{ $startDate->format('d M') }} - {{ $endDate->format('d M Y') }}</p>
            </div>
            <div class="overflow-x-auto">
                <table class="w-full">
                    <thead class="bg-gray-50">
                        <tr>
                            <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">#</th>
                            <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Guru</th>
                            <th class="px-4 py-3 text-center text-xs font-semibold text-gray-600 uppercase">Tidak Hadir</th>
                            <th class="px-4 py-3 text-center text-xs font-semibold text-gray-600 uppercase">Aksi</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-gray-100">
                        @forelse($guruTidakHadirTerbanyak as $index => $item)
                            <tr class="hover:bg-gray-50">
                                <td class="px-4 py-3 text-sm text-gray-500">{{ $index + 1 }}</td>
                                <td class="px-4 py-3">
                                    <div class="flex items-center">
                                        <div class="w-8 h-8 bg-red-100 rounded-full flex items-center justify-center mr-3">
                                            <span class="text-red-600 font-semibold text-sm">{{ strtoupper(substr($item->guru->guru ?? 'G', 0, 1)) }}</span>
                                        </div>
                                        <div>
                                            <p class="font-medium text-gray-800">{{ $item->guru->guru ?? '-' }}</p>
                                            <p class="text-xs text-gray-500">{{ $item->guru->kode_guru ?? '-' }}</p>
                                        </div>
                                    </div>
                                </td>
                                <td class="px-4 py-3 text-center">
                                    <span class="px-3 py-1 bg-red-100 text-red-700 rounded-full text-sm font-bold">
                                        {{ $item->total_tidak_hadir }}x
                                    </span>
                                </td>
                                <td class="px-4 py-3 text-center">
                                    <a href="{{ route('admin.monitoring.kehadiran-guru.detail', $item->guru_id) }}" 
                                       class="text-indigo-500 hover:text-indigo-700" title="Detail">
                                        <i class="fas fa-eye"></i>
                                    </a>
                                </td>
                            </tr>
                        @empty
                            <tr>
                                <td colspan="4" class="px-4 py-8 text-center text-gray-500">
                                    <i class="fas fa-check-circle text-green-500 text-4xl mb-2"></i>
                                    <p>Tidak ada guru dengan ketidakhadiran</p>
                                </td>
                            </tr>
                        @endforelse
                    </tbody>
                </table>
            </div>
        </div>
        
        <!-- Kelas dengan Masalah Kehadiran -->
        <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
            <div class="p-4 border-b border-gray-100 bg-orange-50">
                <h3 class="text-lg font-semibold text-orange-800">
                    <i class="fas fa-school text-orange-500 mr-2"></i>
                    Kelas dengan Masalah Kehadiran
                </h3>
                <p class="text-sm text-orange-600 mt-1">Kelas yang paling banyak guru tidak hadir</p>
            </div>
            <div class="overflow-x-auto">
                <table class="w-full">
                    <thead class="bg-gray-50">
                        <tr>
                            <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">#</th>
                            <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Kelas</th>
                            <th class="px-4 py-3 text-center text-xs font-semibold text-gray-600 uppercase">Total Masalah</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-gray-100">
                        @forelse($kelasProblema as $index => $item)
                            <tr class="hover:bg-gray-50">
                                <td class="px-4 py-3 text-sm text-gray-500">{{ $index + 1 }}</td>
                                <td class="px-4 py-3">
                                    <span class="px-2 py-1 bg-gray-100 rounded font-medium">{{ $item->kelas->nama_kelas ?? '-' }}</span>
                                </td>
                                <td class="px-4 py-3 text-center">
                                    <span class="px-3 py-1 bg-orange-100 text-orange-700 rounded-full text-sm font-bold">
                                        {{ $item->total_masalah }}x
                                    </span>
                                </td>
                            </tr>
                        @empty
                            <tr>
                                <td colspan="3" class="px-4 py-8 text-center text-gray-500">
                                    <i class="fas fa-check-circle text-green-500 text-4xl mb-2"></i>
                                    <p>Tidak ada masalah kehadiran</p>
                                </td>
                            </tr>
                        @endforelse
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    
    <!-- Ringkasan Harian -->
    <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        <div class="p-4 border-b border-gray-100">
            <h3 class="text-lg font-semibold text-gray-800">
                <i class="fas fa-calendar-week text-indigo-500 mr-2"></i>
                Ringkasan Harian
            </h3>
        </div>
        <div class="overflow-x-auto">
            <table class="w-full">
                <thead class="bg-gray-50">
                    <tr>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Tanggal</th>
                        <th class="px-4 py-3 text-center text-xs font-semibold text-gray-600 uppercase">Hadir</th>
                        <th class="px-4 py-3 text-center text-xs font-semibold text-gray-600 uppercase">Tidak Hadir</th>
                        <th class="px-4 py-3 text-center text-xs font-semibold text-gray-600 uppercase">Izin</th>
                        <th class="px-4 py-3 text-center text-xs font-semibold text-gray-600 uppercase">Sakit</th>
                        <th class="px-4 py-3 text-center text-xs font-semibold text-gray-600 uppercase">Total</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-gray-100">
                    @forelse($ringkasanHarian as $tanggalHari => $data)
                        @php
                            $hadir = $data->where('status', 'hadir')->first()->total ?? 0;
                            $tidakHadir = $data->where('status', 'tidak_hadir')->first()->total ?? 0;
                            $izin = $data->where('status', 'izin')->first()->total ?? 0;
                            $sakit = $data->where('status', 'sakit')->first()->total ?? 0;
                            $totalHari = $hadir + $tidakHadir + $izin + $sakit;
                        @endphp
                        <tr class="hover:bg-gray-50">
                            <td class="px-4 py-3 font-medium text-gray-800">
                                {{ \Carbon\Carbon::parse($tanggalHari)->format('l, d M Y') }}
                            </td>
                            <td class="px-4 py-3 text-center">
                                <span class="px-2 py-1 bg-green-100 text-green-700 rounded text-sm">{{ $hadir }}</span>
                            </td>
                            <td class="px-4 py-3 text-center">
                                <span class="px-2 py-1 bg-red-100 text-red-700 rounded text-sm">{{ $tidakHadir }}</span>
                            </td>
                            <td class="px-4 py-3 text-center">
                                <span class="px-2 py-1 bg-yellow-100 text-yellow-700 rounded text-sm">{{ $izin }}</span>
                            </td>
                            <td class="px-4 py-3 text-center">
                                <span class="px-2 py-1 bg-orange-100 text-orange-700 rounded text-sm">{{ $sakit }}</span>
                            </td>
                            <td class="px-4 py-3 text-center font-semibold text-gray-800">{{ $totalHari }}</td>
                        </tr>
                    @empty
                        <tr>
                            <td colspan="6" class="px-4 py-8 text-center text-gray-500">
                                <i class="fas fa-inbox text-4xl mb-2"></i>
                                <p>Tidak ada data untuk periode ini</p>
                            </td>
                        </tr>
                    @endforelse
                </tbody>
            </table>
        </div>
    </div>
@endsection
