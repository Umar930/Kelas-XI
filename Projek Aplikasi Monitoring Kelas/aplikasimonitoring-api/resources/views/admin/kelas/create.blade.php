@extends('admin.layouts.app')

@section('title', 'Tambah Kelas')
@section('page-title', 'Tambah Kelas')

@section('content')
    <div class="max-w-2xl">
        <div class="bg-white rounded-xl shadow-sm border border-gray-100">
            <div class="p-6 border-b border-gray-100"><h3 class="text-lg font-semibold text-gray-800">Form Tambah Kelas</h3></div>
            <form action="{{ route('admin.kelas.store') }}" method="POST" class="p-6">
                @csrf
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-2">Kode Kelas <span class="text-red-500">*</span></label>
                    <input type="text" name="kode_kelas" value="{{ old('kode_kelas') }}" required class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 @error('kode_kelas') border-red-500 @enderror" placeholder="XII-RPL-1">
                    @error('kode_kelas')<p class="text-red-500 text-sm mt-1">{{ $message }}</p>@enderror
                </div>
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-2">Nama Kelas <span class="text-red-500">*</span></label>
                    <input type="text" name="nama_kelas" value="{{ old('nama_kelas') }}" required class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 @error('nama_kelas') border-red-500 @enderror" placeholder="XII RPL 1">
                    @error('nama_kelas')<p class="text-red-500 text-sm mt-1">{{ $message }}</p>@enderror
                </div>
                <div class="grid grid-cols-2 gap-4 mb-4">
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Tingkat <span class="text-red-500">*</span></label>
                        <select name="tingkat" required class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500">
                            <option value="">Pilih</option>
                            <option value="X" {{ old('tingkat') == 'X' ? 'selected' : '' }}>X</option>
                            <option value="XI" {{ old('tingkat') == 'XI' ? 'selected' : '' }}>XI</option>
                            <option value="XII" {{ old('tingkat') == 'XII' ? 'selected' : '' }}>XII</option>
                        </select>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Jurusan <span class="text-red-500">*</span></label>
                        <input type="text" name="jurusan" value="{{ old('jurusan') }}" required class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500" placeholder="RPL">
                    </div>
                </div>
                <div class="grid grid-cols-2 gap-4 mb-6">
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Wali Kelas</label>
                        <select name="guru_id" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500">
                            <option value="">Pilih Guru</option>
                            @foreach($gurus as $guru)
                                <option value="{{ $guru->id }}" {{ old('guru_id') == $guru->id ? 'selected' : '' }}>{{ $guru->guru }}</option>
                            @endforeach
                        </select>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Kapasitas</label>
                        <input type="number" name="kapasitas" value="{{ old('kapasitas', 30) }}" min="1" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500">
                    </div>
                </div>
                <div class="flex items-center space-x-4">
                    <button type="submit" class="px-6 py-2 bg-indigo-500 hover:bg-indigo-600 text-white rounded-lg"><i class="fas fa-save mr-2"></i>Simpan</button>
                    <a href="{{ route('admin.kelas.index') }}" class="px-6 py-2 bg-gray-300 hover:bg-gray-400 text-gray-700 rounded-lg">Batal</a>
                </div>
            </form>
        </div>
    </div>
@endsection
