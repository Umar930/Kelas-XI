package com.umar.aplikasimonitoringkelas.kurikulum

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import android.widget.ImageButton
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.umar.aplikasimonitoringkelas.MainActivity
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.api.RetrofitClient
import com.umar.aplikasimonitoringkelas.data.model.KelasWithSiswa
import com.umar.aplikasimonitoringkelas.data.session.SessionManager
import com.umar.aplikasimonitoringkelas.kurikulum.fragment.DaftarGuruFragment
import com.umar.aplikasimonitoringkelas.kurikulum.fragment.GuruPenggantiFragment
import com.umar.aplikasimonitoringkelas.kurikulum.fragment.LaporanKurikulumFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class KurikulumDashboardActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var spinnerHari: AutoCompleteTextView
    private lateinit var spinnerKelas: AutoCompleteTextView
    private lateinit var spinnerGuru: AutoCompleteTextView
    private lateinit var tvUserInfo: TextView
    private lateinit var btnLogout: ImageButton

    private val scope = CoroutineScope(Dispatchers.Main)
    
    // Data
    private var kelasList: List<KelasWithSiswa> = emptyList()
    private var selectedHari: String = "Senin"
    private var selectedKelasId: Int = 0
    private var selectedGuruName: String = ""
    private var selectedKelasNama: String = ""

    private val hariList = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")

    companion object {
        private const val TAG = "KurikulumDashboard"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kurikulum_dashboard)

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
        spinnerGuru = findViewById(R.id.spinnerGuru)
        tvUserInfo = findViewById(R.id.tvUserInfo)
        btnLogout = findViewById(R.id.btnLogout)

        // Set user info
        val userName = sessionManager.getUserNameSync() ?: ""
        val role = runBlocking { sessionManager.getUserRole().first() } ?: "kurikulum"
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
                Toast.makeText(this@KurikulumDashboardActivity, "Berhasil logout", Toast.LENGTH_SHORT).show()
                
                val intent = Intent(this@KurikulumDashboardActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@KurikulumDashboardActivity, "Gagal logout: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Dashboard Kurikulum"
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
        // Setup Guru spinner will be loaded after kelas list is loaded
    }

    private fun setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_guru_pengganti -> {
                    showFragment(GuruPenggantiFragment.newInstance())
                    true
                }
                R.id.nav_daftar_guru -> {
                    showFragment(DaftarGuruFragment.newInstance())
                    true
                }
                R.id.nav_laporan -> {
                    showFragment(LaporanKurikulumFragment.newInstance())
                    true
                }
                else -> false
            }
        }

        // Default to Guru Pengganti tab
        bottomNavigation.selectedItemId = R.id.nav_guru_pengganti
    }

    private fun loadKelasList() {
        scope.launch {
            try {
                val apiService = RetrofitClient.getApiService(this@KurikulumDashboardActivity)
                val response = apiService.getSemuaKelas()

                if (response.isSuccessful && response.body()?.success == true) {
                    kelasList = response.body()?.data ?: emptyList()
                    
                    if (kelasList.isNotEmpty()) {
                        val kelasNames = kelasList.map { it.namaKelas }
                        val kelasAdapter = ArrayAdapter(
                            this@KurikulumDashboardActivity,
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
                    Toast.makeText(this@KurikulumDashboardActivity, "Gagal memuat daftar kelas", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading kelas: ${e.message}", e)
                Toast.makeText(this@KurikulumDashboardActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            // Also load guru list for the top filter
            loadGuruList()
        }
    }

    private fun loadGuruList() {
        scope.launch {
            try {
                val apiService = RetrofitClient.getApiService(this@KurikulumDashboardActivity)
                val response = apiService.getGuruList()
                if (response.isSuccessful && response.body()?.success == true) {
                    val guruData = response.body()?.data ?: emptyList()
                    // Deduplicate by kodeGuru and build labels like "kode - nama" (fall back to kode if nama missing)
                    val dedup = guruData.distinctBy { it.kodeGuru }
                    val guruLabels = dedup.map { g -> if (g.nama.isNullOrBlank()) (g.kodeGuru ?: "-") else "${g.kodeGuru ?: "-"} - ${g.nama}" }
                    val guruDisplay = listOf("Semua Guru") + guruLabels
                    val guruAdapter = ArrayAdapter(
                            this@KurikulumDashboardActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            guruDisplay
                        )
                    spinnerGuru.setAdapter(guruAdapter)
                    // Preselect previous selection if available
                    if (selectedGuruName.isNotBlank()) {
                        // find label starting with kode
                        val matchIndex = guruDisplay.indexOfFirst { it.startsWith(selectedGuruName) }
                        if (matchIndex >= 0) {
                            spinnerGuru.setText(guruDisplay[matchIndex], false)
                        } else {
                            spinnerGuru.setText("Semua Guru", false)
                        }
                    } else {
                        spinnerGuru.setText("Semua Guru", false)
                    }

                    spinnerGuru.setOnItemClickListener { _, _, position, _ ->
                        selectedGuruName = if (position == 0) "" else guruDisplay[position].substringBefore(" - ").trim()
                        Log.d(TAG, "Selected guru: $selectedGuruName")
                        refreshCurrentFragment()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading guru list: ${e.message}", e)
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
            is GuruPenggantiFragment -> currentFragment.refreshData(selectedHari, selectedKelasId)
            is DaftarGuruFragment -> currentFragment.refreshData(selectedHari, selectedKelasId, selectedGuruName)
            is LaporanKurikulumFragment -> currentFragment.refreshData()
        }
    }

    // Public getters for fragments
    fun getSelectedHari(): String = selectedHari
    fun getSelectedKelasId(): Int = selectedKelasId
    fun getSelectedKelasNama(): String = selectedKelasNama
    fun getSelectedGuruName(): String = selectedGuruName

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
