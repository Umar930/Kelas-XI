package com.umar.aplikasimonitoringkelas.siswa.fragment

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.SiswaDashboardActivity
import com.umar.aplikasimonitoringkelas.data.api.RetrofitClient
import com.umar.aplikasimonitoringkelas.data.model.*
import com.umar.aplikasimonitoringkelas.siswa.adapter.EntriKehadiranAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import kotlin.math.max
import java.util.*

class EntriKehadiranFragment : Fragment() {

    private lateinit var rvEntri: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutEmpty: LinearLayout
    private lateinit var fabTambahJadwal: FloatingActionButton

    private val scope = CoroutineScope(Dispatchers.Main)
    private var currentHari: String = "Senin"
    private var currentKelasId: Int = 0

    private var guruList = listOf<Guru>()
    private var mapelList = listOf<Mapel>()
    private var kelasList = listOf<Kelas>()
    private val hariList = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")

    companion object {
        private const val TAG = "EntriKehadiranFragment"

        fun newInstance(): EntriKehadiranFragment {
            return EntriKehadiranFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_entri_siswa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvEntri = view.findViewById(R.id.rvEntri)
        progressBar = view.findViewById(R.id.progressBar)
        layoutEmpty = view.findViewById(R.id.layoutEmpty)
        fabTambahJadwal = view.findViewById(R.id.fabTambahJadwal)

        rvEntri.layoutManager = LinearLayoutManager(requireContext())

        view.post {
            val bottomNav = activity?.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigation)
            val defaultPad = resources.getDimensionPixelSize(R.dimen.recycler_bottom_padding_nav)
            val extraPad = resources.getDimensionPixelSize(R.dimen.recycler_bottom_padding_extra)
            val bottomNavHeight = bottomNav?.height ?: defaultPad
            rvEntri.setPadding(rvEntri.paddingLeft, rvEntri.paddingTop, rvEntri.paddingRight, bottomNavHeight + extraPad)
            // Adjust FAB position so it is above the bottom navigation
            val fabExtra = resources.getDimensionPixelSize(R.dimen.recycler_bottom_padding_fab)
            val fabParams = fabTambahJadwal.layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
            val desiredBottomMargin = max(bottomNavHeight + extraPad, fabExtra)
            // Ensure fab margin is at least the dimension to show fully
            if (fabParams.bottomMargin < desiredBottomMargin) {
                fabParams.setMargins(fabParams.leftMargin, fabParams.topMargin, fabParams.rightMargin, desiredBottomMargin)
                fabTambahJadwal.layoutParams = fabParams
            }
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

        fabTambahJadwal.setOnClickListener {
            showTambahJadwalDialog()
        }
    }


    fun refreshData(hari: String, kelasId: Int) {
        currentHari = hari
        currentKelasId = kelasId
        if (isAdded && currentKelasId > 0) {
            loadJadwal()
        }
    }


    
    private fun loadGuruList() {
        scope.launch {
            try {
                val apiService = RetrofitClient.getApiService(requireContext())
                val response = apiService.getGuruList()
                if (response.isSuccessful) {
                    val rawGuruList = response.body()?.data ?: emptyList()
                    // Convert to data.model.Guru
                    guruList = rawGuruList.mapNotNull { guru ->
                        try {
                            Guru(
                                id = guru.id ?: 0,
                                nama = guru.nama ?: "",
                                kodeGuru = guru.kodeGuru ?: "",
                                email = guru.email,
                                noTelepon = guru.noTelepon
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                    Log.d(TAG, "Loaded ${guruList.size} guru")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading guru: ${e.message}", e)
            }
        }
    }

    private fun loadMapelList() {
        scope.launch {
            try {
                val apiService = RetrofitClient.getApiService(requireContext())
                val response = apiService.getSiswaListMapel()
                if (response.isSuccessful) {
                    mapelList = response.body()?.data ?: emptyList()
                    Log.d(TAG, "Loaded ${mapelList.size} mapel")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading mapel: ${e.message}", e)
            }
        }
    }

    private fun loadKelasList() {
        scope.launch {
            try {
                val apiService = RetrofitClient.getApiService(requireContext())
                val response = apiService.getSiswaListKelas()
                if (response.isSuccessful) {
                    kelasList = response.body()?.data?.map { kelasItem ->
                        Kelas(
                            id = kelasItem.id,
                            kodeKelas = kelasItem.kodeKelas ?: "",
                            namaKelas = kelasItem.namaKelas ?: kelasItem.nama ?: "",
                            tingkat = "",
                            jurusan = ""
                        )
                    } ?: emptyList()
                    Log.d(TAG, "Loaded ${kelasList.size} kelas")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading kelas: ${e.message}", e)
            }
        }
    }

    private fun showTambahJadwalDialog() {
        if (guruList.isEmpty() || mapelList.isEmpty() || kelasList.isEmpty()) {
            Toast.makeText(requireContext(), "Data belum lengkap, mohon tunggu...", Toast.LENGTH_SHORT).show()
            loadGuruList()
            loadMapelList()
            loadKelasList()
            return
        }

        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_tambah_jadwal, null)

        val spinnerKelas = dialogView.findViewById<MaterialAutoCompleteTextView>(R.id.spinnerKelas)
        val spinnerMapel = dialogView.findViewById<MaterialAutoCompleteTextView>(R.id.spinnerMapel)
        val spinnerGuru = dialogView.findViewById<MaterialAutoCompleteTextView>(R.id.spinnerGuru)
        val spinnerHari = dialogView.findViewById<MaterialAutoCompleteTextView>(R.id.spinnerHari)
        val etJamMulai = dialogView.findViewById<TextInputEditText>(R.id.etJamMulai)
        val etJamSelesai = dialogView.findViewById<TextInputEditText>(R.id.etJamSelesai)
        val dialogProgressBar = dialogView.findViewById<ProgressBar>(R.id.progressBar)

        // Setup adapters
        val kelasNames = kelasList.map { it.namaKelas }
        spinnerKelas.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, kelasNames))

        val mapelNames = mapelList.map { "${it.kodeMapel ?: ""} - ${it.nama ?: it.mapel ?: ""}" }
        spinnerMapel.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, mapelNames))

        val guruNames = guruList.map { "${it.kodeGuru} - ${it.nama}" }
        spinnerGuru.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, guruNames))

        spinnerHari.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, hariList))

        // Track selected IDs
        var selectedKelasId: Int? = null
        var selectedMapelId: Int? = null
        var selectedGuruId: Int? = null
        var selectedHari: String? = null
        var existingJadwalList: List<JadwalItem> = emptyList()

        spinnerKelas.setOnItemClickListener { _, _, position, _ ->
            selectedKelasId = kelasList[position].id
            // Fetch existing jadwal for this class+hari if hari selected
            val hariLocal = selectedHari
            val idLocal = selectedKelasId
            if (hariLocal != null && idLocal != null) {
                scope.launch {
                    try {
                        val apiService = RetrofitClient.getApiService(requireContext())
                        val resp = apiService.getSiswaJadwal(hari = hariLocal, kelasId = idLocal)
                        if (resp.isSuccessful && resp.body()?.success == true) {
                            existingJadwalList = resp.body()?.data ?: emptyList()
                        }
                    } catch (e: Exception) {
                        // ignore
                    }
                }
            }
        }
        spinnerMapel.setOnItemClickListener { _, _, position, _ ->
            selectedMapelId = mapelList[position].id
        }
        spinnerGuru.setOnItemClickListener { _, _, position, _ ->
            selectedGuruId = guruList[position].id
        }
        spinnerHari.setOnItemClickListener { _, _, position, _ ->
            selectedHari = hariList[position]
            // Fetch existing jadwal for this class+hari if class selected
            val hariLocal = selectedHari
            val idLocal = selectedKelasId
            if (hariLocal != null && idLocal != null) {
                scope.launch {
                    try {
                        val apiService = RetrofitClient.getApiService(requireContext())
                        val resp = apiService.getSiswaJadwal(hari = hariLocal, kelasId = idLocal)
                        if (resp.isSuccessful && resp.body()?.success == true) {
                            existingJadwalList = resp.body()?.data ?: emptyList()
                        }
                    } catch (e: Exception) {
                        // ignore
                    }
                }
            }
        }

        // Try to pre-select Kelas and Hari based on current activity selection
        val activity = requireActivity() as? SiswaDashboardActivity
        val selectedKelasFromActivity = activity?.getSelectedKelasId()
        val selectedHariFromActivity = activity?.getSelectedHari()
        if (selectedKelasFromActivity != null && selectedKelasFromActivity > 0) {
            val index = kelasList.indexOfFirst { it.id == selectedKelasFromActivity }
            if (index >= 0) {
                spinnerKelas.setText(kelasNames[index], false)
                selectedKelasId = selectedKelasFromActivity
            }
        }
        if (!selectedHariFromActivity.isNullOrEmpty()) {
            val index = hariList.indexOfFirst { it == selectedHariFromActivity }
            if (index >= 0) {
                spinnerHari.setText(hariList[index], false)
                selectedHari = hariList[index]
            }
        }

        // If we have both, fetch existing jadwal
        if (selectedHari != null && selectedKelasId != null) {
            scope.launch {
                try {
                    val apiService = RetrofitClient.getApiService(requireContext())
                    val resp = apiService.getSiswaJadwal(hari = selectedHari, kelasId = selectedKelasId)
                    if (resp.isSuccessful && resp.body()?.success == true) {
                        existingJadwalList = resp.body()?.data ?: emptyList()
                    }
                } catch (e: Exception) {
                    // ignore
                }
            }
        }

        // Time pickers
        etJamMulai.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(requireContext(), { _, hour, minute ->
                etJamMulai.setText(String.format("%02d:%02d", hour, minute))
                // After setting time, check conflicts
                checkLocalConflicts(etJamMulai.text.toString(), etJamSelesai.text.toString(), existingJadwalList, dialogView)
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        etJamSelesai.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(requireContext(), { _, hour, minute ->
                etJamSelesai.setText(String.format("%02d:%02d", hour, minute))
                // After setting time, check conflicts
                checkLocalConflicts(etJamMulai.text.toString(), etJamSelesai.text.toString(), existingJadwalList, dialogView)
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialogView.findViewById<View>(R.id.btnBatal).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<View>(R.id.btnSimpan).setOnClickListener {
            // Validate
            if (selectedKelasId == null) {
                Toast.makeText(requireContext(), "Pilih kelas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Mapel and Guru will be resolved from spinner text if not selected explicitly
            if (selectedHari == null) {
                Toast.makeText(requireContext(), "Pilih hari", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (etJamMulai.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Isi jam mulai", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (etJamSelesai.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Isi jam selesai", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Resolve selected IDs from spinner tags or list as fallback if not set
            var resolvedKelasId = selectedKelasId
            var resolvedMapelId = selectedMapelId
            var resolvedGuruId = selectedGuruId
            try {
                if (resolvedKelasId == null) {
                    val spinKelas = dialogView.findViewById<com.google.android.material.textfield.MaterialAutoCompleteTextView>(R.id.spinnerKelas)
                    val kelasText = spinKelas.text?.toString()
                    val idxK = kelasList.indexOfFirst { it.namaKelas == kelasText }
                    if (idxK >= 0) resolvedKelasId = kelasList[idxK].id
                }
                if (resolvedMapelId == null) {
                    val spinMapel = dialogView.findViewById<com.google.android.material.textfield.MaterialAutoCompleteTextView>(R.id.spinnerMapel)
                    val mapelText = spinMapel.text?.toString()
                    val idxM = mapelList.indexOfFirst { (it.nama ?: it.mapel ?: "") == mapelText || ("${it.kodeMapel ?: ""} - ${it.nama ?: it.mapel ?: ""}") == mapelText }
                    if (idxM >= 0) resolvedMapelId = mapelList[idxM].id
                }
                if (resolvedGuruId == null) {
                    val spinGuru = dialogView.findViewById<com.google.android.material.textfield.MaterialAutoCompleteTextView>(R.id.spinnerGuru)
                    val guruText = spinGuru.text?.toString()
                    val idxG = guruList.indexOfFirst { ("${it.kodeGuru} - ${it.nama}") == guruText }
                    if (idxG >= 0) resolvedGuruId = guruList[idxG].id
                }
            } catch (e: Exception) {
                // ignore
            }

            if (resolvedMapelId == null) {
                Toast.makeText(requireContext(), "Pilih mata pelajaran", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (resolvedGuruId == null) {
                Toast.makeText(requireContext(), "Pilih guru", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = TambahJadwalRequest(
                kelasId = selectedKelasId!!,
                mapelId = resolvedMapelId!!,
                guruId = resolvedGuruId!!,
                hari = selectedHari!!,
                jamMulai = etJamMulai.text.toString(),
                jamSelesai = etJamSelesai.text.toString()
            )

            dialogProgressBar.visibility = View.VISIBLE
            tambahJadwal(request, dialog, dialogProgressBar, dialogView, existingJadwalList)
        }

        dialog.show()
    }

    private fun tambahJadwal(request: TambahJadwalRequest, dialog: AlertDialog, dialogProgressBar: ProgressBar, dialogView: View, existingJadwalList: List<JadwalItem>) {
        scope.launch {
            try {
                val apiService = RetrofitClient.getApiService(requireContext())
                val response = apiService.tambahJadwalRaw(request)
                val rawBody = response.body()?.string()

                if (response.isSuccessful) {
                    // Try to parse response as ApiResponse<JadwalKelas>
                    try {
                        val type = object : TypeToken<ApiResponse<JadwalKelas>>() {}.type
                        val apiResp: ApiResponse<JadwalKelas>? = Gson().fromJson(rawBody, type)
                        if (apiResp != null && apiResp.success) {
                            Toast.makeText(requireContext(), "Jadwal berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                            loadJadwal() // Refresh
                        } else {
                            Toast.makeText(requireContext(), "Gagal: ${apiResp?.message ?: rawBody ?: "Unknown response"}", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        // fallback - unknown body type: assume success and refresh
                        Log.w(TAG, "Failed to parse success response: ${e.message}")
                        Toast.makeText(requireContext(), "Jadwal berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                        loadJadwal()
                    }
                } else if (response.code() == 422) {
                    // Handle conflict
                    val errorBody = response.errorBody()?.string() ?: rawBody
                    try {
                        val json = JSONObject(errorBody ?: "{}")
                        val message = json.optString("message", "Jadwal konflik")
                        // Try to parse details: conflicts, suggestions, replaced, cannot_delete
                        val details = json.optJSONObject("data") ?: json.optJSONObject("errors") ?: JSONObject()
                        val conflictsArr = details.optJSONArray("conflicts")
                        val suggestionsArr = details.optJSONArray("suggestions")
                        val replacedArr = details.optJSONArray("replaced")
                        val cannotDeleteArr = details.optJSONArray("cannot_delete")

                        // Build readable text for conflicts
                        val conflictsList = mutableListOf<String>()
                        if (conflictsArr != null) {
                            for (i in 0 until conflictsArr.length()) {
                                val c = conflictsArr.optJSONObject(i)
                                val mapelName = c?.optString("mapel") ?: c?.optString("nama_mapel") ?: ""
                                val guruName = c?.optString("nama_guru") ?: c?.optString("guru") ?: ""
                                val jamMulai = c?.optString("jam_mulai") ?: c?.optString("jamMulai") ?: ""
                                val jamSelesai = c?.optString("jam_selesai") ?: c?.optString("jamSelesai") ?: ""
                                conflictsList.add("${mapelName} - ${guruName} (${jamMulai} - ${jamSelesai})")
                            }
                        }

                        // Build suggestions list
                        val suggestionsList = mutableListOf<Pair<String, JSONObject>>()
                        if (suggestionsArr != null) {
                            for (i in 0 until suggestionsArr.length()) {
                                val s = suggestionsArr.optJSONObject(i) ?: continue
                                val mapelName = s.optString("mapel") ?: s.optString("nama_mapel") ?: ""
                                val guruName = s.optString("nama_guru") ?: s.optString("guru") ?: ""
                                val jamMulai = s.optString("jam_mulai") ?: s.optString("jamMulai") ?: ""
                                val jamSelesai = s.optString("jam_selesai") ?: s.optString("jamSelesai") ?: ""
                                val label = "${mapelName} - ${guruName} (${jamMulai} - ${jamSelesai})"
                                suggestionsList.add(label to s)
                            }
                        }

                        // Show conflict dialog with suggestions
                        val conflictMessage = StringBuilder()
                        conflictMessage.append(message)
                        if (conflictsList.isNotEmpty()) {
                            conflictMessage.append("\n\nKonflik yang ditemukan:\n")
                            conflictsList.forEach { conflictMessage.append("- $it\n") }
                        }
                        if (replacedArr != null && replacedArr.length() > 0) {
                            conflictMessage.append("\n\nDiganti (replaced):\n")
                            for (i in 0 until replacedArr.length()) {
                                val r = replacedArr.optJSONObject(i)
                                val sMapel = r?.optString("mapel") ?: r?.optString("nama_mapel") ?: ""
                                val sGuru = r?.optString("nama_guru") ?: r?.optString("guru") ?: ""
                                val sJam = (r?.optString("jam_mulai") ?: r?.optString("jamMulai") ?: "") + " - " + (r?.optString("jam_selesai") ?: r?.optString("jamSelesai") ?: "")
                                conflictMessage.append("- ${sMapel} - ${sGuru} (${sJam})\n")
                            }
                        }
                        if (cannotDeleteArr != null && cannotDeleteArr.length() > 0) {
                            conflictMessage.append("\n\nTidak dapat dihapus:\n")
                            for (i in 0 until cannotDeleteArr.length()) {
                                val c = cannotDeleteArr.optJSONObject(i)
                                val sMapel = c?.optString("mapel") ?: c?.optString("nama_mapel") ?: ""
                                val sGuru = c?.optString("nama_guru") ?: c?.optString("guru") ?: ""
                                val sJam = (c?.optString("jam_mulai") ?: c?.optString("jamMulai") ?: "") + " - " + (c?.optString("jam_selesai") ?: c?.optString("jamSelesai") ?: "")
                                conflictMessage.append("- ${sMapel} - ${sGuru} (${sJam})\n")
                            }
                        }

                        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
                            .setTitle("Jadwal konflik")
                            .setMessage(conflictMessage.toString())
                            .setNegativeButton("Tutup", null)

                        if (suggestionsList.isNotEmpty()) {
                            // Show as a separate dialog allowing user to pick a suggestion
                            builder.setPositiveButton("Lihat Saran") { _, _ ->
                                val items = suggestionsList.map { it.first }.toTypedArray()
                                androidx.appcompat.app.AlertDialog.Builder(requireContext())
                                    .setTitle("Saran Waktu Alternatif")
                                                            .setItems(items) { _, index ->
                                        val sJson = suggestionsList[index].second
                                        // Fill dialog fields: jam_mulai, jam_selesai, mapel/guru if available
                                        val jamMulai = sJson.optString("jam_mulai") ?: sJson.optString("jamMulai")
                                        val jamSelesai = sJson.optString("jam_selesai") ?: sJson.optString("jamSelesai")
                                        val mapelId = sJson.optInt("mapel_id", -1)
                                        val guruId = sJson.optInt("guru_id", -1)

                                        // Set times
                                        val etJamMulai = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etJamMulai)
                                        val etJamSelesai = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etJamSelesai)
                                        etJamMulai.setText(jamMulai)
                                        etJamSelesai.setText(jamSelesai)

                                        // Set spinner mapel/guru selection if IDs provided
                                        try {
                                            if (mapelId > 0) {
                                                val spinnerMapel = dialogView.findViewById<com.google.android.material.textfield.MaterialAutoCompleteTextView>(R.id.spinnerMapel)
                                                val mapelIndex = mapelList.indexOfFirst { it.id == mapelId }
                                                if (mapelIndex >= 0) {
                                                    spinnerMapel.setText(spinnerMapel.adapter.getItem(mapelIndex).toString(), false)
                                                }
                                            }
                                            if (guruId > 0) {
                                                val spinnerGuru = dialogView.findViewById<com.google.android.material.textfield.MaterialAutoCompleteTextView>(R.id.spinnerGuru)
                                                val guruIndex = guruList.indexOfFirst { it.id == guruId }
                                                if (guruIndex >= 0) {
                                                    spinnerGuru.setText(spinnerGuru.adapter.getItem(guruIndex).toString(), false)
                                                }
                                            }
                                        } catch (e: Exception) {
                                            // ignore
                                        }

                                        // After applying suggestion, re-check for conflicts with updated times
                                        checkLocalConflicts(etJamMulai.text.toString(), etJamSelesai.text.toString(), existingJadwalList, dialogView)
                                        Toast.makeText(requireContext(), "Saran diterapkan", Toast.LENGTH_SHORT).show()
                                    }
                                    .setNegativeButton("Batal", null)
                                    .show()
                            }
                        }

                        builder.show()

                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Jadwal konflik dengan jadwal lain", Toast.LENGTH_LONG).show()
                    }
                } else {
                    val err = response.errorBody()?.string() ?: rawBody ?: response.message()
                    Toast.makeText(requireContext(), "Gagal: ${err}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error tambah jadwal: ${e.message}", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                dialogProgressBar.visibility = View.GONE
            }
        }
    }

    private fun loadJadwal() {
        progressBar.visibility = View.VISIBLE
        layoutEmpty.visibility = View.GONE
        rvEntri.visibility = View.GONE

        scope.launch {
            try {
                val apiService = RetrofitClient.getApiService(requireContext())
                
                // Use /api/siswa/jadwal?hari=xxx&kelas_id=xxx endpoint
                val response = apiService.getSiswaJadwal(hari = currentHari, kelasId = currentKelasId)

                if (response.isSuccessful && response.body()?.success == true) {
                    val jadwalList = response.body()?.data ?: emptyList()

                    Log.d(TAG, "Loaded ${jadwalList.size} jadwal for entri")

                    if (jadwalList.isNotEmpty()) {
                        rvEntri.adapter = EntriKehadiranAdapter(
                            jadwalList = jadwalList,
                            guruList = guruList,
                            mapelList = mapelList,
                            onHadirClick = { jadwal -> inputKehadiran(jadwal, "hadir") },
                            onTidakHadirClick = { jadwal -> inputKehadiran(jadwal, "tidak_hadir") },
                            onSimpanPengganti = { jadwal, guruId, mapelId -> 
                                simpanGuruPengganti(jadwal, guruId, mapelId) 
                            },
                            onDeleteClick = { jadwal -> showDeleteConfirmDialog(jadwal) }
                        )
                        rvEntri.visibility = View.VISIBLE
                        layoutEmpty.visibility = View.GONE
                    } else {
                        rvEntri.visibility = View.GONE
                        layoutEmpty.visibility = View.VISIBLE
                    }
                } else {
                    Log.e(TAG, "API error: ${response.code()} - ${response.message()}")
                    Toast.makeText(requireContext(), "Gagal memuat jadwal: ${response.body()?.message ?: response.message()}", Toast.LENGTH_SHORT).show()
                    layoutEmpty.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading jadwal: ${e.message}", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                layoutEmpty.visibility = View.VISIBLE
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun inputKehadiran(jadwal: JadwalItem, status: String) {
        scope.launch {
            try {
                progressBar.visibility = View.VISIBLE
                
                val tanggal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val request = InputKehadiranRequest(
                    jadwalKelasId = jadwal.id,
                    jadwalId = jadwal.jadwalId,
                    guruId = jadwal.guruId,
                    kelasId = currentKelasId,
                    mapelId = jadwal.mapelId,
                    tanggal = tanggal,
                    status = status,
                    keterangan = null
                )

                val apiService = RetrofitClient.getApiService(requireContext())
                val response = apiService.inputKehadiranGuru(request)

                if (response.isSuccessful && response.body()?.success == true) {
                    val statusText = if (status == "hadir") "Hadir" else "Tidak Hadir"
                    Toast.makeText(requireContext(), "Berhasil input: $statusText", Toast.LENGTH_SHORT).show()
                    loadJadwal() // Refresh
                } else {
                    Toast.makeText(requireContext(), "Gagal: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error input kehadiran: ${e.message}", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun checkLocalConflicts(jamMulaiStr: String?, jamSelesaiStr: String?, existing: List<JadwalItem>, dialogView: View) {
        try {
            val tvConflictInfo = dialogView.findViewById<android.widget.TextView>(R.id.tvConflictInfo)
            if (jamMulaiStr.isNullOrEmpty() || jamSelesaiStr.isNullOrEmpty()) {
                tvConflictInfo.visibility = View.GONE
                return
            }

            val reqStart = timeToMinutes(jamMulaiStr) ?: return
            val reqEnd = timeToMinutes(jamSelesaiStr) ?: return

            val conflicts = mutableListOf<String>()
            for (j in existing) {
                val start = timeToMinutes(j.jamMulai ?: "")
                val end = timeToMinutes(j.jamSelesai ?: "")
                if (start != null && end != null) {
                    if (reqStart < end && reqEnd > start) {
                        val mapel = j.namaMapel ?: j.mapel ?: ""
                        val guru = j.namaGuru ?: j.guru ?: ""
                        val s = "${mapel} - ${guru} (${j.jamMulai} - ${j.jamSelesai})"
                        conflicts.add(s)
                    }
                }
            }

            if (conflicts.isNotEmpty()) {
                val sb = StringBuilder()
                sb.append("Konflik dengan jadwal berikut:\n")
                conflicts.forEach { sb.append("- $it\n") }
                tvConflictInfo.text = sb.toString()
                tvConflictInfo.visibility = View.VISIBLE
            } else {
                tvConflictInfo.visibility = View.GONE
            }
        } catch (e: Exception) {
            // ignore
        }
    }

    private fun timeToMinutes(timeStr: String): Int? {
        try {
            val parts = timeStr.split(":")
            if (parts.size < 2) return null
            val h = parts[0].toIntOrNull() ?: return null
            val m = parts[1].toIntOrNull() ?: return null
            return h * 60 + m
        } catch (e: Exception) {
            return null
        }
    }

    private fun simpanGuruPengganti(jadwal: JadwalItem, guruId: Int, mapelId: Int) {
        scope.launch {
            try {
                progressBar.visibility = View.VISIBLE

                val tanggal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                
                // Use AssignGuruPenggantiRequest
                val apiService = RetrofitClient.getApiService(requireContext())
                val request = AssignGuruPenggantiRequest(
                    kehadiranGuruId = jadwal.id,
                    guruPenggantiId = guruId,
                    catatan = "Guru pengganti mapel ID: $mapelId"
                )
                
                val response = apiService.assignGuruPengganti(request)

                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(requireContext(), "Guru pengganti berhasil disimpan", Toast.LENGTH_SHORT).show()
                    loadJadwal() // Refresh
                } else {
                    Toast.makeText(requireContext(), "Gagal: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error simpan guru pengganti: ${e.message}", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showDeleteConfirmDialog(jadwal: JadwalItem) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Jadwal")
            .setMessage("Apakah Anda yakin ingin menghapus jadwal ${jadwal.namaMapel ?: jadwal.mapel ?: ""} - ${jadwal.namaGuru ?: jadwal.guru ?: ""}?")
            .setPositiveButton("Hapus") { _, _ ->
                hapusJadwal(jadwal.id)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun hapusJadwal(jadwalId: Int) {
        progressBar.visibility = View.VISIBLE
        
        scope.launch {
            try {
                val apiService = RetrofitClient.getApiService(requireContext())
                val response = apiService.hapusJadwal(jadwalId)

                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(requireContext(), "Jadwal berhasil dihapus", Toast.LENGTH_SHORT).show()
                    loadJadwal() // Refresh
                } else {
                    Toast.makeText(requireContext(), "Gagal: ${response.body()?.message ?: response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error hapus jadwal: ${e.message}", e)
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
