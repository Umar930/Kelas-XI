<?php

namespace App\Imports;

use App\Models\JadwalKelas;
use App\Models\Kelas;
use App\Models\Mapel;
use App\Models\Guru;
use App\Models\TahunAjaran;
use Maatwebsite\Excel\Concerns\ToModel;
use Maatwebsite\Excel\Concerns\WithHeadingRow;

class JadwalImport implements ToModel, WithHeadingRow
{
    public function model(array $row)
    {
        // Get kelas by name or id
        $kelas = Kelas::where('nama_kelas', $row['kelas'] ?? '')
            ->orWhere('id', $row['kelas_id'] ?? 0)
            ->first();

        // Get mapel by name or id
        $mapel = Mapel::where('mapel', $row['mapel'] ?? '')
            ->orWhere('id', $row['mapel_id'] ?? 0)
            ->first();

        // Get guru by name or id
        $guru = Guru::where('nama', $row['guru'] ?? '')
            ->orWhere('id', $row['guru_id'] ?? 0)
            ->first();

        // Get active tahun ajaran
        $tahunAjaran = TahunAjaran::where('flag', true)->first();

        if (!$kelas || !$mapel || !$guru) {
            return null;
        }

        return new JadwalKelas([
            'kelas_id' => $kelas->id,
            'mapel_id' => $mapel->id,
            'guru_id' => $guru->id,
            'hari' => $row['hari'],
            'jam_mulai' => $row['jam_mulai'],
            'jam_selesai' => $row['jam_selesai'],
            'tahun_ajaran_id' => $row['tahun_ajaran_id'] ?? $tahunAjaran?->id,
        ]);
    }
}
