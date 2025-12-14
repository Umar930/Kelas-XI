package com.umar.aplikasimonitoringkelas.kurikulum.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.api.RetrofitClient
import com.umar.aplikasimonitoringkelas.data.model.DaftarGuruItem
import com.umar.aplikasimonitoringkelas.data.model.DaftarGuruStatistik
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Fragment untuk menampilkan daftar kehadiran guru (read-only)
 * TAB 2 - Daftar Guru dengan statistik
 */
class DaftarGuruFragment : Fragment() {

    private lateinit var rvDaftarGuru: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutEmpty: LinearLayout
    
    // Filter info (received from parent activity)
    private var selectedGuruFilter: String? = null
    
    // Statistik views
    private lateinit var tvHadir: TextView
    private lateinit var tvTidakHadir: TextView
    private lateinit var tvIzin: TextView
    private lateinit var tvSakit: TextView
    
    // Status filter button
    private lateinit var btnFilterStatus: ImageButton
    private var selectedStatusFilter: String? = null

    // use viewLifecycleOwner.lifecycleScope for coroutine work
    private var currentHari: String = "Senin"
    private var currentKelasId: Int = 0
    private var currentTanggal: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private val guruList = mutableListOf<DaftarGuruItem>()
    private var siswaMapGlobal: Map<Int, com.umar.aplikasimonitoringkelas.data.model.KehadiranItem> = emptyMap()
    
    private val hariList = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")

    companion object {
        private const val TAG = "DaftarGuruFragment"

        fun newInstance(): DaftarGuruFragment {
            return DaftarGuruFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_daftar_guru, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvDaftarGuru = view.findViewById(R.id.rvDaftarGuru)
        progressBar = view.findViewById(R.id.progressBar)
        layoutEmpty = view.findViewById(R.id.layoutEmpty)
        
        // Filters will be provided by parent activity; fragment receives them via refreshData
        
        // Statistik views
        tvHadir = view.findViewById(R.id.tvHadir)
        tvTidakHadir = view.findViewById(R.id.tvTidakHadir)
        tvIzin = view.findViewById(R.id.tvIzin)
        tvSakit = view.findViewById(R.id.tvSakit)

        // Single status filter button
        btnFilterStatus = view.findViewById(R.id.btnFilterStatus)
        btnFilterStatus.setOnClickListener { showStatusMenu(it) }
        updateFilterButtonsUI()

        rvDaftarGuru.layoutManager = LinearLayoutManager(requireContext())

        // Ensure RecyclerView is scrollable to the bottom with bottom nav
        view.post {
            val bottomNav = activity?.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigation)
            val defaultPad = resources.getDimensionPixelSize(R.dimen.recycler_bottom_padding_nav)
            val extraPad = resources.getDimensionPixelSize(R.dimen.recycler_bottom_padding_extra)
            val bottomNavHeight = bottomNav?.height ?: defaultPad
            rvDaftarGuru.setPadding(rvDaftarGuru.paddingLeft, rvDaftarGuru.paddingTop, rvDaftarGuru.paddingRight, bottomNavHeight + extraPad)
        }
        
        // We will get filter values from parent activity through refreshData
        // and load data in refreshData when the activity calls refreshCurrentFragment
        // Initialize with current activity selection if available
        val parent = activity as? com.umar.aplikasimonitoringkelas.kurikulum.KurikulumDashboardActivity
        parent?.let {
            currentHari = it.getSelectedHari()
            currentKelasId = it.getSelectedKelasId()
            selectedGuruFilter = it.getSelectedGuruName()
            if (currentKelasId > 0) {
                loadDaftarGuru()
            }
        }
    }
    
    // Filters handled by activity

    fun refreshData(hari: String, kelasId: Int, guruName: String? = null) {
        currentHari = hari
        currentKelasId = kelasId
        selectedGuruFilter = guruName
        if (isAdded) {
            loadDaftarGuru()
        }
    }

    private fun toggleStatusFilter(status: String) {
        selectedStatusFilter = if (selectedStatusFilter == status) null else status
        updateFilterButtonsUI()
        loadDaftarGuru()
    }

    private fun updateFilterButtonsUI() {
        // Visual feedback on single filter button using alpha and tint
        if (selectedStatusFilter.isNullOrBlank()) {
            btnFilterStatus.alpha = 0.7f
            btnFilterStatus.setColorFilter(ContextCompat.getColor(requireContext(), R.color.primary))
        } else {
            btnFilterStatus.alpha = 1.0f
            val color = when (selectedStatusFilter) {
                "hadir" -> R.color.status_hadir
                "tidak_hadir" -> R.color.status_tidak_hadir
                "izin" -> R.color.status_izin
                "sakit" -> R.color.status_sakit
                else -> R.color.primary
            }
            btnFilterStatus.setColorFilter(ContextCompat.getColor(requireContext(), color))
        }
    }

