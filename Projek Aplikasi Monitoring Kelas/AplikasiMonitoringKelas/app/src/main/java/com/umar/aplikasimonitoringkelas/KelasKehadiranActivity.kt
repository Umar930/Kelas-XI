package com.umar.aplikasimonitoringkelas

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umar.aplikasimonitoringkelas.adapter.KelasKehadiranAdapter
import com.umar.aplikasimonitoringkelas.data.api.ApiService
import com.umar.aplikasimonitoringkelas.data.api.RetrofitClient
import com.umar.aplikasimonitoringkelas.data.model.*
import com.umar.aplikasimonitoringkelas.data.session.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class KelasKehadiranActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiService: ApiService
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: KelasKehadiranAdapter
    private lateinit var spinnerKelas: Spinner
    private lateinit var btnFilter: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvTanggal: TextView
    
    private val scope = CoroutineScope(Dispatchers.Main)
    private var kelasList = listOf<KelasWithSiswa>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kelas_kehadiran)

        sessionManager = SessionManager.getInstance(this)
        apiService = RetrofitClient.getApiService(this)

        initViews()
        loadSemuaKelas()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.rvKehadiran)
        spinnerKelas = findViewById(R.id.spinnerKelas)
        btnFilter = findViewById(R.id.btnFilter)
        progressBar = findViewById(R.id.progressBar)
        tvTanggal = findViewById(R.id.tvTanggal)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = KelasKehadiranAdapter(emptyList()) { kelas: KelasWithJadwalKehadiran ->
            // Handle kelas click if needed
        }
        recyclerView.adapter = adapter

        val currentDate = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID")).format(Date())
        tvTanggal.text = currentDate

        btnFilter.setOnClickListener {
            val selectedIndex = spinnerKelas.selectedItemPosition
            if (selectedIndex >= 0 && selectedIndex < kelasList.size) {
                loadKehadiran(kelasList[selectedIndex].id)
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
                        this@KelasKehadiranActivity,
                        android.R.layout.simple_spinner_item,
                        kelasList.map { it.namaKelas }
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerKelas.adapter = adapter
                } else {
                    Toast.makeText(this@KelasKehadiranActivity, "Gagal memuat data kelas", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@KelasKehadiranActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun loadKehadiran(kelasId: Int) {
        progressBar.visibility = View.VISIBLE
        scope.launch {
            try {
                val tanggal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val response = apiService.getKelasKehadiran(
                    tanggal = tanggal,
                    kelasId = kelasId
                )
                
                if (response.isSuccessful && response.body()?.success == true) {
                    val kelasKehadiranData = response.body()?.data
                    val kelasList = kelasKehadiranData?.kelas ?: emptyList()
                    adapter = KelasKehadiranAdapter(kelasList) { kelas: KelasWithJadwalKehadiran ->
                        // Handle kelas click if needed
                    }
                    recyclerView.adapter = adapter
                } else {
                    Toast.makeText(this@KelasKehadiranActivity, "Tidak ada data kehadiran", Toast.LENGTH_SHORT).show()
                    adapter = KelasKehadiranAdapter(emptyList()) { kelas: KelasWithJadwalKehadiran ->
                        // Handle kelas click if needed
                    }
                    recyclerView.adapter = adapter
                }
            } catch (e: Exception) {
                Toast.makeText(this@KelasKehadiranActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
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
