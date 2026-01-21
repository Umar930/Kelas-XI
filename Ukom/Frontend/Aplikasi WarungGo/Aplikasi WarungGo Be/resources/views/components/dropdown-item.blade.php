@props([
    'active' => false
])

@php
    $classes = $active
        ? 'block w-full px-4 py-2 text-left text-sm text-emerald-700 bg-emerald-50 font-medium'
        : 'block w-full px-4 py-2 text-left text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900 transition-colors';
@endphp

<button {{ $attributes->merge(['type' => 'button', 'class' => $classes]) }}>
    {{ $slot }}
</button>
