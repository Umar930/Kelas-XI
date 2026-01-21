@props([
    'src' => null,
    'alt' => '',
    'size' => 'md',
    'initials' => null,
    'status' => null
])

@php
    $sizes = [
        'xs' => 'h-6 w-6 text-xs',
        'sm' => 'h-8 w-8 text-sm',
        'md' => 'h-10 w-10 text-base',
        'lg' => 'h-12 w-12 text-lg',
        'xl' => 'h-16 w-16 text-xl',
        '2xl' => 'h-20 w-20 text-2xl',
    ];
    
    $statusColors = [
        'online' => 'bg-green-500',
        'offline' => 'bg-gray-400',
        'busy' => 'bg-red-500',
        'away' => 'bg-amber-500',
    ];
    
    $statusSizes = [
        'xs' => 'h-1.5 w-1.5',
        'sm' => 'h-2 w-2',
        'md' => 'h-2.5 w-2.5',
        'lg' => 'h-3 w-3',
        'xl' => 'h-3.5 w-3.5',
        '2xl' => 'h-4 w-4',
    ];
    
    $sizeClass = $sizes[$size] ?? $sizes['md'];
    $statusColor = $statusColors[$status] ?? null;
    $statusSize = $statusSizes[$size] ?? $statusSizes['md'];
@endphp

<div class="relative inline-flex">
    @if($src)
        <img 
            src="{{ $src }}" 
            alt="{{ $alt }}"
            {{ $attributes->merge(['class' => "$sizeClass rounded-full object-cover ring-2 ring-white"]) }}
        >
    @else
        <div {{ $attributes->merge(['class' => "$sizeClass rounded-full bg-emerald-100 text-emerald-700 font-medium flex items-center justify-center ring-2 ring-white"]) }}>
            {{ $initials ?? strtoupper(substr($alt, 0, 2)) }}
        </div>
    @endif
    
    @if($status && $statusColor)
        <span class="absolute bottom-0 right-0 block {{ $statusSize }} {{ $statusColor }} rounded-full ring-2 ring-white"></span>
    @endif
</div>
