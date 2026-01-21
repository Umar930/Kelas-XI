@props([
    'padding' => true,
    'shadow' => 'sm',
    'rounded' => 'xl',
    'border' => true,
    'hover' => false
])

@php
    $shadows = [
        'none' => '',
        'sm' => 'shadow-sm',
        'md' => 'shadow-md',
        'lg' => 'shadow-lg',
        'xl' => 'shadow-xl',
    ];
    
    $roundedOptions = [
        'none' => '',
        'sm' => 'rounded-sm',
        'md' => 'rounded-md',
        'lg' => 'rounded-lg',
        'xl' => 'rounded-xl',
        '2xl' => 'rounded-2xl',
    ];
    
    $classes = 'bg-white overflow-hidden ' . 
               ($shadows[$shadow] ?? $shadows['sm']) . ' ' . 
               ($roundedOptions[$rounded] ?? $roundedOptions['xl']) . ' ' .
               ($border ? 'border border-gray-200' : '') . ' ' .
               ($hover ? 'hover:shadow-md transition-shadow duration-200' : '');
@endphp

<div {{ $attributes->merge(['class' => trim($classes)]) }}>
    @if(isset($header))
        <div class="px-6 py-4 border-b border-gray-200 {{ isset($headerClass) ? $headerClass : '' }}">
            {{ $header }}
        </div>
    @endif
    
    <div class="{{ $padding ? 'p-6' : '' }}">
        {{ $slot }}
    </div>
    
    @if(isset($footer))
        <div class="px-6 py-4 border-t border-gray-200 bg-gray-50 {{ isset($footerClass) ? $footerClass : '' }}">
            {{ $footer }}
        </div>
    @endif
</div>
