package com.umar.aplikasimonitoringkelas

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import com.umar.aplikasimonitoringkelas.ui.theme.AplikasiMonitoringKelasTheme

class AdminActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AplikasiMonitoringKelasTheme {
                AdminScreen()
            }
        }
    }
}

@Composable
fun AdminScreen() {
    var selectedItem by remember { mutableStateOf("entryUser") }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedItem == "entryUser",
                    onClick = { selectedItem = "entryUser" },
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Entry User") },
                    label = { Text("Entry User") }
                )
                NavigationBarItem(
                    selected = selectedItem == "entryJadwal",
                    onClick = { selectedItem = "entryJadwal" },
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Entry Jadwal") },
                    label = { Text("Entry Jadwal") }
                )
                NavigationBarItem(
                    selected = selectedItem == "listUser",
                    onClick = { selectedItem = "listUser" },
                    icon = { Icon(Icons.Default.List, contentDescription = "List User") },
                    label = { Text("List User") }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedItem) {
                "entryUser" -> EntryUserPage()
                "entryJadwal" -> EntryJadwalPage()
                "listUser" -> ListUserPage()
            }
        }
    }
}

//////////////////////////////
// 🧩 PAGE 1: Entry User
//////////////////////////////
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryUserPage() {
    // State untuk input
    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Siswa") }
    var expandedRole by remember { mutableStateOf(false) }

    // State untuk list user
    var userList by remember { mutableStateOf(listOf<UserData>()) }

    val roleList = listOf("Siswa", "Kurikulum", "Kepala Sekolah", "Admin")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "👤 Entry User Baru",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Input Nama
        OutlinedTextField(
            value = nama,
            onValueChange = { nama = it },
            label = { Text("Nama") },
            placeholder = { Text("Masukkan nama") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Input Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            placeholder = { Text("Masukkan email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Spinner Role
        ExposedDropdownMenuBox(
            expanded = expandedRole,
            onExpandedChange = { expandedRole = !expandedRole }
        ) {
            OutlinedTextField(
                value = selectedRole,
                onValueChange = {},
                readOnly = true,
                label = { Text("Pilih Role") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRole) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedRole,
                onDismissRequest = { expandedRole = false }
            ) {
                roleList.forEach { role ->
                    DropdownMenuItem(
                        text = { Text(role) },
                        onClick = {
                            selectedRole = role
                            expandedRole = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol Simpan
        Button(
            onClick = {
                if (nama.isNotEmpty() && email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    userList = userList + UserData(nama, email, selectedRole)
                    // Reset form
                    nama = ""
                    email = ""
                    selectedRole = "Siswa"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Simpan User")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Daftar User
        Text(
            "📋 Daftar User",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (userList.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = "Belum ada user yang ditambahkan",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            userList.forEach { user ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = user.nama,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("Email: ${user.email}")
                        Text(
                            text = "Role: ${user.role}",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

data class UserData(
    val nama: String,
    val email: String,
    val role: String
)

//////////////////////////////
// 🧩 PAGE 2: Entry Jadwal
//////////////////////////////
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryJadwalPage() {
    // State untuk input
    var selectedHari by remember { mutableStateOf("Senin") }
    var expandedHari by remember { mutableStateOf(false) }

    var selectedKelas by remember { mutableStateOf("10 RPL") }
    var expandedKelas by remember { mutableStateOf(false) }

    var selectedMapel by remember { mutableStateOf("IPA") }
    var expandedMapel by remember { mutableStateOf(false) }

    var selectedGuru by remember { mutableStateOf("Siti") }
    var expandedGuru by remember { mutableStateOf(false) }

    var jamKe by remember { mutableStateOf("") }

    // State untuk list jadwal
    var jadwalList by remember { mutableStateOf(listOf<JadwalData>()) }

    val hariList = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
    val kelasList = listOf("10 RPL", "11 RPL", "12 RPL")
    val mapelList = listOf("IPA", "IPS", "Bahasa Indonesia", "Matematika", "Pemrograman", "Basis Data")
    val guruList = listOf("Siti", "Budi", "Adi", "Agus", "Rina", "Tono")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "📅 Entry Jadwal Baru",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Spinner Hari
        ExposedDropdownMenuBox(
            expanded = expandedHari,
            onExpandedChange = { expandedHari = !expandedHari }
        ) {
            OutlinedTextField(
                value = selectedHari,
                onValueChange = {},
                readOnly = true,
                label = { Text("Pilih Hari") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedHari) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedHari,
                onDismissRequest = { expandedHari = false }
            ) {
                hariList.forEach { hari ->
                    DropdownMenuItem(
                        text = { Text(hari) },
                        onClick = {
                            selectedHari = hari
                            expandedHari = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Spinner Kelas
        ExposedDropdownMenuBox(
            expanded = expandedKelas,
            onExpandedChange = { expandedKelas = !expandedKelas }
        ) {
            OutlinedTextField(
                value = selectedKelas,
                onValueChange = {},
                readOnly = true,
                label = { Text("Pilih Kelas") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedKelas) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedKelas,
                onDismissRequest = { expandedKelas = false }
            ) {
                kelasList.forEach { kelas ->
                    DropdownMenuItem(
                        text = { Text(kelas) },
                        onClick = {
                            selectedKelas = kelas
                            expandedKelas = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Spinner Mata Pelajaran
        ExposedDropdownMenuBox(
            expanded = expandedMapel,
            onExpandedChange = { expandedMapel = !expandedMapel }
        ) {
            OutlinedTextField(
                value = selectedMapel,
                onValueChange = {},
                readOnly = true,
                label = { Text("Pilih Mata Pelajaran") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMapel) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedMapel,
                onDismissRequest = { expandedMapel = false }
            ) {
                mapelList.forEach { mapel ->
                    DropdownMenuItem(
                        text = { Text(mapel) },
                        onClick = {
                            selectedMapel = mapel
                            expandedMapel = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Spinner Guru
        ExposedDropdownMenuBox(
            expanded = expandedGuru,
            onExpandedChange = { expandedGuru = !expandedGuru }
        ) {
            OutlinedTextField(
                value = selectedGuru,
                onValueChange = {},
                readOnly = true,
                label = { Text("Pilih Guru") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGuru) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedGuru,
                onDismissRequest = { expandedGuru = false }
            ) {
                guruList.forEach { guru ->
                    DropdownMenuItem(
                        text = { Text(guru) },
                        onClick = {
                            selectedGuru = guru
                            expandedGuru = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Input Jam Ke-
        OutlinedTextField(
            value = jamKe,
            onValueChange = { jamKe = it },
            label = { Text("Jam Ke-") },
            placeholder = { Text("Contoh: 1, 2, 3...") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol Simpan
        Button(
            onClick = {
                if (jamKe.isNotEmpty()) {
                    jadwalList = jadwalList + JadwalData(
                        hari = selectedHari,
                        kelas = selectedKelas,
                        mapel = selectedMapel,
                        guru = selectedGuru,
                        jamKe = jamKe
                    )
                    // Reset jam ke
                    jamKe = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Simpan Jadwal")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Daftar Jadwal
        Text(
            "📋 Daftar Jadwal",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (jadwalList.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = "Belum ada jadwal yang ditambahkan",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            jadwalList.forEach { jadwal ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${jadwal.hari} - ${jadwal.kelas}",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("Mata Pelajaran: ${jadwal.mapel}")
                        Text("Guru: ${jadwal.guru}")
                        Text(
                            text = "Jam ke-${jadwal.jamKe}",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

data class JadwalData(
    val hari: String,
    val kelas: String,
    val mapel: String,
    val guru: String,
    val jamKe: String
)

//////////////////////////////
// 🧩 PAGE 3: List User
//////////////////////////////
@Composable
fun ListUserPage() {
    // Data dummy untuk list user
    val userList = listOf(
        UserData("Ahmad", "ahmad@email.com", "Siswa"),
        UserData("Budi", "budi@email.com", "Kurikulum"),
        UserData("Citra", "citra@email.com", "Kepala Sekolah"),
        UserData("Deni", "deni@email.com", "Admin")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "👥 Daftar Semua User",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(userList) { user ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = user.nama,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("Email: ${user.email}")
                        Text(
                            text = "Role: ${user.role}",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

//////////////////////////////
// 🔍 PREVIEW
//////////////////////////////
@Preview(showBackground = true)
@Composable
fun AdminScreenPreview() {
    AplikasiMonitoringKelasTheme {
        AdminScreen()
    }
}
