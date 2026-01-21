<x-layouts.app title="Profil">
    <div class="max-w-4xl mx-auto">
        <!-- Page Header -->
        <div class="mb-8">
            <h1 class="text-2xl font-bold text-gray-900">Profil Saya</h1>
            <p class="mt-1 text-sm text-gray-500">Kelola informasi profil dan keamanan akun Anda.</p>
        </div>

        <!-- Profile Card -->
        <x-card class="mb-6">
            <div class="flex flex-col sm:flex-row items-start sm:items-center gap-6">
                <!-- Avatar -->
                <div class="relative">
                    <div class="w-24 h-24 rounded-full bg-emerald-100 flex items-center justify-center text-emerald-700 text-3xl font-bold ring-4 ring-white shadow-lg">
                        AU
                    </div>
                    <button type="button" class="absolute bottom-0 right-0 p-2 bg-white rounded-full shadow-lg border border-gray-200 hover:bg-gray-50 transition-colors">
                        <svg class="w-4 h-4 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 9a2 2 0 012-2h.93a2 2 0 001.664-.89l.812-1.22A2 2 0 0110.07 4h3.86a2 2 0 011.664.89l.812 1.22A2 2 0 0018.07 7H19a2 2 0 012 2v9a2 2 0 01-2 2H5a2 2 0 01-2-2V9z"></path>
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 13a3 3 0 11-6 0 3 3 0 016 0z"></path>
                        </svg>
                    </button>
                </div>
                
                <!-- Info -->
                <div class="flex-1">
                    <h2 class="text-xl font-bold text-gray-900">Admin User</h2>
                    <p class="text-gray-500">admin@warunggo.com</p>
                    <div class="mt-2 flex items-center gap-2">
                        <x-badge variant="danger">Admin</x-badge>
                        <x-badge variant="success" :dot="true">Aktif</x-badge>
                    </div>
                </div>
                
                <!-- Actions -->
                <div class="flex items-center gap-3">
                    <x-button variant="outline" size="sm">
                        <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-8l-4-4m0 0L8 8m4-4v12"></path>
                        </svg>
                        Upload Foto
                    </x-button>
                </div>
            </div>
        </x-card>

        <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
            <!-- Main Form Column -->
            <div class="lg:col-span-2 space-y-6">
                <!-- Personal Information -->
                <x-card>
                    <x-slot name="header">
                        <div class="flex items-center justify-between">
                            <div>
                                <h3 class="text-lg font-semibold text-gray-900">Informasi Personal</h3>
                                <p class="text-sm text-gray-500">Update informasi profil Anda</p>
                            </div>
                        </div>
                    </x-slot>
                    
                    <form class="space-y-6">
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <x-input name="first_name" label="Nama Depan" value="Admin" required />
                            <x-input name="last_name" label="Nama Belakang" value="User" required />
                        </div>
                        
                        <x-input type="email" name="email" label="Email" value="admin@warunggo.com" required />
                        
                        <x-input type="tel" name="phone" label="Nomor Telepon" value="+62 812 3456 7890" placeholder="+62 xxx xxxx xxxx" />
                        
                        <x-textarea name="address" label="Alamat" value="Jl. Contoh No. 123, Jakarta Selatan" rows="3" />
                        
                        <div class="flex justify-end">
                            <x-button type="submit">
                                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                                </svg>
                                Simpan Perubahan
                            </x-button>
                        </div>
                    </form>
                </x-card>

                <!-- Change Password -->
                <x-card>
                    <x-slot name="header">
                        <div class="flex items-center justify-between">
                            <div>
                                <h3 class="text-lg font-semibold text-gray-900">Ubah Password</h3>
                                <p class="text-sm text-gray-500">Pastikan akun Anda menggunakan password yang kuat</p>
                            </div>
                        </div>
                    </x-slot>
                    
                    <form class="space-y-6" x-data="{ showCurrent: false, showNew: false, showConfirm: false }">
                        <div class="relative">
                            <x-input 
                                :type="'password'" 
                                name="current_password" 
                                label="Password Saat Ini" 
                                placeholder="Masukkan password saat ini" 
                                required 
                            />
                        </div>
                        
                        <x-input type="password" name="new_password" label="Password Baru" placeholder="Minimal 8 karakter" required hint="Gunakan kombinasi huruf besar, huruf kecil, angka, dan simbol" />
                        
                        <x-input type="password" name="confirm_password" label="Konfirmasi Password Baru" placeholder="Ulangi password baru" required />
                        
                        <!-- Password Strength Indicator -->
                        <div>
                            <div class="flex items-center justify-between mb-1">
                                <span class="text-sm text-gray-600">Kekuatan Password</span>
                                <span class="text-sm font-medium text-amber-600">Sedang</span>
                            </div>
                            <div class="w-full bg-gray-200 rounded-full h-2">
                                <div class="bg-amber-500 h-2 rounded-full" style="width: 60%"></div>
                            </div>
                        </div>
                        
                        <div class="flex justify-end">
                            <x-button type="submit">
                                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z"></path>
                                </svg>
                                Update Password
                            </x-button>
                        </div>
                    </form>
                </x-card>
            </div>

            <!-- Sidebar Column -->
            <div class="space-y-6">
                <!-- Account Info -->
                <x-card>
                    <h3 class="text-lg font-semibold text-gray-900 mb-4">Informasi Akun</h3>
                    <div class="space-y-4">
                        <div class="flex items-center justify-between py-2 border-b border-gray-100">
                            <span class="text-sm text-gray-500">Role</span>
                            <x-badge variant="danger">Admin</x-badge>
                        </div>
                        <div class="flex items-center justify-between py-2 border-b border-gray-100">
                            <span class="text-sm text-gray-500">Status</span>
                            <x-badge variant="success" :dot="true">Aktif</x-badge>
                        </div>
                        <div class="flex items-center justify-between py-2 border-b border-gray-100">
                            <span class="text-sm text-gray-500">Bergabung</span>
                            <span class="text-sm text-gray-900">01 Jan 2024</span>
                        </div>
                        <div class="flex items-center justify-between py-2">
                            <span class="text-sm text-gray-500">Last Login</span>
                            <span class="text-sm text-gray-900">21 Jan 2024, 14:35</span>
                        </div>
                    </div>
                </x-card>

                <!-- Activity Log -->
                <x-card>
                    <div class="flex items-center justify-between mb-4">
                        <h3 class="text-lg font-semibold text-gray-900">Aktivitas Terbaru</h3>
                        <a href="#" class="text-sm text-emerald-600 hover:text-emerald-700 font-medium">Lihat Semua</a>
                    </div>
                    
                    <div class="space-y-4">
                        @php
                            $activities = [
                                ['action' => 'Login berhasil', 'time' => '14:35', 'icon' => 'login', 'color' => 'emerald'],
                                ['action' => 'Update profil', 'time' => '12:20', 'icon' => 'edit', 'color' => 'blue'],
                                ['action' => 'Tambah produk baru', 'time' => '10:45', 'icon' => 'plus', 'color' => 'purple'],
                                ['action' => 'Export laporan', 'time' => '09:15', 'icon' => 'download', 'color' => 'gray'],
                                ['action' => 'Login berhasil', 'time' => 'Kemarin', 'icon' => 'login', 'color' => 'emerald'],
                            ];
                        @endphp
                        
                        @foreach($activities as $activity)
                            <div class="flex items-start gap-3">
                                <div class="flex-shrink-0 w-8 h-8 bg-{{ $activity['color'] }}-100 rounded-full flex items-center justify-center">
                                    @if($activity['icon'] === 'login')
                                        <svg class="w-4 h-4 text-{{ $activity['color'] }}-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 16l-4-4m0 0l4-4m-4 4h14m-5 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h7a3 3 0 013 3v1"></path>
                                        </svg>
                                    @elseif($activity['icon'] === 'edit')
                                        <svg class="w-4 h-4 text-{{ $activity['color'] }}-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path>
                                        </svg>
                                    @elseif($activity['icon'] === 'plus')
                                        <svg class="w-4 h-4 text-{{ $activity['color'] }}-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                                        </svg>
                                    @else
                                        <svg class="w-4 h-4 text-{{ $activity['color'] }}-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"></path>
                                        </svg>
                                    @endif
                                </div>
                                <div class="flex-1 min-w-0">
                                    <p class="text-sm text-gray-900">{{ $activity['action'] }}</p>
                                    <p class="text-xs text-gray-500">{{ $activity['time'] }}</p>
                                </div>
                            </div>
                        @endforeach
                    </div>
                </x-card>

                <!-- Danger Zone -->
                <x-card class="border-red-200 bg-red-50">
                    <h3 class="text-lg font-semibold text-red-900 mb-2">Zona Bahaya</h3>
                    <p class="text-sm text-red-700 mb-4">Tindakan ini tidak dapat dibatalkan. Mohon pertimbangkan dengan baik.</p>
                    <x-button variant="danger" size="sm">
                        <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
                        </svg>
                        Hapus Akun
                    </x-button>
                </x-card>
            </div>
        </div>
    </div>
</x-layouts.app>
