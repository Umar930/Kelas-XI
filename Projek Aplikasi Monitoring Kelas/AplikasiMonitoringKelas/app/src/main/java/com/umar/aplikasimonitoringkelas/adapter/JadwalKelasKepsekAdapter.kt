package com.umar.aplikasimonitoringkelas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.model.JadwalKelas

class JadwalKelasKepsekAdapter(
    private val jadwalList: List<JadwalKelas>
) : RecyclerView.Adapter<JadwalKelasKepsekAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJam: TextView = view.findViewById(R.id.tvJam)
        val tvGuru: TextView = view.findViewById(R.id.tvGuru)
        val tvMapel: TextView = view.findViewById(R.id.tvMapel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jadwal_kelas_kepsek, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jadwal = jadwalList[position]
        
        holder.tvJam.text = "${jadwal.jamMulai} - ${jadwal.jamSelesai}"
        holder.tvGuru.text = "Guru: ${jadwal.guru.nama}"
        holder.tvMapel.text = "Mapel: ${jadwal.mapel.nama ?: jadwal.mapel.mapel ?: ""}"
    }

    override fun getItemCount() = jadwalList.size
}
