<?php

namespace App\Imports;

use App\Models\Mapel;
use Maatwebsite\Excel\Concerns\ToModel;
use Maatwebsite\Excel\Concerns\WithHeadingRow;
use Maatwebsite\Excel\Concerns\WithValidation;

class MapelsImport implements ToModel, WithHeadingRow, WithValidation
{
    public function model(array $row)
    {
        return new Mapel([
            'kode_mapel' => $row['kode_mapel'] ?? $row['kode'],
            'mapel' => $row['mapel'] ?? $row['nama_mapel'] ?? $row['mata_pelajaran'],
            'kategori' => $row['kategori'] ?? null,
            'jam_pelajaran' => $row['jam_pelajaran'] ?? $row['jam'] ?? null,
        ]);
    }

    public function rules(): array
    {
        return [
            'kode_mapel' => 'required|unique:mapels,kode_mapel',
            '*.kode_mapel' => 'required|unique:mapels,kode_mapel',
        ];
    }
}
