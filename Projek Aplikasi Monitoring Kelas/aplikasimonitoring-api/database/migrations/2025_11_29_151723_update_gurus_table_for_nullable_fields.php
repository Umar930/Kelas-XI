<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;
use Illuminate\Support\Facades\DB;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        // Update existing null values first (handle DB driver differences)
        $driver = DB::getDriverName();
        $concatExpression = $driver === 'sqlite' ? DB::raw("'G' || id") : DB::raw("CONCAT('G', id)");
        DB::table('gurus')
            ->whereNull('kode_guru')
            ->update(['kode_guru' => $concatExpression]);

        // Update kolom 'guru' menjadi 'nama' jika ada
        if (Schema::hasColumn('gurus', 'guru')) {
            Schema::table('gurus', function (Blueprint $table) {
                $table->renameColumn('guru', 'nama');
            });
        }

        // Update kolom 'telepon' menjadi 'no_telepon' jika ada
        if (Schema::hasColumn('gurus', 'telepon')) {
            Schema::table('gurus', function (Blueprint $table) {
                $table->renameColumn('telepon', 'no_telepon');
            });
        }

        // Tambahkan kolom baru jika belum ada
        Schema::table('gurus', function (Blueprint $table) {
            if (!Schema::hasColumn('gurus', 'email')) {
                $table->string('email', 255)->nullable()->unique()->after('nama');
            }
            if (!Schema::hasColumn('gurus', 'mata_pelajaran')) {
                $table->string('mata_pelajaran', 100)->nullable()->after('no_telepon');
            }
            if (!Schema::hasColumn('gurus', 'alamat')) {
                $table->text('alamat')->nullable()->after('mata_pelajaran');
            }
        });

        // Update existing null nama values
        DB::table('gurus')
            ->whereNull('nama')
            ->update(['nama' => 'Tidak ada nama']);

        // Add indexes untuk performa (use safe creation to handle sqlite/mysql differences)
        Schema::table('gurus', function (Blueprint $table) {
            try {
                $table->index('nama');
            } catch (\Exception $e) {
                // Index might already exist or unsupported in SQLite; ignore
            }
            try {
                $table->index('kode_guru');
            } catch (\Exception $e) {
                // ignore
            }
            try {
                $table->index('mata_pelajaran');
            } catch (\Exception $e) {
                // ignore
            }
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('gurus', function (Blueprint $table) {
            // Drop indexes
            $table->dropIndex(['nama']);
            $table->dropIndex(['kode_guru']);
            $table->dropIndex(['mata_pelajaran']);
            
            // Drop new columns
            if (Schema::hasColumn('gurus', 'alamat')) {
                $table->dropColumn('alamat');
            }
            if (Schema::hasColumn('gurus', 'mata_pelajaran')) {
                $table->dropColumn('mata_pelajaran');
            }
            if (Schema::hasColumn('gurus', 'email')) {
                $table->dropColumn('email');
            }
        });

        // Rename columns back
        if (Schema::hasColumn('gurus', 'no_telepon')) {
            Schema::table('gurus', function (Blueprint $table) {
                $table->renameColumn('no_telepon', 'telepon');
            });
        }

        if (Schema::hasColumn('gurus', 'nama')) {
            Schema::table('gurus', function (Blueprint $table) {
                $table->renameColumn('nama', 'guru');
            });
        }
    }

    /**
     * Check if index exists
     */
    private function indexExists($table, $index)
    {
        $indexes = DB::select("SHOW INDEX FROM {$table}");
        foreach ($indexes as $idx) {
            if ($idx->Key_name === $index) {
                return true;
            }
        }
        return false;
    }
};
