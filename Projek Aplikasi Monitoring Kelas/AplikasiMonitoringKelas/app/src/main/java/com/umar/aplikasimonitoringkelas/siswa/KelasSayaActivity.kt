package com.umar.aplikasimonitoringkelas.siswa

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.api.ApiServiceProvider
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class KelasSayaActivity : AppCompatActivity() {
    
    private lateinit var tvNamaKelas: TextView
    private lateinit var tvKodeKelas: TextView
    private lateinit var tvWaliKelas: TextView
    private lateinit var tvJumlahSiswa: TextView
    private lateinit var tvHari: TextView
    private lateinit var rvJadwal: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutEmpty: View
    
    companion object {
        private const val TAG = "KelasSayaActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kelas_saya)
        
        setupToolbar()
        initViews()
        loadKelasSaya()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            title = "Kelas Saya"
            setDisplayHomeAsUpEnabled(true)
        }
    }
    
    private fun initViews() {
        tvNamaKelas = findViewById(R.id.tvNamaKelas)
        tvKodeKelas = findViewById(R.id.tvKodeKelas)
        tvWaliKelas = findViewById(R.id.tvWaliKelas)
        tvJumlahSiswa = findViewById(R.id.tvJumlahSiswa)
        tvHari = findViewById(R.id.tvHari)
        rvJadwal = findViewById(R.id.rvJadwal)
        progressBar = findViewById(R.id.progressBar)
        layoutEmpty = findViewById(R.id.layoutEmpty)
        
        rvJadwal.layoutManager = LinearLayoutManager(this)
    }
    
    private fun loadKelasSaya() {
        lifecycleScope.launch {
            try {
                progressBar.visibility = View.VISIBLE
                layoutEmpty.visibility = View.GONE
                rvJadwal.visibility = View.GONE
                
                val response = ApiServiceProvider.getApiService().getKelasSaya()
                
                if (response.isSuccessful && response.body()?.success == true) {
                    val data = response.body()?.data
                    
                    if (data != null) {
                        // Set kelas info
                        tvNamaKelas.text = data.kelas.namaKelas
                        tvKodeKelas.text = data.kelas.kodeKelas
                        tvWaliKelas.text = data.kelas.waliKelas ?: "-"
                        tvJumlahSiswa.text = "${data.kelas.jumlahSiswa ?: 0} Siswa"
                        tvHari.text = data.hari
                        
                        // Set jadwal
                        if (data.jadwalHariIni.isEmpty()) {
                            layoutEmpty.visibility = View.VISIBLE
                        } else {
                            rvJadwal.visibility = View.VISIBLE
                            val adapter = JadwalSayaAdapter(data.jadwalHariIni)
                            rvJadwal.adapter = adapter
                        }
                    }
                } else {
                    android.widget.Toast.makeText(this@KelasSayaActivity,
                        response.body()?.message ?: "Error loading kelas",
                        android.widget.Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading kelas: ${e.message}", e)
                android.widget.Toast.makeText(this@KelasSayaActivity,
                    "Error: ${e.message}",
                    android.widget.Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
