@props([
    'type' => 'body',
    'align' => 'left',
    'sortable' => false,
    'sortDirection' => null,
    'width' => null
])

@php
    $alignments = [
        'left' => 'text-left',
        'center' => 'text-center',
        'right' => 'text-right',
    ];
    
    $alignClass = $alignments[$align] ?? $alignments['left'];
    
    if ($type === 'header') {
        $baseClasses = 'px-6 py-3 text-xs font-semibold text-gray-600 uppercase tracking-wider ' . $alignClass;
    } else {
        $baseClasses = 'px-6 py-4 whitespace-nowrap text-sm text-gray-900 ' . $alignClass;
    }
    
    $widthStyle = $width ? "width: {$width};" : '';
@endphp

@if($type === 'header')
    <th 
        scope="col" 
        {{ $attributes->merge(['class' => $baseClasses]) }}
        @if($width) style="{{ $widthStyle }}" @endif
    >
        @if($sortable)
            <button type="button" class="group inline-flex items-center gap-1 hover:text-gray-900">
                {{ $slot }}
                <span class="flex-none rounded text-gray-400 group-hover:text-gray-500">
                    @if($sortDirection === 'asc')
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 15l7-7 7 7"></path>
                        </svg>
                    @elseif($sortDirection === 'desc')
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                        </svg>
                    @else
                        <svg class="w-4 h-4 opacity-0 group-hover:opacity-100" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16V4m0 0L3 8m4-4l4 4m6 0v12m0 0l4-4m-4 4l-4-4"></path>
                        </svg>
                    @endif
                </span>
            </button>
        @else
            {{ $slot }}
        @endif
    </th>
@else
    <td 
        {{ $attributes->merge(['class' => $baseClasses]) }}
        @if($width) style="{{ $widthStyle }}" @endif
    >
        {{ $slot }}
    </td>
@endif
