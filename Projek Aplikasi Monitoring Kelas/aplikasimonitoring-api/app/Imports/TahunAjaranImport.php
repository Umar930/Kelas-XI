<?php

namespace App\Imports;

use App\Models\TahunAjaran;
use Maatwebsite\Excel\Concerns\ToModel;
use Maatwebsite\Excel\Concerns\WithHeadingRow;
use Maatwebsite\Excel\Concerns\WithValidation;

class TahunAjaranImport implements ToModel, WithHeadingRow, WithValidation
{
    public function model(array $row)
    {
        return new TahunAjaran([
            'tahun' => $row['tahun'] ?? $row['tahun_ajaran'],
            'flag' => $row['flag'] ?? $row['aktif'] ?? false,
        ]);
    }

    public function rules(): array
    {
        return [
            'tahun' => 'required|unique:tahun_ajarans,tahun',
            '*.tahun' => 'required|unique:tahun_ajarans,tahun',
        ];
    }
}
