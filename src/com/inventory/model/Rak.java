package com.inventory.model;

/**
 *
 * @author Dearclaudia
 */
public class Rak {

    private int rakID;
    private String rakCode;
    private String rakName;
    private String keterangan;
    private int insertBy;
    private int updateBy;
    private int deleteBy;

    public Rak() {
    }

    public Rak(int rakID, String rakName) {
        this.rakID = rakID;
        this.rakName = rakName;
    }

    public int getRakID() {
        return rakID;
    }

    public void setRakID(int rakID) {
        this.rakID = rakID;
    }

    public String getRakCode() {
        return rakCode;
    }

    public void setRakCode(String rakCode) {
        this.rakCode = rakCode;
    }
    
    public String getRakName() {
        return rakName;
    }

    public void setRakName(String rakName) {
        this.rakName = rakName;
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
        return (rakName == null) ? "Pilih Rak" : rakName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Rak other = (Rak) obj;
        return rakID == other.rakID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(rakID);
    }
}
