package com.inventory.model;

/**
 *
 * @author Dearclaudia
 */
public class BarangKeluar {

    private int barangKeluarID;
    private String noTransaksi;
    private String tanggalKeluar;
    private String jenisKeluar;
    private String tujuan;
    private int totalJumlah;
    private double totalKeluar;
    private User user;
    
    private int insertBy;
    private int updateBy;
    private int deleteBy;

    public int getBarangKeluarID() {
        return barangKeluarID;
    }

    public void setBarangKeluarID(int barangKeluarID) {
        this.barangKeluarID = barangKeluarID;
    }

    public String getNoTransaksi() {
        return noTransaksi;
    }

    public void setNoTransaksi(String noTransaksi) {
        this.noTransaksi = noTransaksi;
    }

    public String getTanggalKeluar() {
        return tanggalKeluar;
    }

    public void setTanggalKeluar(String tanggalKeluar) {
        this.tanggalKeluar = tanggalKeluar;
    }

    public String getJenisKeluar() {
        return jenisKeluar;
    }

    public void setJenisKeluar(String jenisKeluar) {
        this.jenisKeluar = jenisKeluar;
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }

    public int getTotalJumlah() {
        return totalJumlah;
    }

    public void setTotalJumlah(int totalJumlah) {
        this.totalJumlah = totalJumlah;
    }

    public double getTotalKeluar() {
        return totalKeluar;
    }

    public void setTotalKeluar(double totalKeluar) {
        this.totalKeluar = totalKeluar;
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
