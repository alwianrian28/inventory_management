package com.inventory.service.impl;

import com.inventory.dao.GudangDAO;
import com.inventory.model.Gudang;
import com.inventory.service.GudangService;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class GudangServiceImpl implements GudangService {

    private final GudangDAO gudangDAO = new GudangDAO();

    @Override
    public boolean insertData(Gudang gudang) {
        if (gudang.getGudangName() == null || gudang.getGudangName().isEmpty()) {
            return false;
        }

        if (gudang.getAlamat() == null || gudang.getAlamat().isEmpty()) {
            return false;
        }

        if (gudangDAO.isGudangNameExists(gudang.getGudangName())) {
            return false;
        }

        gudangDAO.insertData(gudang);
        return true;
    }

    @Override
    public boolean updateData(Gudang gudang) {
        if (gudang.getGudangID() <= 0) {
            return false;
        }

        if (gudang.getGudangName() == null || gudang.getGudangName().isEmpty()) {
            return false;
        }

        if (gudang.getAlamat() == null || gudang.getAlamat().isEmpty()) {
            return false;
        }

        Gudang old = gudangDAO.getDataById(gudang.getGudangID());
        if (old == null) {
            return false;
        }
        
        if (!old.getGudangName().equalsIgnoreCase(gudang.getGudangName())
                && gudangDAO.isGudangNameExists(gudang.getGudangName())) {
            return false;
        }

        gudangDAO.updateData(gudang);
        return true;
    }

    @Override
    public boolean deleteData(Gudang gudang) {
        if (gudang.getGudangID() <= 0) {
            return false;
        }

        gudangDAO.deleteData(gudang);
        return true;
    }

    @Override
    public boolean restoreData(int gudangID, String gudangCode, String gudangName) {
        if (gudangID <= 0) {
            return false;
        }

        if (gudangDAO.isGudangCodeExists(gudangCode)) {
            return false;
        }

        if (gudangDAO.isGudangNameExists(gudangName)) {
            return false;
        }
        
        gudangDAO.restoreData(gudangID);
        return true;
    }

    @Override
    public List<Gudang> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return gudangDAO.getData(isDelete, itemsPerPage, currentPage);
    }

    @Override
    public List<Gudang> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        return gudangDAO.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    @Override
    public int getTotalItems(boolean isDelete, String keyword) {
        return gudangDAO.getTotalItems(isDelete, keyword);
    }

    @Override
    public String generateGudangCode() {
        return gudangDAO.generateGudangCode();
    }
}
