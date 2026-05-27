package com.inventory.controller;

import com.inventory.model.Satuan;
import com.inventory.service.SatuanService;
import com.inventory.service.impl.SatuanServiceImpl;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class SatuanController {

    private final SatuanService satuanService = new SatuanServiceImpl();

    public boolean insertData(Satuan satuan) {
        return satuanService.insertData(satuan);
    }

    public boolean updateData(Satuan satuan) {
        return satuanService.updateData(satuan);
    }

    public boolean deleteData(Satuan satuan) {
        return satuanService.deleteData(satuan);
    }

    public boolean restoreData(int satuanID, String satuanName) {
        return satuanService.restoreData(satuanID, satuanName);
    }

    public List<Satuan> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return satuanService.getData(isDelete, itemsPerPage, currentPage);
    }

    public List<Satuan> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        return satuanService.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    public int getTotalItems(boolean isDelete, String keyword) {
        return satuanService.getTotalItems(isDelete, keyword);
    }
}
