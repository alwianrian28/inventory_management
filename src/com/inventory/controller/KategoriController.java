package com.inventory.controller;

import com.inventory.model.Kategori;
import com.inventory.service.KategoriService;
import com.inventory.service.impl.KategoriServiceImpl;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class KategoriController {
    
    private final KategoriService kategoriService = new KategoriServiceImpl();
    
    public boolean insertData(Kategori kategori) {
        return kategoriService.insertData(kategori);
    }

    public boolean updateData(Kategori kategori) {
        return kategoriService.updateData(kategori);
    }

    public boolean deleteData(Kategori kategori) {
        return kategoriService.deleteData(kategori);
    }

    public boolean restoreData(int kategoriID, String kategoriName) {
        return kategoriService.restoreData(kategoriID, kategoriName);
    }

    public List<Kategori> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return kategoriService.getData(isDelete, itemsPerPage, currentPage);
    }

    public List<Kategori> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        return kategoriService.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    public int getTotalItems(boolean isDelete, String keyword) {
        return kategoriService.getTotalItems(isDelete, keyword);
    }
}
