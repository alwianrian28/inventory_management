package com.inventory.service.impl;

import com.inventory.dao.BarangKeluarDetailDAO;
import com.inventory.model.BarangKeluarDetail;
import com.inventory.service.BarangKeluarDetailService;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class BarangKeluarDetailServiceImpl implements BarangKeluarDetailService {

    private final BarangKeluarDetailDAO detailDAO = new BarangKeluarDetailDAO();

    @Override
    public boolean insertData(BarangKeluarDetail detail) {

        if (detail == null) 
            return false;

        if (detail.getBarangKeluar() == null 
                || detail.getBarangKeluar().getBarangKeluarID() <= 0)
            return false;

        if (detail.getBarang()== null 
                || detail.getBarang().getBarangID() <= 0)
            return false;

        if (detail.getJumlah() <= 0)
            return false;

        if (detail.getHargaJual()< 0)
            return false;

        detail.setSubtotal(detail.getJumlah() * detail.getHargaJual());

        detailDAO.insertData(detail);
        return true;
    }

    @Override
    public List<BarangKeluarDetail> getDataById(int barangKeluarID,
                                              int itemsPerPage, int currentPage) {

        if (barangKeluarID <= 0)
            return List.of();

        return detailDAO.getDataById(barangKeluarID, itemsPerPage, currentPage);
    }

    @Override
    public List<BarangKeluarDetail> searchDataById(int barangKeluarID, String keyword,
                                                 int itemsPerPage,
                                                 int currentPage) {

        if (barangKeluarID <= 0)
            return List.of();

        if (keyword == null)
            keyword = "";

        return detailDAO.searchDataById(
                barangKeluarID,
                keyword.trim(),
                itemsPerPage,
                currentPage
        );
    }

    @Override
    public int getTotalItems(int barangKeluarID, String keyword) {

        if (barangKeluarID <= 0) {
            return 0;
        }
        
        if (keyword == null)
            keyword = "";

        return detailDAO.getTotalItemsById(barangKeluarID, keyword);
    }
}
