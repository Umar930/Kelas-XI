package com.umar.warunggo.ui.auth.register

/**
 * Immutable UI state for Register screen
 */
data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isTermsAccepted: Boolean = false,
    val isLoading: Boolean = false,
    
    // Field errors
    val fullNameError: String? = null,
    val emailError: String? = null,
    val phoneNumberError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val generalError: String? = null,
    
    // Password strength
    val passwordStrength: PasswordStrength = PasswordStrength.WEAK,
    
    // Form validation
    val isFormValid: Boolean = false
)

/**
 * Password strength levels
 */
enum class PasswordStrength(val label: String, val color: Long) {
    WEAK("Lemah", 0xFFEF5350),      // Red
    MEDIUM("Sedang", 0xFFFFA726),   // Orange
    STRONG("Kuat", 0xFF66BB6A)      // Green
}
