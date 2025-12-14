<?php

require __DIR__.'/vendor/autoload.php';

$app = require_once __DIR__.'/bootstrap/app.php';
$kernel = $app->make(Illuminate\Contracts\Console\Kernel::class);
$kernel->bootstrap();

use Illuminate\Support\Facades\DB;

echo "=== Deleting Default Siswa Record ===\n\n";

// Delete siswa record for user siswa@example.com (user_id 21)
$deleted = DB::table('siswas')->where('user_id', 21)->delete();

if ($deleted) {
    echo "✅ Siswa record untuk user_id 21 (siswa@example.com) berhasil dihapus\n";
} else {
    echo "ℹ️  Tidak ada siswa record untuk user_id 21\n";
}

echo "\n⚠️  CATATAN:\n";
echo "   - siswa@example.com hanya untuk LOGIN dashboard\n";
echo "   - Untuk ABSENSI, pilih nama & email siswa yang sebenarnya\n";
echo "   - Contoh: Fajar Rahman (fajar.rahman@example.com)\n";
