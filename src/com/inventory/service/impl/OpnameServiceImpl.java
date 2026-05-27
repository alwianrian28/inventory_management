package com.inventory.service.impl;

import com.inventory.dao.OpnameDAO;
import com.inventory.model.StokOpname;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.inventory.service.OpnameService;

/**
 *
 * @author Dearclaudia
 */
public class OpnameServiceImpl implements OpnameService {

    private final OpnameDAO opnameDAO = new OpnameDAO();

    @Override
    public boolean insertData(StokOpname model) {

        if (model == null) return false;

        if (model.getTanggalOpname() == null || model.getTanggalOpname().trim().isEmpty())
            return false;

        if (model.getStatus() == null || model.getStatus().trim().isEmpty())
            return false;

        if (model.getUser() == null || model.getUser().getUserID() <= 0)
            return false;

        if (model.getInsertBy() <= 0)
            return false;

        opnameDAO.insertData(model);
        return true;
    }

    @Override
    public boolean updateData(StokOpname model) {

        if (model == null || model.getOpnameID() <= 0)
            return false;

        if (model.getTanggalOpname() == null || model.getTanggalOpname().trim().isEmpty())
            return false;

        if (model.getStatus() == null || model.getStatus().trim().isEmpty())
            return false;

        if (model.getUser() == null || model.getUser().getUserID() <= 0)
            return false;

        if (model.getUpdateBy() <= 0)
            return false;

        opnameDAO.updateData(model);
        return true;
    }

    @Override
    public boolean deleteData(StokOpname model) {

        if (model == null || model.getOpnameID() <= 0)
            return false;

        if (model.getDeleteBy() <= 0)
            return false;

        opnameDAO.deleteData(model);
        return true;
    }

    @Override
    public boolean restoreData(int opnameID) {

        if (opnameID <= 0)
            return false;

        opnameDAO.restoreData(opnameID);
        return true;
    }

    @Override
    public List<StokOpname> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return opnameDAO.getData(isDelete, itemsPerPage, currentPage);
    }

    @Override
    public List<StokOpname> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        return opnameDAO.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    @Override
    public boolean applyStockAdjustment(int opnameID) {

        if (opnameID <= 0)
            return false;

        opnameDAO.applyStockAdjustment(opnameID);
        return true;
    }

    @Override
    public boolean adjustStock(int barangID, int selisih) {

        if (barangID <= 0)
            return false;

        if (selisih == 0)
            return false;

        opnameDAO.adjustStock(barangID, selisih);
        return true;
    }

    @Override
    public boolean rollbackStockOpname(int opnameID) {

        if (opnameID <= 0)
            return false;

        opnameDAO.rollbackStokOpname(opnameID);
        return true;
    }

    @Override
    public int getTotalItems(boolean isDelete, String keyword) {
        return opnameDAO.getTotalItems(isDelete, keyword);
    }
}
