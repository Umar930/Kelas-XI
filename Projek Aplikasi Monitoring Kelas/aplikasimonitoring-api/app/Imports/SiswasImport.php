<?php

namespace App\Imports;

use App\Models\Siswa;
use Maatwebsite\Excel\Concerns\ToModel;
use Maatwebsite\Excel\Concerns\WithHeadingRow;
use Maatwebsite\Excel\Concerns\WithValidation;

class SiswasImport implements ToModel, WithHeadingRow, WithValidation
{
    public function model(array $row)
    {
        return new Siswa([
            'nis' => $row['nis'],
            'nama' => $row['nama'] ?? $row['nama_siswa'],
            'jenis_kelamin' => $row['jenis_kelamin'] ?? $row['jk'] ?? 'L',
            'tempat_lahir' => $row['tempat_lahir'] ?? null,
            'tanggal_lahir' => $row['tanggal_lahir'] ?? null,
            'alamat' => $row['alamat'] ?? null,
            'telepon' => $row['telepon'] ?? $row['no_telepon'] ?? null,
            'email' => $row['email'] ?? null,
            'kelas_id' => $row['kelas_id'] ?? null,
            'user_id' => $row['user_id'] ?? null,
            'status' => $row['status'] ?? 'aktif',
        ]);
    }

    public function rules(): array
    {
        return [
            'nis' => 'required|unique:siswas,nis',
            '*.nis' => 'required|unique:siswas,nis',
        ];
    }
}
