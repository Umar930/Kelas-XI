<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class GuruPengganti extends Model
{
    use HasFactory;

    protected $table = 'guru_penggantis';

    protected $fillable = [
        'kehadiran_guru_id',
        'guru_id',  // guru pengganti yang dipilih
        'guru_pengganti_id', // alias untuk guru_id
        'jadwal_id',
        'kelas_id',
        'mapel_id',
        'tanggal',
        'status',
        'keterangan',
        'requested_by_siswa_id',
        'approved_by_user_id',
        'approved_by',
        'approved_at',
        'assigned_by_kurikulum_id',
    ];

    protected $casts = [
        'tanggal' => 'date',
        'approved_at' => 'datetime',
    ];

    /**
     * Relationships
     */
    public function kehadiranGuru()
    {
        return $this->belongsTo(KehadiranGuru::class, 'kehadiran_guru_id');
    }

    public function guru()
    {
        return $this->belongsTo(Guru::class, 'guru_id');
    }

    // Alias: Guru pengganti yang dipilih
    public function guruPengganti()
    {
        return $this->belongsTo(Guru::class, 'guru_pengganti_id');
    }

    public function jadwal()
    {
        return $this->belongsTo(JadwalKelas::class, 'jadwal_id');
    }

    public function kelas()
    {
        return $this->belongsTo(Kelas::class);
    }

    public function mapel()
    {
        return $this->belongsTo(Mapel::class);
    }

    public function requestedBySiswa()
    {
        return $this->belongsTo(Siswa::class, 'requested_by_siswa_id');
    }

    public function approvedByUser()
    {
        return $this->belongsTo(User::class, 'approved_by_user_id');
    }

    public function assignedByKurikulum()
    {
        return $this->belongsTo(User::class, 'assigned_by_kurikulum_id');
    }

    /**
     * Scopes
     */
    public function scopePending($query)
    {
        return $query->where('status', 'pending');
    }

    public function scopeApproved($query)
    {
        return $query->where('status', 'aktif');
    }

    public function scopeRejected($query)
    {
        return $query->where('status', 'ditolak');
    }

    public function scopeByKelas($query, $kelasId)
    {
        return $query->where('kelas_id', $kelasId);
    }

    public function scopeByTanggal($query, $tanggal)
    {
        return $query->where('tanggal', $tanggal);
    }
}
