<?php
require __DIR__ . '/../vendor/autoload.php';
$app = require __DIR__ . '/../bootstrap/app.php';
$kernel = $app->make(Illuminate\Contracts\Console\Kernel::class);
$kernel->bootstrap();

use App\Models\User;
use Illuminate\Support\Facades\Hash;

// We will look for users where role contains 'kurikulum' or name/email contains 'kurikulum'
$users = User::where('role', 'kurikulum')
    ->orWhere('email', 'like', '%kurikulum%')
    ->orWhere('name', 'like', '%kurikulum%')
    ->get();

if ($users->isEmpty()) {
    echo "No users related to 'kurikulum' found in users table\n";
    exit(0);
}

echo "Found " . $users->count() . " user(s)\n";
foreach ($users as $u) {
    echo "-----\n";
    echo "ID: {$u->id}\n";
    echo "Name: {$u->name}\n";
    echo "Email: {$u->email}\n";
    echo "Role: {$u->role}\n";
    // Check password match
    $passwords = ['password', 'Password', '123456', '12345678'];
    $pwMatch = false;
    foreach ($passwords as $pw) {
        if (Hash::check($pw, $u->password)) {
            echo "Password match found for: '{$pw}'\n";
            $pwMatch = true;
            break;
        }
    }
    if (!$pwMatch) {
        echo "No password match found for 'password' (or common defaults).\n";
    }
}
