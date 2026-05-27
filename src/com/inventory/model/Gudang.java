package com.inventory.model;

/**
 *
 * @author Dearclaudia
 */
public class Gudang {

    private int gudangID;
    private String gudangCode;
    private String gudangName;
    private String alamat;
    private String keterangan;
    private int insertBy;
    private int updateBy;
    private int deleteBy;

    public Gudang() {
    }

    public Gudang(int gudangID, String gudangName) {
        this.gudangID = gudangID;
        this.gudangName = gudangName;
    }

    public int getGudangID() {
        return gudangID;
    }

    public void setGudangID(int gudangID) {
        this.gudangID = gudangID;
    }

    public String getGudangCode() {
        return gudangCode;
    }

    public void setGudangCode(String gudangCode) {
        this.gudangCode = gudangCode;
    }

    public String getGudangName() {
        return gudangName;
    }

    public void setGudangName(String gudangName) {
        this.gudangName = gudangName;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
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
        return (gudangName == null) ? "Pilih Gudang" : gudangName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Gudang other = (Gudang) obj;
        return gudangID == other.gudangID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(gudangID);
    }
}
