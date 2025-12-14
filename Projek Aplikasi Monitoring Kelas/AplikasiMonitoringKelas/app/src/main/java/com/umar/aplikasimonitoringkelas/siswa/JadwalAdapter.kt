package com.umar.aplikasimonitoringkelas.siswa

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.model.JadwalKelas

class JadwalAdapter(
    private val items: List<JadwalKelas>,
    private val kelasId: Int,
    private val tanggal: String,
    private val onInputClick: (JadwalKelas) -> Unit
) : RecyclerView.Adapter<JadwalAdapter.ViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jadwal_kehadiran, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
    
    override fun getItemCount() = items.size
    
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val cardJadwal: CardView = view.findViewById(R.id.cardJadwal)
        private val tvJam: TextView = view.findViewById(R.id.tvJam)
        private val tvGuru: TextView = view.findViewById(R.id.tvGuru)
        private val tvMapel: TextView = view.findViewById(R.id.tvMapel)
        private val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        private val btnInput: MaterialButton = view.findViewById(R.id.btnInput)
        
        fun bind(jadwal: JadwalKelas) {
            tvJam.text = "${jadwal.jamMulai} - ${jadwal.jamSelesai}"
            tvGuru.text = jadwal.guru.nama
            tvMapel.text = jadwal.mapel.mapel ?: jadwal.mapel.nama ?: ""
            
            // Check if already input
            if (jadwal.kehadiran != null) {
                tvStatus.visibility = View.VISIBLE
                btnInput.isEnabled = false
                btnInput.text = "Sudah Input"
                
                when (jadwal.kehadiran.status) {
                    "hadir" -> {
                        tvStatus.text = "✓ Hadir"
                        tvStatus.setTextColor(Color.parseColor("#4CAF50"))
                        cardJadwal.setCardBackgroundColor(Color.parseColor("#E8F5E9"))
                    }
                    "tidak_hadir" -> {
                        tvStatus.text = "✗ Tidak Hadir"
                        tvStatus.setTextColor(Color.parseColor("#F44336"))
                        cardJadwal.setCardBackgroundColor(Color.parseColor("#FFEBEE"))
                    }
                    "izin" -> {
                        tvStatus.text = "📝 Izin"
                        tvStatus.setTextColor(Color.parseColor("#FF9800"))
                        cardJadwal.setCardBackgroundColor(Color.parseColor("#FFF3E0"))
                    }
                    "sakit" -> {
                        tvStatus.text = "🏥 Sakit"
                        tvStatus.setTextColor(Color.parseColor("#FF5722"))
                        cardJadwal.setCardBackgroundColor(Color.parseColor("#FBE9E7"))
                    }
                }
            } else {
                tvStatus.visibility = View.GONE
                btnInput.isEnabled = true
                btnInput.text = "Input Kehadiran"
                cardJadwal.setCardBackgroundColor(Color.WHITE)
            }
            
            btnInput.setOnClickListener {
                onInputClick(jadwal)
            }
        }
    }
}
