package com.inventory.controller;

import com.inventory.model.StokOpname;
import com.inventory.service.impl.OpnameServiceImpl;

import java.util.List;
import com.inventory.service.OpnameService;

/**
 *
 * @author Dearclaudia
 */
public class StokOpnameController {

    private final OpnameService opnameService = new OpnameServiceImpl();

    public boolean insertData(StokOpname model) {
        return opnameService.insertData(model);
    }

    public boolean updateData(StokOpname model) {
        return opnameService.updateData(model);
    }

    public boolean deleteData(StokOpname model) {
        return opnameService.deleteData(model);
    }

    public boolean restoreData(int opnameID) {
        return opnameService.restoreData(opnameID);
    }

    public List<StokOpname> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return opnameService.getData(isDelete, itemsPerPage, currentPage);
    }

    public List<StokOpname> searchData(String keyword, boolean isDelete,
                                       int itemsPerPage, int currentPage) {
        return opnameService.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    public boolean applyStockAdjustment(int opnameID) {
        return opnameService.applyStockAdjustment(opnameID);
    }

    public boolean rollbackStockOpname(int opnameID) {
        return opnameService.rollbackStockOpname(opnameID);
    }

    public int getTotalItems(boolean isDelete, String keyword) {
        return opnameService.getTotalItems(isDelete, keyword);
    }
}
