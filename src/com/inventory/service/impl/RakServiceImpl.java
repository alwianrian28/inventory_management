package com.inventory.service.impl;

import com.inventory.dao.RakDAO;
import com.inventory.model.Rak;
import com.inventory.service.RakService;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class RakServiceImpl implements RakService {

    private final RakDAO rakDAO = new RakDAO();

    @Override
    public boolean insertData(Rak rak) {
        if (rak.getRakName() == null || rak.getRakName().isEmpty()) {
            return false;
        }

        if (rakDAO.isRakNameExists(rak.getRakName())) {
            return false;
        }

        rakDAO.insertData(rak);
        return true;
    }

    @Override
    public boolean updateData(Rak rak) {
        if (rak.getRakID() <= 0) {
            return false;
        }

        if (rak.getRakName() == null || rak.getRakName().isEmpty()) {
            return false;
        }

        Rak old = rakDAO.getDataById(rak.getRakID());
        if (old == null) {
            return false;
        }

        if (!old.getRakName().equalsIgnoreCase(rak.getRakName())
                && rakDAO.isRakNameExists(rak.getRakName())) {
            return false;
        }

        rakDAO.updateData(rak);
        return true;
    }

    @Override
    public boolean deleteData(Rak rak) {
        if (rak.getRakID() <= 0) {
            return false;
        }

        rakDAO.deleteData(rak);
        return true;
    }

    @Override
    public boolean restoreData(int rakID, String rakCode, String rakName) {
        if (rakID <= 0) {
            return false;
        }

        if (rakDAO.isRakCodeExists(rakCode)) {
            return false;
        }
        
        if (rakDAO.isRakNameExists(rakName)) {
            return false;
        }
        
        rakDAO.restoreData(rakID);
        return true;
    }

    @Override
    public List<Rak> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return rakDAO.getData(isDelete, itemsPerPage, currentPage);
    }

    @Override
    public List<Rak> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        return rakDAO.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    @Override
    public int getTotalItems(boolean isDelete, String keyword) {
        return rakDAO.getTotalItems(isDelete, keyword);
    }

    @Override
    public String generateRakCode() {
        return rakDAO.generateRakCode();
    }
}