    private fun showStatusMenu(anchor: View) {
        val popup = android.widget.PopupMenu(requireContext(), anchor)
        popup.menu.add(0, 0, 0, "Semua")
        popup.menu.add(0, 1, 1, "Hadir")
        popup.menu.add(0, 2, 2, "Tidak Hadir")
        popup.menu.add(0, 3, 3, "Izin")
        popup.menu.add(0, 4, 4, "Sakit")
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                0 -> toggleStatusFilter("")
                1 -> toggleStatusFilter("hadir")
                2 -> toggleStatusFilter("tidak_hadir")
                3 -> toggleStatusFilter("izin")
                4 -> toggleStatusFilter("sakit")
            }
            true
        }
        popup.show()
    }

    private fun loadDaftarGuru() {
        progressBar.visibility = View.VISIBLE
        layoutEmpty.visibility = View.GONE
        rvDaftarGuru.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val apiService = RetrofitClient.getApiService(requireContext())
                
                val response = apiService.getDaftarGuru(
                    hari = currentHari,
                    kelasId = if (currentKelasId > 0) currentKelasId else null,
                    tanggal = currentTanggal
                )
                Log.d(TAG, "Requesting daftar-guru?hari=${currentHari}&kelas_id=${currentKelasId}&tanggal=${currentTanggal}")

                if (response.isSuccessful && response.body()?.success == true) {
                    val rawList = response.body()?.data ?: emptyList()

                        // Debug: log admin-entered items and extra kehadiran (no jadwal)
                        val adminEntries = rawList.filter { it.inputBy?.equals("kurikulum", ignoreCase = true) == true }
                        if (adminEntries.isNotEmpty()) {
                            Log.d(TAG, "Found ${adminEntries.size} kurikulum-entered kehadiran (sample): ${adminEntries.take(3)}")
                        }
                        val extraEntries = rawList.filter { it.jadwalId == null }
                        if (extraEntries.isNotEmpty()) {
                            Log.d(TAG, "Found ${extraEntries.size} extra kehadiran (no jadwal) (sample): ${extraEntries.take(3)}")
                        }

                    // Also fetch siswa kehadiran to capture hadirs/tidak hadirs input by students
                    var siswaMap: Map<Int, com.umar.aplikasimonitoringkelas.data.model.KehadiranItem> = emptyMap()
                    var jadwalMap: Map<Int, com.umar.aplikasimonitoringkelas.data.model.JadwalItem> = emptyMap()
                    try {
                        if (currentKelasId > 0) {
                            val siswaResp = apiService.getSiswaListKehadiran(kelasId = currentKelasId, tanggal = currentTanggal)
                            if (siswaResp.isSuccessful && siswaResp.body()?.success == true) {
                                val siswaList = siswaResp.body()?.data ?: emptyList()
                                        siswaMap = siswaList.filter { it.jadwalId != null }.associateBy { it.jadwalId!! }
                                        // Save global siswaMap so adapter can show tanggal for siswa input
                                        siswaMapGlobal = siswaMap
                            }
                            // Also load jadwal for that kelas & hari so we can show jamMulai - jamSelesai
                            try {
                                val jadwalResp = apiService.getSiswaJadwal(hari = currentHari, kelasId = currentKelasId)
                                if (jadwalResp.isSuccessful && jadwalResp.body()?.success == true) {
                                    val jadwalList = jadwalResp.body()?.data ?: emptyList()
                                    // Map by jadwalId (most matching field for DaftarGuruItem.jadwalId)
                                    jadwalMap = jadwalList.filter { it.jadwalId != null }.associateBy { it.jadwalId!! }
                                }
                            } catch (e: Exception) {
                                if (e is CancellationException) throw e
                                Log.w(TAG, "Failed to load jadwal list: ${'$'}{e.message}")
                            }
                        }
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        Log.w(TAG, "Failed to load siswa kehadiran: ${e.message}")
                    }

                    // Merge rawList with siswaMap - give priority to admin (kurikulum/izin/sakit)
                    val merged = rawList.map { item ->
                        var updated = item
                        // If we have jadwal detail for this jadwalId, set jam to a full range
                        val jadwalDetail = item.jadwalId?.let { jadwalMap[it] }
                        if (jadwalDetail != null) {
                            updated = updated.copy(jam = "${jadwalDetail.jamMulai} - ${jadwalDetail.jamSelesai}")
                        }
                        val siswa = item.jadwalId?.let { siswaMap[it] }
                        // If admin has set izin/sakit for this item, keep admin's status
                        if (!item.inputBy.isNullOrEmpty() && item.inputBy.equals("kurikulum", ignoreCase = true) &&
                            ((item.statusKehadiran?.equals("izin", ignoreCase = true) == true) || (item.statusKehadiran?.equals("sakit", ignoreCase = true) == true))) {
                            // keep updated as is
                        } else if (siswa != null) {
                            // siswa inputs hadir/tidak hadir should be reflected
                            if (siswa.status.equals("hadir", ignoreCase = true) || siswa.status.equals("tidak_hadir", ignoreCase = true)) {
                                updated = item.copy(
                                    statusKehadiran = siswa.status,
                                    kehadiranId = siswa.kehadiranId,
                                    keterangan = siswa.keterangan,
                                    inputBy = siswa.inputBy
                                )
                            }
                        }
                        updated
                    }

                    // Apply selected guru filter
                    val filtered = if (!selectedGuruFilter.isNullOrBlank()) {
                        val filterLower = selectedGuruFilter!!.lowercase()
                        merged.filter {
                            val kode = it.guru?.kodeGuru?.lowercase().orEmpty()
                            val nama = it.guru?.nama?.lowercase().orEmpty()
                            val kurikulumInputName = it.inputByKurikulumName?.lowercase().orEmpty()
                            nama.contains(filterLower) || kode.contains(filterLower)
                        }
                    } else merged

                            // Apply status filter if selected
                            val filteredByStatus = if (!selectedStatusFilter.isNullOrBlank()) {
                                val sLower = selectedStatusFilter!!.lowercase()
                                filtered.filter { it.statusKehadiran?.lowercase()?.replace(" ", "_") == sLower }
                            } else filtered

                    guruList.clear()
                    guruList.addAll(filteredByStatus)

                    // Recompute statistik from merged list to reflect what we're showing
                    val hadirCount = guruList.count { it.statusKehadiran?.equals("hadir", ignoreCase = true) == true }
                    val tidakHadirCount = guruList.count { (it.statusKehadiran?.equals("tidak_hadir", ignoreCase = true) == true) || (it.statusKehadiran?.equals("tidak hadir", ignoreCase = true) == true) }
                    val izinCount = guruList.count { it.statusKehadiran?.equals("izin", ignoreCase = true) == true }
                    val sakitCount = guruList.count { it.statusKehadiran?.equals("sakit", ignoreCase = true) == true }
                    updateStatistik(DaftarGuruStatistik(
                        total = guruList.size,
                        hadir = hadirCount,
                        tidakHadir = tidakHadirCount,
                        izin = izinCount,
                        sakit = sakitCount,
                        belumDiisi = guruList.count { it.statusKehadiran.isNullOrEmpty() || (it.statusKehadiran?.equals("belum_diisi", ignoreCase = true) == true) }
                    ))

                    Log.d(TAG, "Loaded ${guruList.size} guru")

                    if (guruList.isNotEmpty()) {
                        rvDaftarGuru.adapter = DaftarGuruAdapter()
                        rvDaftarGuru.visibility = View.VISIBLE
                        layoutEmpty.visibility = View.GONE
                    } else {
                        rvDaftarGuru.visibility = View.GONE
                        layoutEmpty.visibility = View.VISIBLE
                    }
                } else {
                    Log.e(TAG, "API error: ${response.code()} - ${response.message()}")
                    layoutEmpty.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Log.e(TAG, "Error loading kehadiran: ${e.message}", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                layoutEmpty.visibility = View.VISIBLE
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun updateStatistik(stat: DaftarGuruStatistik) {
        tvHadir.text = stat.hadir.toString()
        tvTidakHadir.text = stat.tidakHadir.toString()
        tvIzin.text = stat.izin.toString()
        tvSakit.text = stat.sakit.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // lifecycleScope cancels automatically when the view lifecycle ends
    }

    // ========== ADAPTER ==========
    inner class DaftarGuruAdapter : RecyclerView.Adapter<DaftarGuruAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val cardGuru: MaterialCardView = view.findViewById(R.id.cardGuruKehadiran)
            val tvJamKe: TextView = view.findViewById(R.id.tvJamKe)
            val tvNamaGuru: TextView = view.findViewById(R.id.tvNamaGuru)
            val tvKodeGuru: TextView = view.findViewById(R.id.tvKodeGuru)
            val tvNamaMapel: TextView = view.findViewById(R.id.tvNamaMapel)
            val tvStatusKehadiran: TextView = view.findViewById(R.id.tvStatusKehadiran)
            val tvGuruPengganti: TextView = view.findViewById(R.id.tvGuruPengganti)
            val tvInputBy: TextView = view.findViewById(R.id.tvInputBy)
            val tvTanggal: TextView = view.findViewById(R.id.tvTanggal)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_daftar_kehadiran_guru, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = guruList[position]
            val context = holder.itemView.context

            // Show jam; if item has no jadwal (extra kehadiran), indicate 'Tidak ada jadwal'
            holder.tvJamKe.text = item.jam ?: if (item.jadwalId == null) "Tidak ada jadwal" else (position + 1).toString()
            holder.tvNamaGuru.text = item.guru?.nama ?: item.guru?.kodeGuru ?: "Unknown"
            holder.tvKodeGuru.text = item.guru?.kodeGuru ?: "-"
            holder.tvNamaMapel.text = item.mapel?.nama ?: "-"

            // Status kehadiran dengan warna
                    when (item.statusKehadiran?.lowercase()) {
                "hadir" -> {
                    holder.tvStatusKehadiran.text = "Hadir"
                            holder.tvStatusKehadiran.setTextColor(ContextCompat.getColor(context, R.color.status_hadir))
                            holder.cardGuru.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_status_hadir))
                }
                "tidak_hadir", "tidak hadir" -> {
                    holder.tvStatusKehadiran.text = "Tidak Hadir"
                            holder.tvStatusKehadiran.setTextColor(ContextCompat.getColor(context, R.color.status_tidak_hadir))
                            holder.cardGuru.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_status_tidak_hadir))
                }
                "izin" -> {
                    holder.tvStatusKehadiran.text = "Izin"
                            holder.tvStatusKehadiran.setTextColor(ContextCompat.getColor(context, R.color.status_izin))
                            holder.cardGuru.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_status_izin))
                }
                "sakit" -> {
                    holder.tvStatusKehadiran.text = "Sakit"
                            holder.tvStatusKehadiran.setTextColor(ContextCompat.getColor(context, R.color.status_sakit))
                            holder.cardGuru.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_status_sakit))
                }
                else -> {
                    holder.tvStatusKehadiran.text = "Belum Diisi"
                            holder.tvStatusKehadiran.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
                            holder.cardGuru.setCardBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
                }
            }

            // Show guru pengganti if available
            val guruPenggantiName = item.guruPengganti?.nama
            if (!guruPenggantiName.isNullOrEmpty()) {
                holder.tvGuruPengganti.text = "Diganti: $guruPenggantiName"
                holder.tvGuruPengganti.visibility = View.VISIBLE
            } else if (!item.keterangan.isNullOrEmpty()) {
                holder.tvGuruPengganti.text = item.keterangan
                holder.tvGuruPengganti.visibility = View.VISIBLE
            } else {
                holder.tvGuruPengganti.visibility = View.GONE
            }

            // Show input by (siswa/admin) if available
            if (!item.inputBy.isNullOrEmpty()) {
                if (item.inputBy.equals("kurikulum", ignoreCase = true)) {
                    val kurikulumName = item.inputByKurikulumName
                    val display = if (!kurikulumName.isNullOrBlank()) "Diinput oleh: Kurikulum - $kurikulumName" else "Diinput oleh: Kurikulum"
                    holder.tvInputBy.text = display
                } else {
                    val by = item.inputBy.replaceFirstChar { it.titlecase() }
                    holder.tvInputBy.text = "Diinput oleh: $by"
                }
                holder.tvInputBy.visibility = View.VISIBLE
            } else {
                holder.tvInputBy.visibility = View.GONE
            }

            // Show tanggal for admin or siswa input
            val siswaTanggal = item.jadwalId?.let { siswaMapGlobal[it]?.tanggal }
            // Prefer siswaTanggal when available; if input was by kurikulum, fallback to currentTanggal
            val tanggalToShow = when {
                !siswaTanggal.isNullOrEmpty() -> siswaTanggal
                item.inputBy?.equals("kurikulum", ignoreCase = true) == true -> currentTanggal
                else -> null
            }
            if (!tanggalToShow.isNullOrEmpty()) {
                holder.tvTanggal.text = "Tanggal: $tanggalToShow"
                holder.tvTanggal.visibility = View.VISIBLE
            } else {
                holder.tvTanggal.visibility = View.GONE
            }

            // Highlight admin entries visually
            if (item.inputBy?.equals("kurikulum", ignoreCase = true) == true) {
                // Slightly different background to indicate admin-entered Izin/Sakit
                holder.cardGuru.setCardElevation(2f)
                holder.cardGuru.strokeWidth = 2
                holder.cardGuru.strokeColor = ContextCompat.getColor(context, R.color.primary)
            } else {
                holder.cardGuru.strokeWidth = 0
            }
        }

        override fun getItemCount(): Int = guruList.size
    }
}
