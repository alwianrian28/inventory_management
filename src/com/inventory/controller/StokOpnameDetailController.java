package com.inventory.controller;

import com.inventory.model.StokOpnameDetail;
import com.inventory.service.impl.OpnameDetailServiceImpl;

import java.util.List;
import com.inventory.service.OpnameDetailService;

/**
 *
 * @author Dearclaudia
 */
public class StokOpnameDetailController {

    private final OpnameDetailService detailService = new OpnameDetailServiceImpl();

    public boolean insertData(int opnameID) {
        return detailService.insertData(opnameID);
    }

    public boolean deleteData(int opnameID) {
        return detailService.deleteData(opnameID);
    }

    public List<StokOpnameDetail> getDataById(int opnameID, int itemsPerPage, int currentPage) {
        return detailService.getDataById(opnameID, itemsPerPage, currentPage);
    }

    public List<StokOpnameDetail> searchDataById(int opnameID, String keyword,
                                                 int itemsPerPage, int currentPage) {
        return detailService.searchDataById(opnameID, keyword, itemsPerPage, currentPage);
    }

    public int getTotalItems(int opnameID, String keyword) {
        return detailService.getTotalItems(opnameID, keyword);
    }
}
