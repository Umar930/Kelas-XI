@props([
    'title' => '',
    'value' => '',
    'change' => null,
    'changeType' => 'increase',
    'icon' => null,
    'iconBg' => 'bg-emerald-100',
    'iconColor' => 'text-emerald-600'
])

<div {{ $attributes->merge(['class' => 'bg-white rounded-xl shadow-sm border border-gray-200 p-6']) }}>
    <div class="flex items-center justify-between">
        <div class="flex-1">
            <p class="text-sm font-medium text-gray-500">{{ $title }}</p>
            <p class="mt-2 text-3xl font-bold text-gray-900 font-mono">{{ $value }}</p>
            
            @if($change !== null)
                <div class="mt-2 flex items-center gap-1">
                    @if($changeType === 'increase')
                        <svg class="h-4 w-4 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 10l7-7m0 0l7 7m-7-7v18"></path>
                        </svg>
                        <span class="text-sm font-medium text-green-600">{{ $change }}</span>
                    @elseif($changeType === 'decrease')
                        <svg class="h-4 w-4 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 14l-7 7m0 0l-7-7m7 7V3"></path>
                        </svg>
                        <span class="text-sm font-medium text-red-600">{{ $change }}</span>
                    @else
                        <span class="text-sm font-medium text-gray-500">{{ $change }}</span>
                    @endif
                    <span class="text-sm text-gray-400">vs bulan lalu</span>
                </div>
            @endif
        </div>
        
        @if($icon)
            <div class="flex-shrink-0 {{ $iconBg }} {{ $iconColor }} p-3 rounded-xl">
                {{ $icon }}
            </div>
        @endif
    </div>
</div>
