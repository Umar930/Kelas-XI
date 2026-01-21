package com.umar.warunggo.ui.auth.login

/**
 * Immutable UI state for Login screen
 * Follows MVVM pattern with single source of truth
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null,
    val isFormValid: Boolean = false
)
