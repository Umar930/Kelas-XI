package com.umar.aplikasimonitoringkelas.siswa

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.model.JadwalKelas

class JadwalGuruSiswaAdapter(
    private val jadwalList: List<JadwalKelas>,
    private val onInputKehadiran: (JadwalKelas) -> Unit,
    private val onRequestPengganti: (JadwalKelas) -> Unit
) : RecyclerView.Adapter<JadwalGuruSiswaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJamMapel: TextView = view.findViewById(R.id.tvJamMapel)
        val tvNamaMapel: TextView = view.findViewById(R.id.tvNamaMapel)
        val tvNamaGuru: TextView = view.findViewById(R.id.tvNamaGuru)
        val tvStatusKehadiran: TextView = view.findViewById(R.id.tvStatusKehadiran)
        val btnInputKehadiran: MaterialButton = view.findViewById(R.id.btnInputKehadiran)
        val btnRequestPengganti: MaterialButton = view.findViewById(R.id.btnRequestPengganti)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jadwal_guru_siswa, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jadwal = jadwalList[position]
        
        holder.tvJamMapel.text = "${jadwal.jamMulai} - ${jadwal.jamSelesai}"
        holder.tvNamaMapel.text = jadwal.mapel.nama ?: jadwal.mapel.mapel ?: ""
        holder.tvNamaGuru.text = jadwal.guru.nama
        
        // Check if kehadiran already input
        if (jadwal.kehadiran != null) {
            val kehadiran = jadwal.kehadiran
            when (kehadiran.status) {
                "hadir" -> {
                    holder.tvStatusKehadiran.text = "✓ Hadir"
                    holder.tvStatusKehadiran.setTextColor(Color.parseColor("#4CAF50"))
                    holder.btnInputKehadiran.visibility = View.GONE
                    holder.btnRequestPengganti.visibility = View.GONE
                }
                "tidak_hadir" -> {
                    holder.tvStatusKehadiran.text = "✗ Tidak Hadir"
                    holder.tvStatusKehadiran.setTextColor(Color.parseColor("#F44336"))
                    holder.btnInputKehadiran.visibility = View.GONE
                    holder.btnRequestPengganti.visibility = View.VISIBLE
                }
                "izin" -> {
                    holder.tvStatusKehadiran.text = "📝 Izin"
                    holder.tvStatusKehadiran.setTextColor(Color.parseColor("#FF9800"))
                    holder.btnInputKehadiran.visibility = View.GONE
                    holder.btnRequestPengganti.visibility = View.VISIBLE
                }
                "sakit" -> {
                    holder.tvStatusKehadiran.text = "🏥 Sakit"
                    holder.tvStatusKehadiran.setTextColor(Color.parseColor("#FF5722"))
                    holder.btnInputKehadiran.visibility = View.GONE
                    holder.btnRequestPengganti.visibility = View.VISIBLE
                }
                else -> {
                    holder.tvStatusKehadiran.text = "⏳ Belum Input"
                    holder.tvStatusKehadiran.setTextColor(Color.parseColor("#9E9E9E"))
                    holder.btnInputKehadiran.visibility = View.VISIBLE
                    holder.btnRequestPengganti.visibility = View.GONE
                }
            }
        } else {
            holder.tvStatusKehadiran.text = "⏳ Belum Input"
            holder.tvStatusKehadiran.setTextColor(Color.parseColor("#9E9E9E"))
            holder.btnInputKehadiran.visibility = View.VISIBLE
            holder.btnRequestPengganti.visibility = View.GONE
        }
        
        holder.btnInputKehadiran.setOnClickListener {
            onInputKehadiran(jadwal)
        }
        
        holder.btnRequestPengganti.setOnClickListener {
            onRequestPengganti(jadwal)
        }
    }

    override fun getItemCount() = jadwalList.size
}
