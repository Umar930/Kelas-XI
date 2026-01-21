@props([
    'hoverable' => true,
    'clickable' => false,
    'striped' => false,
    'index' => 0
])

@php
    $baseClasses = 'bg-white';
    $hoverClasses = $hoverable ? 'hover:bg-gray-50 transition-colors' : '';
    $clickableClasses = $clickable ? 'cursor-pointer' : '';
    $stripedClasses = $striped && $index % 2 === 1 ? 'bg-gray-50' : '';
@endphp

<tr {{ $attributes->merge(['class' => trim("$baseClasses $hoverClasses $clickableClasses $stripedClasses")]) }}>
    {{ $slot }}
</tr>
