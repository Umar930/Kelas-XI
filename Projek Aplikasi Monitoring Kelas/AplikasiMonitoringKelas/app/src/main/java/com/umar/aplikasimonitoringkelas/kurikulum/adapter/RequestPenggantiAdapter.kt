package com.umar.aplikasimonitoringkelas.kurikulum.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.model.Guru
import com.umar.aplikasimonitoringkelas.data.model.RequestPenggantiItem

/**
 * Adapter untuk menampilkan request pengganti dari siswa
 * dengan fitur assign guru pengganti
 */
class RequestPenggantiAdapter(
    private val dataList: List<RequestPenggantiItem>,
    private val guruList: List<Guru>,
    private val onAssignClick: (RequestPenggantiItem, Int) -> Unit
) : RecyclerView.Adapter<RequestPenggantiAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardRequest: MaterialCardView = view.findViewById(R.id.cardRequest)
        val tvJamKe: TextView = view.findViewById(R.id.tvJamKe)
        val tvNamaGuru: TextView = view.findViewById(R.id.tvNamaGuru)
        val tvNamaMapel: TextView = view.findViewById(R.id.tvNamaMapel)
        val tvNamaKelas: TextView = view.findViewById(R.id.tvNamaKelas)
        val tvPesan: TextView = view.findViewById(R.id.tvPesan)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val btnAssign: MaterialButton = view.findViewById(R.id.btnAssign)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kurikulum_request, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        val context = holder.itemView.context

        holder.tvJamKe.text = "Jam ke-${item.jamKe}"
        holder.tvNamaGuru.text = item.guruNama ?: "Unknown"
        holder.tvNamaMapel.text = item.mapelNama ?: "Unknown"
        holder.tvNamaKelas.text = item.kelasNama ?: ""
        holder.tvPesan.text = item.pesan ?: "-"

        // Set status
        when (item.status?.lowercase()) {
            "pending" -> {
                holder.tvStatus.text = "Menunggu"
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_izin))
                holder.btnAssign.visibility = View.VISIBLE
            }
            "assigned" -> {
                holder.tvStatus.text = "Sudah Ditugaskan"
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_hadir))
                holder.btnAssign.visibility = View.GONE
            }
            "completed" -> {
                holder.tvStatus.text = "Selesai"
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
                holder.btnAssign.visibility = View.GONE
            }
            else -> {
                holder.tvStatus.text = item.status ?: "Pending"
                holder.btnAssign.visibility = View.VISIBLE
            }
        }

        holder.btnAssign.setOnClickListener {
            showAssignDialog(context, item)
        }
    }

    private fun showAssignDialog(context: android.content.Context, item: RequestPenggantiItem) {
        if (guruList.isEmpty()) {
            AlertDialog.Builder(context)
                .setTitle("Error")
                .setMessage("Tidak ada guru yang tersedia")
                .setPositiveButton("OK", null)
                .show()
            return
        }

        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_assign_guru, null)
        val spinnerGuru = dialogView.findViewById<Spinner>(R.id.spinnerGuru)
        val tvInfo = dialogView.findViewById<TextView>(R.id.tvInfo)

        tvInfo.text = "Jam ke-${item.jamKe} - ${item.mapelNama}"

        val guruNames = guruList.map { it.nama }
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, guruNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGuru.adapter = adapter

        AlertDialog.Builder(context)
            .setTitle("Pilih Guru Pengganti")
            .setView(dialogView)
            .setPositiveButton("Assign") { _, _ ->
                val selectedIndex = spinnerGuru.selectedItemPosition
                if (selectedIndex >= 0 && selectedIndex < guruList.size) {
                    val selectedGuru = guruList[selectedIndex]
                    onAssignClick(item, selectedGuru.id)
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun getItemCount(): Int = dataList.size
}
