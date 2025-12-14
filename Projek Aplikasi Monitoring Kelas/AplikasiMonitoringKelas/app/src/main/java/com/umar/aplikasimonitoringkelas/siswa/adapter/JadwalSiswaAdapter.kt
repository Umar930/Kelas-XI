package com.umar.aplikasimonitoringkelas.siswa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.model.JadwalItem

/**
 * Adapter untuk menampilkan jadwal kelas (read-only)
 * Uses JadwalItem from /api/siswa/jadwal response
 */
class JadwalSiswaAdapter(
    private val jadwalList: List<JadwalItem>
) : RecyclerView.Adapter<JadwalSiswaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJam: TextView = view.findViewById(R.id.tvJam)
        val tvMapel: TextView = view.findViewById(R.id.tvMapel)
        val tvKodeGuru: TextView = view.findViewById(R.id.tvKodeGuru)
        val tvNamaGuru: TextView = view.findViewById(R.id.tvNamaGuru)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jadwal_siswa, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jadwal = jadwalList[position]
        
        // Use jam field if available, else calculate from jam_mulai
        holder.tvJam.text = jadwal.jam ?: "Jam ${calculateJamKe(jadwal.jamMulai ?: "")}"
        holder.tvMapel.text = jadwal.namaMapel ?: jadwal.mapel ?: "-"
        // Prefer kodeGuru if provided by API, otherwise fall back to 'guru' field or namaGuru
        holder.tvKodeGuru.text = jadwal.kodeGuru ?: jadwal.guru ?: jadwal.namaGuru ?: "-"
        holder.tvNamaGuru.text = jadwal.namaGuru ?: "-"
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
