<x-layouts.app title="Laporan Keuangan">
    <!-- Page Header -->
    <div class="mb-8 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
            <h1 class="text-2xl font-bold text-gray-900">Laporan Keuangan</h1>
            <p class="mt-1 text-sm text-gray-500">Analisis pendapatan, pengeluaran, dan profit bisnis Anda.</p>
        </div>
        <div class="flex items-center gap-3">
            <x-button variant="outline">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"></path>
                </svg>
                Export PDF
            </x-button>
            <x-button variant="outline">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"></path>
                </svg>
                Export Excel
            </x-button>
        </div>
    </div>

    <!-- Date Range Picker -->
    <x-card class="mb-6">
        <div class="flex flex-col sm:flex-row sm:items-center gap-4">
            <div class="flex items-center gap-2">
                <span class="text-sm text-gray-600">Periode:</span>
                <x-input type="date" name="date_from" value="2024-01-01" />
                <span class="text-gray-400">-</span>
                <x-input type="date" name="date_to" value="2024-01-21" />
            </div>
            <div class="flex items-center gap-2">
                <button type="button" class="px-3 py-1.5 text-sm text-gray-600 hover:bg-gray-100 rounded-lg transition-colors">Hari Ini</button>
                <button type="button" class="px-3 py-1.5 text-sm text-gray-600 hover:bg-gray-100 rounded-lg transition-colors">Minggu Ini</button>
                <button type="button" class="px-3 py-1.5 text-sm bg-emerald-100 text-emerald-700 rounded-lg font-medium">Bulan Ini</button>
                <button type="button" class="px-3 py-1.5 text-sm text-gray-600 hover:bg-gray-100 rounded-lg transition-colors">Tahun Ini</button>
            </div>
            <x-button variant="primary" class="sm:ml-auto">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"></path>
                </svg>
                Refresh
            </x-button>
        </div>
    </x-card>

    <!-- Summary Cards -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <!-- Revenue -->
        <x-card class="bg-gradient-to-br from-emerald-500 to-emerald-600 border-0">
            <div class="text-white">
                <div class="flex items-center justify-between mb-4">
                    <p class="text-emerald-100 font-medium">Total Pendapatan</p>
                    <div class="p-2 bg-white/20 rounded-lg">
                        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                        </svg>
                    </div>
                </div>
                <p class="text-3xl font-bold font-mono">Rp 185.450.000</p>
                <div class="mt-2 flex items-center gap-2 text-sm">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 10l7-7m0 0l7 7m-7-7v18"></path>
                    </svg>
                    <span>+18.5% dari periode sebelumnya</span>
                </div>
            </div>
        </x-card>

        <!-- Expense -->
        <x-card class="bg-gradient-to-br from-red-500 to-red-600 border-0">
            <div class="text-white">
                <div class="flex items-center justify-between mb-4">
                    <p class="text-red-100 font-medium">Total Pengeluaran</p>
                    <div class="p-2 bg-white/20 rounded-lg">
                        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z"></path>
                        </svg>
                    </div>
                </div>
                <p class="text-3xl font-bold font-mono">Rp 125.320.000</p>
                <div class="mt-2 flex items-center gap-2 text-sm">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 10l7-7m0 0l7 7m-7-7v18"></path>
                    </svg>
                    <span>+12.3% dari periode sebelumnya</span>
                </div>
            </div>
        </x-card>

        <!-- Profit -->
        <x-card class="bg-gradient-to-br from-blue-500 to-blue-600 border-0">
            <div class="text-white">
                <div class="flex items-center justify-between mb-4">
                    <p class="text-blue-100 font-medium">Profit Bersih</p>
                    <div class="p-2 bg-white/20 rounded-lg">
                        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path>
                        </svg>
                    </div>
                </div>
                <p class="text-3xl font-bold font-mono">Rp 60.130.000</p>
                <div class="mt-2 flex items-center gap-2 text-sm">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 10l7-7m0 0l7 7m-7-7v18"></path>
                    </svg>
                    <span>+32.4% margin profit</span>
                </div>
            </div>
        </x-card>
    </div>

    <!-- Charts Row -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
        <!-- Revenue vs Expense Chart -->
        <x-card>
            <x-slot name="header">
                <div class="flex items-center justify-between">
                    <div>
                        <h3 class="text-lg font-semibold text-gray-900">Pendapatan vs Pengeluaran</h3>
                        <p class="text-sm text-gray-500">Perbandingan bulanan</p>
                    </div>
                    <select class="text-sm border-gray-300 rounded-lg focus:ring-emerald-500 focus:border-emerald-500">
                        <option>Tahun 2024</option>
                        <option>Tahun 2023</option>
                    </select>
                </div>
            </x-slot>
            
            <!-- Chart Placeholder -->
            <div class="h-72 flex items-center justify-center bg-gradient-to-br from-gray-50 to-gray-100 rounded-lg">
                <div class="text-center">
                    <svg class="w-16 h-16 mx-auto text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path>
                    </svg>
                    <p class="mt-3 text-sm text-gray-500 font-medium">Bar Chart Placeholder</p>
                    <p class="text-xs text-gray-400">Integrasi dengan Chart.js</p>
                </div>
            </div>
        </x-card>

        <!-- Profit Trend Chart -->
        <x-card>
            <x-slot name="header">
                <div class="flex items-center justify-between">
                    <div>
                        <h3 class="text-lg font-semibold text-gray-900">Tren Profit</h3>
                        <p class="text-sm text-gray-500">Perkembangan profit harian</p>
                    </div>
                    <select class="text-sm border-gray-300 rounded-lg focus:ring-emerald-500 focus:border-emerald-500">
                        <option>30 Hari</option>
                        <option>90 Hari</option>
                    </select>
                </div>
            </x-slot>
            
            <!-- Chart Placeholder -->
            <div class="h-72 flex items-center justify-center bg-gradient-to-br from-blue-50 to-blue-100 rounded-lg">
                <div class="text-center">
                    <svg class="w-16 h-16 mx-auto text-blue-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 12l3-3 3 3 4-4M8 21l4-4 4 4M3 4h18M4 4h16v12a1 1 0 01-1 1H5a1 1 0 01-1-1V4z"></path>
                    </svg>
                    <p class="mt-3 text-sm text-blue-600 font-medium">Line Chart Placeholder</p>
                    <p class="text-xs text-blue-400">Integrasi dengan Chart.js</p>
                </div>
            </div>
        </x-card>
    </div>

    <!-- Breakdown Tables -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
        <!-- Revenue Breakdown -->
        <x-card :padding="false">
            <x-slot name="header">
                <h3 class="text-lg font-semibold text-gray-900">Breakdown Pendapatan</h3>
            </x-slot>
            
            <x-table>
                <x-slot name="header">
                    <x-table-cell type="header">Kategori</x-table-cell>
                    <x-table-cell type="header" align="right">Total</x-table-cell>
                    <x-table-cell type="header" align="right">%</x-table-cell>
                </x-slot>
                
                @php
                    $revenueBreakdown = [
                        ['category' => 'Makanan', 'total' => 65250000, 'percentage' => 35.2],
                        ['category' => 'Minuman', 'total' => 48750000, 'percentage' => 26.3],
                        ['category' => 'Snack', 'total' => 32150000, 'percentage' => 17.3],
                        ['category' => 'Rokok', 'total' => 28450000, 'percentage' => 15.3],
                        ['category' => 'Toiletries', 'total' => 10850000, 'percentage' => 5.9],
                    ];
                @endphp
                
                @foreach($revenueBreakdown as $item)
                    <x-table-row>
                        <x-table-cell>
                            <div class="flex items-center gap-3">
                                <div class="w-3 h-3 rounded-full bg-emerald-{{ 600 - ($loop->index * 100) }}"></div>
                                <span class="font-medium">{{ $item['category'] }}</span>
                            </div>
                        </x-table-cell>
                        <x-table-cell align="right">
                            <span class="font-mono">Rp {{ number_format($item['total'], 0, ',', '.') }}</span>
                        </x-table-cell>
                        <x-table-cell align="right">
                            <x-badge variant="primary">{{ $item['percentage'] }}%</x-badge>
                        </x-table-cell>
                    </x-table-row>
                @endforeach
            </x-table>
        </x-card>

        <!-- Expense Breakdown -->
        <x-card :padding="false">
            <x-slot name="header">
                <h3 class="text-lg font-semibold text-gray-900">Breakdown Pengeluaran</h3>
            </x-slot>
            
            <x-table>
                <x-slot name="header">
                    <x-table-cell type="header">Kategori</x-table-cell>
                    <x-table-cell type="header" align="right">Total</x-table-cell>
                    <x-table-cell type="header" align="right">%</x-table-cell>
                </x-slot>
                
                @php
                    $expenseBreakdown = [
                        ['category' => 'Pembelian Stok', 'total' => 98500000, 'percentage' => 78.6],
                        ['category' => 'Gaji Karyawan', 'total' => 15000000, 'percentage' => 12.0],
                        ['category' => 'Listrik & Air', 'total' => 4500000, 'percentage' => 3.6],
                        ['category' => 'Sewa Tempat', 'total' => 5000000, 'percentage' => 4.0],
                        ['category' => 'Lainnya', 'total' => 2320000, 'percentage' => 1.8],
                    ];
                @endphp
                
                @foreach($expenseBreakdown as $item)
                    <x-table-row>
                        <x-table-cell>
                            <div class="flex items-center gap-3">
                                <div class="w-3 h-3 rounded-full bg-red-{{ 600 - ($loop->index * 100) }}"></div>
                                <span class="font-medium">{{ $item['category'] }}</span>
                            </div>
                        </x-table-cell>
                        <x-table-cell align="right">
                            <span class="font-mono">Rp {{ number_format($item['total'], 0, ',', '.') }}</span>
                        </x-table-cell>
                        <x-table-cell align="right">
                            <x-badge variant="danger">{{ $item['percentage'] }}%</x-badge>
                        </x-table-cell>
                    </x-table-row>
                @endforeach
            </x-table>
        </x-card>
    </div>

    <!-- Daily Report Table -->
    <x-card :padding="false">
        <x-slot name="header">
            <div class="flex items-center justify-between">
                <div>
                    <h3 class="text-lg font-semibold text-gray-900">Laporan Harian</h3>
                    <p class="text-sm text-gray-500">Detail pendapatan per hari</p>
                </div>
                <x-button variant="outline" size="sm">
                    <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"></path>
                    </svg>
                    Download
                </x-button>
            </div>
        </x-slot>
        
        <x-table>
            <x-slot name="header">
                <x-table-cell type="header">Tanggal</x-table-cell>
                <x-table-cell type="header" align="center">Transaksi</x-table-cell>
                <x-table-cell type="header" align="right">Pendapatan</x-table-cell>
                <x-table-cell type="header" align="right">Pengeluaran</x-table-cell>
                <x-table-cell type="header" align="right">Profit</x-table-cell>
                <x-table-cell type="header" align="center">Margin</x-table-cell>
            </x-slot>
            
            @php
                $dailyReports = [
                    ['date' => '21 Jan 2024', 'transactions' => 156, 'revenue' => 8500000, 'expense' => 5800000, 'profit' => 2700000, 'margin' => 31.8],
                    ['date' => '20 Jan 2024', 'transactions' => 142, 'revenue' => 7850000, 'expense' => 5200000, 'profit' => 2650000, 'margin' => 33.8],
                    ['date' => '19 Jan 2024', 'transactions' => 168, 'revenue' => 9200000, 'expense' => 6100000, 'profit' => 3100000, 'margin' => 33.7],
                    ['date' => '18 Jan 2024', 'transactions' => 134, 'revenue' => 7150000, 'expense' => 4900000, 'profit' => 2250000, 'margin' => 31.5],
                    ['date' => '17 Jan 2024', 'transactions' => 189, 'revenue' => 10250000, 'expense' => 6800000, 'profit' => 3450000, 'margin' => 33.7],
                    ['date' => '16 Jan 2024', 'transactions' => 145, 'revenue' => 7650000, 'expense' => 5100000, 'profit' => 2550000, 'margin' => 33.3],
                    ['date' => '15 Jan 2024', 'transactions' => 128, 'revenue' => 6850000, 'expense' => 4600000, 'profit' => 2250000, 'margin' => 32.8],
                ];
            @endphp
            
            @foreach($dailyReports as $report)
                <x-table-row>
                    <x-table-cell>
                        <span class="font-medium text-gray-900">{{ $report['date'] }}</span>
                    </x-table-cell>
                    <x-table-cell align="center">
                        <span class="font-mono">{{ $report['transactions'] }}</span>
                    </x-table-cell>
                    <x-table-cell align="right">
                        <span class="font-mono text-emerald-600 font-medium">Rp {{ number_format($report['revenue'], 0, ',', '.') }}</span>
                    </x-table-cell>
                    <x-table-cell align="right">
                        <span class="font-mono text-red-600">Rp {{ number_format($report['expense'], 0, ',', '.') }}</span>
                    </x-table-cell>
                    <x-table-cell align="right">
                        <span class="font-mono text-blue-600 font-medium">Rp {{ number_format($report['profit'], 0, ',', '.') }}</span>
                    </x-table-cell>
                    <x-table-cell align="center">
                        <x-badge variant="{{ $report['margin'] >= 33 ? 'success' : 'warning' }}">{{ $report['margin'] }}%</x-badge>
                    </x-table-cell>
                </x-table-row>
            @endforeach
        </x-table>
        
        <!-- Pagination -->
        <div class="px-6 py-4 border-t border-gray-200">
            <x-pagination :currentPage="1" :totalPages="3" :perPage="7" :total="21" />
        </div>
    </x-card>
</x-layouts.app>
