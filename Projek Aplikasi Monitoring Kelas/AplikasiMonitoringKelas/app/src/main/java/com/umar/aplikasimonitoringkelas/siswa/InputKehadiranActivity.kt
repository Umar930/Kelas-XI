package com.umar.aplikasimonitoringkelas.siswa

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.api.ApiServiceProvider
import com.umar.aplikasimonitoringkelas.data.model.*
import com.umar.aplikasimonitoringkelas.data.session.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class InputKehadiranActivity : AppCompatActivity() {
    
    private lateinit var sessionManager: SessionManager
    private lateinit var rvJadwal: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var btnRefresh: MaterialButton
    private lateinit var tvTanggal: TextView
    private lateinit var btnChangeTanggal: MaterialButton
    
    private var selectedTanggal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    private val calendar = Calendar.getInstance()
    
    companion object {
        private const val TAG = "InputKehadiranActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_kehadiran)
        
        sessionManager = SessionManager(this)
        
        setupToolbar()
        initViews()
        setupListeners()
        loadJadwalHariIni()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            title = "Input Kehadiran Guru"
            setDisplayHomeAsUpEnabled(true)
        }
    }
    
    private fun initViews() {
        rvJadwal = findViewById(R.id.rvJadwal)
        progressBar = findViewById(R.id.progressBar)
        tvEmpty = findViewById(R.id.tvEmpty)
        btnRefresh = findViewById(R.id.btnRefresh)
        tvTanggal = findViewById(R.id.tvTanggal)
        btnChangeTanggal = findViewById(R.id.btnChangeTanggal)
        
        rvJadwal.layoutManager = LinearLayoutManager(this)
        
        updateTanggalDisplay()
    }
    
    private fun setupListeners() {
        btnRefresh.setOnClickListener {
            loadJadwalHariIni()
        }
        
        btnChangeTanggal.setOnClickListener {
            showDatePicker()
        }
    }
    
    private fun updateTanggalDisplay() {
        val formatter = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(selectedTanggal)
        tvTanggal.text = formatter.format(date ?: Date())
    }
    
    private fun showDatePicker() {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(selectedTanggal)
        calendar.time = date ?: Date()
        
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedTanggal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(calendar.time)
                updateTanggalDisplay()
                loadJadwalHariIni()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    
    private fun loadJadwalHariIni() {
        lifecycleScope.launch {
            try {
                progressBar.visibility = View.VISIBLE
                tvEmpty.visibility = View.GONE
                rvJadwal.visibility = View.GONE
                
                val response = ApiServiceProvider.getApiService().getKelasSaya()
                
                if (response.isSuccessful && response.body()?.success == true) {
                    val data = response.body()?.data
                    val jadwalList = data?.jadwalHariIni ?: emptyList()
                    
                    if (jadwalList.isEmpty()) {
                        tvEmpty.visibility = View.VISIBLE
                        tvEmpty.text = "Tidak ada jadwal untuk hari ini"
                    } else {
                        rvJadwal.visibility = View.VISIBLE
                        val kelasId = sessionManager.getUserKelasId().first() ?: 0
                        val adapter = JadwalAdapter(jadwalList, kelasId, selectedTanggal) { jadwal ->
                            showInputKehadiranDialog(jadwal, kelasId)
                        }
                        rvJadwal.adapter = adapter
                    }
                } else {
                    Toast.makeText(this@InputKehadiranActivity, 
                        response.body()?.message ?: "Error loading jadwal", 
                        Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading jadwal: ${e.message}", e)
                Toast.makeText(this@InputKehadiranActivity, 
                    "Error: ${e.message}", 
                    Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
    
    private fun showInputKehadiranDialog(jadwal: JadwalKelas, kelasId: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_input_kehadiran, null)
        
        val tvGuruMapel = dialogView.findViewById<TextView>(R.id.tvGuruMapel)
        val tvJam = dialogView.findViewById<TextView>(R.id.tvJam)
        val rgStatus = dialogView.findViewById<RadioGroup>(R.id.rgStatus)
        val rbHadir = dialogView.findViewById<RadioButton>(R.id.rbHadir)
        val rbTidakHadir = dialogView.findViewById<RadioButton>(R.id.rbTidakHadir)
        val edtKeterangan = dialogView.findViewById<TextInputEditText>(R.id.edtKeterangan)
        val btnSubmit = dialogView.findViewById<MaterialButton>(R.id.btnSubmit)
        val btnCancel = dialogView.findViewById<MaterialButton>(R.id.btnCancel)
        
        tvGuruMapel.text = "${jadwal.guru.nama} - ${jadwal.mapel.mapel ?: jadwal.mapel.nama}"
        tvJam.text = "${jadwal.jamMulai} - ${jadwal.jamSelesai}"
        
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()
        
        // Enable/disable keterangan based on status
        rgStatus.setOnCheckedChangeListener { _, checkedId ->
            edtKeterangan.isEnabled = checkedId == R.id.rbTidakHadir
            if (checkedId == R.id.rbHadir) {
                edtKeterangan.setText("")
            }
        }
        
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        
        btnSubmit.setOnClickListener {
            val status = if (rbHadir.isChecked) "hadir" else "tidak_hadir"
            val keterangan = edtKeterangan.text.toString()
            
            submitKehadiran(jadwal, kelasId, status, keterangan, dialog)
        }
        
        dialog.show()
    }
    
    private fun submitKehadiran(
        jadwal: JadwalKelas, 
        kelasId: Int, 
        status: String, 
        keterangan: String?,
        dialog: AlertDialog
    ) {
        lifecycleScope.launch {
            try {
                val request = InputKehadiranRequest(
                    jadwalId = jadwal.id,
                    guruId = jadwal.guru.id,
                    kelasId = kelasId,
                    mapelId = jadwal.mapel.id,
                    tanggal = selectedTanggal,
                    status = status,
                    keterangan = keterangan
                )
                
                val response = ApiServiceProvider.getApiService().inputKehadiranGuru(request)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@InputKehadiranActivity, 
                        "Kehadiran guru berhasil diinput", 
                        Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    loadJadwalHariIni()
                    
                    // Jika tidak hadir, tanya apakah mau request guru pengganti
                    if (status == "tidak_hadir") {
                        showRequestGuruPenggantiDialog(response.body()?.data?.id ?: 0)
                    }
                } else {
                    Toast.makeText(this@InputKehadiranActivity, 
                        response.body()?.message ?: "Error input kehadiran", 
                        Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error submit kehadiran: ${e.message}", e)
                Toast.makeText(this@InputKehadiranActivity, 
                    "Error: ${e.message}", 
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun showRequestGuruPenggantiDialog(kehadiranGuruId: Int) {
        // Create EditText for pesan input
        val editTextPesan = android.widget.EditText(this).apply {
            hint = "Tulis pesan ke kurikulum..."
            minLines = 3
            gravity = android.view.Gravity.TOP
            setPadding(32, 24, 32, 24)
        }
        
        AlertDialog.Builder(this)
            .setTitle("Request Guru Pengganti")
            .setMessage("Guru tidak hadir. Kirim pesan ke kurikulum untuk meminta guru pengganti.")
            .setView(editTextPesan)
            .setPositiveButton("Kirim") { _, _ ->
                val pesan = editTextPesan.text.toString().trim()
                if (pesan.isNotEmpty()) {
                    requestGuruPengganti(kehadiranGuruId, pesan)
                } else {
                    Toast.makeText(this, "Pesan tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }
    
    private fun requestGuruPengganti(kehadiranGuruId: Int, pesan: String) {
        lifecycleScope.launch {
            try {
                val request = RequestGuruPenggantiRequest(
                    kehadiranGuruId = kehadiranGuruId,
                    pesan = pesan
                )
                
                val response = ApiServiceProvider.getApiService().requestGuruPengganti(request)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@InputKehadiranActivity, 
                        "Request guru pengganti berhasil dikirim", 
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@InputKehadiranActivity, 
                        response.body()?.message ?: "Error request guru pengganti", 
                        Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error request guru pengganti: ${e.message}", e)
                Toast.makeText(this@InputKehadiranActivity, 
                    "Error: ${e.message}", 
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
