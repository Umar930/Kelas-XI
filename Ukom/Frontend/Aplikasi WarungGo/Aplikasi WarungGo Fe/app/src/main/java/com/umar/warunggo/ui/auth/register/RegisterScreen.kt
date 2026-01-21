package com.umar.warunggo.ui.auth.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.umar.warunggo.R
import com.umar.warunggo.ui.components.AppTextField
import com.umar.warunggo.ui.components.PasswordField
import com.umar.warunggo.ui.components.PrimaryButton

/**
 * Register Screen - Production Ready
 * Clean architecture with comprehensive validation
 */
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(uiState.generalError) {
        uiState.generalError?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                
                // Header
                RegisterHeader()
                
                Spacer(modifier = Modifier.height(40.dp))
                
                // Form
                RegisterForm(
                    uiState = uiState,
                    onFullNameChanged = viewModel::onFullNameChanged,
                    onEmailChanged = viewModel::onEmailChanged,
                    onPhoneNumberChanged = viewModel::onPhoneNumberChanged,
                    onPasswordChanged = viewModel::onPasswordChanged,
                    onConfirmPasswordChanged = viewModel::onConfirmPasswordChanged,
                    onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
                    onToggleConfirmPasswordVisibility = viewModel::onToggleConfirmPasswordVisibility,
                    onTermsAcceptedChanged = viewModel::onTermsAcceptedChanged,
                    onRegisterClick = { viewModel.onRegisterClick(onRegisterSuccess) }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Login Navigation
                LoginPrompt(onClick = onNavigateToLogin)
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun RegisterHeader() {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Logo Image
            Image(
                painter = painterResource(id = R.drawable.logo_warunggo),
                contentDescription = "Logo WarungGo",
                modifier = Modifier.size(100.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Buat Akun Baru",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Daftar untuk mulai mengelola warung",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun RegisterForm(
    uiState: RegisterUiState,
    onFullNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPhoneNumberChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
    onTermsAcceptedChanged: (Boolean) -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Full Name
        AppTextField(
            value = uiState.fullName,
            onValueChange = onFullNameChanged,
            label = "Nama Lengkap",
            placeholder = "Masukkan nama lengkap",
            leadingIcon = Icons.Filled.Person,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            errorMessage = uiState.fullNameError,
            enabled = !uiState.isLoading
        )
        
        // Email
        AppTextField(
            value = uiState.email,
            onValueChange = onEmailChanged,
            label = "Email",
            placeholder = "contoh@email.com",
            leadingIcon = Icons.Filled.Email,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            errorMessage = uiState.emailError,
            enabled = !uiState.isLoading
        )
        
        // Phone Number
        AppTextField(
            value = uiState.phoneNumber,
            onValueChange = onPhoneNumberChanged,
            label = "Nomor Telepon",
            placeholder = "08xxxxxxxxxx",
            leadingIcon = Icons.Filled.Phone,
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next,
            errorMessage = uiState.phoneNumberError,
            enabled = !uiState.isLoading
        )
        
        // Password
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PasswordField(
                value = uiState.password,
                onValueChange = onPasswordChanged,
                label = "Password",
                placeholder = "Minimal 8 karakter",
                isPasswordVisible = uiState.isPasswordVisible,
                onTogglePasswordVisibility = onTogglePasswordVisibility,
                errorMessage = uiState.passwordError,
                imeAction = ImeAction.Next,
                enabled = !uiState.isLoading
            )
            
            // Password Strength Indicator
            if (uiState.password.isNotEmpty()) {
                PasswordStrengthIndicator(strength = uiState.passwordStrength)
            }
        }
        
        // Confirm Password
        PasswordField(
            value = uiState.confirmPassword,
            onValueChange = onConfirmPasswordChanged,
            label = "Konfirmasi Password",
            placeholder = "Ulangi password",
            isPasswordVisible = uiState.isConfirmPasswordVisible,
            onTogglePasswordVisibility = onToggleConfirmPasswordVisibility,
            errorMessage = uiState.confirmPasswordError,
            imeAction = ImeAction.Done,
            onImeAction = onRegisterClick,
            enabled = !uiState.isLoading
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Terms & Conditions
        TermsCheckbox(
            checked = uiState.isTermsAccepted,
            onCheckedChange = onTermsAcceptedChanged,
            enabled = !uiState.isLoading
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Register Button
        PrimaryButton(
            text = "Daftar",
            onClick = onRegisterClick,
            enabled = uiState.isFormValid && !uiState.isLoading,
            isLoading = uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun PasswordStrengthIndicator(strength: PasswordStrength) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Animated progress bar
        val progress by animateFloatAsState(
            targetValue = when (strength) {
                PasswordStrength.WEAK -> 0.33f
                PasswordStrength.MEDIUM -> 0.66f
                PasswordStrength.STRONG -> 1.0f
            },
            label = "progress"
        )
        
        val color by animateColorAsState(
            targetValue = Color(strength.color),
            label = "color"
        )
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(4.dp)
                    .background(color)
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Kekuatan Password:",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = strength.label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun TermsCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
        Text(
            text = "Saya menyetujui Syarat & Ketentuan",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun LoginPrompt(onClick: () -> Unit) {
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
            append("Sudah punya akun? ")
        }
        pushStringAnnotation(tag = "LOGIN", annotation = "login")
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        ) {
            append("Login")
        }
        pop()
    }
    
    ClickableText(
        text = annotatedText,
        style = MaterialTheme.typography.bodyMedium,
        onClick = { offset ->
            annotatedText.getStringAnnotations(
                tag = "LOGIN",
                start = offset,
                end = offset
            ).firstOrNull()?.let {
                onClick()
            }
        }
    )
}
