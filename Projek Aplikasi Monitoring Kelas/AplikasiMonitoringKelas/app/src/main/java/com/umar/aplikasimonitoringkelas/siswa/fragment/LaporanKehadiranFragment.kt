package com.umar.aplikasimonitoringkelas.siswa.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.api.RetrofitClient
import com.umar.aplikasimonitoringkelas.siswa.JadwalGuruSiswaAdapter
import com.umar.aplikasimonitoringkelas.siswa.NotifikasiLaporanAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class LaporanKehadiranFragment : Fragment() {

    private lateinit var rvLaporanKehadiran: RecyclerView
    private lateinit var rvNotifikasi: RecyclerView
    private lateinit var layoutNotifikasi: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var tvTanggal: TextView
    private lateinit var tvSummary: TextView
    private val scope = CoroutineScope(Dispatchers.Main)
    
    private var kelasId: Int = 0
    private var kelasNama: String = ""

    companion object {
        private const val ARG_KELAS_ID = "kelas_id"
        private const val ARG_KELAS_NAMA = "kelas_nama"

        fun newInstance(kelasId: Int, kelasNama: String): LaporanKehadiranFragment {
            return LaporanKehadiranFragment().apply {
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
        return inflater.inflate(R.layout.fragment_laporan_kehadiran, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        rvLaporanKehadiran = view.findViewById(R.id.rvLaporanKehadiran)
        rvNotifikasi = view.findViewById(R.id.rvNotifikasi)
        layoutNotifikasi = view.findViewById(R.id.layoutNotifikasi)
        progressBar = view.findViewById(R.id.progressBar)
        tvTanggal = view.findViewById(R.id.tvTanggal)
        tvSummary = view.findViewById(R.id.tvSummary)
        
        rvLaporanKehadiran.layoutManager = LinearLayoutManager(requireContext())
        rvNotifikasi.layoutManager = LinearLayoutManager(requireContext())
        
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        tvTanggal.text = dateFormat.format(Date())
        
        loadNotifikasi()
        loadLaporanKehadiran()
    }

    private fun loadNotifikasi() {
        scope.launch {
            try {
                val apiService = RetrofitClient.getApiService(requireContext())
                val response = apiService.getNotifikasi()
                
                if (response.isSuccessful && response.body()?.success == true) {
                    val notifikasiData = response.body()?.data
                    val notifikasiList = notifikasiData?.notifikasi ?: emptyList()
                    
                    // Filter notifikasi untuk kelas ini, hari ini, tipe izin/sakit/guru_pengganti
                    val tanggalHariIni = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val filteredNotifikasi = notifikasiList.filter { notif ->
                        notif.kelasId == kelasId && 
                        notif.tanggal == tanggalHariIni &&
                        (notif.tipe.lowercase() == "izin" || 
                         notif.tipe.lowercase() == "sakit" || 
                         notif.tipe.lowercase() == "guru_pengganti")
                    }
                    
                    if (filteredNotifikasi.isNotEmpty()) {
                        layoutNotifikasi.visibility = View.VISIBLE
                        rvNotifikasi.adapter = NotifikasiLaporanAdapter(filteredNotifikasi)
                    } else {
                        layoutNotifikasi.visibility = View.GONE
                    }
                }
            } catch (e: Exception) {
                // Silent fail untuk notifikasi
                layoutNotifikasi.visibility = View.GONE
            }
        }
    }

    private fun loadLaporanKehadiran() {
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
                        
                        // Calculate summary
                        val total = jadwalHariIni.size
                        val hadir = jadwalHariIni.count { it.kehadiran?.status == "hadir" }
                        val tidakHadir = jadwalHariIni.count { it.kehadiran?.status == "tidak_hadir" }
                        val izin = jadwalHariIni.count { it.kehadiran?.status == "izin" }
                        val sakit = jadwalHariIni.count { it.kehadiran?.status == "sakit" }
                        val belumInput = jadwalHariIni.count { it.kehadiran == null }
                        
                        tvSummary.text = "Total: $total | Hadir: $hadir | Tidak Hadir: $tidakHadir | Izin: $izin | Sakit: $sakit | Belum Input: $belumInput"
                        
                        rvLaporanKehadiran.adapter = JadwalGuruSiswaAdapter(
                            jadwalList = jadwalHariIni,
                            onInputKehadiran = { 
                                Toast.makeText(requireContext(), "Gunakan tab Input Kehadiran", Toast.LENGTH_SHORT).show()
                            },
                            onRequestPengganti = { 
                                Toast.makeText(requireContext(), "Gunakan tab Request Pengganti", Toast.LENGTH_SHORT).show()
                            }
                        )
                    } else {
                        Toast.makeText(requireContext(), "Tidak ada jadwal hari ini", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal memuat laporan", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scope.cancel()
    }
}
