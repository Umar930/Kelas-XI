<?php

namespace App\Imports;

use App\Models\GuruMengajar;
use App\Models\Kelas;
use App\Models\Mapel;
use App\Models\Guru;
use App\Models\TahunAjaran;
use Maatwebsite\Excel\Concerns\ToModel;
use Maatwebsite\Excel\Concerns\WithHeadingRow;

class GuruMengajarImport implements ToModel, WithHeadingRow
{
    public function model(array $row)
    {
        $kelas = Kelas::where('nama_kelas', $row['kelas'] ?? '')
            ->orWhere('id', $row['kelas_id'] ?? 0)
            ->first();

        $mapel = Mapel::where('mapel', $row['mapel'] ?? '')
            ->orWhere('id', $row['mapel_id'] ?? 0)
            ->first();

        $guru = Guru::where('nama', $row['guru'] ?? '')
            ->orWhere('id', $row['guru_id'] ?? 0)
            ->first();

        $tahunAjaran = TahunAjaran::where('flag', true)->first();

        if (!$kelas || !$mapel || !$guru) {
            return null;
        }

        return new GuruMengajar([
            'tahun_ajaran_id' => $row['tahun_ajaran_id'] ?? $tahunAjaran?->id,
            'guru_id' => $guru->id,
            'mapel_id' => $mapel->id,
            'kelas_id' => $kelas->id,
            'jam_per_minggu' => $row['jam_per_minggu'] ?? null,
            'keterangan' => $row['keterangan'] ?? null,
        ]);
    }
}
