package com.umar.recyclerviewcardview;

public class Siswa {
    private String nama;
    private String alamat;
    private int fotoId;

    public Siswa(String nama, String alamat, int fotoId) {
        this.nama = nama;
        this.alamat = alamat;
        this.fotoId = fotoId;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public int getFotoId() {
        return fotoId;
    }

    public void setFotoId(int fotoId) {
        this.fotoId = fotoId;
    }
}
