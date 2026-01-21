<x-layouts.app title="Transaksi">
    <!-- Page Header -->
    <div class="mb-8 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
            <h1 class="text-2xl font-bold text-gray-900">Transaksi</h1>
            <p class="mt-1 text-sm text-gray-500">Riwayat semua transaksi penjualan.</p>
        </div>
        <div class="flex items-center gap-3">
            <x-button variant="outline">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"></path>
                </svg>
                Export Excel
            </x-button>
            <x-button variant="outline">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 17h2a2 2 0 002-2v-4a2 2 0 00-2-2H5a2 2 0 00-2 2v4a2 2 0 002 2h2m2 4h6a2 2 0 002-2v-4a2 2 0 00-2-2H9a2 2 0 00-2 2v4a2 2 0 002 2zm8-12V5a2 2 0 00-2-2H9a2 2 0 00-2 2v4h10z"></path>
                </svg>
                Print
            </x-button>
        </div>
    </div>

    <!-- Filters Card -->
    <x-card class="mb-6">
        <div class="flex flex-col lg:flex-row gap-4">
            <div class="flex-1">
                <x-search-input placeholder="Cari berdasarkan Invoice ID, nama pelanggan..." />
            </div>
            <div class="flex flex-wrap items-center gap-3">
                <!-- Date Range -->
                <div class="flex items-center gap-2">
                    <x-input type="date" name="date_from" placeholder="Dari" />
                    <span class="text-gray-400">-</span>
                    <x-input type="date" name="date_to" placeholder="Sampai" />
                </div>
                
                <x-select 
                    name="status" 
                    :options="[
                        'all' => 'Semua Status',
                        'success' => 'Sukses',
                        'pending' => 'Pending',
                        'cancelled' => 'Dibatalkan'
                    ]"
                    selected="all"
                    placeholder=""
                />
                
                <x-select 
                    name="payment_method" 
                    :options="[
                        'all' => 'Semua Metode',
                        'cash' => 'Cash',
                        'qris' => 'QRIS',
                        'transfer' => 'Transfer',
                        'debit' => 'Kartu Debit'
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

    <!-- Summary Cards -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
        <div class="bg-white rounded-xl border border-gray-200 p-4">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-gray-500">Total Transaksi Hari Ini</p>
                    <p class="text-2xl font-bold text-gray-900 font-mono mt-1">156</p>
                </div>
                <div class="p-3 bg-emerald-100 rounded-xl">
                    <svg class="w-6 h-6 text-emerald-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01"></path>
                    </svg>
                </div>
            </div>
        </div>
        <div class="bg-white rounded-xl border border-gray-200 p-4">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-gray-500">Pendapatan Hari Ini</p>
                    <p class="text-2xl font-bold text-gray-900 font-mono mt-1">Rp 8.5M</p>
                </div>
                <div class="p-3 bg-blue-100 rounded-xl">
                    <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                </div>
            </div>
        </div>
        <div class="bg-white rounded-xl border border-gray-200 p-4">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-gray-500">Transaksi Sukses</p>
                    <p class="text-2xl font-bold text-green-600 font-mono mt-1">148</p>
                </div>
                <div class="p-3 bg-green-100 rounded-xl">
                    <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                </div>
            </div>
        </div>
        <div class="bg-white rounded-xl border border-gray-200 p-4">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-sm text-gray-500">Dibatalkan</p>
                    <p class="text-2xl font-bold text-red-600 font-mono mt-1">8</p>
                </div>
                <div class="p-3 bg-red-100 rounded-xl">
                    <svg class="w-6 h-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                </div>
            </div>
        </div>
    </div>

    <!-- Transactions Table -->
    <x-card :padding="false">
        <x-table>
            <x-slot name="header">
                <x-table-cell type="header">Invoice ID</x-table-cell>
                <x-table-cell type="header">Tanggal & Waktu</x-table-cell>
                <x-table-cell type="header">Pelanggan</x-table-cell>
                <x-table-cell type="header">Item</x-table-cell>
                <x-table-cell type="header">Metode Bayar</x-table-cell>
                <x-table-cell type="header" align="right">Total</x-table-cell>
                <x-table-cell type="header" align="center">Status</x-table-cell>
                <x-table-cell type="header" align="center">Aksi</x-table-cell>
            </x-slot>

            @php
                $transactions = [
                    ['invoice' => 'INV-2024-0156', 'date' => '21 Jan 2024', 'time' => '14:35:22', 'customer' => 'Budi Santoso', 'items' => 5, 'method' => 'QRIS', 'total' => 185000, 'status' => 'success'],
                    ['invoice' => 'INV-2024-0155', 'date' => '21 Jan 2024', 'time' => '14:28:15', 'customer' => 'Dewi Lestari', 'items' => 3, 'method' => 'Cash', 'total' => 75000, 'status' => 'success'],
                    ['invoice' => 'INV-2024-0154', 'date' => '21 Jan 2024', 'time' => '14:15:08', 'customer' => 'Ahmad Yani', 'items' => 8, 'method' => 'Transfer', 'total' => 267500, 'status' => 'pending'],
                    ['invoice' => 'INV-2024-0153', 'date' => '21 Jan 2024', 'time' => '13:55:44', 'customer' => 'Siti Rahayu', 'items' => 2, 'method' => 'QRIS', 'total' => 54000, 'status' => 'success'],
                    ['invoice' => 'INV-2024-0152', 'date' => '21 Jan 2024', 'time' => '13:42:31', 'customer' => 'Rudi Hermawan', 'items' => 4, 'method' => 'Cash', 'total' => 125000, 'status' => 'cancelled'],
                    ['invoice' => 'INV-2024-0151', 'date' => '21 Jan 2024', 'time' => '13:28:19', 'customer' => 'Maya Sari', 'items' => 6, 'method' => 'Debit', 'total' => 198000, 'status' => 'success'],
                    ['invoice' => 'INV-2024-0150', 'date' => '21 Jan 2024', 'time' => '13:15:05', 'customer' => 'Eko Prasetyo', 'items' => 1, 'method' => 'Cash', 'total' => 25000, 'status' => 'success'],
                    ['invoice' => 'INV-2024-0149', 'date' => '21 Jan 2024', 'time' => '12:58:47', 'customer' => 'Rina Wati', 'items' => 7, 'method' => 'QRIS', 'total' => 312000, 'status' => 'success'],
                ];
            @endphp

            @foreach($transactions as $trx)
                <x-table-row>
                    <x-table-cell>
                        <span class="font-mono font-semibold text-emerald-600">{{ $trx['invoice'] }}</span>
                    </x-table-cell>
                    <x-table-cell>
                        <div>
                            <p class="text-gray-900">{{ $trx['date'] }}</p>
                            <p class="text-sm text-gray-500 font-mono">{{ $trx['time'] }}</p>
                        </div>
                    </x-table-cell>
                    <x-table-cell>
                        <div class="flex items-center gap-3">
                            <x-avatar :alt="$trx['customer']" size="sm" />
                            <span>{{ $trx['customer'] }}</span>
                        </div>
                    </x-table-cell>
                    <x-table-cell>
                        <span class="text-gray-600">{{ $trx['items'] }} item</span>
                    </x-table-cell>
                    <x-table-cell>
                        @if($trx['method'] === 'QRIS')
                            <x-badge variant="primary">
                                <svg class="w-3 h-3 mr-1" fill="currentColor" viewBox="0 0 20 20">
                                    <path d="M3 4a1 1 0 011-1h3a1 1 0 011 1v3a1 1 0 01-1 1H4a1 1 0 01-1-1V4zm2 2V5h1v1H5zM3 13a1 1 0 011-1h3a1 1 0 011 1v3a1 1 0 01-1 1H4a1 1 0 01-1-1v-3zm2 2v-1h1v1H5zM13 3a1 1 0 00-1 1v3a1 1 0 001 1h3a1 1 0 001-1V4a1 1 0 00-1-1h-3zm1 2v1h1V5h-1z"></path>
                                </svg>
                                QRIS
                            </x-badge>
                        @elseif($trx['method'] === 'Cash')
                            <x-badge variant="gray">
                                <svg class="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z"></path>
                                </svg>
                                Cash
                            </x-badge>
                        @elseif($trx['method'] === 'Transfer')
                            <x-badge variant="info">
                                <svg class="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4"></path>
                                </svg>
                                Transfer
                            </x-badge>
                        @else
                            <x-badge variant="purple">
                                <svg class="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z"></path>
                                </svg>
                                Debit
                            </x-badge>
                        @endif
                    </x-table-cell>
                    <x-table-cell align="right">
                        <span class="font-mono font-semibold text-gray-900">Rp {{ number_format($trx['total'], 0, ',', '.') }}</span>
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
                        <div class="flex items-center justify-center gap-2">
                            <button type="button" class="p-1.5 text-gray-400 hover:text-emerald-600 hover:bg-emerald-50 rounded-lg transition-colors" title="Detail" @click="$dispatch('open-modal-transaction-detail')">
                                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"></path>
                                </svg>
                            </button>
                            <button type="button" class="p-1.5 text-gray-400 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition-colors" title="Print">
                                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 17h2a2 2 0 002-2v-4a2 2 0 00-2-2H5a2 2 0 00-2 2v4a2 2 0 002 2h2m2 4h6a2 2 0 002-2v-4a2 2 0 00-2-2H9a2 2 0 00-2 2v4a2 2 0 002 2zm8-12V5a2 2 0 00-2-2H9a2 2 0 00-2 2v4h10z"></path>
                                </svg>
                            </button>
                        </div>
                    </x-table-cell>
                </x-table-row>
            @endforeach
        </x-table>

        <!-- Pagination -->
        <div class="px-6 py-4 border-t border-gray-200">
            <x-pagination :currentPage="1" :totalPages="20" :perPage="10" :total="156" />
        </div>
    </x-card>

    <!-- Transaction Detail Modal -->
    <x-modal id="transaction-detail" title="Detail Transaksi" size="lg">
        <div class="space-y-6">
            <!-- Invoice Header -->
            <div class="flex items-center justify-between pb-4 border-b border-gray-200">
                <div>
                    <p class="text-sm text-gray-500">Invoice ID</p>
                    <p class="text-xl font-bold text-emerald-600 font-mono">INV-2024-0156</p>
                </div>
                <x-badge variant="success" size="lg" :dot="true">Sukses</x-badge>
            </div>
            
            <!-- Transaction Info -->
            <div class="grid grid-cols-2 gap-4">
                <div>
                    <p class="text-sm text-gray-500">Tanggal & Waktu</p>
                    <p class="font-medium text-gray-900">21 Jan 2024, 14:35:22</p>
                </div>
                <div>
                    <p class="text-sm text-gray-500">Kasir</p>
                    <p class="font-medium text-gray-900">Admin</p>
                </div>
                <div>
                    <p class="text-sm text-gray-500">Pelanggan</p>
                    <p class="font-medium text-gray-900">Budi Santoso</p>
                </div>
                <div>
                    <p class="text-sm text-gray-500">Metode Pembayaran</p>
                    <p class="font-medium text-gray-900">QRIS</p>
                </div>
            </div>
            
            <!-- Items -->
            <div>
                <p class="text-sm font-medium text-gray-700 mb-3">Item Pembelian</p>
                <div class="bg-gray-50 rounded-lg overflow-hidden">
                    <table class="min-w-full">
                        <thead>
                            <tr class="border-b border-gray-200">
                                <th class="px-4 py-2 text-left text-xs font-semibold text-gray-600">Produk</th>
                                <th class="px-4 py-2 text-center text-xs font-semibold text-gray-600">Qty</th>
                                <th class="px-4 py-2 text-right text-xs font-semibold text-gray-600">Harga</th>
                                <th class="px-4 py-2 text-right text-xs font-semibold text-gray-600">Subtotal</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-200">
                            <tr>
                                <td class="px-4 py-3 text-sm text-gray-900">Indomie Goreng</td>
                                <td class="px-4 py-3 text-sm text-gray-600 text-center">3</td>
                                <td class="px-4 py-3 text-sm text-gray-600 text-right font-mono">Rp 3.000</td>
                                <td class="px-4 py-3 text-sm text-gray-900 text-right font-mono font-medium">Rp 9.000</td>
                            </tr>
                            <tr>
                                <td class="px-4 py-3 text-sm text-gray-900">Aqua 600ml</td>
                                <td class="px-4 py-3 text-sm text-gray-600 text-center">5</td>
                                <td class="px-4 py-3 text-sm text-gray-600 text-right font-mono">Rp 3.000</td>
                                <td class="px-4 py-3 text-sm text-gray-900 text-right font-mono font-medium">Rp 15.000</td>
                            </tr>
                            <tr>
                                <td class="px-4 py-3 text-sm text-gray-900">Chitato Original 68g</td>
                                <td class="px-4 py-3 text-sm text-gray-600 text-center">2</td>
                                <td class="px-4 py-3 text-sm text-gray-600 text-right font-mono">Rp 10.000</td>
                                <td class="px-4 py-3 text-sm text-gray-900 text-right font-mono font-medium">Rp 20.000</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            
            <!-- Summary -->
            <div class="bg-emerald-50 rounded-lg p-4 space-y-2">
                <div class="flex justify-between text-sm">
                    <span class="text-gray-600">Subtotal</span>
                    <span class="font-mono text-gray-900">Rp 44.000</span>
                </div>
                <div class="flex justify-between text-sm">
                    <span class="text-gray-600">Diskon</span>
                    <span class="font-mono text-gray-900">- Rp 0</span>
                </div>
                <div class="flex justify-between text-sm">
                    <span class="text-gray-600">Pajak (0%)</span>
                    <span class="font-mono text-gray-900">Rp 0</span>
                </div>
                <div class="flex justify-between pt-2 border-t border-emerald-200">
                    <span class="font-semibold text-gray-900">Total</span>
                    <span class="text-xl font-bold font-mono text-emerald-600">Rp 185.000</span>
                </div>
                <div class="flex justify-between text-sm">
                    <span class="text-gray-600">Bayar</span>
                    <span class="font-mono text-gray-900">Rp 200.000</span>
                </div>
                <div class="flex justify-between text-sm">
                    <span class="text-gray-600">Kembalian</span>
                    <span class="font-mono text-gray-900">Rp 15.000</span>
                </div>
            </div>
        </div>
        
        <x-slot name="footer">
            <x-button variant="secondary" @click="$dispatch('close-modal-transaction-detail')">Tutup</x-button>
            <x-button variant="outline">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 17h2a2 2 0 002-2v-4a2 2 0 00-2-2H5a2 2 0 00-2 2v4a2 2 0 002 2h2m2 4h6a2 2 0 002-2v-4a2 2 0 00-2-2H9a2 2 0 00-2 2v4a2 2 0 002 2zm8-12V5a2 2 0 00-2-2H9a2 2 0 00-2 2v4h10z"></path>
                </svg>
                Print Struk
            </x-button>
        </x-slot>
    </x-modal>
</x-layouts.app>
