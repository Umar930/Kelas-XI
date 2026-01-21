package com.umar.warunggo.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umar.warunggo.data.model.BusinessSummary
import com.umar.warunggo.data.model.ProfileData
import com.umar.warunggo.data.model.StoreInfo
import com.umar.warunggo.data.model.UserRole
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for Profile screen
 * Manages profile data and business summary
 */
class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    /**
     * Load profile data from repository
     */
    fun loadProfileData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                // Simulate network delay
                delay(500)
                
                // Mock data - Replace with actual repository call
                val profile = generateMockProfileData()
                val summary = generateMockBusinessSummary()
                
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        profileData = profile,
                        businessSummary = summary
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Gagal memuat data profil"
                    )
                }
            }
        }
    }

    /**
     * Show logout confirmation dialog
     */
    fun showLogoutDialog() {
        _uiState.update { it.copy(showLogoutDialog = true) }
    }

    /**
     * Hide logout confirmation dialog
     */
    fun hideLogoutDialog() {
        _uiState.update { it.copy(showLogoutDialog = false) }
    }

    /**
     * Show image picker
     */
    fun showImagePicker() {
        _uiState.update { it.copy(showImagePicker = true) }
    }

    /**
     * Hide image picker
     */
    fun hideImagePicker() {
        _uiState.update { it.copy(showImagePicker = false) }
    }

    /**
     * Perform logout action
     */
    fun logout() {
        viewModelScope.launch {
            // TODO: Implement actual logout logic
            // Clear user session, navigate to login
            _uiState.update { it.copy(showLogoutDialog = false) }
        }
    }

    /**
     * Update profile avatar
     */
    fun updateAvatar(imageUri: String) {
        viewModelScope.launch {
            // TODO: Upload avatar to server
            _uiState.update { state ->
                state.copy(
                    profileData = state.profileData?.copy(avatarUrl = imageUri),
                    showImagePicker = false
                )
            }
        }
    }

    /**
     * Generate mock profile data for preview
     */
    private fun generateMockProfileData(): ProfileData {
        return ProfileData(
            id = "user_001",
            fullName = "Umar Ahmad Fauzi",
            email = "umar.fauzi@warunggo.com",
            phone = "08123456789",
            role = UserRole.OWNER,
            avatarUrl = null,
            joinDate = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000), // 30 days ago
            lastLogin = System.currentTimeMillis() - (2L * 60 * 60 * 1000), // 2 hours ago
            storeInfo = StoreInfo(
                storeName = "Warung Berkah Jaya",
                address = "Jl. Merdeka No. 123, Bandung, Jawa Barat 40111",
                phone = "0227654321",
                category = "Toko Kelontong & Sembako",
                operationalHours = "07:00 - 22:00",
                isActive = true
            )
        )
    }

    /**
     * Generate mock business summary for preview
     */
    private fun generateMockBusinessSummary(): BusinessSummary {
        return BusinessSummary(
            totalProducts = 156,
            totalTransactions = 1248,
            monthlyRevenue = 165000000.0,
            monthlyProfit = 42500000.0
        )
    }
}
