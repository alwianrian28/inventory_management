package com.inventory.controller;

import com.inventory.model.Rak;
import com.inventory.service.RakService;
import com.inventory.service.impl.RakServiceImpl;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class RakController {

    private final RakService rakService = new RakServiceImpl();

    public boolean insertData(Rak rak) {
        return rakService.insertData(rak);
    }

    public boolean updateData(Rak rak) {
        return rakService.updateData(rak);
    }

    public boolean deleteData(Rak rak) {
        return rakService.deleteData(rak);
    }

    public boolean restoreData(int rakID, String rakCode, String rakName) {
        return rakService.restoreData(rakID, rakCode, rakName);
    }

    public List<Rak> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return rakService.getData(isDelete, itemsPerPage, currentPage);
    }

    public List<Rak> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        return rakService.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    public int getTotalItems(boolean isDelete, String keyword) {
        return rakService.getTotalItems(isDelete, keyword);
    }
    
    public String generateRakCode() {
        return rakService.generateRakCode();
    }
}
