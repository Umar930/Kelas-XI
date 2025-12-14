<?php

require __DIR__.'/vendor/autoload.php';

$app = require_once __DIR__.'/bootstrap/app.php';
$kernel = $app->make(Illuminate\Contracts\Console\Kernel::class);
$kernel->bootstrap();

use Illuminate\Support\Facades\DB;

echo "=== CHECKING USERS TABLE ===\n\n";

$users = DB::table('users')
    ->select('id', 'email', 'name', 'role')
    ->orderBy('role')
    ->orderBy('email')
    ->get();

echo "Total users: " . $users->count() . "\n\n";

foreach ($users as $user) {
    echo sprintf(
        "ID: %-3s | Email: %-35s | Name: %-20s | Role: %s\n",
        $user->id,
        $user->email,
        $user->name,
        $user->role
    );
}

echo "\n=== SEARCHING FOR siswa@example.com ===\n";
$siswaUser = DB::table('users')->where('email', 'siswa@example.com')->first();

if ($siswaUser) {
    echo "✅ FOUND!\n";
    echo "ID: {$siswaUser->id}\n";
    echo "Email: {$siswaUser->email}\n";
    echo "Name: {$siswaUser->name}\n";
    echo "Role: {$siswaUser->role}\n";
} else {
    echo "❌ NOT FOUND! Email siswa@example.com tidak ada di database.\n";
}

echo "\n=== USERS WITH ROLE 'siswa' ===\n";
$siswaUsers = DB::table('users')->where('role', 'siswa')->get();
echo "Total: {$siswaUsers->count()}\n\n";

foreach ($siswaUsers as $user) {
    echo "- {$user->email} | {$user->name}\n";
}
