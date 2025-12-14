package com.umar.aplikasimonitoringkelas.kurikulum.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.model.DaftarGuruItem

/**
 * Adapter untuk menampilkan daftar guru dengan status kehadiran (read-only)
 */
class DaftarGuruAdapter(
    private val guruList: List<DaftarGuruItem>
) : RecyclerView.Adapter<DaftarGuruAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardGuru: MaterialCardView = view.findViewById(R.id.cardGuruKehadiran)
        val tvJamKe: TextView = view.findViewById(R.id.tvJamKe)
        val tvNamaGuru: TextView = view.findViewById(R.id.tvNamaGuru)
        val tvNamaMapel: TextView = view.findViewById(R.id.tvNamaMapel)
        val tvStatusKehadiran: TextView = view.findViewById(R.id.tvStatusKehadiran)
        val tvGuruPengganti: TextView = view.findViewById(R.id.tvGuruPengganti)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_daftar_kehadiran_guru, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = guruList[position]
        val context = holder.itemView.context

        // Set jam ke
        holder.tvJamKe.text = item.jam ?: (position + 1).toString()
        holder.tvNamaGuru.text = item.guru?.nama ?: "Unknown"
        holder.tvNamaMapel.text = item.mapel?.nama ?: "Unknown"

        // Set status kehadiran
        val statusKehadiran = item.statusKehadiran ?: "belum_diisi"
        when (statusKehadiran.lowercase()) {
            "hadir" -> {
                holder.tvStatusKehadiran.text = "Hadir"
                holder.tvStatusKehadiran.setTextColor(ContextCompat.getColor(context, R.color.status_hadir))
                holder.cardGuru.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_status_hadir))
            }
            "tidak_hadir", "tidakhadir" -> {
                holder.tvStatusKehadiran.text = "Tidak Hadir"
                holder.tvStatusKehadiran.setTextColor(ContextCompat.getColor(context, R.color.status_tidak_hadir))
                holder.cardGuru.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_status_tidak_hadir))
            }
            "izin" -> {
                holder.tvStatusKehadiran.text = "Izin"
                holder.tvStatusKehadiran.setTextColor(ContextCompat.getColor(context, R.color.status_izin))
                holder.cardGuru.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_status_izin))
            }
            "sakit" -> {
                holder.tvStatusKehadiran.text = "Sakit"
                holder.tvStatusKehadiran.setTextColor(ContextCompat.getColor(context, R.color.status_sakit))
                holder.cardGuru.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_status_sakit))
            }
            else -> {
                holder.tvStatusKehadiran.text = "Belum Diisi"
                holder.tvStatusKehadiran.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
                holder.cardGuru.setCardBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
            }
        }

        // Show guru pengganti if available
        val guruPenggantiName = item.guruPengganti?.nama
        
        if (!guruPenggantiName.isNullOrEmpty()) {
            holder.tvGuruPengganti.text = "Diganti: $guruPenggantiName"
            holder.tvGuruPengganti.visibility = View.VISIBLE
        } else {
            holder.tvGuruPengganti.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = guruList.size
}
