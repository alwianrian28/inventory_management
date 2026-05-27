package com.inventory.model;

/**
 *
 * @author Dearclaudia
 */
public class StokOpnameDetail {

    private int opnameDetailID;
    private StokOpname stokOpname;
    private Barang barang;
    private int stokSistem;
    private int stokFisik;
    private int selisih;
    private String catatan;

    public int getOpnameDetailID() {
        return opnameDetailID;
    }

    public void setOpnameDetailID(int opnameDetailID) {
        this.opnameDetailID = opnameDetailID;
    }

    public StokOpname getStokOpname() {
        return stokOpname;
    }

    public void setStokOpname(StokOpname stokOpname) {
        this.stokOpname = stokOpname;
    }

    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
    }

    public int getStokSistem() {
        return stokSistem;
    }

    public void setStokSistem(int stokSistem) {
        this.stokSistem = stokSistem;
    }

    public int getStokFisik() {
        return stokFisik;
    }

    public void setStokFisik(int stokFisik) {
        this.stokFisik = stokFisik;
    }

    public int getSelisih() {
        return selisih;
    }

    public void setSelisih(int selisih) {
        this.selisih = selisih;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }
}
