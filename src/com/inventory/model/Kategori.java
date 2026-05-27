package com.inventory.model;

/**
 *
 * @author Dearclaudia
 */
public class Kategori {

    private int kategoriID;
    private String kategoriName;
    private String keterangan;
    private int insertBy;
    private int updateBy;
    private int deleteBy;    

    public Kategori() {
    }

    public Kategori(int kategoriID, String kategoriName) {
        this.kategoriID = kategoriID;
        this.kategoriName = kategoriName;
    }
    
    public int getKategoriID() {
        return kategoriID;
    }

    public void setKategoriID(int kategoriID) {
        this.kategoriID = kategoriID;
    }

    public String getKategoriName() {
        return kategoriName;
    }

    public void setKategoriName(String kategoriName) {
        this.kategoriName = kategoriName;
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
        return (kategoriName == null) ? "Pilih Kategori" : kategoriName;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Kategori other = (Kategori) obj;
        return kategoriID == other.kategoriID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(kategoriID);
    }
}
