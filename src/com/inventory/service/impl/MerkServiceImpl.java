package com.inventory.service.impl;

import com.inventory.dao.MerkDAO;
import com.inventory.model.Merk;
import com.inventory.service.MerkService;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class MerkServiceImpl implements MerkService {

    private final MerkDAO merkDAO = new MerkDAO();

    @Override
    public boolean insertData(Merk merk) {
        if (merk.getMerkName() == null || merk.getMerkName().isEmpty()) {
            return false;
        }

        if (merkDAO.isMerkNameExists(merk.getMerkName())) {
            return false;
        }

        merkDAO.insertData(merk);
        return true;
    }

    @Override
    public boolean updateData(Merk merk) {
        if (merk.getMerkID() <= 0) {
            return false;
        }

        if (merk.getMerkName() == null || merk.getMerkName().isEmpty()) {
            return false;
        }

        Merk old = merkDAO.getDataById(merk.getMerkID());
        if (old == null) return false;

        if (!old.getMerkName().equalsIgnoreCase(merk.getMerkName())
                && merkDAO.isMerkNameExists(merk.getMerkName())) {
            return false;
        }

        merkDAO.updateData(merk);
        return true;
    }

    @Override
    public boolean deleteData(Merk merk) {
        if (merk.getMerkID() <= 0) {
            return false;
        }

        merkDAO.deleteData(merk);
        return true;
    }

    @Override
    public boolean restoreData(int merkID, String merkName) {
        if (merkID <= 0) {
            return false;
        }
        
        if (merkDAO.isMerkNameExists(merkName)) {
            return false;
        }

        merkDAO.restoreData(merkID);
        return true;
    }

    @Override
    public List<Merk> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return merkDAO.getData(isDelete, itemsPerPage, currentPage);
    }

    @Override
    public List<Merk> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        return merkDAO.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    @Override
    public int getTotalItems(boolean isDelete, String keyword) {
        return merkDAO.getTotalItems(isDelete, keyword);
    }
}
