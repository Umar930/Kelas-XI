package com.umar.warunggo.ui.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.umar.warunggo.data.model.BusinessSummary
import com.umar.warunggo.data.model.ProfileData
import com.umar.warunggo.data.model.SettingsMenuItem
import com.umar.warunggo.data.model.StoreInfo
import com.umar.warunggo.data.model.UserRole
import com.umar.warunggo.ui.theme.WarungGoTheme
import java.text.SimpleDateFormat
import java.util.*

/**
 * Main Profile Screen
 * Displays user profile, business summary, store info, and settings
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onChangePassword: () -> Unit = {},
    onEditStore: () -> Unit = {},
    onOpenSettings: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingState()
                }
                uiState.error != null -> {
                    ErrorState(
                        message = uiState.error ?: "Terjadi kesalahan",
                        onRetry = { viewModel.loadProfileData() }
                    )
                }
                uiState.profileData != null && uiState.businessSummary != null -> {
                    ProfileContent(
                        profileData = uiState.profileData!!,
                        businessSummary = uiState.businessSummary!!,
                        onAvatarClick = { viewModel.showImagePicker() },
                        onEditProfile = onEditProfile,
                        onEditStore = onEditStore,
                        onChangePassword = onChangePassword,
                        onOpenSettings = onOpenSettings,
                        onLogoutClick = { viewModel.showLogoutDialog() }
                    )
                }
            }
        }

        // Logout confirmation dialog
        if (uiState.showLogoutDialog) {
            LogoutConfirmationDialog(
                onConfirm = {
                    viewModel.logout()
                    onLogout()
                },
                onDismiss = { viewModel.hideLogoutDialog() }
            )
        }

        // Image picker dialog (placeholder)
        if (uiState.showImagePicker) {
            ImagePickerDialog(
                onImageSelected = { uri ->
                    viewModel.updateAvatar(uri)
                },
                onDismiss = { viewModel.hideImagePicker() }
            )
        }
    }
}

/**
 * Main content with all profile sections
 */
