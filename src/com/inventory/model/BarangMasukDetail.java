package com.inventory.model;

/**
 *
 * @author Dearclaudia
 */
public class BarangMasukDetail {

    private int barangMasukDetailID;
    private BarangMasuk barangMasuk;
    private StokGudang stokGudang;
    
    private int jumlah;
    private double hargaBeli;
    private double subtotal;
    
    private Rak rak;
    private Gudang gudang;

    public int getBarangMasukDetailID() {
        return barangMasukDetailID;
    }

    public void setBarangMasukDetailID(int barangMasukDetailID) {
        this.barangMasukDetailID = barangMasukDetailID;
    }

    public BarangMasuk getBarangMasuk() {
        return barangMasuk;
    }

    public void setBarangMasuk(BarangMasuk barangMasuk) {
        this.barangMasuk = barangMasuk;
    }

    public StokGudang getStokGudang() {
        return stokGudang;
    }

    public void setStokGudang(StokGudang stokGudang) {
        this.stokGudang = stokGudang;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public double getHargaBeli() {
        return hargaBeli;
    }

    public void setHargaBeli(double hargaBeli) {
        this.hargaBeli = hargaBeli;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Rak getRak() {
        return rak;
    }

    public void setRak(Rak rak) {
        this.rak = rak;
    }

    public Gudang getGudang() {
        return gudang;
    }

    public void setGudang(Gudang gudang) {
        this.gudang = gudang;
    }

    
}
