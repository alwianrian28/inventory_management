package com.inventory.service.impl;

import com.inventory.dao.BarangDAO;
import com.inventory.model.Barang;
import com.inventory.service.BarangService;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class BarangServiceImpl implements BarangService {

    private final BarangDAO barangDAO = new BarangDAO();

    @Override
    public boolean insertData(Barang barang) {

        if (barang == null) return false;

        if (barang.getBarangCode() == null || barang.getBarangCode().trim().isEmpty())
            return false;

        if (barang.getBarangName() == null || barang.getBarangName().trim().isEmpty())
            return false;

        if (barangDAO.isBarangCodeExists(barang.getBarangCode()))
            return false;

        if (barang.getBarcode() != null && !barang.getBarcode().isEmpty()
                && barangDAO.isBarcodeExists(barang.getBarcode()))
            return false;

        if (barang.getMerk() == null || barang.getMerk().getMerkID() <= 0) return false;
        if (barang.getKategori() == null || barang.getKategori().getKategoriID() <= 0) return false;
        if (barang.getSatuan() == null || barang.getSatuan().getSatuanID() <= 0) return false;
        if (barang.getSupplier() == null || barang.getSupplier().getSupplierID() <= 0) return false;
        
        if (barang.getStokMinimum()< 0 || barang.getStokMinimum()< 0) return false;
        if (barang.getHargaJual()< 0 || barang.getHargaJual() < 0) return false;

        barangDAO.insertData(barang);
        return true;
    }

    @Override
    public boolean updateData(Barang barang) {

        if (barang == null || barang.getBarangID() <= 0)
            return false;

        if (barang.getBarangName() == null || barang.getBarangName().trim().isEmpty())
            return false;
        
        Barang old = barangDAO.getDataById(barang.getBarangID());
        if (old == null)
            return false;

        if (!old.getBarangCode().equals(barang.getBarangCode())
                && barangDAO.isBarangCodeExists(barang.getBarangCode()))
            return false;

        if (!old.getBarcode().equals(barang.getBarcode())
                && barangDAO.isBarcodeExists(barang.getBarcode()))
            return false;

        barangDAO.updateData(barang);
        return true;
    }

    @Override
    public boolean deleteData(Barang barang) {
        if (barang == null || barang.getBarangID() <= 0)
            return false;

        barangDAO.deleteData(barang);
        return true;
    }

    @Override
    public boolean restoreData(int barangID, String barangCode, String barcode) {
        if (barangID <= 0)
            return false;

        if (barangDAO.isBarangCodeExists(barangCode)) {
            return false;
        }

        if (barangDAO.isBarcodeExists(barcode)) {
            return false;
        }
        
        barangDAO.restoreData(barangID);
        return true;
    }

    @Override
    public List<Barang> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return barangDAO.getData(isDelete, itemsPerPage, currentPage);
    }

    @Override
    public List<Barang> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        return barangDAO.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    @Override
    public int getTotalItems(boolean isDelete, String keyword) {
        return barangDAO.getTotalItems(isDelete, keyword);
    }

    @Override
    public Barang getDataById(int barangID) {
        if (barangID <= 0) return null;
        return barangDAO.getDataById(barangID);
    }

    @Override
    public Barang getDataByCode(String barangCode) {
        if (barangCode == null || barangCode.trim().isEmpty()) return null;
        return barangDAO.getDataByCode(barangCode);
    }

    @Override
    public Barang getDataByBarcode(String barcode) {
        if (barcode == null || barcode.trim().isEmpty()) return null;
        return barangDAO.getDataByBarcode(barcode);
    }

    @Override
    public String generateBarangCode() {
        return barangDAO.generateBarangCode();
    }

    @Override
    public List<Barang> getDataBySupplier(String supplierName, boolean isDelete, int itemsPerPage, int currentPage) {
        return barangDAO.getDataBySupplier(supplierName, isDelete, itemsPerPage, currentPage);
    }

    @Override
    public List<Barang> searchDataBySupplier(String supplierName, String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        return barangDAO.searchDataBySupplier(supplierName, keyword, isDelete, itemsPerPage, currentPage);
    }
}