@Composable
private fun ProfileContent(
    profileData: ProfileData,
    businessSummary: BusinessSummary,
    onAvatarClick: () -> Unit,
    onEditProfile: () -> Unit,
    onEditStore: () -> Unit,
    onChangePassword: () -> Unit,
    onOpenSettings: (String) -> Unit,
    onLogoutClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section 1: Profile Header
        item {
            ProfileHeader(
                profileData = profileData,
                onAvatarClick = onAvatarClick,
                onEditClick = onEditProfile
            )
        }

        // Section 2: Business Summary Dashboard
        item {
            Text(
                text = "Ringkasan Bisnis",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        
        item {
            BusinessSummaryGrid(
                businessSummary = businessSummary,
                onItemClick = { /* Navigate to details */ }
            )
        }

        // Section 3: Store Information
        item {
            Text(
                text = "Informasi Warung",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        
        item {
            StoreInfoCard(
                storeInfo = profileData.storeInfo,
                onEditStore = onEditStore
            )
        }

        // Section 4: Account Information
        item {
            Text(
                text = "Informasi Akun",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        
        item {
            AccountInfoCard(profileData = profileData)
        }

        // Section 5: Settings Menu
        item {
            Text(
                text = "Pengaturan",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        
        item {
            SettingsList(
                onChangePassword = onChangePassword,
                onOpenSettings = onOpenSettings
            )
        }

        // Section 6: Logout Button
        item {
            Spacer(modifier = Modifier.height(8.dp))
            LogoutSection(onLogoutClick = onLogoutClick)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Section 1: Profile Header Card
 * Shows avatar, name, role, store name, and status
 */
@Composable
private fun ProfileHeader(
    profileData: ProfileData,
    onAvatarClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Gradient background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar with edit overlay
                Box(
                    modifier = Modifier.size(120.dp)
                ) {
                    // Avatar
                    Surface(
                        modifier = Modifier
                            .size(120.dp)
                            .clickable { onAvatarClick() },
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        border = androidx.compose.foundation.BorderStroke(
                            3.dp,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            if (profileData.avatarUrl != null) {
                                // TODO: Load image with Coil
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    // Edit icon overlay
                    Surface(
                        modifier = Modifier
                            .size(36.dp)
                            .align(Alignment.BottomEnd)
                            .clickable { onAvatarClick() },
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        shadowElevation = 4.dp
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit avatar",
                            modifier = Modifier.padding(8.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name
                Text(
                    text = profileData.fullName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Role badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.secondary
                ) {
                    Text(
                        text = profileData.role.displayName,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Store name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Store,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = profileData.storeInfo.storeName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Status badge
                StatusBadge(isActive = profileData.storeInfo.isActive)
            }

            // Edit button (top right)
            IconButton(
                onClick = onEditClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit profil",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * Status badge showing Active/Inactive
 */
@Composable
private fun StatusBadge(isActive: Boolean) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isActive) 
            MaterialTheme.colorScheme.tertiaryContainer 
        else 
            MaterialTheme.colorScheme.errorContainer
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (isActive) 
                            MaterialTheme.colorScheme.tertiary 
                        else 
                            MaterialTheme.colorScheme.error
                    )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (isActive) "Aktif" else "Nonaktif",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = if (isActive) 
                    MaterialTheme.colorScheme.onTertiaryContainer 
                else 
                    MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

/**
 * Section 2: Business Summary Grid (2x2)
 * Shows products, transactions, revenue, profit
 */
@Composable
private fun BusinessSummaryGrid(
    businessSummary: BusinessSummary,
    onItemClick: (String) -> Unit
) {
    val summaryItems = remember(businessSummary) {
        listOf(
            SummaryItem(
                id = "products",
                icon = Icons.Default.Inventory2,
                title = "Total Produk",
                value = businessSummary.totalProducts.toString(),
                color = Color(0xFF6366F1)
            ),
            SummaryItem(
                id = "transactions",
                icon = Icons.Default.Receipt,
                title = "Total Transaksi",
                value = businessSummary.totalTransactions.toString(),
                color = Color(0xFF8B5CF6)
            ),
            SummaryItem(
                id = "revenue",
                icon = Icons.Default.TrendingUp,
                title = "Omzet Bulan Ini",
                value = businessSummary.getFormattedRevenue(),
                color = Color(0xFF10B981)
            ),
            SummaryItem(
                id = "profit",
                icon = Icons.Default.Paid,
                title = "Laba Bulan Ini",
                value = businessSummary.getFormattedProfit(),
                color = Color(0xFFF59E0B)
            )
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.height(300.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(summaryItems) { item ->
            SummaryCard(
                item = item,
                onClick = { onItemClick(item.id) }
            )
        }
    }
}

/**
 * Summary card item
 */
private data class SummaryItem(
    val id: String,
    val icon: ImageVector,
    val title: String,
    val value: String,
    val color: Color
)

/**
 * Individual summary card
 */
@Composable
private fun SummaryCard(
    item: SummaryItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = item.color.copy(alpha = 0.15f)
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(24.dp),
                    tint = item.color
                )
            }

            // Title and value
            Column {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * Section 3: Store Information Card
 */
@Composable
private fun StoreInfoCard(
    storeInfo: StoreInfo,
    onEditStore: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            InfoRow(
                icon = Icons.Default.Store,
                label = "Nama Warung",
                value = storeInfo.storeName
            )
            
            Divider(modifier = Modifier.padding(vertical = 12.dp))
            
            InfoRow(
                icon = Icons.Default.LocationOn,
                label = "Alamat",
                value = storeInfo.address
            )
            
            Divider(modifier = Modifier.padding(vertical = 12.dp))
            
            InfoRow(
                icon = Icons.Default.Phone,
                label = "Telepon",
                value = storeInfo.phone
            )
            
            Divider(modifier = Modifier.padding(vertical = 12.dp))
            
            InfoRow(
                icon = Icons.Default.Category,
                label = "Kategori Bisnis",
                value = storeInfo.category
            )
            
            Divider(modifier = Modifier.padding(vertical = 12.dp))
            
            InfoRow(
                icon = Icons.Default.Schedule,
                label = "Jam Operasional",
                value = storeInfo.operationalHours
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Edit button
            Button(
                onClick = onEditStore,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Informasi Warung")
            }
        }
    }
}

/**
 * Section 4: Account Information Card
 */
@Composable
private fun AccountInfoCard(profileData: ProfileData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            InfoRow(
                icon = Icons.Default.Person,
                label = "Nama Lengkap",
                value = profileData.fullName
            )
            
            Divider(modifier = Modifier.padding(vertical = 12.dp))
            
            InfoRow(
                icon = Icons.Default.Email,
                label = "Email",
                value = profileData.email
            )
            
            Divider(modifier = Modifier.padding(vertical = 12.dp))
            
            InfoRow(
                icon = Icons.Default.Badge,
                label = "Role",
                value = profileData.role.displayName
            )
            
            Divider(modifier = Modifier.padding(vertical = 12.dp))
            
            InfoRow(
                icon = Icons.Default.CalendarMonth,
                label = "Bergabung Sejak",
                value = formatDate(profileData.joinDate)
            )
            
            Divider(modifier = Modifier.padding(vertical = 12.dp))
            
            InfoRow(
                icon = Icons.Default.AccessTime,
                label = "Login Terakhir",
                value = formatDateTime(profileData.lastLogin)
            )
        }
    }
}

/**
 * Reusable info row with icon, label, and value
 */
@Composable
private fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Section 5: Settings Menu List
 */
@Composable
private fun SettingsList(
    onChangePassword: () -> Unit,
    onOpenSettings: (String) -> Unit
) {
    val settingsItems = remember {
        listOf(
            SettingsMenuItem("profile", "Edit Profil", Icons.Default.Person),
            SettingsMenuItem("password", "Ubah Password", Icons.Default.Lock),
            SettingsMenuItem("store", "Pengaturan Toko", Icons.Default.Settings),
            SettingsMenuItem("notifications", "Notifikasi", Icons.Default.Notifications),
            SettingsMenuItem("theme", "Tema", Icons.Default.Palette),
            SettingsMenuItem("backup", "Backup & Sinkronisasi", Icons.Default.CloudSync),
            SettingsMenuItem("help", "Bantuan", Icons.Default.Help),
            SettingsMenuItem("about", "Tentang Aplikasi", Icons.Default.Info)
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            settingsItems.forEachIndexed { index, item ->
                SettingsItem(
                    icon = item.icon,
                    title = item.title,
                    onClick = {
                        when (item.id) {
                            "password" -> onChangePassword()
                            else -> onOpenSettings(item.id)
                        }
                    }
                )
                
                if (index < settingsItems.size - 1) {
                    Divider(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

/**
 * Individual settings menu item
 */
@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}

/**
 * Section 6: Logout Action Button
 */
@Composable
private fun LogoutSection(onLogoutClick: () -> Unit) {
    OutlinedButton(
        onClick = onLogoutClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f),
            contentColor = MaterialTheme.colorScheme.error
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
        )
    ) {
        Icon(
            imageVector = Icons.Default.Logout,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Logout",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/**
 * Logout confirmation dialog
 */
@Composable
private fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(
                text = "Konfirmasi Logout",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = "Apakah Anda yakin ingin keluar dari aplikasi?",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Ya, Logout")
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
 * Image picker dialog (placeholder)
 */
@Composable
private fun ImagePickerDialog(
    onImageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = null
            )
        },
        title = {
            Text("Pilih Foto Profil")
        },
        text = {
            Column {
                TextButton(
                    onClick = {
                        // TODO: Open camera
                        onImageSelected("camera://image_uri")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.CameraAlt, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Ambil Foto")
                }
                
                TextButton(
                    onClick = {
                        // TODO: Open gallery
                        onImageSelected("gallery://image_uri")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.PhotoLibrary, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Pilih dari Galeri")
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

/**
 * Loading state
 */
@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Memuat profil...",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/**
 * Error state
 */
@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Icon(Icons.Default.Refresh, null)
                Spacer(Modifier.width(8.dp))
                Text("Coba Lagi")
            }
        }
    }
}

/**
 * Format date to readable string
 */
private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
    return sdf.format(Date(timestamp))
}

/**
 * Format date time to readable string
 */
private fun formatDateTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
    return sdf.format(Date(timestamp))
}

// ============================================
// PREVIEW SECTION
// ============================================

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileScreenPreview() {
    WarungGoTheme {
        ProfileScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProfileScreenDarkPreview() {
    WarungGoTheme {
        ProfileScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileHeaderPreview() {
    WarungGoTheme {
        ProfileHeader(
            profileData = ProfileData(
                id = "1",
                fullName = "Umar Ahmad Fauzi",
                email = "umar@warunggo.com",
                phone = "08123456789",
                role = UserRole.OWNER,
                avatarUrl = null,
                joinDate = System.currentTimeMillis(),
                lastLogin = System.currentTimeMillis(),
                storeInfo = StoreInfo(
                    storeName = "Warung Berkah Jaya",
                    address = "Jl. Merdeka No. 123",
                    phone = "0227654321",
                    category = "Toko Kelontong",
                    operationalHours = "07:00 - 22:00",
                    isActive = true
                )
            ),
            onAvatarClick = {},
            onEditClick = {}
        )
    }
}
