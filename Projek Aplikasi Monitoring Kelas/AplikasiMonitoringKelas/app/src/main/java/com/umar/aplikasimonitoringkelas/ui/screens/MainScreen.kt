package com.umar.aplikasimonitoringkelas.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.umar.aplikasimonitoringkelas.navigation.Screens
import com.umar.aplikasimonitoringkelas.ui.theme.AplikasiMonitoringKelasTheme
import com.umar.aplikasimonitoringkelas.ui.viewmodel.LoginViewModel

/**
 * Layar Dashboard utama aplikasi
 * 
 * @param navController Controller untuk navigasi antar layar
 * @param role Role pengguna yang login
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    role: String = "Admin"
) {
    val context = LocalContext.current
    val viewModel = remember { LoginViewModel(context) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Dashboard",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    ) 
                },
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Sapaan
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Selamat Datang,",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = role,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Section Title
            Text(
                text = "Menu Navigasi Cepat",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Kartu Navigasi: Data Guru
            NavigationCard(
                title = "Data Guru",
                description = "Lihat dan kelola data guru",
                icon = Icons.Default.Person,
                onClick = {
                    navController.navigate(Screens.GURU_LIST)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Kartu Navigasi: Data Siswa
            NavigationCard(
                title = "Data Siswa",
                description = "Lihat dan kelola data siswa",
                icon = Icons.Default.AccountCircle,
                onClick = {
                    navController.navigate(Screens.SISWA_LIST)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Kartu Navigasi: Data Kelas
            NavigationCard(
                title = "Data Kelas",
                description = "Lihat dan kelola data kelas",
                icon = Icons.Default.Home,
                onClick = {
                    navController.navigate(Screens.KELAS_LIST)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Kartu Navigasi: Monitoring
            NavigationCard(
                title = "Monitoring",
                description = "Monitor aktivitas kelas",
                icon = Icons.Default.Info,
                onClick = {
                    Toast.makeText(
                        context,
                        "Fitur Monitoring akan segera hadir",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

    // Dialog Logout
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Konfirmasi Logout") },
            text = { Text("Apakah Anda yakin ingin keluar dari aplikasi?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        // Panggil fungsi logout dari ViewModel
                        viewModel.logout {
                            // Navigate ke login dan hapus semua back stack
                            navController.navigate(Screens.LOGIN) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                ) {
                    Text("Ya, Logout")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

/**
 * Komponen Kartu Navigasi yang dapat diklik
 */
@Composable
fun NavigationCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    AplikasiMonitoringKelasTheme {
        MainScreen(
            navController = rememberNavController(),
            role = "Admin"
        )
    }
}
