package com.umar.aplikasimonitoringkelas

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.umar.aplikasimonitoringkelas.data.api.ApiService
import com.umar.aplikasimonitoringkelas.data.api.RetrofitClient
import com.umar.aplikasimonitoringkelas.data.model.*
import com.umar.aplikasimonitoringkelas.data.session.SessionManager
import com.umar.aplikasimonitoringkelas.model.Guru
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class InputGuruIzinActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiService: ApiService
    private lateinit var spinnerGuru: Spinner
    private lateinit var radioGroup: RadioGroup
    private lateinit var etTanggalMulai: EditText
    private lateinit var etTanggalSelesai: EditText
    private lateinit var etKeterangan: EditText
    private lateinit var btnSubmit: Button
    private lateinit var progressBar: ProgressBar

    private val scope = CoroutineScope(Dispatchers.Main)
    private var guruList = listOf<Guru>()
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_guru_izin)

        sessionManager = SessionManager.getInstance(this)
        apiService = RetrofitClient.getApiService(this)

        initViews()
        loadGuruList()
    }

    private fun initViews() {
        spinnerGuru = findViewById(R.id.spinnerGuru)
        radioGroup = findViewById(R.id.radioGroupStatus)
        etTanggalMulai = findViewById(R.id.etTanggalMulai)
        etTanggalSelesai = findViewById(R.id.etTanggalSelesai)
        etKeterangan = findViewById(R.id.etKeterangan)
        btnSubmit = findViewById(R.id.btnSubmit)
        progressBar = findViewById(R.id.progressBar)

        etTanggalMulai.setOnClickListener { showDatePicker(etTanggalMulai) }
        etTanggalSelesai.setOnClickListener { showDatePicker(etTanggalSelesai) }
        btnSubmit.setOnClickListener { submitInputGuruIzin() }
    }

    private fun showDatePicker(editText: EditText) {
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                editText.setText(format.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun loadGuruList() {
        progressBar.visibility = View.VISIBLE
        scope.launch {
            try {
                val response = apiService.getGuruList()
                
                if (response.isSuccessful && response.body()?.success == true) {
                    guruList = response.body()?.data ?: emptyList()
                    
                    val adapter = ArrayAdapter(
                        this@InputGuruIzinActivity,
                        android.R.layout.simple_spinner_item,
                        guruList.map { it.nama }
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerGuru.adapter = adapter
                } else {
                    Toast.makeText(this@InputGuruIzinActivity, "Gagal memuat data guru", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@InputGuruIzinActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun submitInputGuruIzin() {
        val selectedGuru = guruList.getOrNull(spinnerGuru.selectedItemPosition) ?: return
        val status = when (radioGroup.checkedRadioButtonId) {
            R.id.radioIzin -> "izin"
            R.id.radioSakit -> "sakit"
            else -> {
                Toast.makeText(this, "Pilih status terlebih dahulu", Toast.LENGTH_SHORT).show()
                return
            }
        }
        val tanggalMulai = etTanggalMulai.text.toString()
        val tanggalSelesai = etTanggalSelesai.text.toString()
        val keterangan = etKeterangan.text.toString()

        if (tanggalMulai.isEmpty() || tanggalSelesai.isEmpty()) {
            Toast.makeText(this, "Lengkapi semua field", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE
        scope.launch {
            try {
                val request = GuruIzinSakitRequest(
                    guruId = selectedGuru.id ?: 0,
                    tanggal = tanggalMulai,
                    status = status,
                    keterangan = keterangan,
                    jadwalIds = emptyList() // You may need to get actual jadwal IDs
                )
                val response = apiService.inputGuruIzinSakit(request)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@InputGuruIzinActivity, "Berhasil input guru $status", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@InputGuruIzinActivity, "Gagal: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@InputGuruIzinActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
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
