package com.umar.aplikasimonitoringkelas

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umar.aplikasimonitoringkelas.adapter.JadwalKelasKepsekAdapter
import com.umar.aplikasimonitoringkelas.data.api.ApiService
import com.umar.aplikasimonitoringkelas.data.api.RetrofitClient
import com.umar.aplikasimonitoringkelas.data.model.*
import com.umar.aplikasimonitoringkelas.data.session.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class JadwalKelasActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiService: ApiService
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JadwalKelasKepsekAdapter
    private lateinit var spinnerKelas: Spinner
    private lateinit var spinnerHari: Spinner
    private lateinit var btnFilter: Button
    private lateinit var progressBar: ProgressBar

    private val scope = CoroutineScope(Dispatchers.Main)
    private var kelasList = listOf<KelasWithSiswa>()
    private val hariList = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jadwal_kelas)

        sessionManager = SessionManager.getInstance(this)
        apiService = RetrofitClient.getApiService(this)

        initViews()
        loadSemuaKelas()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.rvJadwalKelas)
        spinnerKelas = findViewById(R.id.spinnerKelas)
        spinnerHari = findViewById(R.id.spinnerHari)
        btnFilter = findViewById(R.id.btnFilter)
        progressBar = findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val hariAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, hariList)
        hariAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerHari.adapter = hariAdapter

        btnFilter.setOnClickListener {
            val selectedKelas = spinnerKelas.selectedItem as? String
            val selectedHari = spinnerHari.selectedItem as? String
            if (selectedKelas != null && selectedHari != null) {
                loadJadwalKelas(selectedKelas, selectedHari)
            }
        }
    }

    private fun loadSemuaKelas() {
        progressBar.visibility = View.VISIBLE
        scope.launch {
            try {
                val response = apiService.getSemuaKelas()
                
                if (response.isSuccessful && response.body()?.success == true) {
                    kelasList = response.body()?.data ?: emptyList()
                    
                    val adapter = ArrayAdapter(
                        this@JadwalKelasActivity,
                        android.R.layout.simple_spinner_item,
                        kelasList.map { it.namaKelas }
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerKelas.adapter = adapter
                } else {
                    Toast.makeText(this@JadwalKelasActivity, "Gagal memuat data kelas", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@JadwalKelasActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun loadJadwalKelas(namaKelas: String, hari: String) {
        progressBar.visibility = View.VISIBLE
        scope.launch {
            try {
                val selectedKelas = kelasList.find { it.namaKelas == namaKelas }
                val response = apiService.getJadwalKelas(
                    kelasId = selectedKelas?.id,
                    hari = hari
                )
                
                if (response.isSuccessful && response.body()?.success == true) {
                    val jadwalDataList = response.body()?.data ?: emptyList()
                    // Extract JadwalKelas from JadwalKelasData
                    val jadwalList = jadwalDataList.flatMap { jadwalData ->
                        jadwalData.jadwal.flatMap { it.mataPelajaran }
                    }
                    adapter = JadwalKelasKepsekAdapter(jadwalList)
                    recyclerView.adapter = adapter
                } else {
                    Toast.makeText(this@JadwalKelasActivity, "Tidak ada jadwal", Toast.LENGTH_SHORT).show()
                    adapter = JadwalKelasKepsekAdapter(emptyList())
                    recyclerView.adapter = adapter
                }
            } catch (e: Exception) {
                Toast.makeText(this@JadwalKelasActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
