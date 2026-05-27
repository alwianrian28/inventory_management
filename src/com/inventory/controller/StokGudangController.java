package com.inventory.controller;

import com.inventory.model.BarangMasuk;
import com.inventory.model.StokGudang;
import com.inventory.service.StokGudangService;
import com.inventory.service.impl.StokGudangServiceImpl;
import java.util.List;

public class StokGudangController {

    private final StokGudangService stokGudangService = new StokGudangServiceImpl();

    public boolean insertData(StokGudang stok) {
        return stokGudangService.insertData(stok);
    }

    public boolean deleteData(StokGudang stok) {
        return stokGudangService.deleteData(stok);
    }
    
    public boolean deleteData(BarangMasuk masuk) {
        return stokGudangService.deleteData(masuk);
    }

    public boolean restoreData(int barangMasukID) {
        return stokGudangService.restoreData(barangMasukID);
    }

    public List<StokGudang> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return stokGudangService.getData(isDelete, itemsPerPage, currentPage);
    }

    public List<StokGudang> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        return stokGudangService.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    public int getTotalItems(boolean isDelete, String keyword) {
        return stokGudangService.getTotalItems(isDelete, keyword);
    }
}
