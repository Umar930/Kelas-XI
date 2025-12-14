@extends('admin.layouts.app')

@section('title', 'Tambah Mata Pelajaran')
@section('page-title', 'Tambah Mata Pelajaran')

@section('content')
    <div class="max-w-2xl">
        <div class="bg-white rounded-xl shadow-sm border border-gray-100">
            <div class="p-6 border-b border-gray-100"><h3 class="text-lg font-semibold text-gray-800">Form Tambah Mapel</h3></div>
            <form action="{{ route('admin.mapels.store') }}" method="POST" class="p-6">
                @csrf
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-2">Kode Mapel <span class="text-red-500">*</span></label>
                    <input type="text" name="kode_mapel" value="{{ old('kode_mapel') }}" required class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 @error('kode_mapel') border-red-500 @enderror" placeholder="MTK">
                    @error('kode_mapel')<p class="text-red-500 text-sm mt-1">{{ $message }}</p>@enderror
                </div>
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-2">Nama Mapel <span class="text-red-500">*</span></label>
                    <input type="text" name="mapel" value="{{ old('mapel') }}" required class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 @error('mapel') border-red-500 @enderror" placeholder="Matematika">
                    @error('mapel')<p class="text-red-500 text-sm mt-1">{{ $message }}</p>@enderror
                </div>
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-2">Kategori</label>
                    <input type="text" name="kategori" value="{{ old('kategori') }}" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500" placeholder="Wajib/Peminatan">
                </div>
                <div class="mb-6">
                    <label class="block text-sm font-medium text-gray-700 mb-2">Jam Pelajaran</label>
                    <input type="number" name="jam_pelajaran" value="{{ old('jam_pelajaran') }}" min="1" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500" placeholder="4">
                </div>
                <div class="flex items-center space-x-4">
                    <button type="submit" class="px-6 py-2 bg-indigo-500 hover:bg-indigo-600 text-white rounded-lg"><i class="fas fa-save mr-2"></i>Simpan</button>
                    <a href="{{ route('admin.mapels.index') }}" class="px-6 py-2 bg-gray-300 hover:bg-gray-400 text-gray-700 rounded-lg">Batal</a>
                </div>
            </form>
        </div>
    </div>
@endsection
