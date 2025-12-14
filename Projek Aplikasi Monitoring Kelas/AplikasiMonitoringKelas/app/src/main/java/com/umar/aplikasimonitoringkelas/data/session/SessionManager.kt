package com.umar.aplikasimonitoringkelas.data.session

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

/**
 * SessionManager untuk mengelola token autentikasi menggunakan DataStore
 */
class SessionManager(private val context: Context) {
    
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = "auth_preferences"
        )
        
        private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_KELAS_ID_KEY = stringPreferencesKey("user_kelas_id")
        
        @Volatile
        private var instance: SessionManager? = null
        
        /**
         * Mendapatkan instance SessionManager (singleton)
         */
        fun getInstance(context: Context): SessionManager {
            return instance ?: synchronized(this) {
                instance ?: SessionManager(context.applicationContext).also { instance = it }
            }
        }
    }
    
    /**
     * Menyimpan token autentikasi ke DataStore
     */
    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[AUTH_TOKEN_KEY] = token
        }
    }
    
    /**
     * Menyimpan data user ke DataStore
     */
    suspend fun saveUserData(role: String, email: String, name: String? = null, userId: String? = null) {
        context.dataStore.edit { preferences ->
            preferences[USER_ROLE_KEY] = role
            preferences[USER_EMAIL_KEY] = email
            if (name != null) {
                preferences[USER_NAME_KEY] = name
            }
            if (userId != null) {
                preferences[USER_ID_KEY] = userId
            }
        }
    }
    
    /**
     * Mendapatkan token autentikasi sebagai Flow
     */
    fun getAuthToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[AUTH_TOKEN_KEY]
        }
    }
    
    /**
     * Mendapatkan token autentikasi secara sinkron
     * Digunakan untuk Interceptor
     */
    fun getAuthTokenSync(): String? {
        return runBlocking {
            context.dataStore.data.first()[AUTH_TOKEN_KEY]
        }
    }
    
    /**
     * Mendapatkan role user sebagai Flow
     */
    fun getUserRole(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ROLE_KEY]
        }
    }
    
    /**
     * Mendapatkan email user sebagai Flow
     */
    fun getUserEmail(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_EMAIL_KEY]
        }
    }
    
    /**
     * Mendapatkan nama user sebagai Flow
     */
    fun getUserName(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_NAME_KEY]
        }
    }
    
    /**
     * Mendapatkan nama user secara sinkron
     */
    fun getUserNameSync(): String? {
        return runBlocking {
            context.dataStore.data.first()[USER_NAME_KEY]
        }
    }
    
    /**
     * Mendapatkan user_id sebagai Flow
     */
    fun getUserId(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ID_KEY]
        }
    }
    
    /**
     * Mendapatkan kelas_id untuk siswa sebagai Flow
     */
    fun getUserKelasId(): Flow<Int?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_KELAS_ID_KEY]?.toIntOrNull()
        }
    }
    
    /**
     * Menyimpan kelas_id untuk siswa
     */
    suspend fun saveUserKelasId(kelasId: Int) {
        context.dataStore.edit { preferences ->
            preferences[USER_KELAS_ID_KEY] = kelasId.toString()
        }
    }
    
    /**
     * Menghapus semua data sesi (Logout)
     */
    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
    
    /**
     * Menghapus sesi secara sinkron (untuk Interceptor)
     */
    fun clearAuthSync() {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences.clear()
            }
        }
    }
    
    /**
     * Mengecek apakah user sudah login
     */
    fun isLoggedIn(): Flow<Boolean> {
        return getAuthToken().map { token ->
            !token.isNullOrEmpty()
        }
    }
}
