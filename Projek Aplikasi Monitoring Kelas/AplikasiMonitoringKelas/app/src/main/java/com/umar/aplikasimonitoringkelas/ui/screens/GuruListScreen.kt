package com.umar.aplikasimonitoringkelas.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.umar.aplikasimonitoringkelas.data.model.Resource
import com.umar.aplikasimonitoringkelas.model.Guru
import com.umar.aplikasimonitoringkelas.ui.theme.AplikasiMonitoringKelasTheme
import com.umar.aplikasimonitoringkelas.ui.viewmodel.CrudState
import com.umar.aplikasimonitoringkelas.ui.viewmodel.GuruViewModel

private const val TAG = "GuruListScreen"

/**
 * Layar untuk menampilkan daftar guru dengan state handling dan RBAC
 * 
 * @param navController Controller untuk navigasi antar layar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuruListScreen(
    navController: NavController
) {
    Log.d(TAG, "GuruListScreen composable started")
    
    val viewModel: GuruViewModel = viewModel()
    Log.d(TAG, "GuruViewModel obtained: $viewModel")
    
    // Observe states with lifecycle
    val guruListState by viewModel.guruListState.collectAsStateWithLifecycle()
    val filteredGuruList by viewModel.filteredGuruList.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val userRole by viewModel.userRole.collectAsStateWithLifecycle()
    val crudState by viewModel.crudState.collectAsStateWithLifecycle()
    
    // State untuk dialog konfirmasi hapus
    var showDeleteDialog by remember { mutableStateOf(false) }
    var guruToDelete by remember { mutableStateOf<Guru?>(null) }
    
    val context = LocalContext.current
    
    // Cek apakah user memiliki akses write (RBAC)
    val hasWriteAccess = viewModel.hasWriteAccess()
    
    // Handle CRUD state changes
    LaunchedEffect(crudState) {
        when (crudState) {
            is CrudState.Success -> {
                Toast.makeText(
                    context,
                    (crudState as CrudState.Success).message,
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.resetCrudState()
            }
            is CrudState.Error -> {
                Toast.makeText(
                    context,
                    (crudState as CrudState.Error).message,
                    Toast.LENGTH_LONG
                ).show()
                viewModel.resetCrudState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Daftar Guru",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            // FAB hanya tampil jika user memiliki akses write (RBAC)
            if (hasWriteAccess) {
                FloatingActionButton(
                    onClick = {
                        // TODO: Navigate to Add Guru Screen
                        Toast.makeText(
                            context,
                            "Navigasi ke form tambah guru",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tambah Guru"
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar
            SearchBarSection(
                searchQuery = searchQuery,
                onSearchQueryChange = { query ->
                    viewModel.searchGurus(query)
                }
            )
            
            // Content berdasarkan state
            when (guruListState) {
                is Resource.Loading -> {
                    // Loading State
                    LoadingState()
                }
                
                is Resource.Error -> {
                    // Error State
                    ErrorState(
                        message = (guruListState as Resource.Error).message,
                        onRetry = { viewModel.fetchGurus() }
                    )
                }
                
                is Resource.Empty -> {
                    // Empty State
                    EmptyState()
                }
                
                is Resource.Success -> {
                    // Success State - tampilkan list
                    if (filteredGuruList.isEmpty() && searchQuery.isNotBlank()) {
                        // Tidak ada hasil pencarian
                        SearchEmptyState()
                    } else {
                        GuruListContent(
                            guruList = filteredGuruList,
                            hasWriteAccess = hasWriteAccess,
                            onEditClick = { guru ->
                                // TODO: Navigate to Edit Guru Screen
                                Toast.makeText(
                                    context,
                                    "Edit: ${guru.nama}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onDeleteClick = { guru ->
                                guruToDelete = guru
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
    }
    
    // Dialog Konfirmasi Hapus
    if (showDeleteDialog && guruToDelete != null) {
        DeleteConfirmationDialog(
            guruName = guruToDelete!!.nama ?: "Guru",
            onConfirm = {
                guruToDelete?.id?.let { id ->
                    viewModel.deleteGuru(id)
                }
                showDeleteDialog = false
                guruToDelete = null
            },
            onDismiss = {
                showDeleteDialog = false
                guruToDelete = null
            }
        )
    }
}

/**
 * Komponen Search Bar
 */
@Composable
fun SearchBarSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = { Text("Cari guru berdasarkan nama, kode, atau mata pelajaran...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear"
                    )
                }
            }
        },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors()
    )
}

/**
 * State: Loading
 */
@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Memuat data guru...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * State: Error
 */
@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Error",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Terjadi Kesalahan",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRetry
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Coba Lagi")
            }
        }
    }
}

/**
 * State: Empty (tidak ada data)
 */
@Composable
fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Tidak ada data",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Data Guru Kosong",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Belum ada data guru yang tersedia",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * State: Search Empty (tidak ada hasil pencarian)
 */
@Composable
fun SearchEmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Tidak ditemukan",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Tidak Ditemukan",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tidak ada guru yang sesuai dengan pencarian",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Content: Daftar Guru
 */
@Composable
fun GuruListContent(
    guruList: List<Guru>,
    hasWriteAccess: Boolean,
    onEditClick: (Guru) -> Unit,
    onDeleteClick: (Guru) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(guruList) { guru ->
            GuruItem(
                guru = guru,
                hasWriteAccess = hasWriteAccess,
                onEditClick = { onEditClick(guru) },
                onDeleteClick = { onDeleteClick(guru) }
            )
        }
    }
}

/**
 * Item Guru dalam list
 */
@Composable
fun GuruItem(
    guru: Guru,
    hasWriteAccess: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Informasi Guru
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = guru.nama ?: "Nama tidak tersedia",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Kode: ${guru.kodeGuru ?: "-"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                // Tampilkan mata pelajaran jika ada
                guru.mataPelajaran?.let { mataPelajaran ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Mapel: $mataPelajaran",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            // Tombol Aksi (hanya tampil jika user memiliki akses write - RBAC)
            if (hasWriteAccess) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Tombol Edit
                    IconButton(
                        onClick = onEditClick,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit"
                        )
                    }
                    
                    // Tombol Delete
                    IconButton(
                        onClick = onDeleteClick,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Hapus"
                        )
                    }
                }
            }
        }
    }
}

/**
 * Dialog Konfirmasi Hapus
 */
@Composable
fun DeleteConfirmationDialog(
    guruName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text("Konfirmasi Hapus")
        },
        text = {
            Text("Apakah Anda yakin ingin menghapus data guru \"$guruName\"? Tindakan ini tidak dapat dibatalkan.")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Hapus")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

/**
 * Error Screen dengan pesan custom untuk initialization error
 */
@Composable
fun ErrorScreenWithMessage(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(64.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Terjadi Kesalahan",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Kembali")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GuruListScreenPreview() {
    AplikasiMonitoringKelasTheme {
        GuruListScreen(navController = rememberNavController())
    }
}
