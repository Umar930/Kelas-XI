package com.umar.aplikasimonitoringkelas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umar.aplikasimonitoringkelas.R
import com.umar.aplikasimonitoringkelas.data.model.GuruPengganti

class RequestPenggantiAdapter(
    private val requestList: List<GuruPengganti>
) : RecyclerView.Adapter<RequestPenggantiAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTanggal: TextView = view.findViewById(R.id.tvTanggal)
        val tvJam: TextView = view.findViewById(R.id.tvJam)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_request_pengganti, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request = requestList[position]
        
        holder.tvTanggal.text = "Tanggal: ${request.tanggal}"
        val jam = if (request.jamMulai != null && request.jamSelesai != null) {
            "${request.jamMulai} - ${request.jamSelesai}"
        } else {
            "-"
        }
        holder.tvJam.text = "Jam: $jam"
        
        val statusText = when(request.status) {
            "pending" -> "⏳ Pending"
            "diterima" -> "✓ Diterima"
            "ditolak" -> "✗ Ditolak"
            else -> request.status
        }
        holder.tvStatus.text = "Status: $statusText"
        
        val statusColor = when(request.status) {
            "diterima" -> 0xFF4CAF50.toInt()
            "ditolak" -> 0xFFF44336.toInt()
            else -> 0xFFFF9800.toInt()
        }
        holder.tvStatus.setTextColor(statusColor)
    }

    override fun getItemCount() = requestList.size
}
