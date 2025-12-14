package com.umar.aplikasimonitoringkelas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.ImageButton
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.umar.aplikasimonitoringkelas.data.api.RetrofitClient
import com.umar.aplikasimonitoringkelas.data.model.KelasWithSiswa
import com.umar.aplikasimonitoringkelas.data.session.SessionManager
import com.umar.aplikasimonitoringkelas.siswa.fragment.EntriKehadiranFragment
import com.umar.aplikasimonitoringkelas.siswa.fragment.JadwalSiswaFragment
import com.umar.aplikasimonitoringkelas.siswa.fragment.ListGuruHadirFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SiswaDashboardActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var spinnerHari: AutoCompleteTextView
    private lateinit var spinnerKelas: AutoCompleteTextView
    private lateinit var tvUserInfo: TextView
    private lateinit var btnLogout: ImageButton

    private val scope = CoroutineScope(Dispatchers.Main)
    
    // Data
    private var kelasList: List<KelasWithSiswa> = emptyList()
    private var selectedHari: String = "Senin"
    private var selectedKelasId: Int = 0
    private var selectedKelasNama: String = ""

    private val hariList = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")

    companion object {
        private const val TAG = "SiswaDashboardActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_siswa_dashboard)

        sessionManager = SessionManager.getInstance(this)

        initViews()
        setupToolbar()
        setupDropdowns()
        setupBottomNavigation()
        loadKelasList()
    }

    private fun initViews() {
        bottomNavigation = findViewById(R.id.bottomNavigation)
        spinnerHari = findViewById(R.id.spinnerHari)
        spinnerKelas = findViewById(R.id.spinnerKelas)
        tvUserInfo = findViewById(R.id.tvUserInfo)
        btnLogout = findViewById(R.id.btnLogout)

        // Set user info - menggunakan sync method and get role
        val userName = sessionManager.getUserNameSync() ?: ""
        val role = runBlocking { sessionManager.getUserRole().first() } ?: "siswa"
        val roleDisplay = role.replace("_", " ").split(" ").joinToString(" ") { it.replaceFirstChar { ch -> ch.uppercase() } }
        val displayName = if (userName.isNotBlank() && userName.startsWith(roleDisplay, ignoreCase = true)) userName else "$roleDisplay ${userName}".trim()
        tvUserInfo.text = "Login sebagai: $displayName"
        
        // Setup logout button
        btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }
    }
    
    private fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin keluar?")
            .setPositiveButton("Ya") { _, _ ->
                performLogout()
            }
            .setNegativeButton("Tidak", null)
            .show()
    }
    
    private fun performLogout() {
        scope.launch {
            try {
                sessionManager.clearSession()
                Toast.makeText(this@SiswaDashboardActivity, "Berhasil logout", Toast.LENGTH_SHORT).show()
                
                // Navigate to login (MainActivity handles login with Compose)
                val intent = Intent(this@SiswaDashboardActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@SiswaDashboardActivity, "Gagal logout: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Dashboard Siswa"
    }

    private fun setupDropdowns() {
        // Setup Hari dropdown
        val hariAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, hariList)
        spinnerHari.setAdapter(hariAdapter)
        spinnerHari.setText(selectedHari, false)
        
        spinnerHari.setOnItemClickListener { _, _, position, _ ->
            selectedHari = hariList[position]
            Log.d(TAG, "Selected hari: $selectedHari")
            refreshCurrentFragment()
        }
    }

    private fun setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_jadwal -> {
                    showFragment(JadwalSiswaFragment.newInstance())
                    true
                }
                R.id.nav_entri -> {
                    showFragment(EntriKehadiranFragment.newInstance())
                    true
                }
                R.id.nav_list -> {
                    showFragment(ListGuruHadirFragment.newInstance())
                    true
                }
                else -> false
            }
        }

        // Default to Jadwal tab
        bottomNavigation.selectedItemId = R.id.nav_jadwal
    }

    private fun loadKelasList() {
        scope.launch {
            try {
                val apiService = RetrofitClient.getApiService(this@SiswaDashboardActivity)
                val response = apiService.getSemuaKelas()

                if (response.isSuccessful && response.body()?.success == true) {
                    kelasList = response.body()?.data ?: emptyList()
                    
                    if (kelasList.isNotEmpty()) {
                        val kelasNames = kelasList.map { it.namaKelas }
                        val kelasAdapter = ArrayAdapter(
                            this@SiswaDashboardActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            kelasNames
                        )
                        spinnerKelas.setAdapter(kelasAdapter)
                        
                        // Set default kelas
                        selectedKelasId = kelasList[0].id
                        selectedKelasNama = kelasList[0].namaKelas
                        spinnerKelas.setText(selectedKelasNama, false)
                        
                        spinnerKelas.setOnItemClickListener { _, _, position, _ ->
                            selectedKelasId = kelasList[position].id
                            selectedKelasNama = kelasList[position].namaKelas
                            Log.d(TAG, "Selected kelas: $selectedKelasNama (ID: $selectedKelasId)")
                            refreshCurrentFragment()
                        }
                        
                        // Refresh fragment with initial data
                        refreshCurrentFragment()
                    }
                } else {
                    Toast.makeText(this@SiswaDashboardActivity, "Gagal memuat daftar kelas", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading kelas: ${e.message}", e)
                Toast.makeText(this@SiswaDashboardActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun refreshCurrentFragment() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        
        when (currentFragment) {
            is JadwalSiswaFragment -> currentFragment.refreshData(selectedHari, selectedKelasId)
            is EntriKehadiranFragment -> currentFragment.refreshData(selectedHari, selectedKelasId)
            is ListGuruHadirFragment -> currentFragment.refreshData(selectedHari, selectedKelasId)
        }
    }

    // Public getters for fragments
    fun getSelectedHari(): String = selectedHari
    fun getSelectedKelasId(): Int = selectedKelasId
    fun getSelectedKelasNama(): String = selectedKelasNama

    fun logout() {
        scope.launch {
            sessionManager.clearSession()
            Toast.makeText(this@SiswaDashboardActivity, "Berhasil logout", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@SiswaDashboardActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
