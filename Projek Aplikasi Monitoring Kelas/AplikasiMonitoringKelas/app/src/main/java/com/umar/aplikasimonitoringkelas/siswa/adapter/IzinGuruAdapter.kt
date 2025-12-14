package com.umar.aplikasimonitoringkelas.siswa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.model.KehadiranItem

class IzinGuruAdapter(
    private val izinSakitList: List<KehadiranItem>
) : RecyclerView.Adapter<IzinGuruAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvKodeGuru: TextView = view.findViewById(R.id.tvKodeGuru)
        val tvNamaGuru: TextView = view.findViewById(R.id.tvNamaGuru)
        val tvTanggal: TextView = view.findViewById(R.id.tvTanggal)
        val tvMapel: TextView = view.findViewById(R.id.tvMapel)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvKeterangan: TextView = view.findViewById(R.id.tvKeterangan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_izin_guru, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = izinSakitList[position]
        holder.tvKodeGuru.text = item.kodeGuru ?: item.guru ?: item.namaGuru ?: "-"
        holder.tvNamaGuru.text = item.namaGuru ?: item.guru ?: "-"
        holder.tvTanggal.text = item.tanggal ?: "-"
        val mapel = item.namaMapel ?: item.mapel ?: ""
        if (mapel.isNotBlank()) {
            holder.tvMapel.visibility = View.VISIBLE
            holder.tvMapel.text = mapel
        } else {
            holder.tvMapel.visibility = View.GONE
        }
        holder.tvStatus.text = item.status.capitalize()
        holder.tvKeterangan.text = item.keterangan ?: "-"
    }

    override fun getItemCount() = izinSakitList.size
}
