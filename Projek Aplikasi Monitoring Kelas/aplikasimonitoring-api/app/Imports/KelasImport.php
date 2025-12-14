<?php

namespace App\Imports;

use App\Models\Kelas;
use Maatwebsite\Excel\Concerns\ToModel;
use Maatwebsite\Excel\Concerns\WithHeadingRow;
use Maatwebsite\Excel\Concerns\WithValidation;

class KelasImport implements ToModel, WithHeadingRow, WithValidation
{
    public function model(array $row)
    {
        return new Kelas([
            'kode_kelas' => $row['kode_kelas'] ?? $row['kode'],
            'nama_kelas' => $row['nama_kelas'] ?? $row['nama'],
            'guru_id' => $row['guru_id'] ?? null,
            'tingkat' => $row['tingkat'] ?? 'X',
            'jurusan' => $row['jurusan'] ?? '-',
            'kapasitas' => $row['kapasitas'] ?? 30,
        ]);
    }

    public function rules(): array
    {
        return [
            'kode_kelas' => 'required|unique:kelas,kode_kelas',
            '*.kode_kelas' => 'required|unique:kelas,kode_kelas',
        ];
    }
}
