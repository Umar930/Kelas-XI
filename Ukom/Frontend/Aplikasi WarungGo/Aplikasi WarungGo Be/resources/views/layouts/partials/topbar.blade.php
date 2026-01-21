<header class="sticky top-0 z-30 bg-white border-b border-gray-200">
    <div class="flex h-16 items-center gap-4 px-4 sm:px-6 lg:px-8">
        <!-- Mobile menu button -->
        <button 
            type="button" 
            @click="sidebarOpen = true"
            class="lg:hidden -m-2.5 p-2.5 text-gray-700 hover:text-gray-900"
        >
            <span class="sr-only">Open sidebar</span>
            <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path>
            </svg>
        </button>
        
        <!-- Separator -->
        <div class="h-6 w-px bg-gray-200 lg:hidden"></div>
        
        <!-- Page Title -->
        <div class="flex-1">
            <h1 class="text-lg font-semibold text-gray-900">
                @if(request()->routeIs('dashboard')) Dashboard
                @elseif(request()->routeIs('products')) Produk & Stok
                @elseif(request()->routeIs('transactions')) Transaksi
                @elseif(request()->routeIs('reports')) Laporan Keuangan
                @elseif(request()->routeIs('users')) User Management
                @elseif(request()->routeIs('profile')) Profil
                @elseif(request()->routeIs('settings')) Settings
                @else Dashboard
                @endif
            </h1>
        </div>
        
        <!-- Search -->
        <div class="hidden md:block w-72">
            <x-search-input placeholder="Cari menu, produk..." />
        </div>
        
        <!-- Right section -->
        <div class="flex items-center gap-3">
            <!-- Notifications -->
            <x-dropdown align="right" width="64">
                <x-slot name="trigger">
                    <button type="button" class="relative p-2 text-gray-400 hover:text-gray-600 focus:outline-none focus:ring-2 focus:ring-emerald-500 rounded-lg">
                        <span class="sr-only">View notifications</span>
                        <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"></path>
                        </svg>
                        <!-- Notification badge -->
                        <span class="absolute top-1.5 right-1.5 block h-2.5 w-2.5 rounded-full bg-red-500 ring-2 ring-white"></span>
                    </button>
                </x-slot>
                
                <x-slot name="content">
                    <div class="px-4 py-3 border-b border-gray-200">
                        <p class="text-sm font-semibold text-gray-900">Notifikasi</p>
                    </div>
                    <div class="max-h-96 overflow-y-auto">
                        <!-- Notification Items -->
                        <a href="#" class="block px-4 py-3 hover:bg-gray-50 border-b border-gray-100">
                            <div class="flex gap-3">
                                <div class="flex-shrink-0">
                                    <span class="inline-flex items-center justify-center w-8 h-8 bg-amber-100 text-amber-600 rounded-full">
                                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path>
                                        </svg>
                                    </span>
                                </div>
                                <div class="flex-1 min-w-0">
                                    <p class="text-sm font-medium text-gray-900">Stok Rendah</p>
                                    <p class="text-sm text-gray-500 truncate">5 produk memiliki stok di bawah minimum</p>
                                    <p class="text-xs text-gray-400 mt-1">5 menit yang lalu</p>
                                </div>
                            </div>
                        </a>
                        <a href="#" class="block px-4 py-3 hover:bg-gray-50 border-b border-gray-100">
                            <div class="flex gap-3">
                                <div class="flex-shrink-0">
                                    <span class="inline-flex items-center justify-center w-8 h-8 bg-green-100 text-green-600 rounded-full">
                                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                                        </svg>
                                    </span>
                                </div>
                                <div class="flex-1 min-w-0">
                                    <p class="text-sm font-medium text-gray-900">Transaksi Berhasil</p>
                                    <p class="text-sm text-gray-500 truncate">Transaksi #INV-2024-001 telah selesai</p>
                                    <p class="text-xs text-gray-400 mt-1">15 menit yang lalu</p>
                                </div>
                            </div>
                        </a>
                        <a href="#" class="block px-4 py-3 hover:bg-gray-50">
                            <div class="flex gap-3">
                                <div class="flex-shrink-0">
                                    <span class="inline-flex items-center justify-center w-8 h-8 bg-blue-100 text-blue-600 rounded-full">
                                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                                        </svg>
                                    </span>
                                </div>
                                <div class="flex-1 min-w-0">
                                    <p class="text-sm font-medium text-gray-900">User Baru</p>
                                    <p class="text-sm text-gray-500 truncate">Kasir baru telah ditambahkan</p>
                                    <p class="text-xs text-gray-400 mt-1">1 jam yang lalu</p>
                                </div>
                            </div>
                        </a>
                    </div>
                    <div class="px-4 py-3 border-t border-gray-200">
                        <a href="#" class="text-sm font-medium text-emerald-600 hover:text-emerald-700">Lihat semua notifikasi</a>
                    </div>
                </x-slot>
            </x-dropdown>
            
            <!-- Profile Dropdown -->
            <x-dropdown align="right" width="48">
                <x-slot name="trigger">
                    <button type="button" class="flex items-center gap-3 focus:outline-none">
                        <x-avatar alt="Admin User" size="sm" status="online" />
                        <div class="hidden md:block text-left">
                            <p class="text-sm font-medium text-gray-900">Admin User</p>
                            <p class="text-xs text-gray-500">Administrator</p>
                        </div>
                        <svg class="hidden md:block w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                        </svg>
                    </button>
                </x-slot>
                
                <x-slot name="content">
                    <div class="px-4 py-3 border-b border-gray-200">
                        <p class="text-sm font-medium text-gray-900">Admin User</p>
                        <p class="text-sm text-gray-500 truncate">admin@warunggo.com</p>
                    </div>
                    <div class="py-1">
                        <a href="{{ route('profile') }}" class="flex items-center gap-2 px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
                            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                            </svg>
                            Profil Saya
                        </a>
                        <a href="{{ route('settings') }}" class="flex items-center gap-2 px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
                            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"></path>
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>
                            </svg>
                            Pengaturan
                        </a>
                    </div>
                    <div class="border-t border-gray-200 py-1">
                        <a href="{{ route('login') }}" class="flex items-center gap-2 px-4 py-2 text-sm text-red-600 hover:bg-red-50">
                            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path>
                            </svg>
                            Keluar
                        </a>
                    </div>
                </x-slot>
            </x-dropdown>
        </div>
    </div>
</header>
