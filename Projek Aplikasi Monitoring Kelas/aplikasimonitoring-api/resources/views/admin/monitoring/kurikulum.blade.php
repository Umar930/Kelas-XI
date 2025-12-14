@extends('admin.layouts.app')

@section('title', 'Monitoring Role Kurikulum')
@section('page-title', 'Monitoring Role Kurikulum')
@section('page-subtitle', 'Pantau aktivitas kurikulum yang menginput izin/sakit guru dan assign guru pengganti')

@section('content')
    <!-- Filter -->
    <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100 mb-6">
        <form method="GET" class="flex flex-wrap gap-4 items-end">
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Tanggal</label>
                <input type="date" name="tanggal" value="{{ $tanggal }}" 
                    class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500">
            </div>
            <button type="submit" class="px-6 py-2 bg-indigo-500 text-white rounded-lg hover:bg-indigo-600 transition">
                <i class="fas fa-filter mr-2"></i>Filter
            </button>
            <a href="{{ route('admin.monitoring.kurikulum') }}" class="px-6 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition">
                <i class="fas fa-sync mr-2"></i>Reset
            </a>
        </form>
    </div>
    
    <!-- Stats Cards -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-6">
        <div class="bg-gradient-to-r from-purple-500 to-purple-600 rounded-xl shadow-lg p-6 text-white">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-purple-100 text-sm">Total User Kurikulum</p>
                    <p class="text-4xl font-bold">{{ $stats['total_kurikulum'] }}</p>
                </div>
                <div class="w-14 h-14 bg-white/20 rounded-xl flex items-center justify-center">
                    <i class="fas fa-user-tie text-2xl"></i>
                </div>
            </div>
        </div>
        
        <div class="bg-gradient-to-r from-yellow-500 to-orange-500 rounded-xl shadow-lg p-6 text-white">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-yellow-100 text-sm">Input Izin/Sakit</p>
                    <p class="text-4xl font-bold">{{ $stats['input_izin_sakit_today'] }}</p>
                    <p class="text-yellow-200 text-xs mt-1">Hari ini</p>
                </div>
                <div class="w-14 h-14 bg-white/20 rounded-xl flex items-center justify-center">
                    <i class="fas fa-envelope-open-text text-2xl"></i>
                </div>
            </div>
        </div>
        
        <div class="bg-gradient-to-r from-teal-500 to-teal-600 rounded-xl shadow-lg p-6 text-white">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-teal-100 text-sm">Guru Pengganti</p>
                    <p class="text-4xl font-bold">{{ $stats['guru_pengganti_today'] }}</p>
                    <p class="text-teal-200 text-xs mt-1">Di-assign hari ini</p>
                </div>
                <div class="w-14 h-14 bg-white/20 rounded-xl flex items-center justify-center">
                    <i class="fas fa-user-friends text-2xl"></i>
                </div>
            </div>
        </div>
        
        <div class="bg-gradient-to-r from-pink-500 to-pink-600 rounded-xl shadow-lg p-6 text-white">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-pink-100 text-sm">Notifikasi Terkirim</p>
                    <p class="text-4xl font-bold">{{ $stats['notifikasi_today'] }}</p>
                    <p class="text-pink-200 text-xs mt-1">Ke siswa hari ini</p>
                </div>
                <div class="w-14 h-14 bg-white/20 rounded-xl flex items-center justify-center">
                    <i class="fas fa-bell text-2xl"></i>
                </div>
            </div>
        </div>
    </div>
    
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">
        <!-- Daftar User Kurikulum -->
        <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
            <div class="p-4 border-b border-gray-100 bg-purple-50">
                <h3 class="text-lg font-semibold text-purple-800">
                    <i class="fas fa-users text-purple-500 mr-2"></i>
                    User Kurikulum
                </h3>
            </div>
            <div class="p-4">
                @forelse($kurikulumUsers as $user)
                    <div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg mb-2">
                        <div class="flex items-center">
                            <div class="w-10 h-10 bg-purple-100 rounded-full flex items-center justify-center mr-3">
                                <span class="text-purple-600 font-bold">{{ strtoupper(substr($user->name, 0, 1)) }}</span>
                            </div>
                            <div>
                                <p class="font-medium text-gray-800">{{ $user->name }}</p>
                                <p class="text-sm text-gray-500">{{ $user->email }}</p>
                            </div>
                        </div>
                        <span class="px-2 py-1 bg-purple-100 text-purple-700 rounded-full text-xs">Kurikulum</span>
                    </div>
                @empty
                    <p class="text-center text-gray-500 py-4">Tidak ada user kurikulum</p>
                @endforelse
            </div>
        </div>
        
        <!-- Notifikasi Terkirim -->
        <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
            <div class="p-4 border-b border-gray-100 bg-pink-50">
                <h3 class="text-lg font-semibold text-pink-800">
                    <i class="fas fa-bell text-pink-500 mr-2"></i>
                    Notifikasi ke Siswa - {{ \Carbon\Carbon::parse($tanggal)->format('d F Y') }}
                </h3>
            </div>
            <div class="p-4 max-h-80 overflow-y-auto">
                @forelse($notifikasiList as $notif)
                    <div class="p-3 bg-gray-50 rounded-lg mb-2">
                        <div class="flex items-start justify-between">
                            <div class="flex-1">
                                <div class="flex items-center mb-1">
                                    <span class="px-2 py-0.5 rounded text-xs font-medium mr-2
                                        {{ $notif->tipe == 'izin' ? 'bg-yellow-100 text-yellow-700' : 'bg-orange-100 text-orange-700' }}">
                                        {{ ucfirst($notif->tipe) }}
                                    </span>
                                    <span class="text-xs text-gray-500">{{ $notif->created_at->format('H:i') }}</span>
                                </div>
                                <p class="text-sm text-gray-700">{{ $notif->pesan }}</p>
                                <p class="text-xs text-gray-500 mt-1">
                                    <i class="fas fa-school mr-1"></i>{{ $notif->kelas->nama_kelas ?? '-' }}
                                </p>
                            </div>
                        </div>
                    </div>
                @empty
                    <p class="text-center text-gray-500 py-4">Tidak ada notifikasi hari ini</p>
                @endforelse
            </div>
        </div>
    </div>
    
    <!-- Input Izin/Sakit oleh Kurikulum -->
    <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden mb-6">
        <div class="p-4 border-b border-gray-100 bg-yellow-50">
            <h3 class="text-lg font-semibold text-yellow-800">
                <i class="fas fa-envelope-open-text text-yellow-500 mr-2"></i>
                Guru Izin/Sakit (Input Kurikulum) - {{ \Carbon\Carbon::parse($tanggal)->format('d F Y') }}
            </h3>
            <p class="text-sm text-yellow-600 mt-1">Data izin/sakit guru yang di-input oleh kurikulum beserta nama guru</p>
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
                    </tr>
                </thead>
                <tbody class="divide-y divide-gray-100">
                    @forelse($inputIzinSakit as $input)
                        <tr class="hover:bg-gray-50">
                            <td class="px-4 py-3">
                                <div class="flex items-center">
                                    <div class="w-8 h-8 bg-indigo-100 rounded-full flex items-center justify-center mr-3">
                                        <span class="text-indigo-600 font-semibold text-sm">{{ strtoupper(substr($input->guru->guru ?? 'G', 0, 1)) }}</span>
                                    </div>
                                    <div>
                                        <p class="font-medium text-gray-800">{{ $input->guru->guru ?? '-' }}</p>
                                        <p class="text-xs text-gray-500">{{ $input->guru->kode_guru ?? '-' }}</p>
                                    </div>
                                </div>
                            </td>
                            <td class="px-4 py-3">
                                <span class="px-2 py-1 bg-gray-100 rounded text-sm">{{ $input->kelas->nama_kelas ?? '-' }}</span>
                            </td>
                            <td class="px-4 py-3 text-sm text-gray-600">{{ $input->mapel->mapel ?? '-' }}</td>
                            <td class="px-4 py-3">
                                @if($input->status == 'izin')
                                    <span class="px-2 py-1 bg-yellow-100 text-yellow-700 rounded-full text-xs font-medium">
                                        <i class="fas fa-envelope mr-1"></i>Izin
                                    </span>
                                @elseif($input->status == 'sakit')
                                    <span class="px-2 py-1 bg-orange-100 text-orange-700 rounded-full text-xs font-medium">
                                        <i class="fas fa-medkit mr-1"></i>Sakit
                                    </span>
                                @endif
                            </td>
                            <td class="px-4 py-3 text-sm text-gray-600 max-w-xs truncate">{{ $input->keterangan ?? '-' }}</td>
                            <td class="px-4 py-3">
                                <div class="flex items-center">
                                    <span class="w-2 h-2 bg-purple-500 rounded-full mr-2"></span>
                                    <p class="text-sm font-medium text-purple-600">{{ $input->inputByKurikulum->name ?? '-' }}</p>
                                </div>
                            </td>
                            <td class="px-4 py-3 text-sm text-gray-500">{{ $input->created_at->format('H:i') }}</td>
                        </tr>
                    @empty
                        <tr>
                            <td colspan="7" class="px-4 py-8 text-center text-gray-500">
                                <i class="fas fa-inbox text-4xl mb-2"></i>
                                <p>Tidak ada input izin/sakit dari kurikulum hari ini</p>
                            </td>
                        </tr>
                    @endforelse
                </tbody>
            </table>
        </div>
    </div>
    
    <!-- Guru Pengganti yang Di-assign -->
    <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        <div class="p-4 border-b border-gray-100 bg-teal-50">
            <h3 class="text-lg font-semibold text-teal-800">
                <i class="fas fa-user-friends text-teal-500 mr-2"></i>
                Guru Pengganti yang Di-assign - {{ \Carbon\Carbon::parse($tanggal)->format('d F Y') }}
            </h3>
        </div>
        
        <div class="overflow-x-auto">
            <table class="w-full">
                <thead class="bg-gray-50">
                    <tr>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Guru Asli</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Guru Pengganti</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Kelas</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Mapel</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Status</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase">Waktu</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-gray-100">
                    @forelse($guruPenggantiList as $gp)
                        <tr class="hover:bg-gray-50">
                            <td class="px-4 py-3">
                                <p class="font-medium text-gray-800">{{ $gp->kehadiranGuru->guru->guru ?? '-' }}</p>
                            </td>
                            <td class="px-4 py-3">
                                <div class="flex items-center">
                                    <div class="w-8 h-8 bg-teal-100 rounded-full flex items-center justify-center mr-3">
                                        <span class="text-teal-600 font-semibold text-sm">{{ strtoupper(substr($gp->guruPengganti->guru ?? 'G', 0, 1)) }}</span>
                                    </div>
                                    <p class="font-medium text-teal-700">{{ $gp->guruPengganti->guru ?? '-' }}</p>
                                </div>
                            </td>
                            <td class="px-4 py-3">
                                <span class="px-2 py-1 bg-gray-100 rounded text-sm">{{ $gp->kehadiranGuru->kelas->nama_kelas ?? '-' }}</span>
                            </td>
                            <td class="px-4 py-3 text-sm text-gray-600">{{ $gp->kehadiranGuru->mapel->mapel ?? '-' }}</td>
                            <td class="px-4 py-3">
                                <span class="px-2 py-1 bg-{{ $gp->status == 'aktif' ? 'green' : ($gp->status == 'pending' ? 'yellow' : 'red') }}-100 
                                    text-{{ $gp->status == 'aktif' ? 'green' : ($gp->status == 'pending' ? 'yellow' : 'red') }}-700 rounded-full text-xs font-medium">
                                    {{ ucfirst($gp->status ?? 'pending') }}
                                </span>
                            </td>
                            <td class="px-4 py-3 text-sm text-gray-500">{{ $gp->created_at->format('H:i') }}</td>
                        </tr>
                    @empty
                        <tr>
                            <td colspan="6" class="px-4 py-8 text-center text-gray-500">
                                <i class="fas fa-inbox text-4xl mb-2"></i>
                                <p>Tidak ada guru pengganti yang di-assign hari ini</p>
                            </td>
                        </tr>
                    @endforelse
                </tbody>
            </table>
        </div>
    </div>
@endsection
