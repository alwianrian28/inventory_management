package com.inventory.model;

/**
 *
 * @author Dearclaudia
 */
public class BarangMasuk {

    private int barangMasukID;
    private String noTransaksi;
    private String noNota;
    private String tanggalMasuk;
    private int totalJumlah;
    private double totalMasuk;
    
    private Supplier supplier;
    private User user;
    
    private int insertBy;
    private int updateBy;
    private int deleteBy;

    public int getBarangMasukID() {
        return barangMasukID;
    }

    public void setBarangMasukID(int barangMasukID) {
        this.barangMasukID = barangMasukID;
    }

    public String getNoTransaksi() {
        return noTransaksi;
    }

    public void setNoTransaksi(String noTransaksi) {
        this.noTransaksi = noTransaksi;
    }

    public String getNoNota() {
        return noNota;
    }

    public void setNoNota(String noNota) {
        this.noNota = noNota;
    }

    public String getTanggalMasuk() {
        return tanggalMasuk;
    }

    public void setTanggalMasuk(String tanggalMasuk) {
        this.tanggalMasuk = tanggalMasuk;
    }

    public int getTotalJumlah() {
        return totalJumlah;
    }

    public void setTotalJumlah(int totalJumlah) {
        this.totalJumlah = totalJumlah;
    }

    public double getTotalMasuk() {
        return totalMasuk;
    }

    public void setTotalMasuk(double totalMasuk) {
        this.totalMasuk = totalMasuk;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    

}
