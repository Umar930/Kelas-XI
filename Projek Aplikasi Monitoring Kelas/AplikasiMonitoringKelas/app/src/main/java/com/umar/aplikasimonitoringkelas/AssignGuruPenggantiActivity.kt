package com.umar.aplikasimonitoringkelas

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umar.aplikasimonitoringkelas.adapter.RequestPenggantiAdapter
import com.umar.aplikasimonitoringkelas.data.api.ApiService
import com.umar.aplikasimonitoringkelas.data.api.RetrofitClient
import com.umar.aplikasimonitoringkelas.data.model.*
import com.umar.aplikasimonitoringkelas.data.session.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AssignGuruPenggantiActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiService: ApiService
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RequestPenggantiAdapter
    private lateinit var progressBar: ProgressBar

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assign_guru_pengganti)

        sessionManager = SessionManager.getInstance(this)
        apiService = RetrofitClient.getApiService(this)

        initViews()
        loadRequestPengganti()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.rvRequestPengganti)
        progressBar = findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun loadRequestPengganti() {
        progressBar.visibility = View.VISIBLE
        scope.launch {
            try {
                // Note: This endpoint doesn't exist in ApiService, using placeholder
                Toast.makeText(this@AssignGuruPenggantiActivity, "Fitur belum tersedia", Toast.LENGTH_SHORT).show()
                // When endpoint is ready, use: apiService.getGuruPengganti(...)
            } catch (e: Exception) {
                Toast.makeText(this@AssignGuruPenggantiActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun assignGuruPengganti(kehadiranGuruId: Int, guruPenggantiId: Int) {
        progressBar.visibility = View.VISIBLE
        scope.launch {
            try {
                val request = AssignGuruPenggantiRequest(
                    kehadiranGuruId = kehadiranGuruId,
                    guruPenggantiId = guruPenggantiId,
                    catatan = null
                )
                val response = apiService.assignGuruPengganti(request)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@AssignGuruPenggantiActivity, "Berhasil assign guru pengganti", Toast.LENGTH_SHORT).show()
                    loadRequestPengganti()
                } else {
                    Toast.makeText(this@AssignGuruPenggantiActivity, "Gagal: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AssignGuruPenggantiActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
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
