package com.umar.aplikasimonitoringkelas.siswa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.model.JadwalItem
import com.umar.aplikasimonitoringkelas.data.model.Guru
import com.umar.aplikasimonitoringkelas.data.model.Mapel

/**
 * Adapter untuk entri kehadiran guru dengan tombol hadir/tidak hadir
 * Uses JadwalItem from /api/siswa/jadwal response
 */
class EntriKehadiranAdapter(
    private val jadwalList: List<JadwalItem>,
    private val guruList: List<Guru>,
    private val mapelList: List<Mapel>,
    private val onHadirClick: (JadwalItem) -> Unit,
    private val onTidakHadirClick: (JadwalItem) -> Unit,
    private val onSimpanPengganti: (JadwalItem, Int, Int) -> Unit,
    private val onDeleteClick: ((JadwalItem) -> Unit)? = null
) : RecyclerView.Adapter<EntriKehadiranAdapter.ViewHolder>() {

    private val selectedGuruMap = mutableMapOf<Int, Int>() // position -> guruId
    private val selectedMapelMap = mutableMapOf<Int, Int>() // position -> mapelId

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJam: TextView = view.findViewById(R.id.tvJam)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvMapel: TextView = view.findViewById(R.id.tvMapel)
        val tvKodeGuru: TextView = view.findViewById(R.id.tvKodeGuru)
        val tvNamaGuru: TextView = view.findViewById(R.id.tvNamaGuru)
        val btnHadir: MaterialButton = view.findViewById(R.id.btnHadir)
        val btnTidakHadir: MaterialButton = view.findViewById(R.id.btnTidakHadir)
        val btnDelete: ImageButton? = view.findViewById(R.id.btnDeleteJadwal)
        val layoutGuruPengganti: LinearLayout = view.findViewById(R.id.layoutGuruPengganti)
        val tilGuruPengganti: TextInputLayout = view.findViewById(R.id.tilGuruPengganti)
        val spinnerGuruPengganti: AutoCompleteTextView = view.findViewById(R.id.spinnerGuruPengganti)
        val tilMapelPengganti: TextInputLayout = view.findViewById(R.id.tilMapelPengganti)
        val spinnerMapelPengganti: AutoCompleteTextView = view.findViewById(R.id.spinnerMapelPengganti)
        val btnSimpanPengganti: MaterialButton = view.findViewById(R.id.btnSimpanPengganti)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entri_kehadiran, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jadwal = jadwalList[position]
        
        // Use jam field if available, else calculate from jam_mulai
        holder.tvJam.text = jadwal.jam ?: jadwal.jamMulai?.let { "Jam ${calculateJamKe(it)}" } ?: "-"
        holder.tvMapel.text = jadwal.namaMapel ?: jadwal.mapel ?: "-"
        holder.tvKodeGuru.text = jadwal.kodeGuru ?: jadwal.guru ?: jadwal.namaGuru ?: "-"
        holder.tvNamaGuru.text = jadwal.namaGuru ?: "-"
        
        // Setup delete button
        if (onDeleteClick != null) {
            holder.btnDelete?.visibility = View.VISIBLE
            holder.btnDelete?.setOnClickListener {
                onDeleteClick.invoke(jadwal)
            }
        } else {
            holder.btnDelete?.visibility = View.GONE
        }
        
        // Check status kehadiran
        if (jadwal.sudahDiisi) {
            // Sudah diisi - tampilkan status
            when (jadwal.statusKehadiran) {
                "hadir" -> {
                    holder.tvStatus.text = "✓ Hadir"
                    holder.tvStatus.setBackgroundResource(R.drawable.badge_background_success)
                    holder.btnHadir.visibility = View.GONE
                    holder.btnTidakHadir.visibility = View.GONE
                    holder.layoutGuruPengganti.visibility = View.GONE
                }
                "tidak_hadir" -> {
                    holder.tvStatus.text = "✗ Tidak Hadir"
                    holder.tvStatus.setBackgroundResource(R.drawable.badge_background_danger)
                    holder.btnHadir.visibility = View.GONE
                    holder.btnTidakHadir.visibility = View.GONE
                    holder.layoutGuruPengganti.visibility = View.GONE
                }
                else -> showDefaultState(holder, jadwal)
            }
        } else {
            // Belum diisi - tampilkan tombol
            showDefaultState(holder, jadwal)
        }
    }

    private fun showDefaultState(holder: ViewHolder, jadwal: JadwalItem) {
        holder.tvStatus.text = "Belum Diisi"
        holder.tvStatus.setBackgroundResource(R.drawable.badge_background_warning)
        holder.btnHadir.visibility = View.VISIBLE
        holder.btnTidakHadir.visibility = View.VISIBLE
        holder.layoutGuruPengganti.visibility = View.GONE

        holder.btnHadir.setOnClickListener {
            onHadirClick(jadwal)
        }

        holder.btnTidakHadir.setOnClickListener {
            onTidakHadirClick(jadwal)
        }
    }

    private fun setupGuruPenggantiDropdowns(holder: ViewHolder, position: Int, jadwal: JadwalItem) {
        val context = holder.itemView.context
        
        // Setup guru dropdown
        val guruNames = guruList.map { it.nama }
        val guruAdapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, guruNames)
        holder.spinnerGuruPengganti.setAdapter(guruAdapter)
        
        holder.spinnerGuruPengganti.setOnItemClickListener { _, _, idx, _ ->
            selectedGuruMap[position] = guruList[idx].id
        }

        // Setup mapel dropdown
        val mapelNames = mapelList.map { it.nama ?: it.mapel ?: "-" }
        val mapelAdapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, mapelNames)
        holder.spinnerMapelPengganti.setAdapter(mapelAdapter)
        
        holder.spinnerMapelPengganti.setOnItemClickListener { _, _, idx, _ ->
            selectedMapelMap[position] = mapelList[idx].id
        }

        // Setup save button
        holder.btnSimpanPengganti.setOnClickListener {
            val guruId = selectedGuruMap[position]
            val mapelId = selectedMapelMap[position]
            
            if (guruId != null && mapelId != null) {
                onSimpanPengganti(jadwal, guruId, mapelId)
            } else {
                android.widget.Toast.makeText(context, "Pilih guru dan mapel terlebih dahulu", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount() = jadwalList.size

    private fun calculateJamKe(jamMulai: String): String {
        return when {
            jamMulai.startsWith("07") -> "1-2"
            jamMulai.startsWith("08") -> "3-4"
            jamMulai.startsWith("09") -> "5-6"
            jamMulai.startsWith("10") -> "7-8"
            jamMulai.startsWith("11") -> "9-10"
            jamMulai.startsWith("12") -> "11-12"
            jamMulai.startsWith("13") -> "13-14"
            else -> "1-2"
        }
    }
}
