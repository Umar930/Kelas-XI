@props([
    'type' => 'text',
    'name' => '',
    'id' => null,
    'label' => null,
    'placeholder' => '',
    'value' => '',
    'error' => null,
    'hint' => null,
    'required' => false,
    'disabled' => false,
    'readonly' => false,
    'icon' => null,
    'iconPosition' => 'left',
    'prefix' => null,
    'suffix' => null
])

@php
    $inputId = $id ?? $name;
    $baseClasses = 'block w-full px-4 py-2.5 text-gray-900 border rounded-lg bg-white focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 transition-colors duration-200 disabled:bg-gray-100 disabled:cursor-not-allowed';
    $errorClasses = $error ? 'border-red-500 focus:ring-red-500 focus:border-red-500' : 'border-gray-300';
    $iconPadding = $icon ? ($iconPosition === 'left' ? 'pl-10' : 'pr-10') : '';
    $prefixPadding = $prefix ? 'pl-16' : '';
    $suffixPadding = $suffix ? 'pr-16' : '';
@endphp

<div class="w-full">
    @if($label)
        <label for="{{ $inputId }}" class="block text-sm font-medium text-gray-700 mb-1.5">
            {{ $label }}
            @if($required)
                <span class="text-red-500">*</span>
            @endif
        </label>
    @endif
    
    <div class="relative">
        @if($icon && $iconPosition === 'left')
            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-gray-400">
                {{ $icon }}
            </div>
        @endif
        
        @if($prefix)
            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <span class="text-gray-500 sm:text-sm">{{ $prefix }}</span>
            </div>
        @endif
        
        <input 
            type="{{ $type }}"
            name="{{ $name }}"
            id="{{ $inputId }}"
            value="{{ $value }}"
            placeholder="{{ $placeholder }}"
            {{ $required ? 'required' : '' }}
            {{ $disabled ? 'disabled' : '' }}
            {{ $readonly ? 'readonly' : '' }}
            {{ $attributes->merge(['class' => $baseClasses . ' ' . $errorClasses . ' ' . $iconPadding . ' ' . $prefixPadding . ' ' . $suffixPadding]) }}
        >
        
        @if($icon && $iconPosition === 'right')
            <div class="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none text-gray-400">
                {{ $icon }}
            </div>
        @endif
        
        @if($suffix)
            <div class="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
                <span class="text-gray-500 sm:text-sm">{{ $suffix }}</span>
            </div>
        @endif
    </div>
    
    @if($error)
        <p class="mt-1.5 text-sm text-red-600">{{ $error }}</p>
    @elseif($hint)
        <p class="mt-1.5 text-sm text-gray-500">{{ $hint }}</p>
    @endif
</div>
