@props([
    'name' => '',
    'id' => null,
    'label' => null,
    'rows' => 4,
    'placeholder' => '',
    'value' => '',
    'error' => null,
    'hint' => null,
    'required' => false,
    'disabled' => false,
    'readonly' => false,
    'maxlength' => null
])

@php
    $inputId = $id ?? $name;
    $baseClasses = 'block w-full px-4 py-2.5 text-gray-900 border rounded-lg bg-white focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 transition-colors duration-200 disabled:bg-gray-100 disabled:cursor-not-allowed resize-none';
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
    
    <textarea 
        name="{{ $name }}"
        id="{{ $inputId }}"
        rows="{{ $rows }}"
        placeholder="{{ $placeholder }}"
        {{ $required ? 'required' : '' }}
        {{ $disabled ? 'disabled' : '' }}
        {{ $readonly ? 'readonly' : '' }}
        @if($maxlength) maxlength="{{ $maxlength }}" @endif
        {{ $attributes->merge(['class' => $baseClasses . ' ' . $errorClasses]) }}
    >{{ $value }}</textarea>
    
    <div class="flex items-center justify-between mt-1.5">
        @if($error)
            <p class="text-sm text-red-600">{{ $error }}</p>
        @elseif($hint)
            <p class="text-sm text-gray-500">{{ $hint }}</p>
        @else
            <span></span>
        @endif
        
        @if($maxlength)
            <p class="text-sm text-gray-400">
                <span x-data="{ count: '{{ strlen($value) }}' }" x-text="count"></span>/{{ $maxlength }}
            </p>
        @endif
    </div>
</div>
