@extends('admin.layouts.app')

@section('title', 'Edit Guru Pengganti')
@section('page-title', 'Edit Guru Pengganti')
@section('page-subtitle', 'Ubah data guru pengganti')

@section('content')
    <div class="max-w-2xl">
        <div class="bg-white rounded-xl shadow-sm border border-gray-100">
            <div class="p-6 border-b border-gray-100">
                <h3 class="text-lg font-semibold text-gray-800">Form Edit Guru Pengganti</h3>
            </div>
            <form action="{{ route('admin.guru-pengganti.update', $guruPengganti) }}" method="POST" class="p-6">
                @csrf
                @method('PUT')
                <div class="mb-6">
                    <label class="block text-sm font-medium text-gray-700 mb-2">Tanggal <span class="text-red-500">*</span></label>
                    <input type="date" name="tanggal" value="{{ old('tanggal', $guruPengganti->tanggal ? $guruPengganti->tanggal->format('Y-m-d') : '') }}" required class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 @error('tanggal') border-red-500 @enderror">
                    @error('tanggal')<p class="mt-1 text-sm text-red-500">{{ $message }}</p>@enderror
                </div>
                
                <div class="mb-6">
                    <label class="block text-sm font-medium text-gray-700 mb-2">Guru Pengganti <span class="text-red-500">*</span></label>
                    <select name="guru_pengganti_id" required class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 @error('guru_pengganti_id') border-red-500 @enderror">
                        <option value="">Pilih Guru Pengganti</option>
                        @foreach($gurus as $g)
                            <option value="{{ $g->id }}" {{ old('guru_pengganti_id', $guruPengganti->guru_pengganti_id) == $g->id ? 'selected' : '' }}>{{ $g->guru }} - {{ $g->kode_guru }}</option>
                        @endforeach
                    </select>
                    @error('guru_pengganti_id')<p class="mt-1 text-sm text-red-500">{{ $message }}</p>@enderror
                </div>
                
                <div class="mb-6">
                    <label class="block text-sm font-medium text-gray-700 mb-2">Kelas <span class="text-red-500">*</span></label>
                    <select name="kelas_id" required class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 @error('kelas_id') border-red-500 @enderror">
                        <option value="">Pilih Kelas</option>
                        @foreach($kelasList as $k)
                            <option value="{{ $k->id }}" {{ old('kelas_id', $guruPengganti->kelas_id) == $k->id ? 'selected' : '' }}>{{ $k->nama_kelas }}</option>
                        @endforeach
                    </select>
                    @error('kelas_id')<p class="mt-1 text-sm text-red-500">{{ $message }}</p>@enderror
                </div>
                
                <div class="mb-6">
                    <label class="block text-sm font-medium text-gray-700 mb-2">Mata Pelajaran <span class="text-red-500">*</span></label>
                    <select name="mapel_id" required class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 @error('mapel_id') border-red-500 @enderror">
                        <option value="">Pilih Mata Pelajaran</option>
                        @foreach($mapels as $m)
                            <option value="{{ $m->id }}" {{ old('mapel_id', $guruPengganti->mapel_id) == $m->id ? 'selected' : '' }}>{{ $m->mapel }}</option>
                        @endforeach
                    </select>
                    @error('mapel_id')<p class="mt-1 text-sm text-red-500">{{ $message }}</p>@enderror
                </div>
                
                <div class="grid grid-cols-2 gap-6 mb-6">
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Jam Mulai</label>
                        <input type="time" name="jam_mulai" value="{{ old('jam_mulai', $guruPengganti->jam_mulai) }}" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Jam Selesai</label>
                        <input type="time" name="jam_selesai" value="{{ old('jam_selesai', $guruPengganti->jam_selesai) }}" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500">
                    </div>
                </div>
                
                <div class="mb-6">
                    <label class="block text-sm font-medium text-gray-700 mb-2">Catatan</label>
                    <textarea name="catatan" rows="3" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500" placeholder="Catatan...">{{ old('catatan', $guruPengganti->catatan) }}</textarea>
                </div>
                
                <div class="mb-6">
                    <label class="block text-sm font-medium text-gray-700 mb-2">Status <span class="text-red-500">*</span></label>
                    <select name="status" required class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500">
                        <option value="pending" {{ old('status', $guruPengganti->status) == 'pending' ? 'selected' : '' }}>Pending</option>
                        <option value="aktif" {{ old('status', $guruPengganti->status) == 'aktif' ? 'selected' : '' }}>Aktif</option>
                        <option value="selesai" {{ old('status', $guruPengganti->status) == 'selesai' ? 'selected' : '' }}>Selesai</option>
                        <option value="ditolak" {{ old('status', $guruPengganti->status) == 'ditolak' ? 'selected' : '' }}>Ditolak</option>
                    </select>
                </div>
                
                <div class="flex items-center justify-end space-x-4 pt-6 border-t border-gray-100">
                    <a href="{{ route('admin.guru-pengganti.index') }}" class="px-6 py-2 bg-gray-300 hover:bg-gray-400 text-gray-700 rounded-lg">Batal</a>
                    <button type="submit" class="px-6 py-2 bg-indigo-500 hover:bg-indigo-600 text-white rounded-lg"><i class="fas fa-save mr-2"></i>Update</button>
                </div>
            </form>
        </div>
    </div>
@endsection
