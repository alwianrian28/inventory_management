package com.inventory.controller;

import com.inventory.model.BarangMasuk;
import com.inventory.service.BarangMasukService;
import com.inventory.service.impl.BarangMasukServiceImpl;

import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class BarangMasukController {

    private final BarangMasukService barangMasukService = new BarangMasukServiceImpl();

    public boolean insertData(BarangMasuk masuk) {
        return barangMasukService.insertData(masuk);
    }

    public boolean deleteData(BarangMasuk masuk) {
        return barangMasukService.deleteData(masuk);
    }

    public boolean restoreData(int barangMasukID) {
        return barangMasukService.restoreData(barangMasukID);
    }

    public List<BarangMasuk> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return barangMasukService.getData(isDelete, itemsPerPage, currentPage);
    }

    public List<BarangMasuk> searchData(String keyword, boolean isDelete,
                                        int itemsPerPage, int currentPage) {
        return barangMasukService.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    public int getTotalItems(boolean isDelete, String keyword) {
        return barangMasukService.getTotalItems(isDelete, keyword);
    }

    public String generateBarangMasukCode() {
        return barangMasukService.generateBarangMasukCode();
    }
}
