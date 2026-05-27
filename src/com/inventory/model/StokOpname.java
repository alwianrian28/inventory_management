package com.inventory.model;

/**
 *
 * @author Dearclaudia
 */
public class StokOpname {

    private int opnameID;
    private String tanggalOpname;
    private String keterangan;
    private String status;
    private User user;
    private int insertBy;
    private int updateBy;
    private int deleteBy;

    public int getOpnameID() {
        return opnameID;
    }

    public void setOpnameID(int opnameID) {
        this.opnameID = opnameID;
    }

    public String getTanggalOpname() {
        return tanggalOpname;
    }

    public void setTanggalOpname(String tanggalOpname) {
        this.tanggalOpname = tanggalOpname;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
