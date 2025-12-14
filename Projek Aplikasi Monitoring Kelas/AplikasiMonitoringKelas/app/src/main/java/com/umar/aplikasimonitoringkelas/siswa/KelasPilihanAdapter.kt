package com.umar.aplikasimonitoringkelas.siswa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.model.KelasWithSiswa

class KelasPilihanAdapter(
    private val kelasList: List<KelasWithSiswa>,
    private val onKelasClick: (KelasWithSiswa) -> Unit
) : RecyclerView.Adapter<KelasPilihanAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaKelas: TextView = view.findViewById(R.id.tvNamaKelas)
        val tvKodeKelas: TextView = view.findViewById(R.id.tvKodeKelas)
        val cardKelas: MaterialCardView = view.findViewById(R.id.cardKelas)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kelas_pilihan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kelas = kelasList[position]
        
        holder.tvNamaKelas.text = kelas.namaKelas
        holder.tvKodeKelas.text = kelas.kodeKelas
        
        holder.cardKelas.setOnClickListener {
            onKelasClick(kelas)
        }
    }

    override fun getItemCount() = kelasList.size
}
