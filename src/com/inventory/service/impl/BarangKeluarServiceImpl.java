package com.inventory.service.impl;

import com.inventory.dao.BarangKeluarDAO;
import com.inventory.model.BarangKeluar;
import com.inventory.service.BarangKeluarService;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class BarangKeluarServiceImpl implements BarangKeluarService {

    private final BarangKeluarDAO barangKeluarDAO = new BarangKeluarDAO();

    @Override
    public boolean insertData(BarangKeluar keluar) {

        if (keluar == null) return false;

        if (keluar.getNoTransaksi()== null || keluar.getNoTransaksi().trim().isEmpty())
            return false;

        if (keluar.getTanggalKeluar() == null)
            return false;
        
        if (keluar.getTotalJumlah()< 0)
            return false;
        
        if (keluar.getTotalKeluar()< 0)
            return false;

        if (keluar.getInsertBy() <= 0)
            return false;

        barangKeluarDAO.insertData(keluar);
        return true;
    }

    @Override
    public boolean deleteData(BarangKeluar keluar) {

        if (keluar == null || keluar.getBarangKeluarID() <= 0)
            return false;

        barangKeluarDAO.deleteData(keluar);
        return true;
    }

    @Override
    public boolean restoreData(int barangKeluarID) {

        if (barangKeluarID <= 0)
            return false;

        barangKeluarDAO.restoreData(barangKeluarID);
        return true;
    }

    @Override
    public List<BarangKeluar> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return barangKeluarDAO.getData(isDelete, itemsPerPage, currentPage);
    }

    @Override
    public List<BarangKeluar> searchData(
            String keyword,
            boolean isDelete,
            int itemsPerPage,
            int currentPage
    ) {
        return barangKeluarDAO.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    @Override
    public int getTotalItems(boolean isDelete, String keyword) {
        return barangKeluarDAO.getTotalItems(isDelete, keyword);
    }

    @Override
    public String generateBarangKeluarCode() {
        return barangKeluarDAO.generateBarangKeluarCode();
    }

    @Override
    public void updateStok(int barangKeluarID) {
        barangKeluarDAO.updateStok(barangKeluarID);
    }

    @Override
    public void rollbackStok(int barangKeluarID) {
        barangKeluarDAO.rollbackStok(barangKeluarID);
    }
}
