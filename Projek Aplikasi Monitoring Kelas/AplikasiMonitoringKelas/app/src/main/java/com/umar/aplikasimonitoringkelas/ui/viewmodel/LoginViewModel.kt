package com.umar.aplikasimonitoringkelas.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umar.aplikasimonitoringkelas.data.api.RetrofitClient
import com.umar.aplikasimonitoringkelas.data.model.LoginRequest
import com.umar.aplikasimonitoringkelas.data.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel untuk menangani logika autentikasi
 */
class LoginViewModel(private val context: Context) : ViewModel() {
    
    private val apiService = RetrofitClient.createApiService(context)
    private val sessionManager = SessionManager(context)
    
    // State untuk UI
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState
    
    companion object {
        private const val TAG = "LoginViewModel"
    }
    
    /**
     * Fungsi untuk melakukan login
     */
    fun login(email: String, password: String, role: String) {
        viewModelScope.launch {
            try {
                // Set state ke Loading
                _loginState.value = LoginState.Loading
                
                // Log request data untuk debugging
                Log.d(TAG, "=== LOGIN REQUEST ===")
                Log.d(TAG, "Email: $email")
                Log.d(TAG, "Password: ${password.take(3)}***") // Hanya 3 karakter pertama
                Log.d(TAG, "Role: $role")
                
                // Panggil API login
                val request = LoginRequest(email, password, role)
                val response = apiService.login(request)
                
                Log.d(TAG, "=== LOGIN RESPONSE ===")
                Log.d(TAG, "Response Code: ${response.code()}")
                Log.d(TAG, "Response isSuccessful: ${response.isSuccessful}")
                
                // Cek response
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    
                    Log.d(TAG, "Response body: $loginResponse")
                    Log.d(TAG, "Response success: ${loginResponse?.success}")
                    Log.d(TAG, "Response message: ${loginResponse?.message}")
                    Log.d(TAG, "Response data: ${loginResponse?.data}")
                    
                    if (loginResponse?.success == true) {
                        // Ambil token dan user dari data wrapper
                        val token = loginResponse.data?.token
                        val userData = loginResponse.data?.user
                        
                        // Simpan token jika ada
                        token?.let {
                            Log.d(TAG, "Saving token: $it")
                            sessionManager.saveAuthToken(it)
                        }
                        
                        // Simpan data user jika ada
                        userData?.let { user ->
                            Log.d(TAG, "Saving user: ${user.email}, role: ${user.role}, name: ${user.name}, id: ${user.id}")
                            
                            // Tentukan user_id berdasarkan role
                            val userId = when (user.role.lowercase()) {
                                "siswa" -> user.siswaId?.toString() ?: user.id.toString()
                                "guru" -> user.guruId?.toString() ?: user.id.toString()
                                else -> user.id.toString()
                            }
                            
                            sessionManager.saveUserData(
                                role = user.role,
                                email = user.email,
                                name = user.name ?: user.email,
                                userId = userId
                            )
                        }
                        
                        // Set state ke Success
                        _loginState.value = LoginState.Success(userData?.role ?: role)
                    } else {
                        // Login gagal
                        _loginState.value = LoginState.Error(
                            loginResponse?.message ?: "Login gagal"
                        )
                    }
                } else {
                    // HTTP error
                    val errorBody = response.errorBody()?.string()
                    Log.e(TAG, "=== LOGIN ERROR ===")
                    Log.e(TAG, "HTTP Error Code: ${response.code()}")
                    Log.e(TAG, "HTTP Error Message: ${response.message()}")
                    Log.e(TAG, "Error Body: $errorBody")
                    
                    val errorMessage = when (response.code()) {
                        500 -> "Server error (500). Server sedang bermasalah, silakan coba lagi."
                        401 -> "Email atau password salah"
                        422 -> {
                                // Log detail untuk error 422
                                Log.e(TAG, "Validation Error 422 - Request data:")
                                Log.e(TAG, "Email yang dikirim: $email")
                                Log.e(TAG, "Role yang dikirim: $role")

                                // Try to extract server validation messages from errorBody
                                if (!errorBody.isNullOrEmpty()) {
                                    try {
                                        val json = org.json.JSONObject(errorBody)
                                        // Common Laravel style: { "message": "The given data was invalid.", "errors": { "email": ["..."], ... } }
                                        val sb = StringBuilder()
                                        if (json.has("message")) sb.append(json.optString("message")).append("\n")
                                        if (json.has("errors")) {
                                            val errors = json.getJSONObject("errors")
                                            val keys = errors.keys()
                                            while (keys.hasNext()) {
                                                val key = keys.next()
                                                val arr = errors.optJSONArray(key)
                                                if (arr != null && arr.length() > 0) {
                                                    sb.append("${key}: ${arr.optString(0)}\n")
                                                }
                                            }
                                        }
                                        val parsedMsg = sb.toString().trim()
                                        if (parsedMsg.isNotEmpty()) parsedMsg else "Data tidak valid. Periksa kembali inputan Anda.\nEmail: $email\nRole: $role"
                                    } catch (je: Exception) {
                                        Log.e(TAG, "Failed parse 422 body: ${je.message}", je)
                                        "Data tidak valid. Periksa kembali inputan Anda.\nEmail: $email\nRole: $role"
                                    }
                                } else {
                                    "Data tidak valid. Periksa kembali inputan Anda.\nEmail: $email\nRole: $role"
                                }
                            }
                        404 -> "Endpoint tidak ditemukan. Hubungi administrator."
                        else -> "Login gagal (${response.code()}): ${response.message()}"
                    }
                    
                    _loginState.value = LoginState.Error(errorMessage)
                }
            } catch (e: Exception) {
                // Log error untuk debugging
                Log.e(TAG, "Login error: ${e.message}", e)
                Log.e(TAG, "Login error: ${e.message}", e)
                
                // Handle error dengan detail
                val errorMessage = when {
                    e.message?.contains("Expected BEGIN_OBJECT") == true -> 
                        "Format response API tidak sesuai. Hubungi administrator."
                    e.message?.contains("Unable to resolve host") == true -> 
                        "Tidak dapat terhubung ke server. Periksa koneksi internet."
                    e.message?.contains("timeout") == true -> 
                        "Koneksi timeout. Server tidak merespon."
                    else -> e.message ?: "Terjadi kesalahan"
                }
                
                _loginState.value = LoginState.Error(errorMessage)
            }
        }
    }
    
    /**
     * Fungsi untuk logout
     */
    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                // Hapus semua data sesi
                sessionManager.clearSession()
                
                // Reset state
                _loginState.value = LoginState.Idle
                
                // Callback setelah logout
                onLogoutComplete()
            } catch (e: Exception) {
                // Handle error jika ada
                _loginState.value = LoginState.Error(
                    e.message ?: "Gagal logout"
                )
            }
        }
    }
    
    /**
     * Reset state ke Idle
     */
    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}

/**
 * Sealed class untuk state login
 */
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val role: String) : LoginState()
    data class Error(val message: String) : LoginState()
}
