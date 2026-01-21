package com.umar.warunggo.ui.profile

import com.umar.warunggo.data.model.BusinessSummary
import com.umar.warunggo.data.model.ProfileData

/**
 * UI state for Profile screen
 */
data class ProfileUiState(
    val isLoading: Boolean = false,
    val profileData: ProfileData? = null,
    val businessSummary: BusinessSummary? = null,
    val error: String? = null,
    val showLogoutDialog: Boolean = false,
    val showImagePicker: Boolean = false
)
