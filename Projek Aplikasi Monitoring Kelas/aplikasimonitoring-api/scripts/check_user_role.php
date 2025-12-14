<?php
require __DIR__ . '/../vendor/autoload.php';
$app = require __DIR__ . '/../bootstrap/app.php';
$kernel = $app->make(Illuminate\Contracts\Console\Kernel::class);
$kernel->bootstrap();

use App\Models\User;

$u = User::where('role','kurikulum')->first();
if(!$u) {
    echo "No kurikulum user found\n";
} else {
    echo "user: id={$u->id} name={$u->name} email={$u->email}\n";
}
