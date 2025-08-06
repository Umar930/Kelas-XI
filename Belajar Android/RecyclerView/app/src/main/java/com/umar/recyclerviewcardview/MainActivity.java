package com.umar.recyclerviewcardview;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SiswaAdapter adapter;
    private List<Siswa> siswaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        load();
    }

    private void load() {
        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerViewDaftar);

        // Inisialisasi Button
        findViewById(R.id.btnTambahData).setOnClickListener(view -> tambahDataBaru());

        // Mengatur LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Mengisi data
        isiData();

        // Membuat dan mengatur adapter
        adapter = new SiswaAdapter(this, siswaList);
        recyclerView.setAdapter(adapter);
    }

    private void isiData() {
        siswaList = new ArrayList<>();

        // Menambahkan data siswa
        siswaList.add(new Siswa("Joni Susanto", "Jl. Mawar No. 1, Surabaya", R.drawable.ic_launcher_foreground));
        siswaList.add(new Siswa("Eko Prasetyo", "Jl. Melati No. 2, Malang", R.drawable.ic_launcher_foreground));
        siswaList.add(new Siswa("Tejo Kusumo", "Jl. Anggrek No. 3, Kediri", R.drawable.ic_launcher_foreground));
        siswaList.add(new Siswa("Siti Aminah", "Jl. Dahlia No. 4, Bandung", R.drawable.ic_launcher_foreground));
        siswaList.add(new Siswa("Roni Wijaya", "Jl. Kenanga No. 5, Jakarta", R.drawable.ic_launcher_foreground));
        siswaList.add(new Siswa("Budi Santoso", "Jl. Tulip No. 6, Semarang", R.drawable.ic_launcher_foreground));
        siswaList.add(new Siswa("Ani Wulandari", "Jl. Kamboja No. 7, Yogyakarta", R.drawable.ic_launcher_foreground));
    }

    private void tambahDataBaru() {
        // Membuat data siswa baru
        int nomorBaru = siswaList.size() + 1;
        Siswa siswaBaru = new Siswa(
                "Siswa Baru " + nomorBaru,
                "Alamat Baru " + nomorBaru,
                R.drawable.ic_launcher_foreground);

        // Menambahkan ke list
        siswaList.add(siswaBaru);

        // Memberitahu adapter bahwa ada item baru
        adapter.notifyItemInserted(siswaList.size() - 1);

        // Scroll ke posisi item baru
        recyclerView.smoothScrollToPosition(siswaList.size() - 1);
    }
}