package com.umar.aplikasimonitoringkelas.siswa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.model.KehadiranItem

/**
 * Adapter untuk menampilkan daftar kehadiran guru
 * Uses KehadiranItem from /api/siswa/list-kehadiran response
 */
class ListGuruHadirAdapter(
    private val dataList: List<KehadiranItem>,
    private val onInputPenggantiClick: ((KehadiranItem) -> Unit)? = null
) : RecyclerView.Adapter<ListGuruHadirAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJam: TextView = view.findViewById(R.id.tvJam)
        val tvMapel: TextView = view.findViewById(R.id.tvMapel)
        val tvKodeGuru: TextView = view.findViewById(R.id.tvKodeGuru)
        val tvNamaGuru: TextView = view.findViewById(R.id.tvNamaGuru)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val layoutRequestPengganti: LinearLayout = view.findViewById(R.id.layoutRequestPengganti)
        val btnInputPengganti: MaterialButton = view.findViewById(R.id.btnInputPengganti)
        val layoutStatusPengganti: LinearLayout = view.findViewById(R.id.layoutStatusPengganti)
        val tvGuruPengganti: TextView = view.findViewById(R.id.tvGuruPengganti)
        val tvStatusPengganti: TextView = view.findViewById(R.id.tvStatusPengganti)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_guru_hadir, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kehadiran = dataList[position]
        
        // Use jam field if available
        holder.tvJam.text = kehadiran.jam ?: kehadiran.jamMulai?.let { "Jam ${calculateJamKe(it)}" } ?: "Tanggal: ${kehadiran.tanggal ?: "-"}"
        holder.tvMapel.text = kehadiran.namaMapel ?: kehadiran.mapel ?: "-"
        holder.tvKodeGuru.text = kehadiran.kodeGuru ?: kehadiran.guru ?: kehadiran.namaGuru ?: "-"
        holder.tvNamaGuru.text = kehadiran.namaGuru ?: "-"
        
        // Status badge
        val statusText = when (kehadiran.status) {
            "hadir" -> "✓ Hadir"
            "tidak_hadir" -> "✗ Tidak Hadir"
            "izin" -> "⚠ Izin"
            "sakit" -> "⚠ Sakit"
            else -> kehadiran.status
        }
        holder.tvStatus.text = statusText
        
        val bgRes = when (kehadiran.status) {
            "hadir" -> R.drawable.badge_background_success
            "tidak_hadir" -> R.drawable.badge_background_danger
            else -> R.drawable.badge_background_warning
        }
        holder.tvStatus.setBackgroundResource(bgRes)
        
        // Handle request pengganti section
        // Show request pengganti UI when teacher is absent or marked izin/sakit (possibly set by admin)
        if (kehadiran.status == "tidak_hadir" || kehadiran.status == "izin" || kehadiran.status == "sakit") {
            holder.layoutRequestPengganti.visibility = View.VISIBLE
            
            if (kehadiran.hasRequestPengganti && kehadiran.requestPengganti != null) {
                // Sudah ada request - tampilkan status
                holder.btnInputPengganti.visibility = View.GONE
                holder.layoutStatusPengganti.visibility = View.VISIBLE
                val rp = kehadiran.requestPengganti

                // Prefer server-provided display fields when active
                val displayKode = rp.displayKodeGuru ?: rp.guruPenggantiId?.toString() ?: "-"
                val displayNama = rp.displayGuru ?: rp.guruPengganti ?: "Belum ditentukan"

                when (rp.status?.lowercase()) {
                    "pending" -> {
                        holder.tvGuruPengganti.text = "Ganti guru (menunggu kurikulum)"
                        holder.tvStatusPengganti.text = "⏳ Pending"
                        holder.tvStatusPengganti.setBackgroundResource(R.drawable.badge_background_warning)
                        holder.btnInputPengganti.visibility = View.GONE
                        holder.layoutStatusPengganti.visibility = View.VISIBLE
                    }
                    "aktif", "approved", "disetujui" -> {
                        holder.tvGuruPengganti.text = "Guru diganti: $displayKode - $displayNama"
                        holder.tvStatusPengganti.text = "✓ Aktif"
                        holder.tvStatusPengganti.setBackgroundResource(R.drawable.badge_background_success)
                        // Allow entering attendance for the (replacement) schedule
                        holder.btnInputPengganti.visibility = View.VISIBLE
                        holder.btnInputPengganti.text = "Entri Kehadiran"
                        holder.layoutStatusPengganti.visibility = View.VISIBLE
                    }
                    "rejected", "ditolak" -> {
                        holder.tvGuruPengganti.text = "Request ditolak"
                        holder.tvStatusPengganti.text = "✗ Ditolak"
                        holder.tvStatusPengganti.setBackgroundResource(R.drawable.badge_background_danger)
                        holder.btnInputPengganti.visibility = View.GONE
                        holder.layoutStatusPengganti.visibility = View.VISIBLE
                    }
                    else -> {
                        holder.tvGuruPengganti.text = rp.guruPengganti ?: "Belum ditentukan"
                        holder.tvStatusPengganti.text = rp.status ?: "-"
                        holder.tvStatusPengganti.setBackgroundResource(R.drawable.badge_background_warning)
                        holder.btnInputPengganti.visibility = View.GONE
                        holder.layoutStatusPengganti.visibility = View.VISIBLE
                    }
                }
                
            } else {
                // Belum ada request - tampilkan tombol untuk meminta pengganti
                holder.btnInputPengganti.visibility = View.VISIBLE
                holder.btnInputPengganti.text = "Request Pengganti"
                holder.layoutStatusPengganti.visibility = View.GONE

                holder.btnInputPengganti.setOnClickListener {
                    onInputPenggantiClick?.invoke(kehadiran)
                }
            }
        } else {
            holder.layoutRequestPengganti.visibility = View.GONE
        }

        // If replacement active, clicking the button should either request or open attendance input
        holder.btnInputPengganti.setOnClickListener {
            onInputPenggantiClick?.invoke(kehadiran)
        }
    }

    override fun getItemCount() = dataList.size

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
