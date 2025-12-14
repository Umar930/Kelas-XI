package com.umar.aplikasimonitoringkelas.siswa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.model.Siswa

class SiswaKelasAdapter(
    private val siswaList: List<Siswa>
) : RecyclerView.Adapter<SiswaKelasAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNomorSiswa: TextView = view.findViewById(R.id.tvNomorSiswa)
        val tvNamaSiswa: TextView = view.findViewById(R.id.tvNamaSiswa)
        val tvNisSiswa: TextView = view.findViewById(R.id.tvNisSiswa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_siswa_kelas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val siswa = siswaList[position]
        
        holder.tvNomorSiswa.text = (position + 1).toString()
        holder.tvNamaSiswa.text = siswa.nama
        holder.tvNisSiswa.text = "NIS: ${siswa.nis}"
    }

    override fun getItemCount() = siswaList.size
}
