package com.inventory.service;

import com.inventory.model.Kategori;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public interface KategoriService {

    boolean insertData(Kategori kategori);
    boolean updateData(Kategori kategori);
    boolean deleteData(Kategori kategori);
    boolean restoreData(int kategoriID, String kategoriName);
    
    List<Kategori> getData(boolean isDelete, int itemsPerPage, int currentPage);
    List<Kategori> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage);
    
    int getTotalItems(boolean isDelete, String keyword);
}
