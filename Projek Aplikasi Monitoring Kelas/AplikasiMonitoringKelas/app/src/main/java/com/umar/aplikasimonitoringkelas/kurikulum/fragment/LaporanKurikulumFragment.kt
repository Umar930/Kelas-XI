package com.umar.aplikasimonitoringkelas.kurikulum.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
// Menu imports removed (no menu used in this fragment)
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
// removed filter controls - no UI bindings for spinners or clear button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
// menu provider removed
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.api.RetrofitClient
// ClearLaporanRequest removed - clear action isn't part of the UI
import com.umar.aplikasimonitoringkelas.data.model.KurikulumRequestPengganti
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Fragment untuk menampilkan laporan pesan guru pengganti dari siswa
 * dengan fitur delete/clear pesan
 */
class LaporanKurikulumFragment : Fragment() {

    private lateinit var rvLaporan: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutEmpty: LinearLayout
    // removed dropdown filters and clear button from layout to simplify the Laporan tab

    // use viewLifecycleOwner.lifecycleScope for coroutine work
    private val laporanList = mutableListOf<KurikulumRequestPengganti>()
    // no currentTanggal used; the fragment loads all laporan without date filter
    // no kelas filter list required for the simplified Laporan view

    companion object {
        private const val TAG = "LaporanKurikulumFragment"

        fun newInstance(): LaporanKurikulumFragment {
            return LaporanKurikulumFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_laporan_kurikulum, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvLaporan = view.findViewById(R.id.rvLaporan)
        progressBar = view.findViewById(R.id.progressBar)
        layoutEmpty = view.findViewById(R.id.layoutEmpty)

        // No filter views used anymore

        rvLaporan.layoutManager = LinearLayoutManager(requireContext())
        // Ensure full scrollable content - avoid bottom navbar covering items
        view.post {
            val bottomNav = activity?.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigation)
            val defaultPad = resources.getDimensionPixelSize(R.dimen.recycler_bottom_padding_nav)
            val extraPad = resources.getDimensionPixelSize(R.dimen.recycler_bottom_padding_extra)
            val bottomNavHeight = bottomNav?.height ?: defaultPad
            rvLaporan.setPadding(rvLaporan.paddingLeft, rvLaporan.paddingTop, rvLaporan.paddingRight, bottomNavHeight + extraPad)
        }
        
        // Menu and filter functions removed; Laporan shows all requests
        
        loadLaporan()
    }

    // No filterable spinners; show all laporan requests
    
    // Removed menu & clear all action. The menu resource is kept but empty for future use.

    fun refreshData() {
        if (isAdded) {
            loadLaporan()
        }
    }

