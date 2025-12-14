package com.umar.aplikasimonitoringkelas.siswa

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.model.Notifikasi
import java.text.SimpleDateFormat
import java.util.*

class NotifikasiAdapter(
    private val items: List<Notifikasi>,
    private val onItemClick: (Notifikasi) -> Unit
) : RecyclerView.Adapter<NotifikasiAdapter.ViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notifikasi, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
    
    override fun getItemCount() = items.size
    
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val cardNotif: CardView = view.findViewById(R.id.cardNotif)
        private val ivIcon: ImageView = view.findViewById(R.id.ivIcon)
        private val tvPesan: TextView = view.findViewById(R.id.tvPesan)
        private val tvTanggal: TextView = view.findViewById(R.id.tvTanggal)
        private val tvTipe: TextView = view.findViewById(R.id.tvTipe)
        
        fun bind(notif: Notifikasi) {
            tvPesan.text = notif.pesan
            tvTanggal.text = formatTanggal(notif.createdAt)
            
            // Set icon dan warna based on tipe
            when (notif.tipe) {
                "izin" -> {
                    tvTipe.text = "IZIN"
                    tvTipe.setBackgroundColor(Color.parseColor("#FF9800"))
                    ivIcon.setImageResource(android.R.drawable.ic_menu_info_details)
                }
                "sakit" -> {
                    tvTipe.text = "SAKIT"
                    tvTipe.setBackgroundColor(Color.parseColor("#FF5722"))
                    ivIcon.setImageResource(android.R.drawable.ic_dialog_info)
                }
                "guru_pengganti" -> {
                    tvTipe.text = "GURU PENGGANTI"
                    tvTipe.setBackgroundColor(Color.parseColor("#4CAF50"))
                    ivIcon.setImageResource(android.R.drawable.ic_menu_my_calendar)
                }
            }
            
            // Background berbeda untuk unread
            if (!notif.isRead) {
                cardNotif.setCardBackgroundColor(Color.parseColor("#E3F2FD"))
            } else {
                cardNotif.setCardBackgroundColor(Color.WHITE)
            }
            
            cardNotif.setOnClickListener {
                onItemClick(notif)
            }
        }
        
        private fun formatTanggal(dateString: String): String {
            return try {
                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
                val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
                val date = parser.parse(dateString)
                formatter.format(date ?: Date())
            } catch (e: Exception) {
                dateString
            }
        }
    }
}