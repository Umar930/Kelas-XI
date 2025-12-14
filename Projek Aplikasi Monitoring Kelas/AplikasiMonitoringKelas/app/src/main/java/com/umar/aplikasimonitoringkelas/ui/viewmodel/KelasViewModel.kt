package com.umar.aplikasimonitoringkelas.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umar.aplikasimonitoringkelas.MonitoringApplication
import com.umar.aplikasimonitoringkelas.data.api.ApiServiceProvider
import com.umar.aplikasimonitoringkelas.data.model.Resource
import com.umar.aplikasimonitoringkelas.data.session.SessionManager
import com.umar.aplikasimonitoringkelas.model.Kelas
import com.umar.aplikasimonitoringkelas.model.KelasRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel untuk mengelola data kelas
 */
class KelasViewModel : ViewModel() {
    
    companion object {
        private const val TAG = "KelasViewModel"
    }
    
    private val apiService by lazy {
        Log.d(TAG, "Lazy initializing ApiService")
        ApiServiceProvider.getApiService()
    }
    
    private val sessionManager by lazy {
        Log.d(TAG, "Lazy initializing SessionManager")
        SessionManager(MonitoringApplication.instance.applicationContext)
    }
    
    // State untuk daftar kelas
    private val _kelasListState = MutableStateFlow<Resource<List<Kelas>>>(Resource.Loading)
    val kelasListState: StateFlow<Resource<List<Kelas>>> = _kelasListState.asStateFlow()
    
    // State untuk user role (RBAC)
    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole.asStateFlow()
    
    // State untuk pencarian
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    // State untuk filtered list (hasil pencarian)
    private val _filteredKelasList = MutableStateFlow<List<Kelas>>(emptyList())
    val filteredKelasList: StateFlow<List<Kelas>> = _filteredKelasList.asStateFlow()
    
    // State untuk operasi CRUD (create, update, delete)
    private val _crudState = MutableStateFlow<CrudState>(CrudState.Idle)
    val crudState: StateFlow<CrudState> = _crudState.asStateFlow()
    
    init {
        loadUserRole()
        fetchKelas()
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
     * Mengambil daftar kelas dari API
     */
    fun fetchKelas() {
        viewModelScope.launch {
            try {
                _kelasListState.value = Resource.Loading
                
                val response = apiService.getKelasList()
                
                if (response.isSuccessful) {
                    val kelasListResponse = response.body()
                    
                    if (kelasListResponse != null && kelasListResponse.success) {
                        val kelasList = kelasListResponse.data ?: emptyList()
                        _kelasListState.value = Resource.Success(kelasList)
                        _filteredKelasList.value = kelasList
                    } else {
                        val errorMsg = kelasListResponse?.message ?: "Gagal memuat data kelas"
                        _kelasListState.value = Resource.Error(errorMsg)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _kelasListState.value = Resource.Error("HTTP ${response.code()}: ${errorBody ?: "Unknown error"}")
                }
            } catch (e: Exception) {
                _kelasListState.value = Resource.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }
    
    /**
     * Melakukan pencarian kelas
     */
    fun searchKelas(query: String) {
        _searchQuery.value = query
        
        val currentList = when (val state = _kelasListState.value) {
            is Resource.Success -> state.data
            else -> emptyList()
        }
        
        _filteredKelasList.value = if (query.isEmpty()) {
            currentList
        } else {
            currentList.filter { kelas ->
                kelas.namaKelas.contains(query, ignoreCase = true) ||
                kelas.tingkat.contains(query, ignoreCase = true) ||
                (kelas.jurusan?.contains(query, ignoreCase = true) == true)
            }
        }
    }
    
    /**
     * Menambahkan kelas baru
     */
    fun createKelas(request: KelasRequest) {
        viewModelScope.launch {
            try {
                _crudState.value = CrudState.Loading
                
                val response = apiService.createKelas(request)
                
                if (response.isSuccessful) {
                    val kelasResponse = response.body()
                    
                    if (kelasResponse != null && kelasResponse.success) {
                        _crudState.value = CrudState.Success(kelasResponse.message ?: "Kelas berhasil ditambahkan")
                        fetchKelas()
                    } else {
                        _crudState.value = CrudState.Error(kelasResponse?.message ?: "Gagal menambahkan kelas")
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
     * Mengupdate kelas
     */
    fun updateKelas(id: Int, request: KelasRequest) {
        viewModelScope.launch {
            try {
                _crudState.value = CrudState.Loading
                
                val response = apiService.updateKelas(id, request)
                
                if (response.isSuccessful) {
                    val kelasResponse = response.body()
                    
                    if (kelasResponse != null && kelasResponse.success) {
                        _crudState.value = CrudState.Success(kelasResponse.message ?: "Kelas berhasil diupdate")
                        fetchKelas()
                    } else {
                        _crudState.value = CrudState.Error(kelasResponse?.message ?: "Gagal mengupdate kelas")
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
     * Menghapus kelas
     */
    fun deleteKelas(id: Int) {
        viewModelScope.launch {
            try {
                _crudState.value = CrudState.Loading
                
                val response = apiService.deleteKelas(id)
                
                if (response.isSuccessful) {
                    val kelasResponse = response.body()
                    
                    if (kelasResponse != null && kelasResponse.success) {
                        _crudState.value = CrudState.Success(kelasResponse.message ?: "Kelas berhasil dihapus")
                        fetchKelas()
                    } else {
                        _crudState.value = CrudState.Error(kelasResponse?.message ?: "Gagal menghapus kelas")
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
