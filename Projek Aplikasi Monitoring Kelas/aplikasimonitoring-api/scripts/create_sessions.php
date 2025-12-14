<?php
require __DIR__ . '/../vendor/autoload.php';
$app = require __DIR__ . '/../bootstrap/app.php';
$kernel = $app->make(Illuminate\Contracts\Console\Kernel::class);
$kernel->bootstrap();

try {
    $dbName = DB::select('select database() as db')[0]->db;
    $r = DB::select("select count(*) as c from information_schema.tables where table_schema = ? and table_name = ?", [$dbName, 'sessions']);
    $count = $r[0]->c ?? 0;
    if ($count > 0) {
        echo "sessions table already exists (count={$count})\n";
        exit(0);
    }

    echo "Creating sessions table...\n";
    DB::statement(<<<'SQL'
CREATE TABLE IF NOT EXISTS sessions (
    id VARCHAR(255) PRIMARY KEY,
    user_id BIGINT UNSIGNED NULL,
    ip_address VARCHAR(45) NULL,
    user_agent TEXT NULL,
    payload LONGTEXT NOT NULL,
    last_activity INT NOT NULL,
    INDEX sessions_user_id_index (user_id),
    INDEX sessions_last_activity_index (last_activity)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
SQL
    );

    echo "sessions table created.\n";
} catch (Exception $e) {
    echo "ERROR: " . $e->getMessage() . "\n";
    exit(1);
}
