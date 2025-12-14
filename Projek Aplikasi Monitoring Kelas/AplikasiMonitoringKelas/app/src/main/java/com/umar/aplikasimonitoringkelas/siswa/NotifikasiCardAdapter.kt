package com.umar.aplikasimonitoringkelas.siswa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.model.Notifikasi

class NotifikasiCardAdapter(
    private val notifikasiList: List<Notifikasi>
) : RecyclerView.Adapter<NotifikasiCardAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJudulNotifikasi: TextView = view.findViewById(R.id.tvJudulNotifikasi)
        val tvIsiNotifikasi: TextView = view.findViewById(R.id.tvIsiNotifikasi)
        val tvWaktuNotifikasi: TextView = view.findViewById(R.id.tvWaktuNotifikasi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notifikasi_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notif = notifikasiList[position]
        
        holder.tvJudulNotifikasi.text = notif.tipe.uppercase()
        holder.tvIsiNotifikasi.text = notif.pesan
        holder.tvWaktuNotifikasi.text = formatWaktu(notif.createdAt)
    }

    override fun getItemCount() = notifikasiList.size.coerceAtMost(3) // Max 3 notifications

    private fun formatWaktu(timestamp: String): String {
        // Simple time format, you can improve this
        return try {
            val parts = timestamp.split("T")
            if (parts.size > 1) {
                val time = parts[1].substring(0, 5)
                "Hari ini, $time"
            } else {
                timestamp
            }
        } catch (e: Exception) {
            timestamp
        }
    }
}
