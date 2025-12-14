package com.umar.aplikasimonitoringkelas.siswa.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.api.RetrofitClient
import com.umar.aplikasimonitoringkelas.data.model.JadwalKelas
import com.umar.aplikasimonitoringkelas.data.model.InputKehadiranRequest
import com.umar.aplikasimonitoringkelas.siswa.JadwalGuruSiswaAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class InputKehadiranFragment : Fragment() {

    private lateinit var rvJadwalGuru: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val scope = CoroutineScope(Dispatchers.Main)
    
    private var kelasId: Int = 0
    private var kelasNama: String = ""

    companion object {
        private const val ARG_KELAS_ID = "kelas_id"
        private const val ARG_KELAS_NAMA = "kelas_nama"

        fun newInstance(kelasId: Int, kelasNama: String): InputKehadiranFragment {
            return InputKehadiranFragment().apply {
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
        return inflater.inflate(R.layout.fragment_input_kehadiran, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        rvJadwalGuru = view.findViewById(R.id.rvJadwalGuru)
        progressBar = view.findViewById(R.id.progressBar)
        
        rvJadwalGuru.layoutManager = LinearLayoutManager(requireContext())
        
        loadJadwalGuru()
    }

    private fun loadJadwalGuru() {
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
                        rvJadwalGuru.adapter = JadwalGuruSiswaAdapter(
                            jadwalList = jadwalHariIni,
                            onInputKehadiran = { jadwal -> showInputKehadiranDialog(jadwal) },
                            onRequestPengganti = { jadwal -> 
                                Toast.makeText(requireContext(), "Gunakan tab Request Pengganti", Toast.LENGTH_SHORT).show()
                            }
                        )
                    } else {
                        Toast.makeText(requireContext(), "Tidak ada jadwal hari ini", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal memuat jadwal", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showInputKehadiranDialog(jadwal: JadwalKelas) {
        val options = arrayOf("Hadir", "Tidak Hadir", "Izin", "Sakit")
        val statusValues = arrayOf("hadir", "tidak_hadir", "izin", "sakit")
        
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Input Kehadiran ${jadwal.guru.nama}")
            .setItems(options) { _, which ->
                inputKehadiran(jadwal, statusValues[which])
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun inputKehadiran(jadwal: JadwalKelas, status: String) {
        scope.launch {
            try {
                val request = InputKehadiranRequest(
                    jadwalKelasId = jadwal.id,
                    kelasId = kelasId,
                    tanggal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                    status = status,
                    keterangan = null
                )
                
                val apiService = RetrofitClient.getApiService(requireContext())
                val response = apiService.inputKehadiranGuru(request)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(requireContext(), "Berhasil input kehadiran", Toast.LENGTH_SHORT).show()
                    loadJadwalGuru() // Reload
                } else {
                    Toast.makeText(requireContext(), "Gagal: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
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
