<?php

/**
 * API Testing Script
 * Run: php test-api.php
 */

class APITester {
    private $baseUrl = 'http://127.0.0.1:8000/api';
    private $token = null;
    private $testResults = [];
    
    public function __construct() {
        echo "🚀 Starting API Tests...\n";
        echo "Base URL: {$this->baseUrl}\n";
        echo str_repeat("=", 80) . "\n\n";
    }
    
    private function request($method, $endpoint, $data = null, $requireAuth = false) {
        $url = $this->baseUrl . $endpoint;
        
        $headers = [
            'Content-Type: application/json',
            'Accept: application/json'
        ];
        
        if ($requireAuth && $this->token) {
            $headers[] = 'Authorization: Bearer ' . $this->token;
        }
        
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, $method);
        
        if ($data) {
            curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
        }
        
        $response = curl_exec($ch);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        $error = curl_error($ch);
        curl_close($ch);
        
        if ($error) {
            return ['error' => $error, 'http_code' => 0];
        }
        
        return [
            'http_code' => $httpCode,
            'body' => json_decode($response, true),
            'raw' => $response
        ];
    }
    
    private function logTest($name, $success, $message = '') {
        $status = $success ? '✅ PASS' : '❌ FAIL';
        echo "{$status} - {$name}\n";
        if ($message) {
            echo "   └─ {$message}\n";
        }
        $this->testResults[] = ['name' => $name, 'success' => $success, 'message' => $message];
    }
    
    // 1. AUTHENTICATION TESTS
    public function testAuthentication() {
        echo "\n📝 1. AUTHENTICATION TESTS\n";
        echo str_repeat("-", 80) . "\n";
        
        // Test Login
        $response = $this->request('POST', '/login', [
            'email' => 'admin@example.com',
            'password' => 'password'
        ]);
        
        if ($response['http_code'] === 200 && isset($response['body']['data']['token'])) {
            $this->token = $response['body']['data']['token'];
            $this->logTest('Login', true, "Token: " . substr($this->token, 0, 20) . "...");
        } else {
            $this->logTest('Login', false, "HTTP {$response['http_code']}");
            return false;
        }
        
        // Test Check Auth
        $response = $this->request('GET', '/check', null, true);
        $this->logTest('Check Auth', $response['http_code'] === 200, "User: " . ($response['body']['data']['name'] ?? 'N/A'));
        
        return true;
    }
    
    // 2. GURU TESTS
    public function testGuru() {
        echo "\n👨‍🏫 2. GURU CRUD TESTS\n";
        echo str_repeat("-", 80) . "\n";
        
        // List Guru with Pagination
        $response = $this->request('GET', '/gurus?per_page=5', null, true);
        $total = isset($response['body']['data']['total']) ? $response['body']['data']['total'] 
                : (isset($response['body']['data']) && is_array($response['body']['data']) ? count($response['body']['data']) : 0);
        $this->logTest('List Guru (Pagination)', 
            $response['http_code'] === 200,
            "Total: " . $total
        );
        
        // Search Guru
        $response = $this->request('GET', '/gurus?search=guru', null, true);
        $this->logTest('Search Guru', 
            $response['http_code'] === 200,
            "Results: " . count($response['body']['data']['data'] ?? [])
        );
        
        // Create Guru
        $newGuru = [
            'kode_guru' => 'TEST001',
            'guru' => 'Test Guru API',
            'telepon' => '081234567890'
        ];
        
        $response = $this->request('POST', '/gurus', $newGuru, true);
        $createdId = null;
        if ($response['http_code'] === 201 && isset($response['body']['data']['id'])) {
            $createdId = $response['body']['data']['id'];
            $this->logTest('Create Guru', true, "ID: {$createdId}");
        } else {
            $this->logTest('Create Guru', false, "HTTP {$response['http_code']}");
        }
        
        // Update Guru
        if ($createdId) {
            $response = $this->request('PUT', "/gurus/{$createdId}", [
                'kode_guru' => 'TEST001',
                'guru' => 'Test Guru Updated',
                'telepon' => '089999999999'
            ], true);
            $this->logTest('Update Guru', $response['http_code'] === 200);
            
            // Show Guru
            $response = $this->request('GET', "/gurus/{$createdId}", null, true);
            $this->logTest('Show Guru', 
                $response['http_code'] === 200,
                "Nama: " . ($response['body']['data']['guru'] ?? 'N/A')
            );
            
            // Delete Guru
            $response = $this->request('DELETE', "/gurus/{$createdId}", null, true);
            $this->logTest('Delete Guru', $response['http_code'] === 200);
        }
    }
    
    // 3. MAPEL TESTS
    public function testMapel() {
        echo "\n📚 3. MAPEL CRUD TESTS\n";
        echo str_repeat("-", 80) . "\n";
        
        // List Mapel
        $response = $this->request('GET', '/mapels', null, true);
        $this->logTest('List Mapel', 
            $response['http_code'] === 200,
            "Total: " . count($response['body']['data'] ?? [])
        );
        
        // Create Mapel
        $newMapel = [
            'kode_mapel' => 'TST' . time(),
            'mapel' => 'Test Mapel'
        ];
        
        $response = $this->request('POST', '/mapels', $newMapel, true);
        $createdId = null;
        if ($response['http_code'] === 201 && isset($response['body']['data']['id'])) {
            $createdId = $response['body']['data']['id'];
            $kodeMapel = $response['body']['data']['kode_mapel'];
            $this->logTest('Create Mapel', true, "ID: {$createdId}");
            
            // Update Mapel
            $response = $this->request('PUT', "/mapels/{$createdId}", [
                'kode_mapel' => $kodeMapel,
                'mapel' => 'Test Mapel Updated'
            ], true);
            $this->logTest('Update Mapel', $response['http_code'] === 200);
            
            // Delete Mapel
            $response = $this->request('DELETE', "/mapels/{$createdId}", null, true);
            $this->logTest('Delete Mapel', $response['http_code'] === 200);
        } else {
            $this->logTest('Create Mapel', false, "HTTP {$response['http_code']}");
        }
    }
    
    // 4. TAHUN AJARAN TESTS
    public function testTahunAjaran() {
        echo "\n📅 4. TAHUN AJARAN CRUD TESTS\n";
        echo str_repeat("-", 80) . "\n";
        
        // List Tahun Ajaran
        $response = $this->request('GET', '/tahun-ajarans', null, true);
        $this->logTest('List Tahun Ajaran', 
            $response['http_code'] === 200,
            "Total: " . count($response['body']['data'] ?? [])
        );
        
        // Create Tahun Ajaran
        $newTA = [
            'tahun' => '2025/2026 Test ' . time(),
            'flag' => false
        ];
        
        $response = $this->request('POST', '/tahun-ajarans', $newTA, true);
        $createdId = null;
        if ($response['http_code'] === 201 && isset($response['body']['data']['id'])) {
            $createdId = $response['body']['data']['id'];
            $this->logTest('Create Tahun Ajaran', true, "ID: {$createdId}");
            
            // Delete Tahun Ajaran
            $response = $this->request('DELETE', "/tahun-ajarans/{$createdId}", null, true);
            $this->logTest('Delete Tahun Ajaran', $response['http_code'] === 200);
        } else {
            $this->logTest('Create Tahun Ajaran', false, "HTTP {$response['http_code']}");
        }
    }
    
    // 5. KELAS TESTS
    public function testKelas() {
        echo "\n🏫 5. KELAS CRUD TESTS\n";
        echo str_repeat("-", 80) . "\n";
        
        // List Kelas with Pagination
        $response = $this->request('GET', '/kelas?per_page=5', null, true);
        $total = isset($response['body']['data']['total']) ? $response['body']['data']['total'] 
                : (isset($response['body']['data']) && is_array($response['body']['data']) ? count($response['body']['data']) : 0);
        $this->logTest('List Kelas (Pagination)', 
            $response['http_code'] === 200,
            "Total: " . $total
        );
        
        // Search Kelas
        $response = $this->request('GET', '/kelas?search=RPL', null, true);
        $this->logTest('Search Kelas by RPL', 
            $response['http_code'] === 200,
            "Results: " . count($response['body']['data']['data'] ?? [])
        );
        
        // Filter Kelas by Tingkat
        $response = $this->request('GET', '/kelas?tingkat=XI', null, true);
        $this->logTest('Filter Kelas by Tingkat XI', 
            $response['http_code'] === 200,
            "Results: " . count($response['body']['data']['data'] ?? [])
        );
        
        // Filter Kelas by Jurusan
        $response = $this->request('GET', '/kelas?jurusan=RPL', null, true);
        $this->logTest('Filter Kelas by Jurusan RPL', 
            $response['http_code'] === 200,
            "Results: " . count($response['body']['data']['data'] ?? [])
        );
        
        // Create Kelas
        $newKelas = [
            'kode_kelas' => 'TEST-1',
            'nama_kelas' => 'Test Kelas',
            'guru_id' => 1,
            'tingkat' => 'X',
            'jurusan' => 'RPL',
            'kapasitas' => 30
        ];
        
        $response = $this->request('POST', '/kelas', $newKelas, true);
        $createdId = null;
        if ($response['http_code'] === 201 && isset($response['body']['data']['id'])) {
            $createdId = $response['body']['data']['id'];
            $this->logTest('Create Kelas', true, "ID: {$createdId}");
            
            // Show Kelas with relationships
            $response = $this->request('GET', "/kelas/{$createdId}", null, true);
            $this->logTest('Show Kelas with Guru', 
                $response['http_code'] === 200 && isset($response['body']['data']['guru']),
                "Guru: " . ($response['body']['data']['guru']['nama'] ?? 'N/A')
            );
            
            // Delete Kelas
            $response = $this->request('DELETE', "/kelas/{$createdId}", null, true);
            $this->logTest('Delete Kelas', $response['http_code'] === 200);
        } else {
            $this->logTest('Create Kelas', false, "HTTP {$response['http_code']}");
        }
    }
    
    // 6. SISWA TESTS
    public function testSiswa() {
        echo "\n👨‍🎓 6. SISWA CRUD TESTS\n";
        echo str_repeat("-", 80) . "\n";
        
        // List Siswa with Pagination
        $response = $this->request('GET', '/siswas?per_page=5', null, true);
        $total = isset($response['body']['data']['total']) ? $response['body']['data']['total'] 
                : (isset($response['body']['data']) && is_array($response['body']['data']) ? count($response['body']['data']) : 0);
        $this->logTest('List Siswa (Pagination)', 
            $response['http_code'] === 200,
            "Total: " . $total
        );
        
        // Search Siswa
        $response = $this->request('GET', '/siswas?search=ahmad', null, true);
        $this->logTest('Search Siswa by Name', 
            $response['http_code'] === 200,
            "Results: " . count($response['body']['data']['data'] ?? [])
        );
        
        // Filter Siswa by Kelas
        $response = $this->request('GET', '/siswas?kelas_id=3', null, true);
        $this->logTest('Filter Siswa by Kelas', 
            $response['http_code'] === 200,
            "Results: " . count($response['body']['data']['data'] ?? [])
        );
        
        // Filter Siswa by Status
        $response = $this->request('GET', '/siswas?status=aktif', null, true);
        $this->logTest('Filter Siswa by Status Aktif', 
            $response['http_code'] === 200,
            "Results: " . count($response['body']['data']['data'] ?? [])
        );
        
        // Create Siswa
        $newSiswa = [
            'nis' => '9999999',
            'nama' => 'Test Siswa API',
            'jenis_kelamin' => 'L',
            'tempat_lahir' => 'Jakarta',
            'tanggal_lahir' => '2005-01-01',
            'alamat' => 'Jl. Test',
            'telepon' => '081234567890',
            'email' => 'testsiswa@example.com',
            'kelas_id' => 1,
            'status' => 'aktif'
        ];
        
        $response = $this->request('POST', '/siswas', $newSiswa, true);
        $createdId = null;
        if ($response['http_code'] === 201 && isset($response['body']['data']['id'])) {
            $createdId = $response['body']['data']['id'];
            $this->logTest('Create Siswa', true, "ID: {$createdId}");
            
            // Show Siswa with relationships
            $response = $this->request('GET', "/siswas/{$createdId}", null, true);
            $this->logTest('Show Siswa with Kelas', 
                $response['http_code'] === 200 && isset($response['body']['data']['kelas']),
                "Kelas: " . ($response['body']['data']['kelas']['nama_kelas'] ?? 'N/A')
            );
            
            // Delete Siswa
            $response = $this->request('DELETE', "/siswas/{$createdId}", null, true);
            $this->logTest('Delete Siswa', $response['http_code'] === 200);
        } else {
            $this->logTest('Create Siswa', false, "HTTP {$response['http_code']}");
        }
    }
    
    // 7. ABSENSI SISWA TESTS
    public function testAbsensiSiswa() {
        echo "\n📝 7. ABSENSI SISWA CRUD TESTS\n";
        echo str_repeat("-", 80) . "\n";
        
        // List Absensi Siswa with Pagination
        $response = $this->request('GET', '/absensi-siswas?per_page=5', null, true);
        $total = isset($response['body']['data']['total']) ? $response['body']['data']['total'] 
                : (isset($response['body']['data']) && is_array($response['body']['data']) ? count($response['body']['data']) : 0);
        $this->logTest('List Absensi Siswa (Pagination)', 
            $response['http_code'] === 200,
            "Total: " . $total
        );
        
        // Filter by Date Range
        $response = $this->request('GET', '/absensi-siswas?tanggal_from=2025-11-01&tanggal_to=2025-11-30', null, true);
        $this->logTest('Filter Absensi Siswa by Date Range', 
            $response['http_code'] === 200,
            "Results: " . count($response['body']['data']['data'] ?? [])
        );
        
        // Filter by Status
        $response = $this->request('GET', '/absensi-siswas?status=hadir', null, true);
        $this->logTest('Filter Absensi Siswa by Status Hadir', 
            $response['http_code'] === 200,
            "Results: " . count($response['body']['data']['data'] ?? [])
        );
        
        // Create Absensi Siswa
        $newAbsensi = [
            'siswa_id' => 1,
            'kelas_id' => 3,
            'mapel_id' => 1,
            'tanggal' => date('Y-m-d'),
            'status' => 'hadir',
            'keterangan' => 'Test absensi via API',
            'jam_masuk' => '07:00',
            'jam_keluar' => '14:00'
        ];
        
        $response = $this->request('POST', '/absensi-siswas', $newAbsensi, true);
        $createdId = null;
        if ($response['http_code'] === 201 && isset($response['body']['data']['id'])) {
            $createdId = $response['body']['data']['id'];
            $this->logTest('Create Absensi Siswa', true, "ID: {$createdId}");
            
            // Show with relationships
            $response = $this->request('GET', "/absensi-siswas/{$createdId}", null, true);
            $this->logTest('Show Absensi Siswa with Relations', 
                $response['http_code'] === 200 && isset($response['body']['data']['siswa']),
                "Siswa: " . ($response['body']['data']['siswa']['nama'] ?? 'N/A')
            );
            
            // Delete Absensi Siswa
            $response = $this->request('DELETE', "/absensi-siswas/{$createdId}", null, true);
            $this->logTest('Delete Absensi Siswa', $response['http_code'] === 200);
        } else {
            $this->logTest('Create Absensi Siswa', false, "HTTP {$response['http_code']}");
        }
    }
    
    // 8. ABSENSI GURU TESTS
    public function testAbsensiGuru() {
        echo "\n📋 8. ABSENSI GURU CRUD TESTS\n";
        echo str_repeat("-", 80) . "\n";
        
        // List Absensi Guru with Pagination
        $response = $this->request('GET', '/absensi-gurus?per_page=5', null, true);
        $total = isset($response['body']['data']['total']) ? $response['body']['data']['total'] 
                : (isset($response['body']['data']) && is_array($response['body']['data']) ? count($response['body']['data']) : 0);
        $this->logTest('List Absensi Guru (Pagination)', 
            $response['http_code'] === 200,
            "Total: " . $total
        );
        
        // Filter by Date Range
        $response = $this->request('GET', '/absensi-gurus?tanggal_from=2025-11-01&tanggal_to=2025-11-30', null, true);
        $this->logTest('Filter Absensi Guru by Date Range', 
            $response['http_code'] === 200,
            "Results: " . count($response['body']['data']['data'] ?? [])
        );
        
        // Filter by Guru
        $response = $this->request('GET', '/absensi-gurus?guru_id=1', null, true);
        $this->logTest('Filter Absensi Guru by Guru ID', 
            $response['http_code'] === 200,
            "Results: " . count($response['body']['data']['data'] ?? [])
        );
        
        // Create Absensi Guru
        $newAbsensi = [
            'guru_id' => 1,
            'mapel_id' => 1,
            'kelas_id' => 3,
            'tanggal' => date('Y-m-d'),
            'status' => 'hadir',
            'keterangan' => 'Test absensi guru via API',
            'jam_masuk' => '07:00',
            'jam_keluar' => '14:00'
        ];
        
        $response = $this->request('POST', '/absensi-gurus', $newAbsensi, true);
        $createdId = null;
        if ($response['http_code'] === 201 && isset($response['body']['data']['id'])) {
            $createdId = $response['body']['data']['id'];
            $this->logTest('Create Absensi Guru', true, "ID: {$createdId}");
            
            // Show with relationships
            $response = $this->request('GET', "/absensi-gurus/{$createdId}", null, true);
            $this->logTest('Show Absensi Guru with Relations', 
                $response['http_code'] === 200 && isset($response['body']['data']['guru']),
                "Guru: " . ($response['body']['data']['guru']['nama'] ?? 'N/A')
            );
            
            // Delete Absensi Guru
            $response = $this->request('DELETE', "/absensi-gurus/{$createdId}", null, true);
            $this->logTest('Delete Absensi Guru', $response['http_code'] === 200);
        } else {
            $this->logTest('Create Absensi Guru', false, "HTTP {$response['http_code']}");
        }
    }
    
    // 9. LOGOUT TEST
    public function testLogout() {
        echo "\n🚪 9. LOGOUT TEST\n";
        echo str_repeat("-", 80) . "\n";
        
        $response = $this->request('POST', '/logout', null, true);
        $this->logTest('Logout', $response['http_code'] === 200);
    }
    
    public function printSummary() {
        echo "\n" . str_repeat("=", 80) . "\n";
        echo "📊 TEST SUMMARY\n";
        echo str_repeat("=", 80) . "\n";
        
        $passed = count(array_filter($this->testResults, fn($r) => $r['success']));
        $total = count($this->testResults);
        $failed = $total - $passed;
        
        echo "Total Tests: {$total}\n";
        echo "✅ Passed: {$passed}\n";
        echo "❌ Failed: {$failed}\n";
        
        $percentage = $total > 0 ? round(($passed / $total) * 100, 2) : 0;
        echo "Success Rate: {$percentage}%\n";
        
        if ($failed > 0) {
            echo "\n❌ Failed Tests:\n";
            foreach ($this->testResults as $result) {
                if (!$result['success']) {
                    echo "  - {$result['name']}: {$result['message']}\n";
                }
            }
        }
        
        echo str_repeat("=", 80) . "\n";
    }
    
    public function runAll() {
        if ($this->testAuthentication()) {
            $this->testGuru();
            $this->testMapel();
            $this->testTahunAjaran();
            $this->testKelas();
            $this->testSiswa();
            $this->testAbsensiSiswa();
            $this->testAbsensiGuru();
            $this->testLogout();
        } else {
            echo "❌ Authentication failed. Cannot proceed with other tests.\n";
        }
        
        $this->printSummary();
    }
}

// Check if server is running
$ch = curl_init('http://127.0.0.1:8000');
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_NOBODY, true);
curl_setopt($ch, CURLOPT_TIMEOUT, 2);
curl_exec($ch);
$httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
curl_close($ch);

if ($httpCode === 0) {
    echo "❌ Error: Server is not running!\n";
    echo "Please start the server with: php artisan serve\n";
    exit(1);
}

// Run tests
$tester = new APITester();
$tester->runAll();
