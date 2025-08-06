package com.umar.recyclerviewcardview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SiswaAdapter extends RecyclerView.Adapter<SiswaAdapter.SiswaHolder> {
    private Context context;
    private List<Siswa> siswaList;

    public SiswaAdapter(Context context, List<Siswa> siswaList) {
        this.context = context;
        this.siswaList = siswaList;
    }

    @NonNull
    @Override
    public SiswaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new SiswaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SiswaHolder holder, int position) {
        Siswa siswa = siswaList.get(position);
        holder.tvNama.setText(siswa.getNama());
        holder.tvAlamat.setText(siswa.getAlamat());
        holder.imageViewFoto.setImageResource(siswa.getFotoId());

        // Set OnClickListener untuk menu
        holder.tvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.tvMenu);
                popupMenu.inflate(R.menu.menu_option);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.menu_edit) {
                            showEditDialog(siswa, holder.getAdapterPosition());
                            return true;
                        } else if (itemId == R.id.menu_simpan) {
                            Toast.makeText(context, "Menyimpan data: " + siswa.getNama(), Toast.LENGTH_SHORT).show();
                            return true;
                        } else if (itemId == R.id.menu_hapus) {
                            int clickedPosition = holder.getAdapterPosition();
                            if (clickedPosition != RecyclerView.NO_POSITION) {
                                siswaList.remove(clickedPosition);
                                notifyItemRemoved(clickedPosition);
                                Toast.makeText(context, "Data " + siswa.getNama() + " telah dihapus",
                                        Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return siswaList.size();
    }

    public static class SiswaHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvAlamat, tvMenu;
        ImageView imageViewFoto;

        public SiswaHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.textViewJudulItem);
            tvAlamat = itemView.findViewById(R.id.textViewDeskripsiItem);
            imageViewFoto = itemView.findViewById(R.id.imageViewItem);
            tvMenu = itemView.findViewById(R.id.tv_menu);
        }
    }

    private void showEditDialog(final Siswa siswa, final int position) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_siswa, null);

        EditText editTextNama = dialogView.findViewById(R.id.editTextNama);
        EditText editTextAlamat = dialogView.findViewById(R.id.editTextAlamat);

        // Set data yang sudah ada
        editTextNama.setText(siswa.getNama());
        editTextAlamat.setText(siswa.getAlamat());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Data Siswa")
                .setView(dialogView)
                .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String namaBaru = editTextNama.getText().toString().trim();
                        String alamatBaru = editTextAlamat.getText().toString().trim();

                        if (!namaBaru.isEmpty() && !alamatBaru.isEmpty()) {
                            // Update data siswa
                            siswa.setNama(namaBaru);
                            siswa.setAlamat(alamatBaru);

                            // Beritahu adapter untuk memperbarui tampilan
                            notifyItemChanged(position);

                            Toast.makeText(context, "Data berhasil diubah", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Nama dan alamat tidak boleh kosong", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Batal", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
