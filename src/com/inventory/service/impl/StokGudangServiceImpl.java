package com.inventory.service.impl;

import com.inventory.dao.StokGudangDAO;
import com.inventory.model.BarangMasuk;
import com.inventory.model.StokGudang;
import com.inventory.service.StokGudangService;
import java.util.List;

public class StokGudangServiceImpl implements StokGudangService {

    private final StokGudangDAO stokGudangDAO = new StokGudangDAO();

    @Override
    public boolean insertData(StokGudang stok) {
        if (stok == null) return false;

        if (stok.getBarang() == null || stok.getBarang().getBarangID() <= 0) return false;
        if (stok.getRak() == null || stok.getRak().getRakID() <= 0) return false;
        if (stok.getGudang() == null || stok.getGudang().getGudangID() <= 0) return false;

        if (stok.getJumlah() <= 0) return false;
        if (stok.getHargaBeli() < 0) return false;

        stokGudangDAO.insertData(stok);
        return true;
    }

    @Override
    public boolean deleteData(StokGudang stok) {
        if (stok == null || stok.getStokID() <= 0) return false;

        stokGudangDAO.deleteData(stok);
        return true;
    }
    
    @Override
    public boolean deleteData(BarangMasuk masuk) {
        if (masuk == null || masuk.getBarangMasukID() <= 0) return false;

        stokGudangDAO.deleteData(masuk);
        return true;
    }

    @Override
    public boolean restoreData(int barangMasukID) {
        if (barangMasukID <= 0) return false;

        stokGudangDAO.restoreData(barangMasukID);
        return true;
    }

    @Override
    public List<StokGudang> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return stokGudangDAO.getData(isDelete, itemsPerPage, currentPage);
    }

    @Override
    public List<StokGudang> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        return stokGudangDAO.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    @Override
    public int getTotalItems(boolean isDelete, String keyword) {
        return stokGudangDAO.getTotalItems(isDelete, keyword);
    }
}
