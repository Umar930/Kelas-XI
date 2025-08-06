package com.umar.sqlitedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Nama Database
    private static final String DATABASE_NAME = "sekolah.db";
    // Versi Database
    private static final int DATABASE_VERSION = 1;

    // Nama Tabel
    public static final String TABLE_NAME = "siswa";

    // Nama-nama Kolom
    public static final String COL_1_ID = "id";
    public static final String COL_2_NAMA = "nama";
    public static final String COL_3_ALAMAT = "alamat";

    // Constructor
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Membuat tabel siswa
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_1_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2_NAMA + " TEXT, " +
                COL_3_ALAMAT + " TEXT" +
                ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Menghapus tabel yang ada jika versi database diupgrade
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Membuat tabel baru
        onCreate(db);
    }

    // Method untuk menambah data siswa baru
    public long insertData(String nama, String alamat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_NAMA, nama);
        contentValues.put(COL_3_ALAMAT, alamat);
        return db.insert(TABLE_NAME, null, contentValues);
    }

    // Method untuk mengambil semua data siswa
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    // Method untuk memperbarui data siswa
    public boolean updateData(String id, String nama, String alamat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_ID, id);
        contentValues.put(COL_2_NAMA, nama);
        contentValues.put(COL_3_ALAMAT, alamat);
        db.update(TABLE_NAME, contentValues, "id = ?", new String[] { id });
        return true;
    }

    // Method untuk menghapus data siswa
    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "id = ?", new String[] { id });
    }
}
