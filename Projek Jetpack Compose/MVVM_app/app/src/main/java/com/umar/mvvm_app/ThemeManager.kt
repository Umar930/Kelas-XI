package com.umar.mvvm_app

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Definisi extension property untuk Context untuk membuat singleton DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

/**
 * Manager class untuk mengelola preferensi tema menggunakan DataStore
 */
class ThemeManager(private val context: Context) {
    companion object {
        // Definisi kunci preferensi untuk pengaturan mode gelap
        val IS_DARK_MODE_KEY = booleanPreferencesKey("is_dark_mode")
    }

    /**
     * Menyimpan preferensi tema ke DataStore
     * @param isDarkMode Boolean yang menunjukkan apakah mode gelap harus diaktifkan
     */
    suspend fun saveTheme(isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_MODE_KEY] = isDarkMode
        }
    }

    /**
     * Mendapatkan preferensi tema saat ini dari DataStore
     * @return Flow<Boolean> yang mengemisikan preferensi tema saat ini (true untuk mode gelap, false untuk mode terang)
     */
    fun getTheme(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_DARK_MODE_KEY] ?: false  // Default ke mode terang (false)
        }
    }
}