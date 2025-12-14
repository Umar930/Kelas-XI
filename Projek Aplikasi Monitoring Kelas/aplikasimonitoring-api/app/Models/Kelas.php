<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Kelas extends Model
{
    use HasFactory;

    protected $table = 'kelas';

    protected $fillable = [
        'kode_kelas',
        'nama_kelas',
        'guru_id',
        'tingkat',
        'jurusan',
        'kapasitas',
    ];

    /**
     * Relasi ke Guru (Wali Kelas)
     */
    public function guru()
    {
        return $this->belongsTo(Guru::class);
    }

    /**
     * Relasi ke Tahun Ajaran
     */
    public function tahunAjaran()
    {
        return $this->belongsTo(TahunAjaran::class);
    }

    /**
     * Relasi ke Siswa
     */
    public function siswas()
    {
        return $this->hasMany(Siswa::class);
    }

    /**
     * Relasi ke Absensi Siswa
     */
    public function absensiSiswas()
    {
        return $this->hasMany(AbsensiSiswa::class);
    }

    /**
     * Relasi ke Absensi Guru
     */
    public function absensiGurus()
    {
        return $this->hasMany(AbsensiGuru::class);
    }
}
