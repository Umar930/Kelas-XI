<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;

class GuruMengajar extends Model
{
    use HasFactory;

    protected $table = 'guru_mengajar';

    protected $fillable = [
        'tahun_ajaran_id',
        'guru_id',
        'mapel_id',
        'kelas_id',
        'jam_per_minggu',
        'keterangan',
    ];

    /**
     * Relasi ke TahunAjaran
     */
    public function tahunAjaran()
    {
        return $this->belongsTo(TahunAjaran::class);
    }

    /**
     * Relasi ke Guru
     */
    public function guru()
    {
        return $this->belongsTo(Guru::class);
    }

    /**
     * Relasi ke Mapel (Mata Pelajaran)
     */
    public function mapel()
    {
        return $this->belongsTo(Mapel::class);
    }

    /**
     * Relasi ke Kelas
     */
    public function kelas()
    {
        return $this->belongsTo(Kelas::class);
    }
}
