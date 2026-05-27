package com.inventory.controller;

import com.inventory.model.BarangKeluar;
import com.inventory.service.BarangKeluarService;
import com.inventory.service.impl.BarangKeluarServiceImpl;

import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class BarangKeluarController {

    private final BarangKeluarService barangKeluarService = new BarangKeluarServiceImpl();

    public boolean insertData(BarangKeluar masuk) {
        return barangKeluarService.insertData(masuk);
    }

    public boolean deleteData(BarangKeluar masuk) {
        return barangKeluarService.deleteData(masuk);
    }

    public boolean restoreData(int barangKeluarID) {
        return barangKeluarService.restoreData(barangKeluarID);
    }

    public List<BarangKeluar> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return barangKeluarService.getData(isDelete, itemsPerPage, currentPage);
    }

    public List<BarangKeluar> searchData(String keyword, boolean isDelete,
                                        int itemsPerPage, int currentPage) {
        return barangKeluarService.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    public int getTotalItems(boolean isDelete, String keyword) {
        return barangKeluarService.getTotalItems(isDelete, keyword);
    }

    public String generateBarangKeluarCode() {
        return barangKeluarService.generateBarangKeluarCode();
    }
    
    public void updateStok(int barangKeluarID) {
        barangKeluarService.updateStok(barangKeluarID);
    }

    public void rollbackStok(int barangKeluarID) {
        barangKeluarService.rollbackStok(barangKeluarID);
    }
}
