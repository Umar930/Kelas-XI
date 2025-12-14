package com.umar.aplikasimonitoringkelas.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umar.aplikasimonitoringkelas.MonitoringApplication
import com.umar.aplikasimonitoringkelas.data.api.ApiServiceProvider
import com.umar.aplikasimonitoringkelas.data.model.Resource
import com.umar.aplikasimonitoringkelas.data.session.SessionManager
import com.umar.aplikasimonitoringkelas.model.Guru
import com.umar.aplikasimonitoringkelas.model.GuruRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel untuk mengelola data guru
 */
class GuruViewModel : ViewModel() {
    
    companion object {
        private const val TAG = "GuruViewModel"
    }
    
    // Lazy initialization untuk mencegah error saat Application belum siap
    private val apiService by lazy {
        try {
            Log.d(TAG, "Lazy initializing ApiService")
            ApiServiceProvider.getApiService()
        } catch (e: Exception) {
            Log.e(TAG, "FATAL: Failed to initialize ApiService", e)
            throw IllegalStateException("Cannot initialize ApiService. Please restart the app.", e)
        }
    }
    
    private val sessionManager by lazy {
        try {
            Log.d(TAG, "Lazy initializing SessionManager")
            SessionManager(MonitoringApplication.instance.applicationContext)
        } catch (e: Exception) {
            Log.e(TAG, "FATAL: Failed to initialize SessionManager", e)
            throw IllegalStateException("Cannot initialize SessionManager. Please restart the app.", e)
        }
    }
    
    // State untuk daftar guru
    private val _guruListState = MutableStateFlow<Resource<List<Guru>>>(Resource.Loading)
    val guruListState: StateFlow<Resource<List<Guru>>> = _guruListState.asStateFlow()
    
    // State untuk user role (RBAC)
    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole.asStateFlow()
    
    // State untuk pencarian
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    // State untuk filtered list (hasil pencarian)
    private val _filteredGuruList = MutableStateFlow<List<Guru>>(emptyList())
    val filteredGuruList: StateFlow<List<Guru>> = _filteredGuruList.asStateFlow()
    
    // State untuk operasi CRUD (create, update, delete)
    private val _crudState = MutableStateFlow<CrudState>(CrudState.Idle)
    val crudState: StateFlow<CrudState> = _crudState.asStateFlow()
    
    init {
        Log.d(TAG, "GuruViewModel created, starting initialization")
        try {
            // Load user role saat ViewModel dibuat
            loadUserRole()
            // Load data guru
            fetchGurus()
        } catch (e: Exception) {
            Log.e(TAG, "Error during ViewModel initialization", e)
            _guruListState.value = Resource.Error("Initialization error: ${e.message}")
        }
    }
    
