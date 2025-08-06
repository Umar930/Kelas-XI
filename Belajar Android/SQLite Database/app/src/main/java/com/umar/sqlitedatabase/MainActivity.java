package com.umar.sqlitedatabase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BarangAdapter.OnItemClickListener {

    private Database database;
    private RecyclerView rvBarang;
    private BarangAdapter adapter;
    private EditText etNamaBarang, etStok, etHarga;
    private Button btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new Database(this);

        etNamaBarang = findViewById(R.id.et_nama_barang);
        etStok = findViewById(R.id.et_stok);
        etHarga = findViewById(R.id.et_harga);
        btnSimpan = findViewById(R.id.btn_simpan);
        rvBarang = findViewById(R.id.rv_barang);

        rvBarang.setLayoutManager(new LinearLayoutManager(this));
        loadDataFromDatabase();

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = etNamaBarang.getText().toString().trim();
                String stokStr = etStok.getText().toString().trim();
                String hargaStr = etHarga.getText().toString().trim();

                if (nama.isEmpty() || stokStr.isEmpty() || hargaStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                    return;
                }

                int stok, harga;
                try {
                    stok = Integer.parseInt(stokStr);
                    harga = Integer.parseInt(hargaStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Stok dan Harga harus angka", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean inserted = database.insertBarang(nama, stok, harga);
                if (inserted) {
                    Toast.makeText(MainActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    clearInputFields();
                    loadDataFromDatabase();
                } else {
                    Toast.makeText(MainActivity.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadDataFromDatabase() {
        List<Barang> barangList = database.getAllBarang();
        if (adapter == null) {
            adapter = new BarangAdapter(this, barangList, this, this);
            rvBarang.setAdapter(adapter);
        } else {
            adapter.updateData(barangList);
        }
    }

    private void clearInputFields() {
        etNamaBarang.setText("");
        etStok.setText("");
        etHarga.setText("");
    }

    public void deleteBarang(int id) {
        boolean deleted = database.deleteBarang(id);
        if (deleted) {
            Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
            loadDataFromDatabase();
        } else {
            Toast.makeText(this, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(Barang barang) {
        // Implementasi klik item jika diperlukan
    }

    @Override
    public void onItemEdit(Barang barang) {
        showEditDialog(barang);
    }

    private void showEditDialog(Barang barang) {
        android.app.Dialog dialog = new android.app.Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_barang);

        EditText etNama = dialog.findViewById(R.id.et_edit_nama);
        EditText etStok = dialog.findViewById(R.id.et_edit_stok);
        EditText etHarga = dialog.findViewById(R.id.et_edit_harga);
        Button btn_update = dialog.findViewById(R.id.btn_update);
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);

        etNama.setText(barang.getBarang());
        etStok.setText(String.valueOf(barang.getStok()));
        etHarga.setText(String.valueOf(barang.getHarga()));

        btn_update.setOnClickListener(v -> {
            String namaBaru = etNama.getText().toString().trim();
            String stokStr = etStok.getText().toString().trim();
            String hargaStr = etHarga.getText().toString().trim();

            if (namaBaru.isEmpty() || stokStr.isEmpty() || hargaStr.isEmpty()) {
                Toast.makeText(MainActivity.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            int stokBaru, hargaBaru;
            try {
                stokBaru = Integer.parseInt(stokStr);
                hargaBaru = Integer.parseInt(hargaStr);
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "Stok dan Harga harus angka", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean updated = database.updateBarang(barang.getIdbarang(), namaBaru, stokBaru, hargaBaru);
            if (updated) {
                Toast.makeText(MainActivity.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                loadDataFromDatabase();
                dialog.dismiss();
            } else {
                Toast.makeText(MainActivity.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
            }
        });

        btn_cancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public void onItemDelete(Barang barang) {
        deleteBarang(barang.getIdbarang());
    }
}
