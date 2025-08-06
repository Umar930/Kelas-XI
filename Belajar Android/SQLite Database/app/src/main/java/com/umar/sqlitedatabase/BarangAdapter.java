package com.umar.sqlitedatabase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.MenuItem;

public class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.BarangViewHolder> {
    private Context context;
    private List<Barang> barangList;
    private OnItemClickListener listener;
    private MainActivity activity;

    public interface OnItemClickListener {
        void onItemClick(Barang barang);

        void onItemEdit(Barang barang);

        void onItemDelete(Barang barang);
    }

    public BarangAdapter(Context context, List<Barang> barangList, OnItemClickListener listener,
            MainActivity activity) {
        this.context = context;
        this.barangList = barangList;
        this.listener = listener;
        this.activity = activity;
    }

    @NonNull
    @Override
    public BarangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_barang, parent, false);
        return new BarangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarangViewHolder holder, int position) {
        Barang barang = barangList.get(position);
        holder.tvNamaBarang.setText(barang.getBarang());
        holder.tvStok.setText(String.valueOf(barang.getStok()));
        holder.tvHarga.setText(String.valueOf(barang.getHarga()));

        holder.itemView.setOnClickListener(v -> listener.onItemClick(barang));
        holder.btnEdit.setOnClickListener(v -> listener.onItemEdit(barang));
        holder.btnDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Konfirmasi");
            builder.setMessage("Apakah Anda yakin ingin menghapus data ini?");

            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activity.deleteBarang(barang.getIdbarang());
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return barangList.size();
    }

    public void updateData(List<Barang> newList) {
        barangList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    public static class BarangViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaBarang;
        TextView tvStok;
        TextView tvHarga;
        ImageButton btnEdit;
        ImageButton btnDelete;

        public BarangViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaBarang = itemView.findViewById(R.id.tv_item_nama);
            tvStok = itemView.findViewById(R.id.tv_item_stok);
            tvHarga = itemView.findViewById(R.id.tv_item_harga);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
