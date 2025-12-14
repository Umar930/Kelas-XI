package com.umar.mvvm_app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * ViewModel untuk Aplikasi Berita, juga menangani state tema
 */
class NewsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val themeManager = ThemeManager(application)
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme
    
    init {
        // Inisialisasi tema dari preferensi yang tersimpan
        viewModelScope.launch {
            themeManager.getTheme().collectLatest { isDarkMode ->
                _isDarkTheme.value = isDarkMode
            }
        }
    }
    
    /**
     * Mengatur tema aplikasi (terang atau gelap)
     * @param isDark Boolean yang menunjukkan apakah tema gelap harus diaktifkan
     */
    fun setTheme(isDark: Boolean) {
        viewModelScope.launch {
            // Update DataStore
            themeManager.saveTheme(isDark)
            // Update state lokal segera untuk responsivitas UI
            _isDarkTheme.value = isDark
        }
    }
    
    // Tambahkan fungsionalitas lain terkait berita di sini
}