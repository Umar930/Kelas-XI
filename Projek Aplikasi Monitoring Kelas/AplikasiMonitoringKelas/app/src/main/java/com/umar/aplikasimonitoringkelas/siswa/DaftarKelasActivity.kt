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
import com.google.android.material.button.MaterialButton
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.api.ApiServiceProvider
import com.umar.aplikasimonitoringkelas.data.model.KelasWithSiswa
import kotlinx.coroutines.launch

class DaftarKelasActivity : AppCompatActivity() {
    
    private lateinit var rvKelas: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var btnRefresh: MaterialButton
    
    companion object {
        private const val TAG = "DaftarKelasActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_kelas)
        
        setupToolbar()
        initViews()
        setupListeners()
        loadSemuaKelas()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            title = "Daftar Kelas & Siswa"
            setDisplayHomeAsUpEnabled(true)
        }
    }
    
    private fun initViews() {
        rvKelas = findViewById(R.id.rvKelas)
        progressBar = findViewById(R.id.progressBar)
        tvEmpty = findViewById(R.id.tvEmpty)
        btnRefresh = findViewById(R.id.btnRefresh)
        
        rvKelas.layoutManager = LinearLayoutManager(this)
    }
    
    private fun setupListeners() {
        btnRefresh.setOnClickListener {
            loadSemuaKelas()
        }
    }
    
    private fun loadSemuaKelas() {
        lifecycleScope.launch {
            try {
                progressBar.visibility = View.VISIBLE
                tvEmpty.visibility = View.GONE
                rvKelas.visibility = View.GONE
                
                val response = ApiServiceProvider.getApiService().getSemuaKelas()
                
                if (response.isSuccessful && response.body()?.success == true) {
                    val kelasList = response.body()?.data ?: emptyList()
                    
                    if (kelasList.isEmpty()) {
                        tvEmpty.visibility = View.VISIBLE
                        tvEmpty.text = "Tidak ada data kelas"
                    } else {
                        rvKelas.visibility = View.VISIBLE
                        val adapter = KelasAdapter(kelasList)
                        rvKelas.adapter = adapter
                    }
                } else {
                    android.widget.Toast.makeText(this@DaftarKelasActivity,
                        response.body()?.message ?: "Error loading kelas",
                        android.widget.Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading kelas: ${e.message}", e)
                android.widget.Toast.makeText(this@DaftarKelasActivity,
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
