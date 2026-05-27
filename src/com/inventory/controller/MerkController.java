package com.inventory.controller;

import com.inventory.model.Merk;
import com.inventory.service.MerkService;
import com.inventory.service.impl.MerkServiceImpl;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class MerkController {

    private final MerkService merkService = new MerkServiceImpl();

    public boolean insertData(Merk merk) {
        return merkService.insertData(merk);
    }

    public boolean updateData(Merk merk) {
        return merkService.updateData(merk);
    }

    public boolean deleteData(Merk merk) {
        return merkService.deleteData(merk);
    }

    public boolean restoreData(int merkID, String merkName) {
        return merkService.restoreData(merkID,merkName);
    }

    public List<Merk> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return merkService.getData(isDelete, itemsPerPage, currentPage);
    }

    public List<Merk> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        return merkService.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    public int getTotalItems(boolean isDelete, String keyword) {
        return merkService.getTotalItems(isDelete, keyword);
    }
}
