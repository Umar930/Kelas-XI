<x-layouts.app title="Dashboard">
    <!-- Page Header -->
    <div class="mb-8">
        <h1 class="text-2xl font-bold text-gray-900">Dashboard</h1>
        <p class="mt-1 text-sm text-gray-500">Selamat datang kembali! Berikut ringkasan bisnis Anda hari ini.</p>
    </div>

    <!-- KPI Cards -->
    <div class="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-4 mb-8">
        <!-- Total Revenue -->
        <x-stat-card 
            title="Total Pendapatan"
            value="Rp 45.250.000"
            change="+12.5%"
            changeType="increase"
        >
            <x-slot name="icon">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                </svg>
            </x-slot>
        </x-stat-card>

        <!-- Total Transactions -->
        <x-stat-card 
            title="Total Transaksi"
            value="1.248"
            change="+8.2%"
            changeType="increase"
            iconBg="bg-blue-100"
            iconColor="text-blue-600"
        >
            <x-slot name="icon">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z"></path>
                </svg>
            </x-slot>
        </x-stat-card>

        <!-- Total Products -->
        <x-stat-card 
            title="Total Produk"
            value="324"
            change="+5"
            changeType="increase"
            iconBg="bg-purple-100"
            iconColor="text-purple-600"
        >
            <x-slot name="icon">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"></path>
                </svg>
            </x-slot>
        </x-stat-card>

        <!-- Low Stock Alerts -->
        <x-stat-card 
            title="Stok Rendah"
            value="12"
            change="Perlu perhatian"
            changeType="warning"
            iconBg="bg-amber-100"
            iconColor="text-amber-600"
        >
            <x-slot name="icon">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path>
                </svg>
            </x-slot>
        </x-stat-card>
    </div>

    <!-- Charts Row -->
    <div class="grid grid-cols-1 gap-6 lg:grid-cols-2 mb-8">
        <!-- Sales Trend Chart -->
        <x-card>
            <x-slot name="header">
                <div class="flex items-center justify-between">
                    <div>
                        <h3 class="text-lg font-semibold text-gray-900">Tren Penjualan</h3>
                        <p class="text-sm text-gray-500">7 hari terakhir</p>
                    </div>
                    <select class="text-sm border-gray-300 rounded-lg focus:ring-emerald-500 focus:border-emerald-500">
                        <option>7 Hari</option>
                        <option>30 Hari</option>
                        <option>90 Hari</option>
                    </select>
                </div>
            </x-slot>
            
            <!-- Chart Placeholder -->
            <div class="h-64 flex items-center justify-center bg-gradient-to-br from-emerald-50 to-emerald-100 rounded-lg">
                <div class="text-center">
                    <svg class="w-12 h-12 mx-auto text-emerald-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 12l3-3 3 3 4-4M8 21l4-4 4 4M3 4h18M4 4h16v12a1 1 0 01-1 1H5a1 1 0 01-1-1V4z"></path>
                    </svg>
                    <p class="mt-2 text-sm text-emerald-600 font-medium">Sales Trend Chart</p>
                    <p class="text-xs text-emerald-500">Integrasi dengan Chart.js</p>
                </div>
            </div>
            
            <!-- Quick Stats -->
            <div class="mt-4 grid grid-cols-3 gap-4">
                <div class="text-center p-3 bg-gray-50 rounded-lg">
                    <p class="text-2xl font-bold text-gray-900 font-mono">Rp 6.5M</p>
                    <p class="text-xs text-gray-500">Hari Ini</p>
                </div>
                <div class="text-center p-3 bg-gray-50 rounded-lg">
                    <p class="text-2xl font-bold text-gray-900 font-mono">Rp 42M</p>
                    <p class="text-xs text-gray-500">Minggu Ini</p>
                </div>
                <div class="text-center p-3 bg-gray-50 rounded-lg">
                    <p class="text-2xl font-bold text-gray-900 font-mono">Rp 180M</p>
                    <p class="text-xs text-gray-500">Bulan Ini</p>
                </div>
            </div>
        </x-card>

        <!-- Best Selling Products Chart -->
        <x-card>
            <x-slot name="header">
                <div class="flex items-center justify-between">
                    <div>
                        <h3 class="text-lg font-semibold text-gray-900">Produk Terlaris</h3>
                        <p class="text-sm text-gray-500">Bulan ini</p>
                    </div>
                    <a href="{{ route('products') }}" class="text-sm text-emerald-600 hover:text-emerald-700 font-medium">
                        Lihat Semua
                    </a>
                </div>
            </x-slot>
            
            <!-- Products List -->
            <div class="space-y-4">
                @php
                    $topProducts = [
                        ['name' => 'Indomie Goreng', 'sold' => 245, 'revenue' => 735000, 'percentage' => 100],
                        ['name' => 'Aqua 600ml', 'sold' => 198, 'revenue' => 594000, 'percentage' => 81],
                        ['name' => 'Teh Pucuk 350ml', 'sold' => 167, 'revenue' => 501000, 'percentage' => 68],
                        ['name' => 'Chitato Original', 'sold' => 134, 'revenue' => 402000, 'percentage' => 55],
                        ['name' => 'Pocari Sweat 350ml', 'sold' => 112, 'revenue' => 672000, 'percentage' => 46],
                    ];
                @endphp
                
                @foreach($topProducts as $index => $product)
                    <div class="flex items-center gap-4">
                        <div class="flex-shrink-0 w-8 h-8 flex items-center justify-center rounded-full {{ $index === 0 ? 'bg-amber-100 text-amber-700' : 'bg-gray-100 text-gray-600' }} font-semibold text-sm">
                            {{ $index + 1 }}
                        </div>
                        <div class="flex-1 min-w-0">
                            <div class="flex items-center justify-between mb-1">
                                <p class="text-sm font-medium text-gray-900 truncate">{{ $product['name'] }}</p>
                                <p class="text-sm text-gray-500">{{ $product['sold'] }} terjual</p>
                            </div>
                            <div class="w-full bg-gray-200 rounded-full h-2">
                                <div class="bg-emerald-500 h-2 rounded-full" style="width: {{ $product['percentage'] }}%"></div>
                            </div>
                        </div>
                        <div class="flex-shrink-0 text-right">
                            <p class="text-sm font-semibold text-gray-900 font-mono">Rp {{ number_format($product['revenue'], 0, ',', '.') }}</p>
                        </div>
                    </div>
                @endforeach
            </div>
        </x-card>
    </div>

    <!-- Recent Transactions -->
    <x-card>
        <x-slot name="header">
            <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
                <div>
                    <h3 class="text-lg font-semibold text-gray-900">Transaksi Terbaru</h3>
                    <p class="text-sm text-gray-500">Daftar transaksi hari ini</p>
                </div>
                <div class="flex items-center gap-3">
                    <x-search-input placeholder="Cari transaksi..." class="w-64" />
                    <a href="{{ route('transactions') }}" class="btn btn-primary">
                        Lihat Semua
                    </a>
                </div>
            </div>
        </x-slot>
        
        <x-table>
            <x-slot name="header">
                <x-table-cell type="header">Invoice</x-table-cell>
                <x-table-cell type="header">Waktu</x-table-cell>
                <x-table-cell type="header">Pelanggan</x-table-cell>
                <x-table-cell type="header">Metode</x-table-cell>
                <x-table-cell type="header" align="right">Total</x-table-cell>
                <x-table-cell type="header" align="center">Status</x-table-cell>
                <x-table-cell type="header" align="center">Aksi</x-table-cell>
            </x-slot>
            
            @php
                $transactions = [
                    ['invoice' => 'INV-2024-001', 'time' => '14:35', 'customer' => 'Budi Santoso', 'method' => 'QRIS', 'total' => 85000, 'status' => 'success'],
                    ['invoice' => 'INV-2024-002', 'time' => '14:28', 'customer' => 'Dewi Lestari', 'method' => 'Cash', 'total' => 125000, 'status' => 'success'],
                    ['invoice' => 'INV-2024-003', 'time' => '14:15', 'customer' => 'Ahmad Yani', 'method' => 'Transfer', 'total' => 67500, 'status' => 'pending'],
                    ['invoice' => 'INV-2024-004', 'time' => '13:55', 'customer' => 'Siti Rahayu', 'method' => 'QRIS', 'total' => 234000, 'status' => 'success'],
                    ['invoice' => 'INV-2024-005', 'time' => '13:42', 'customer' => 'Rudi Hermawan', 'method' => 'Cash', 'total' => 45000, 'status' => 'cancelled'],
                ];
            @endphp
            
            @foreach($transactions as $trx)
                <x-table-row>
                    <x-table-cell>
                        <span class="font-mono font-medium text-gray-900">{{ $trx['invoice'] }}</span>
                    </x-table-cell>
                    <x-table-cell>
                        <span class="text-gray-500">{{ $trx['time'] }}</span>
                    </x-table-cell>
                    <x-table-cell>
                        <div class="flex items-center gap-3">
                            <x-avatar :alt="$trx['customer']" size="sm" />
                            <span>{{ $trx['customer'] }}</span>
                        </div>
                    </x-table-cell>
                    <x-table-cell>
                        <x-badge variant="{{ $trx['method'] === 'QRIS' ? 'primary' : ($trx['method'] === 'Cash' ? 'gray' : 'info') }}">
                            {{ $trx['method'] }}
                        </x-badge>
                    </x-table-cell>
                    <x-table-cell align="right">
                        <span class="font-mono font-semibold">Rp {{ number_format($trx['total'], 0, ',', '.') }}</span>
                    </x-table-cell>
                    <x-table-cell align="center">
                        @if($trx['status'] === 'success')
                            <x-badge variant="success" :dot="true">Sukses</x-badge>
                        @elseif($trx['status'] === 'pending')
                            <x-badge variant="warning" :dot="true">Pending</x-badge>
                        @else
                            <x-badge variant="danger" :dot="true">Dibatalkan</x-badge>
                        @endif
                    </x-table-cell>
                    <x-table-cell align="center">
                        <button type="button" class="text-gray-400 hover:text-gray-600">
                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"></path>
                            </svg>
                        </button>
                    </x-table-cell>
                </x-table-row>
            @endforeach
        </x-table>
    </x-card>

    <!-- Low Stock Alert -->
    <div class="mt-8">
        <x-alert type="warning" title="Peringatan Stok Rendah" :dismissible="true">
            <p>Ada <strong>12 produk</strong> dengan stok di bawah minimum. <a href="{{ route('products') }}" class="font-medium underline hover:no-underline">Lihat detail &rarr;</a></p>
        </x-alert>
    </div>
</x-layouts.app>
