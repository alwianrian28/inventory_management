package com.inventory.model;

/**
 *
 * @author Dearclaudia
 */
public class Satuan {

    private int satuanID;
    private String satuanName;
    private String keterangan;
    private int insertBy;
    private int updateBy;
    private int deleteBy;

    public Satuan() {
    }

    public Satuan(int satuanID, String satuanName) {
        this.satuanID = satuanID;
        this.satuanName = satuanName;
    }

    public int getSatuanID() {
        return satuanID;
    }

    public void setSatuanID(int satuanID) {
        this.satuanID = satuanID;
    }

    public String getSatuanName() {
        return satuanName;
    }

    public void setSatuanName(String satuanName) {
        this.satuanName = satuanName;
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
        return (satuanName == null) ? "Pilih Satuan" : satuanName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Satuan other = (Satuan) obj;
        return satuanID == other.satuanID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(satuanID);
    }
}
