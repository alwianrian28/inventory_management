package com.inventory.model;

/**
 *
 * @author Dearclaudia
 */
public class StokGudang {

    private int stokID;
    private Barang barang;
    
    private int jumlah;
    private double hargaBeli;
    private double subtotal;
    
    private Rak rak;
    private Gudang gudang;

    private int insertBy;
    private int updateBy;
    private int deleteBy;

    public int getStokID() {
        return stokID;
    }

    public void setStokID(int stokID) {
        this.stokID = stokID;
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

    public int getInsertBy() {
        return insertBy;
    }

    public void setInsertBy(int insertBy) {
        this.insertBy = insertBy;
    }

    public int getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(int updateBy) {
        this.updateBy = updateBy;
    }

    public int getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(int deleteBy) {
        this.deleteBy = deleteBy;
    }
    
    @Override
    public String toString() {
        return barang != null
            ? barang.getBarangName() + " (" + jumlah + ")"
            : "Stok Gudang";
    }

}
