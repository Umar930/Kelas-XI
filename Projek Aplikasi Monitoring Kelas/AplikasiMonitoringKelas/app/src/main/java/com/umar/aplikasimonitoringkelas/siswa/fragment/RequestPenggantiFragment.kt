package com.umar.aplikasimonitoringkelas.siswa.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.api.RetrofitClient
import com.umar.aplikasimonitoringkelas.data.model.JadwalKelas
import com.umar.aplikasimonitoringkelas.data.model.RequestGuruPenggantiRequest
import com.umar.aplikasimonitoringkelas.siswa.JadwalGuruSiswaAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RequestPenggantiFragment : Fragment() {

    private lateinit var rvGuruAbsent: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val scope = CoroutineScope(Dispatchers.Main)
    
    private var kelasId: Int = 0
    private var kelasNama: String = ""

    companion object {
        private const val ARG_KELAS_ID = "kelas_id"
        private const val ARG_KELAS_NAMA = "kelas_nama"

        fun newInstance(kelasId: Int, kelasNama: String): RequestPenggantiFragment {
            return RequestPenggantiFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_KELAS_ID, kelasId)
                    putString(ARG_KELAS_NAMA, kelasNama)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            kelasId = it.getInt(ARG_KELAS_ID)
            kelasNama = it.getString(ARG_KELAS_NAMA) ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_request_pengganti, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        rvGuruAbsent = view.findViewById(R.id.rvGuruAbsent)
        progressBar = view.findViewById(R.id.progressBar)
        
        rvGuruAbsent.layoutManager = LinearLayoutManager(requireContext())
        
        loadGuruAbsent()
    }

    private fun loadGuruAbsent() {
        progressBar.visibility = View.VISIBLE
        scope.launch {
            try {
                val tanggal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val apiService = RetrofitClient.getApiService(requireContext())
                val response = apiService.getKelasKehadiran(tanggal = tanggal, kelasId = kelasId)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    val kelasKehadiranData = response.body()?.data
                    val kelasList = kelasKehadiranData?.kelas ?: emptyList()
                    
                    if (kelasList.isNotEmpty()) {
                        val jadwalHariIni = kelasList[0].jadwalHariIni
                        
                        // Filter only absent teachers
                        val guruAbsent = jadwalHariIni.filter { 
                            val status = it.kehadiran?.status
                            status == "tidak_hadir" || status == "izin" || status == "sakit"
                        }
                        
                        if (guruAbsent.isNotEmpty()) {
                            rvGuruAbsent.adapter = JadwalGuruSiswaAdapter(
                                jadwalList = guruAbsent,
                                onInputKehadiran = { 
                                    Toast.makeText(requireContext(), "Gunakan tab Input Kehadiran", Toast.LENGTH_SHORT).show()
                                },
                                onRequestPengganti = { jadwal -> showRequestPenggantiDialog(jadwal) }
                            )
                        } else {
                            Toast.makeText(requireContext(), "Tidak ada guru yang absent hari ini", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Tidak ada jadwal hari ini", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showRequestPenggantiDialog(jadwal: JadwalKelas) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_request_pengganti, null)
        val etAlasan = dialogView.findViewById<EditText>(R.id.etAlasan)
        etAlasan.hint = "Tulis pesan ke kurikulum..."
        
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Request Guru Pengganti")
            .setMessage("${jadwal.guru.nama} tidak hadir. Kirim pesan ke kurikulum untuk meminta guru pengganti.")
            .setView(dialogView)
            .setPositiveButton("Kirim") { _, _ ->
                val pesan = etAlasan.text.toString().trim()
                if (pesan.isNotBlank()) {
                    requestGuruPengganti(jadwal, pesan)
                } else {
                    Toast.makeText(requireContext(), "Pesan tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun requestGuruPengganti(jadwal: JadwalKelas, pesan: String) {
        val kehadiranGuruId = jadwal.kehadiran?.id
        if (kehadiranGuruId == null) {
            Toast.makeText(requireContext(), "Kehadiran guru tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }
        
        scope.launch {
            try {
                val request = RequestGuruPenggantiRequest(
                    kehadiranGuruId = kehadiranGuruId,
                    pesan = pesan
                )
                
                val apiService = RetrofitClient.getApiService(requireContext())
                val response = apiService.requestGuruPengganti(request)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(requireContext(), "Request guru pengganti berhasil dikirim", Toast.LENGTH_SHORT).show()
                    loadGuruAbsent() // Reload
                } else {
                    Toast.makeText(requireContext(), response.body()?.message ?: "Gagal mengirim request", Toast.LENGTH_SHORT).show()
                }
                
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scope.cancel()
    }
}
