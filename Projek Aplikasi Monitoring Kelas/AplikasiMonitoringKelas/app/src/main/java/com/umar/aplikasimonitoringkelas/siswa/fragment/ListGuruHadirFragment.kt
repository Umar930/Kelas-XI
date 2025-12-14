package com.umar.aplikasimonitoringkelas.siswa.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.SiswaDashboardActivity
import com.umar.aplikasimonitoringkelas.data.api.RetrofitClient
import com.umar.aplikasimonitoringkelas.data.model.KehadiranItem
import com.umar.aplikasimonitoringkelas.data.model.RequestGuruPenggantiRequest
import com.umar.aplikasimonitoringkelas.data.model.InputKehadiranRequest
import com.umar.aplikasimonitoringkelas.siswa.adapter.ListGuruHadirAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment untuk menampilkan daftar kehadiran guru
 */
class ListGuruHadirFragment : Fragment() {

    private lateinit var rvListGuru: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutEmpty: LinearLayout

    private val scope = CoroutineScope(Dispatchers.Main)
    private var currentHari: String = "Senin"
    private var currentKelasId: Int = 0

    companion object {
        private const val TAG = "ListGuruHadirFragment"

        fun newInstance(): ListGuruHadirFragment {
            return ListGuruHadirFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_siswa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvListGuru = view.findViewById(R.id.rvListGuru)
        progressBar = view.findViewById(R.id.progressBar)
        layoutEmpty = view.findViewById(R.id.layoutEmpty)

        rvListGuru.layoutManager = LinearLayoutManager(requireContext())

        view.post {
            val bottomNav = activity?.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigation)
            val defaultPad = resources.getDimensionPixelSize(R.dimen.recycler_bottom_padding_nav)
            val extraPad = resources.getDimensionPixelSize(R.dimen.recycler_bottom_padding_extra)
            val bottomNavHeight = bottomNav?.height ?: defaultPad
            rvListGuru.setPadding(rvListGuru.paddingLeft, rvListGuru.paddingTop, rvListGuru.paddingRight, bottomNavHeight + extraPad)
        }

        // Get initial filter from activity
        val activity = requireActivity() as? SiswaDashboardActivity
        activity?.let {
            currentHari = it.getSelectedHari()
            currentKelasId = it.getSelectedKelasId()
            if (currentKelasId > 0) {
                loadKehadiranList()
            }
        }
    }

    fun refreshData(hari: String, kelasId: Int) {
        currentHari = hari
        currentKelasId = kelasId
        if (isAdded && currentKelasId > 0) {
            loadKehadiranList()
        }
    }

