@props([
    'align' => 'right',
    'width' => '48',
    'contentClasses' => 'py-1 bg-white'
])

@php
    $alignmentClasses = match ($align) {
        'left' => 'left-0 origin-top-left',
        'top' => 'origin-top',
        'right' => 'right-0 origin-top-right',
        default => 'right-0 origin-top-right',
    };

    $widthClass = match ($width) {
        '48' => 'w-48',
        '56' => 'w-56',
        '64' => 'w-64',
        'full' => 'w-full',
        default => 'w-48',
    };
@endphp

<div 
    x-data="{ open: false }" 
    @click.outside="open = false" 
    @close.stop="open = false"
    class="relative"
>
    <!-- Trigger -->
    <div @click="open = !open">
        {{ $trigger }}
    </div>

    <!-- Dropdown Menu -->
    <div
        x-show="open"
        x-transition:enter="transition ease-out duration-200"
        x-transition:enter-start="opacity-0 scale-95"
        x-transition:enter-end="opacity-100 scale-100"
        x-transition:leave="transition ease-in duration-100"
        x-transition:leave-start="opacity-100 scale-100"
        x-transition:leave-end="opacity-0 scale-95"
        class="absolute z-50 mt-2 {{ $widthClass }} {{ $alignmentClasses }} rounded-lg shadow-lg ring-1 ring-black ring-opacity-5 {{ $contentClasses }}"
        style="display: none;"
        @click="open = false"
    >
        {{ $content }}
    </div>
</div>
