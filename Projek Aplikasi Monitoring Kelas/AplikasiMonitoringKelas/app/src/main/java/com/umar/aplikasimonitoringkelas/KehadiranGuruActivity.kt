package com.umar.aplikasimonitoringkelas

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umar.aplikasimonitoringkelas.adapter.KehadiranGuruKepsekAdapter
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

class KehadiranGuruActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiService: ApiService
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: KehadiranGuruKepsekAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var tvHadir: TextView
    private lateinit var tvTidakHadir: TextView
    private lateinit var tvIzin: TextView
    private lateinit var tvSakit: TextView

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kehadiran_guru)

        sessionManager = SessionManager.getInstance(this)
        apiService = RetrofitClient.getApiService(this)

        initViews()
        loadKehadiranGuru()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.rvKehadiranGuru)
        progressBar = findViewById(R.id.progressBar)
        tvHadir = findViewById(R.id.tvHadir)
        tvTidakHadir = findViewById(R.id.tvTidakHadir)
        tvIzin = findViewById(R.id.tvIzin)
        tvSakit = findViewById(R.id.tvSakit)

        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun loadKehadiranGuru() {
        progressBar.visibility = View.VISIBLE
        scope.launch {
            try {
                val tanggal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val response = apiService.getKehadiranGuru(
                    tanggal = tanggal,
                    kelasId = null,
                    guruId = null,
                    status = null
                )
                
                if (response.isSuccessful && response.body()?.success == true) {
                    val data = response.body()?.data
                    val kehadiranPerKelasList = data?.kehadiran ?: emptyList()
                    // Extract all kehadiran details from all kelas
                    val kehadiranList = kehadiranPerKelasList.flatMap { it.kehadiran }
                    val summary = data?.summary
                    
                    adapter = KehadiranGuruKepsekAdapter(kehadiranList)
                    recyclerView.adapter = adapter

                    tvHadir.text = "${summary?.totalHadir ?: 0}"
                    tvTidakHadir.text = "${summary?.totalTidakHadir ?: 0}"
                    tvIzin.text = "${summary?.totalIzin ?: 0}"
                    tvSakit.text = "${summary?.totalSakit ?: 0}"
                } else {
                    Toast.makeText(this@KehadiranGuruActivity, "Gagal memuat data kehadiran", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@KehadiranGuruActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
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
