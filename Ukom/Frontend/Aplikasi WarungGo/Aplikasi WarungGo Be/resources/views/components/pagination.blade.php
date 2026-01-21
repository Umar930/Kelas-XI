@props([
    'currentPage' => 1,
    'totalPages' => 1,
    'perPage' => 10,
    'total' => 0
])

@php
    $start = (($currentPage - 1) * $perPage) + 1;
    $end = min($currentPage * $perPage, $total);
@endphp

<div class="flex flex-col sm:flex-row items-center justify-between gap-4">
    <!-- Info -->
    <div class="text-sm text-gray-700">
        Menampilkan <span class="font-medium">{{ $start }}</span> sampai <span class="font-medium">{{ $end }}</span> dari <span class="font-medium">{{ $total }}</span> hasil
    </div>
    
    <!-- Pagination Links -->
    <nav class="flex items-center gap-1" aria-label="Pagination">
        <!-- Previous -->
        <button 
            type="button"
            {{ $currentPage <= 1 ? 'disabled' : '' }}
            class="relative inline-flex items-center px-2 py-2 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
        >
            <span class="sr-only">Previous</span>
            <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"></path>
            </svg>
        </button>
        
        <!-- Page Numbers -->
        @for($i = 1; $i <= min(5, $totalPages); $i++)
            @php
                $pageNumber = $i;
                if ($totalPages > 5) {
                    if ($currentPage <= 3) {
                        $pageNumber = $i;
                    } elseif ($currentPage >= $totalPages - 2) {
                        $pageNumber = $totalPages - 5 + $i;
                    } else {
                        $pageNumber = $currentPage - 3 + $i;
                    }
                }
            @endphp
            <button 
                type="button"
                class="relative inline-flex items-center px-4 py-2 text-sm font-medium rounded-lg {{ $pageNumber === $currentPage ? 'bg-emerald-600 text-white' : 'text-gray-700 bg-white border border-gray-300 hover:bg-gray-50' }}"
            >
                {{ $pageNumber }}
            </button>
        @endfor
        
        <!-- Next -->
        <button 
            type="button"
            {{ $currentPage >= $totalPages ? 'disabled' : '' }}
            class="relative inline-flex items-center px-2 py-2 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
        >
            <span class="sr-only">Next</span>
            <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
            </svg>
        </button>
    </nav>
</div>
