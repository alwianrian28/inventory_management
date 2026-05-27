package com.inventory.service.impl;

import com.inventory.dao.BarangMasukDAO;
import com.inventory.model.BarangMasuk;
import com.inventory.service.BarangMasukService;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class BarangMasukServiceImpl implements BarangMasukService {

    private final BarangMasukDAO barangMasukDAO = new BarangMasukDAO();

    @Override
    public boolean insertData(BarangMasuk masuk) {

        if (masuk == null) return false;

        if (masuk.getNoTransaksi()== null || masuk.getNoTransaksi().trim().isEmpty())
            return false;

        if (masuk.getNoNota() == null)
            return false;
        
        if (masuk.getTanggalMasuk() == null)
            return false;
        
        if (masuk.getTotalJumlah()< 0)
            return false;
        
        if (masuk.getTotalMasuk()< 0)
            return false;

        if (masuk.getSupplier() == null || masuk.getSupplier().getSupplierID() <= 0)
            return false;

        if (masuk.getInsertBy() <= 0)
            return false;

        barangMasukDAO.insertData(masuk);
        return true;
    }

    @Override
    public boolean deleteData(BarangMasuk masuk) {

        if (masuk == null || masuk.getBarangMasukID() <= 0)
            return false;

        barangMasukDAO.deleteData(masuk);
        return true;
    }

    @Override
    public boolean restoreData(int barangMasukID) {

        if (barangMasukID <= 0)
            return false;

        barangMasukDAO.restoreData(barangMasukID);
        return true;
    }

    @Override
    public List<BarangMasuk> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return barangMasukDAO.getData(isDelete, itemsPerPage, currentPage);
    }

    @Override
    public List<BarangMasuk> searchData(
            String keyword,
            boolean isDelete,
            int itemsPerPage,
            int currentPage
    ) {
        return barangMasukDAO.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    @Override
    public int getTotalItems(boolean isDelete, String keyword) {
        return barangMasukDAO.getTotalItems(isDelete, keyword);
    }

    @Override
    public String generateBarangMasukCode() {
        return barangMasukDAO.generateBarangMasukCode();
    }
}
