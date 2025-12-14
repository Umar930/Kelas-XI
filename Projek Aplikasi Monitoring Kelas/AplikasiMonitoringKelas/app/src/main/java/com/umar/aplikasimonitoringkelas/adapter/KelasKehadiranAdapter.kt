package com.umar.aplikasimonitoringkelas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.model.KelasWithJadwalKehadiran

class KelasKehadiranAdapter(
    private val kelasList: List<KelasWithJadwalKehadiran>,
    private val onKelasClick: (KelasWithJadwalKehadiran) -> Unit
) : RecyclerView.Adapter<KelasKehadiranAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvKelas: TextView = view.findViewById(R.id.tvKelas)
        val card: MaterialCardView = view.findViewById(R.id.cardJadwal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kelas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kelas = kelasList[position]
        
        holder.tvKelas.text = "${kelas.namaKelas} (${kelas.kodeKelas})"
        
        holder.card.setOnClickListener {
            onKelasClick(kelas)
        }
    }

    override fun getItemCount() = kelasList.size
}
