package com.umar.messagedialog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.umar.messagedialog.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("onCreate() dipanggil");
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart() dipanggil");
    }

    public void tampilkanToast(View view) {
        Toast.makeText(this, "Ini adalah pesan Toast!", Toast.LENGTH_SHORT).show();
    }

    public void tampilkanAlertDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Peringatan!");
        builder.setMessage("Ini adalah contoh Alert Dialog dasar.");
        builder.setCancelable(true);
        builder.show();
    }

    public void tampilkanAlertDialogTombol(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Hapus");
        builder.setMessage("Apakah Anda yakin ingin menghapus data ini?");

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Data sudah dihapus!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Data tidak dihapus!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy() dipanggil");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("onRestart() dipanggil");
    }
}