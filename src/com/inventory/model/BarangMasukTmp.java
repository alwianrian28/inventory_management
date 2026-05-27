package com.inventory.model;

/**
 *
 * @author Dearclaudia
 */
public class BarangMasukTmp {

    private int barangMasukTmpID;
    private Barang barang;
    
    private int jumlah;
    private double hargaBeli;
    private double subtotal;
    
    private Rak rak;
    private Gudang gudang;

    public int getBarangMasukTmpID() {
        return barangMasukTmpID;
    }

    public void setBarangMasukTmpID(int barangMasukTmpID) {
        this.barangMasukTmpID = barangMasukTmpID;
    }

    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
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
