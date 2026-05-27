package com.inventory.model;

/**
 *
 * @author Dearclaudia
 */
public class Merk {

    private int merkID;
    private String merkName;
    private String keterangan;
    private int insertBy;
    private int updateBy;
    private int deleteBy;

    public Merk() {
    }

    public Merk(int merkID, String merkName) {
        this.merkID = merkID;
        this.merkName = merkName;
    }

    public int getMerkID() {
        return merkID;
    }

    public void setMerkID(int merkID) {
        this.merkID = merkID;
    }

    public String getMerkName() {
        return merkName;
    }

    public void setMerkName(String merkName) {
        this.merkName = merkName;
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
        return (merkName == null) ? "Pilih Merk" : merkName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Merk other = (Merk) obj;
        return merkID == other.merkID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(merkID);
    }
}
