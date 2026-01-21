@props([
    'enabled' => false,
    'disabled' => false,
    'size' => 'md'
])

@php
    $sizes = [
        'sm' => [
            'toggle' => 'h-5 w-9',
            'dot' => 'h-4 w-4',
            'translate' => 'translate-x-4',
        ],
        'md' => [
            'toggle' => 'h-6 w-11',
            'dot' => 'h-5 w-5',
            'translate' => 'translate-x-5',
        ],
        'lg' => [
            'toggle' => 'h-7 w-14',
            'dot' => 'h-6 w-6',
            'translate' => 'translate-x-7',
        ],
    ];
    
    $sizeConfig = $sizes[$size] ?? $sizes['md'];
@endphp

<button
    type="button"
    x-data="{ enabled: {{ $enabled ? 'true' : 'false' }} }"
    @click="enabled = !enabled"
    :class="enabled ? 'bg-emerald-600' : 'bg-gray-200'"
    :aria-pressed="enabled.toString()"
    {{ $disabled ? 'disabled' : '' }}
    {{ $attributes->merge(['class' => "relative inline-flex flex-shrink-0 {$sizeConfig['toggle']} border-2 border-transparent rounded-full cursor-pointer transition-colors ease-in-out duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-emerald-500 disabled:opacity-50 disabled:cursor-not-allowed"]) }}
    role="switch"
>
    <span class="sr-only">Toggle</span>
    <span 
        :class="enabled ? '{{ $sizeConfig['translate'] }}' : 'translate-x-0'"
        class="pointer-events-none inline-block {{ $sizeConfig['dot'] }} rounded-full bg-white shadow transform ring-0 transition ease-in-out duration-200"
    ></span>
</button>
