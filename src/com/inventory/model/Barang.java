package com.inventory.model;

import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;

/**
 *
 * @author Dearclaudia
 */
public class Barang {

    private int barangID;
    private String barangCode;
    private String barcode;
    private String barangName;
    private Merk merk;
    private Kategori kategori;
    private Satuan satuan;
    private double hargaJual;
    private int stokMinimum;
    private int totalStok;
    private Supplier supplier;
    private String photo;

    private int insertBy;
    private int updateBy;
    private int deleteBy;
    
    public int getBarangID() {
        return barangID;
    }

    public void setBarangID(int barangID) {
        this.barangID = barangID;
    }

    public String getBarangCode() {
        return barangCode;
    }

    public void setBarangCode(String barangCode) {
        this.barangCode = barangCode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBarangName() {
        return barangName;
    }

    public void setBarangName(String barangName) {
        this.barangName = barangName;
    }

    public Merk getMerk() {
        return merk;
    }

    public void setMerk(Merk merk) {
        this.merk = merk;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }

    public Satuan getSatuan() {
        return satuan;
    }

    public void setSatuan(Satuan satuan) {
        this.satuan = satuan;
    }

    public double getHargaJual() {
        return hargaJual;
    }

    public void setHargaJual(double hargaJual) {
        this.hargaJual = hargaJual;
    }

    public int getStokMinimum() {
        return stokMinimum;
    }

    public void setStokMinimum(int stokMinimum) {
        this.stokMinimum = stokMinimum;
    }
    
    public int getTotalStok() {
        return totalStok;
    }

    public void setTotalStok(int totalStok) {
        this.totalStok = totalStok;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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
    
    private transient ImageIcon photoThumbnail;

    public ImageIcon getPhotoThumbnail() {
        if (photoThumbnail == null && photo != null) {
            File file = new File(photo);
            if (file.exists()) {
                ImageIcon raw = new ImageIcon(photo);
                Image scaled = raw.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                photoThumbnail = new ImageIcon(scaled);
            }
        }
        return photoThumbnail;
    }

}
