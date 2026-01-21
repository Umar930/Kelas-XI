@props([
    'name' => '',
    'id' => null,
    'label' => null,
    'options' => [],
    'selected' => null,
    'placeholder' => 'Pilih opsi...',
    'error' => null,
    'required' => false,
    'disabled' => false
])

@php
    $inputId = $id ?? $name;
    $baseClasses = 'block w-full px-4 py-2.5 text-gray-900 border rounded-lg bg-white focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 transition-colors duration-200 disabled:bg-gray-100 disabled:cursor-not-allowed appearance-none';
    $errorClasses = $error ? 'border-red-500 focus:ring-red-500 focus:border-red-500' : 'border-gray-300';
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
        <select 
            name="{{ $name }}"
            id="{{ $inputId }}"
            {{ $required ? 'required' : '' }}
            {{ $disabled ? 'disabled' : '' }}
            {{ $attributes->merge(['class' => $baseClasses . ' ' . $errorClasses]) }}
        >
            @if($placeholder)
                <option value="" disabled {{ !$selected ? 'selected' : '' }}>{{ $placeholder }}</option>
            @endif
            
            @foreach($options as $value => $text)
                <option value="{{ $value }}" {{ $selected == $value ? 'selected' : '' }}>{{ $text }}</option>
            @endforeach
        </select>
        
        <div class="absolute inset-y-0 right-0 flex items-center pr-3 pointer-events-none text-gray-400">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
            </svg>
        </div>
    </div>
    
    @if($error)
        <p class="mt-1.5 text-sm text-red-600">{{ $error }}</p>
    @endif
</div>
