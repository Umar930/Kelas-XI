@props([
    'striped' => false,
    'hoverable' => true,
    'bordered' => true
])

@php
    $tableClasses = 'min-w-full divide-y divide-gray-200';
@endphp

<div class="overflow-x-auto rounded-lg {{ $bordered ? 'border border-gray-200' : '' }}">
    <table {{ $attributes->merge(['class' => $tableClasses]) }}>
        @if(isset($header))
            <thead class="bg-gray-50">
                <tr>
                    {{ $header }}
                </tr>
            </thead>
        @endif
        
        <tbody class="divide-y divide-gray-200 bg-white">
            {{ $slot }}
        </tbody>
        
        @if(isset($footer))
            <tfoot class="bg-gray-50">
                {{ $footer }}
            </tfoot>
        @endif
    </table>
</div>

@if(isset($pagination))
    <div class="mt-4">
        {{ $pagination }}
    </div>
@endif
