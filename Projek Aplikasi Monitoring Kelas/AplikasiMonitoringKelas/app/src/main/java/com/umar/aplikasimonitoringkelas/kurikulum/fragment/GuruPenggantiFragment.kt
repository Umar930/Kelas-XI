package com.umar.aplikasimonitoringkelas.kurikulum.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.api.RetrofitClient
import com.umar.aplikasimonitoringkelas.data.model.GuruTersediaItem
import com.umar.aplikasimonitoringkelas.data.model.KurikulumRequestPengganti
import com.umar.aplikasimonitoringkelas.data.model.PilihGuruRequest
import com.umar.aplikasimonitoringkelas.data.model.TolakRequestBody
import com.umar.aplikasimonitoringkelas.kurikulum.KurikulumDashboardActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Fragment untuk menampilkan dan assign guru pengganti
 * TAB 1 - Guru Pengganti
 */
class GuruPenggantiFragment : Fragment() {

    private lateinit var rvGuruPengganti: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutEmpty: LinearLayout
    private lateinit var spinnerHari: AutoCompleteTextView
    private lateinit var spinnerKelas: AutoCompleteTextView
    private lateinit var spinnerGuru: AutoCompleteTextView

    private var currentHari: String = "Senin"
    private var currentKelasId: Int = 0
    private var currentTanggal: String? = null
    
    private val requestList = mutableListOf<KurikulumRequestPengganti>()
    private var guruTersediaList: List<GuruTersediaItem> = emptyList()

    companion object {
        private const val TAG = "GuruPenggantiFragment"

        fun newInstance(): GuruPenggantiFragment {
            return GuruPenggantiFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_guru_pengganti, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvGuruPengganti = view.findViewById(R.id.rvGuruPengganti)
        progressBar = view.findViewById(R.id.progressBar)
        layoutEmpty = view.findViewById(R.id.layoutEmpty)

        rvGuruPengganti.layoutManager = LinearLayoutManager(requireContext())

        // Ensure RecyclerView scrolls fully (avoid being hidden by bottom nav)
        view.post {
            val bottomNav = activity?.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigation)
            val defaultPad = resources.getDimensionPixelSize(R.dimen.recycler_bottom_padding_nav)
            val extraPad = resources.getDimensionPixelSize(R.dimen.recycler_bottom_padding_extra)
            val bottomNavHeight = bottomNav?.height ?: defaultPad
            rvGuruPengganti.setPadding(rvGuruPengganti.paddingLeft, rvGuruPengganti.paddingTop, rvGuruPengganti.paddingRight, bottomNavHeight + extraPad)
        }

        // Bind filters
        spinnerHari = view.findViewById(R.id.spinnerHari)
        spinnerKelas = view.findViewById(R.id.spinnerKelas)
        spinnerGuru = view.findViewById(R.id.spinnerGuru)

        setupFilters()

        // Get initial filter from activity
        val activity = requireActivity() as? KurikulumDashboardActivity
        activity?.let {
            currentHari = it.getSelectedHari()
            currentKelasId = it.getSelectedKelasId()
            loadRequestPengganti()
        }
    }

    fun refreshData(hari: String, kelasId: Int) {
        currentHari = hari
        currentKelasId = kelasId
        if (isAdded) {
            loadRequestPengganti()
        }
    }

    private fun setupFilters() {
        // Setup hari
        val hariList = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")
        val hariAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, hariList)
        spinnerHari.setAdapter(hariAdapter)
        spinnerHari.setText(currentHari, false)
        spinnerHari.setOnItemClickListener { _, _, position, _ ->
            currentHari = hariList[position]
            loadRequestPengganti()
        }

