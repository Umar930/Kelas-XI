<?php

namespace App\Imports;

use App\Models\GuruPengganti;
use App\Models\Kelas;
use App\Models\Mapel;
use App\Models\Guru;
use Maatwebsite\Excel\Concerns\ToModel;
use Maatwebsite\Excel\Concerns\WithHeadingRow;

class GuruPenggantiImport implements ToModel, WithHeadingRow
{
    public function model(array $row)
    {
        $kelas = Kelas::where('nama_kelas', $row['kelas'] ?? '')
            ->orWhere('id', $row['kelas_id'] ?? 0)
            ->first();

        $mapel = Mapel::where('mapel', $row['mapel'] ?? '')
            ->orWhere('id', $row['mapel_id'] ?? 0)
            ->first();

        $guruPengganti = Guru::where('nama', $row['guru_pengganti'] ?? '')
            ->orWhere('id', $row['guru_pengganti_id'] ?? 0)
            ->first();

        if (!$kelas || !$mapel || !$guruPengganti) {
            return null;
        }

        return new GuruPengganti([
            'kehadiran_guru_id' => $row['kehadiran_guru_id'] ?? null,
            'guru_pengganti_id' => $guruPengganti->id,
            'kelas_id' => $kelas->id,
            'mapel_id' => $mapel->id,
            'tanggal' => $row['tanggal'],
            'jam_mulai' => $row['jam_mulai'] ?? null,
            'jam_selesai' => $row['jam_selesai'] ?? null,
            'status' => $row['status'] ?? 'pending',
            'catatan' => $row['catatan'] ?? null,
        ]);
    }
}
