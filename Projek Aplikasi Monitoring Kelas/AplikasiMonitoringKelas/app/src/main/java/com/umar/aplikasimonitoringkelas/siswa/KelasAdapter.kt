package com.umar.aplikasimonitoringkelas.siswa

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.model.KelasWithSiswa
import com.umar.aplikasimonitoringkelas.data.model.Siswa

class KelasAdapter(
    private val items: List<KelasWithSiswa>
) : RecyclerView.Adapter<KelasAdapter.ViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kelas, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
    
    override fun getItemCount() = items.size
    
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvNamaKelas: TextView = view.findViewById(R.id.tvNamaKelas)
        private val tvKodeKelas: TextView = view.findViewById(R.id.tvKodeKelas)
        private val tvTingkat: TextView = view.findViewById(R.id.tvTingkat)
        private val tvJurusan: TextView = view.findViewById(R.id.tvJurusan)
        private val tvWaliKelas: TextView = view.findViewById(R.id.tvWaliKelas)
        private val tvJumlahSiswa: TextView = view.findViewById(R.id.tvJumlahSiswa)
        private val rvSiswa: RecyclerView = view.findViewById(R.id.rvSiswa)
        private val btnToggleSiswa: MaterialButton = view.findViewById(R.id.btnToggleSiswa)
        
        private var isExpanded = false
        
        fun bind(kelas: KelasWithSiswa) {
            tvNamaKelas.text = kelas.namaKelas
            tvKodeKelas.text = kelas.kodeKelas
            tvTingkat.text = kelas.tingkat
            tvJurusan.text = kelas.jurusan
            tvWaliKelas.text = kelas.waliKelas ?: "-"
            tvJumlahSiswa.text = "${kelas.jumlahSiswa} Siswa"
            
            // Setup siswa RecyclerView
            rvSiswa.layoutManager = LinearLayoutManager(itemView.context)
            val siswaAdapter = SiswaAdapter(kelas.siswa)
            rvSiswa.adapter = siswaAdapter
            
            // Toggle button
            btnToggleSiswa.setOnClickListener {
                isExpanded = !isExpanded
                if (isExpanded) {
                    rvSiswa.visibility = View.VISIBLE
                    btnToggleSiswa.text = "Sembunyikan Daftar Siswa"
                    btnToggleSiswa.icon = itemView.context.getDrawable(android.R.drawable.arrow_up_float)
                } else {
                    rvSiswa.visibility = View.GONE
                    btnToggleSiswa.text = "Lihat Daftar Siswa"
                    btnToggleSiswa.icon = itemView.context.getDrawable(android.R.drawable.arrow_down_float)
                }
            }
        }
    }
}

// Adapter untuk siswa
class SiswaAdapter(
    private val items: List<Siswa>
) : RecyclerView.Adapter<SiswaAdapter.ViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position + 1)
    }
    
    override fun getItemCount() = items.size
    
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val text1: TextView = view.findViewById(android.R.id.text1)
        private val text2: TextView = view.findViewById(android.R.id.text2)
        
        fun bind(siswa: Siswa, nomor: Int) {
            val jenisKelamin = if (siswa.jenisKelamin == "L") "♂" else "♀"
            text1.text = "$nomor. $jenisKelamin ${siswa.nama}"
            text2.text = "NIS: ${siswa.nis} | ${siswa.email ?: "-"}"
            
            // Color based on gender
            val color = if (siswa.jenisKelamin == "L") "#1976D2" else "#E91E63"
            text1.setTextColor(Color.parseColor(color))
        }
    }
}
