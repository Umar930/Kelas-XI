package com.umar.aplikasimonitoringkelas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.model.KehadiranGuruDetail

class KehadiranGuruKepsekAdapter(
    private val kehadiranList: List<KehadiranGuruDetail>
) : RecyclerView.Adapter<KehadiranGuruKepsekAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvGuru: TextView = view.findViewById(R.id.tvGuru)
        val tvMapel: TextView = view.findViewById(R.id.tvMapel)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kehadiran_guru_kepsek, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kehadiran = kehadiranList[position]
        
        holder.tvGuru.text = kehadiran.guru.nama
        holder.tvMapel.text = "Mapel: ${kehadiran.mapel.nama ?: kehadiran.mapel.mapel ?: ""}"
        
        val status = kehadiran.status
        holder.tvStatus.text = when(status) {
            "hadir" -> "✓ Hadir"
            "tidak_hadir" -> "✗ Tidak Hadir"
            "izin" -> "📝 Izin"
            "sakit" -> "🏥 Sakit"
            else -> "⏳ Belum Input"
        }

        val statusColor = when(status) {
            "hadir" -> 0xFF4CAF50.toInt()
            "tidak_hadir" -> 0xFFF44336.toInt()
            "izin" -> 0xFFFF9800.toInt()
            "sakit" -> 0xFFFF5722.toInt()
            else -> 0xFF9E9E9E.toInt()
        }
        holder.tvStatus.setTextColor(statusColor)
    }

    override fun getItemCount() = kehadiranList.size
}
