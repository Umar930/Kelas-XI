package com.umar.aplikasimonitoringkelas.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.model.AbsensiSiswa

class AbsensiSiswaAdapter(
    private val onDeleteClick: (AbsensiSiswa) -> Unit
) : ListAdapter<AbsensiSiswa, AbsensiSiswaAdapter.ViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_absensi_siswa, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: CardView = itemView.findViewById(R.id.card_view)
        private val tvTanggal: TextView = itemView.findViewById(R.id.tv_tanggal)
        private val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        private val tvKeterangan: TextView = itemView.findViewById(R.id.tv_keterangan)
        private val tvNamaEmail: TextView? = itemView.findViewById(R.id.tv_nama_email)
        private val tvKelas: TextView? = itemView.findViewById(R.id.tv_kelas)
        private val tvGenderIndicator: TextView? = itemView.findViewById(R.id.tv_gender_indicator)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btn_delete)
        
        fun bind(absensi: AbsensiSiswa) {
            // ✅ PRIORITAS: Gunakan field virtual dari backend
            // Fallback ke nested object jika field virtual null
            val nama = absensi.namaSiswa ?: absensi.siswa.nama
            val email = absensi.emailSiswa ?: absensi.siswa.email ?: ""
            val kelas = absensi.namaKelas ?: absensi.siswa.kelas?.nama ?: "-"
            
            // Nama dan Email
            val siswaInfo = buildString {
                append(nama)
                if (email.isNotEmpty()) {
                    append("\n$email")
                }
            }
            tvNamaEmail?.text = siswaInfo
            
            // Kelas
            tvKelas?.text = kelas
            
            // Gender Indicator (L/P)
            val genderIndicator = when (absensi.siswa.jenisKelamin?.uppercase()) {
                "L", "LAKI-LAKI" -> "L"
                "P", "PEREMPUAN" -> "P"
                else -> "-"
            }
            tvGenderIndicator?.text = genderIndicator
            
            // Status color
            val statusColor = when (absensi.status.lowercase()) {
                "hadir" -> Color.parseColor("#4CAF50") // Green
                "sakit" -> Color.parseColor("#FF9800") // Orange
                "izin" -> Color.parseColor("#2196F3")  // Blue
                else -> Color.GRAY
            }
            tvStatus.setTextColor(statusColor)
            
            // Tanggal
            tvTanggal.text = absensi.tanggal
            
            // Status
            tvStatus.text = "Status: ${absensi.status.uppercase()}"
            
            // Keterangan
            if (absensi.keterangan.isNullOrEmpty()) {
                tvKeterangan.visibility = View.GONE
            } else {
                tvKeterangan.visibility = View.VISIBLE
                tvKeterangan.text = "Keterangan: ${absensi.keterangan}"
            }
            
            // Card background color based on status
            val backgroundColor = when (absensi.status.lowercase()) {
                "hadir" -> Color.parseColor("#E8F5E9") // Light Green
                "sakit" -> Color.parseColor("#FFF3E0") // Light Orange
                "izin" -> Color.parseColor("#E3F2FD")  // Light Blue
                else -> Color.WHITE
            }
            cardView.setCardBackgroundColor(backgroundColor)
            
            // Delete button
            btnDelete.setOnClickListener {
                onDeleteClick(absensi)
            }
        }
    }
    
    class DiffCallback : DiffUtil.ItemCallback<AbsensiSiswa>() {
        override fun areItemsTheSame(oldItem: AbsensiSiswa, newItem: AbsensiSiswa): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: AbsensiSiswa, newItem: AbsensiSiswa): Boolean {
            return oldItem == newItem
        }
    }
}
