package com.inventory.service.impl;

import com.inventory.dao.SatuanDAO;
import com.inventory.model.Satuan;
import com.inventory.service.SatuanService;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class SatuanServiceImpl implements SatuanService {

    private final SatuanDAO satuanDAO = new SatuanDAO();

    @Override
    public boolean insertData(Satuan satuan) {
        if (satuan.getSatuanName() == null || satuan.getSatuanName().isEmpty()) {
            return false;
        }

        if (satuanDAO.isSatuanNameExists(satuan.getSatuanName())) {
            return false;
        }

        satuanDAO.insertData(satuan);
        return true;
    }

    @Override
    public boolean updateData(Satuan satuan) {
        if (satuan.getSatuanID() <= 0) {
            return false;
        }

        if (satuan.getSatuanName() == null || satuan.getSatuanName().isEmpty()) {
            return false;
        }

        Satuan old = satuanDAO.getDataById(satuan.getSatuanID());
        if (old == null) {
            return false;
        }

        if (!old.getSatuanName().equalsIgnoreCase(satuan.getSatuanName())
                && satuanDAO.isSatuanNameExists(satuan.getSatuanName())) {
            return false;
        }

        satuanDAO.updateData(satuan);
        return true;
    }

    @Override
    public boolean deleteData(Satuan satuan) {
        if (satuan.getSatuanID() <= 0) {
            return false;
        }

        satuanDAO.deleteData(satuan);
        return true;
    }

    @Override
    public boolean restoreData(int satuanID, String satuanName) {
        if (satuanID <= 0) {
            return false;
        }

        if (satuanDAO.isSatuanNameExists(satuanName)) {
            return false;
        }
        
        satuanDAO.restoreData(satuanID);
        return true;
    }

    @Override
    public List<Satuan> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return satuanDAO.getData(isDelete, itemsPerPage, currentPage);
    }

    @Override
    public List<Satuan> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        return satuanDAO.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    @Override
    public int getTotalItems(boolean isDelete, String keyword) {
        return satuanDAO.getTotalItems(isDelete, keyword);
    }
}
