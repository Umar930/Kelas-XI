package com.umar.aplikasimonitoringkelas.siswa.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.SiswaDashboardActivity
import com.umar.aplikasimonitoringkelas.data.api.RetrofitClient
import com.umar.aplikasimonitoringkelas.siswa.adapter.IzinGuruAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Fragment untuk menampilkan jadwal kelas (hanya view, tidak bisa edit)
 */
class JadwalSiswaFragment : Fragment() {

    private lateinit var rvJadwal: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutEmpty: LinearLayout

    private val scope = CoroutineScope(Dispatchers.Main)
    private var currentHari: String = "Senin"
    private var currentKelasId: Int = 0

    companion object {
        private const val TAG = "JadwalSiswaFragment"

        fun newInstance(): JadwalSiswaFragment {
            return JadwalSiswaFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_jadwal_siswa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvJadwal = view.findViewById(R.id.rvJadwal)
        progressBar = view.findViewById(R.id.progressBar)
        layoutEmpty = view.findViewById(R.id.layoutEmpty)

        rvJadwal.layoutManager = LinearLayoutManager(requireContext())

        // Adjust bottom padding to account for bottom navigation height / safe area
        view.post {
            val bottomNav = activity?.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigation)
            val defaultPad = resources.getDimensionPixelSize(R.dimen.recycler_bottom_padding_nav)
            val extraPad = resources.getDimensionPixelSize(R.dimen.recycler_bottom_padding_extra)
            val bottomNavHeight = bottomNav?.height ?: defaultPad
            rvJadwal.setPadding(rvJadwal.paddingLeft, rvJadwal.paddingTop, rvJadwal.paddingRight, bottomNavHeight + extraPad)
        }

        // Get initial filter from activity
        val activity = requireActivity() as? SiswaDashboardActivity
        activity?.let {
            currentHari = it.getSelectedHari()
            currentKelasId = it.getSelectedKelasId()
            if (currentKelasId > 0) {
                loadJadwal()
            }
        }
    }

    fun refreshData(hari: String, kelasId: Int) {
        currentHari = hari
        currentKelasId = kelasId
        if (isAdded && currentKelasId > 0) {
            loadJadwal()
        }
    }

    private fun loadJadwal() {
        progressBar.visibility = View.VISIBLE
        layoutEmpty.visibility = View.GONE
        rvJadwal.visibility = View.GONE

        scope.launch {
            try {
                val apiService = RetrofitClient.getApiService(requireContext())
                // Use /api/siswa/list-kehadiran endpoint (kelasId wajib, tanggal opsional)
                val response = apiService.getSiswaListKehadiran(kelasId = currentKelasId)

                if (response.isSuccessful && response.body()?.success == true) {
                    val allKehadiran = response.body()?.data ?: emptyList()
                    // Filter hanya izin/sakit
                    val izinSakitList = allKehadiran.filter { it.status == "izin" || it.status == "sakit" }

                    Log.d(TAG, "Loaded ${izinSakitList.size} izin/sakit for $currentHari")

                    if (izinSakitList.isNotEmpty()) {
                        rvJadwal.adapter = IzinGuruAdapter(izinSakitList)
                        rvJadwal.visibility = View.VISIBLE
                        layoutEmpty.visibility = View.GONE
                    } else {
                        rvJadwal.visibility = View.GONE
                        layoutEmpty.visibility = View.VISIBLE
                    }
                } else {
                    Log.e(TAG, "API error: ${response.code()} - ${response.message()}")
                    Toast.makeText(requireContext(), "Gagal memuat data: ${response.body()?.message ?: response.message()}", Toast.LENGTH_SHORT).show()
                    layoutEmpty.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading izin/sakit: ${e.message}", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                layoutEmpty.visibility = View.VISIBLE
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
