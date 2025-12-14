<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Support\Facades\Schema;

class Guru extends Model
{
    use HasFactory;

    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'gurus';

    /**
     * The attributes that are mass assignable.
     *
     * @var array<int, string>
     */
    protected $fillable = [
        'kode_guru',
        'guru',
        'nama',
        'telepon', // logical attribute, maps to physical 'telepon' or 'no_telepon'
        'no_telepon',
    ];

    /**
     * The attributes that should be cast.
     *
     * @var array
     */
    protected $casts = [
        'created_at' => 'datetime',
        'updated_at' => 'datetime',
    ];

    /**
     * Accessor untuk guru - ensure tidak null
     */
    public function getGuruAttribute($value)
    {
        // Support legacy column name 'guru' and new column 'nama'
        if (!empty($value)) {
            return $value;
        }

        if (array_key_exists('nama', $this->attributes) && !empty($this->attributes['nama'])) {
            return $this->attributes['nama'];
        }

        return 'Tidak ada nama';
    }

    /**
     * Scope to order by the correct name column (supports legacy 'guru' and new 'nama')
     */
    public function scopeOrderByName($query, $direction = 'asc')
    {
        $column = 'id';
        if (Schema::hasColumn($this->getTable(), 'guru')) {
            $column = 'guru';
        } elseif (Schema::hasColumn($this->getTable(), 'nama')) {
            $column = 'nama';
        }

        return $query->orderBy($column, $direction);
    }

    /**
     * Accessor untuk kode_guru - ensure tidak null
     */
    public function getKodeGuruAttribute($value)
    {
        return $value ?? '';
    }

    /**
     * Telepon accessor that supports both legacy 'telepon' and 'no_telepon' columns
     */
    public function getTeleponAttribute($value)
    {
        if (!empty($value)) {
            return $value;
        }

        if (array_key_exists('no_telepon', $this->attributes) && !empty($this->attributes['no_telepon'])) {
            return $this->attributes['no_telepon'];
        }

        return null;
    }

    /**
     * Telepon mutator - write to available column
     */
    public function setTeleponAttribute($value)
    {
        // If table has physical column 'telepon', use it; otherwise set 'no_telepon'
        if (\Schema::hasColumn($this->getTable(), 'telepon')) {
            $this->attributes['telepon'] = $value;
        } else {
            $this->attributes['no_telepon'] = $value;
        }
    }

    /**
     * Scope untuk search
     */
    public function scopeSearch($query, $keyword)
    {
        return $query->where(function($q) use ($keyword) {
            $q->where('guru', 'like', "%{$keyword}%")
              ->orWhere('kode_guru', 'like', "%{$keyword}%")
              ->orWhere('telepon', 'like', "%{$keyword}%");
        });
    }
}
