@extends('admin.layouts.app')

@section('title', 'Edit Tahun Ajaran')
@section('page-title', 'Edit Tahun Ajaran')

@section('content')
    <div class="max-w-2xl">
        <div class="bg-white rounded-xl shadow-sm border border-gray-100">
            <div class="p-6 border-b border-gray-100"><h3 class="text-lg font-semibold text-gray-800">Form Edit Tahun Ajaran</h3></div>
            <form action="{{ route('admin.tahun-ajaran.update', $tahunAjaran) }}" method="POST" class="p-6">
                @csrf @method('PUT')
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-2">Tahun Ajaran <span class="text-red-500">*</span></label>
                    <input type="text" name="tahun" value="{{ old('tahun', $tahunAjaran->tahun) }}" required class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 @error('tahun') border-red-500 @enderror">
                    @error('tahun')<p class="text-red-500 text-sm mt-1">{{ $message }}</p>@enderror
                </div>
                <div class="mb-6">
                    <label class="flex items-center">
                        <input type="checkbox" name="flag" value="1" {{ old('flag', $tahunAjaran->flag) ? 'checked' : '' }} class="w-4 h-4 text-indigo-500 border-gray-300 rounded focus:ring-indigo-500">
                        <span class="ml-2 text-sm text-gray-700">Jadikan sebagai tahun ajaran aktif</span>
                    </label>
                </div>
                <div class="flex items-center space-x-4">
                    <button type="submit" class="px-6 py-2 bg-indigo-500 hover:bg-indigo-600 text-white rounded-lg"><i class="fas fa-save mr-2"></i>Update</button>
                    <a href="{{ route('admin.tahun-ajaran.index') }}" class="px-6 py-2 bg-gray-300 hover:bg-gray-400 text-gray-700 rounded-lg">Batal</a>
                </div>
            </form>
        </div>
    </div>
@endsection
