package com.umar.warunggo.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for Register screen
 * Handles complex validation and registration flow
 */
class RegisterViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()
    
    fun onFullNameChanged(name: String) {
        _uiState.update { 
            it.copy(
                fullName = name,
                fullNameError = validateFullName(name),
                generalError = null
            )
        }
        validateForm()
    }
    
    fun onEmailChanged(email: String) {
        _uiState.update { 
            it.copy(
                email = email,
                emailError = validateEmail(email),
                generalError = null
            )
        }
        validateForm()
    }
    
    fun onPhoneNumberChanged(phone: String) {
        // Only allow digits
        val cleanedPhone = phone.filter { it.isDigit() }
        _uiState.update { 
            it.copy(
                phoneNumber = cleanedPhone,
                phoneNumberError = validatePhoneNumber(cleanedPhone),
                generalError = null
            )
        }
        validateForm()
    }
    
    fun onPasswordChanged(password: String) {
        _uiState.update { 
            it.copy(
                password = password,
                passwordError = validatePassword(password),
                passwordStrength = calculatePasswordStrength(password),
                confirmPasswordError = if (it.confirmPassword.isNotEmpty()) {
                    validateConfirmPassword(password, it.confirmPassword)
                } else null,
                generalError = null
            )
        }
        validateForm()
    }
    
    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { 
            it.copy(
                confirmPassword = confirmPassword,
                confirmPasswordError = validateConfirmPassword(it.password, confirmPassword),
                generalError = null
            )
        }
        validateForm()
    }
    
    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }
    
    fun onToggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }
    
    fun onTermsAcceptedChanged(accepted: Boolean) {
        _uiState.update { it.copy(isTermsAccepted = accepted) }
        validateForm()
    }
    
    fun onRegisterClick(onSuccess: () -> Unit) {
        if (!_uiState.value.isFormValid) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }
            
            // Simulate API call
            delay(2000)
            
            // For demo: always success if validation passes
            _uiState.update { it.copy(isLoading = false) }
            onSuccess()
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(generalError = null) }
    }
    
    // Validation functions
    private fun validateFullName(name: String): String? {
        return when {
            name.isEmpty() -> "Nama lengkap tidak boleh kosong"
            name.length < 3 -> "Nama minimal 3 karakter"
            else -> null
        }
    }
    
    private fun validateEmail(email: String): String? {
        return when {
            email.isEmpty() -> "Email tidak boleh kosong"
            !email.contains("@") -> "Format email tidak valid"
            !email.contains(".") -> "Format email tidak valid"
            else -> null
        }
    }
    
    private fun validatePhoneNumber(phone: String): String? {
        return when {
            phone.isEmpty() -> "Nomor telepon tidak boleh kosong"
            phone.length < 10 -> "Nomor telepon minimal 10 digit"
            phone.length > 15 -> "Nomor telepon maksimal 15 digit"
            else -> null
        }
    }
    
    private fun validatePassword(password: String): String? {
        return when {
            password.isEmpty() -> "Password tidak boleh kosong"
            password.length < 8 -> "Password minimal 8 karakter"
            !password.any { it.isDigit() } -> "Password harus mengandung minimal 1 angka"
            !password.any { it.isLetter() } -> "Password harus mengandung minimal 1 huruf"
            else -> null
        }
    }
    
    private fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        return when {
            confirmPassword.isEmpty() -> "Konfirmasi password tidak boleh kosong"
            password != confirmPassword -> "Password tidak cocok"
            else -> null
        }
    }
    
    /**
     * Calculate password strength based on length and character diversity
     */
    private fun calculatePasswordStrength(password: String): PasswordStrength {
        if (password.isEmpty()) return PasswordStrength.WEAK
        
        var score = 0
        
        // Length check
        when {
            password.length >= 12 -> score += 3
            password.length >= 8 -> score += 2
            else -> score += 1
        }
        
        // Character diversity
        if (password.any { it.isLowerCase() }) score++
        if (password.any { it.isUpperCase() }) score++
        if (password.any { it.isDigit() }) score++
        if (password.any { !it.isLetterOrDigit() }) score++
        
        return when {
            score >= 7 -> PasswordStrength.STRONG
            score >= 4 -> PasswordStrength.MEDIUM
            else -> PasswordStrength.WEAK
        }
    }
    
    private fun validateForm() {
        val isValid = _uiState.value.fullNameError == null &&
                     _uiState.value.emailError == null &&
                     _uiState.value.phoneNumberError == null &&
                     _uiState.value.passwordError == null &&
                     _uiState.value.confirmPasswordError == null &&
                     _uiState.value.fullName.isNotEmpty() &&
                     _uiState.value.email.isNotEmpty() &&
                     _uiState.value.phoneNumber.isNotEmpty() &&
                     _uiState.value.password.isNotEmpty() &&
                     _uiState.value.confirmPassword.isNotEmpty() &&
                     _uiState.value.isTermsAccepted
        
        _uiState.update { it.copy(isFormValid = isValid) }
    }
}
