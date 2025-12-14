package com.umar.aplikasimonitoringkelas.siswa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.model.Notifikasi
import java.text.SimpleDateFormat
import java.util.*

class NotifikasiLaporanAdapter(
    private val notifikasiList: List<Notifikasi>
) : RecyclerView.Adapter<NotifikasiLaporanAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvIcon: TextView = view.findViewById(R.id.tvIcon)
        val tvTipeNotifikasi: TextView = view.findViewById(R.id.tvTipeNotifikasi)
        val tvWaktu: TextView = view.findViewById(R.id.tvWaktu)
        val tvPesan: TextView = view.findViewById(R.id.tvPesan)
        val tvDetail: TextView = view.findViewById(R.id.tvDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notifikasi_laporan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notif = notifikasiList[position]
        
        // Set icon based on type
        holder.tvIcon.text = when (notif.tipe.lowercase()) {
            "izin" -> "📅"
            "sakit" -> "🤒"
            "guru_pengganti" -> "👨‍🏫"
            else -> "🔔"
        }
        
        // Set tipe
        holder.tvTipeNotifikasi.text = when (notif.tipe.lowercase()) {
            "izin" -> "GURU IZIN"
            "sakit" -> "GURU SAKIT"
            "guru_pengganti" -> "GURU PENGGANTI"
            else -> notif.tipe.uppercase()
        }
        
        // Set waktu
        holder.tvWaktu.text = formatWaktu(notif.createdAt)
        
        // Set pesan
        holder.tvPesan.text = notif.pesan
        
        // Set detail (mapel + guru)
        val mapelNama = notif.mapel?.nama ?: notif.mapel?.mapel ?: "Mata Pelajaran"
        val guruNama = notif.guru.nama
        
        val detailText = if (notif.tipe.lowercase() == "guru_pengganti" && notif.guruPengganti != null) {
            "$mapelNama • Pengganti: ${notif.guruPengganti.nama}"
        } else {
            "$mapelNama • $guruNama"
        }
        
        holder.tvDetail.text = detailText
    }

    override fun getItemCount() = notifikasiList.size

    private fun formatWaktu(timestamp: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(timestamp)
            
            val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            date?.let { outputFormat.format(it) } ?: timestamp
        } catch (e: Exception) {
            // Fallback if parsing fails
            try {
                val parts = timestamp.split("T")
                if (parts.size > 1) {
                    val timeParts = parts[1].split(":")
                    "${timeParts[0]}:${timeParts[1]}"
                } else {
                    timestamp
                }
            } catch (e: Exception) {
                timestamp
            }
        }
    }
}
