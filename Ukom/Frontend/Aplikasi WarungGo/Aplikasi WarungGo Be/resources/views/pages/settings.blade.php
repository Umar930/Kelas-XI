<x-layouts.app title="Pengaturan">
    <div class="max-w-4xl mx-auto">
        <!-- Page Header -->
        <div class="mb-8">
            <h1 class="text-2xl font-bold text-gray-900">Pengaturan</h1>
            <p class="mt-1 text-sm text-gray-500">Kelola preferensi dan konfigurasi aplikasi Anda.</p>
        </div>

        <div class="space-y-6" x-data="{ 
            darkMode: false, 
            notifications: {
                email: true,
                push: true,
                sms: false,
                orderUpdates: true,
                stockAlerts: true,
                promotions: false,
                reports: true
            },
            language: 'id'
        }">
            <!-- Appearance Settings -->
            <x-card>
                <x-slot name="header">
                    <div class="flex items-center gap-3">
                        <div class="p-2 bg-purple-100 rounded-lg">
                            <svg class="w-5 h-5 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21a4 4 0 01-4-4V5a2 2 0 012-2h4a2 2 0 012 2v12a4 4 0 01-4 4zm0 0h12a2 2 0 002-2v-4a2 2 0 00-2-2h-2.343M11 7.343l1.657-1.657a2 2 0 012.828 0l2.829 2.829a2 2 0 010 2.828l-8.486 8.485M7 17h.01"></path>
                            </svg>
                        </div>
                        <div>
                            <h3 class="text-lg font-semibold text-gray-900">Tampilan</h3>
                            <p class="text-sm text-gray-500">Kustomisasi tampilan aplikasi</p>
                        </div>
                    </div>
                </x-slot>

                <div class="space-y-6">
                    <!-- Dark Mode Toggle -->
                    <div class="flex items-center justify-between">
                        <div class="flex items-center gap-3">
                            <div class="p-2 bg-gray-100 rounded-lg">
                                <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z"></path>
                                </svg>
                            </div>
                            <div>
                                <p class="text-sm font-medium text-gray-900">Mode Gelap</p>
                                <p class="text-sm text-gray-500">Aktifkan tampilan gelap untuk kenyamanan mata</p>
                            </div>
                        </div>
                        <x-toggle name="dark_mode" x-model="darkMode" />
                    </div>

                    <!-- Theme Color -->
                    <div class="flex items-center justify-between">
                        <div class="flex items-center gap-3">
                            <div class="p-2 bg-emerald-100 rounded-lg">
                                <svg class="w-5 h-5 text-emerald-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.871 4A17.926 17.926 0 003 12c0 2.874.673 5.59 1.871 8m14.13 0a17.926 17.926 0 001.87-8c0-2.874-.673-5.59-1.87-8M9 9h1.246a1 1 0 01.961.725l1.586 5.55a1 1 0 00.961.725H15m1-7h-.08a2 2 0 00-1.519.698L9.6 15.302A2 2 0 018.08 16H8"></path>
                                </svg>
                            </div>
                            <div>
                                <p class="text-sm font-medium text-gray-900">Warna Tema</p>
                                <p class="text-sm text-gray-500">Pilih warna utama aplikasi</p>
                            </div>
                        </div>
                        <div class="flex items-center gap-2">
                            <button class="w-8 h-8 rounded-full bg-emerald-500 ring-2 ring-emerald-500 ring-offset-2 focus:outline-none" title="Emerald"></button>
                            <button class="w-8 h-8 rounded-full bg-blue-500 hover:ring-2 hover:ring-blue-500 hover:ring-offset-2 focus:outline-none" title="Blue"></button>
                            <button class="w-8 h-8 rounded-full bg-purple-500 hover:ring-2 hover:ring-purple-500 hover:ring-offset-2 focus:outline-none" title="Purple"></button>
                            <button class="w-8 h-8 rounded-full bg-rose-500 hover:ring-2 hover:ring-rose-500 hover:ring-offset-2 focus:outline-none" title="Rose"></button>
                            <button class="w-8 h-8 rounded-full bg-amber-500 hover:ring-2 hover:ring-amber-500 hover:ring-offset-2 focus:outline-none" title="Amber"></button>
                        </div>
                    </div>

                    <!-- Sidebar Compact -->
                    <div class="flex items-center justify-between">
                        <div class="flex items-center gap-3">
                            <div class="p-2 bg-gray-100 rounded-lg">
                                <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h7"></path>
                                </svg>
                            </div>
                            <div>
                                <p class="text-sm font-medium text-gray-900">Sidebar Compact</p>
                                <p class="text-sm text-gray-500">Tampilkan sidebar dalam mode ringkas</p>
                            </div>
                        </div>
                        <x-toggle name="sidebar_compact" />
                    </div>
                </div>
            </x-card>

            <!-- Notification Settings -->
            <x-card>
                <x-slot name="header">
                    <div class="flex items-center gap-3">
                        <div class="p-2 bg-blue-100 rounded-lg">
                            <svg class="w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"></path>
                            </svg>
                        </div>
                        <div>
                            <h3 class="text-lg font-semibold text-gray-900">Notifikasi</h3>
                            <p class="text-sm text-gray-500">Kelola preferensi notifikasi Anda</p>
                        </div>
                    </div>
                </x-slot>

                <div class="space-y-6">
                    <!-- Notification Channels -->
                    <div class="pb-4 border-b border-gray-200">
                        <p class="text-sm font-medium text-gray-700 mb-3">Channel Notifikasi</p>
                        <div class="space-y-4">
                            <div class="flex items-center justify-between">
                                <div>
                                    <p class="text-sm font-medium text-gray-900">Email</p>
                                    <p class="text-sm text-gray-500">Terima notifikasi via email</p>
                                </div>
                                <x-toggle name="notif_email" x-model="notifications.email" />
                            </div>
                            <div class="flex items-center justify-between">
                                <div>
                                    <p class="text-sm font-medium text-gray-900">Push Notification</p>
                                    <p class="text-sm text-gray-500">Terima notifikasi di browser</p>
                                </div>
                                <x-toggle name="notif_push" x-model="notifications.push" />
                            </div>
                            <div class="flex items-center justify-between">
                                <div>
                                    <p class="text-sm font-medium text-gray-900">SMS</p>
                                    <p class="text-sm text-gray-500">Terima notifikasi via SMS</p>
                                </div>
                                <x-toggle name="notif_sms" x-model="notifications.sms" />
                            </div>
                        </div>
                    </div>

                    <!-- Notification Types -->
                    <div>
                        <p class="text-sm font-medium text-gray-700 mb-3">Jenis Notifikasi</p>
                        <div class="space-y-4">
                            <x-checkbox name="order_updates" label="Update Pesanan" description="Notifikasi saat ada pesanan baru atau perubahan status" x-model="notifications.orderUpdates" />
                            <x-checkbox name="stock_alerts" label="Alert Stok" description="Notifikasi saat stok produk menipis atau habis" x-model="notifications.stockAlerts" />
                            <x-checkbox name="promotions" label="Promosi & Marketing" description="Informasi tentang promosi dan tips bisnis" x-model="notifications.promotions" />
                            <x-checkbox name="reports" label="Laporan Berkala" description="Ringkasan laporan harian/mingguan via email" x-model="notifications.reports" />
                        </div>
                    </div>
                </div>
            </x-card>

            <!-- Language Settings -->
            <x-card>
                <x-slot name="header">
                    <div class="flex items-center gap-3">
                        <div class="p-2 bg-amber-100 rounded-lg">
                            <svg class="w-5 h-5 text-amber-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5h12M9 3v2m1.048 9.5A18.022 18.022 0 016.412 9m6.088 9h7M11 21l5-10 5 10M12.751 5C11.783 10.77 8.07 15.61 3 18.129"></path>
                            </svg>
                        </div>
                        <div>
                            <h3 class="text-lg font-semibold text-gray-900">Bahasa & Region</h3>
                            <p class="text-sm text-gray-500">Pengaturan bahasa dan lokasi</p>
                        </div>
                    </div>
                </x-slot>

                <div class="space-y-6">
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <x-select name="language" label="Bahasa" x-model="language">
                            <option value="id">🇮🇩 Bahasa Indonesia</option>
                            <option value="en">🇺🇸 English</option>
                        </x-select>
                        
                        <x-select name="timezone" label="Zona Waktu">
                            <option value="Asia/Jakarta">Asia/Jakarta (WIB)</option>
                            <option value="Asia/Makassar">Asia/Makassar (WITA)</option>
                            <option value="Asia/Jayapura">Asia/Jayapura (WIT)</option>
                        </x-select>
                    </div>
                    
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <x-select name="currency" label="Mata Uang">
                            <option value="IDR">IDR - Rupiah Indonesia</option>
                            <option value="USD">USD - US Dollar</option>
                        </x-select>
                        
                        <x-select name="date_format" label="Format Tanggal">
                            <option value="DD/MM/YYYY">DD/MM/YYYY (21/01/2024)</option>
                            <option value="MM/DD/YYYY">MM/DD/YYYY (01/21/2024)</option>
                            <option value="YYYY-MM-DD">YYYY-MM-DD (2024-01-21)</option>
                        </x-select>
                    </div>
                </div>
            </x-card>

            <!-- System Information -->
            <x-card>
                <x-slot name="header">
                    <div class="flex items-center gap-3">
                        <div class="p-2 bg-gray-100 rounded-lg">
                            <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.75 17L9 20l-1 1h8l-1-1-.75-3M3 13h18M5 17h14a2 2 0 002-2V5a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"></path>
                            </svg>
                        </div>
                        <div>
                            <h3 class="text-lg font-semibold text-gray-900">Informasi Sistem</h3>
                            <p class="text-sm text-gray-500">Informasi tentang aplikasi dan sistem</p>
                        </div>
                    </div>
                </x-slot>

                <div class="space-y-4">
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div class="p-4 bg-gray-50 rounded-lg">
                            <p class="text-sm text-gray-500">Versi Aplikasi</p>
                            <p class="text-lg font-semibold text-gray-900">WarungGo v1.0.0</p>
                        </div>
                        <div class="p-4 bg-gray-50 rounded-lg">
                            <p class="text-sm text-gray-500">Laravel Framework</p>
                            <p class="text-lg font-semibold text-gray-900">v12.x</p>
                        </div>
                        <div class="p-4 bg-gray-50 rounded-lg">
                            <p class="text-sm text-gray-500">PHP Version</p>
                            <p class="text-lg font-semibold text-gray-900">8.2.x</p>
                        </div>
                        <div class="p-4 bg-gray-50 rounded-lg">
                            <p class="text-sm text-gray-500">Database</p>
                            <p class="text-lg font-semibold text-gray-900">SQLite</p>
                        </div>
                    </div>

                    <div class="pt-4 border-t border-gray-200">
                        <div class="flex items-center justify-between">
                            <div>
                                <p class="text-sm font-medium text-gray-900">Status Server</p>
                                <p class="text-sm text-gray-500">Semua sistem berjalan normal</p>
                            </div>
                            <div class="flex items-center gap-2">
                                <span class="relative flex h-3 w-3">
                                    <span class="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"></span>
                                    <span class="relative inline-flex rounded-full h-3 w-3 bg-emerald-500"></span>
                                </span>
                                <span class="text-sm font-medium text-emerald-600">Online</span>
                            </div>
                        </div>
                    </div>
                </div>
            </x-card>

            <!-- Data Management -->
            <x-card>
                <x-slot name="header">
                    <div class="flex items-center gap-3">
                        <div class="p-2 bg-red-100 rounded-lg">
                            <svg class="w-5 h-5 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 7v10c0 2.21 3.582 4 8 4s8-1.79 8-4V7M4 7c0 2.21 3.582 4 8 4s8-1.79 8-4M4 7c0-2.21 3.582-4 8-4s8 1.79 8 4m0 5c0 2.21-3.582 4-8 4s-8-1.79-8-4"></path>
                            </svg>
                        </div>
                        <div>
                            <h3 class="text-lg font-semibold text-gray-900">Manajemen Data</h3>
                            <p class="text-sm text-gray-500">Backup, restore, dan pengelolaan data</p>
                        </div>
                    </div>
                </x-slot>

                <div class="space-y-4">
                    <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4 p-4 bg-gray-50 rounded-lg">
                        <div>
                            <p class="text-sm font-medium text-gray-900">Backup Terakhir</p>
                            <p class="text-sm text-gray-500">20 Januari 2024, 23:00 WIB</p>
                        </div>
                        <div class="flex items-center gap-2">
                            <x-button variant="outline" size="sm">
                                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-8l-4-4m0 0L8 8m4-4v12"></path>
                                </svg>
                                Backup Sekarang
                            </x-button>
                            <x-button variant="outline" size="sm">
                                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"></path>
                                </svg>
                                Download
                            </x-button>
                        </div>
                    </div>

                    <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4 p-4 bg-amber-50 border border-amber-200 rounded-lg">
                        <div class="flex items-start gap-3">
                            <svg class="w-5 h-5 text-amber-600 flex-shrink-0 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path>
                            </svg>
                            <div>
                                <p class="text-sm font-medium text-amber-800">Clear Cache</p>
                                <p class="text-sm text-amber-700">Hapus cache aplikasi untuk memperbaiki masalah</p>
                            </div>
                        </div>
                        <x-button variant="warning" size="sm">Clear Cache</x-button>
                    </div>

                    <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4 p-4 bg-red-50 border border-red-200 rounded-lg">
                        <div class="flex items-start gap-3">
                            <svg class="w-5 h-5 text-red-600 flex-shrink-0 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path>
                            </svg>
                            <div>
                                <p class="text-sm font-medium text-red-800">Reset Data</p>
                                <p class="text-sm text-red-700">Hapus semua data dan kembalikan ke pengaturan awal</p>
                            </div>
                        </div>
                        <x-button variant="danger" size="sm">Reset Data</x-button>
                    </div>
                </div>
            </x-card>

            <!-- Save Button -->
            <div class="flex justify-end gap-3">
                <x-button variant="outline">Batal</x-button>
                <x-button>
                    <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                    </svg>
                    Simpan Pengaturan
                </x-button>
            </div>
        </div>
    </div>
</x-layouts.app>
