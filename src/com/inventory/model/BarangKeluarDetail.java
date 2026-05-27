package com.inventory.model;

/**
 *
 * @author Dearclaudia
 */
public class BarangKeluarDetail {

    private int barangKeluarDetailID;
    private BarangKeluar barangKeluar;
    private Barang barang;
    
    private int jumlah;
    private double hargaJual;
    private double subtotal;

    public int getBarangKeluarDetailID() {
        return barangKeluarDetailID;
    }

    public void setBarangKeluarDetailID(int barangKeluarDetailID) {
        this.barangKeluarDetailID = barangKeluarDetailID;
    }

    public BarangKeluar getBarangKeluar() {
        return barangKeluar;
    }

    public void setBarangKeluar(BarangKeluar barangKeluar) {
        this.barangKeluar = barangKeluar;
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
