package com.umar.aplikasimonitoringkelas.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umar.aplikasimonitoringkelas.MonitoringApplication
import com.umar.aplikasimonitoringkelas.data.api.ApiServiceProvider
import com.umar.aplikasimonitoringkelas.data.model.Resource
import com.umar.aplikasimonitoringkelas.data.session.SessionManager
import com.umar.aplikasimonitoringkelas.model.Siswa
import com.umar.aplikasimonitoringkelas.model.SiswaRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel untuk mengelola data siswa
 */
class SiswaViewModel : ViewModel() {
    
    companion object {
        private const val TAG = "SiswaViewModel"
    }
    
    private val apiService by lazy {
        Log.d(TAG, "Lazy initializing ApiService")
        ApiServiceProvider.getApiService()
    }
    
    private val sessionManager by lazy {
        Log.d(TAG, "Lazy initializing SessionManager")
        SessionManager(MonitoringApplication.instance.applicationContext)
    }
    
    // State untuk daftar siswa
    private val _siswaListState = MutableStateFlow<Resource<List<Siswa>>>(Resource.Loading)
    val siswaListState: StateFlow<Resource<List<Siswa>>> = _siswaListState.asStateFlow()
    
    // State untuk user role (RBAC)
    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole.asStateFlow()
    
    // State untuk pencarian
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    // State untuk filtered list (hasil pencarian)
    private val _filteredSiswaList = MutableStateFlow<List<Siswa>>(emptyList())
    val filteredSiswaList: StateFlow<List<Siswa>> = _filteredSiswaList.asStateFlow()
    
    // State untuk operasi CRUD (create, update, delete)
    private val _crudState = MutableStateFlow<CrudState>(CrudState.Idle)
    val crudState: StateFlow<CrudState> = _crudState.asStateFlow()
    
    init {
        loadUserRole()
        fetchSiswas()
    }
    
    /**
     * Memuat role user dari SessionManager
     */
    private fun loadUserRole() {
        viewModelScope.launch {
            try {
                val role = sessionManager.getUserRole().first()
                _userRole.value = role
            } catch (e: Exception) {
                _userRole.value = null
            }
        }
    }
    
    /**
     * Mengambil daftar siswa dari API
     */
    fun fetchSiswas() {
        viewModelScope.launch {
            try {
                _siswaListState.value = Resource.Loading
                
                val response = apiService.getSiswaList()
                
                if (response.isSuccessful) {
                    val siswaListResponse = response.body()
                    
                    if (siswaListResponse != null && siswaListResponse.success) {
                        val siswas = siswaListResponse.data ?: emptyList()
                        _siswaListState.value = Resource.Success(siswas)
                        _filteredSiswaList.value = siswas
                    } else {
                        val errorMsg = siswaListResponse?.message ?: "Gagal memuat data siswa"
                        _siswaListState.value = Resource.Error(errorMsg)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _siswaListState.value = Resource.Error("HTTP ${response.code()}: ${errorBody ?: "Unknown error"}")
                }
            } catch (e: Exception) {
                _siswaListState.value = Resource.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }
    
    /**
     * Melakukan pencarian siswa
     */
    fun searchSiswa(query: String) {
        _searchQuery.value = query
        
        val currentList = when (val state = _siswaListState.value) {
            is Resource.Success -> state.data
            else -> emptyList()
        }
        
        _filteredSiswaList.value = if (query.isEmpty()) {
            currentList
        } else {
            currentList.filter { siswa ->
                siswa.nama.contains(query, ignoreCase = true) ||
                siswa.nis.contains(query, ignoreCase = true) ||
                (siswa.nisn?.contains(query, ignoreCase = true) == true)
            }
        }
    }
    
    /**
     * Menambahkan siswa baru
     */
    fun createSiswa(request: SiswaRequest) {
        viewModelScope.launch {
            try {
                _crudState.value = CrudState.Loading
                
                val response = apiService.createSiswa(request)
                
                if (response.isSuccessful) {
                    val siswaResponse = response.body()
                    
                    if (siswaResponse != null && siswaResponse.success) {
                        _crudState.value = CrudState.Success(siswaResponse.message ?: "Siswa berhasil ditambahkan")
                        fetchSiswas()
                    } else {
                        _crudState.value = CrudState.Error(siswaResponse?.message ?: "Gagal menambahkan siswa")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _crudState.value = CrudState.Error("HTTP ${response.code()}: ${errorBody ?: "Unknown error"}")
                }
            } catch (e: Exception) {
                _crudState.value = CrudState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }
    
    /**
     * Mengupdate siswa
     */
    fun updateSiswa(id: Int, request: SiswaRequest) {
        viewModelScope.launch {
            try {
                _crudState.value = CrudState.Loading
                
                val response = apiService.updateSiswa(id, request)
                
                if (response.isSuccessful) {
                    val siswaResponse = response.body()
                    
                    if (siswaResponse != null && siswaResponse.success) {
                        _crudState.value = CrudState.Success(siswaResponse.message ?: "Siswa berhasil diupdate")
                        fetchSiswas()
                    } else {
                        _crudState.value = CrudState.Error(siswaResponse?.message ?: "Gagal mengupdate siswa")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _crudState.value = CrudState.Error("HTTP ${response.code()}: ${errorBody ?: "Unknown error"}")
                }
            } catch (e: Exception) {
                _crudState.value = CrudState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }
    
    /**
     * Menghapus siswa
     */
    fun deleteSiswa(id: Int) {
        viewModelScope.launch {
            try {
                _crudState.value = CrudState.Loading
                
                val response = apiService.deleteSiswa(id)
                
                if (response.isSuccessful) {
                    val siswaResponse = response.body()
                    
                    if (siswaResponse != null && siswaResponse.success) {
                        _crudState.value = CrudState.Success(siswaResponse.message ?: "Siswa berhasil dihapus")
                        fetchSiswas()
                    } else {
                        _crudState.value = CrudState.Error(siswaResponse?.message ?: "Gagal menghapus siswa")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _crudState.value = CrudState.Error("HTTP ${response.code()}: ${errorBody ?: "Unknown error"}")
                }
            } catch (e: Exception) {
                _crudState.value = CrudState.Error(e.message ?: "Terjadi kesalahan")
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
     * Cek apakah user memiliki akses write (RBAC)
     */
    fun hasWriteAccess(): Boolean {
        val role = _userRole.value
        return role == "admin" || role == "kurikulum"
    }
}
