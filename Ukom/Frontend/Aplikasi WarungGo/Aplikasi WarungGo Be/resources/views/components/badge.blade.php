@props([
    'variant' => 'gray',
    'size' => 'md',
    'rounded' => 'full',
    'dot' => false
])

@php
    $variants = [
        'success' => 'bg-green-100 text-green-800',
        'warning' => 'bg-amber-100 text-amber-800',
        'danger' => 'bg-red-100 text-red-800',
        'info' => 'bg-blue-100 text-blue-800',
        'gray' => 'bg-gray-100 text-gray-800',
        'primary' => 'bg-emerald-100 text-emerald-800',
        'purple' => 'bg-purple-100 text-purple-800',
    ];
    
    $sizes = [
        'sm' => 'px-2 py-0.5 text-xs',
        'md' => 'px-2.5 py-0.5 text-xs',
        'lg' => 'px-3 py-1 text-sm',
    ];
    
    $roundedOptions = [
        'none' => 'rounded-none',
        'sm' => 'rounded',
        'md' => 'rounded-md',
        'lg' => 'rounded-lg',
        'full' => 'rounded-full',
    ];
    
    $dotColors = [
        'success' => 'bg-green-500',
        'warning' => 'bg-amber-500',
        'danger' => 'bg-red-500',
        'info' => 'bg-blue-500',
        'gray' => 'bg-gray-500',
        'primary' => 'bg-emerald-500',
        'purple' => 'bg-purple-500',
    ];
    
    $classes = 'inline-flex items-center font-medium ' . ($variants[$variant] ?? $variants['gray']) . ' ' . ($sizes[$size] ?? $sizes['md']) . ' ' . ($roundedOptions[$rounded] ?? $roundedOptions['full']);
@endphp

<span {{ $attributes->merge(['class' => $classes]) }}>
    @if($dot)
        <span class="w-1.5 h-1.5 mr-1.5 rounded-full {{ $dotColors[$variant] ?? $dotColors['gray'] }}"></span>
    @endif
    {{ $slot }}
</span>
