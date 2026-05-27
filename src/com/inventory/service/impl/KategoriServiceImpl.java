package com.inventory.service.impl;

import com.inventory.dao.KategoriDAO;
import com.inventory.model.Kategori;
import com.inventory.service.KategoriService;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class KategoriServiceImpl implements KategoriService{

    private final KategoriDAO kategoriDAO = new KategoriDAO();

    @Override
    public boolean insertData(Kategori kategori) {
        if (kategori.getKategoriName() == null || kategori.getKategoriName().isEmpty()) {
            return false;
        }

        if (kategoriDAO.iskategoriNameExists(kategori.getKategoriName())) {
            return false;
        }

        kategoriDAO.insertData(kategori);
        return true;
    }

    @Override
    public boolean updateData(Kategori kategori) {
        if (kategori.getKategoriID() <= 0) {
            return false;
        }

        if (kategori.getKategoriName() == null || kategori.getKategoriName().isEmpty()) {
            return false;
        }
        
        Kategori old = kategoriDAO.getDataById(kategori.getKategoriID());
        if (old == null) return false;

        if (!old.getKategoriName().equalsIgnoreCase(kategori.getKategoriName())
                && kategoriDAO.iskategoriNameExists(kategori.getKategoriName())) {
            return false;
        }

        kategoriDAO.updateData(kategori);
        return true;
    }

    @Override
    public boolean deleteData(Kategori kategori) {
        if (kategori.getKategoriID() <= 0) {
            return false;
        }

        kategoriDAO.deleteData(kategori);
        return true;
    }

    @Override
    public boolean restoreData(int kategoriID, String kategoriName) {
        if (kategoriID <= 0) {
            return false;
        }

        if (kategoriDAO.iskategoriNameExists(kategoriName)) {
            return false;
        }
        
        kategoriDAO.restoreData(kategoriID);
        return true;
    }

    @Override
    public List<Kategori> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return kategoriDAO.getData(isDelete, itemsPerPage, currentPage);
    }

    @Override
    public List<Kategori> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        return kategoriDAO.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    @Override
    public int getTotalItems(boolean isDelete, String keyword) {
        return kategoriDAO.getTotalItems(isDelete, keyword);
    }
}
