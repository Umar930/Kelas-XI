package com.umar.aplikasimonitoringkelas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.umar.aplikasimonitoringkelas.data.api.RetrofitClient
import com.umar.aplikasimonitoringkelas.data.model.KelasWithSiswa
import com.umar.aplikasimonitoringkelas.data.session.SessionManager
import com.umar.aplikasimonitoringkelas.siswa.KelasPilihanAdapter
import com.umar.aplikasimonitoringkelas.siswa.fragment.InputKehadiranFragment
import com.umar.aplikasimonitoringkelas.siswa.fragment.LaporanKehadiranFragment
import com.umar.aplikasimonitoringkelas.siswa.fragment.RequestPenggantiFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SiswaActivityNew : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var sessionManager: SessionManager
    
    // Views for class selection
    private lateinit var layoutKelasSelection: LinearLayout
    private lateinit var rvDaftarKelas: RecyclerView
    private lateinit var progressBarKelas: ProgressBar
    
    // Views for main content
    private lateinit var layoutMainContent: ConstraintLayout
    private lateinit var tvSelectedKelas: TextView
    private lateinit var tvSelectedKelasInfo: TextView
    private lateinit var btnChangeKelas: Button
    private lateinit var bottomNavigation: BottomNavigationView
    
    private val scope = CoroutineScope(Dispatchers.Main)
    private var selectedKelas: KelasWithSiswa? = null
    
    companion object {
        private const val TAG = "SiswaActivityNew"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_siswa_with_bottom_nav)

        sessionManager = SessionManager.getInstance(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Dashboard Siswa"

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        initViews()
        loadSemuaKelas()
    }

    private fun initViews() {
        // Class selection views
        layoutKelasSelection = findViewById(R.id.layoutKelasSelection)
        rvDaftarKelas = findViewById(R.id.rvDaftarKelas)
        progressBarKelas = findViewById(R.id.progressBarKelas)
        
        // Main content views
        layoutMainContent = findViewById(R.id.layoutMainContent)
        tvSelectedKelas = findViewById(R.id.tvSelectedKelas)
        tvSelectedKelasInfo = findViewById(R.id.tvSelectedKelasInfo)
        btnChangeKelas = findViewById(R.id.btnChangeKelas)
        bottomNavigation = findViewById(R.id.bottomNavigation)
        
        // RecyclerView setup
        rvDaftarKelas.layoutManager = GridLayoutManager(this, 2)
        
        // Button listeners
        btnChangeKelas.setOnClickListener {
            showKelasSelection()
        }
        
        // Bottom navigation listener
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_input_kehadiran -> {
                    showFragment(InputKehadiranFragment.newInstance(
                        selectedKelas?.id ?: 0,
                        selectedKelas?.namaKelas ?: ""
                    ))
                    true
                }
                R.id.nav_laporan_kehadiran -> {
                    showFragment(LaporanKehadiranFragment.newInstance(
                        selectedKelas?.id ?: 0,
                        selectedKelas?.namaKelas ?: ""
                    ))
                    true
                }
                R.id.nav_request_pengganti -> {
                    showFragment(RequestPenggantiFragment.newInstance(
                        selectedKelas?.id ?: 0,
                        selectedKelas?.namaKelas ?: ""
                    ))
                    true
                }
                else -> false
            }
        }
    }

    private fun loadSemuaKelas() {
        progressBarKelas.visibility = View.VISIBLE
        scope.launch {
            try {
                val apiService = RetrofitClient.getApiService(this@SiswaActivityNew)
                val response = apiService.getSemuaKelas()
                
                if (response.isSuccessful && response.body()?.success == true) {
                    val kelasList = response.body()?.data ?: emptyList()
                    rvDaftarKelas.adapter = KelasPilihanAdapter(kelasList) { kelas ->
                        onKelasSelected(kelas)
                    }
                } else {
                    Toast.makeText(this@SiswaActivityNew, "Gagal memuat daftar kelas", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SiswaActivityNew, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBarKelas.visibility = View.GONE
            }
        }
    }

    private fun onKelasSelected(kelas: KelasWithSiswa) {
        selectedKelas = kelas
        
        Log.d(TAG, "Kelas selected: ${kelas.namaKelas}, ID: ${kelas.id}")
        
        // Update UI
        tvSelectedKelas.text = kelas.namaKelas
        tvSelectedKelasInfo.text = kelas.kodeKelas
        
        Log.d(TAG, "Hiding class selection, showing main content")
        
        // Show main content with bottom nav
        layoutKelasSelection.visibility = View.GONE
        layoutMainContent.visibility = View.VISIBLE
        
        Log.d(TAG, "layoutMainContent visibility: ${layoutMainContent.visibility}")
        Log.d(TAG, "bottomNavigation visibility: ${bottomNavigation.visibility}")
        
        // Load default fragment (Input Kehadiran)
        Log.d(TAG, "Setting bottom nav selected item to Input Kehadiran")
        bottomNavigation.selectedItemId = R.id.nav_input_kehadiran
    }

    private fun showKelasSelection() {
        layoutMainContent.visibility = View.GONE
        layoutKelasSelection.visibility = View.VISIBLE
        selectedKelas = null
    }

    private fun showFragment(fragment: Fragment) {
        Log.d(TAG, "showFragment called with: ${fragment::class.java.simpleName}")
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
        Log.d(TAG, "Fragment transaction committed")
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
                scope.launch {
                    sessionManager.clearSession()
                    Toast.makeText(this@SiswaActivityNew, "Berhasil logout", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SiswaActivityNew, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else if (layoutMainContent.visibility == View.VISIBLE) {
            // Go back to class selection
            showKelasSelection()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
