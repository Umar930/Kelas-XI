<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="csrf-token" content="{{ csrf_token() }}">
    <title>@yield('title', 'Admin Panel') - Monitoring Sekolah</title>
    
    <!-- Tailwind CSS -->
    <script src="https://cdn.tailwindcss.com"></script>
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    
    <style>
        body {
            font-family: 'Inter', sans-serif;
        }
        .sidebar {
            transition: all 0.3s ease;
        }
        .sidebar-item:hover {
            background-color: rgba(99, 102, 241, 0.1);
        }
        .sidebar-item.active {
            background-color: #6366f1;
            color: white;
        }
        .sidebar-item.active i {
            color: white;
        }
    </style>
    
    @stack('styles')
</head>
<body class="bg-gray-100">
    <div class="flex min-h-screen">
        <!-- Sidebar -->
        <aside class="sidebar w-64 bg-slate-800 text-white fixed h-full overflow-y-auto">
            <!-- Logo -->
            <div class="p-4 border-b border-slate-700">
                <div class="flex items-center space-x-3">
                    <div class="w-10 h-10 bg-indigo-500 rounded-lg flex items-center justify-center">
                        <i class="fas fa-school text-white"></i>
                    </div>
                    <div>
                        <h1 class="font-bold text-lg">Admin Panel</h1>
                        <p class="text-xs text-slate-400">Monitoring Sekolah</p>
                    </div>
                </div>
            </div>
            
            <!-- Navigation -->
            <nav class="p-4">
                <!-- Dashboard -->
                <a href="{{ route('admin.dashboard') }}" 
                   class="sidebar-item flex items-center space-x-3 px-4 py-3 rounded-lg mb-1 {{ request()->routeIs('admin.dashboard') ? 'active' : 'text-slate-300 hover:text-white' }}">
                    <i class="fas fa-home w-5"></i>
                    <span>Dashboard</span>
                </a>
                
                <!-- Master Data Section -->
                <div class="mt-6 mb-2">
                    <p class="px-4 text-xs font-semibold text-slate-500 uppercase tracking-wider">Master Data</p>
                </div>
                
                <a href="{{ route('admin.users.index') }}" 
                   class="sidebar-item flex items-center space-x-3 px-4 py-3 rounded-lg mb-1 {{ request()->routeIs('admin.users.*') ? 'active' : 'text-slate-300 hover:text-white' }}">
                    <i class="fas fa-users w-5"></i>
                    <span>Users</span>
                </a>
                
                <a href="{{ route('admin.gurus.index') }}" 
                   class="sidebar-item flex items-center space-x-3 px-4 py-3 rounded-lg mb-1 {{ request()->routeIs('admin.gurus.*') ? 'active' : 'text-slate-300 hover:text-white' }}">
                    <i class="fas fa-chalkboard-teacher w-5"></i>
                    <span>Guru</span>
                </a>
                
                <a href="{{ route('admin.mapels.index') }}" 
                   class="sidebar-item flex items-center space-x-3 px-4 py-3 rounded-lg mb-1 {{ request()->routeIs('admin.mapels.*') ? 'active' : 'text-slate-300 hover:text-white' }}">
                    <i class="fas fa-book w-5"></i>
                    <span>Mata Pelajaran</span>
                </a>
                
                <a href="{{ route('admin.kelas.index') }}" 
                   class="sidebar-item flex items-center space-x-3 px-4 py-3 rounded-lg mb-1 {{ request()->routeIs('admin.kelas.*') ? 'active' : 'text-slate-300 hover:text-white' }}">
                    <i class="fas fa-door-open w-5"></i>
                    <span>Kelas</span>
                </a>
                
                <a href="{{ route('admin.tahun-ajaran.index') }}" 
                   class="sidebar-item flex items-center space-x-3 px-4 py-3 rounded-lg mb-1 {{ request()->routeIs('admin.tahun-ajaran.*') ? 'active' : 'text-slate-300 hover:text-white' }}">
                    <i class="fas fa-calendar-alt w-5"></i>
                    <span>Tahun Ajaran</span>
                </a>
                
                <a href="{{ route('admin.siswas.index') }}" 
                   class="sidebar-item flex items-center space-x-3 px-4 py-3 rounded-lg mb-1 {{ request()->routeIs('admin.siswas.*') ? 'active' : 'text-slate-300 hover:text-white' }}">
                    <i class="fas fa-user-graduate w-5"></i>
                    <span>Siswa</span>
                </a>
                
                <!-- Akademik Section -->
                <div class="mt-6 mb-2">
                    <p class="px-4 text-xs font-semibold text-slate-500 uppercase tracking-wider">Akademik</p>
                </div>
                
                <a href="{{ route('admin.jadwals.index') }}" 
                   class="sidebar-item flex items-center space-x-3 px-4 py-3 rounded-lg mb-1 {{ request()->routeIs('admin.jadwals.*') ? 'active' : 'text-slate-300 hover:text-white' }}">
                    <i class="fas fa-clock w-5"></i>
                    <span>Jadwal</span>
                </a>
                
                <a href="{{ route('admin.guru-mengajar.index') }}" 
                   class="sidebar-item flex items-center space-x-3 px-4 py-3 rounded-lg mb-1 {{ request()->routeIs('admin.guru-mengajar.*') ? 'active' : 'text-slate-300 hover:text-white' }}">
                    <i class="fas fa-user-tie w-5"></i>
                    <span>Guru Mengajar</span>
                </a>
                
                <a href="{{ route('admin.guru-pengganti.index') }}" 
                   class="sidebar-item flex items-center space-x-3 px-4 py-3 rounded-lg mb-1 {{ request()->routeIs('admin.guru-pengganti.*') ? 'active' : 'text-slate-300 hover:text-white' }}">
                    <i class="fas fa-user-friends w-5"></i>
                    <span>Guru Pengganti</span>
                </a>
                
                <!-- Monitoring Section -->
                <div class="mt-6 mb-2">
                    <p class="px-4 text-xs font-semibold text-slate-500 uppercase tracking-wider">Monitoring Aplikasi</p>
                </div>
                
                <a href="{{ route('admin.monitoring.kehadiran-guru') }}" 
                   class="sidebar-item flex items-center space-x-3 px-4 py-3 rounded-lg mb-1 {{ request()->routeIs('admin.monitoring.kehadiran-guru*') ? 'active' : 'text-slate-300 hover:text-white' }}">
                    <i class="fas fa-clipboard-check w-5"></i>
                    <span>Kehadiran Guru</span>
                </a>
                
                <a href="{{ route('admin.monitoring.siswa') }}" 
                   class="sidebar-item flex items-center space-x-3 px-4 py-3 rounded-lg mb-1 {{ request()->routeIs('admin.monitoring.siswa') ? 'active' : 'text-slate-300 hover:text-white' }}">
                    <i class="fas fa-user-graduate w-5"></i>
                    <span>Monitoring Siswa</span>
                </a>
                
                <a href="{{ route('admin.monitoring.kurikulum') }}" 
                   class="sidebar-item flex items-center space-x-3 px-4 py-3 rounded-lg mb-1 {{ request()->routeIs('admin.monitoring.kurikulum') ? 'active' : 'text-slate-300 hover:text-white' }}">
                    <i class="fas fa-tasks w-5"></i>
                    <span>Monitoring Kurikulum</span>
                </a>
                
                <a href="{{ route('admin.monitoring.kepala-sekolah') }}" 
                   class="sidebar-item flex items-center space-x-3 px-4 py-3 rounded-lg mb-1 {{ request()->routeIs('admin.monitoring.kepala-sekolah') ? 'active' : 'text-slate-300 hover:text-white' }}">
                    <i class="fas fa-user-shield w-5"></i>
                    <span>Monitoring Kepsek</span>
                </a>
            </nav>
        </aside>
        
        <!-- Main Content -->
        <div class="flex-1 ml-64">
            <!-- Header -->
            <header class="bg-white shadow-sm border-b border-gray-200 sticky top-0 z-10">
                <div class="flex items-center justify-between px-6 py-4">
                    <div>
                        <h2 class="text-xl font-semibold text-gray-800">@yield('page-title', 'Dashboard')</h2>
                        <p class="text-sm text-gray-500">@yield('page-subtitle', 'Selamat datang di admin panel')</p>
                    </div>
                    
                    <div class="flex items-center space-x-4">
                        <div class="text-right">
                            <p class="font-medium text-gray-800">{{ auth()->user()->name ?? 'Administrator' }}</p>
                            <p class="text-xs text-gray-500 capitalize">{{ auth()->user()->role ?? 'Admin' }}</p>
                        </div>
                        <div class="w-10 h-10 bg-purple-500 rounded-full flex items-center justify-center text-white font-bold">
                            {{ strtoupper(substr(auth()->user()->name ?? 'A', 0, 1)) }}
                        </div>
                        <form action="{{ route('admin.logout') }}" method="POST" class="inline">
                            @csrf
                            <button type="submit" class="text-gray-500 hover:text-red-500 transition-colors" title="Logout">
                                <i class="fas fa-sign-out-alt text-xl"></i>
                            </button>
                        </form>
                    </div>
                </div>
            </header>
            
            <!-- Page Content -->
            <main class="p-6">
                <!-- Flash Messages -->
                @if(session('success'))
                    <div class="mb-4 p-4 bg-green-100 border border-green-400 text-green-700 rounded-lg flex items-center">
                        <i class="fas fa-check-circle mr-2"></i>
                        {{ session('success') }}
                    </div>
                @endif
                
                @if(session('error'))
                    <div class="mb-4 p-4 bg-red-100 border border-red-400 text-red-700 rounded-lg flex items-center">
                        <i class="fas fa-exclamation-circle mr-2"></i>
                        {{ session('error') }}
                    </div>
                @endif
                
                @if($errors->any())
                    <div class="mb-4 p-4 bg-red-100 border border-red-400 text-red-700 rounded-lg">
                        <div class="flex items-center mb-2">
                            <i class="fas fa-exclamation-triangle mr-2"></i>
                            <strong>Terdapat kesalahan:</strong>
                        </div>
                        <ul class="list-disc list-inside">
                            @foreach($errors->all() as $error)
                                <li>{{ $error }}</li>
                            @endforeach
                        </ul>
                    </div>
                @endif
                
                @yield('content')
            </main>
        </div>
    </div>
    
    @stack('scripts')
</body>
</html>
