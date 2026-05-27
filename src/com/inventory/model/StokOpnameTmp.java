package com.inventory.model;

/**
 *
 * @author Dearclaudia
 */
public class StokOpnameTmp {

    private int opnameTmpID;
    private Barang barang;
    private int stokSistem;
    private int stokFisik;
    private int selisih;
    private String catatan;

    public int getOpnameTmpID() {
        return opnameTmpID;
    }

    public void setOpnameTmpID(int opnameTmpID) {
        this.opnameTmpID = opnameTmpID;
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
