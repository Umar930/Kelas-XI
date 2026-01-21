<x-layouts.app title="User Management">
    <!-- Page Header -->
    <div class="mb-8 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
            <h1 class="text-2xl font-bold text-gray-900">User Management</h1>
            <p class="mt-1 text-sm text-gray-500">Kelola semua pengguna dan hak akses sistem.</p>
        </div>
        <x-button @click="$dispatch('open-modal-add-user')">
            <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z"></path>
            </svg>
            Tambah User
        </x-button>
    </div>

    <!-- Filters -->
    <x-card class="mb-6">
        <div class="flex flex-col lg:flex-row lg:items-center gap-4">
            <div class="flex-1">
                <x-search-input placeholder="Cari berdasarkan nama atau email..." />
            </div>
            <div class="flex flex-wrap items-center gap-3">
                <x-select 
                    name="role" 
                    :options="[
                        'all' => 'Semua Role',
                        'admin' => 'Admin',
                        'kasir' => 'Kasir',
                        'manager' => 'Manager',
                        'viewer' => 'Viewer'
                    ]"
                    selected="all"
                    placeholder=""
                />
                <x-select 
                    name="status" 
                    :options="[
                        'all' => 'Semua Status',
                        'active' => 'Aktif',
                        'inactive' => 'Non-aktif'
                    ]"
                    selected="all"
                    placeholder=""
                />
                <x-button variant="secondary">
                    <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z"></path>
                    </svg>
                    Filter
                </x-button>
            </div>
        </div>
    </x-card>

    <!-- Users Table -->
    <x-card :padding="false">
        <x-table>
            <x-slot name="header">
                <x-table-cell type="header">
                    <input type="checkbox" class="h-4 w-4 text-emerald-600 border-gray-300 rounded focus:ring-emerald-500">
                </x-table-cell>
                <x-table-cell type="header">User</x-table-cell>
                <x-table-cell type="header">Role</x-table-cell>
                <x-table-cell type="header" align="center">Status</x-table-cell>
                <x-table-cell type="header">Last Login</x-table-cell>
                <x-table-cell type="header">Dibuat</x-table-cell>
                <x-table-cell type="header" align="center">Aksi</x-table-cell>
            </x-slot>

            @php
                $users = [
                    ['id' => 1, 'name' => 'Admin User', 'email' => 'admin@warunggo.com', 'role' => 'Admin', 'status' => 'active', 'last_login' => '21 Jan 2024, 14:35', 'created' => '01 Jan 2024'],
                    ['id' => 2, 'name' => 'Budi Kasir', 'email' => 'budi@warunggo.com', 'role' => 'Kasir', 'status' => 'active', 'last_login' => '21 Jan 2024, 13:28', 'created' => '05 Jan 2024'],
                    ['id' => 3, 'name' => 'Siti Manager', 'email' => 'siti@warunggo.com', 'role' => 'Manager', 'status' => 'active', 'last_login' => '20 Jan 2024, 17:45', 'created' => '03 Jan 2024'],
                    ['id' => 4, 'name' => 'Dewi Kasir', 'email' => 'dewi@warunggo.com', 'role' => 'Kasir', 'status' => 'active', 'last_login' => '21 Jan 2024, 08:15', 'created' => '10 Jan 2024'],
                    ['id' => 5, 'name' => 'Ahmad Viewer', 'email' => 'ahmad@warunggo.com', 'role' => 'Viewer', 'status' => 'inactive', 'last_login' => '15 Jan 2024, 10:22', 'created' => '08 Jan 2024'],
                    ['id' => 6, 'name' => 'Rina Kasir', 'email' => 'rina@warunggo.com', 'role' => 'Kasir', 'status' => 'active', 'last_login' => '21 Jan 2024, 09:30', 'created' => '12 Jan 2024'],
                    ['id' => 7, 'name' => 'Eko Staff', 'email' => 'eko@warunggo.com', 'role' => 'Kasir', 'status' => 'inactive', 'last_login' => '18 Jan 2024, 16:40', 'created' => '15 Jan 2024'],
                ];
            @endphp

            @foreach($users as $user)
                <x-table-row>
                    <x-table-cell>
                        <input type="checkbox" class="h-4 w-4 text-emerald-600 border-gray-300 rounded focus:ring-emerald-500">
                    </x-table-cell>
                    <x-table-cell>
                        <div class="flex items-center gap-4">
                            <x-avatar :alt="$user['name']" size="md" :status="$user['status'] === 'active' ? 'online' : 'offline'" />
                            <div>
                                <p class="font-medium text-gray-900">{{ $user['name'] }}</p>
                                <p class="text-sm text-gray-500">{{ $user['email'] }}</p>
                            </div>
                        </div>
                    </x-table-cell>
                    <x-table-cell>
                        @if($user['role'] === 'Admin')
                            <x-badge variant="danger">{{ $user['role'] }}</x-badge>
                        @elseif($user['role'] === 'Manager')
                            <x-badge variant="purple">{{ $user['role'] }}</x-badge>
                        @elseif($user['role'] === 'Kasir')
                            <x-badge variant="info">{{ $user['role'] }}</x-badge>
                        @else
                            <x-badge variant="gray">{{ $user['role'] }}</x-badge>
                        @endif
                    </x-table-cell>
                    <x-table-cell align="center">
                        <div x-data="{ enabled: {{ $user['status'] === 'active' ? 'true' : 'false' }} }">
                            <button
                                type="button"
                                @click="enabled = !enabled"
                                :class="enabled ? 'bg-emerald-600' : 'bg-gray-200'"
                                class="relative inline-flex flex-shrink-0 h-6 w-11 border-2 border-transparent rounded-full cursor-pointer transition-colors ease-in-out duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-emerald-500"
                                role="switch"
                            >
                                <span 
                                    :class="enabled ? 'translate-x-5' : 'translate-x-0'"
                                    class="pointer-events-none inline-block h-5 w-5 rounded-full bg-white shadow transform ring-0 transition ease-in-out duration-200"
                                ></span>
                            </button>
                        </div>
                    </x-table-cell>
                    <x-table-cell>
                        <span class="text-gray-600">{{ $user['last_login'] }}</span>
                    </x-table-cell>
                    <x-table-cell>
                        <span class="text-gray-500">{{ $user['created'] }}</span>
                    </x-table-cell>
                    <x-table-cell align="center">
                        <div class="flex items-center justify-center gap-2">
                            <button type="button" class="p-1.5 text-gray-400 hover:text-emerald-600 hover:bg-emerald-50 rounded-lg transition-colors" title="Edit" @click="$dispatch('open-modal-edit-user')">
                                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path>
                                </svg>
                            </button>
                            <button type="button" class="p-1.5 text-gray-400 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition-colors" title="Reset Password">
                                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z"></path>
                                </svg>
                            </button>
                            <button type="button" class="p-1.5 text-gray-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition-colors" title="Hapus" @click="$dispatch('open-modal-delete-user')">
                                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
                                </svg>
                            </button>
                        </div>
                    </x-table-cell>
                </x-table-row>
            @endforeach
        </x-table>

        <!-- Pagination -->
        <div class="px-6 py-4 border-t border-gray-200">
            <x-pagination :currentPage="1" :totalPages="2" :perPage="10" :total="7" />
        </div>
    </x-card>

    <!-- Add User Modal -->
    <x-modal id="add-user" title="Tambah User Baru" size="md">
        <form class="space-y-6">
            <x-input name="name" label="Nama Lengkap" placeholder="Masukkan nama lengkap" required />
            
            <x-input type="email" name="email" label="Email" placeholder="email@example.com" required />
            
            <x-select 
                name="role" 
                label="Role"
                :options="[
                    'admin' => 'Admin',
                    'manager' => 'Manager',
                    'kasir' => 'Kasir',
                    'viewer' => 'Viewer'
                ]"
                required
            />
            
            <x-input type="password" name="password" label="Password" placeholder="Minimal 8 karakter" required hint="Password minimal 8 karakter dengan kombinasi huruf dan angka" />
            
            <x-input type="password" name="password_confirmation" label="Konfirmasi Password" placeholder="Ulangi password" required />
            
            <div class="flex items-center gap-2">
                <x-checkbox name="send_email" label="Kirim email notifikasi ke user" checked />
            </div>
        </form>
        
        <x-slot name="footer">
            <x-button variant="secondary" @click="$dispatch('close-modal-add-user')">Batal</x-button>
            <x-button type="submit">Simpan User</x-button>
        </x-slot>
    </x-modal>

    <!-- Edit User Modal -->
    <x-modal id="edit-user" title="Edit User" size="md">
        <form class="space-y-6">
            <x-input name="name" label="Nama Lengkap" value="Budi Kasir" required />
            
            <x-input type="email" name="email" label="Email" value="budi@warunggo.com" required />
            
            <x-select 
                name="role" 
                label="Role"
                :options="[
                    'admin' => 'Admin',
                    'manager' => 'Manager',
                    'kasir' => 'Kasir',
                    'viewer' => 'Viewer'
                ]"
                selected="kasir"
                required
            />
            
            <div class="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                <div>
                    <p class="font-medium text-gray-900">Status Akun</p>
                    <p class="text-sm text-gray-500">Aktifkan atau nonaktifkan akun user</p>
                </div>
                <x-toggle :enabled="true" />
            </div>
        </form>
        
        <x-slot name="footer">
            <x-button variant="secondary" @click="$dispatch('close-modal-edit-user')">Batal</x-button>
            <x-button type="submit">Update User</x-button>
        </x-slot>
    </x-modal>

    <!-- Delete Confirmation Modal -->
    <x-modal id="delete-user" title="Hapus User" size="sm">
        <div class="text-center">
            <div class="mx-auto flex items-center justify-center h-12 w-12 rounded-full bg-red-100 mb-4">
                <svg class="h-6 w-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path>
                </svg>
            </div>
            <h3 class="text-lg font-medium text-gray-900 mb-2">Hapus User?</h3>
            <p class="text-sm text-gray-500">Apakah Anda yakin ingin menghapus user ini? Tindakan ini tidak dapat dibatalkan.</p>
        </div>
        
        <x-slot name="footer">
            <x-button variant="secondary" @click="$dispatch('close-modal-delete-user')">Batal</x-button>
            <x-button variant="danger">Hapus User</x-button>
        </x-slot>
    </x-modal>
</x-layouts.app>
