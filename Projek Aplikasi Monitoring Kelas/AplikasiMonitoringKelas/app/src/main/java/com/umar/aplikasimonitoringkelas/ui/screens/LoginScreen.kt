package com.umar.aplikasimonitoringkelas.ui.screens

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.SiswaDashboardActivity
import com.umar.aplikasimonitoringkelas.navigation.Screens
import com.umar.aplikasimonitoringkelas.ui.viewmodel.LoginViewModel
import com.umar.aplikasimonitoringkelas.ui.viewmodel.LoginState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {

    val context = LocalContext.current
    val viewModel = remember { LoginViewModel(context) }
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("") }
    var expandedRoleMenu by remember { mutableStateOf(false) }

    val roleOptions = listOf("Admin", "Kurikulum", "Kepala Sekolah", "Siswa")

    fun convertRoleToApi(displayRole: String): String = when (displayRole) {
        "Admin" -> "admin"
        "Kurikulum" -> "kurikulum"
        "Kepala Sekolah" -> "kepala_sekolah"
        "Siswa" -> "siswa"
        else -> displayRole.lowercase()
    }

    // Navigasi bila sukses
    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                val role = (loginState as LoginState.Success).role
                
                // Launch XML-based Activity for Siswa role
                if (role.equals("siswa", ignoreCase = true)) {
                    Log.d("LoginScreen", "Launching SiswaDashboardActivity for role: $role")
                    try {
                        val intent = Intent(context, SiswaDashboardActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                        Log.d("LoginScreen", "SiswaDashboardActivity started successfully")
                    } catch (e: Exception) {
                        Log.e("LoginScreen", "Error launching SiswaDashboardActivity: ${e.message}", e)
                    }
                } else {
                    navController.navigate(Screens.createDashboardRoute(role)) {
                        popUpTo(Screens.LOGIN) { inclusive = true }
                    }
                }
                viewModel.resetState()
            }
            else -> {}
        }
    }

    // ======= UI BARU DI SINI =======
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.02f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {

            Column(
                modifier = Modifier
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Logo modern kecil
                Image(
                    painter = painterResource(id = R.drawable.logo_sekolah),
                    contentDescription = null,
                    modifier = Modifier
                        .size(110.dp)
                        .padding(bottom = 4.dp)
                )

                Text(
                    text = "Selamat Datang",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "Silakan masuk untuk melanjutkan",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 28.dp)
                )

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(14.dp),
                )

                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(14.dp),
                )

                // Role dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedRoleMenu,
                    onExpandedChange = { expandedRoleMenu = !expandedRoleMenu },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    OutlinedTextField(
                        value = selectedRole,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Pilih Role") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRoleMenu)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = expandedRoleMenu,
                        onDismissRequest = { expandedRoleMenu = false }
                    ) {
                        roleOptions.forEach { role ->
                            DropdownMenuItem(
                                text = { Text(role) },
                                onClick = {
                                    selectedRole = role
                                    expandedRoleMenu = false
                                }
                            )
                        }
                    }
                }

                // Tombol Login
                Button(
                    onClick = {
                        val apiRole = convertRoleToApi(selectedRole)
                        viewModel.login(email, password, apiRole)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = email.isNotEmpty() && password.isNotEmpty() && selectedRole.isNotEmpty() && loginState !is LoginState.Loading,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (loginState is LoginState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(26.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("LOGIN", style = MaterialTheme.typography.titleMedium)
                    }
                }

                if (loginState is LoginState.Error) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = (loginState as LoginState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
