package com.umar.sqlitedatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gudang.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE = "CREATE TABLE tblbarang(" +
            "idbarang INTEGER PRIMARY KEY AUTOINCREMENT," +
            "barang TEXT," +
            "stok INTEGER," +
            "harga INTEGER" +
            ")";

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SQL_CREATE_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS tblbarang");
            onCreate(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean runSQL(String sql) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean buatTabel() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String sql = "CREATE TABLE IF NOT EXISTS tblbarang(" +
                    "idbarang INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "barang TEXT," +
                    "stok INTEGER," +
                    "harga INTEGER" +
                    ")";
            return runSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertBarang(String nama, int stok, int harga) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String sql = "INSERT INTO tblbarang (barang, stok, harga) VALUES ('" +
                    nama + "', " +
                    stok + ", " +
                    harga + ")";
            db.execSQL(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Barang> getAllBarang() {
        List<Barang> barangList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tblbarang ORDER BY idbarang DESC", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String nama = cursor.getString(1);
                int stok = cursor.getInt(2);
                int harga = cursor.getInt(3);

                Barang barang = new Barang(id, nama, stok, harga);
                barangList.add(barang);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return barangList;
    }

    public boolean updateBarang(int id, String nama, int stok, int harga) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String sql = "UPDATE tblbarang SET " +
                    "barang='" + nama + "', " +
                    "stok=" + stok + ", " +
                    "harga=" + harga + " " +
                    "WHERE idbarang=" + id;
            db.execSQL(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBarang(int id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String sql = "DELETE FROM tblbarang WHERE idbarang=" + id;
            db.execSQL(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
