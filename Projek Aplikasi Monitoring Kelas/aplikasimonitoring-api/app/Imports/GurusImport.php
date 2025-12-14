<?php

namespace App\Imports;

use App\Models\Guru;
use Maatwebsite\Excel\Concerns\ToModel;
use Maatwebsite\Excel\Concerns\WithHeadingRow;
use Maatwebsite\Excel\Concerns\WithValidation;

class GurusImport implements ToModel, WithHeadingRow, WithValidation
{
    public function model(array $row)
    {
        return new Guru([
            'kode_guru' => $row['kode_guru'] ?? $row['kode'],
            'guru' => $row['guru'] ?? $row['nama'] ?? $row['nama_guru'],
            'telepon' => $row['telepon'] ?? $row['no_telepon'] ?? null,
        ]);
    }

    public function rules(): array
    {
        return [
            'kode_guru' => 'required|unique:gurus,kode_guru',
            '*.kode_guru' => 'required|unique:gurus,kode_guru',
        ];
    }
}
