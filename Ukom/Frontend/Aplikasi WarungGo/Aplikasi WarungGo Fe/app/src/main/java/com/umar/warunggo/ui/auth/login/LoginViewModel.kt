package com.umar.warunggo.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for Login screen
 * Handles business logic, validation, and state management
 */
class LoginViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    
    /**
     * Handle email text change with real-time validation
     */
    fun onEmailChanged(email: String) {
        _uiState.update { currentState ->
            currentState.copy(
                email = email,
                emailError = validateEmail(email),
                generalError = null
            )
        }
        validateForm()
    }
    
    /**
     * Handle password text change with real-time validation
     */
    fun onPasswordChanged(password: String) {
        _uiState.update { currentState ->
            currentState.copy(
                password = password,
                passwordError = validatePassword(password),
                generalError = null
            )
        }
        validateForm()
    }
    
    /**
     * Toggle password visibility
     */
    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }
    
    /**
     * Handle login submit
     * Simulates API call with delay
     */
    fun onLoginClick(onSuccess: () -> Unit) {
        if (!_uiState.value.isFormValid) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }
            
            // Simulate API call
            delay(1500)
            
            // For demo: accept any valid email/password
            val isSuccess = _uiState.value.email.isNotEmpty() && 
                           _uiState.value.password.length >= 8
            
            if (isSuccess) {
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            } else {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        generalError = "Login gagal. Periksa kembali email dan password Anda."
                    )
                }
            }
        }
    }
    
    /**
     * Clear error message
     */
    fun clearError() {
        _uiState.update { it.copy(generalError = null) }
    }
    
    /**
     * Validate email format
     */
    private fun validateEmail(email: String): String? {
        return when {
            email.isEmpty() -> "Email tidak boleh kosong"
            !email.contains("@") -> "Format email tidak valid"
            !email.contains(".") -> "Format email tidak valid"
            else -> null
        }
    }
    
    /**
     * Validate password requirements
     */
    private fun validatePassword(password: String): String? {
        return when {
            password.isEmpty() -> "Password tidak boleh kosong"
            password.length < 8 -> "Password minimal 8 karakter"
            !password.any { it.isDigit() } -> "Password harus mengandung minimal 1 angka"
            else -> null
        }
    }
    
    /**
     * Validate entire form
     */
    private fun validateForm() {
        val isValid = _uiState.value.emailError == null &&
                     _uiState.value.passwordError == null &&
                     _uiState.value.email.isNotEmpty() &&
                     _uiState.value.password.isNotEmpty()
        
        _uiState.update { it.copy(isFormValid = isValid) }
    }
}
