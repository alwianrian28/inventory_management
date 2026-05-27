package com.inventory.controller;

import com.inventory.model.Barang;
import com.inventory.service.BarangService;
import com.inventory.service.impl.BarangServiceImpl;

import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class BarangController {

    private final BarangService barangService = new BarangServiceImpl();

    public boolean insertData(Barang barang) {
        return barangService.insertData(barang);
    }

    public boolean updateData(Barang barang) {
        return barangService.updateData(barang);
    }

    public boolean deleteData(Barang barang) {
        return barangService.deleteData(barang);
    }

    public boolean restoreData(int barangID, String barangCode, String barcode) {
        return barangService.restoreData(barangID, barangCode, barcode);
    }

    public Barang getDataById(int barangID) {
        return barangService.getDataById(barangID);
    }

    public Barang getDataByCode(String barangCode) {
        return barangService.getDataByCode(barangCode);
    }

    public Barang getDataByBarcode(String barcode) {
        return barangService.getDataByBarcode(barcode);
    }

    public List<Barang> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return barangService.getData(isDelete, itemsPerPage, currentPage);
    }

    public List<Barang> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        return barangService.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    public int getTotalItems(boolean isDelete, String keyword) {
        return barangService.getTotalItems(isDelete, keyword);
    }

    public String generateBarangCode() {
        return barangService.generateBarangCode();
    }
    
    public List<Barang> getDataBySupplier(String supplierName, boolean isDelete, int itemsPerPage, int currentPage) {
        return barangService.getDataBySupplier(supplierName, isDelete, itemsPerPage, currentPage);
    }
    
    public List<Barang> searchDataBySupplier(String supplierName, String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        return barangService.searchDataBySupplier(supplierName, keyword, isDelete, itemsPerPage, currentPage);
    }
}
