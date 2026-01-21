package com.umar.warunggo.ui.auth.login

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
 * Login Screen - Production Ready
 * Modern, elegant design optimized for dark theme
 * Follows Material 3 guidelines and MVVM architecture
 */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Show error in Snackbar
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                
                // Header Section
                LoginHeader()
                
                Spacer(modifier = Modifier.height(48.dp))
                
                // Form Section
                LoginForm(
                    uiState = uiState,
                    onEmailChanged = viewModel::onEmailChanged,
                    onPasswordChanged = viewModel::onPasswordChanged,
                    onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
                    onLoginClick = { viewModel.onLoginClick(onLoginSuccess) },
                    onForgotPasswordClick = { /* TODO */ }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Register Navigation
                RegisterPrompt(onClick = onNavigateToRegister)
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

/**
 * Header section with logo and app name
 */
@Composable
private fun LoginHeader() {
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
                modifier = Modifier.size(120.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // App Name
            Text(
                text = "WarungGo",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Subtitle
            Text(
                text = "Kelola warungmu lebih pintar",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Login form with validation
 */
@Composable
private fun LoginForm(
    uiState: LoginUiState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Email Field
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
        
        // Password Field
        PasswordField(
            value = uiState.password,
            onValueChange = onPasswordChanged,
            label = "Password",
            placeholder = "Minimal 8 karakter",
            isPasswordVisible = uiState.isPasswordVisible,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
            errorMessage = uiState.passwordError,
            imeAction = ImeAction.Done,
            onImeAction = onLoginClick,
            enabled = !uiState.isLoading
        )
        
        // Forgot Password
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onForgotPasswordClick) {
                Text(
                    text = "Lupa Password?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Login Button
        PrimaryButton(
            text = "Login",
            onClick = onLoginClick,
            enabled = uiState.isFormValid && !uiState.isLoading,
            isLoading = uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Register navigation prompt
 */
@Composable
private fun RegisterPrompt(onClick: () -> Unit) {
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
            append("Belum punya akun? ")
        }
        pushStringAnnotation(tag = "REGISTER", annotation = "register")
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        ) {
            append("Daftar")
        }
        pop()
    }
    
    ClickableText(
        text = annotatedText,
        style = MaterialTheme.typography.bodyMedium,
        onClick = { offset ->
            annotatedText.getStringAnnotations(
                tag = "REGISTER",
                start = offset,
                end = offset
            ).firstOrNull()?.let {
                onClick()
            }
        }
    )
}
