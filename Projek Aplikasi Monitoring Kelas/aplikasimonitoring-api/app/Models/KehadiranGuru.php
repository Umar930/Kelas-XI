<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class KehadiranGuru extends Model
{
    use HasFactory;

    protected $table = 'kehadiran_gurus';

    protected $fillable = [
        'jadwal_id',
        'guru_id',
        'kelas_id',
        'mapel_id',
        'tanggal',
        'status',
        'keterangan',
        'input_by_siswa_id',
        'input_by_kurikulum_id',
    ];

    protected $casts = [
        'tanggal' => 'date',
    ];

    /**
     * Relationships
     */
    public function jadwal()
    {
        return $this->belongsTo(JadwalKelas::class, 'jadwal_id');
    }

    public function guru()
    {
        return $this->belongsTo(Guru::class);
    }

    public function kelas()
    {
        return $this->belongsTo(Kelas::class);
    }

    public function mapel()
    {
        return $this->belongsTo(Mapel::class);
    }

    public function inputBySiswa()
    {
        return $this->belongsTo(Siswa::class, 'input_by_siswa_id');
    }

    public function inputByKurikulum()
    {
        return $this->belongsTo(User::class, 'input_by_kurikulum_id');
    }

    public function guruPengganti()
    {
        return $this->hasMany(GuruPengganti::class, 'kehadiran_guru_id');
    }

    /**
     * Scopes
     */
    public function scopeByGuru($query, $guruId)
    {
        return $query->where('guru_id', $guruId);
    }

    public function scopeByKelas($query, $kelasId)
    {
        return $query->where('kelas_id', $kelasId);
    }

    public function scopeByTanggal($query, $tanggal)
    {
        return $query->where('tanggal', $tanggal);
    }

    public function scopeByStatus($query, $status)
    {
        return $query->where('status', $status);
    }

    public function scopeHariIni($query)
    {
        return $query->where('tanggal', now()->toDateString());
    }

    public function scopeTidakHadir($query)
    {
        return $query->whereIn('status', ['tidak_hadir', 'izin', 'sakit']);
    }

    /**
     * Accessor untuk nama guru
     */
    public function getNamaGuruAttribute()
    {
        return $this->guru ? $this->guru->guru : null;
    }

    /**
     * Accessor untuk nama kelas
     */
    public function getNamaKelasAttribute()
    {
        return $this->kelas ? $this->kelas->nama_kelas : null;
    }

    /**
     * Accessor untuk nama mapel
     */
    public function getNamaMapelAttribute()
    {
        return $this->mapel ? $this->mapel->mapel : null;
    }
}
