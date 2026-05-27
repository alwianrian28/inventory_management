package com.inventory.controller;

import com.inventory.model.Gudang;
import com.inventory.service.GudangService;
import com.inventory.service.impl.GudangServiceImpl;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class GudangController {

    private final GudangService gudangService = new GudangServiceImpl();

    public boolean insertData(Gudang gudang) {
        return gudangService.insertData(gudang);
    }

    public boolean updateData(Gudang gudang) {
        return gudangService.updateData(gudang);
    }

    public boolean deleteData(Gudang gudang) {
        return gudangService.deleteData(gudang);
    }

    public boolean restoreData(int gudangID, String gudangCode, String gudangName) {
        return gudangService.restoreData(gudangID, gudangCode, gudangName);
    }

    public List<Gudang> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return gudangService.getData(isDelete, itemsPerPage, currentPage);
    }

    public List<Gudang> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        return gudangService.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    public int getTotalItems(boolean isDelete, String keyword) {
        return gudangService.getTotalItems(isDelete, keyword);
    }

    public String generateGudangCode() {
        return gudangService.generateGudangCode();
    }
}
