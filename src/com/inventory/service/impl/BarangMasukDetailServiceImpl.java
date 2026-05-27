package com.inventory.service.impl;

import com.inventory.dao.BarangMasukDetailDAO;
import com.inventory.model.BarangMasukDetail;
import com.inventory.service.BarangMasukDetailService;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class BarangMasukDetailServiceImpl implements BarangMasukDetailService {

    private final BarangMasukDetailDAO detailDAO = new BarangMasukDetailDAO();

    @Override
    public boolean insertData(BarangMasukDetail detail) {

        if (detail == null) 
            return false;

        if (detail.getBarangMasuk() == null 
                || detail.getBarangMasuk().getBarangMasukID() <= 0)
            return false;

        if (detail.getStokGudang()== null 
                || detail.getStokGudang().getStokID() <= 0)
            return false;

        if (detail.getJumlah() <= 0)
            return false;

        if (detail.getHargaBeli() < 0)
            return false;

        if (detail.getRak() == null 
                || detail.getRak().getRakID() <= 0)
            return false;

        if (detail.getGudang() == null 
                || detail.getGudang().getGudangID() <= 0)
            return false;

        detail.setSubtotal(detail.getJumlah() * detail.getHargaBeli());

        detailDAO.insertData(detail);
        return true;
    }

    @Override
    public List<BarangMasukDetail> getDataById(int barangMasukID,
                                              int itemsPerPage, int currentPage) {

        if (barangMasukID <= 0)
            return List.of();

        return detailDAO.getDataById(barangMasukID, itemsPerPage, currentPage);
    }

    @Override
    public List<BarangMasukDetail> searchDataById(int barangMasukID, String keyword,
                                                 int itemsPerPage,
                                                 int currentPage) {

        if (barangMasukID <= 0)
            return List.of();

        if (keyword == null)
            keyword = "";

        return detailDAO.searchDataById(
                barangMasukID,
                keyword.trim(),
                itemsPerPage,
                currentPage
        );
    }

    @Override
    public int getTotalItems(int barangMasukID, String keyword) {

        if (barangMasukID <= 0) {
            return 0;
        }
        
        if (keyword == null)
            keyword = "";

        return detailDAO.getTotalItemsById(barangMasukID, keyword);
    }
}
