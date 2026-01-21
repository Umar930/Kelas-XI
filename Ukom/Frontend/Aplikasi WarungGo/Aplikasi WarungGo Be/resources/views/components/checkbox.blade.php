@props([
    'name' => '',
    'id' => null,
    'label' => null,
    'checked' => false,
    'disabled' => false,
    'description' => null
])

@php
    $inputId = $id ?? $name;
@endphp

<div class="flex items-start">
    <div class="flex items-center h-5">
        <input 
            type="checkbox"
            name="{{ $name }}"
            id="{{ $inputId }}"
            {{ $checked ? 'checked' : '' }}
            {{ $disabled ? 'disabled' : '' }}
            {{ $attributes->merge(['class' => 'h-4 w-4 text-emerald-600 border-gray-300 rounded focus:ring-emerald-500 disabled:opacity-50 disabled:cursor-not-allowed transition-colors']) }}
        >
    </div>
    
    @if($label || $description)
        <div class="ml-3 text-sm">
            @if($label)
                <label for="{{ $inputId }}" class="font-medium text-gray-700 {{ $disabled ? 'opacity-50' : 'cursor-pointer' }}">
                    {{ $label }}
                </label>
            @endif
            
            @if($description)
                <p class="text-gray-500">{{ $description }}</p>
            @endif
        </div>
    @endif
</div>
