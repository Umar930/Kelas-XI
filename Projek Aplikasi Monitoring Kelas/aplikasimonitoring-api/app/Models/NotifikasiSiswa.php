<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class NotifikasiSiswa extends Model
{
    use HasFactory;

    protected $table = 'notifikasi_siswa';

    protected $fillable = [
        'kelas_id',
        'guru_id',
        'mapel_id',
        'tanggal',
        'tipe',
        'pesan',
        'guru_pengganti_id',
        'created_by_kurikulum_id',
        'is_read',
    ];

    protected $casts = [
        'tanggal' => 'date',
        'is_read' => 'boolean',
    ];

    /**
     * Relationships
     */
    public function kelas()
    {
        return $this->belongsTo(Kelas::class);
    }

    public function guru()
    {
        return $this->belongsTo(Guru::class);
    }

    public function mapel()
    {
        return $this->belongsTo(Mapel::class);
    }

    public function guruPengganti()
    {
        return $this->belongsTo(Guru::class, 'guru_pengganti_id');
    }

    public function createdByKurikulum()
    {
        return $this->belongsTo(User::class, 'created_by_kurikulum_id');
    }

    /**
     * Scopes
     */
    public function scopeByKelas($query, $kelasId)
    {
        return $query->where('kelas_id', $kelasId);
    }

    public function scopeByTipe($query, $tipe)
    {
        return $query->where('tipe', $tipe);
    }

    public function scopeUnread($query)
    {
        return $query->where('is_read', false);
    }

    public function scopeRead($query)
    {
        return $query->where('is_read', true);
    }

    public function scopeHariIni($query)
    {
        return $query->where('tanggal', now()->toDateString());
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

    /**
     * Accessor untuk nama guru pengganti
     */
    public function getNamaGuruPenggantiAttribute()
    {
        return $this->guruPengganti ? $this->guruPengganti->guru : null;
    }
}