    private fun loadKehadiranList() {
        progressBar.visibility = View.VISIBLE
        layoutEmpty.visibility = View.GONE
        rvListGuru.visibility = View.GONE

        scope.launch {
            try {
                val apiService = RetrofitClient.getApiService(requireContext())
                
                // Use /api/siswa/list-kehadiran?kelas_id=xxx endpoint
                val response = apiService.getSiswaListKehadiran(kelasId = currentKelasId)

                if (response.isSuccessful && response.body()?.success == true) {
                    val kehadiranList = response.body()?.data ?: emptyList()

                    Log.d(TAG, "Loaded ${kehadiranList.size} kehadiran records")

                    if (kehadiranList.isNotEmpty()) {
                        rvListGuru.adapter = ListGuruHadirAdapter(
                            dataList = kehadiranList,
                            onInputPenggantiClick = { kehadiran -> showInputPenggantiDialog(kehadiran) }
                        )
                        rvListGuru.visibility = View.VISIBLE
                        layoutEmpty.visibility = View.GONE
                    } else {
                        rvListGuru.visibility = View.GONE
                        layoutEmpty.visibility = View.VISIBLE
                    }
                } else {
                    Log.e(TAG, "API error: ${response.code()} - ${response.message()}")
                    Toast.makeText(requireContext(), "Gagal memuat data: ${response.body()?.message ?: response.message()}", Toast.LENGTH_SHORT).show()
                    layoutEmpty.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading kehadiran: ${e.message}", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                layoutEmpty.visibility = View.VISIBLE
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showInputPenggantiDialog(kehadiran: KehadiranItem) {
        // If there's already a replacement and it's active, open attendance input for the replacement
        val rpStatus = kehadiran.requestPengganti?.status?.lowercase()
        if (kehadiran.hasRequestPengganti && (rpStatus == "aktif" || rpStatus == "approved" || rpStatus == "disetujui")) {
            showInputKehadiranDialogFromList(kehadiran)
            return
        }

        // Otherwise show the request dialog (pending or no request)
        val namaGuru = kehadiran.namaGuru ?: kehadiran.guru ?: "Guru"

        // Create EditText for pesan input
        val editTextPesan = android.widget.EditText(requireContext()).apply {
            hint = "Tulis pesan ke kurikulum..."
            minLines = 3
            gravity = android.view.Gravity.TOP
            setPadding(48, 32, 48, 32)
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Request Guru Pengganti")
            .setMessage("Guru $namaGuru tidak hadir. Kirim pesan ke kurikulum untuk meminta guru pengganti.")
            .setView(editTextPesan)
            .setPositiveButton("Kirim") { _, _ ->
                val pesan = editTextPesan.text.toString().trim()
                if (pesan.isNotEmpty()) {
                    submitGuruPengganti(kehadiran, pesan)
                } else {
                    Toast.makeText(requireContext(), "Pesan tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showInputKehadiranDialogFromList(kehadiran: KehadiranItem) {
        val tanggal = kehadiran.tanggal ?: SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val dialogView = layoutInflater.inflate(R.layout.dialog_input_kehadiran, null)

        val tvGuruMapel = dialogView.findViewById<TextView>(R.id.tvGuruMapel)
        val tvJam = dialogView.findViewById<TextView>(R.id.tvJam)
        val rgStatus = dialogView.findViewById<android.widget.RadioGroup>(R.id.rgStatus)
        val rbHadir = dialogView.findViewById<android.widget.RadioButton>(R.id.rbHadir)
        val rbTidakHadir = dialogView.findViewById<android.widget.RadioButton>(R.id.rbTidakHadir)
        val edtKeterangan = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.edtKeterangan)
        val btnSubmit = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSubmit)
        val btnCancel = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnCancel)

        // Show the replacement teacher info if available
        val rp = kehadiran.requestPengganti
        val displayName = rp?.displayGuru ?: rp?.guruPengganti ?: "(Belum ditentukan)"
        val displayKode = rp?.displayKodeGuru ?: ""
        tvGuruMapel.text = if (displayKode.isNotBlank()) "$displayKode - $displayName" else displayName
        tvJam.text = kehadiran.jam ?: ""

        val dialog = AlertDialog.Builder(requireContext())
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

        btnCancel.setOnClickListener { dialog.dismiss() }

        btnSubmit.setOnClickListener {
            val status = if (rbHadir.isChecked) "hadir" else "tidak_hadir"
            val keterangan = edtKeterangan.text.toString()

            submitKehadiranFromList(kehadiran, status, keterangan, tanggal, dialog)
        }

        dialog.show()
    }

    private fun submitKehadiranFromList(
        kehadiran: KehadiranItem,
        status: String,
        keterangan: String?,
        tanggal: String,
        dialog: AlertDialog
    ) {
        progressBar.visibility = View.VISIBLE

        scope.launch {
            try {
                val apiService = RetrofitClient.getApiService(requireContext())

                // Use replacement guru id if provided by request (server will also accept jadwal and mapel)
                val guruId = kehadiran.requestPengganti?.guruPenggantiId ?: kehadiran.guruId

                val request = InputKehadiranRequest(
                    jadwalId = kehadiran.jadwalId,
                    guruId = guruId,
                    kelasId = currentKelasId,
                    mapelId = kehadiran.mapelId,
                    tanggal = tanggal,
                    status = status,
                    keterangan = if (keterangan.isNullOrBlank()) null else keterangan
                )

                val response = apiService.inputKehadiranGuru(request)

                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(requireContext(), "Kehadiran berhasil diinput", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    loadKehadiranList()
                } else {
                    Toast.makeText(requireContext(), response.body()?.message ?: "Gagal input kehadiran", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error submit kehadiran from list: ${e.message}", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun submitGuruPengganti(kehadiran: KehadiranItem, pesan: String) {
        progressBar.visibility = View.VISIBLE

        scope.launch {
            try {
                val apiService = RetrofitClient.getApiService(requireContext())
                
                val kehadiranGuruId = kehadiran.kehadiranId ?: kehadiran.id
                val request = RequestGuruPenggantiRequest(
                    kehadiranGuruId = kehadiranGuruId,
                    pesan = pesan
                )
                
                val response = apiService.requestGuruPengganti(request)

                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(requireContext(), "Request guru pengganti berhasil dikirim", Toast.LENGTH_SHORT).show()
                    loadKehadiranList() // Refresh data
                } else {
                    Toast.makeText(requireContext(), "Gagal: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error submitting guru pengganti: ${e.message}", e)
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
