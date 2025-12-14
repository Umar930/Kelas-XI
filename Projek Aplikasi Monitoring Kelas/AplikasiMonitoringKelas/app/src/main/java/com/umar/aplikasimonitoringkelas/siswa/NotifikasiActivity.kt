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
import kotlinx.coroutines.launch

class NotifikasiActivity : AppCompatActivity() {
    
    private lateinit var rvNotifikasi: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var btnRefresh: MaterialButton
    private lateinit var tvUnreadCount: TextView
    
    companion object {
        private const val TAG = "NotifikasiActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifikasi)
        
        setupToolbar()
        initViews()
        setupListeners()
        loadNotifikasi()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            title = "Notifikasi"
            setDisplayHomeAsUpEnabled(true)
        }
    }
    
    private fun initViews() {
        rvNotifikasi = findViewById(R.id.rvNotifikasi)
        progressBar = findViewById(R.id.progressBar)
        tvEmpty = findViewById(R.id.tvEmpty)
        btnRefresh = findViewById(R.id.btnRefresh)
        tvUnreadCount = findViewById(R.id.tvUnreadCount)
        
        rvNotifikasi.layoutManager = LinearLayoutManager(this)
    }
    
    private fun setupListeners() {
        btnRefresh.setOnClickListener {
            loadNotifikasi()
        }
    }
    
    private fun loadNotifikasi() {
        lifecycleScope.launch {
            try {
                progressBar.visibility = View.VISIBLE
                tvEmpty.visibility = View.GONE
                rvNotifikasi.visibility = View.GONE
                
                val response = ApiServiceProvider.getApiService().getNotifikasi()
                
                if (response.isSuccessful && response.body()?.success == true) {
                    val data = response.body()?.data
                    val notifikasiList = data?.notifikasi ?: emptyList()
                    val unreadCount = data?.unreadCount ?: 0
                    
                    tvUnreadCount.text = "$unreadCount notifikasi belum dibaca"
                    
                    if (notifikasiList.isEmpty()) {
                        tvEmpty.visibility = View.VISIBLE
                        tvEmpty.text = "Tidak ada notifikasi"
                    } else {
                        rvNotifikasi.visibility = View.VISIBLE
                        val adapter = NotifikasiAdapter(notifikasiList) { notif ->
                            markAsRead(notif.id)
                        }
                        rvNotifikasi.adapter = adapter
                    }
                } else {
                    android.widget.Toast.makeText(this@NotifikasiActivity,
                        response.body()?.message ?: "Error loading notifikasi",
                        android.widget.Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading notifikasi: ${e.message}", e)
                android.widget.Toast.makeText(this@NotifikasiActivity,
                    "Error: ${e.message}",
                    android.widget.Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
    
    private fun markAsRead(notifId: Int) {
        lifecycleScope.launch {
            try {
                val response = ApiServiceProvider.getApiService().markNotifikasiAsRead(notifId)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    loadNotifikasi() // Reload to update unread count
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error mark as read: ${e.message}", e)
            }
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