    private fun loadLaporan() {
        progressBar.visibility = View.VISIBLE
        layoutEmpty.visibility = View.GONE
        rvLaporan.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val apiService = RetrofitClient.getApiService(requireContext())

                // Use new API endpoint
                // Load all laporan requests without date/kelas/guru/hari filter to ensure student requests are visible
                val response = apiService.getLaporanRequest(
                    tanggalDari = null,
                    tanggalSampai = null,
                    kelasId = null,
                    status = null
                )

                if (response.isSuccessful && response.body()?.success == true) {
                    val rawList = response.body()?.data ?: emptyList()
                    laporanList.clear()
                    laporanList.addAll(rawList)

                    Log.d(TAG, "Loaded ${laporanList.size} laporan")

                    if (laporanList.isNotEmpty()) {
                        rvLaporan.adapter = LaporanAdapter()
                        rvLaporan.visibility = View.VISIBLE
                        layoutEmpty.visibility = View.GONE
                    } else {
                        rvLaporan.visibility = View.GONE
                        layoutEmpty.visibility = View.VISIBLE
                    }
                } else {
                    Log.e(TAG, "API error: ${response.code()} - ${response.message()}")
                    layoutEmpty.visibility = View.VISIBLE
                    }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Log.e(TAG, "Error loading laporan: ${e.message}", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                layoutEmpty.visibility = View.VISIBLE
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showDeleteConfirmDialog(item: KurikulumRequestPengganti) {
        val pesan = item.keterangan ?: "Laporan"
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Laporan")
            .setMessage("Apakah Anda yakin ingin menghapus pesan ini?\n\n\"$pesan\"")
            .setPositiveButton("Hapus") { _, _ ->
                deleteLaporan(item)
            }
            .setNegativeButton("Batal", null)
            .show()
    }
    
    private fun deleteLaporan(item: KurikulumRequestPengganti) {
        progressBar.visibility = View.VISIBLE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val apiService = RetrofitClient.getApiService(requireContext())

                val response = apiService.hapusLaporan(id = item.id)

                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Laporan berhasil dihapus", Toast.LENGTH_SHORT).show()
                    loadLaporan()
                } else {
                    Toast.makeText(requireContext(), "Gagal menghapus laporan", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Log.e(TAG, "Error deleting laporan: ${e.message}", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
    
    // clearAllLaporan method removed since the action has been removed from the UI to avoid mass deletion

    override fun onDestroyView() {
        super.onDestroyView()
        // lifecycleScope is used; it cancels automatically when view lifecycle ends
    }

    // Adapter for laporan list
    inner class LaporanAdapter : RecyclerView.Adapter<LaporanAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val cardLaporan: MaterialCardView = view.findViewById(R.id.cardLaporan)
            val tvJamKe: TextView = view.findViewById(R.id.tvJamKe)
            val tvTanggal: TextView = view.findViewById(R.id.tvTanggal)
            val tvNamaGuru: TextView = view.findViewById(R.id.tvNamaGuru)
            val tvKodeGuru: TextView = view.findViewById(R.id.tvKodeGuru)
            val tvNamaMapel: TextView = view.findViewById(R.id.tvNamaMapel)
            val tvPesan: TextView = view.findViewById(R.id.tvPesan)
            val tvRequestedBy: TextView = view.findViewById(R.id.tvRequestedBy)
            val tvGuruPengganti: TextView = view.findViewById(R.id.tvGuruPengganti)
            val btnDelete: View = view.findViewById(R.id.btnDelete)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_laporan_request, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = laporanList[position]

            holder.tvJamKe.text = "Jam: ${item.jadwal?.jam ?: (position + 1)}"
            holder.tvTanggal.text = item.tanggal ?: item.createdAt ?: "-"
            holder.tvNamaGuru.text = item.guruTidakHadir?.nama ?: "Unknown"
            holder.tvKodeGuru.text = item.guruTidakHadir?.kodeGuru ?: "-"
            holder.tvNamaMapel.text = item.jadwal?.mapel ?: "-"
            holder.tvPesan.text = item.keterangan ?: "-"
            holder.tvRequestedBy.text = item.requestedBy?.nama?.let { "Dikirim oleh: $it" } ?: ""

            // Show status
            when (item.status?.lowercase()) {
                "pending" -> {
                    holder.tvGuruPengganti.text = "Menunggu persetujuan"
                    holder.tvGuruPengganti.setTextColor(requireContext().getColor(R.color.status_izin))
                    holder.tvGuruPengganti.visibility = View.VISIBLE
                }
                "approved", "disetujui", "aktif" -> {
                    // aktif indicates the replacement has been set and is in effect
                    val displayKode = item.guruPengganti?.let { it.kodeGuru ?: "" } ?: ""
                    val displayName = item.guruPengganti?.nama ?: item.guruPengganti?.nama ?: ""
                    holder.tvGuruPengganti.text = if (item.status?.lowercase() == "aktif") {
                        "Guru diganti: ${displayKode.takeIf { it.isNotBlank() }?.let { "$it - " } ?: "" }$displayName. Silakan entri kehadiran untuk jadwal ini."
                    } else {
                        "Disetujui: ${item.guruPengganti?.nama ?: ""}"
                    }
                    holder.tvGuruPengganti.setTextColor(requireContext().getColor(R.color.status_hadir))
                    holder.tvGuruPengganti.visibility = View.VISIBLE
                }
                "rejected", "ditolak" -> {
                    holder.tvGuruPengganti.text = "Ditolak"
                    holder.tvGuruPengganti.setTextColor(requireContext().getColor(R.color.status_tidak_hadir))
                    holder.tvGuruPengganti.visibility = View.VISIBLE
                }
                else -> {
                    holder.tvGuruPengganti.visibility = View.GONE
                }
            }

            holder.btnDelete.setOnClickListener {
                showDeleteConfirmDialog(item)
            }
        }

        override fun getItemCount(): Int = laporanList.size
    }
}