    /**
     * Memuat role user dari SessionManager
     */
    private fun loadUserRole() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Loading user role...")
                val role = sessionManager.getUserRole().first()
                Log.d(TAG, "User role loaded: $role")
                _userRole.value = role
            } catch (e: Exception) {
                Log.e(TAG, "Error loading user role", e)
                _userRole.value = null
            }
        }
    }
    
    /**
     * Mengambil daftar guru dari API
     */
    fun fetchGurus() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "=== FETCH GURU START ===")
                Log.d(TAG, "Memulai fetch data guru...")
                
                // Set state ke Loading
                _guruListState.value = Resource.Loading
                
                // Panggil API
                val response = apiService.getGuruList()
                
                Log.d(TAG, "Response received:")
                Log.d(TAG, "- isSuccessful: ${response.isSuccessful}")
                Log.d(TAG, "- code: ${response.code()}")
                Log.d(TAG, "- message: ${response.message()}")
                
                // Cek response
                if (response.isSuccessful) {
                    val guruListResponse = response.body()
                    Log.d(TAG, "Response body:")
                    Log.d(TAG, "- success: ${guruListResponse?.success}")
                    Log.d(TAG, "- message: ${guruListResponse?.message}")
                    Log.d(TAG, "- data size: ${guruListResponse?.data?.size}")
                    
                    if (guruListResponse?.success == true) {
                        val guruList = guruListResponse.data ?: emptyList()
                        
                        // Cek apakah list kosong
                        if (guruList.isEmpty()) {
                            Log.d(TAG, "Data guru kosong")
                            _guruListState.value = Resource.Empty
                            _filteredGuruList.value = emptyList()
                        } else {
                            Log.d(TAG, "✓ Data guru berhasil dimuat: ${guruList.size} item")
                            // Filter dan log hanya guru yang valid (nama tidak null)
                            val validGuruList = guruList.filter { it.nama != null && it.kodeGuru != null }
                            Log.d(TAG, "Valid guru count: ${validGuruList.size}")
                            
                            validGuruList.forEachIndexed { index, guru ->
                                Log.d(TAG, "  [$index] ${guru.nama} (${guru.kodeGuru})")
                            }
                            
                            if (validGuruList.isEmpty()) {
                                Log.w(TAG, "All guru data invalid (null nama or kodeGuru)")
                                _guruListState.value = Resource.Error("Data guru tidak valid")
                                _filteredGuruList.value = emptyList()
                            } else {
                                _guruListState.value = Resource.Success(validGuruList)
                                // Set filtered list dengan data yang valid
                                _filteredGuruList.value = validGuruList
                            }
                        }
                    } else {
                        val errorMsg = guruListResponse?.message ?: "Gagal memuat data guru"
                        Log.e(TAG, "API Error: $errorMsg")
                        _guruListState.value = Resource.Error(errorMsg)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = when (response.code()) {
                        401 -> "Unauthorized - Token mungkin expired atau tidak valid"
                        403 -> "Forbidden - Tidak memiliki akses"
                        404 -> "Not Found - Endpoint tidak ditemukan"
                        500 -> "Server Error - ${errorBody ?: response.message()}"
                        else -> "HTTP ${response.code()}: ${errorBody ?: response.message()}"
                    }
                    Log.e(TAG, "HTTP Error: $errorMsg")
                    Log.e(TAG, "Error body: $errorBody")
                    _guruListState.value = Resource.Error(errorMsg)
                }
                Log.d(TAG, "=== FETCH GURU END ===")
            } catch (e: com.google.gson.JsonSyntaxException) {
                val errorMsg = "Error parsing data: Data dari server tidak sesuai format"
                Log.e(TAG, "GSON Parse Error:", e)
                Log.e(TAG, "This usually means the API response structure doesn't match the model")
                _guruListState.value = Resource.Error(errorMsg)
                _filteredGuruList.value = emptyList()
            } catch (e: java.net.UnknownHostException) {
                val errorMsg = "Tidak dapat terhubung ke server. Periksa koneksi internet."
                Log.e(TAG, "Network Error - Unknown Host:", e)
                _guruListState.value = Resource.Error(errorMsg)
                _filteredGuruList.value = emptyList()
            } catch (e: java.net.SocketTimeoutException) {
                val errorMsg = "Koneksi timeout. Server tidak merespons."
                Log.e(TAG, "Network Error - Timeout:", e)
                _guruListState.value = Resource.Error(errorMsg)
                _filteredGuruList.value = emptyList()
            } catch (e: retrofit2.HttpException) {
                val errorMsg = "HTTP Error ${e.code()}: ${e.message()}"
                Log.e(TAG, "HTTP Exception:", e)
                _guruListState.value = Resource.Error(errorMsg)
                _filteredGuruList.value = emptyList()
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Terjadi kesalahan saat memuat data"
                Log.e(TAG, "=== EXCEPTION IN FETCH GURU ===")
                Log.e(TAG, "Exception type: ${e.javaClass.name}")
                Log.e(TAG, "Exception message: $errorMsg")
                Log.e(TAG, "Stack trace:", e)
                e.printStackTrace()
                _guruListState.value = Resource.Error("Error: $errorMsg")
                _filteredGuruList.value = emptyList()
            } finally {
                Log.d(TAG, "=== FETCH GURU FINALLY ===")
            }
        }
    }
    
    /**
     * Mencari guru berdasarkan query
     * Filter dilakukan di client-side
     */
    fun searchGurus(query: String) {
        _searchQuery.value = query
        
        viewModelScope.launch {
            try {
                // Ambil data dari state saat ini
                val currentState = _guruListState.value
                
                if (currentState is Resource.Success) {
                    val allGurus = currentState.data
                    
                    // Filter berdasarkan query
                    val filtered = if (query.isBlank()) {
                        allGurus
                    } else {
                        allGurus.filter { guru ->
                            guru.nama?.contains(query, ignoreCase = true) == true ||
                            guru.kodeGuru?.contains(query, ignoreCase = true) == true ||
                            guru.mataPelajaran?.contains(query, ignoreCase = true) == true
                        }
                    }
                    
                    _filteredGuruList.value = filtered
                }
            } catch (e: Exception) {
                // Log error jika perlu
            }
        }
    }
    
    /**
     * Menambah guru baru
     */
    fun createGuru(request: GuruRequest) {
        viewModelScope.launch {
            try {
                _crudState.value = CrudState.Loading
                
                val response = apiService.createGuru(request)
                
                if (response.isSuccessful) {
                    val guruResponse = response.body()
                    if (guruResponse?.success == true) {
                        _crudState.value = CrudState.Success("Guru berhasil ditambahkan")
                        // Refresh data
                        fetchGurus()
                    } else {
                        _crudState.value = CrudState.Error(
                            guruResponse?.message ?: "Gagal menambahkan guru"
                        )
                    }
                } else {
                    _crudState.value = CrudState.Error(
                        "HTTP Error: ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                _crudState.value = CrudState.Error(
                    e.message ?: "Terjadi kesalahan"
                )
            }
        }
    }
    
    /**
     * Mengupdate data guru
     */
    fun updateGuru(id: Int, request: GuruRequest) {
        viewModelScope.launch {
            try {
                _crudState.value = CrudState.Loading
                
                val response = apiService.updateGuru(id, request)
                
                if (response.isSuccessful) {
                    val guruResponse = response.body()
                    if (guruResponse?.success == true) {
                        _crudState.value = CrudState.Success("Guru berhasil diupdate")
                        // Refresh data
                        fetchGurus()
                    } else {
                        _crudState.value = CrudState.Error(
                            guruResponse?.message ?: "Gagal mengupdate guru"
                        )
                    }
                } else {
                    _crudState.value = CrudState.Error(
                        "HTTP Error: ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                _crudState.value = CrudState.Error(
                    e.message ?: "Terjadi kesalahan"
                )
            }
        }
    }
    
    /**
     * Menghapus guru
     */
    fun deleteGuru(id: Int) {
        viewModelScope.launch {
            try {
                _crudState.value = CrudState.Loading
                
                val response = apiService.deleteGuru(id)
                
                if (response.isSuccessful) {
                    val guruResponse = response.body()
                    if (guruResponse?.success == true) {
                        _crudState.value = CrudState.Success("Guru berhasil dihapus")
                        // Refresh data
                        fetchGurus()
                    } else {
                        _crudState.value = CrudState.Error(
                            guruResponse?.message ?: "Gagal menghapus guru"
                        )
                    }
                } else {
                    _crudState.value = CrudState.Error(
                        "HTTP Error: ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                _crudState.value = CrudState.Error(
                    e.message ?: "Terjadi kesalahan"
                )
            }
        }
    }
    
    /**
     * Reset CRUD state
     */
    fun resetCrudState() {
        _crudState.value = CrudState.Idle
    }
    
    /**
     * Cek apakah user memiliki akses untuk CRUD
     * Hanya Admin dan Kurikulum yang bisa CRUD
     */
    fun hasWriteAccess(): Boolean {
        val role = _userRole.value?.lowercase() ?: ""
        return role == "admin" || role == "kurikulum"
    }
}

/**
 * Sealed class untuk state operasi CRUD
 */
sealed class CrudState {
    object Idle : CrudState()
    object Loading : CrudState()
    data class Success(val message: String) : CrudState()
    data class Error(val message: String) : CrudState()
}
