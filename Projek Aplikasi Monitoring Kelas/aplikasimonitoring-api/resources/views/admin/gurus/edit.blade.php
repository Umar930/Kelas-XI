@extends('admin.layouts.app')

@section('title', 'Edit Guru')
@section('page-title', 'Edit Guru')
@section('page-subtitle', 'Ubah data guru')

@section('content')
    <div class="max-w-2xl">
        <div class="bg-white rounded-xl shadow-sm border border-gray-100">
            <div class="p-6 border-b border-gray-100">
                <h3 class="text-lg font-semibold text-gray-800">Form Edit Guru</h3>
            </div>
            
            <form action="{{ route('admin.gurus.update', $guru) }}" method="POST" class="p-6">
                @csrf
                @method('PUT')
                
                <div class="mb-6">
                    <label for="kode_guru" class="block text-sm font-medium text-gray-700 mb-2">Kode Guru <span class="text-red-500">*</span></label>
                    <input type="text" id="kode_guru" name="kode_guru" value="{{ old('kode_guru', $guru->kode_guru) }}" required
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 @error('kode_guru') border-red-500 @enderror">
                    @error('kode_guru')<p class="text-red-500 text-sm mt-1">{{ $message }}</p>@enderror
                </div>
                
                <div class="mb-6">
                    <label for="guru" class="block text-sm font-medium text-gray-700 mb-2">Nama Guru <span class="text-red-500">*</span></label>
                    <input type="text" id="guru" name="guru" value="{{ old('guru', $guru->guru) }}" required
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 @error('guru') border-red-500 @enderror">
                    @error('guru')<p class="text-red-500 text-sm mt-1">{{ $message }}</p>@enderror
                </div>
                
                <div class="mb-6">
                    <label for="telepon" class="block text-sm font-medium text-gray-700 mb-2">No. Telepon</label>
                    <input type="text" id="telepon" name="telepon" value="{{ old('telepon', $guru->telepon) }}"
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 @error('telepon') border-red-500 @enderror">
                    @error('telepon')<p class="text-red-500 text-sm mt-1">{{ $message }}</p>@enderror
                </div>
                
                <div class="flex items-center justify-end space-x-4 pt-6 border-t border-gray-100">
                    <a href="{{ route('admin.gurus.index') }}" class="px-6 py-2 bg-gray-300 hover:bg-gray-400 text-gray-700 rounded-lg transition-colors">Batal</a>
                    <button type="submit" class="px-6 py-2 bg-indigo-500 hover:bg-indigo-600 text-white rounded-lg transition-colors">
                        <i class="fas fa-save mr-2"></i>Update
                    </button>
                </div>
            </form>
        </div>
    </div>
@endsection
