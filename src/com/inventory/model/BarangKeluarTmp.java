package com.inventory.model;

/**
 *
 * @author Dearclaudia
 */
public class BarangKeluarTmp {

    private int barangKeluarTmpID;
    private Barang barang;
    
    private int jumlah;
    private double hargaJual;
    private double subtotal;

    public int getBarangKeluarTmpID() {
        return barangKeluarTmpID;
    }

    public void setBarangKeluarTmpID(int barangKeluarTmpID) {
        this.barangKeluarTmpID = barangKeluarTmpID;
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

    public double getHargaJual() {
        return hargaJual;
    }

    public void setHargaJual(double hargaJual) {
        this.hargaJual = hargaJual;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
