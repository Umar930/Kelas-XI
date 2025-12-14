@extends('admin.layouts.app')

@section('title', 'Dashboard')
@section('page-title', 'Dashboard')
@section('page-subtitle', 'Selamat datang di admin panel monitoring kelas')

@section('content')
    <!-- Stats Cards -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <!-- Users -->
        <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-gray-500 mb-1">Total Users</p>
                    <p class="text-3xl font-bold text-gray-800">{{ $stats['users'] }}</p>
                </div>
                <div class="w-14 h-14 bg-blue-100 rounded-xl flex items-center justify-center">
                    <i class="fas fa-users text-blue-500 text-2xl"></i>
                </div>
            </div>
        </div>
        
        <!-- Guru -->
        <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-gray-500 mb-1">Total Guru</p>
                    <p class="text-3xl font-bold text-gray-800">{{ $stats['gurus'] }}</p>
                </div>
                <div class="w-14 h-14 bg-green-100 rounded-xl flex items-center justify-center">
                    <i class="fas fa-chalkboard-teacher text-green-500 text-2xl"></i>
                </div>
            </div>
        </div>
        
        <!-- Siswa -->
        <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-gray-500 mb-1">Total Siswa</p>
                    <p class="text-3xl font-bold text-gray-800">{{ $stats['siswas'] }}</p>
                </div>
                <div class="w-14 h-14 bg-purple-100 rounded-xl flex items-center justify-center">
                    <i class="fas fa-user-graduate text-purple-500 text-2xl"></i>
                </div>
            </div>
        </div>
        
        <!-- Kelas -->
        <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-gray-500 mb-1">Total Kelas</p>
                    <p class="text-3xl font-bold text-gray-800">{{ $stats['kelas'] }}</p>
                </div>
                <div class="w-14 h-14 bg-orange-100 rounded-xl flex items-center justify-center">
                    <i class="fas fa-door-open text-orange-500 text-2xl"></i>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Kehadiran Guru Hari Ini -->
    <div class="bg-gradient-to-r from-indigo-500 to-purple-600 rounded-xl shadow-lg p-6 mb-8 text-white">
        <div class="flex items-center justify-between mb-4">
            <div>
                <h3 class="text-xl font-bold">
                    <i class="fas fa-clipboard-check mr-2"></i>
                    Kehadiran Guru Hari Ini
                </h3>
                <p class="text-indigo-200 text-sm">{{ now()->format('l, d F Y') }}</p>
            </div>
            <a href="{{ route('admin.monitoring.kehadiran-guru') }}" class="px-4 py-2 bg-white/20 hover:bg-white/30 rounded-lg text-sm transition">
                Lihat Detail <i class="fas fa-arrow-right ml-1"></i>
            </a>
        </div>
        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div class="bg-white/10 rounded-lg p-4 text-center">
                <p class="text-3xl font-bold">{{ $kehadiranHariIni['hadir'] }}</p>
                <p class="text-indigo-200 text-sm">Hadir</p>
            </div>
            <div class="bg-white/10 rounded-lg p-4 text-center">
                <p class="text-3xl font-bold">{{ $kehadiranHariIni['tidak_hadir'] }}</p>
                <p class="text-indigo-200 text-sm">Tidak Hadir</p>
            </div>
            <div class="bg-white/10 rounded-lg p-4 text-center">
                <p class="text-3xl font-bold">{{ $kehadiranHariIni['izin'] }}</p>
                <p class="text-indigo-200 text-sm">Izin</p>
            </div>
            <div class="bg-white/10 rounded-lg p-4 text-center">
                <p class="text-3xl font-bold">{{ $kehadiranHariIni['sakit'] }}</p>
                <p class="text-indigo-200 text-sm">Sakit</p>
            </div>
        </div>
    </div>
    
    <!-- Aktivitas Role -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <!-- Role Siswa -->
        <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
            <div class="flex items-center justify-between mb-4">
                <h3 class="text-lg font-semibold text-gray-800">
                    <i class="fas fa-user-graduate text-blue-500 mr-2"></i>
                    Aktivitas Siswa
                </h3>
                <a href="{{ route('admin.monitoring.siswa') }}" class="text-blue-500 hover:text-blue-700 text-sm">
                    Detail <i class="fas fa-arrow-right ml-1"></i>
                </a>
            </div>
            <div class="space-y-3">
                <div class="flex justify-between items-center p-3 bg-blue-50 rounded-lg">
                    <span class="text-gray-600">Total User Siswa</span>
                    <span class="font-bold text-blue-600">{{ $roleStats['siswa']['total'] }}</span>
                </div>
                <div class="flex justify-between items-center p-3 bg-green-50 rounded-lg">
                    <span class="text-gray-600">Input Kehadiran Hari Ini</span>
                    <span class="font-bold text-green-600">{{ $roleStats['siswa']['input_kehadiran_today'] }}</span>
                </div>
            </div>
        </div>
        
        <!-- Role Kurikulum -->
        <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
            <div class="flex items-center justify-between mb-4">
                <h3 class="text-lg font-semibold text-gray-800">
                    <i class="fas fa-tasks text-purple-500 mr-2"></i>
                    Aktivitas Kurikulum
                </h3>
                <a href="{{ route('admin.monitoring.kurikulum') }}" class="text-purple-500 hover:text-purple-700 text-sm">
                    Detail <i class="fas fa-arrow-right ml-1"></i>
                </a>
            </div>
            <div class="space-y-3">
                <div class="flex justify-between items-center p-3 bg-purple-50 rounded-lg">
                    <span class="text-gray-600">Total User Kurikulum</span>
                    <span class="font-bold text-purple-600">{{ $roleStats['kurikulum']['total'] }}</span>
                </div>
                <div class="flex justify-between items-center p-3 bg-yellow-50 rounded-lg">
                    <span class="text-gray-600">Input Izin/Sakit Hari Ini</span>
                    <span class="font-bold text-yellow-600">{{ $roleStats['kurikulum']['input_izin_sakit_today'] }}</span>
                </div>
                <div class="flex justify-between items-center p-3 bg-teal-50 rounded-lg">
                    <span class="text-gray-600">Guru Pengganti Hari Ini</span>
                    <span class="font-bold text-teal-600">{{ $roleStats['kurikulum']['guru_pengganti_today'] }}</span>
                </div>
            </div>
        </div>
        
        <!-- Role Kepala Sekolah -->
        <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
            <div class="flex items-center justify-between mb-4">
                <h3 class="text-lg font-semibold text-gray-800">
                    <i class="fas fa-user-shield text-indigo-500 mr-2"></i>
                    Kepala Sekolah
                </h3>
                <a href="{{ route('admin.monitoring.kepala-sekolah') }}" class="text-indigo-500 hover:text-indigo-700 text-sm">
                    Detail <i class="fas fa-arrow-right ml-1"></i>
                </a>
            </div>
            <div class="space-y-3">
                <div class="flex justify-between items-center p-3 bg-indigo-50 rounded-lg">
                    <span class="text-gray-600">Total User Kepsek</span>
                    <span class="font-bold text-indigo-600">{{ $roleStats['kepala_sekolah']['total'] }}</span>
                </div>
                <p class="text-sm text-gray-500 text-center py-2">
                    <i class="fas fa-info-circle mr-1"></i>
                    Kepala sekolah dapat melihat laporan kehadiran guru
                </p>
            </div>
        </div>
    </div>
    
    <!-- Kehadiran Guru dari Aplikasi -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
        <!-- Input oleh Siswa -->
        <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
            <div class="p-4 border-b border-gray-100 bg-blue-50">
                <h3 class="text-lg font-semibold text-blue-800">
                    <i class="fas fa-mobile-alt text-blue-500 mr-2"></i>
                    Kehadiran dari Siswa (Hari Ini)
                </h3>
                <p class="text-sm text-blue-600">Guru hadir/tidak hadir yang di-input siswa dari aplikasi</p>
            </div>
            <div class="max-h-80 overflow-y-auto">
                @forelse($kehadiranDariSiswa as $kehadiran)
                    <div class="p-4 border-b border-gray-100 hover:bg-gray-50">
                        <div class="flex items-center justify-between">
                            <div class="flex items-center">
                                <div class="w-10 h-10 bg-{{ $kehadiran->status == 'hadir' ? 'green' : 'red' }}-100 rounded-full flex items-center justify-center mr-3">
                                    <i class="fas fa-{{ $kehadiran->status == 'hadir' ? 'check' : 'times' }} text-{{ $kehadiran->status == 'hadir' ? 'green' : 'red' }}-500"></i>
                                </div>
                                <div>
                                    <p class="font-medium text-gray-800">{{ $kehadiran->guru->guru ?? '-' }}</p>
                                    <p class="text-sm text-gray-500">
                                        {{ $kehadiran->kelas->nama_kelas ?? '-' }} - {{ $kehadiran->mapel->mapel ?? '-' }}
                                    </p>
                                </div>
                            </div>
                            <div class="text-right">
                                <span class="px-2 py-1 rounded-full text-xs font-medium 
                                    {{ $kehadiran->status == 'hadir' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700' }}">
                                    {{ ucfirst(str_replace('_', ' ', $kehadiran->status)) }}
                                </span>
                                <p class="text-xs text-gray-400 mt-1">
                                    oleh: {{ $kehadiran->inputBySiswa->nama ?? '-' }}
                                </p>
                            </div>
                        </div>
                    </div>
                @empty
                    <div class="p-8 text-center text-gray-500">
                        <i class="fas fa-inbox text-4xl mb-2"></i>
                        <p>Belum ada input kehadiran dari siswa hari ini</p>
                    </div>
                @endforelse
            </div>
        </div>
        
        <!-- Input oleh Kurikulum (Izin/Sakit) -->
        <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
            <div class="p-4 border-b border-gray-100 bg-purple-50">
                <h3 class="text-lg font-semibold text-purple-800">
                    <i class="fas fa-user-tie text-purple-500 mr-2"></i>
                    Guru Izin/Sakit (Hari Ini)
                </h3>
                <p class="text-sm text-purple-600">Guru izin/sakit yang di-input kurikulum beserta namanya</p>
            </div>
            <div class="max-h-80 overflow-y-auto">
                @forelse($guruIzinSakitDariKurikulum as $kehadiran)
                    <div class="p-4 border-b border-gray-100 hover:bg-gray-50">
                        <div class="flex items-center justify-between">
                            <div class="flex items-center">
                                <div class="w-10 h-10 bg-{{ $kehadiran->status == 'izin' ? 'yellow' : 'orange' }}-100 rounded-full flex items-center justify-center mr-3">
                                    <i class="fas fa-{{ $kehadiran->status == 'izin' ? 'envelope' : 'medkit' }} text-{{ $kehadiran->status == 'izin' ? 'yellow' : 'orange' }}-500"></i>
                                </div>
                                <div>
                                    <p class="font-medium text-gray-800">{{ $kehadiran->guru->guru ?? '-' }}</p>
                                    <p class="text-sm text-gray-500">
                                        {{ $kehadiran->kelas->nama_kelas ?? '-' }} - {{ $kehadiran->mapel->mapel ?? '-' }}
                                    </p>
                                    <p class="text-xs text-gray-400 mt-1">{{ $kehadiran->keterangan ?? '-' }}</p>
                                </div>
                            </div>
                            <div class="text-right">
                                <span class="px-2 py-1 rounded-full text-xs font-medium 
                                    {{ $kehadiran->status == 'izin' ? 'bg-yellow-100 text-yellow-700' : 'bg-orange-100 text-orange-700' }}">
                                    {{ ucfirst($kehadiran->status) }}
                                </span>
                                <p class="text-xs text-gray-400 mt-1">
                                    oleh: {{ $kehadiran->inputByKurikulum->name ?? '-' }}
                                </p>
                            </div>
                        </div>
                    </div>
                @empty
                    <div class="p-8 text-center text-gray-500">
                        <i class="fas fa-inbox text-4xl mb-2"></i>
                        <p>Tidak ada guru izin/sakit hari ini</p>
                    </div>
                @endforelse
            </div>
        </div>
    </div>
    
    <!-- Second Row Stats -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <!-- Mapel -->
        <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-gray-500 mb-1">Mata Pelajaran</p>
                    <p class="text-3xl font-bold text-gray-800">{{ $stats['mapels'] }}</p>
                </div>
                <div class="w-14 h-14 bg-indigo-100 rounded-xl flex items-center justify-center">
                    <i class="fas fa-book text-indigo-500 text-2xl"></i>
                </div>
            </div>
        </div>
        
        <!-- Jadwal -->
        <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-gray-500 mb-1">Jadwal Kelas</p>
                    <p class="text-3xl font-bold text-gray-800">{{ $stats['jadwals'] }}</p>
                </div>
                <div class="w-14 h-14 bg-pink-100 rounded-xl flex items-center justify-center">
                    <i class="fas fa-clock text-pink-500 text-2xl"></i>
                </div>
            </div>
        </div>
        
        <!-- Guru Mengajar -->
        <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-gray-500 mb-1">Guru Mengajar</p>
                    <p class="text-3xl font-bold text-gray-800">{{ $stats['guru_mengajar'] }}</p>
                </div>
                <div class="w-14 h-14 bg-teal-100 rounded-xl flex items-center justify-center">
                    <i class="fas fa-user-tie text-teal-500 text-2xl"></i>
                </div>
            </div>
        </div>
        
        <!-- Guru Pengganti -->
        <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-gray-500 mb-1">Guru Pengganti</p>
                    <p class="text-3xl font-bold text-gray-800">{{ $stats['guru_pengganti'] }}</p>
                </div>
                <div class="w-14 h-14 bg-red-100 rounded-xl flex items-center justify-center">
                    <i class="fas fa-user-friends text-red-500 text-2xl"></i>
                </div>
            </div>
        </div>
    </div>
    
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <!-- Users by Role -->
        <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
            <h3 class="text-lg font-semibold text-gray-800 mb-4">
                <i class="fas fa-chart-pie text-indigo-500 mr-2"></i>
                Users berdasarkan Role
            </h3>
            <div class="space-y-3">
                @foreach($usersByRole as $role)
                    @php
                        $colors = [
                            'admin' => 'bg-red-500',
                            'siswa' => 'bg-blue-500',
                            'kurikulum' => 'bg-green-500',
                            'kepala_sekolah' => 'bg-purple-500',
                            'guru' => 'bg-orange-500',
                        ];
                        $bgColor = $colors[$role->role] ?? 'bg-gray-500';
                    @endphp
                    <div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                        <div class="flex items-center">
                            <span class="w-3 h-3 {{ $bgColor }} rounded-full mr-3"></span>
                            <span class="font-medium text-gray-700 capitalize">{{ str_replace('_', ' ', $role->role) }}</span>
                        </div>
                        <span class="px-3 py-1 bg-white rounded-full text-sm font-semibold text-gray-700 shadow-sm">
                            {{ $role->total }}
                        </span>
                    </div>
                @endforeach
            </div>
        </div>
        
        <!-- Recent Users -->
        <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
            <h3 class="text-lg font-semibold text-gray-800 mb-4">
                <i class="fas fa-user-clock text-indigo-500 mr-2"></i>
                User Terbaru
            </h3>
            <div class="space-y-3">
                @forelse($recentUsers as $user)
                    <div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                        <div class="flex items-center">
                            <div class="w-10 h-10 bg-indigo-500 rounded-full flex items-center justify-center text-white font-bold mr-3">
                                {{ strtoupper(substr($user->name, 0, 1)) }}
                            </div>
                            <div>
                                <p class="font-medium text-gray-800">{{ $user->name }}</p>
                                <p class="text-sm text-gray-500">{{ $user->email }}</p>
                            </div>
                        </div>
                        <span class="px-2 py-1 text-xs rounded-full 
                            @if($user->role == 'admin') bg-red-100 text-red-700
                            @elseif($user->role == 'siswa') bg-blue-100 text-blue-700
                            @elseif($user->role == 'kurikulum') bg-green-100 text-green-700
                            @elseif($user->role == 'kepala_sekolah') bg-purple-100 text-purple-700
                            @else bg-gray-100 text-gray-700
                            @endif">
                            {{ ucfirst(str_replace('_', ' ', $user->role)) }}
                        </span>
                    </div>
                @empty
                    <p class="text-gray-500 text-center py-4">Belum ada user</p>
                @endforelse
            </div>
        </div>
    </div>
@endsection