        // Load kelas and guru lists
        loadKelasList()
        loadGuruList()
    }

    private fun loadKelasList() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val apiService = RetrofitClient.getApiService(requireContext())
                val response = apiService.getKurikulumListKelas()
                if (response.isSuccessful && response.body()?.success == true) {
                    val kelas = response.body()?.data ?: emptyList()
                    val kelasNames = kelas.map { it.nama ?: "" }
                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, kelasNames)
                    spinnerKelas.setAdapter(adapter)
                    spinnerKelas.setText(kelasNames.firstOrNull() ?: "", false)
                    spinnerKelas.setOnItemClickListener { _, _, position, _ ->
                        currentKelasId = kelas[position].id ?: 0
                        loadRequestPengganti()
                    }
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Log.e(TAG, "Error loading kelas: ${e.message}", e)
            }
        }
    }

    private fun loadGuruList() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val apiService = RetrofitClient.getApiService(requireContext())
                val response = apiService.getGuruList()
                if (response.isSuccessful && response.body()?.success == true) {
                        val guruData = response.body()?.data ?: emptyList()
                        // Deduplicate by kodeGuru, and format label: "kode - nama" or fallback to kode if nama missing
                        val dedup = guruData.distinctBy { it.kodeGuru }
                        val guruNames = listOf("Semua Guru") + dedup.map { g -> if (g.nama.isNullOrBlank()) g.kodeGuru!! else "${g.kodeGuru} - ${g.nama}" }
                        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, guruNames)
                        spinnerGuru.setAdapter(adapter)
                        // Preselect stored selection from dashboard activity if available
                        val parent = activity as? KurikulumDashboardActivity
                        val sel = parent?.getSelectedGuruName().orEmpty()
                        if (sel.isNotBlank()) {
                            val idx = guruNames.indexOfFirst { it.startsWith(sel) }
                            if (idx >= 0) spinnerGuru.setText(guruNames[idx], false) else spinnerGuru.setText("Semua Guru", false)
                        } else {
                            spinnerGuru.setText("Semua Guru", false)
                        }
                        spinnerGuru.setOnItemClickListener { _, _, position, _ ->
                            // Filter will be applied locally after loading data
                            loadRequestPengganti()
                        }
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Log.e(TAG, "Error loading guru: ${e.message}", e)
            }
        }
    }

    private fun loadRequestPengganti() {
        progressBar.visibility = View.VISIBLE
        layoutEmpty.visibility = View.GONE
        rvGuruPengganti.visibility = View.GONE

            viewLifecycleOwner.lifecycleScope.launch {
            try {
                val apiService = RetrofitClient.getApiService(requireContext())
                
                // load all statuses so kurikulum can see pending/aktif/ditolak
                val response = apiService.getKurikulumRequestPengganti(
                    status = null,
                    tanggal = currentTanggal,
                    kelasId = if (currentKelasId > 0) currentKelasId else null
                )

                if (response.isSuccessful && response.body()?.success == true) {
                    val rawList = response.body()?.data ?: emptyList()
                    // Apply optional local filter by selected guru
                    val selectedGuruFull = spinnerGuru.text.toString().trim()
                    val selectedGuruName = if (selectedGuruFull.contains(" - ")) selectedGuruFull.substringBefore(" - ").trim() else selectedGuruFull
                    requestList.clear()
                    if (selectedGuruName.isNotEmpty()) {
                        requestList.addAll(rawList.filter {
                            val kode = it.guruTidakHadir?.kodeGuru ?: ""
                            val nama = it.guruTidakHadir?.nama ?: ""
                            kode.equals(selectedGuruName, ignoreCase = true) || nama.contains(selectedGuruName, ignoreCase = true)
                        })
                    } else {
                        requestList.addAll(rawList)
                    }

                    Log.d(TAG, "Loaded ${requestList.size} pending requests")

                    if (requestList.isNotEmpty()) {
                        rvGuruPengganti.adapter = RequestPenggantiAdapter()
                        rvGuruPengganti.visibility = View.VISIBLE
                        layoutEmpty.visibility = View.GONE
                    } else {
                        rvGuruPengganti.visibility = View.GONE
                        layoutEmpty.visibility = View.VISIBLE
                    }
                } else {
                    Log.e(TAG, "API error: ${response.code()} - ${response.message()}")
                    layoutEmpty.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Log.e(TAG, "Error loading requests: ${e.message}", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                layoutEmpty.visibility = View.VISIBLE
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun loadGuruTersedia(request: KurikulumRequestPengganti, onLoaded: (List<GuruTersediaItem>) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val apiService = RetrofitClient.getApiService(requireContext())
                
                val hari = request.jadwal?.hari ?: currentHari
                val jamMulai = request.jadwal?.jamMulai ?: "07:00"
                val jamSelesai = request.jadwal?.jamSelesai ?: "08:00"
                val tanggal = request.tanggal ?: SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(java.util.Date())
                
                val response = apiService.getKurikulumGuruTersedia(
                    hari = hari,
                    jamMulai = jamMulai,
                    jamSelesai = jamSelesai,
                    tanggal = tanggal
                )

                if (response.isSuccessful && response.body()?.success == true) {
                    val guruList = response.body()?.data?.guruTersedia ?: emptyList()
                    guruTersediaList = guruList
                    onLoaded(guruList)
                } else {
                    Toast.makeText(requireContext(), "Gagal memuat guru tersedia", Toast.LENGTH_SHORT).show()
                    onLoaded(emptyList())
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Log.e(TAG, "Error loading guru tersedia: ${e.message}", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                onLoaded(emptyList())
            }
        }
    }

    private fun showPilihGuruDialog(request: KurikulumRequestPengganti) {
        progressBar.visibility = View.VISIBLE
        
        loadGuruTersedia(request) { guruList ->
            progressBar.visibility = View.GONE
            
            if (guruList.isEmpty()) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Tidak Ada Guru Tersedia")
                    .setMessage("Tidak ada guru yang tersedia untuk jam tersebut.")
                    .setPositiveButton("OK", null)
                    .show()
                return@loadGuruTersedia
            }

            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_pilih_guru_pengganti, null)
            val tvInfo = dialogView.findViewById<TextView>(R.id.tvInfo)
            val spinnerGuru = dialogView.findViewById<Spinner>(R.id.spinnerGuru)
            val etCatatan = dialogView.findViewById<EditText>(R.id.etCatatan)

            tvInfo.text = "Guru: ${request.guruTidakHadir?.nama ?: "-"}\n" +
                    "Mapel: ${request.jadwal?.mapel ?: "-"}\n" +
                    "Kelas: ${request.jadwal?.kelas ?: "-"}\n" +
                    "Jam: ${request.jadwal?.jam ?: "${request.jadwal?.jamMulai} - ${request.jadwal?.jamSelesai}"}"

            val guruNames = guruList.map { "${it.nama} (${it.kodeGuru ?: "-"})" }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, guruNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGuru.adapter = adapter

            AlertDialog.Builder(requireContext())
                .setTitle("Pilih Guru Pengganti")
                .setView(dialogView)
                .setPositiveButton("Pilih") { _, _ ->
                    val selectedIndex = spinnerGuru.selectedItemPosition
                    if (selectedIndex >= 0 && selectedIndex < guruList.size) {
                        val selectedGuru = guruList[selectedIndex]
                        val catatan = etCatatan.text.toString().takeIf { it.isNotBlank() }
                        pilihGuruPengganti(request.id, selectedGuru.id, catatan)
                    }
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    private fun pilihGuruPengganti(requestId: Int, guruId: Int, catatan: String?) {
        progressBar.visibility = View.VISIBLE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val apiService = RetrofitClient.getApiService(requireContext())
                
                val response = apiService.pilihGuruPengganti(
                    PilihGuruRequest(
                        requestId = requestId,
                        guruPenggantiId = guruId,
                        catatan = catatan
                    )
                )

                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(requireContext(), "Guru pengganti berhasil dipilih", Toast.LENGTH_SHORT).show()
                    loadRequestPengganti()
                } else {
                    Toast.makeText(requireContext(), "Gagal: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Log.e(TAG, "Error assigning guru: ${e.message}", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showTolakDialog(request: KurikulumRequestPengganti) {
        val editText = EditText(requireContext()).apply {
            hint = "Alasan penolakan (opsional)"
            setPadding(48, 32, 48, 32)
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Tolak Request")
            .setMessage("Apakah Anda yakin ingin menolak request dari ${request.requestedBy?.nama ?: "siswa"}?")
            .setView(editText)
            .setPositiveButton("Tolak") { _, _ ->
                val alasan = editText.text.toString().takeIf { it.isNotBlank() }
                tolakRequest(request.id, alasan)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun tolakRequest(requestId: Int, alasan: String?) {
        progressBar.visibility = View.VISIBLE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val apiService = RetrofitClient.getApiService(requireContext())
                
                val response = apiService.tolakRequestPengganti(
                    TolakRequestBody(
                        requestId = requestId,
                        alasan = alasan
                    )
                )

                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(requireContext(), "Request berhasil ditolak", Toast.LENGTH_SHORT).show()
                    loadRequestPengganti()
                } else {
                    Toast.makeText(requireContext(), "Gagal: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Log.e(TAG, "Error rejecting request: ${e.message}", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // No manual cancellation necessary; lifecycleScope cancels automatically
    }

    // ========== ADAPTER ==========
    inner class RequestPenggantiAdapter : RecyclerView.Adapter<RequestPenggantiAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val cardRequest: MaterialCardView = view.findViewById(R.id.cardRequest)
            val tvJamKe: TextView = view.findViewById(R.id.tvJamKe)
            val tvNamaGuru: TextView = view.findViewById(R.id.tvNamaGuru)
            val tvNamaMapel: TextView = view.findViewById(R.id.tvNamaMapel)
            val tvNamaKelas: TextView = view.findViewById(R.id.tvNamaKelas)
            val tvPesan: TextView = view.findViewById(R.id.tvPesan)
            val tvStatus: TextView = view.findViewById(R.id.tvStatus)
            val btnAssign: MaterialButton = view.findViewById(R.id.btnAssign)
            val btnTolak: MaterialButton? = view.findViewById(R.id.btnTolak)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_kurikulum_request, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = requestList[position]
            val context = holder.itemView.context

            // Format jam
            val jam = item.jadwal?.jam ?: "${item.jadwal?.jamMulai ?: ""} - ${item.jadwal?.jamSelesai ?: ""}"
            holder.tvJamKe.text = jam.ifBlank { "Jam -" }
            
            holder.tvNamaGuru.text = item.guruTidakHadir?.nama ?: "Guru tidak diketahui"
            holder.tvNamaMapel.text = item.jadwal?.mapel ?: "-"
            holder.tvNamaKelas.text = item.jadwal?.kelas ?: "-"
            holder.tvPesan.text = item.keterangan ?: "Mohon dicarikan guru pengganti"

            // Status
            when (item.status?.lowercase()) {
                "pending" -> {
                    holder.tvStatus.text = "Menunggu"
                    holder.tvStatus.setTextColor(context.getColor(R.color.status_izin))
                    holder.btnAssign.visibility = View.VISIBLE
                    holder.btnTolak?.visibility = View.VISIBLE
                }
                "approved", "disetujui", "aktif" -> {
                    // 'aktif' indicates replacement has been made and is in effect
                    holder.tvStatus.text = if (item.status?.lowercase() == "aktif") "Aktif" else "Disetujui"
                    holder.tvStatus.setTextColor(context.getColor(R.color.status_hadir))
                    holder.btnAssign.visibility = View.GONE
                    holder.btnTolak?.visibility = View.GONE
                }
                "rejected", "ditolak" -> {
                    holder.tvStatus.text = "Ditolak"
                    holder.tvStatus.setTextColor(context.getColor(R.color.status_tidak_hadir))
                    holder.btnAssign.visibility = View.GONE
                    holder.btnTolak?.visibility = View.GONE
                }
                else -> {
                    holder.tvStatus.text = item.status ?: "Pending"
                    holder.btnAssign.visibility = View.VISIBLE
                    holder.btnTolak?.visibility = View.VISIBLE
                }
            }

            holder.btnAssign.setOnClickListener {
                showPilihGuruDialog(item)
            }

            holder.btnTolak?.setOnClickListener {
                showTolakDialog(item)
            }
        }

        override fun getItemCount(): Int = requestList.size
    }
}
