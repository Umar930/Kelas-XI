<x-layouts.app title="Produk & Stok">
    <!-- Page Header -->
    <div class="mb-8 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
            <h1 class="text-2xl font-bold text-gray-900">Produk & Stok</h1>
            <p class="mt-1 text-sm text-gray-500">Kelola semua produk dan inventaris toko Anda.</p>
        </div>
        <div class="flex items-center gap-3">
            <x-button variant="outline" @click="$dispatch('open-modal-import-product')">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-8l-4-4m0 0L8 8m4-4v12"></path>
                </svg>
                Import
            </x-button>
            <x-button @click="$dispatch('open-modal-add-product')">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                </svg>
                Tambah Produk
            </x-button>
        </div>
    </div>

    <!-- Filters -->
    <x-card class="mb-6">
        <div class="flex flex-col lg:flex-row lg:items-center gap-4">
            <div class="flex-1">
                <x-search-input placeholder="Cari produk berdasarkan nama, SKU, atau barcode..." />
            </div>
            <div class="flex flex-wrap items-center gap-3">
                <x-select 
                    name="category" 
                    :options="[
                        'all' => 'Semua Kategori',
                        'makanan' => 'Makanan',
                        'minuman' => 'Minuman',
                        'snack' => 'Snack',
                        'rokok' => 'Rokok',
                        'toiletries' => 'Toiletries',
                        'lainnya' => 'Lainnya'
                    ]"
                    selected="all"
                    placeholder=""
                />
                <x-select 
                    name="status" 
                    :options="[
                        'all' => 'Semua Status',
                        'active' => 'Aktif',
                        'inactive' => 'Non-aktif',
                        'low_stock' => 'Stok Rendah',
                        'out_of_stock' => 'Habis'
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

    <!-- Products Table -->
    <x-card :padding="false">
        <x-table>
            <x-slot name="header">
                <x-table-cell type="header" width="50px">
                    <input type="checkbox" class="h-4 w-4 text-emerald-600 border-gray-300 rounded focus:ring-emerald-500">
                </x-table-cell>
                <x-table-cell type="header">Produk</x-table-cell>
                <x-table-cell type="header">Kategori</x-table-cell>
                <x-table-cell type="header" align="center">Stok</x-table-cell>
                <x-table-cell type="header" align="right">Harga Beli</x-table-cell>
                <x-table-cell type="header" align="right">Harga Jual</x-table-cell>
                <x-table-cell type="header" align="center">Status</x-table-cell>
                <x-table-cell type="header" align="center">Aksi</x-table-cell>
            </x-slot>

            @php
                $products = [
                    ['id' => 1, 'name' => 'Indomie Goreng', 'sku' => 'SKU-001', 'image' => null, 'category' => 'Makanan', 'stock' => 245, 'min_stock' => 50, 'buy_price' => 2500, 'sell_price' => 3000, 'status' => 'active'],
                    ['id' => 2, 'name' => 'Aqua 600ml', 'sku' => 'SKU-002', 'image' => null, 'category' => 'Minuman', 'stock' => 198, 'min_stock' => 100, 'buy_price' => 2000, 'sell_price' => 3000, 'status' => 'active'],
                    ['id' => 3, 'name' => 'Teh Pucuk 350ml', 'sku' => 'SKU-003', 'image' => null, 'category' => 'Minuman', 'stock' => 25, 'min_stock' => 50, 'buy_price' => 2500, 'sell_price' => 3000, 'status' => 'low_stock'],
                    ['id' => 4, 'name' => 'Chitato Original 68g', 'sku' => 'SKU-004', 'image' => null, 'category' => 'Snack', 'stock' => 134, 'min_stock' => 30, 'buy_price' => 8000, 'sell_price' => 10000, 'status' => 'active'],
                    ['id' => 5, 'name' => 'Pocari Sweat 350ml', 'sku' => 'SKU-005', 'image' => null, 'category' => 'Minuman', 'stock' => 0, 'min_stock' => 30, 'buy_price' => 5000, 'sell_price' => 6000, 'status' => 'out_of_stock'],
                    ['id' => 6, 'name' => 'Gudang Garam Filter 12', 'sku' => 'SKU-006', 'image' => null, 'category' => 'Rokok', 'stock' => 89, 'min_stock' => 20, 'buy_price' => 22000, 'sell_price' => 25000, 'status' => 'active'],
                    ['id' => 7, 'name' => 'Sabun Lifebuoy 100g', 'sku' => 'SKU-007', 'image' => null, 'category' => 'Toiletries', 'stock' => 45, 'min_stock' => 20, 'buy_price' => 3500, 'sell_price' => 4500, 'status' => 'active'],
                    ['id' => 8, 'name' => 'Mie Sedaap Goreng', 'sku' => 'SKU-008', 'image' => null, 'category' => 'Makanan', 'stock' => 15, 'min_stock' => 50, 'buy_price' => 2500, 'sell_price' => 3000, 'status' => 'low_stock'],
                ];
            @endphp

            @foreach($products as $product)
                <x-table-row>
                    <x-table-cell>
                        <input type="checkbox" class="h-4 w-4 text-emerald-600 border-gray-300 rounded focus:ring-emerald-500">
                    </x-table-cell>
                    <x-table-cell>
                        <div class="flex items-center gap-4">
                            <div class="w-12 h-12 bg-gray-100 rounded-lg flex items-center justify-center flex-shrink-0">
                                @if($product['image'])
                                    <img src="{{ $product['image'] }}" alt="{{ $product['name'] }}" class="w-full h-full object-cover rounded-lg">
                                @else
                                    <svg class="w-6 h-6 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                                    </svg>
                                @endif
                            </div>
                            <div>
                                <p class="font-medium text-gray-900">{{ $product['name'] }}</p>
                                <p class="text-sm text-gray-500 font-mono">{{ $product['sku'] }}</p>
                            </div>
                        </div>
                    </x-table-cell>
                    <x-table-cell>
                        <x-badge variant="gray">{{ $product['category'] }}</x-badge>
                    </x-table-cell>
                    <x-table-cell align="center">
                        <div class="flex flex-col items-center">
                            <span class="font-mono font-semibold {{ $product['stock'] <= $product['min_stock'] ? 'text-red-600' : 'text-gray-900' }}">
                                {{ $product['stock'] }}
                            </span>
                            @if($product['stock'] <= $product['min_stock'])
                                <span class="text-xs text-red-500">Min: {{ $product['min_stock'] }}</span>
                            @endif
                        </div>
                    </x-table-cell>
                    <x-table-cell align="right">
                        <span class="font-mono text-gray-600">Rp {{ number_format($product['buy_price'], 0, ',', '.') }}</span>
                    </x-table-cell>
                    <x-table-cell align="right">
                        <span class="font-mono font-semibold text-gray-900">Rp {{ number_format($product['sell_price'], 0, ',', '.') }}</span>
                    </x-table-cell>
                    <x-table-cell align="center">
                        @if($product['status'] === 'active')
                            <x-badge variant="success" :dot="true">Aktif</x-badge>
                        @elseif($product['status'] === 'low_stock')
                            <x-badge variant="warning" :dot="true">Stok Rendah</x-badge>
                        @elseif($product['status'] === 'out_of_stock')
                            <x-badge variant="danger" :dot="true">Habis</x-badge>
                        @else
                            <x-badge variant="gray" :dot="true">Non-aktif</x-badge>
                        @endif
                    </x-table-cell>
                    <x-table-cell align="center">
                        <div class="flex items-center justify-center gap-2">
                            <button type="button" class="p-1.5 text-gray-400 hover:text-emerald-600 hover:bg-emerald-50 rounded-lg transition-colors" title="Edit" @click="$dispatch('open-modal-edit-product')">
                                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path>
                                </svg>
                            </button>
                            <button type="button" class="p-1.5 text-gray-400 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition-colors" title="Update Stok">
                                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16V4m0 0L3 8m4-4l4 4m6 0v12m0 0l4-4m-4 4l-4-4"></path>
                                </svg>
                            </button>
                            <button type="button" class="p-1.5 text-gray-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition-colors" title="Hapus">
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
            <x-pagination :currentPage="1" :totalPages="10" :perPage="10" :total="80" />
        </div>
    </x-card>

    <!-- Add Product Modal -->
    <x-modal id="add-product" title="Tambah Produk Baru" size="lg">
        <form class="space-y-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div class="md:col-span-2">
                    <x-input name="name" label="Nama Produk" placeholder="Masukkan nama produk" required />
                </div>
                
                <x-input name="sku" label="SKU / Barcode" placeholder="SKU-001" required />
                
                <x-select 
                    name="category" 
                    label="Kategori"
                    :options="[
                        'makanan' => 'Makanan',
                        'minuman' => 'Minuman',
                        'snack' => 'Snack',
                        'rokok' => 'Rokok',
                        'toiletries' => 'Toiletries',
                        'lainnya' => 'Lainnya'
                    ]"
                    required
                />
                
                <x-input type="number" name="buy_price" label="Harga Beli" placeholder="0" prefix="Rp" required />
                
                <x-input type="number" name="sell_price" label="Harga Jual" placeholder="0" prefix="Rp" required />
                
                <x-input type="number" name="stock" label="Stok Awal" placeholder="0" required />
                
                <x-input type="number" name="min_stock" label="Stok Minimum" placeholder="10" hint="Peringatan akan muncul jika stok di bawah nilai ini" />
                
                <div class="md:col-span-2">
                    <label class="block text-sm font-medium text-gray-700 mb-1.5">Gambar Produk</label>
                    <div class="mt-1 flex justify-center px-6 pt-5 pb-6 border-2 border-gray-300 border-dashed rounded-lg hover:border-emerald-400 transition-colors">
                        <div class="space-y-1 text-center">
                            <svg class="mx-auto h-12 w-12 text-gray-400" stroke="currentColor" fill="none" viewBox="0 0 48 48">
                                <path d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                            </svg>
                            <div class="flex text-sm text-gray-600">
                                <label for="file-upload" class="relative cursor-pointer rounded-md font-medium text-emerald-600 hover:text-emerald-500">
                                    <span>Upload file</span>
                                    <input id="file-upload" name="file-upload" type="file" class="sr-only" accept="image/*">
                                </label>
                                <p class="pl-1">atau drag and drop</p>
                            </div>
                            <p class="text-xs text-gray-500">PNG, JPG, GIF max 2MB</p>
                        </div>
                    </div>
                </div>
                
                <div class="md:col-span-2">
                    <x-textarea name="description" label="Deskripsi" placeholder="Deskripsi produk (opsional)" rows="3" />
                </div>
            </div>
        </form>
        
        <x-slot name="footer">
            <x-button variant="secondary" @click="$dispatch('close-modal-add-product')">Batal</x-button>
            <x-button type="submit">Simpan Produk</x-button>
        </x-slot>
    </x-modal>

    <!-- Edit Product Modal -->
    <x-modal id="edit-product" title="Edit Produk" size="lg">
        <form class="space-y-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div class="md:col-span-2">
                    <x-input name="name" label="Nama Produk" value="Indomie Goreng" required />
                </div>
                
                <x-input name="sku" label="SKU / Barcode" value="SKU-001" required />
                
                <x-select 
                    name="category" 
                    label="Kategori"
                    :options="[
                        'makanan' => 'Makanan',
                        'minuman' => 'Minuman',
                        'snack' => 'Snack',
                        'rokok' => 'Rokok',
                        'toiletries' => 'Toiletries',
                        'lainnya' => 'Lainnya'
                    ]"
                    selected="makanan"
                    required
                />
                
                <x-input type="number" name="buy_price" label="Harga Beli" value="2500" prefix="Rp" required />
                
                <x-input type="number" name="sell_price" label="Harga Jual" value="3000" prefix="Rp" required />
                
                <x-input type="number" name="stock" label="Stok Saat Ini" value="245" required />
                
                <x-input type="number" name="min_stock" label="Stok Minimum" value="50" />
            </div>
        </form>
        
        <x-slot name="footer">
            <x-button variant="secondary" @click="$dispatch('close-modal-edit-product')">Batal</x-button>
            <x-button type="submit">Update Produk</x-button>
        </x-slot>
    </x-modal>
</x-layouts.app>
